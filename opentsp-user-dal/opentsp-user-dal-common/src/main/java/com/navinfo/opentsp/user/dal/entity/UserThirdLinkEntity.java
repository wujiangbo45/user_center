package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.annotation.Column;
import com.navinfo.opentsp.user.dal.annotation.Entity;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "opentsp_third_link")
@CompoundIndexes({
        @CompoundIndex(name = "uid_uthird", unique = false, def = "{'userId' : 1}")
})
@Entity(table = "opentsp_third_link")
public class UserThirdLinkEntity implements Identified<String> {
    private String id;

    @Column("user_id")
    private String userId;

    @Column("op_product_id")
    private String opProductId;

    @Column("third_type")
    private String thirdType;

    @Column("third_auth_info")
    private String thirdAuthInfo;

    @Column("third_open_id")
    private String thirdOpenId;

    @Column("access_token")
    private String accessToken;

    @Column("refresh_token")
    private String refreshToken;

    @Column("create_time")
    private Date createTime;

    @Column("update_time")
    private Date updateTime;

    @Column("is_valid")
    private Byte isValid;

    @Column("unionid")
    private String unionid;

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
        this.opProductId = opProductId;
    }

    public String getThirdType() {
        return thirdType;
    }

    public void setThirdType(String thirdType) {
        this.thirdType = thirdType == null ? null : thirdType.trim();
    }

    public String getThirdAuthInfo() {
        return thirdAuthInfo;
    }

    public void setThirdAuthInfo(String thirdAuthInfo) {
        this.thirdAuthInfo = thirdAuthInfo == null ? null : thirdAuthInfo.trim();
    }

    public String getThirdOpenId() {
        return thirdOpenId;
    }

    public void setThirdOpenId(String thirdOpenId) {
        this.thirdOpenId = thirdOpenId == null ? null : thirdOpenId.trim();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? null : accessToken.trim();
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken == null ? null : refreshToken.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}