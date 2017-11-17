package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.UserPropertyDao;
import com.navinfo.opentsp.user.dal.entity.UserPropertyEntity;
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
public class UserPropertyDaoImpl extends BaseMongoDaoImpl<UserPropertyEntity, String> implements UserPropertyDao {
    @Override
    public UserPropertyEntity findAttr(String userId, String product, String attrName) {
        QueryBuilder builder = new QueryBuilder(3);
        builder.addQueryItem("userId", userId).addQueryItem("productId", product).addQueryItem("attrName", attrName);

        return mongoTemplate().findOne(builder.build(), UserPropertyEntity.class);
    }
}
