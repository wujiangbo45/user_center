package com.navinfo.opentsp.user.service.param.setting;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import com.navinfo.opentsp.user.service.param.IpManaged;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author wanliang
 * @version 1.0
 * @date 2015/10/19
 * @modify
 * @copyright Navi Tsp
 */
public class CheckChangeBindEmailParam extends BaseTokenParam implements IpManaged {

    @Email
    private String email;
    @Product
    private String product;
    @NotEmpty
    private String verifyCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public void setIp(String ip) {

    }

    @Override
    public String getIp() {
        return null;
    }

}
