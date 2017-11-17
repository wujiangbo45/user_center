package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.annotation.Column;
import com.navinfo.opentsp.user.dal.annotation.Entity;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
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
@Document(collection = "opentsp_user_info")
@CompoundIndexes({
    @CompoundIndex(name = "user_entity_mobile", unique = true, def = "{'mobile':1}"),
    @CompoundIndex(name = "user_entity_email", unique = true, def = "{'email':1}"),
    @CompoundIndex(name = "idx_oui_ctime", unique = false, def = "{'createTime':1}")
})
@Entity(table = "opentsp_user_info")
public class UserEntity implements Serializable, Identified<String> {

    /**
     * 帐号锁定状态-未被锁定
     */
    public static final byte ACCOUNT_NO_LOCKED = 1;
    /**
     * 帐号锁定状态-已锁定
     */
    public static final byte ACCOUNT_LOCKED = 0;

    /**
     * 帐号过期状态 - 未过期
     */
    public static final byte ACCOUNT_NO_EXPIRED = 1;

    /**
     *
     * 帐号过期状态 - 已过期
     */
    public static final byte ACCOUNT_EXPIRED = 0;

    /**
     * 帐号激活状态 - 已激活
     */
    public static final byte ACCOUNT_NO_ACTIVED = 0;

    /**
     * 帐号激活状态 - 未激活
     */
    public static final byte ACCOUNT_ACTIVED = 1;

    public static final byte ACCOUNT_ENABLE = 1;
    public static final byte ACCOUNT_DISABLE = 0;

    @Id
    private String id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 随机码
     */
    private String salt;

    /**
     * 帐号是否被锁定
     *
     */
    @Column(name = "account_non_locked", nullable = false)
    private Byte accountNonLocked;

    /**
     * 帐号是否过期
     *
     */
    @Column(name = "account_non_expired", nullable = false)
    private Byte accountNonExpired;

    /**
     * 帐号是否已经激活
     *
     */
    @Column(name = "account_actived", nullable = false)
    private Byte accountActived;

    /**
     * 是否可用
     *
     */
    @Column(nullable = false)
    private Byte enable;

    /**
     * 哪个产品线
     */
    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Byte getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Byte accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Byte getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Byte accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Byte getAccountActived() {
        return accountActived;
    }

    public void setAccountActived(Byte accountActived) {
        this.accountActived = accountActived;
    }

    public Byte getEnable() {
        return enable;
    }

    public void setEnable(Byte enable) {
        this.enable = enable;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    @Override
    public void generateID() {
        this.setId(UUIDUtil.randomUUID());
    }
}
