package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.UserProfileEntity;

public interface UserProfileEntityMapper extends MybatisMapper<UserProfileEntity, String>{

    public void updateImgUrlById(String url , String id);
}