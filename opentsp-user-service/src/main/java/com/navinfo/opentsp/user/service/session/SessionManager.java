package com.navinfo.opentsp.user.service.session;

import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.service.cache.CacheService;
import com.navinfo.opentsp.user.service.token.TokenService;
import com.navinfo.opentsp.user.service.var.ConfigPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-14
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private static final String COMMON_SESSION_ID = "common_sid";

    @Autowired
    private CacheService cacheService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ConfigPropertyService configPropertyService;

    public Session getSession(String token) {
        SessionMeta sessionMeta = this.getSessionMeta(token);
        DefaultSessionImpl defaultSession = new DefaultSessionImpl(sessionMeta, this.cacheService);
        return defaultSession;
    }

    public Session getCommonSession() {
        return this.getSession(COMMON_SESSION_ID);
    }

    public SessionMeta getSessionMeta(String token) {
        Assert.notNull(token);
        SessionMeta sessionMeta = (SessionMeta) cacheService.hget(token, SessionMeta.META_KEY);
        if (sessionMeta == null) {
            if(!COMMON_SESSION_ID.equals(token)) {
                TokenEntity tokenEntity = this.tokenService.getToken(token);
                if (!this.tokenService.isTokenValid(tokenEntity)) {
                    logger.error("token {} is invalid !", token);
                    throw new InvalidTokenException("token [ " + token + " ] invalid !");
                }
            }

            sessionMeta = new SessionMeta(token);
        } else {
            sessionMeta.updateLastAccessTime();
        }

        this.cacheService.hset(token, SessionMeta.META_KEY, sessionMeta);//update session meta

        long timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_SESSION_EXPIRE_TIME, ConfigPropertyEntity.Types.session));
        this.cacheService.expire(token, timeout, TimeUnit.MINUTES);
        return sessionMeta;
    }


}
