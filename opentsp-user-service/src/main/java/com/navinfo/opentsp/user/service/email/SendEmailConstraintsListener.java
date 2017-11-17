package com.navinfo.opentsp.user.service.email;

import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import com.navinfo.opentsp.user.dal.dao.SendEmailLogDao;
import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.dal.entity.SendEmailLogEntity;
import com.navinfo.opentsp.user.service.event.SendEmailCountEvent;
import com.navinfo.opentsp.user.service.param.email.SendEmailParam;
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
 * 发送次数限制
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class SendEmailConstraintsListener  extends AbstractOpentspListener<SendEmailCountEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SendEmailConstraintsListener.class);

    @Autowired
    private SendEmailLogDao logDao;
    @Autowired
    private ConfigPropertyService configPropertyService;

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void onEvent(SendEmailCountEvent event) {
        SendEmailParam param = event.getData();

        SendEmailLogEntity latest = this.logDao.findLatestLog(param.getEmail());
        if(latest != null) {
            if(System.currentTimeMillis() - latest.getCreateTime().getTime() < TimeUnit.SECONDS.toMillis(60)) {//如果时间间隔不超过60秒
                event.setResultCode(ResultCode.SEND_EMAIL_FREQ);
                logger.info("send email to frequency ! ");
                return;
            }
        }

        latest = this.logDao.findLatestLogByIp(param.getIp());
        if(latest != null) {
            if(System.currentTimeMillis() - latest.getCreateTime().getTime() < TimeUnit.SECONDS.toMillis(60)) {//如果时间间隔不超过60秒
                event.setResultCode(ResultCode.SEND_EMAIL_FREQ);
                logger.info("send email to frequency ! ");
                return;
            }
        }

        long ipLimit = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_SMS_IP_MAX, ConfigPropertyEntity.Types.sms));
        long phoneLimit = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_SMS_PHONE_MAX, ConfigPropertyEntity.Types.sms));

        /**
         * 获取到今天0点0分0秒的日期
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_YEAR, -1);

        long ipcount = this.logDao.countByIp(param.getIp(), calendar.getTime());
        if(ipcount >= ipLimit) {
            event.setResultCode(ResultCode.SEND_EMAIL_LIMIT);
            logger.info("send email reached ip limit !");
            return;
        }

        long phoneCount = this.logDao.countByEmail(param.getEmail(), calendar.getTime());
        if(phoneCount >= phoneLimit) {
            event.setResultCode(ResultCode.SEND_EMAIL_LIMIT);
            logger.info("send email reached email limit !");
            return;
        }

    }
}
