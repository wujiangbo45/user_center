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
public class VerifySmsCodeParam extends BaseParam implements IpManaged, CommonSmsParam {

    @MobilePhone
    private String phone;
    @NotEmpty
    private String verifyCode;
    @NotEmpty
    @CommSmsProduct
    private String product;
    @NotEmpty
    private String key;

    private String smsChannel;

    private String ip;

    /**
     * 验证成功之后是否清除
     */
    private int expire = 0;

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getSmsChannel() {
        return smsChannel;
    }

    public void setSmsChannel(String smsChannel) {
        this.smsChannel = smsChannel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }
}
