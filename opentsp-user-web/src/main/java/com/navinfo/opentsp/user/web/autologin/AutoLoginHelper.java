package com.navinfo.opentsp.user.web.autologin;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.service.cache.CacheService;
import com.navinfo.opentsp.user.service.device.DeviceType;
import com.navinfo.opentsp.user.service.param.login.LoginParam;
import com.navinfo.opentsp.user.service.param.login.LogoutParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.login.LoginResult;
import com.navinfo.opentsp.user.service.security.Md5;
import com.navinfo.opentsp.user.service.user.UserService;
import com.navinfo.opentsp.user.web.util.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-22
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class AutoLoginHelper {
    private static final Logger logger = LoggerFactory.getLogger(AutoLoginHelper.class);
    public static final String KEY_PREFIX = "atLogin_";

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserService userService;

    @Value("${opentsp.auto.login.keep.time:15}")
    private Integer autoLoginKeepDays;
    @Value("${opentsp.auto.login.ip.related:true}")
    private boolean ipRelated;

    /**
     * 设置自动登录，发送cookie
     * @param request
     * @param response
     * @param loginDto
     */
    public void setAutoLogin(HttpServletRequest request, HttpServletResponse response, LoginParam loginDto){
        String uuid = UUIDUtil.randomUUID().replace("-", "").toUpperCase();
        String cookieName = this.cookieName(request);
        //remember login info in redis
        this.cacheService.set(this.autoLoginKey(uuid), loginDto, autoLoginKeepDays, TimeUnit.DAYS);

        Cookie cookie = new Cookie(cookieName, uuid);
        cookie.setMaxAge(Long.valueOf(TimeUnit.DAYS.toSeconds(autoLoginKeepDays)).intValue());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void deleteLogin(HttpServletRequest request, HttpServletResponse response, LogoutParam loginDto) {
        String cookieName = this.cookieName(request);
        Cookie[] cookies = request.getCookies();
        String uuid = null;
        if(cookies != null && cookies.length > 0) {
            for(Cookie cookie : cookies) {
                if(cookieName.equals(cookie.getName())) {
                    uuid = cookie.getValue();
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);//delete cookie
                    break;
                }
            }
        }

        if(!StringUtils.isEmpty(uuid)) {
            this.cacheService.delete(this.autoLoginKey(uuid));
        }
    }

    /**
     *
     * 自动登录
     *
     * @param request
     * @param response
     */
    public CommonResult<LoginResult> autoLogin(HttpServletRequest request, HttpServletResponse response){
        String cookieName = this.cookieName(request);
        Cookie[] cookies = request.getCookies();
        String uuid = null;
        if(cookies != null && cookies.length > 0) {
            for(Cookie cookie : cookies) {
                if(cookieName.equals(cookie.getName())) {
                    uuid = cookie.getValue();
                    break;
                }
            }
        }

        if(StringUtils.isEmpty(uuid)) {
            return new CommonResult<LoginResult>().fillResult(ResultCode.NOT_LOGIN);
        }

        LoginParam loginDto = (LoginParam) this.cacheService.get(this.autoLoginKey(uuid));
        if(loginDto != null){
            loginDto.setAutoLogin(0);//这个值如果不改，userController.login会延长自动登录cookie的有效期
            return this.userService.login(loginDto);
        }

        return new CommonResult<LoginResult>().fillResult(ResultCode.NOT_LOGIN);
    }

    private String autoLoginKey(String uuid){
        return KEY_PREFIX + uuid;
    }

    /**
     *
     * 计算cookie名字
     *
     * @param request
     * @return
     */
    public String cookieName(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        String acceptEncoding = "gzip, deflate";//request.getHeader("Accept-Encoding"); //this header will changes according to request method (get/post)
        String acceptLanguage = request.getHeader("Accept-Language");
        String accept = request.getHeader("Accept");
        String deviceType = request.getHeader(GlobalConstans.HEADER_DEVICE_TYPE);
        if(StringUtils.isEmpty(deviceType)) {
            deviceType = DeviceType.web.name();
        }

        String ip = "localhost";//request.getRemoteAddr();//not reliable
        if(ipRelated)
            ip = HttpRequestUtil.getIpAddr(request);

        StringBuilder sb = new StringBuilder(userAgent).append(acceptEncoding).append(acceptLanguage).append(deviceType).append(accept).append(ip);

        return Md5.md5(sb.toString());
    }

}
