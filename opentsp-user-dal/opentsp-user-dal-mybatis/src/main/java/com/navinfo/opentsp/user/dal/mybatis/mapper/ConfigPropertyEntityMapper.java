package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;

import java.util.List;

public interface ConfigPropertyEntityMapper extends MybatisMapper<ConfigPropertyEntity, String>{

    public List<ConfigPropertyEntity> findAll();

}