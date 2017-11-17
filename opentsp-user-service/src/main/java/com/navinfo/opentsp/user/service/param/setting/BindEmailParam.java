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
public class BindEmailParam extends BaseTokenParam implements IpManaged {

    @Email
    private String email;
    private String oldEmail;
    @NotEmpty
    private String type;//绑定还是解绑， 取值范围：bind, changeBind
    @Product
    private String product;
    @NotEmpty
    private String verifyCode;

    private String oldVerifyCode;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getOldVerifyCode() {
        return oldVerifyCode;
    }

    public void setOldVerifyCode(String oldVerifyCode) {
        this.oldVerifyCode = oldVerifyCode;
    }

    @Override
    public void setIp(String ip) {

    }

    @Override
    public String getIp() {
        return null;
    }
}
