package com.navinfo.opentsp.user.service.param.password;

import com.navinfo.opentsp.user.service.param.BaseParam;
import com.navinfo.opentsp.user.service.validator.Password;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
public class ResetPasswordParam extends BaseParam {
    @NotEmpty
    private String identifier;
    @NotEmpty
    private String verifyCode;
    @Password
    private String newPassword;
    @Product
    private String product;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
