package com.navinfo.opentsp.user.service.exception;

import com.navinfo.opentsp.user.service.result.ReturnResult;

/**
 *
 * 数据过期
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-29
 * @modify
 * @copyright Navi Tsp
 */
public class DataExpiredException extends OpenTspException {

    public DataExpiredException(String message, ReturnResult result) {
        super(message, result);
    }

}
