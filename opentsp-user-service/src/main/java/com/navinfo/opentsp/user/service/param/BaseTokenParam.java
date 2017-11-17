package com.navinfo.opentsp.user.service.param;

import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.service.token.TokenManaged;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-09
 * @modify
 * @copyright Navi Tsp
 */
public abstract class BaseTokenParam extends BaseParam implements TokenManaged {

    private String token;

    private UserEntity user;

    private TokenEntity tokenEntity;

    @Override
    public TokenEntity getTokenEntity() {
        return tokenEntity;
    }

    @Override
    public void setTokenEntity(TokenEntity tokenEntity) {
        this.tokenEntity = tokenEntity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
