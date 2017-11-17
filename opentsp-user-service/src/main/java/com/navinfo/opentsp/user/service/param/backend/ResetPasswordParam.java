package com.navinfo.opentsp.user.service.param.backend;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by wupeng on 11/3/15.
 */
public class ResetPasswordParam extends BaseTokenParam {

    //0 - 邮箱， 1 - 手机
    private int type = 0;
    @NotEmpty
    private String userId;
    @NotEmpty
    private String notifier;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotifier() {
        return notifier;
    }

    public void setNotifier(String notifier) {
        this.notifier = notifier;
    }
}
