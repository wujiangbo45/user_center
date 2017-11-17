package com.navinfo.opentsp.user.service.resultdto.login;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-18
 * @modify
 * @copyright Navi Tsp
 */
public class LoginHistory {

    private String loginTime;
    private String ip;
    private String city;
    private String device;

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
