package com.navinfo.opentsp.user.service.sms.comm;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class CommonSmsSender4MapbarTest {

    @Test
    public void testSend() {
        CommonSmsSender4Mapbar sender = new CommonSmsSender4Mapbar();
        sender.setRestTemplate(new RestTemplate());
        sender.setOldSendSmsUrl("http://send.sms.mapbar.com/send/send.do");
        String msg = sender.sendSms("15311612804", "你的验1证12q3码是#{code}, 赶紧填写!30分钟内有效1!", "127.0.0.1");
        System.out.println(msg);
    }

    @Test
    public void testSendWeDrive() {
        CommonSmsSender4WeDrive sender = new CommonSmsSender4WeDrive();
        sender.setSendSmsUrl("https://wedrive.mapbar.com/sms/send.do");
        sender.setRestTemplate(new RestTemplate());
        sender.setKey("f67919a6edf94945a3845ce73bc9b12a");
        sender.setProductId("opentsp_user");
        String msg = sender.sendSms("15311612804", "你的验证12q3码是#{code}, 赶紧填写!30分钟内有效1!", "127.0.0.1");
        System.out.println(msg);
    }

}