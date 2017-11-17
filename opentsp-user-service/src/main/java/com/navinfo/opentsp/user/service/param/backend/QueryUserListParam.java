package com.navinfo.opentsp.user.service.param.backend;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;

/**
 *
 * Created by wupeng on 11/3/15.
 */
public class QueryUserListParam extends BaseTokenParam {

    private String key;
    private String startDate;
    private String endDate;
    //0 - all, 1 - no, 2 - wechat, 3 - qq, 4 - wechat or qq
    private String bindInfo;
    //from 1
    private int pageNo;

    private int pageSize;

    private String product;

    private String order;

    private int orderType;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBindInfo() {
        return bindInfo;
    }

    public void setBindInfo(String bindInfo) {
        this.bindInfo = bindInfo;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
