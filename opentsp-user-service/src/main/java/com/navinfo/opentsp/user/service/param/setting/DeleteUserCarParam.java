package com.navinfo.opentsp.user.service.param.setting;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-05
 * @modify
 * @copyright Navi Tsp
 */
public class DeleteUserCarParam extends BaseTokenParam {

    @NotEmpty
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
