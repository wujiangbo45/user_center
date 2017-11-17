package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.UserEntityDao;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.dal.profiles.Mongo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

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
public class UserDaoImpl extends BaseMongoDaoImpl<UserEntity, String> implements UserEntityDao {
    @Override
    public UserEntity findUserByMobile(String mobile) {
        QueryBuilder builder = new QueryBuilder(1);
        builder.addQueryItem("mobile", mobile);

        return mongoTemplate.findOne(builder.build(), UserEntity.class);
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        QueryBuilder builder = new QueryBuilder(1);
        builder.addQueryItem("email", email);

        return mongoTemplate.findOne(builder.build(), UserEntity.class);
    }

    @Override
    public UserEntity findUserByIdentifier(String identifier) {
        String pattern = "/" + identifier + "/";
        Criteria email = Criteria.where("email").regex(pattern);
        Criteria mobile = Criteria.where("mobile").regex(pattern);
        Criteria id = Criteria.where("id").is(identifier);

        Criteria criteria = new Criteria();
        criteria.orOperator(email, mobile, id);
        Query query = new Query(criteria);
        query.limit(1);
        return mongoTemplate.findOne(query, UserEntity.class);
    }

    @Override
    public int updateMobileIsNullById(String id) {
        Update update = Update.update("mobile", null).set("updateTime", new Date());
        return mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), update, UserEntity.class).getN();
    }

    @Override
    public int updateEmailIsNullById(String id) {
        Update update = Update.update("email", null).set("updateTime", new Date());
        return mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), update, UserEntity.class).getN();
    }

    @Override
    public List<UserEntity> query(String key, String startDate, String endDate, String bind, int pageNum, int pageSize,String order,String orderType) {
        return null;//TODO implements
    }

    @Override
    public long queryCount(String key, String startDate, String endDate, String bind) {
        return 0;//TODO implements
    }
}
