package com.navinfo.opentsp.user.service.param.password;

import com.navinfo.opentsp.user.service.param.BaseParam;
import com.navinfo.opentsp.user.service.param.IpManaged;
import com.navinfo.opentsp.user.service.validator.MobilePhone;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wujiangbo
 * @version 1.0
 * @date 2017-11-16
 * @modify
 * @copyright Navi Tsp
 */
public class FindPasswordBySmsForNoPicParam extends BaseParam implements IpManaged {

    @MobilePhone
    private String mobile;
    @Product
    private String product;

    private String ip;

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

}
