package com.navinfo.opentsp.user.service.device;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public interface DeviceManaged {

    @JsonIgnore
    public void setDeviceType(DeviceType type);

    @JsonIgnore
    public DeviceType getDeviceType();

    public void setDeviceId(String deviceId);

    public String getDeviceId();

    public void setAppVersion(String appVersion);

    public String getAppVersion();

}
