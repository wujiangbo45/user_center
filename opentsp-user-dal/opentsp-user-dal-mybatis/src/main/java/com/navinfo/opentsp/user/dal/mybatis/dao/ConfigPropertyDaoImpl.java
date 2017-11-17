package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.dao.ConfigPropertyDao;
import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.ConfigPropertyEntityMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.profiles.Mybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@Repository
@Mybatis
public class ConfigPropertyDaoImpl extends BaseDAOImpl<ConfigPropertyEntity, String> implements ConfigPropertyDao {

    @Autowired
    private ConfigPropertyEntityMapper mapper;

    @Override
    protected MybatisMapper<ConfigPropertyEntity, String> getMapper() {
        return mapper;
    }

    @Override
    public List<ConfigPropertyEntity> findAll() {
        return mapper.findAll();
    }
}
