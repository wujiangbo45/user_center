package com.navinfo.opentsp.user.service.event;

import com.navinfo.opentsp.user.common.util.event.AbstractOpentspEvent;
import com.navinfo.opentsp.user.service.param.sms.comm.CommonSendSmsParam;
import com.navinfo.opentsp.user.service.result.ReturnResult;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-26
 * @modify
 * @copyright Navi Tsp
 */
public class CommonSendSmsEvent extends AbstractOpentspEvent<CommonSendSmsParam> {

    private CommonSendSmsParam param;

    private ReturnResult resultCode;

    public CommonSendSmsEvent() {
    }

    public CommonSendSmsEvent(CommonSendSmsParam param) {
        this.param = param;
    }

    public CommonSendSmsParam getParam() {
        return param;
    }

    public void setParam(CommonSendSmsParam param) {
        this.param = param;
    }

    public ReturnResult getResultCode() {
        return resultCode;
    }

    public void setResultCode(ReturnResult resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public CommonSendSmsParam getData() {
        return param;
    }
}
