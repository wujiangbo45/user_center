package com.navinfo.opentsp.user.service.param.backend;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import com.navinfo.opentsp.user.service.validator.Product;

/**
 * Created by wupeng on 11/3/15.
 */
public class DeleteProductParam extends BaseTokenParam {

    @Product
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
