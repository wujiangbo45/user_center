package com.navinfo.opentsp.user.service.resultdto.setting;

import java.util.Date;

/**
 * @author wanliang
 * @version 1.0
 * @date 2015/10/16
 * @modify
 * @copyright Navi Tsp
 */
public class CarInfo {
    private String carId;
    private String lisenceNo;
    private String carModelId;
    private String carIcon;
    private String engineNo;
    private Date carBuyDate;

    public String getCarIcon() {
        return carIcon;
    }

    public void setCarIcon(String carIcon) {
        this.carIcon = carIcon;
    }

    public String getCarId() {
        return carId;
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

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(String carModelId) {
        this.carModelId = carModelId;
    }

    public Date getCarBuyDate() {
        return carBuyDate;
    }

    public void setCarBuyDate(Date carBuyDate) {
        this.carBuyDate = carBuyDate;
    }
}
