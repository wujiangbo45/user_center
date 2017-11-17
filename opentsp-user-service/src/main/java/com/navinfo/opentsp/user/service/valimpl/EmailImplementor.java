package com.navinfo.opentsp.user.service.valimpl;

import org.hibernate.validator.constraints.Email;
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
public class EmailImplementor implements ValidatorImplmentor {

    public static final String email_pattern = "([a-zA-Z0-9]|_)([a-zA-Z0-9]|_|-)+@\\S{2,6}\\.[a-zA-Z0-9\\\\.]*[a-zA-Z0-9]";

    @Override
    public boolean doValidate(Object arg) {
        String email = String.valueOf(arg);
        if(StringUtils.isEmpty(email)) {
            return false;
        }

        return email.matches(email_pattern);
    }

    @Override
    public Class supportValidation() {
        return Email.class;
    }

    public static void main(String[] args) {
        System.out.println("36459-3133@dfsdf.com.cn".matches(email_pattern));
    }
}
