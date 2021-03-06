package com.navinfo.opentsp.user.service.exception;

import com.navinfo.opentsp.user.service.result.ResultCode;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-30
 * @modify
 * @copyright Navi Tsp
 */
public interface ExceptionHandler<T> {


    public ResultCode handle(T exception);


}
