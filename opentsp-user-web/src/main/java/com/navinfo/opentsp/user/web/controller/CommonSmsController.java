package com.navinfo.opentsp.user.web.controller;

import com.navinfo.opentsp.user.common.util.event.EventPublisher;
import com.navinfo.opentsp.user.dal.dao.CommonSendSmsLogDao;
import com.navinfo.opentsp.user.dal.entity.CommonSendSmsLogEntity;
import com.navinfo.opentsp.user.service.cache.CacheService;
import com.navinfo.opentsp.user.service.enums.SmsChannels;
import com.navinfo.opentsp.user.service.event.CommonSendSmsEvent;
import com.navinfo.opentsp.user.service.exception.SendSmsException;
import com.navinfo.opentsp.user.service.param.sms.comm.CommonSendSmsParam;
import com.navinfo.opentsp.user.service.param.sms.comm.CommonSmsParam;
import com.navinfo.opentsp.user.service.param.sms.comm.VerifySmsCodeParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.sms.comm.CommonSmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * 这个类应该好好分层次的， 分出来一个service， 不过我懒得这么做了， 以后谁维护看这不爽就拆了吧
 *
 * @author wupeng
 * @version 1.0
 * @date 2016-01-26
 * @modify
 * @copyright Navi Tsp
 */
@RestController
@RequestMapping("/comm/sms")
public class CommonSmsController {
    private static final Logger logger = LoggerFactory.getLogger(CommonSmsController.class);

    @Value("${comm.sms.length}")
    private long smsCodeLength;

    @Value("${comm.sms.timeout}")
    private long smsTimeout;

    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private List<CommonSmsSender> smsSenders;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CommonSendSmsLogDao logDao;

    private Random random = new Random();

    @RequestMapping(value = "/send", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    public CommonResult<String> sendSms(@RequestBody CommonSendSmsParam sendSmsParam) {
        /**
         * 检查短信渠道是不是填对了
         */
        SmsChannels channels = null;
        try {
            channels = SmsChannels.valueOf(sendSmsParam.getSmsChannel());
            if (channels == null)
                throw new IllegalArgumentException("Illegal argument for smsChannel : " + sendSmsParam.getSmsChannel());
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
        }

        /**
         * 发事件，检查发送次数限制
         */
        CommonSendSmsEvent event = new CommonSendSmsEvent(sendSmsParam);
        eventPublisher.publishEvent(event);
        if (event.getResultCode() != ResultCode.SUCCESS) {
            return CommonResult.newInstance(event.getResultCode(), String.class);
        }

        /**
         * 发送短信内容，验证码
         */
        String content = sendSmsParam.getContent();
        String code = this.randomSmsCode(sendSmsParam);
        content = content.replace("#{code}", code);
        logger.info("send sms content : {}", content);

        /**
         * 发送
         */
        try {
            String response = null;
            for (CommonSmsSender sender : smsSenders) {
                if (sender.channel() == channels) {
                    response = sender.sendSms(sendSmsParam.getPhone(), content, sendSmsParam.getIp());
                    break;
                }
            }
            this.setCache(sendSmsParam, code);
            this.saveLog(sendSmsParam, code, response, true);
        } catch (SendSmsException e) {
            logger.error(e.getMessage(), e);
            this.saveLog(sendSmsParam, code, e.getOriginalMessage(), false);
            return CommonResult.newInstance(ResultCode.SEND_SMS_ERROR, String.class).setData(e.getOriginalMessage());
        }

        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    private void saveLog(CommonSendSmsParam param, String code, String response, boolean success) {
        CommonSendSmsLogEntity entity = new CommonSendSmsLogEntity();
        entity.setIp(param.getIp());
        entity.setContent(param.getContent());
        entity.setKey(param.getKey());
        entity.setPhone(param.getPhone());
        entity.setProduct(param.getProduct());
        entity.setSendTime(new Date());
        entity.setSmsChannel(param.getSmsChannel());
        entity.setVerifyCode(code);
        entity.setResponse(response);
        entity.setSuccess(success);
        logDao.save(entity);
    }

    private String key(CommonSmsParam param) {
        return new StringBuilder("comm_code.")
                .append(param.getProduct()).append(".")
                .append(param.getSmsChannel()).append(".")
                .append(param.getPhone()).append(".")
                .append(param.getKey()).toString();
    }

    private void setCache(CommonSmsParam param, String code) {
        String key = key(param);
        cacheService.set(key, code, smsTimeout, TimeUnit.MINUTES);
    }

    private String getCode(CommonSmsParam param) {
        String key = key(param);
        return (String) cacheService.get(key);
    }

    private String randomSmsCode(CommonSmsParam param) {
        String code = this.getCode(param);
        if (!StringUtils.isEmpty(code)) {
            return code;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < smsCodeLength; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }


    @RequestMapping(value = "/verify", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    public CommonResult<String> sendSms(@RequestBody VerifySmsCodeParam verifySmsCodeParam) {
        /**
         * 检查短信渠道是不是填对了
         */
        SmsChannels channels = null;
        try {
            channels = SmsChannels.valueOf(verifySmsCodeParam.getSmsChannel());
            if (channels == null)
                throw new IllegalArgumentException("Illegal argument for smsChannel : " + verifySmsCodeParam.getSmsChannel());
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
        }

        String code = this.getCode(verifySmsCodeParam);
        if (StringUtils.isEmpty(code)) {
            return CommonResult.newInstance(ResultCode.VERIFY_CODE_EXPIRED, String.class);
        }

        if (code.equals(verifySmsCodeParam.getVerifyCode())) {
            if (verifySmsCodeParam.getExpire() == 1) {
                cacheService.delete(this.key(verifySmsCodeParam));
            }
            return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
        } else {
            return CommonResult.newInstance(ResultCode.VERIFY_CODE_ERROR, String.class);
        }
    }

}
