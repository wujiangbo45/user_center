package com.navinfo.opentsp.user.service.valimpl;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-09
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class NotEmptyImplmentor implements ValidatorImplmentor {
    @Override
    public boolean doValidate(Object arg) {
        if(arg == null)
            return false;

        return !"".equals(arg);
    }

    @Override
    public Class supportValidation() {
        return NotEmpty.class;
    }
}
