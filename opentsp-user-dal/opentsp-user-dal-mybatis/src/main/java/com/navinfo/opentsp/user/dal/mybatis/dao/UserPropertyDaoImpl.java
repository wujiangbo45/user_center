package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.annotation.MybatisComponent;
import com.navinfo.opentsp.user.dal.dao.UserPropertyDao;
import com.navinfo.opentsp.user.dal.entity.UserPropertyEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.UserPropertyEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@MybatisComponent
public class UserPropertyDaoImpl extends BaseDAOImpl<UserPropertyEntity, String> implements UserPropertyDao {

    @Autowired
    private UserPropertyEntityMapper mapper;

    @Override
    protected MybatisMapper<UserPropertyEntity, String> getMapper() {
        return mapper;
    }

    @Override
    public UserPropertyEntity findAttr(String userId, String product, String attrName) {
        return mapper.findAttr(userId, product, attrName);
    }
}
