package com.navinfo.opentsp.user.service.param.setting;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import com.navinfo.opentsp.user.service.param.IpManaged;
import com.navinfo.opentsp.user.service.validator.MobilePhone;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author wanliang
 * @version 1.0
 * @date 2015/10/19
 * @modify
 * @copyright Navi Tsp
 */
public class ApplyBindMobileParam extends BaseTokenParam implements IpManaged {

    @MobilePhone
    private String mobile;
    private String oldMobile;
    @NotEmpty
    private String type;//绑定还是解绑， 取值范围：bind, changeBind
    @Product
    private String product;

    private String ip;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOldMobile() {
        return oldMobile;
    }

    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile;
    }

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


    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String getIp() {
        return ip;
    }
}
