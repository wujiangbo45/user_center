package com.navinfo.opentsp.user.web.controller;

import com.navinfo.opentsp.user.common.util.event.EventPublisher;
import com.navinfo.opentsp.user.service.email.EmailService;
import com.navinfo.opentsp.user.service.event.SendEmailEvent;
import com.navinfo.opentsp.user.service.param.email.SendEmailParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.web.util.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
@RestController
@RequestMapping("/user")
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private EventPublisher eventPublisher;

    @RequestMapping(value = "/sendEmail", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<String> sendEmail(HttpServletRequest request, @RequestBody SendEmailParam param){
        param.setIp(HttpRequestUtil.getIpAddr(request));

        SendEmailEvent event = new SendEmailEvent(param);
        this.eventPublisher.publishEvent(event);
        if(event.getResultCode() != null && event.getResultCode() != ResultCode.SUCCESS) {
            return CommonResult.newInstance(event.getResultCode(), String.class);
        }

        return this.emailService.sendEmail(param);
    }

}
