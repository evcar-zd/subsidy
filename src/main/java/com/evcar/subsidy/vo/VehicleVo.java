package com.evcar.subsidy.vo;

import java.util.Date;

/**
 * Created by Kong on 2017/5/31.
 */
public class VehicleVo {
    private String id ;
    private String carType ;                    //车类型
    private String vinCode ;                    //VIN
    private Date veDeliveredDate ;              //交车日期
    private Date releaseTime ;                  //下线日期
    private Date tm ;                           //数据时间

    public VehicleVo(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
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

    public Date getTm() {
        return tm;
    }

    public void setTm(Date tm) {
        this.tm = tm;
    }
}
