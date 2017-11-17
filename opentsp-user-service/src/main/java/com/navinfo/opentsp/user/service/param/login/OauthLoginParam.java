package com.navinfo.opentsp.user.service.param.login;

import com.navinfo.opentsp.user.service.param.DeviceBaseParam;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */
public class OauthLoginParam extends DeviceBaseParam {
    @NotEmpty
    private String openId;
    @NotEmpty
    private String unionid;

    @NotEmpty
    private String accessToken;

    private String refreshToken;
    @NotEmpty
    private String type; //类型 qq, weixin
    @Product
    private String product;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refershToken) {
        this.refreshToken = refershToken;
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
        return new StringBuilder("openId : ").append(openId)
                .append(" , accessToken : ").append(accessToken)
                .append(" , refreshToken : ").append(refreshToken)
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
