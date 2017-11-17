package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.CommomSmsProductEntity;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-28
 * @modify
 * @copyright Navi Tsp
 */
public interface CommonSmsProductDao extends BaseDAO<CommomSmsProductEntity, String> {

    CommomSmsProductEntity findByProductId(String productId);

}
