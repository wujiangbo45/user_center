package com.navinfo.opentsp.user.service.param.register;

import com.navinfo.opentsp.user.service.param.BaseParam;
import org.hibernate.validator.constraints.Email;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-29
 * @modify
 * @copyright Navi Tsp
 */
public class CheckEmailParam extends BaseParam {

    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
