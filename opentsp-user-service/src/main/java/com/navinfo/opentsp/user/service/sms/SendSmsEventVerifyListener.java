package com.navinfo.opentsp.user.service.sms;

import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import com.navinfo.opentsp.user.dal.dao.UserEntityDao;
import com.navinfo.opentsp.user.service.cache.VerifyCodeService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.event.SendSmsEvent;
import com.navinfo.opentsp.user.service.param.sms.SendSmsParam;
import com.navinfo.opentsp.user.service.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class SendSmsEventVerifyListener  extends AbstractOpentspListener<SendSmsEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SendSmsEventVerifyListener.class);

    private static final Set<Functions> functionsSet = new HashSet<>();

    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private UserEntityDao userEntityDao;

    static {
        functionsSet.add(Functions.QUIK_LOGIN);
        functionsSet.add(Functions.REGISTER);
        functionsSet.add(Functions.resentRegister);
        functionsSet.add(Functions.resent_bind);
        functionsSet.add(Functions.resent_changeBind);
        functionsSet.add(Functions.resent_findPassword);
        functionsSet.add(Functions.resent_unbind);
        functionsSet.add(Functions.FIND_PASSWORD);
    }

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
        } catch (IllegalArgumentException e) {//这个错误说明type参数不对，没有匹配到枚举
            logger.error(e.getMessage(), e);
            event.setResultCode(ResultCode.PARAM_ERROR);
            return;
        }

        boolean needCaptcha = false;

        if(!functionsSet.contains(functions)) {
            logger.info("error type param !");
            event.setResultCode(ResultCode.PARAM_ERROR);
            return;
        }

        if(functions == Functions.REGISTER) {
            needCaptcha = true;
        }

        if(needCaptcha) {
            if(StringUtils.isEmpty(param.getCaptcha())) {
                logger.info("captcha is empty !");
                event.setResultCode(ResultCode.BAD_REQUEST);
                return;
            }

            /**
             * 检查图形验证码， 图形验证码使用一次即失效
             */
            if(!verifyCodeService.checkVerifyCode(functions, VerifyTypes.CAPTCHA,
                    param.getProduct(), param.getMobile(), param.getCaptcha(), true)) {
                event.setResultCode(ResultCode.VERIFY_CODE_ERROR);
                return;
            }
        }

        if(functions == Functions.REGISTER) {
            if(userEntityDao.findUserByMobile(param.getMobile()) != null) {
                event.setResultCode(ResultCode.PHONE_ALREADY_EXISTS);
                return;
            }
        }

    }


}
