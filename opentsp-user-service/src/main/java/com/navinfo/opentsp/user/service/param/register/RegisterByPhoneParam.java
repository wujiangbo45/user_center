package com.navinfo.opentsp.user.service.param.register;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.service.device.DeviceManaged;
import com.navinfo.opentsp.user.service.device.DeviceType;
import com.navinfo.opentsp.user.service.param.BaseParam;
import com.navinfo.opentsp.user.service.param.HttpHeaderParamable;
import com.navinfo.opentsp.user.service.validator.MobilePhone;
import com.navinfo.opentsp.user.service.validator.Password;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.StringUtils;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public class RegisterByPhoneParam extends BaseParam implements HttpHeaderParamable, DeviceManaged {
    @MobilePhone
    private String mobile;
    @Password
    private String password;
    @NotEmpty
    private String smsCode;
    @Product
    private String product;

    private String deviceType;

    private String deviceId;

    private String appVersion;

    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public void setDeviceType(DeviceType type) {
        this.setDeviceId(type.name());
    }

    @Override
    public DeviceType getDeviceType() {
        return Enum.valueOf(DeviceType.class, this.getDeviceTypeString());
    }

    public String getDeviceTypeString() {
        if(StringUtils.isEmpty(this.deviceType))
            this.deviceType = DeviceType.web.name();
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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
