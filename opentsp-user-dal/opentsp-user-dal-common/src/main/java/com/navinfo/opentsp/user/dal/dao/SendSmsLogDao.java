package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.SendSmsLogEntity;

import java.util.Date;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface SendSmsLogDao extends BaseDAO<SendSmsLogEntity, String> {

    public long countByIp(String ip, Date date);

    public long countByPhone(String phone, Date date);

    public long countByPhoneAndContent(String phone, String content, Date date);

    public SendSmsLogEntity findLatestLog(String phone);

    public SendSmsLogEntity findLatestLogByIp(String ip);

}
