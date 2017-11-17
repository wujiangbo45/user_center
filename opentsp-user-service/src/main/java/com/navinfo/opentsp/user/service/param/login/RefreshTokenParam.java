package com.navinfo.opentsp.user.service.param.login;

import com.navinfo.opentsp.user.service.param.DeviceBaseTokenParam;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-15
 * @modify
 * @copyright Navi Tsp
 */
public class RefreshTokenParam extends DeviceBaseTokenParam {

    @NotEmpty
    private String loginName;
    private String token;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
