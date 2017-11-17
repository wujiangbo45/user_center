package com.navinfo.opentsp.user.service.resultdto.login;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-04-29
 * @modify
 * @copyright Navi Tsp
 */
public class WexinResult {

    private int errcode;
    private String errmsg;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
