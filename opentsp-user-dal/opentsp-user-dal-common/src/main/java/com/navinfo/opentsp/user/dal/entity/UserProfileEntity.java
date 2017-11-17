package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.annotation.Column;
import com.navinfo.opentsp.user.dal.annotation.Entity;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-25
 * @modify
 * @copyright Navi Tsp
 */
@Document(collection = "opentsp_user_profile")
@Entity(table = "opentsp_user_profile")
public class UserProfileEntity implements Serializable, Identified<String> {

    /**
     * 性别 - 男
     */
    public static final byte GENDER_MAN = 1;
    /**
     * 性别 - 女
     */
    public static final byte GENDER_WOMAN = 0;

    /**
     * 手机已激活
     */
    public static final byte MOBILE_ACTIVED = 1;

    /**
     * 手机未激活
     */
    public static final byte MOBILE_NO_ACTIVED = 0;

    /**
     * 邮箱已激活
     */
    public static final byte EMAIL_ACTIVED = 1;

    /**
     * 邮箱未激活
     */
    public static final byte EMAIL_NO_ACTIVED = 0;

    /**
     * 昵称可修改
     */
    public static final byte NICKNAME_CAN_MODIFY = 1;

    /**
     * 昵称不可修改
     */
    public static final byte NICKNAME_CANNOT_MODIFY = 0;

    public static final String register_web = "web";

    public static final String register_mobile = "web";

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * 头像url
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 性别
     */
    @Column(nullable = false)
    private Byte gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 所属省
     */
    private String province;

    /**
     * 所属市
     */
    private String city;

    /**
     * 所属区县
     */
    private String district;

    /**
     * 手机是否激活
     */
    @Column(name = "mobile_actived", nullable = false)
    private Byte mobileActived;

    /**
     * 邮箱是否已经激活
     */
    @Column(name = "email_actived", nullable = false)
    private Byte emailActived;

    /**
     * 昵称是否可以修改
     */
    @Column(name = "nickname_modifiable", nullable = false)
    private Byte nicknameModifiable;

    /**
     * 注册来源
     * TODO  create a enum
     */
    @Column(name = "register_src", nullable = false)
    private String registerSrc;

    /**
     * 注册的设备id
     */
    @Column(name = "register_device_id", nullable = false)
    private String registerDeviceId;

    /**
     * 注册的设备类型
     */
    @Column(name = "register_device_type", nullable = false)
    private String registerDeviceType;

    /**
     * 注册的app版本号
     */
    @Column(name = "register_app_version", nullable = false)
    private String registerAppVersion;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Byte getMobileActived() {
        return mobileActived;
    }

    public void setMobileActived(Byte mobileActived) {
        this.mobileActived = mobileActived;
    }

    public Byte getEmailActived() {
        return emailActived;
    }

    public void setEmailActived(Byte emailActived) {
        this.emailActived = emailActived;
    }

    public Byte getNicknameModifiable() {
        return nicknameModifiable;
    }

    public void setNicknameModifiable(Byte nicknameModifiable) {
        this.nicknameModifiable = nicknameModifiable;
    }

    public String getRegisterSrc() {
        return registerSrc;
    }

    public void setRegisterSrc(String registerSrc) {
        this.registerSrc = registerSrc;
    }

    public String getRegisterDeviceId() {
        return registerDeviceId;
    }

    public void setRegisterDeviceId(String registerDeviceId) {
        this.registerDeviceId = registerDeviceId;
    }

    public String getRegisterDeviceType() {
        return registerDeviceType;
    }

    public void setRegisterDeviceType(String registerDeviceType) {
        this.registerDeviceType = registerDeviceType;
    }

    public String getRegisterAppVersion() {
        return registerAppVersion;
    }

    public void setRegisterAppVersion(String registerAppVersion) {
        this.registerAppVersion = registerAppVersion;
    }

    @Override
    public void setId(String s) {
        this.setUserId(s);
    }

    @Override
    public String getId() {
        return this.getUserId();
    }

    @Override
    public void generateID() {
        this.setId(UUIDUtil.randomUUID());
    }
}
