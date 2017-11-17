package com.navinfo.opentsp.user.service.session;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-14
 * @modify
 * @copyright Navi Tsp
 */
public class InvalidTokenException extends IllegalArgumentException {

    public InvalidTokenException(String message){
        super(message);
    }


    public InvalidTokenException(String message, Throwable throwable){
        super(message, throwable);
    }

}
