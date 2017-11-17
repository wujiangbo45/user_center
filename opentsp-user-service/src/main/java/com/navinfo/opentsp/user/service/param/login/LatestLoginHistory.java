package com.navinfo.opentsp.user.service.param.login;

import com.navinfo.opentsp.user.service.param.BaseParam;
import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import com.navinfo.opentsp.user.service.param.DeviceBaseTokenParam;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-18
 * @modify
 * @copyright Navi Tsp
 */
public class LatestLoginHistory extends BaseParam{

    @NotEmpty
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
