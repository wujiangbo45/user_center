package com.navinfo.opentsp.user.service.token;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.service.cache.CacheService;
import com.navinfo.opentsp.user.service.device.DeviceType;
import com.navinfo.opentsp.user.service.var.ConfigPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-12
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {

    public static final String REDIS_TOKEN_HKEY = "_token_";

    @Autowired
    private CacheService cacheService;
    @Autowired
    private ConfigPropertyService configPropertyService;


    /**
     * token -> tokenEntity
     *
     * @param tokenEntity
     */
    @Override
    public void putTokenIntoCache(TokenEntity tokenEntity) {
        long timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_TOKEN_EXPIRE_TIME,
                tokenEntity.getOpProductId(), ConfigPropertyEntity.Types.session));

        if(DeviceType.web.name().equals(tokenEntity.getDeviceType())) {
            timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_TOKEN_WEB_EXPIRE_TIME,
                    tokenEntity.getOpProductId(), ConfigPropertyEntity.Types.session));
        }

        String key = GlobalConstans.REDIS_KEY_SESSION + tokenEntity.getToken();
        this.cacheService.set(key, tokenEntity, timeout, TimeUnit.MINUTES);

        /**
         * 将token存入redis， 同时设置user的hkey缓存
         */
        String hkey = REDIS_TOKEN_HKEY + tokenEntity.getDeviceType();
        this.setUserAttr(tokenEntity.getUserId(), hkey, tokenEntity.getToken());
    }

    @Override
    public TokenEntity getTokenFromCache(String token) {
        return (TokenEntity) this.cacheService.get(GlobalConstans.REDIS_KEY_SESSION + token);
    }

    /**
     * 先后现根据userId 和 deviceType  从user hash中获取token 的值，然后根据token值获取token对象
     * @param userId
     * @param deviceType
     * @return
     */
    @Override
    public TokenEntity getTokenFromCache(String userId, DeviceType deviceType) {
        String hkey = REDIS_TOKEN_HKEY + deviceType.name();
        String token = (String) this.getUserAttr(userId, hkey);

        return this.getTokenFromCache(token);
    }

    public void removeTokenFromCache(String token) {
        this.cacheService.delete(GlobalConstans.REDIS_KEY_SESSION + token);
    }

    @Override
    public void removeTokenFromCache(String userId, DeviceType deviceType) {
        String hkey = REDIS_TOKEN_HKEY + deviceType.name();
        String token = (String) this.rmUserAttr(userId, hkey);
        this.removeTokenFromCache(token);
    }

    @Override
    public void removeAllToken(String userId) {
        for(DeviceType deviceType : DeviceType.values()) {
            this.removeTokenFromCache(userId, deviceType);
        }
    }

    @Override
    public void setUserAttr(String userId, String hkey, Serializable value) {
        String key = GlobalConstans.REDIS_KEY_USER + userId;
        this.cacheService.hset(key, hkey, value);
        long timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_TOKEN_EXPIRE_TIME,
                ConfigPropertyEntity.DEFAULT_PRODUCT, ConfigPropertyEntity.Types.session));
        this.cacheService.expire(key, timeout, TimeUnit.MINUTES);
    }

    @Override
    public Serializable rmUserAttr(String userId, String hkey) {
        String key = GlobalConstans.REDIS_KEY_USER + userId;
        String value = (String) this.cacheService.hget(key, hkey);
        this.cacheService.hdelete(key, hkey);
        return value;
    }

    @Override
    public Serializable getUserAttr(String userId, String hkey) {
        String key = GlobalConstans.REDIS_KEY_USER + userId;
        return this.cacheService.hget(key, hkey);
    }

    @Override
    public void removeAllUserAttr(String userId) {
        this.cacheService.delete(GlobalConstans.REDIS_KEY_USER + userId);
    }
}
