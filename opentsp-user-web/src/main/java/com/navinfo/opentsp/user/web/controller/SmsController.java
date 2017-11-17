package com.navinfo.opentsp.user.web.controller;

import com.navinfo.opentsp.user.common.util.event.EventPublisher;
import com.navinfo.opentsp.user.service.event.SendSmsEvent;
import com.navinfo.opentsp.user.service.param.sms.SendSmsParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.sms.SmsService;
import com.navinfo.opentsp.user.web.util.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@RestController
@RequestMapping("/user")
public class SmsController {
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private SmsService smsService;
    @Autowired
    private EventPublisher eventPublisher;

    @RequestMapping(value = "/sendSms", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<String> sendSms(HttpServletRequest request, @RequestBody SendSmsParam param){
        param.setIp(HttpRequestUtil.getIpAddr(request));

        /**
         * 快速登录不要验证码， 这里考虑使用 事件/监听 来实现各种条件校验
         */
        SendSmsEvent smsEvent = new SendSmsEvent(param);
        this.eventPublisher.publishEvent(smsEvent);
        if(smsEvent.getResultCode() != null && smsEvent.getResultCode() != ResultCode.SUCCESS) {
            return new CommonResult<String>().fillResult(smsEvent.getResultCode());
        }

        return smsService.sendSms(param);
    }

}
