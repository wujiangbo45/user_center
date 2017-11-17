package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.annotation.Column;
import com.navinfo.opentsp.user.dal.annotation.Entity;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "opentsp_user_car_info")
@CompoundIndexes({
        @CompoundIndex(name = "ucar_uid", unique = false, def = "{'userId':1}")
})
@Entity(table = "opentsp_user_car_info")
public class UserCarEntity implements Identified<String> {
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column("op_product_id")
    private String opProductId;

    @Column("car_model_id")
    private String carModelId;

	@Column("car_icon")
    private String carIcon;
	
    @Column("car_no")
    private String carNo;

    @Column("engine_no")
    private String engineNo;

    @Column("buy_date")
    private Date buyDate;

    @Column("create_time")
    private Date createTime;

    @Column("update_time")
    private Date updateTime;

    @Column("is_valid")
    private Boolean isValid;

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

    public String getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(String carModelId) {
        this.carModelId = carModelId == null ? null : carModelId.trim();
    }

    public String getCarIcon() {
        return carIcon;
    }

    public void setCarIcon(String carIcon) {
        this.carIcon = carIcon == null ? null : carIcon.trim();
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo == null ? null : carNo.trim();
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo == null ? null : engineNo.trim();
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
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

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    @Override
    public void generateID() {
        this.setId(UUIDUtil.randomUUID());
    }
}