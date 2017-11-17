package com.navinfo.opentsp.user.service.param.login;

import com.navinfo.opentsp.user.service.param.DeviceBaseParam;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-14
 * @modify
 * @copyright Navi Tsp
 */
public class LoginParam extends DeviceBaseParam implements Serializable {

    @NotEmpty
    private String loginName;
    @NotEmpty
    private String password;
    @Product
    private String product;

    private Integer useDetail;
    private Integer autoLogin;
    private String captcha;

    private String webServerUrl;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getUseDetail() {
        return useDetail;
    }

    public void setUseDetail(Integer useDetail) {
        this.useDetail = useDetail;
    }

    public Integer getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(Integer autoLogin) {
        this.autoLogin = autoLogin;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getWebServerUrl() {
        return webServerUrl;
    }

    public void setWebServerUrl(String webServerUrl) {
        this.webServerUrl = webServerUrl;
    }
}
