package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.annotation.Column;
import com.navinfo.opentsp.user.dal.annotation.Entity;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "opentsp_user_token_info")
@CompoundIndexes({
        @CompoundIndex(name = "token_token", unique = true, def = "{'token':1}"),
        @CompoundIndex(name = "token_uid", unique = false, def = "{'userId':1}")
})
@Entity(table = "opentsp_user_token_info")
public class TokenEntity implements Identified<String>, Serializable {
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "login_name", nullable = false)
    private String loginName;

    @Column(name = "op_product_id", nullable = false)
    private String opProductId;

    private String token;

    @Column(name = "create_time")
    private Date createTime = new Date();

    @Column(name = "device_id")
    private String deviceId;

    @Column("device_type")
    private String deviceType;

    @Column("app_version")
    private String appVersion;

    @Column("client_ip")
    private String clientIp;

    @Column("is_valid")
    private Byte isValid;

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
        this.loginName = loginName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion == null ? null : appVersion.trim();
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public Byte getIsValid() {
        return isValid;
    }

    public void setIsValid(Byte isValid) {
        this.isValid = isValid;
    }

    @Override
    public void generateID() {
        this.setId(UUIDUtil.randomUUID());
    }
}