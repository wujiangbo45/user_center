package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.LocationEntity;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-05
 * @modify
 * @copyright Navi Tsp
 */
public interface LocationDao extends BaseMongoDao<LocationEntity, String> {


    public void deleteAll();

    public long count();

    public List<LocationEntity> findCity(String parentId, String name);

}
