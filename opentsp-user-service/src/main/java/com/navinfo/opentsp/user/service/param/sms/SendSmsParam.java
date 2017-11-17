package com.navinfo.opentsp.user.service.param.sms;

import com.navinfo.opentsp.user.service.param.BaseParam;
import com.navinfo.opentsp.user.service.param.HttpHeaderParamable;
import com.navinfo.opentsp.user.service.validator.MobilePhone;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public class SendSmsParam extends BaseParam implements HttpHeaderParamable {

    @NotEmpty
    private String type;
    @MobilePhone
    private String mobile;
    @Product
    private String product;
    private String captcha;
    private String ip;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Override
    public String[][] fieldHeaderMapping() {
//        return new String[][]{{"ip","User-Agent"}};
        return null;
    }
}
