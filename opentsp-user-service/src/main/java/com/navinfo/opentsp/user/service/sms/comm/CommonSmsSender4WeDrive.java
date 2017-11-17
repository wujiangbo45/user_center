package com.navinfo.opentsp.user.service.sms.comm;

import com.navinfo.opentsp.user.common.util.json.JsonUtil;
import com.navinfo.opentsp.user.service.enums.SmsChannels;
import com.navinfo.opentsp.user.service.exception.SendSmsException;
import com.navinfo.opentsp.user.service.security.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-26
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class CommonSmsSender4WeDrive implements CommonSmsSender {
    private static final Logger logger = LoggerFactory.getLogger(CommonSmsSender4WeDrive.class);

    @Value("${opentsp.sms.url}")
    private String sendSmsUrl;

    @Value("${opentsp.sms.product.id}")
    private String productId;

    @Value("${opentsp.sms.product.key}")
    private String key;

    /**
     * 签名的参数名
     */
    private static final String SIGN_PARAM = "user_sign";
    /**
     * 渠道的参数名
     */
    private static final String CHANNEL_PARAM = "sendposition";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SmsChannels channel() {
        return SmsChannels.wedrive;
    }

    @Override
    public String sendSms(String phone, String content, String clientIp) {
        Assert.hasText(clientIp, "clientIp can not be empty !");
        String response = null;

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("method","sendIntegralWall");//to specify execute which method in action
            map.put(CHANNEL_PARAM, productId);
            map.put("phone", phone);
            map.put("title", encodeChinese("短信发送"));
            map.put("content", encodeChinese(content));
            map.put("ip", clientIp);
            map.put("timestamp", System.currentTimeMillis());
            map.put(SIGN_PARAM, this.sign(map));

            StringBuilder sb = new StringBuilder(sendSmsUrl).append("?");
            for(Map.Entry<String, Object> entry : map.entrySet()){
                sb.append(entry.getKey()).append("=")
                        .append(entry.getValue() == null ? "" : String.valueOf(entry.getValue()))
                        .append("&");
            }

            sb.deleteCharAt(sb.length() - 1);// delete last char '&'

            String url = sb.toString();
            logger.debug("send message url : " + url);
            response = restTemplate.getForObject(url, String.class);
            logger.info("send message, response : " + response);

            Map<String, String> result = JsonUtil.fromJson(response, Map.class);
            if(!"200".equals(result.get("code"))){
                throw new SendSmsException("send sms error ! response : " + response, response);
            }
        } finally {

        }

        return response;
    }

    private String encodeChinese(String message){
        try {
            return URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }

        return message;
    }

    /**
     * generate sign info, need 3 steps
     * 1, sort all param names by nature order (exclude SIGN_PARAM)
     * 2, spell params and values with &. eg. name=admin&password=mhy
     * 3, hmac-sha1 encrypt with specified key
     *
     *
     * @param params
     * @return
     */
    public String sign(Map<String, Object> params){
        if(params == null || params.size() == 0){
            return null;
        }

        Object channel = String.valueOf(params.get(CHANNEL_PARAM));
        if(channel == null)
            return null;

        params.remove(SIGN_PARAM);
        List<String> paramNames = new ArrayList<>(params.keySet());
        Collections.sort(paramNames);//step 1,sort param names by nature order

        StringBuilder sb = new StringBuilder();
        for(String paramName : paramNames){//step 2
            Object obj = params.get(paramName);
            sb.append(paramName).append("=").append(obj == null ? "" : String.valueOf(obj)).append("&");
        }

        if(sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);

        return EncryptUtil.HMAC_SHA1(sb.toString(), key);//step 3
    }

    public void setSendSmsUrl(String sendSmsUrl) {
        this.sendSmsUrl = sendSmsUrl;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
