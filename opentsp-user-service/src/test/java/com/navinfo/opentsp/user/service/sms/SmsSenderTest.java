package com.navinfo.opentsp.user.service.sms;

import com.navinfo.opentsp.user.common.util.UtilScan;
import com.navinfo.opentsp.user.dal.entity.SendSmsLogEntity;
import com.navinfo.opentsp.user.dal.mybatis.MybatisScan;
import com.navinfo.opentsp.user.service.ServiceScan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackageClasses = {UtilScan.class, MybatisScan.class, ServiceScan.class})
public class SmsSenderTest{

    @Autowired
    private SmsSender smsSender;

    @Test
    public void testSend(){
        SendSmsLogEntity entity = new SendSmsLogEntity();
        entity.setMobile(15311612804L);
        entity.setResponse("");
        entity.setContent("1231sdfdsf中文测试rret");
        smsSender.sendSms(entity);
    }


}
