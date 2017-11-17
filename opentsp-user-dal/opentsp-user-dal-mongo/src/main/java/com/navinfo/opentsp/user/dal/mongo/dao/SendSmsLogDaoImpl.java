package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.SendSmsLogDao;
import com.navinfo.opentsp.user.dal.entity.SendSmsLogEntity;
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
public class SendSmsLogDaoImpl extends BaseMongoDaoImpl<SendSmsLogEntity, String> implements SendSmsLogDao {
    @Override
    public long countByIp(String ip, Date date) {
        return 0;
    }

    @Override
    public long countByPhone(String phone, Date date) {
        return 0;
    }

    @Override
    public long countByPhoneAndContent(String phone, String content, Date date) {
        return 0;
    }

    @Override
    public SendSmsLogEntity findLatestLog(String phone) {
        return null;
    }

    @Override
    public SendSmsLogEntity findLatestLogByIp(String ip) {
        return null;
    }
}
