package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.BaseMongoDao;
import com.navinfo.opentsp.user.dal.dao.CommonSendSmsLogDao;
import com.navinfo.opentsp.user.dal.entity.CommonSendSmsLogEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-26
 * @modify
 * @copyright Navi Tsp
 */
@Repository
public class CommonSendSmsLogDaoMongoImpl extends BaseMongoDaoImpl<CommonSendSmsLogEntity, String>
        implements CommonSendSmsLogDao, BaseMongoDao<CommonSendSmsLogEntity, String> {

    @Override
    public long countByIp(String ip, Date date, boolean success) {
        Criteria criteria = Criteria.where("ip").is(ip).and("sendTime").gte(date).and("success").is(success);
        Query query = new Query(criteria);
        return mongoTemplate.count(query, CommonSendSmsLogEntity.class);
    }

    @Override
    public long countByPhone(String phone, Date date, boolean success) {
        Criteria criteria = Criteria.where("phone").is(phone).and("sendTime").gte(date).and("success").is(success);
        Query query = new Query(criteria);
        return mongoTemplate.count(query, CommonSendSmsLogEntity.class);
    }

    @Override
    public CommonSendSmsLogEntity findLatestByPhone(String phone, boolean success) {
        Criteria criteria = Criteria.where("phone").is(phone).and("success").is(success);
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "sendTime"));
        query.limit(1);
        return mongoTemplate.findOne(query, CommonSendSmsLogEntity.class);
    }

    @Override
    public CommonSendSmsLogEntity findLatestByIP(String ip, boolean success) {
        Criteria criteria = Criteria.where("ip").is(ip).and("success").is(success);
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "sendTime"));
        query.limit(1);
        return mongoTemplate.findOne(query, CommonSendSmsLogEntity.class);
    }

    @Override
    public long countByPhoneAndContent(String phone, String content, Date date, boolean success) {
        Criteria criteria = Criteria.where("phone").is(phone).and("sendTime").gte(date)
                .and("content").is(content).and("success").is(success);
        Query query = new Query(criteria);
        return mongoTemplate.count(query, CommonSendSmsLogEntity.class);
    }
}
