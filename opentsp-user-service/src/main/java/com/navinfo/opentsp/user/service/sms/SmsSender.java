package com.navinfo.opentsp.user.service.sms;

import com.navinfo.opentsp.user.dal.entity.SendSmsLogEntity;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public interface SmsSender {

    public void sendSms(SendSmsLogEntity entity);

    public void sendSms(String[] phones, SendSmsLogEntity entity);

}
