package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.common.util.date.DateUtil;
import com.navinfo.opentsp.user.dal.annotation.MybatisComponent;
import com.navinfo.opentsp.user.dal.dao.UserEntityDao;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.UserEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

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
public class UserDaoImpl extends BaseDAOImpl<UserEntity, String> implements UserEntityDao {

    @Autowired
    private UserEntityMapper mapper;

    @Override
    protected MybatisMapper<UserEntity, String> getMapper() {
        return mapper;
    }

    @Override
    public UserEntity findUserByMobile(String mobile) {
        if (StringUtils.isEmpty(mobile))
            return null;
        return mapper.findUserByMobile(mobile);
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        if (StringUtils.isEmpty(email))
            return null;
        return mapper.findUserByEmail(email);
    }

    @Override
    public UserEntity findUserByIdentifier(String identifier) {
        return this.mapper.findUserByIdentifier(identifier);
    }


    /**
     * 将手机号置空
     *
     * @param id
     * @return
     */
    public int updateMobileIsNullById(String id) {
        return mapper.updateMobileIsNullById(id);
    }

    /**
     * 将邮箱置空
     *
     * @param id
     * @return
     */
    public int updateEmailIsNullById(String id) {
        return mapper.updateEmailIsNullById(id);
    }

    @Override
    public List<UserEntity> query(String key, String startDate, String endDate, String bind, int pageNum, int pageSize,String order,String orderType) {
        Date sDate = null;
        Date eDate = null;

        if (!StringUtils.isEmpty(startDate)) {
            sDate = DateUtil.parseDate(startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            eDate = DateUtil.parseDate(endDate);
        }

        int offset = (pageNum - 1) * pageSize;
        return mapper.query(key, sDate, eDate, bind, offset, pageSize,order,orderType);
    }

    @Override
    public long queryCount(String key, String startDate, String endDate, String bind) {
        Date sDate = null;
        Date eDate = null;

        if (!StringUtils.isEmpty(startDate)) {
            sDate = DateUtil.parseDate(startDate);
        }
        if (!StringUtils.isEmpty(endDate)) {
            eDate = DateUtil.parseDate(endDate);
        }

        return mapper.queryCount(key, sDate, eDate, bind);
    }
}
