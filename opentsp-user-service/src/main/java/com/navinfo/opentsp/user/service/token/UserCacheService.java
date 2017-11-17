package com.navinfo.opentsp.user.service.token;

import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.service.device.DeviceType;

import java.io.Serializable;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-12
 * @modify
 * @copyright Navi Tsp
 */
public interface UserCacheService {

    void putTokenIntoCache(TokenEntity tokenEntity);

    Serializable getTokenFromCache(String token);

    TokenEntity getTokenFromCache(String userId, DeviceType deviceType);

    void removeTokenFromCache(String userId, DeviceType deviceType);

    void removeTokenFromCache(String token);

    void removeAllToken(String userId);

    void setUserAttr(String userId, String hkey, Serializable value);

    Serializable rmUserAttr(String userId, String hkey);

    Serializable getUserAttr(String userId, String hkey);

    void removeAllUserAttr(String userId);
}
