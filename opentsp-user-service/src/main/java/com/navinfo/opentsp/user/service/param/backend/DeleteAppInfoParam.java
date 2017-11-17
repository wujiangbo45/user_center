package com.navinfo.opentsp.user.service.param.backend;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-09
 * @modify
 * @copyright Navi Tsp
 */
public class DeleteAppInfoParam extends BaseTokenParam {

    @Product
    private String product;
    @NotEmpty
    private String thirdType;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getThirdType() {
        return thirdType;
    }

    public void setThirdType(String thirdType) {
        this.thirdType = thirdType;
    }
}
