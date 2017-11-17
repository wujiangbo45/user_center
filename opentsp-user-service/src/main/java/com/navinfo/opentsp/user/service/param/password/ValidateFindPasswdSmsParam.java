package com.navinfo.opentsp.user.service.param.password;

import com.navinfo.opentsp.user.service.param.BaseParam;
import com.navinfo.opentsp.user.service.validator.MobilePhone;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
public class ValidateFindPasswdSmsParam extends BaseParam{
    @MobilePhone
    private String mobile;
    @NotEmpty
    private String smsCode;
    @Product
    private String product;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
