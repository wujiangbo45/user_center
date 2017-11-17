package com.navinfo.opentsp.user.web.interceptor;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.common.util.json.JsonUtil;
import com.navinfo.opentsp.user.dal.dao.UserPropertyDao;
import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.dal.entity.UserPropertyEntity;
import com.navinfo.opentsp.user.service.cache.CacheReloadable;
import com.navinfo.opentsp.user.service.device.DeviceType;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.token.TokenService;
import com.navinfo.opentsp.user.web.util.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by wupeng on 11/3/15.
 */
@Component
public class BackendCheckInterceptor implements OpentspInterceptor, InitializingBean, CacheReloadable {

    private static final Logger logger = LoggerFactory.getLogger(BackendCheckInterceptor.class);

    public static final String BACKEND_ADMIN_PROPERTY = "backend_admin";
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Autowired
    private TokenService tokenService;

    @Value("${opentsp.backend.admin:}")
    private String adminNames;

    @Autowired
    private UserPropertyDao propertyDao;

    private final Set<String> admins = new HashSet<>();
    //TODO use redis instead
    private final Set<String> dbAdmins = new HashSet<>();

    @Override
    public String urlMapping() {
        return "/backend/**";
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
            throws Exception {

        String token = httpServletRequest.getHeader(GlobalConstans.TOKEN_HEADER_NAME);
        if (StringUtils.isEmpty(token)) {
            logger.info("can not found token !");
            this.writeError(httpServletResponse, ResultCode.NOT_LOGIN);
            return false;
        }

        TokenEntity tokenEntity = this.tokenService.getToken(token);
        if (tokenEntity == null || !tokenService.isTokenValid(tokenEntity)) {
            logger.info("token was invalid !");
            this.writeError(httpServletResponse, ResultCode.TOKEN_INVALID);
            return false;
        }

        String deviceType = httpServletRequest.getHeader(GlobalConstans.HEADER_DEVICE_TYPE);
        if (StringUtils.isEmpty(deviceType)) {
            logger.info("no deviceType found ! use default web device !");
            deviceType = DeviceType.web.name();
        } else {
            try {
                Enum.valueOf(DeviceType.class, deviceType);
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
                this.writeError(httpServletResponse, ResultCode.BAD_REQUEST);
                return false;
            }
        }

        String ip = HttpRequestUtil.getIpAddr(httpServletRequest);
        if (!deviceType.equals(tokenEntity.getDeviceType())) {
            logger.info("deviceType from right now is not match with the one from login !");
            this.writeError(httpServletResponse, ResultCode.TOKEN_NOT_MATCH);
            return false;
        }

        if (!ip.equals(tokenEntity.getClientIp())) {
            logger.info("client ip from right now is not match with the one from login !");
            this.writeError(httpServletResponse, ResultCode.TOKEN_NOT_MATCH);
            return false;
        }

        if (!authed(tokenEntity)) {
            logger.info("your name has no backend admin privileges !");
            this.writeError(httpServletResponse, ResultCode.UNAUTHORIZED);
            return false;
        }

        return true;
    }

    private boolean authed(TokenEntity tokenEntity) {
        boolean authed = false;
        if (!admins.contains(tokenEntity.getLoginName())) {
            String userId = tokenEntity.getUserId();
            lock.readLock().lock();
            try {
                if (dbAdmins.contains(userId)) {
                    authed = true;
                }
            } finally {
                lock.readLock().unlock();
            }

            if (!authed) {
                UserPropertyEntity propertyEntity = this.propertyDao.findAttr(userId, GlobalConstans.DEFAULT_PRODUCT, BACKEND_ADMIN_PROPERTY);
                if (propertyEntity != null && "1".equals(propertyEntity.getAttrValue())) {
                    lock.writeLock().lock();
                    try {
                        dbAdmins.add(propertyEntity.getUserId());
                    } finally {
                        lock.writeLock().unlock();
                    }
                    authed = true;
                }
            }
        } else {
            authed = true;
        }

        return authed;
    }

    private void writeError(HttpServletResponse response, ResultCode resultCode) {
        response.setStatus(resultCode.httpCode());
        CommonResult<String> result = new CommonResult<String>().fillResult(resultCode);

        PrintWriter writer = null;
        try {
            writer = response.getWriter();

            writer.write(JsonUtil.toJson(result));
            writer.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.isEmpty(adminNames)) {
            for (String name : adminNames.split(",")) {
                admins.add(name);
            }
        }
    }

    @Override
    public void reload() {
        logger.info("clear db admin cache !");
        lock.writeLock().lock();
        try {
            dbAdmins.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
