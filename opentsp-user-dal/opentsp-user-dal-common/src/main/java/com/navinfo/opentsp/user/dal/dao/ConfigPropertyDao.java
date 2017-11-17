package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface ConfigPropertyDao extends BaseDAO<ConfigPropertyEntity, String>{

    public List<ConfigPropertyEntity> findAll();

}
