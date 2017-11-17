package com.navinfo.opentsp.user.service.email;

import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import com.navinfo.opentsp.user.service.cache.VerifyCodeService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.event.SendEmailEvent;
import com.navinfo.opentsp.user.service.param.email.SendEmailParam;
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
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class SendEmailListener extends AbstractOpentspListener<SendEmailEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SendEmailListener.class);

    private static final Set<Functions> functionsSet = new HashSet<>();

    @Autowired
    private VerifyCodeService verifyCodeService;

    static {
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
    public void onEvent(SendEmailEvent event) {
        SendEmailParam emailParam = event.getSendEmailParam();

        Functions functions = Functions.valuesOf(emailParam.getType());

        if(!functionsSet.contains(functions)) {
            event.setResultCode(ResultCode.PARAM_ERROR);
            logger.info("error type param !");
            return;
        }

        boolean needCaptcha = false;
        /**
         * 检查图片验证码
         */
        if(needCaptcha) {
            if(StringUtils.isEmpty(emailParam.getCaptcha())) {
                logger.info("captcha was missing !");
                event.setResultCode(ResultCode.BAD_REQUEST);
                return;
            }
            if (!this.verifyCodeService.checkVerifyCode(functions, VerifyTypes.CAPTCHA,
                    emailParam.getProduct(), emailParam.getEmail(), emailParam.getCaptcha(), true)) {
                logger.debug("invalid captcha code !");
                event.setResultCode(ResultCode.VERIFY_CODE_ERROR);
                return;
            }
        }

    }
}
