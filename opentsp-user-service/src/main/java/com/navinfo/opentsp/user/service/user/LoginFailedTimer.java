package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.service.cache.CacheService;
import com.navinfo.opentsp.user.service.var.ConfigPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-14
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class LoginFailedTimer {
    private static final String login_failed_key = "_loginF_";

    @Autowired
    private CacheService cacheService;
    @Autowired
    private ConfigPropertyService configPropertyService;

    public void incrFailed(String userId, String product) {
        long timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_LOGIN_FAILED_TIME,
                product, ConfigPropertyEntity.Types.login));
        String key = this.keys(userId);
        this.cacheService.increase(key, 1L);
        this.cacheService.expire(key, timeout, TimeUnit.MINUTES);
    }

    public boolean needCaptcha(String userId, String product){
        String key = this.keys(userId);
        Serializable val = this.cacheService.get(key);
        if(val != null) {
            Integer count = Integer.valueOf(String.valueOf(String.valueOf(val)));
            int line = Integer.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_LOGIN_FAILED_COUNT,
                    product, ConfigPropertyEntity.Types.login));

            long timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_LOGIN_FAILED_TIME,
                    product, ConfigPropertyEntity.Types.login));
            this.cacheService.expire(key, timeout, TimeUnit.MINUTES);

            return count != null && count >= line;
        }

        return false;
    }

    public void clearFailed(String userId){
        this.cacheService.delete(this.keys(userId));
    }

    private String keys(String userId){
        return login_failed_key + userId;
    }



}
