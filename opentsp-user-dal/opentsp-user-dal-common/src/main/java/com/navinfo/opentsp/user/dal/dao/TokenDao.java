package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.TokenEntity;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface TokenDao extends BaseDAO<TokenEntity, String>{

    /**
     * 删除一个token
     * @param token
     * @return
     */
    public int removeToken(String token);

    /**
     * 删除用户的所有token
     * @param userId
     * @return
     */
    public int removeAllToken(String userId);

    /**
     * 删除某个设备类型的所有token
     * @param userId
     * @param deviceType
     * @return
     */
    public int removeTokenByDeviceType(String userId, String deviceType);

    /**
     * 查找token
     * @param token
     * @return
     */
    public TokenEntity getToken(String token);

    /**
     * 查到最新可用的token
     * @param userId
     * @param deviceType
     * @return
     */
    public TokenEntity getLatestAvailableToken(String userId, String deviceType);

}
