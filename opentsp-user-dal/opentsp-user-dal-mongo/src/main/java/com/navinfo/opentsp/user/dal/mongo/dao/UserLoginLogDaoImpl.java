package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.UserLoginLogDao;
import com.navinfo.opentsp.user.dal.entity.UserLoginLogEntity;
import com.navinfo.opentsp.user.dal.profiles.Mongo;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-25
 * @modify
 * @copyright Navi Tsp
 */
@Repository
@Mongo
public class UserLoginLogDaoImpl extends BaseMongoDaoImpl<UserLoginLogEntity, String> implements UserLoginLogDao {

    @Override
    public List<UserLoginLogEntity> latestLogs(String userId, Date date, int num) {
        return Collections.EMPTY_LIST;
    }
}
