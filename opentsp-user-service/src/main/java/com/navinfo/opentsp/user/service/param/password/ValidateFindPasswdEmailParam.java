package com.navinfo.opentsp.user.service.param.password;

import com.navinfo.opentsp.user.service.param.BaseParam;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
public class ValidateFindPasswdEmailParam extends BaseParam{
    @Email
    private String email;
    @NotEmpty
    private String verifyCode;
    @Product
    private String product;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
