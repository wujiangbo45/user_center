package com.navinfo.opentsp.user.service.param.setting;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wanliang
 * @version 1.0
 * @date 2015/10/20
 * @modify
 * @copyright Navi Tsp
 */
public class UnbindThriatformParam extends BaseTokenParam {
    @NotEmpty
    private String type;
    @Product
    private String product;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
