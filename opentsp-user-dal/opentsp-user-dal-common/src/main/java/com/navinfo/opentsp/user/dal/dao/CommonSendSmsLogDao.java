package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.CommonSendSmsLogEntity;

import java.util.Date;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-26
 * @modify
 * @copyright Navi Tsp
 */
public interface CommonSendSmsLogDao extends BaseDAO<CommonSendSmsLogEntity, String> {

    public long countByIp(String ip, Date date, boolean success);

    public long countByPhone(String phone, Date date, boolean success);

    public CommonSendSmsLogEntity findLatestByPhone(String phone, boolean success);

    public CommonSendSmsLogEntity findLatestByIP(String ip, boolean success);

    public long countByPhoneAndContent(String phone, String content, Date date, boolean success);

}
