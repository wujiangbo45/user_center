package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.annotation.Column;
import com.navinfo.opentsp.user.dal.annotation.Entity;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "opentsp_appid_key_info")
@Entity(table = "opentsp_appid_key_info")
public class AppIDKeyEntity implements Identified<String> {
    @Id
    private String id;

    /**
     * 第三方类型
     * TODO create a enum
     */
    @Column(name = "third_type", nullable = false)
    private String thirdType;

    /**
     * 第三方名称
     * TODO create a enum
     */
    @Column(name = "third_name", nullable = false)
    private String thirdName;

    private String appid;

    private String appkey;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "create_time", nullable = false)
    private Date createTime = new Date();

    public String getId() {
        return id;
    }

    @Override
    public void generateID() {
        this.setId(UUIDUtil.randomUUID());
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getThirdType() {
        return thirdType;
    }

    public void setThirdType(String thirdType) {
        this.thirdType = thirdType == null ? null : thirdType.trim();
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName == null ? null : thirdName.trim();
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey == null ? null : appkey.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}