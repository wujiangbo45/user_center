package com.navinfo.opentsp.user.service.sms.comm;

import com.navinfo.opentsp.user.service.enums.SmsChannels;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-26
 * @modify
 * @copyright Navi Tsp
 */
public interface CommonSmsSender {

    SmsChannels channel();

    String sendSms(String phone, String content, String clientIp);

}
