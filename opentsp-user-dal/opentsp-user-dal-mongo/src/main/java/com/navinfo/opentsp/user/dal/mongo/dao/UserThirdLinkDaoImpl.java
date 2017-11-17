package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.entity.UserThirdLinkEntity;
import com.navinfo.opentsp.user.dal.profiles.Mongo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
public class UserThirdLinkDaoImpl extends BaseMongoDaoImpl<UserThirdLinkEntity, String> implements com.navinfo.opentsp.user.dal.dao.UserThirdLinkDao {
    @Override
    public UserThirdLinkEntity findByTypeAndId(String thirdType, String openId) {
        Criteria criteria = new Criteria();
        criteria.and("thirdType").is(thirdType).and("thirdOpenId").is(openId);
        Query query = new Query(criteria);

        return mongoTemplate().findOne(query, UserThirdLinkEntity.class);
    }
    @Override
    public UserThirdLinkEntity findByTypeAndUnionId(String thirdType, String unionid) {
        Criteria criteria = new Criteria();
        criteria.and("thirdType").is(thirdType).and("unionid").is(unionid);
        Query query = new Query(criteria);

        return mongoTemplate().findOne(query, UserThirdLinkEntity.class);
    }
    @Override
    public UserThirdLinkEntity findByTypeAndOpenId(String thirdType, String openId) {
        Criteria criteria = new Criteria();
        criteria.and("thirdType").is(thirdType).and("thirdOpenId").is(openId);
        Query query = new Query(criteria);

        return mongoTemplate().findOne(query, UserThirdLinkEntity.class);
    }
    @Override
    public List<String> selectByUserId(String userId, byte isVaild) {
        QueryBuilder builder = new QueryBuilder(2);
        builder.addQueryItem("userId", userId).addQueryItem("isValid", isVaild);

        List<UserThirdLinkEntity> entities = mongoTemplate().find(builder.build(), UserThirdLinkEntity.class);
        List<String> list = new ArrayList<>(entities.size());
        for (UserThirdLinkEntity entity : entities) {
            list.add(entity.getThirdType());
        }

        return list;
    }

    @Override
    public String selectIdByUserIdAndType(String userId, String thridType, byte isValid) {
        QueryBuilder builder = new QueryBuilder(3);
        builder.addQueryItem("userId", userId).addQueryItem("thirdType", thridType).addQueryItem("isValid", isValid);

        UserThirdLinkEntity entity = mongoTemplate().findOne(builder.build(), UserThirdLinkEntity.class);
        if (entity != null)
            return entity.getId();
        return null;
    }
}
