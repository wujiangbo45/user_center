package com.navinfo.opentsp.user.service.param.statics;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-02
 * @modify
 * @copyright Navi Tsp
 */
public class QueryStaticsParam extends BaseTokenParam {

    public static final String day = "day";
    public static final String week = "week";
    public static final String month = "month";
    public static final String year = "year";


    @NotEmpty
    private String startDate;

    @NotEmpty
    private String endDate;

    private String product;

    /**
     * day, week, month, year
     */
    private String unit;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
