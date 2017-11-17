package com.navinfo.opentsp.user.service.param.setting;

import com.navinfo.opentsp.user.service.param.BaseTokenParam;
import com.navinfo.opentsp.user.service.validator.Product;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * @author wanliang
 * @version 1.0
 * @date 2015/10/19
 * @modify
 * @copyright Navi Tsp
 */
public class UpdateUserCarParam extends BaseTokenParam {

    private String carId;
    private String lisenceNo;
    private String engineNo;
    private Date carBuyDate;
    @NotEmpty
    private String carModelId;
    private String carIcon;
    @Product
    private String product;

    public String getCarIcon() {
        return carIcon;
    }

    public void setCarIcon(String carIcon) {
        this.carIcon = carIcon;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getLisenceNo() {
        return lisenceNo;
    }

    public void setLisenceNo(String lisenceNo) {
        this.lisenceNo = lisenceNo;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public Date getCarBuyDate() {
        return carBuyDate;
    }

    public void setCarBuyDate(Date carBuyDate) {
        this.carBuyDate = carBuyDate;
    }

    public String getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(String carModelId) {
        this.carModelId = carModelId;
    }
}
