package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.annotation.MybatisComponent;
import com.navinfo.opentsp.user.dal.dao.UserCarDao;
import com.navinfo.opentsp.user.dal.entity.UserCarEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.UserCarEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@MybatisComponent
public class UserCarDaoImpl extends BaseDAOImpl<UserCarEntity, String> implements UserCarDao {

    @Autowired
    private UserCarEntityMapper mapper;

    @Override
    protected MybatisMapper<UserCarEntity, String> getMapper() {
        return mapper;
    }

    /**
     * 根据用户ID查找车信息
     * @param userId
     * @return
     */
    @Override
    public List<UserCarEntity> findByUserId(String userId) {
        return mapper.selectByUserId(userId);
    }

    @Override
    public UserCarEntity findByUserIdAndCarNo(String userId, String carNo) {
        return mapper.findByUserIdAndCarNo(userId, carNo);
    }

    @Override
    public int deleteCarById(String id) {
        return mapper.deleteByCarId(id);
    }
}
