package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.dal.annotation.Column;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-28
 * @modify
 * @copyright Navi Tsp
 */
@Document(collection = "comm_sms_product_info")
public class CommomSmsProductEntity implements Identified<String> {

    @Id
    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    private String description;

    @Column(name = "create_time", nullable = false)
    private Date createTime = new Date();

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public void generateID() {
        throw new IllegalStateException("No product Id provided !");
    }

    @Override
    public void setId(String s) {
        this.setProductId(s);
    }

    @Override
    public String getId() {
        return this.getProductId();
    }
}
