package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.BaseMongoDao;
import com.navinfo.opentsp.user.dal.dao.CommonSmsProductDao;
import com.navinfo.opentsp.user.dal.entity.CommomSmsProductEntity;
import org.springframework.stereotype.Repository;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-28
 * @modify
 * @copyright Navi Tsp
 */
@Repository
public class CommonSmsProductMongoDao extends BaseMongoDaoImpl<CommomSmsProductEntity, String>
        implements CommonSmsProductDao, BaseMongoDao<CommomSmsProductEntity, String> {

        @Override
        public CommomSmsProductEntity findByProductId(String productId) {
                return this.findById(productId);
        }
}
