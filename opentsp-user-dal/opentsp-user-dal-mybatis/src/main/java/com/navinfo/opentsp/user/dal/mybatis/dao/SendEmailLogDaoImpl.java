package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.annotation.MybatisComponent;
import com.navinfo.opentsp.user.dal.dao.SendEmailLogDao;
import com.navinfo.opentsp.user.dal.entity.SendEmailLogEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.SendEmailLogEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@MybatisComponent
public class SendEmailLogDaoImpl extends BaseDAOImpl<SendEmailLogEntity, String> implements SendEmailLogDao {

    @Autowired
    private SendEmailLogEntityMapper mapper;

    @Override
    protected MybatisMapper<SendEmailLogEntity, String> getMapper() {
        return mapper;
    }

    @Override
    public long countByIp(String ip, Date date) {
        return mapper.countByIp(ip, date);
    }

    @Override
    public long countByEmail(String email, Date date) {
        return mapper.countByEmail(email, date);
    }

    @Override
    public SendEmailLogEntity findLatestLog(String email) {
        return mapper.findLatestLog(email);
    }

    @Override
    public SendEmailLogEntity findLatestLogByIp(String ip) {
        return mapper.findLatestLogByIp(ip);
    }
}
