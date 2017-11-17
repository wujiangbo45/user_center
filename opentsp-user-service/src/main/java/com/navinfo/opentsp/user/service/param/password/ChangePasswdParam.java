package com.navinfo.opentsp.user.service.param.password;

import com.navinfo.opentsp.user.service.param.DeviceBaseTokenParam;
import com.navinfo.opentsp.user.service.validator.Password;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-20
 * @modify
 * @copyright Navi Tsp
 */
public class ChangePasswdParam extends DeviceBaseTokenParam {
    @NotEmpty
    private String oldPassword;
    @Password
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
