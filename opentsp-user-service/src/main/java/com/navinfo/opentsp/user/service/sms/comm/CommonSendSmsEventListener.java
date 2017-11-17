package com.navinfo.opentsp.user.service.sms.comm;

import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import com.navinfo.opentsp.user.dal.dao.CommonSendSmsLogDao;
import com.navinfo.opentsp.user.dal.entity.CommonSendSmsLogEntity;
import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.service.event.CommonSendSmsEvent;
import com.navinfo.opentsp.user.service.param.sms.comm.CommonSendSmsParam;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.var.ConfigPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * 此服务限制：
 * 1、每个手机号、每个ip每隔60秒才能发下一条短信
 * 2、每个手机号每天最多发3条相同内容短信
 * 3、每个手机号每天最多发5条短信
 * 4、每个IP每天最多发8条短信
 * 5、每个IP每周最多发20条
 *
 * 此服务调用的短信平台服务限制（图吧科技）：
 * 1、每个手机号每天最多发1条相同内容短信
 * 2、每个手机号每天最多发3条短信
 * 3、每个IP每周最多发20条
 *
 * 此服务调用的短信平台服务限制（趣驾wedrive）：
 * 1、每个手机号每天最多发3条相同内容短信
 * 2、每个手机号每天最多发5条短信
 * 3、每个IP每周最多发20条
 *
 *
 * @author wupeng
 * @version 1.0
 * @date 2016-01-27
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class CommonSendSmsEventListener extends AbstractOpentspListener<CommonSendSmsEvent> {
    private static final Logger logger = LoggerFactory.getLogger(CommonSendSmsEventListener.class);

    @Autowired
    private CommonSendSmsLogDao logDao;
    @Autowired
    private ConfigPropertyService configPropertyService;

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void onEvent(CommonSendSmsEvent event) {
        boolean success = true;
        CommonSendSmsParam param = event.getParam();

        /**
         * 1、每个手机号、每个IP每个60秒才能发下一条
         */
        CommonSendSmsLogEntity latest = this.logDao.findLatestByPhone(param.getPhone(), success);
        if(latest != null &&
                (System.currentTimeMillis() - latest.getSendTime().getTime() < TimeUnit.SECONDS.toMillis(60))) {
            event.setResultCode(ResultCode.SEND_SMS_FREQ);
            logger.info("send sms too frequency ! please try again later !");
            return;
        }

        latest = this.logDao.findLatestByIP(param.getIp(), success);
        if(latest != null &&
                (System.currentTimeMillis() - latest.getSendTime().getTime() < TimeUnit.SECONDS.toMillis(60))) {
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

        long ipCount = this.logDao.countByIp(param.getIp(), calendar.getTime() ,success);
        if(ipCount >= ipLimit) {
            event.setResultCode(ResultCode.SEND_SMS_LIMIT);
            logger.info("send sms reached ip limit !");
            return;
        }

        long contentCount = this.logDao.countByPhoneAndContent(param.getPhone(), param.getContent(), calendar.getTime(), success);
        if(contentCount >= phoneContentLimit) {
            event.setResultCode(ResultCode.SEND_SMS_LIMIT);
            logger.info("send sms reached phone content limit !");
            return;
        }

        long phoneCount = this.logDao.countByPhone(param.getPhone(), calendar.getTime(), success);
        if(phoneCount >= phoneLimit) {
            event.setResultCode(ResultCode.SEND_SMS_LIMIT);
            logger.info("send sms reached phone limit !");
            return;
        }

        event.setResultCode(ResultCode.SUCCESS);
    }
}
