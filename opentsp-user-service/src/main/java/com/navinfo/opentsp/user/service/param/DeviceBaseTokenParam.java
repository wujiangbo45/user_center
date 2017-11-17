package com.navinfo.opentsp.user.service.param;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.service.device.DeviceManaged;
import com.navinfo.opentsp.user.service.device.DeviceType;
import org.springframework.util.StringUtils;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-15
 * @modify
 * @copyright Navi Tsp
 */
public class DeviceBaseTokenParam extends BaseTokenParam implements HttpHeaderParamable, DeviceManaged, IpManaged {

    private String deviceTypeString = DeviceType.web.name();
    private String deviceId;
    private String appVersion;
    private String ip;

    public String getDeviceTypeString() {
        if(StringUtils.isEmpty(deviceTypeString))
            this.deviceTypeString = DeviceType.web.name();
        return deviceTypeString;
    }

    public void setDeviceType(String deviceType) {
        this.deviceTypeString = deviceType;
    }

    public void setDeviceTypeString(String deviceTypeString) {
        this.deviceTypeString = deviceTypeString;
    }

    @Override
    public String getDeviceId() {
        if(StringUtils.isEmpty(deviceId) && DeviceType.web.name().equals(this.getDeviceTypeString())) {
            this.deviceId = this.ip;
        }
        return deviceId;
    }

    @Override
    public void setDeviceType(DeviceType type) {
        this.setDeviceType(type.name());
    }

    @Override
    public DeviceType getDeviceType() {
        return Enum.valueOf(DeviceType.class, this.getDeviceTypeString());
    }

    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String getAppVersion() {
        return appVersion;
    }

    @Override
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String[][] fieldHeaderMapping() {
        return new String[][]{
                {"deviceTypeString", GlobalConstans.HEADER_DEVICE_TYPE},
                {"deviceId", GlobalConstans.HEADER_DEVICE_ID},
                {"appVersion", GlobalConstans.HEADER_APP_VERSION}
        };
    }
}
