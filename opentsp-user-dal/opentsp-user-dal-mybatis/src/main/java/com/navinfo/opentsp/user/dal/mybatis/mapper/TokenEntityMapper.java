package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import org.apache.ibatis.annotations.Param;

public interface TokenEntityMapper extends MybatisMapper<TokenEntity, String>{

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
     * 根据device类型删除所有token
     * @param userId
     * @param deviceType
     * @return
     */
    public int removeTokenByDeviceType(@Param("userId") String userId, @Param("deviceType") String deviceType);

    /**
     * 获取token实体
     * @param token
     * @return
     */
    TokenEntity getToken(String token);

    /**
     * 获取最新可用的token
     * @param userId
     * @param deviceType
     * @return
     */
    public TokenEntity getLatestAvailableToken(@Param("userId") String userId, @Param("deviceType") String deviceType);
}