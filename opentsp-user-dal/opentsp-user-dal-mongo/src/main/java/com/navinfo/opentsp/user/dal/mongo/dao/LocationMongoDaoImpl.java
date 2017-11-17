package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.LocationDao;
import com.navinfo.opentsp.user.dal.entity.LocationEntity;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-05
 * @modify
 * @copyright Navi Tsp
 */
@Repository
public class LocationMongoDaoImpl extends BaseMongoDaoImpl<LocationEntity, String> implements LocationDao {

    @Override
    public void deleteAll() {
        this.mongoTemplate.dropCollection(LocationEntity.class);
    }

    @Override
    public long count() {
        return this.mongoTemplate.count(new Query(), LocationEntity.class);
    }

    @Override
    public List<LocationEntity> findCity(String parentId, String name) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(parentId)) {
            criteria.and("parentCode").is(parentId);
        } else {
            criteria.and("level").is(0);
        }

        if (!StringUtils.isEmpty(name)) {
            criteria.and("name").regex("/." + name + ".*/");
        }

        return this.mongoTemplate.find(new Query(criteria), LocationEntity.class);
    }
}
