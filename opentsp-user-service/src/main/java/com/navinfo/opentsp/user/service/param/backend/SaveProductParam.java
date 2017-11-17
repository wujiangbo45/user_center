package com.navinfo.opentsp.user.service.param.backend;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by wupeng on 11/3/15.
 */
public class SaveProductParam extends BaseTokenParam {
    @NotEmpty
    private String productId;
    @NotEmpty
    private String productName;
    @NotEmpty
    private String description;
    @NotEmpty
    private int status;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
