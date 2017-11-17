package com.navinfo.opentsp.user.service.resultdto.setting;

import java.util.List;

/**
 * @author wanliang
 * @version 1.0
 * @date 2015/10/16
 * @modify
 * @copyright Navi Tsp
 */
public class AccountResult {

    private String userId;
    private String nickname;
    private String mobile;
    private String email;
    private Byte nickEditable;
    private String bindInfo;
    private Byte gender;
    private String birthday;
    private String liveProvince;
    private String liveCity;
    private String liveDistrict;
    private List<CarInfo>  carInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Byte getNickEditable() {
        return nickEditable;
    }

    public void setNickEditable(Byte nickEditable) {
        this.nickEditable = nickEditable;
    }

    public String getBindInfo() {
        return bindInfo;
    }

    public void setBindInfo(String bindInfo) {
        this.bindInfo = bindInfo;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLiveProvince() {
        return liveProvince;
    }

    public void setLiveProvince(String liveProvince) {
        this.liveProvince = liveProvince;
    }

    public String getLiveCity() {
        return liveCity;
    }

    public void setLiveCity(String liveCity) {
        this.liveCity = liveCity;
    }

    public String getLiveDistrict() {
        return liveDistrict;
    }

    public void setLiveDistrict(String liveDistrict) {
        this.liveDistrict = liveDistrict;
    }

    public List<CarInfo> getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(List<CarInfo> carInfo) {
        this.carInfo = carInfo;
    }
}
