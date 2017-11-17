package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.UserPropertyEntity;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface UserPropertyDao extends BaseDAO<UserPropertyEntity, String>{

    UserPropertyEntity findAttr(String userId, String product, String attrName);

}
