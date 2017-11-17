package com.navinfo.opentsp.user.service.param.statics;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-28
 * @modify
 * @copyright Navi Tsp
 */
public class AnalyzeStaticsParam extends BaseTokenParam {

    private String product;
    private String src;
    @NotEmpty
    private String dayStart;//format 2015-10-25-12-25-23
    @NotEmpty
    private String dayEnd;
    private String override;

    public String getOverride() {
        return override;
    }

    public void setOverride(String override) {
        this.override = override;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDayStart() {
        return dayStart;
    }

    public void setDayStart(String dayStart) {
        this.dayStart = dayStart;
    }

    public String getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(String dayEnd) {
        this.dayEnd = dayEnd;
    }
}
