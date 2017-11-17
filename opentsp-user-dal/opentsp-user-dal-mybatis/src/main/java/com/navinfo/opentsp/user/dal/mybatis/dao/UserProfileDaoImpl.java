package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.annotation.MybatisComponent;
import com.navinfo.opentsp.user.dal.dao.UserProfileDao;
import com.navinfo.opentsp.user.dal.entity.UserProfileEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.UserProfileEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@MybatisComponent
public class UserProfileDaoImpl extends BaseDAOImpl<UserProfileEntity, String> implements UserProfileDao {

    @Autowired
    private UserProfileEntityMapper mapper;

    @Override
    protected MybatisMapper<UserProfileEntity, String> getMapper() {
        return mapper;
    }
}
