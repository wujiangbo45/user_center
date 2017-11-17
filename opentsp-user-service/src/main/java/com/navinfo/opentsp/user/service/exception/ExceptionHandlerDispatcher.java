package com.navinfo.opentsp.user.service.exception;

import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.result.ReturnResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-30
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class ExceptionHandlerDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerDispatcher.class);

    public ReturnResult handlerException(Exception e ) {
        ReturnResult returnResult = null;
        try {
            throw e;
        } catch (OpenTspException e1) {
            returnResult = e1.returnResult();
        } catch (Exception e1 ) {
            returnResult = ResultCode.SERVER_ERROR;
            logger.error(e.getMessage(), e);
        }

        if(returnResult == null) {
            returnResult = ResultCode.SERVER_ERROR;
        }

        return returnResult;
    }


}
