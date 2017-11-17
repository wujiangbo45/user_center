package com.navinfo.opentsp.user.service.sms;

import com.navinfo.opentsp.user.service.param.sms.SendSmsParam;
import com.navinfo.opentsp.user.service.result.CommonResult;

import java.util.Map;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public interface SmsService {

    public CommonResult<String> sendSms(SendSmsParam param);

    public boolean notify(String mobile, String template, Map<String, String> attrs);

}
