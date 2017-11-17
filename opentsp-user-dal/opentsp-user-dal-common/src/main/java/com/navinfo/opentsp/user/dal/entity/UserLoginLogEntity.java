package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.annotation.Column;
import com.navinfo.opentsp.user.dal.annotation.Entity;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "opentsp_user_login_log")
@CompoundIndexes({
        @CompoundIndex(name = "uid_ulogin", unique = false, def = "{'userId':1}")
})
@Entity(table = "opentsp_user_login_log")
public class UserLoginLogEntity implements Identified<String> {
    private String id;

    @Column("user_id")
    private String userId;

    /**
     * 用户属于哪个产品
     */
    @Column("product_id")
    private String productId;

    /**
     * 哪个产品发出的登录
     */
    @Column("op_product_id")
    private String opProductId;

    @Column("login_name")
    private String loginName;

    @Column("login_time")
    private Date loginTime = new Date();

    @Column("login_ip")
    private String loginIp;

    @Column("login_result")
    private String loginResult;

    private String token;

    @Column("device_type")
    private String deviceType;

    @Column("device_id")
    private String deviceId;

    @Column("app_version")
    private String appVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getOpProductId() {
        return opProductId;
    }

    public void setOpProductId(String opProductId) {
        this.opProductId = opProductId == null ? null : opProductId.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult == null ? null : loginResult.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion == null ? null : appVersion.trim();
    }

    @Override
    public void generateID() {
        this.setId(UUIDUtil.randomUUID());
    }
}