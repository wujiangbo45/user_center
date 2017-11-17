package com.navinfo.opentsp.user.service.location;

import com.navinfo.opentsp.user.dal.entity.LocationEntity;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-05
 * @modify
 * @copyright Navi Tsp
 */
public interface LocationService {

    /**
     * 查找
     * @param parentId
     * @param name
     * @return
     */
    public List<LocationEntity> findCity(String parentId, String name);

    public void reload();

}
