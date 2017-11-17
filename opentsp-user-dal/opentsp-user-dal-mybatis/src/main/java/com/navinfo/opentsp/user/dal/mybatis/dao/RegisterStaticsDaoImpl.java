package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.entity.RegisterStaticsEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.RegisterStaticsEntityMapper;
import com.navinfo.opentsp.user.dal.profiles.Mybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@Repository
@Mybatis
public class RegisterStaticsDaoImpl extends BaseDAOImpl<RegisterStaticsEntity, String> implements com.navinfo.opentsp.user.dal.dao.RegisterStaticsDao {

    @Autowired
    private RegisterStaticsEntityMapper mapper;

    @Override
    protected MybatisMapper<RegisterStaticsEntity, String> getMapper() {
        return mapper;
    }

    @Override
    public int analyzeRegister(Date dayStart, Date dayEnd) {
        return mapper.analyzeRegister(dayStart, dayEnd);
    }

    @Override
    public int analyzeOne(String product, String registerSrc, Date dayStart, Date dayEnd) {
        return mapper.analyzeOne(product, registerSrc, dayStart, dayEnd);
    }

    @Override
    public int deleteAnalyzer(Date dayStart, Date dayEnd, String product, String registerSrc) {
        return mapper.deleteAnalyzer(product, registerSrc, dayStart, dayEnd);
    }

    @Override
    public List<Map<String, Object>> queryList(String product, String date) {
        return mapper.queryList(product, date);
    }

    @Override
    public List<Map<String, Object>> query4Statics(String product, String startDate, String endDate, String column) {
        return mapper.queryStatics(product, startDate, endDate, column);
    }
}
