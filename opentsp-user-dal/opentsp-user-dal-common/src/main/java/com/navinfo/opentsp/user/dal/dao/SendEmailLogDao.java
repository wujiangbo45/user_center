package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.SendEmailLogEntity;

import java.util.Date;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface SendEmailLogDao extends BaseDAO<SendEmailLogEntity, String> {

    public long countByIp(String ip, Date date);

    public long countByEmail(String email, Date date);

    public SendEmailLogEntity findLatestLog(String email);

    public SendEmailLogEntity findLatestLogByIp(String ip);
}
