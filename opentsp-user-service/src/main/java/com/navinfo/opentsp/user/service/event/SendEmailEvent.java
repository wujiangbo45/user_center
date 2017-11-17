package com.navinfo.opentsp.user.service.event;

import com.navinfo.opentsp.user.common.util.event.AbstractOpentspEvent;
import com.navinfo.opentsp.user.service.param.email.SendEmailParam;
import com.navinfo.opentsp.user.service.result.ResultCode;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
public class SendEmailEvent extends AbstractOpentspEvent<SendEmailParam> {

    private SendEmailParam sendEmailParam;

    private ResultCode resultCode;

    public SendEmailEvent(){}

    public SendEmailEvent(SendEmailParam param){
        this.sendEmailParam = param;
    }

    public SendEmailParam getSendEmailParam() {
        return sendEmailParam;
    }

    public void setSendEmailParam(SendEmailParam sendEmailParam) {
        this.sendEmailParam = sendEmailParam;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public SendEmailParam getData() {
        return sendEmailParam;
    }
}
