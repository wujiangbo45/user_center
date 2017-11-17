package com.navinfo.opentsp.user.service.param.login;

import com.navinfo.opentsp.user.service.param.DeviceBaseParam;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wanliang
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */
public class WebOauthLoginParam extends DeviceBaseParam {


    private String code;
    @NotEmpty
    private String type; //类型 qq, weixin
    @Product
    private String product;

    private String unionid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
    public String toString() {
        return new StringBuilder()
                .append(" , code : ").append(code)
                .append(" , type : ").append(type)
                .append(" , product : ").append(product)
                .toString();
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
