package com.navinfo.opentsp.user.service.sms;

import com.navinfo.opentsp.user.dal.dao.SendSmsLogDao;
import com.navinfo.opentsp.user.dal.entity.SendSmsLogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-13
 * @modify
 * @copyright Navi Tsp
 */
@Service
@Profile("dev")
public class SmsSenderImpl4Dev implements SmsSender {
    private static final Logger logger = LoggerFactory.getLogger(SmsSenderImpl4Dev.class);

    @Autowired
    private SendSmsLogDao smsLogDao;

    @Override
    public void sendSms(SendSmsLogEntity entity) {
        this.sendSms(new String[]{String.valueOf(entity.getMobile())}, entity);
    }

    @Override
    public void sendSms(String[] phones, SendSmsLogEntity entity) {
        entity.setResponse("success[test]");
        smsLogDao.save(entity);
        logger.info("send sms success ! content : {}", entity.getContent());
    }
}
