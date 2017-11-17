package com.navinfo.opentsp.user.service.sms;

import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import com.navinfo.opentsp.user.dal.dao.SendSmsLogDao;
import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.dal.entity.SendSmsLogEntity;
import com.navinfo.opentsp.user.service.event.SendSmsCountEvent;
import com.navinfo.opentsp.user.service.param.sms.SendSmsParam;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.var.ConfigPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 *
 * <p>
 *     1、每个IP 每天最多发10条
 * </p>
 * <p>
 *     2、每个手机号， 相同内容的短信每天最多接收三次
 * </p>
 * <p>
 *     3、每个手机号， 每天最多接收10条短信
 * </p>
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class SendSmsConstraintsListener  extends AbstractOpentspListener<SendSmsCountEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SendSmsConstraintsListener.class);

    @Autowired
    private SendSmsLogDao logDao;
    @Autowired
    private ConfigPropertyService configPropertyService;

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void onEvent(SendSmsCountEvent event) {
        SendSmsParam param = event.getData();

        SendSmsLogEntity logEntity = this.logDao.findLatestLog(param.getMobile());
        if(logEntity != null &&
                (System.currentTimeMillis() - logEntity.getCreateTime().getTime() < TimeUnit.SECONDS.toMillis(60))) {
            event.setResultCode(ResultCode.SEND_SMS_FREQ);
            logger.info("send sms too frequency ! please try again later !");
            return;
        }

        logEntity = this.logDao.findLatestLogByIp(param.getIp());
        if(logEntity != null &&
                (System.currentTimeMillis() - logEntity.getCreateTime().getTime() < TimeUnit.SECONDS.toMillis(60))) {
            event.setResultCode(ResultCode.SEND_SMS_FREQ);
            logger.info("send sms too frequency ! please try again later !");
            return;
        }

        long ipLimit = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_SMS_IP_MAX, ConfigPropertyEntity.Types.sms));
        long phoneLimit = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_SMS_PHONE_MAX, ConfigPropertyEntity.Types.sms));
        long phoneContentLimit = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_SMS_PHONE_CONTENT_MAX,
                ConfigPropertyEntity.Types.sms));

        /**
         * 获取到今天0点0分0秒的日期
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_YEAR, -1);

        long ipCount = this.logDao.countByIp(param.getIp(), calendar.getTime());
        if(ipCount >= ipLimit) {
            event.setResultCode(ResultCode.SEND_SMS_LIMIT);
            logger.info("send sms reached ip limit !");
            return;
        }

        long contentCount = this.logDao.countByPhoneAndContent(param.getMobile(), param.getContent(), calendar.getTime());
        if(contentCount >= phoneContentLimit) {
            event.setResultCode(ResultCode.SEND_SMS_LIMIT);
            logger.info("send sms reached phone content limit !");
            return;
        }

        long phoneCount = this.logDao.countByPhone(param.getMobile(), calendar.getTime());
        if(phoneCount >= phoneLimit) {
            event.setResultCode(ResultCode.SEND_SMS_LIMIT);
            logger.info("send sms reached phone limit !");
            return;
        }
    }
}
