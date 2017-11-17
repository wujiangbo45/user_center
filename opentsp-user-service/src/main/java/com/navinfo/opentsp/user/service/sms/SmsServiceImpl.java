package com.navinfo.opentsp.user.service.sms;

import com.navinfo.opentsp.user.common.util.event.EventPublisher;
import com.navinfo.opentsp.user.common.util.file.FileUtil;
import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.dal.entity.SendSmsLogEntity;
import com.navinfo.opentsp.user.service.cache.VerifyCodeService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.event.SendSmsCountEvent;
import com.navinfo.opentsp.user.service.param.sms.SendSmsParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.var.ConfigPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class SmsServiceImpl implements SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Autowired
    private SmsSender smsSender;
    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private ConfigPropertyService configPropertyService;
    private Random random = new Random();

    @Override
    public CommonResult<String> sendSms(SendSmsParam param) {
        /**
         * 短信验证码  失效时间
         */
        Long timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_REGISTER_SMS_TIMEOUT,
                param.getProduct(), ConfigPropertyEntity.Types.register));
        logger.debug("sms code timeout time : {}", timeout);
        /**
         * 检查一下是否已经声称过验证码， 如果已经有了，则使用先前的， 没有则生成一个新的
         */
        String verifyCode = this.verifyCodeService.getVerifyCode(Functions.valuesOf(param.getType()),
                VerifyTypes.SMS, param.getProduct(), param.getMobile());
        logger.debug("previous sms code is : {}", verifyCode);

        if(StringUtils.isEmpty(verifyCode)) {
            logger.debug("previous sms code not found ! will generate new code !");
            int length = Integer.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_SMS_CODE_LENGTH,
                    param.getProduct(), ConfigPropertyEntity.Types.register));
            verifyCode = this.randomCode(length);
            logger.debug("generate new sms verify code : ", verifyCode);
        }

        /**
         * 获取短信内容
         */
        String content = null;
        try {
            content = this.getContent(Functions.valuesOf(param.getType()));
            content = content.replace("${code}", verifyCode);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            return CommonResult.newInstance(ResultCode.SEND_SMS_ERROR, String.class);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
        }

        /**
         * 简单限制一下发送次数， 短信服务有限制，但是这里也简单限制一下
         */
        param.setContent(content);
        SendSmsCountEvent smsEvent = new SendSmsCountEvent(param);
        this.eventPublisher.publishEvent(smsEvent);
        if(smsEvent.getResultCode() != null && smsEvent.getResultCode() != ResultCode.SUCCESS) {
            return new CommonResult<String>().fillResult(smsEvent.getResultCode());
        }

        SendSmsLogEntity entity = new SendSmsLogEntity();
        entity.setClientIp(param.getIp());
        entity.setContent(content);
        entity.setCreateTime(new Date());
        entity.setMobile(Long.valueOf(param.getMobile()));
        entity.setOpProductId(param.getProduct());
        entity.setResponse("");
        entity.setType(param.getType());

        try {
            //发送
            this.smsSender.sendSms(entity);
            //将验证码缓存到redis
            this.verifyCodeService.setVerifyCode(Functions.valuesOf(param.getType()),
                    VerifyTypes.SMS, param.getProduct(), param.getMobile(), verifyCode, timeout);
        } catch (RuntimeException e ) {
            logger.error(e.getMessage(), e);
            return CommonResult.newInstance(ResultCode.SEND_SMS_ERROR, String.class);
        }

        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    @Override
    public boolean notify(String mobile, String template, Map<String, String> attrs) {
        SendSmsLogEntity entity = null;
        try {
            String content= FileUtil.readString("templates/sms/" + template + ".txt");
            content = FileUtil.replace(content, attrs);
            entity = new SendSmsLogEntity();
            entity.setClientIp("127.0.0.1");
            entity.setContent(content);
            entity.setCreateTime(new Date());
            entity.setMobile(Long.valueOf(mobile));
            entity.setOpProductId("_internal_");
            entity.setResponse("");
            entity.setType("_notify_");
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        if (entity != null) {
            this.smsSender.sendSms(entity);
        }

        return true;
    }

    private String getContent(Functions functions) throws FileNotFoundException {
        String name = "templates/sms/" + functions.func() + ".txt";
        String content = FileUtil.readString(name);
        return content;
    }

    public String randomCode(int length){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
