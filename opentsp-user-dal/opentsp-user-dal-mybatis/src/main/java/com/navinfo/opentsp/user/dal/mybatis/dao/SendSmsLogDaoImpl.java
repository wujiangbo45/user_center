package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.annotation.MybatisComponent;
import com.navinfo.opentsp.user.dal.dao.SendSmsLogDao;
import com.navinfo.opentsp.user.dal.entity.SendSmsLogEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.SendSmsLogEntityMapper;
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
public class SendSmsLogDaoImpl extends BaseDAOImpl<SendSmsLogEntity, String> implements SendSmsLogDao {

    @Autowired
    private SendSmsLogEntityMapper mapper;

    @Override
    protected MybatisMapper<SendSmsLogEntity, String> getMapper() {
        return mapper;
    }

    @Override
    public long countByIp(String ip, Date date) {
        return mapper.countByIp(ip, date);
    }

    @Override
    public long countByPhone(String phone, Date date) {
        return mapper.countByPhone(phone, date);
    }

    @Override
    public long countByPhoneAndContent(String phone, String content, Date date) {
        return mapper.countByPhoneAndContent(phone, content, date);
    }

    @Override
    public SendSmsLogEntity findLatestLog(String phone) {
        return mapper.findLatestLog(phone);
    }

    @Override
    public SendSmsLogEntity findLatestLogByIp(String ip) {
        return this.mapper.findLatestLogByIp(ip);
    }
}
