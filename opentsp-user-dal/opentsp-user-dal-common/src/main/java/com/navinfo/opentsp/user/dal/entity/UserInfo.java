package com.navinfo.opentsp.user.dal.entity;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-06
 * @modify
 * @copyright Navi Tsp
 */
public class UserInfo {

    private UserEntity userEntity;

    private UserProfileEntity userProfileEntity;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserProfileEntity getUserProfileEntity() {
        return userProfileEntity;
    }

    public void setUserProfileEntity(UserProfileEntity userProfileEntity) {
        this.userProfileEntity = userProfileEntity;
    }
}
