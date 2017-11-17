package com.navinfo.opentsp.user.service.email;

import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import com.navinfo.opentsp.user.dal.dao.SendEmailLogDao;
import com.navinfo.opentsp.user.dal.dao.UserEntityDao;
import com.navinfo.opentsp.user.dal.entity.SendEmailLogEntity;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.event.SendEmailEvent;
import com.navinfo.opentsp.user.service.param.email.SendEmailParam;
import com.navinfo.opentsp.user.service.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 重新发送注册邮件的校验
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class SendEmailResentRegisterListener  extends AbstractOpentspListener<SendEmailEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SendEmailResentRegisterListener.class);

    @Autowired
    private UserEntityDao userEntityDao;

    @Autowired
    private SendEmailLogDao emailDao;

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void onEvent(SendEmailEvent event) {
        SendEmailParam emailParam = event.getSendEmailParam();

        Functions functions = Functions.valuesOf(emailParam.getType());

        if(functions != Functions.resentRegister) {
            logger.info("error type param ! ");
            return;
        }

        SendEmailLogEntity entity = emailDao.findLatestLog(emailParam.getEmail());
        if(entity == null) {
            event.setResultCode(ResultCode.EMAIL_NOT_EXISTS);
            return;
        }

        UserEntity userEntity = this.userEntityDao.findUserByEmail(emailParam.getEmail());
        if(userEntity != null && userEntity.getAccountActived() == UserEntity.ACCOUNT_ACTIVED) {
            event.setResultCode(ResultCode.ACCOUNT_ALREADY_ACTIVED);
            logger.info("account already exists !");
            return;
        }

    }
}
