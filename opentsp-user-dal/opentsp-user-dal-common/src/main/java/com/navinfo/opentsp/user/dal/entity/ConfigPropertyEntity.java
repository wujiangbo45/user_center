package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.annotation.Column;
import com.navinfo.opentsp.user.dal.annotation.Entity;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "opentsp_config_props")
@CompoundIndexes({
        @CompoundIndex(name = "pro_type_name", unique = true, def = "{'productId':1, 'type':1, 'propName':1}")
})
@Entity(table = "opentsp_config_props")
public class ConfigPropertyEntity implements Identified<String> {

    public static final String DEFAULT_PRODUCT = GlobalConstans.DEFAULT_PRODUCT;

    @Id
    private String id;

    @Column(name = "product_id", nullable = false)
    private String productId;

    /**
     * 类型/功能点， 比如： captcha, session...
     */
    private String type;

    /**
     *
     */
    @Column(name = "prop_name", nullable = false)
    private String propName;

    @Column(name = "prop_value")
    private String propValue;

    private String description;

    @Column(name = "default_val", nullable = false)
    private String defaultVal;

    @Column(name = "create_time")
    private Date createTime = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName == null ? null : propName.trim();
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue == null ? null : propValue.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal == null ? null : defaultVal.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public void generateID() {
        this.setId(UUIDUtil.randomUUID());
    }

    public static enum Types {
        system,
        register,
        captcha,
        login,
        sms,
        session;
    }
}