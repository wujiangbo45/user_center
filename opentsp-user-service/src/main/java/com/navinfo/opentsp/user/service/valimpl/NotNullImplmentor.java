package com.navinfo.opentsp.user.service.valimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-09
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class NotNullImplmentor implements ValidatorImplmentor {
    private static final Logger logger = LoggerFactory.getLogger(NotNullImplmentor.class);

    @Override
    public boolean doValidate(Object arg) {
        return arg != null;
    }

    @Override
    public Class supportValidation() {
        return NotNull.class;
    }
}
