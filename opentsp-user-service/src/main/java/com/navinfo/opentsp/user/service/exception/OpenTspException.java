package com.navinfo.opentsp.user.service.exception;

import com.navinfo.opentsp.user.service.result.ResultCodeSupport;
import com.navinfo.opentsp.user.service.result.ReturnResult;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-29
 * @modify
 * @copyright Navi Tsp
 */
public class OpenTspException extends RuntimeException implements ResultCodeSupport {

    private ReturnResult returnResult;

    public OpenTspException(){}

    public OpenTspException(String message, ReturnResult result){
        super(message);
        this.returnResult = result;
    }

    public OpenTspException(String message, Throwable t, ReturnResult result){
        super(message, t);
        this.returnResult = result;
    }

    @Override
    public ReturnResult returnResult() {
        return returnResult;
    }

}
