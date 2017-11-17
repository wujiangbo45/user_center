package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.annotation.MybatisComponent;
import com.navinfo.opentsp.user.dal.dao.UserLoginLogDao;
import com.navinfo.opentsp.user.dal.entity.UserLoginLogEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.UserLoginLogEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@MybatisComponent
public class UserLoginLogDaoImpl extends BaseDAOImpl<UserLoginLogEntity, String> implements UserLoginLogDao {

    @Autowired
    private UserLoginLogEntityMapper mapper;

    @Override
    protected MybatisMapper<UserLoginLogEntity, String> getMapper() {
        return mapper;
    }

    @Override
    public List<UserLoginLogEntity> latestLogs(String userId, Date date, int num) {

        return mapper.latestLogs(userId, date, num);
    }
}
