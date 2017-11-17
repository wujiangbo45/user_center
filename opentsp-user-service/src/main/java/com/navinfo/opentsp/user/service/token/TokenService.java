package com.navinfo.opentsp.user.service.token;

import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.service.device.DeviceManaged;
import com.navinfo.opentsp.user.service.device.DeviceType;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-12
 * @modify
 * @copyright Navi Tsp
 */
public interface TokenService {

    /**
     * 删除token
     * @param token
     */
    public void removeToken(String token);

    /**
     * 删除用户的所有token
     * @param userId  userId
     */
    public void removeUserToken(String userId);

    /**
     * 删除用户token， 保留指定的token
     * @param token
     */
    public void removeUserTokenExcept(String token);

    /**
     * 删除用户在某个设备类型的所有登录
     * @param userId
     * @param deviceType
     */
    public void removeUserTokenByDeviceType(String userId, DeviceType deviceType);

    /**
     * 创建token
     * @param userEntity
     * @param deviceManaged
     * @Param clientIp
     * @Param productId
     * @return
     */
    public TokenEntity createToken(UserEntity userEntity, DeviceManaged deviceManaged, String clientIp, String productId, String loginName);

    /**
     * 延长token时间
     * @param token
     */
    public void extendTokenExpireTime(String token);

    /**
     * 创建token
     * @param token
     * @return
     */
    public TokenEntity getToken(String token);

    /**
     * 得到最新的可用的token
     * @param userId
     * @param deviceType
     * @return
     */
    public TokenEntity getLatestAvailableToken(String userId, DeviceType deviceType);

    /**
     * 校验token 是否可用
     * @param tokenEntity
     * @return
     */
    public boolean isTokenValid(TokenEntity tokenEntity);
}
