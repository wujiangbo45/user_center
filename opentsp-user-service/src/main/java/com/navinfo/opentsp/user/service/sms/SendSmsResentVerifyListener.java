package com.navinfo.opentsp.user.service.sms;

import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import com.navinfo.opentsp.user.dal.dao.SendSmsLogDao;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.event.SendSmsEvent;
import com.navinfo.opentsp.user.service.param.sms.SendSmsParam;
import com.navinfo.opentsp.user.service.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * 重新发送注册短信的验证
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
@Deprecated
//@Component
public class SendSmsResentVerifyListener extends AbstractOpentspListener<SendSmsEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SendSmsResentVerifyListener.class);

    @Autowired
    private SendSmsLogDao logDao;

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void onEvent(SendSmsEvent event) {
        SendSmsParam param = event.getData();

        Functions functions = null;

        try {
            functions = Functions.valuesOf(param.getType());
            if(functions != Functions.resentRegister) {
                return;
            }
        } catch (IllegalArgumentException e) {//这个错误说明type参数不对，没有匹配到枚举
            logger.error(e.getMessage(), e);
            event.setResultCode(ResultCode.PARAM_ERROR);
            return;
        }

    }
}
