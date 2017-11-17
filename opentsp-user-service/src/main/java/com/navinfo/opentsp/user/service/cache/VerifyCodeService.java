package com.navinfo.opentsp.user.service.cache;

import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.exception.DataExpiredException;

import java.io.Serializable;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface VerifyCodeService {

    /**
     *
     * 设置验证码
     *
     * @param function
     * @param type
     * @param product
     * @param key
     * @param value
     * @param timeout  seconds
     */
    public void setVerifyCode(Functions function, VerifyTypes type, String product, String key, String value, long timeout);

    public void setVerifyData(Functions function, VerifyTypes type, String product, String key, Serializable value, long timeout);

    public Serializable getVerifyData(Functions function, VerifyTypes type, String product, String key);

    public String getVerifyCode(Functions function, VerifyTypes type, String product, String key, boolean expire);

    public String getVerifyCode(Functions function, VerifyTypes type, String product, String key);

    public boolean checkVerifyCode(Functions function, VerifyTypes type, String product, String key, String code) throws DataExpiredException;

    public boolean checkVerifyCode(Functions function, VerifyTypes type, String product, String key, String code, boolean expire) throws DataExpiredException;
}
