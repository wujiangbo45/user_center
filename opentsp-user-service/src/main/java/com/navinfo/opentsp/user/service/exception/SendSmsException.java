package com.navinfo.opentsp.user.service.exception;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-26
 * @modify
 * @copyright Navi Tsp
 */
public class SendSmsException extends RuntimeException {

    private String originalMessage;

    public SendSmsException(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    public SendSmsException(String message, String originalMessage) {
        super(message);
        this.originalMessage = originalMessage;
    }

    public SendSmsException(String message, Throwable cause, String originalMessage) {
        super(message, cause);
        this.originalMessage = originalMessage;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }
}
