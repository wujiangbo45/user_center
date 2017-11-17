package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.UserCarDao;
import com.navinfo.opentsp.user.dal.entity.UserCarEntity;
import com.navinfo.opentsp.user.dal.profiles.Mongo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-25
 * @modify
 * @copyright Navi Tsp
 */
//TODO implements
@Repository
@Mongo
public class UserCarDaoImpl extends BaseMongoDaoImpl<UserCarEntity, String> implements UserCarDao {
    @Override
    public List<UserCarEntity> findByUserId(String userId) {
        return null;
    }

    @Override
    public UserCarEntity findByUserIdAndCarNo(String userId, String carNo) {
        return null;
    }

    @Override
    public int deleteCarById(String id) {
        return 0;
    }
}
