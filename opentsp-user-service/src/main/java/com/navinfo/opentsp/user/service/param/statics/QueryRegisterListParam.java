package com.navinfo.opentsp.user.service.param.statics;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-02
 * @modify
 * @copyright Navi Tsp
 */
public class QueryRegisterListParam extends BaseTokenParam {

    private String product;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
