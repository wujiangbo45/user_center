package com.navinfo.opentsp.user.service.event;

import com.navinfo.opentsp.user.common.util.event.AbstractOpentspEvent;
import com.navinfo.opentsp.user.service.param.sms.SendSmsParam;
import com.navinfo.opentsp.user.service.result.ResultCode;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */
public class SendSmsEvent extends AbstractOpentspEvent<SendSmsParam> {

    private SendSmsParam sendSmsParam;

    /**
     * 如果各个监听有什么返回值 直接设置此属性
     */
    private ResultCode resultCode;

    public SendSmsEvent() {
    }

    public SendSmsEvent(SendSmsParam sendSmsParam) {
        this.sendSmsParam = sendSmsParam;
    }

    @Override
    public SendSmsParam getData() {
        return this.sendSmsParam;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
