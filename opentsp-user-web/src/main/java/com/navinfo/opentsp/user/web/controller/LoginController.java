package com.navinfo.opentsp.user.web.controller;

import com.navinfo.opentsp.user.common.util.date.DateUtil;
import com.navinfo.opentsp.user.common.util.json.JsonUtil;
import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.entity.AppIDKeyEntity;
import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.dal.entity.UserLoginLogEntity;
import com.navinfo.opentsp.user.service.appinfo.AppInfoService;
import com.navinfo.opentsp.user.service.cache.CacheService;
import com.navinfo.opentsp.user.service.param.DeviceBaseParam;
import com.navinfo.opentsp.user.service.param.login.*;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.login.LoginHistory;
import com.navinfo.opentsp.user.service.resultdto.login.LoginResult;
import com.navinfo.opentsp.user.service.resultdto.login.ValidateLoginResult;
import com.navinfo.opentsp.user.service.resultdto.login.WebWeixinTokenInfo;
import com.navinfo.opentsp.user.service.resultdto.setting.AccountResult;
import com.navinfo.opentsp.user.service.setting.AccountService;
import com.navinfo.opentsp.user.service.token.TokenService;
import com.navinfo.opentsp.user.service.user.OauthLoginService;
import com.navinfo.opentsp.user.service.user.UserService;
import com.navinfo.opentsp.user.service.user.WebWeixinOauth;
import com.navinfo.opentsp.user.web.autologin.AutoLoginHelper;
import com.navinfo.opentsp.user.web.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-14
 * @modify
 * @copyright Navi Tsp
 */
