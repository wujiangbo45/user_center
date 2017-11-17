package com.navinfo.opentsp.user.service.event;

import com.navinfo.opentsp.user.common.util.event.AbstractOpentspEvent;
import com.navinfo.opentsp.user.dal.entity.UserEntity;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-12-21
 * @modify
 * @copyright Navi Tsp
 */
public class UpdateUserEvent extends AbstractOpentspEvent<UserEntity> {

    private UserEntity userEntity;
    private String token;

    public UpdateUserEvent(UserEntity userEntity, String token) {
        this.userEntity = userEntity;
        this.token = token;
    }

    @Override
    public UserEntity getData() {
        return userEntity;
    }

    public String token() {
        return token;
    }
}
