package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.ProductEntity;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface ProductDao extends BaseDAO<ProductEntity, String>{

    public List<ProductEntity> findAll();

}