@RestController
@RequestMapping("/user")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private OauthLoginService oauthLoginService;
    @Autowired
    private AutoLoginHelper autoLoginHelper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private WebWeixinOauth webWeixinOauth;

    @Value("${opentsp.getCityByIp.url:}")
    private String getCityByIpUrl;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/login", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<LoginResult> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginParam loginParam) {
        CommonResult<LoginResult> loginResultCommonResult = this.userService.login(loginParam);
        if (loginResultCommonResult.getCode() == ResultCode.SUCCESS.code()) {
            String token = loginResultCommonResult.getData().getToken();
            String uuid = SessionUtil.createSessionId(request, response);
            cacheService.set(SessionUtil.SESSION_ID_PREFIX + uuid, token, 1800, TimeUnit.SECONDS);
        }
        if (loginParam.getAutoLogin() != null && loginParam.getAutoLogin().intValue() == 1 &&
                loginResultCommonResult.getCode() == ResultCode.SUCCESS.code()) {//如果登录成功了
            this.autoLoginHelper.setAutoLogin(request, response, loginParam);
        }
        return loginResultCommonResult;
    }

    @RequestMapping(value = "/loginHistory", produces = {"application/json; charset=utf-8"})
    public CommonResult<List<LoginHistory>> latestHistory(@RequestBody LatestLoginHistory history) {
//        UserEntity userEntity = history.getUser();
//        if (userEntity == null || StringUtils.isEmpty(userEntity.getId())) {
//            return new CommonResult<List<LoginHistory>>().fillResult(ResultCode.NOT_LOGIN);
//        }
        //TODO extract this number to param
        List<UserLoginLogEntity> entities = this.userService.latestLoginLog(history.getUserId(), 10);
        List<LoginHistory> histories = new ArrayList<>(entities.size());
        for (UserLoginLogEntity entity : entities) {
            LoginHistory history1 = new LoginHistory();
            history1.setIp(entity.getLoginIp());
            history1.setDevice(entity.getDeviceType());
            history1.setLoginTime(DateUtil.formatTime(entity.getLoginTime()));
            history1.setCity(getCityNameByIp(entity.getLoginIp()));
            histories.add(history1);
        }

        return new CommonResult<List<LoginHistory>>().fillResult(ResultCode.SUCCESS).setData(histories);
    }

    public String getCityNameByIp(String ip) {
        String cityName = "";
        try {
            String result = restTemplate.getForObject(getCityByIpUrl + "?ip=" + ip, String.class);
            Map<String, Object> cityInfo = JsonUtil.toMap(result);
            if (ResultCode.SUCCESS.code() == Integer.parseInt(cityInfo.get("statue").toString())) {
                Map<String, Object> city = (Map) cityInfo.get("city");
                cityName = city.get("cname").toString();
            }
        } catch (Exception e) {
            logger.error("get city name by ip error {}", ip);
        }
        return cityName;
    }

    /**
     * 如果token未过期，刷新token会产生一个新的token
     * 如果token已过期，则会提示token过期，需要重新登录
     *
     * @param request
     * @param tokenParam
     * @return
     */
    @RequestMapping(value = "/refreshToken", produces = {"application/json; charset=utf-8"})
    public CommonResult<LoginResult> refreshToken(HttpServletRequest request, @RequestBody RefreshTokenParam tokenParam) {
        return this.userService.refreshToken(tokenParam);
    }

    @RequestMapping(value = "/validateToken", produces = {"application/json; charset=utf-8"})
    public CommonResult<LoginResult> validateToken(HttpServletRequest request, ValidateTokenParam tokenParam) {
        UserEntity userEntity = tokenParam.getUser();
        if (userEntity != null) {
            return CommonResult.newInstance(ResultCode.SUCCESS, LoginResult.class)
                    .setData(new LoginResult(userEntity.getId(), userEntity.getNickname(), tokenParam.getToken()));
        } else {
            return CommonResult.newInstance(ResultCode.TOKEN_INVALID, LoginResult.class);
        }
    }

    /**
     * 单点登录token校验接口
     * @param request
     * @param tokenParam
     * @return
     */
    @RequestMapping(value = "/validateSSOToken", produces = {"application/json; charset=utf-8"})
    public CommonResult<ValidateLoginResult> validateToken(HttpServletRequest request, SSOValidateTokenParam tokenParam) {
        UserEntity userEntity = tokenParam.getUser();
        StringBuffer stringBuffer = new StringBuffer();
        if (cacheService.exists(SessionUtil.SSO_LOGOUT_PREFIX + tokenParam.getToken())) {
            cacheService.set(SessionUtil.SSO_LOGOUT_PREFIX + tokenParam.getToken(), tokenParam.getServerUrl(), 1800, TimeUnit.SECONDS);
        } else {
            String url = (String) cacheService.get(SessionUtil.SSO_LOGOUT_PREFIX + tokenParam.getToken());
            stringBuffer.append(url).append(",").append(tokenParam.getServerUrl());
            cacheService.set(SessionUtil.SSO_LOGOUT_PREFIX + tokenParam.getToken(), stringBuffer.toString(), 1800, TimeUnit.SECONDS);
        }
        if (userEntity != null) {
            return CommonResult.newInstance(ResultCode.SUCCESS, ValidateLoginResult.class)
                    .setData(new ValidateLoginResult(userEntity.getId(), userEntity.getNickname(), tokenParam.getToken(),userEntity.getEmail(), userEntity.getMobile()));
        } else {
            return CommonResult.newInstance(ResultCode.TOKEN_INVALID, ValidateLoginResult.class);
        }
    }

    /**
     * 增加用于单点登录使用的Cookie校验
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/validateSSOLogin")
    public CommonResult<ValidateLoginResult> validateLogin(HttpServletRequest request,DeviceBaseParam param) {
        Cookie[] cookies = request.getCookies();
        UserEntity userEntity = null;
        String sessionId = SessionUtil.getSessionId(request);
        logger.info("sessionId:"+sessionId);
        if (sessionId == null || cacheService.exists(SessionUtil.SESSION_ID_PREFIX + sessionId)) {
            return CommonResult.newInstance(ResultCode.NOT_LOGIN, ValidateLoginResult.class);
        }
        String token = cacheService.get(SessionUtil.SESSION_ID_PREFIX + sessionId).toString();
        logger.info("sessionId---token:"+token);
        if (StringUtils.isEmpty(token)) {
            return CommonResult.newInstance(ResultCode.NOT_LOGIN, ValidateLoginResult.class);
        }
        TokenEntity tokenEntity = tokenService.getToken(token);
        if (tokenEntity == null) {
            return CommonResult.newInstance(ResultCode.NOT_LOGIN, ValidateLoginResult.class);
        }
        CommonResult<UserEntity> commonResult = userService.userDetailById(tokenEntity.getUserId());
        userEntity = commonResult.getData();
        if (userEntity != null) {
            return CommonResult.newInstance(ResultCode.SUCCESS, ValidateLoginResult.class)
                    .setData(new ValidateLoginResult(userEntity.getId(), userEntity.getNickname(), token, userEntity.getEmail(), userEntity.getMobile()));
        } else {
            return CommonResult.newInstance(ResultCode.NOT_LOGIN, ValidateLoginResult.class);
        }
    }

    @RequestMapping(value = "/quickLogin", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<LoginResult> quikLogin(HttpServletRequest request,HttpServletResponse response,@RequestBody QuickLoginParam loginParam) {
        CommonResult<LoginResult>  loginResultCommonResult=this.userService.quickLogin(loginParam);
        if (loginResultCommonResult.getCode() == ResultCode.SUCCESS.code()) {
            String token = loginResultCommonResult.getData().getToken();
            String uuid = SessionUtil.createSessionId(request, response);
            cacheService.set(SessionUtil.SESSION_ID_PREFIX + uuid, token, 1800, TimeUnit.SECONDS);
        }
        return loginResultCommonResult;
    }

    @RequestMapping(value = "/thirdLogin", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<LoginResult> thirdLogin(HttpServletRequest request,HttpServletResponse response,@RequestBody OauthLoginParam oauthLoginParam) {
        CommonResult<LoginResult>  loginResultCommonResult=this.oauthLoginService.oauthLogin(oauthLoginParam);
        if (loginResultCommonResult.getCode() == ResultCode.SUCCESS.code()) {
            String token = loginResultCommonResult.getData().getToken();
            String uuid = SessionUtil.createSessionId(request, response);
            cacheService.set(SessionUtil.SESSION_ID_PREFIX + uuid, token, 1800, TimeUnit.SECONDS);
        }
        return loginResultCommonResult;
    }

    @RequestMapping(value = "/webThirdLogin", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<LoginResult> webThirdLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody WebOauthLoginParam webOauthLoginParam) {
        AppIDKeyEntity appIDKeyEntity = appInfoService.findAppInfo(webOauthLoginParam.getType(), webOauthLoginParam.getProduct());
        if (appIDKeyEntity == null) {
            return CommonResult.newInstance(ResultCode.THIRD_USER_BIND_FAILED, LoginResult.class);
        }
        WebWeixinTokenInfo webWeixinTokenInfo = webWeixinOauth.getOauthResult(webOauthLoginParam.getCode(), appIDKeyEntity);
        if (webWeixinTokenInfo == null) {
            return CommonResult.newInstance(ResultCode.THIRD_USER_BIND_FAILED, LoginResult.class);
        }
        OauthLoginParam oauthLoginParam = new OauthLoginParam();
        oauthLoginParam.setProduct(webOauthLoginParam.getProduct());
        oauthLoginParam.setAccessToken(webWeixinTokenInfo.getAccess_token());
        oauthLoginParam.setRefreshToken(webWeixinTokenInfo.getRefresh_token());
        oauthLoginParam.setType(webOauthLoginParam.getType());
        oauthLoginParam.setOpenId(webWeixinTokenInfo.getOpenid());
        oauthLoginParam.setAppVersion(webOauthLoginParam.getAppVersion());
        oauthLoginParam.setDeviceId(webOauthLoginParam.getDeviceId());
        oauthLoginParam.setIp(webOauthLoginParam.getIp());
        oauthLoginParam.setUnionid(webWeixinTokenInfo.getUnionid());
        oauthLoginParam.setDeviceTypeString(webOauthLoginParam.getDeviceTypeString());
        CommonResult<LoginResult> loginResultCommonResult = this.oauthLoginService.oauthLogin(oauthLoginParam);
        if (loginResultCommonResult.getCode() == ResultCode.SUCCESS.code()) {
            String uuid = SessionUtil.createSessionId(request, response);
            cacheService.set(SessionUtil.SESSION_ID_PREFIX + uuid, loginResultCommonResult.getData().getToken(), 1800, TimeUnit.SECONDS);
        }
        return loginResultCommonResult;
    }


    @RequestMapping(value = "/logout", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<String> thirdLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody LogoutParam logoutParam) {
        CommonResult<String> result = this.userService.logout(logoutParam);
        autoLoginHelper.deleteLogin(request, response, logoutParam);
        return result;
    }

    @RequestMapping(value = "/ssoLogout", method = {RequestMethod.GET}, produces = {"application/json; charset=utf-8"})
    public CommonResult<String> ssoLogin(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        CommonResult<String> result = null;
        String sessionId = SessionUtil.getSessionId(request);
        logger.info(SessionUtil.SESSION_ID_PREFIX + sessionId);
        if (sessionId != null && !cacheService.exists(SessionUtil.SESSION_ID_PREFIX + sessionId)) {
            String token = cacheService.get(SessionUtil.SESSION_ID_PREFIX + sessionId) + "";
            logger.info("sso logout token:{}", token);
            LogoutParam logoutParam = new LogoutParam();
            logoutParam.setToken(token);
            result = this.userService.logout(logoutParam);
            autoLoginHelper.deleteLogin(request, response, logoutParam);
            String logoutUrl = (String) cacheService.get(SessionUtil.SSO_LOGOUT_PREFIX + token);
           try {
               if (logoutUrl != null) {
                   String[] logoutUrlArr = StringUtils.split(logoutUrl, ",");

                   for (String logout : logoutUrlArr) {
                       logger.info(logout + "?token={}",token);
                       String logoutRecsult = restTemplate.postForObject(logout + "?token={token}", null, String.class, token);
                       logger.info(logoutRecsult);
                   }
               }
           }catch (Exception e){
               logger.error("sso logout third service error!,{}",e.getMessage());
           }
            cacheService.delete(SessionUtil.SESSION_ID_PREFIX + sessionId);
            cacheService.delete(SessionUtil.SSO_LOGOUT_PREFIX + token);
        } else {
            result = CommonResult.newInstance(ResultCode.NOT_LOGIN, String.class);
        }
        return result;
    }
}
