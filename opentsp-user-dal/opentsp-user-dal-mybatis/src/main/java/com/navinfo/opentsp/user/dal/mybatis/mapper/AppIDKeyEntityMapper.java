package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.AppIDKeyEntity;

import java.util.List;

public interface AppIDKeyEntityMapper extends MybatisMapper<AppIDKeyEntity, String> {

    List<AppIDKeyEntity> findAll();

}