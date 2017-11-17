package com.navinfo.opentsp.user.service.cache.impl;

import com.navinfo.opentsp.user.service.cache.CacheService;
import com.navinfo.opentsp.user.service.cache.VerifyCodeService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.exception.DataExpiredException;
import com.navinfo.opentsp.user.service.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private CacheService cacheService;

    @Override
    public void setVerifyCode(Functions function, VerifyTypes type, String product, String key, String value, long timeout) {
        cacheService.set(this.keys(function, type, key, product), value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void setVerifyData(Functions function, VerifyTypes type, String product, String key, Serializable value, long timeout) {
        cacheService.set(this.keys(function, type, key, product), value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public Serializable getVerifyData(Functions function, VerifyTypes type, String product, String key) {
        String realKey = this.keys(function, type, key, product);
        Serializable serializable = cacheService.get(realKey);
        cacheService.delete(realKey);
        return serializable;
    }

    private String keys(Functions functions, VerifyTypes type, String key, String productId){
        return new StringBuilder(productId).append(".")
                .append(functions.func()).append(".").append(type.name()).append(".").append(key).toString();
    }

    @Override
    public String getVerifyCode(Functions function, VerifyTypes type, String product, String key, boolean expire) {
        String realKey = this.keys(function, type, key, product);
        String code = (String) cacheService.get(realKey);
        if(expire)
            cacheService.delete(realKey);
        return code;
    }

    @Override
    public String getVerifyCode(Functions function, VerifyTypes type, String product, String key) {
        return getVerifyCode(function, type, product, key, false);
    }

    @Override
    public boolean checkVerifyCode(Functions function, VerifyTypes type, String product, String key, String code)
            throws DataExpiredException {
        return this.checkVerifyCode(function, type, product, key, code, false);
    }

    @Override
    public boolean checkVerifyCode(Functions function, VerifyTypes type, String product, String key, String code, boolean expire)
            throws DataExpiredException {
        String exists = this.getVerifyCode(function, type, product, key, expire);
        if(exists == null) {
            throw new DataExpiredException("验证码错误!", ResultCode.VERIFY_CODE_ERROR);
        }
        if(exists.equalsIgnoreCase(code)) {
            return true;
        }
        return false;
    }
}
