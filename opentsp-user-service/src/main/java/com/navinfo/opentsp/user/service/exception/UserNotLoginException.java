package com.navinfo.opentsp.user.service.exception;

import com.navinfo.opentsp.user.service.result.ResultCode;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-20
 * @modify
 * @copyright Navi Tsp
 */
public class UserNotLoginException extends ValidatorException {

    public UserNotLoginException(String message){
        super(message, ResultCode.NOT_LOGIN);
    }

}
