package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.dao.AppIDKeyDao;
import com.navinfo.opentsp.user.dal.entity.AppIDKeyEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.AppIDKeyEntityMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.profiles.Mybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-28
 * @modify
 * @copyright Navi Tsp
 */
@Repository
@Mybatis
public class AppIDKeyDaoImpl extends BaseDAOImpl<AppIDKeyEntity, String> implements AppIDKeyDao{

    @Autowired
    private AppIDKeyEntityMapper mapper;

    @Override
    public String save(AppIDKeyEntity entity) {
        if(StringUtils.isEmpty(entity.getId()))
            entity.setId(UUIDUtil.randomUUID());
       return super.save(entity);
    }

    @Override
    protected MybatisMapper<AppIDKeyEntity, String> getMapper() {
        return mapper;
    }

    @Override
    public List<AppIDKeyEntity> findAll() {
        return mapper.findAll();
    }
}
