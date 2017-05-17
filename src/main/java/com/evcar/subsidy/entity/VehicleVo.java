package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Kong on 2017/5/11.
 */
public class VehicleVo implements Serializable{

    private String vinCode ;
    private String carType ;
    private Date veDeliveredDate ;
    private Date releaseTime ;

    public VehicleVo(){

    }

    public VehicleVo(String vinCode,String carType ,Date veDeliveredDate,Date releaseTime){
        this.vinCode = vinCode ;
        this.carType = carType ;
        this.veDeliveredDate = veDeliveredDate ;
        this.releaseTime = releaseTime ;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public Date getVeDeliveredDate() {
        return veDeliveredDate;
    }

    public void setVeDeliveredDate(Date veDeliveredDate) {
        this.veDeliveredDate = veDeliveredDate;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }
}
