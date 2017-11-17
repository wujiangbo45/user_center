package com.navinfo.opentsp.user.service.param.setting;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import com.navinfo.opentsp.user.service.validator.Product;

/**
 * @author wanliang
 * @version 1.0
 * @date 2015/10/19
 * @modify
 * @copyright Navi Tsp
 */
public class UserDetailParam extends BaseTokenParam {

    @Product
    private  String product;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
