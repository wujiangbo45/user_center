package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.dal.annotation.Column;
import com.navinfo.opentsp.user.dal.annotation.Entity;
import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_register_statics")
@Entity(table = "user_register_statics")
public class RegisterStaticsEntity implements Identified<String> {

    private String id;

    @Column(name = "register_year")
    private Integer registerYear;

    @Column(name = "register_month")
    private String registerMonth;

    @Column(name = "register_week")
    private Integer registerWeek;

    @Column(name = "register_date")
    private String registerDate;

    private String product;

    /**
     * 注册来源， web, mobile, car_box
     * TODO create a enum
     */
    @Column(name = "register_src")
    private String registerSrc;

    @Column(name = "register_num")
    private Long registerNum;

    public Integer getRegisterYear() {
        return registerYear;
    }

    public void setRegisterYear(Integer registerYear) {
        this.registerYear = registerYear;
    }

    public String getRegisterMonth() {
        return registerMonth;
    }

    public void setRegisterMonth(String registerMonth) {
        this.registerMonth = registerMonth;
    }

    public Integer getRegisterWeek() {
        return registerWeek;
    }

    public void setRegisterWeek(Integer registerWeek) {
        this.registerWeek = registerWeek;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate == null ? null : registerDate.trim();
    }

    public String getRegisterSrc() {
        return registerSrc;
    }

    public void setRegisterSrc(String registerSrc) {
        this.registerSrc = registerSrc == null ? null : registerSrc.trim();
    }

    @Override
    public void generateID() {
        this.setId(UUIDUtil.randomUUID());
    }

    public Long getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(Long registerNum) {
        this.registerNum = registerNum;
    }
}