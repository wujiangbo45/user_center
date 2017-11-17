package com.navinfo.opentsp.user.service.token;

import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.dal.entity.UserEntity;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public interface TokenManaged {

    public String getToken();

    public void setToken(String token);

    public UserEntity getUser();

    public void setUser(UserEntity user);

    public void setTokenEntity(TokenEntity tokenEntity);

    public TokenEntity getTokenEntity();
}
