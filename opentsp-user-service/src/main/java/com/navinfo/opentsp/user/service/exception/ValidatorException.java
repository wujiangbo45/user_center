package com.navinfo.opentsp.user.service.exception;

import com.navinfo.opentsp.user.service.result.ReturnResult;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-20
 * @modify
 * @copyright Navi Tsp
 */
public class ValidatorException extends OpenTspException {

    public ValidatorException(String message, ReturnResult result){
        super(message, result);
    }

}
