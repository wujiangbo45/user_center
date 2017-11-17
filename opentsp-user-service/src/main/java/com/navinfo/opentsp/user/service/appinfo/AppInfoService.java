package com.navinfo.opentsp.user.service.appinfo;

import com.navinfo.opentsp.user.dal.entity.AppIDKeyEntity;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */
public interface AppInfoService {

    /**
     *
     * @param thirdType
     * @param product
     * @return
     */
    public AppIDKeyEntity findAppInfo(String thirdType, String product);

    public void reload();

    public void save(AppIDKeyEntity entity);

    public List<AppIDKeyEntity> findAll();

    public void delete(String product, String thirdType);

}
