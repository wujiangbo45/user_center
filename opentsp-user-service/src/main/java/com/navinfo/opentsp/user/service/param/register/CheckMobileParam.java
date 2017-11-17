package com.navinfo.opentsp.user.service.param.register;

import com.navinfo.opentsp.user.service.param.BaseParam;
import com.navinfo.opentsp.user.service.validator.MobilePhone;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-29
 * @modify
 * @copyright Navi Tsp
 */
public class CheckMobileParam extends BaseParam {

    @MobilePhone
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
