package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.ProductEntity;

import java.util.List;

public interface ProductEntityMapper extends MybatisMapper<ProductEntity, String>{

    List<ProductEntity> findAll();
}