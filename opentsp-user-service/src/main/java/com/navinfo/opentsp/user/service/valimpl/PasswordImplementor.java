package com.navinfo.opentsp.user.service.valimpl;

import com.navinfo.opentsp.user.service.validator.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class PasswordImplementor implements ValidatorImplmentor {
    private static final Logger logger = LoggerFactory.getLogger(PasswordImplementor.class);

    @Override
    public boolean doValidate(Object arg) {
        if(arg instanceof String) {
            String password = (String) arg;
            if(password != null && !"".equals(password)) {
                int len = password.length();
                if(len >= 6 && len <= 20) {
                    return true;
                }
            }
        }

        logger.debug("invalid password : {}", arg);

        return false;
    }

    @Override
    public Class supportValidation() {
        return Password.class;
    }
}
