package com.navinfo.opentsp.user.dal.entity;

import com.navinfo.opentsp.user.dal.dao.Identified;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-05
 * @modify
 * @copyright Navi Tsp
 */
@Document(collection = "location_info")
public class LocationEntity implements Identified<String> {

    @Id
    private String cityCode;

    private String parentCode;

    private String name;

    //拼音
    private String ename;

    private Integer order;

    private int level;

    private String path;

    private String country = "中国";

    private Date createTime = new Date();

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public void generateID() {

    }

    @Override
    public void setId(String s) {
        this.setCityCode(s);
    }

    @Override
    public String getId() {
        return this.getCityCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj != null && (obj instanceof LocationEntity)) {
            return this.getCityCode().equals(((LocationEntity) obj).getCityCode());
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getCityCode());
    }
}
