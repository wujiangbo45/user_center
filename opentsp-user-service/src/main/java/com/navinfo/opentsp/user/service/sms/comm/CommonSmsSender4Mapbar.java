package com.navinfo.opentsp.user.service.sms.comm;

import com.navinfo.opentsp.user.common.util.http.HttpUtil;
import com.navinfo.opentsp.user.service.enums.SmsChannels;
import com.navinfo.opentsp.user.service.exception.SendSmsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-26
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class CommonSmsSender4Mapbar implements CommonSmsSender {
    private static final Logger logger = LoggerFactory.getLogger(CommonSmsSender4Mapbar.class);

    @Value("${opentsp.sms.old.url}")
    private String oldSendSmsUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SmsChannels channel() {
        return SmsChannels.mapbar;
    }

    @Override
    public String sendSms(String phone, String content, String clientIp) {

        String json = null;

        try {
            String url = new StringBuilder(oldSendSmsUrl).append("?method=sendSMSUCenter2")
                    .append("&modetype=0")
                    .append("&linkUrl=")
                    .append("&sendposition=wapad")
                    .append("&phone=").append(phone)
                    .append("&title=短信发送")
                    .append("&content=").append(URLEncoder.encode(content, "UTF-8"))
//                    .append("&content=").append(content)
                    .append("&pageurl=http%3A%2F%2Fpoi.mapbar.com%2Fbeijing%2FMAPEFWCWYMJNHRYMQIQOS")
                    .append("&userid=")
                    .append("&msgid=")
                    .append("&vericode=")
                    .append("&sign=c8cb7616d25a1aa0414b120d21021142")
                    .append("&ip=").append(clientIp).toString();

//            json = restTemplate.getForObject(url, String.class);
            json = HttpUtil.get(url.toString(), null, "");

            if(StringUtils.isEmpty(json) || !json.contains("短信发送成功")){
                throw new SendSmsException("send sms error ! response : " + json, json);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }


        return json;
    }

    public void setOldSendSmsUrl(String oldSendSmsUrl) {
        this.oldSendSmsUrl = oldSendSmsUrl;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
