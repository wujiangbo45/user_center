package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.UserProfileDao;
import com.navinfo.opentsp.user.dal.entity.UserProfileEntity;
import com.navinfo.opentsp.user.dal.profiles.Mongo;
import org.springframework.stereotype.Repository;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-25
 * @modify
 * @copyright Navi Tsp
 */
@Repository
@Mongo
public class UserProfileDaoImpl extends BaseMongoDaoImpl<UserProfileEntity, String> implements UserProfileDao {
}
