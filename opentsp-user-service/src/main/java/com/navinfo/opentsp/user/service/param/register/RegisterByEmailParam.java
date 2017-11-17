package com.navinfo.opentsp.user.service.param.register;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.service.device.DeviceManaged;
import com.navinfo.opentsp.user.service.device.DeviceType;
import com.navinfo.opentsp.user.service.param.BaseParam;
import com.navinfo.opentsp.user.service.param.HttpHeaderParamable;
import com.navinfo.opentsp.user.service.validator.Password;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public class RegisterByEmailParam extends BaseParam implements HttpHeaderParamable, DeviceManaged, Serializable{
    @Email
    private String email;
    @Password
    private String password;
    @Product
    private String product;
    @NotEmpty
    private String captcha;
    /**
     * 发送的激活邮件里面的页面地址
     */
    @NotEmpty
    private String redirectUrl;

    private String deviceType;

    private String deviceId;

    private String appVersion;

    private String ip;

    private long createTime = 0L;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public void setDeviceType(DeviceType type) {
        this.setDeviceType(type.name());
    }

    @Override
    public DeviceType getDeviceType() {
        return Enum.valueOf(DeviceType.class, this.getDeviceTypeString());
    }

    public String getDeviceTypeString() {
        if(StringUtils.isEmpty(deviceType))
            deviceType = DeviceType.web.name();
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public String[][] fieldHeaderMapping() {
        return new String[][]{
                {"deviceType", GlobalConstans.HEADER_DEVICE_TYPE},
                {"deviceId", GlobalConstans.HEADER_DEVICE_ID},
                {"appVersion", GlobalConstans.HEADER_APP_VERSION}
        };
    }
}
