package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.RegisterStaticsDao;
import com.navinfo.opentsp.user.dal.entity.RegisterStaticsEntity;
import com.navinfo.opentsp.user.dal.profiles.Mongo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
public class RegisterStaticsDaoImpl extends BaseMongoDaoImpl<RegisterStaticsEntity, String> implements RegisterStaticsDao {
    @Override
    public int analyzeRegister(Date dayStart, Date dayEnd) {
        return 0;
    }

    @Override
    public int analyzeOne(String product, String registerSrc, Date dayStart, Date dayEnd) {
        return 0;
    }

    @Override
    public int deleteAnalyzer(Date dayStart, Date dayEnd, String product, String registerSrc) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> queryList(String product, String date) {
        return null;
    }

    @Override
    public List<Map<String, Object>> query4Statics(String product, String startDate, String endDate, String column) {
        return null;
    }
}
