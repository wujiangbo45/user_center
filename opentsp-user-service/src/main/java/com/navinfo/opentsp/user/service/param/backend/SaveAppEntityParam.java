package com.navinfo.opentsp.user.service.param.backend;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by wupeng on 11/3/15.
 */
public class SaveAppEntityParam extends BaseTokenParam {

    @NotEmpty
    private String thirdType;

    /**
     * 第三方名称
     * TODO create a enum
     */
    @NotEmpty
    private String thirdName;

    @NotEmpty
    private String appid;

    @NotEmpty
    private String appkey;

    @Product
    private String productId;

    public String getThirdType() {
        return thirdType;
    }

    public void setThirdType(String thirdType) {
        this.thirdType = thirdType;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
