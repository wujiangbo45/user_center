package com.navinfo.opentsp.user.service.resultdto.setting;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-11
 * @modify
 * @copyright Navi Tsp
 */
public class UpdateUserCarResult {

    private String carId;

    public UpdateUserCarResult(){}

    public UpdateUserCarResult(String carId){
        this.carId = carId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }
}
