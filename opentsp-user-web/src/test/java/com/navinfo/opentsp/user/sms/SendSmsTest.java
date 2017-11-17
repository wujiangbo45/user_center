package com.navinfo.opentsp.user.sms;

import com.navinfo.opentsp.user.common.util.json.JsonUtil;
import com.navinfo.opentsp.user.service.security.EncryptUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-27
 * @modify
 * @copyright Navi Tsp
 */
public class SendSmsTest {

    private static final Logger logger = LoggerFactory.getLogger(SendSmsTest.class);

    private String sendSmsUrl="https://wedrive.mapbar.com/sms/send.do";
//    private String sendSmsUrl="http://localhost:8080/sms/send.do";
    private String productId="opentsp_user";
    private String key="f67919a6edf94945a3845ce73bc9b12a";

    /**
     * 签名的参数名
     */
    private static final String SIGN_PARAM = "user_sign";
    /**
     * 渠道的参数名
     */
    private static final String CHANNEL_PARAM = "sendposition";

    private Random random = new Random();

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void test(){
        sendMessage("18640525668", "您的短信验证码为022347,请勿告诉他人");
    }

    public void sendMessage(String mobile, String content) {
        logger.debug("apply to send message, mobile : " + mobile + ", content : " + content);
        String ip = this.randomIp();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("method","sendIntegralWall");//to specify execute which method in action
            map.put(CHANNEL_PARAM, productId);
            map.put("phone",mobile);
            map.put("title", encodeChinese("短信发送"));
            map.put("content", encodeChinese(content));
            map.put("ip",ip);
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
            String response = restTemplate.getForObject(url, String.class);
            logger.info("send message, response : " + response);

            Map<String, String> result = JsonUtil.fromJson(response, Map.class);
            if(!"200".equals(result.get("code"))){
                throw new RuntimeException("send sms error ! response : " + response);
            }
        } finally {

        }
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

    private String randomIp(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 4; i++){
            sb.append(random.nextInt(255)).append(".");
        }

        return sb.deleteCharAt(sb.length() - 1).toString();
    }

}
