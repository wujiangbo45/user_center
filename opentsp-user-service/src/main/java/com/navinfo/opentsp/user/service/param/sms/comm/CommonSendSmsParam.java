package com.navinfo.opentsp.user.service.param.sms.comm;

import com.navinfo.opentsp.user.service.param.BaseParam;
import com.navinfo.opentsp.user.service.param.IpManaged;
import com.navinfo.opentsp.user.service.validator.CommSmsProduct;
import com.navinfo.opentsp.user.service.validator.MobilePhone;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-26
 * @modify
 * @copyright Navi Tsp
 */
public class CommonSendSmsParam extends BaseParam implements IpManaged, CommonSmsParam {

    /**
     * 手机号
     */
    @MobilePhone
    private String phone;
    /**
     * 短信内容，#{code} 替换成验证码
     */
    @NotEmpty
    private String content;
    /**
     * 渠道来源
     */
    @NotEmpty
    @CommSmsProduct
    private String product;
    @NotEmpty
    private String smsChannel;
    /**
     * 一个自定义标识
     */
    @NotEmpty
    private String key;

    private String ip;

    @Override
    public String getSmsChannel() {
        return smsChannel;
    }

    public void setSmsChannel(String smsChannel) {
        this.smsChannel = smsChannel;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
