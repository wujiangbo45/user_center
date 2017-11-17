package com.navinfo.opentsp.user.service.email;

import com.navinfo.opentsp.user.common.util.event.EventPublisher;
import com.navinfo.opentsp.user.common.util.json.JsonUtil;
import com.navinfo.opentsp.user.common.util.string.HexUtils;
import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.dal.entity.SendEmailLogEntity;
import com.navinfo.opentsp.user.service.cache.VerifyCodeService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.event.SendEmailCountEvent;
import com.navinfo.opentsp.user.service.param.email.SendEmailParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.security.AES;
import com.navinfo.opentsp.user.service.security.EncryptUtil;
import com.navinfo.opentsp.user.service.security.Md5;
import com.navinfo.opentsp.user.service.var.ConfigPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-12
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final char[] RANDOM_CODES = {
            '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L'
            ,'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
    };

    @Autowired
    private EmailSender emailSender;
    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private ConfigPropertyService configPropertyService;
    @Autowired
    private EventPublisher eventPublisher;

    private Random random = new Random();

    @Override
    public CommonResult<String> sendEmail(SendEmailParam emailParam) {
        Functions functions = Functions.valuesOf(emailParam.getType());

        String verifyCode = this.verifyCodeService.getVerifyCode(functions, VerifyTypes.EMAIL,
                emailParam.getProduct(), emailParam.getEmail());

        logger.debug("found previous email code : {}", verifyCode);
        if(StringUtils.isEmpty(verifyCode)) {
            verifyCode = salt();
            logger.debug("can not found previous email code , create a new one : {}", verifyCode);
        }

        SendEmailCountEvent event = new SendEmailCountEvent(emailParam);
        this.eventPublisher.publishEvent(event);
        if(event.getResultCode() != null && event.getResultCode() != ResultCode.SUCCESS) {
            return CommonResult.newInstance(event.getResultCode(), String.class);
        }

        Date date = new Date();
        SendEmailLogEntity logEntity = new SendEmailLogEntity();
        logEntity.setEmail(emailParam.getEmail());
        logEntity.setClientIp(emailParam.getIp());
        logEntity.setCreateTime(date);
        logEntity.setOpProductId(emailParam.getProduct());
        logEntity.setType(emailParam.getType());
        logEntity.setSubject(functions.emailSubject());

        try {
            Map<String, String> map = this.emailParams(emailParam);
            map.put("code", verifyCode);
            this.emailSender.sendEmail(functions.func(), map, logEntity);

            Long timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_REGISTER_EMAIL_TIMEOUT,
                    emailParam.getProduct(), ConfigPropertyEntity.Types.register));

            this.verifyCodeService.setVerifyCode(functions, VerifyTypes.EMAIL, emailParam.getProduct(), emailParam.getEmail(), verifyCode, timeout);
            return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
        } catch (RuntimeException e ) {
            logger.error(e.getMessage(), e);
            return new CommonResult<String>().fillResult(ResultCode.SEND_EMAIL_ERROR);
        }
    }

    @Override
    public boolean notify(String email, String subject, String template, Map<String, String> attrs) {
        Date date = new Date();
        SendEmailLogEntity logEntity = new SendEmailLogEntity();
        logEntity.setEmail(email);
        logEntity.setClientIp("127.0.0.1");
        logEntity.setCreateTime(date);
        logEntity.setOpProductId("_internal_");
        logEntity.setType("_notify_");
        logEntity.setSubject(subject);
        this.emailSender.sendEmail(template, attrs, logEntity);
        return true;
    }

    public Map<String, String> emailParams(SendEmailParam emailParam){
        /**
         * 获取到 配置的 AES 的key
         */
        String aesKey = this.configPropertyService.getValue(ConfigPropertyService.PROP_SIMPLE_AES_KEY, ConfigPropertyEntity.Types.system);

        StringBuilder url = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        try {
            if(!StringUtils.isEmpty(emailParam.getRedirectUrl())) {
                url.append(emailParam.getRedirectUrl());
                if(emailParam.getCreateTime() == 0L) {
                    emailParam.setCreateTime(System.currentTimeMillis());
                }
                String param = JsonUtil.toJson(emailParam);
                /**
                 * 将 SendEmailParam 加密成一个串, 拼凑到url里面
                 */
//            param = Base64Utils.encodeToString(AES.encode(param, EncryptUtil.HMAC_SHA1(aesKey, Md5.md5(aesKey))));
                param = HexUtils.toHexString(AES.encode(param, EncryptUtil.HMAC_SHA1(aesKey, Md5.md5(aesKey))));
                logger.debug("encrypt param string : {}", param);
                param = URLEncoder.encode(param, "UTF-8");
                url.append("?q=").append(param);
                logger.debug("active account by email url : {}", url.toString());
            }

            Date date = new Date();
            map.put("to", emailParam.getEmail());
            map.put("time", new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(date));
            map.put("date", new SimpleDateFormat("yyyy年MM月dd日").format(date));
            map.put("hlink", url.toString());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }


    private String salt(){
        StringBuilder sb = new StringBuilder();
        int max = RANDOM_CODES.length;
        for (int i = 0; i < 6; i++) {
            char ch = RANDOM_CODES[random.nextInt(max)];
            if (random.nextInt(256) % 2 == 0) {
                sb.append(String.valueOf(ch).toLowerCase());
            } else {
               sb.append(String.valueOf(ch).toUpperCase());
            }
        }

        return sb.toString();
    }

}
