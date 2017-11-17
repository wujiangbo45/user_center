package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.SendEmailLogDao;
import com.navinfo.opentsp.user.dal.entity.SendEmailLogEntity;
import com.navinfo.opentsp.user.dal.profiles.Mongo;
import org.springframework.stereotype.Repository;

import java.util.Date;

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
public class SendEmailLogDaoImpl extends BaseMongoDaoImpl<SendEmailLogEntity, String> implements SendEmailLogDao {
    @Override
    public long countByIp(String ip, Date date) {
        return 0;
    }

    @Override
    public long countByEmail(String email, Date date) {
        return 0;
    }

    @Override
    public SendEmailLogEntity findLatestLog(String email) {
        return null;
    }

    @Override
    public SendEmailLogEntity findLatestLogByIp(String ip) {
        return null;
    }
}
