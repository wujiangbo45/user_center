package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.AppIDKeyEntity;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-28
 * @modify
 * @copyright Navi Tsp
 */
public interface AppIDKeyDao extends BaseDAO<AppIDKeyEntity, String> {

    public List<AppIDKeyEntity> findAll();

}
