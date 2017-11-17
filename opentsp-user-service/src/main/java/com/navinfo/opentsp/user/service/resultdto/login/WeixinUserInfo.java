package com.navinfo.opentsp.user.service.resultdto.login;

import com.navinfo.opentsp.user.dal.entity.UserProfileEntity;

import java.util.List;

/**
 * Result of get https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
 *
 * @see https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&lang=zh_CN
 * @author wupeng
 * @version 1.0
 * @date 2015-04-27
 * @modify
 * @copyright Navi Tsp
 */
public class WeixinUserInfo implements OauthResult{

    private String openid;
    private String nickname;
    private Integer sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private List<String> privilege;
    private String unionid;
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public List<String> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<String> privilege) {
        this.privilege = privilege;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public Byte gender() {
        if(this.getSex() != null) {
            return this.getSex().intValue() == 1 ? UserProfileEntity.GENDER_MAN : UserProfileEntity.GENDER_WOMAN;
        }
        return null;
    }

    @Override
    public String nickname() {
        return this.getNickname();
    }

    @Override
    public String imgUrl() {
        return this.getHeadimgurl();
    }
}
