package com.navinfo.opentsp.user.service.param.login;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016-02-22
 * @modify
 * @copyright Navi Tsp
 */
public class SSOValidateTokenParam extends BaseTokenParam {

    private String serverUrl;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
