package com.navinfo.opentsp.user.service.valimpl;

import com.navinfo.opentsp.user.service.validator.MobilePhone;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-13
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class MobilePhoneImplementor implements ValidatorImplmentor {
    //TODO match all phone formats on the world
    public static final String phone_pattern = "(\\d|\\+)\\d{8,15}";

    @Override
    public boolean doValidate(Object arg) {
        String phone = String.valueOf(arg);
        if(StringUtils.isEmpty(phone)) {
            return false;
        }

        return phone.matches(phone_pattern);
    }

    @Override
    public Class supportValidation() {
        return MobilePhone.class;
    }

    public static void main(String[] args) {
        System.out.println("+8615812345862".matches(phone_pattern));
    }
}
