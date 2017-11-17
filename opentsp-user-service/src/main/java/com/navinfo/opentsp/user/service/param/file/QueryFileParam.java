package com.navinfo.opentsp.user.service.param.file;

import com.navinfo.opentsp.user.service.param.BaseParam;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-12-25
 * @modify
 * @copyright Navi Tsp
 */
public class QueryFileParam extends BaseParam {
    @NotEmpty
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
