package com.navinfo.opentsp.user.service.param.setting;

import com.navinfo.opentsp.user.service.param.DeviceBaseTokenParam;
import com.navinfo.opentsp.user.service.validator.Product;

import java.util.Date;

/**
 * @author wanliang
 * @version 1.0
 * @date 2015/10/19
 * @modify 修改用户参数信息
 * @copyright Navi Tsp
 */
public class UpdateUserParam extends DeviceBaseTokenParam {

    private String nickname;
    private Byte gender;
    private Date birthday;
    private String liveProvince;
    private String liveCity;
    private String liveDistrict;//有的城市没有区， 与客户端约定，传字符串'NULL'将此字段置为空
    @Product
    private String product;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
