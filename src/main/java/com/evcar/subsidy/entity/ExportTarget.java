package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 导出指标
 * Created by Kong on 2017/5/17.
 */
public class ExportTarget implements Serializable {

    private String cartype ;
    private String vinCode ;
    private BigDecimal mileage ;
    private BigDecimal limitMileage ;
    private BigDecimal maxEnergyTime ;
    private BigDecimal maxElectricPower ;
    private BigDecimal avgDailyRunTime ;
    private BigDecimal hundredsKmusePower ;

    public ExportTarget(){

    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public BigDecimal getLimitMileage() {
        return limitMileage;
    }

    public void setLimitMileage(BigDecimal limitMileage) {
        this.limitMileage = limitMileage;
    }

    public BigDecimal getMaxEnergyTime() {
        return maxEnergyTime;
    }

    public void setMaxEnergyTime(BigDecimal maxEnergyTime) {
        this.maxEnergyTime = maxEnergyTime;
    }

    public BigDecimal getMaxElectricPower() {
        return maxElectricPower;
    }

    public void setMaxElectricPower(BigDecimal maxElectricPower) {
        this.maxElectricPower = maxElectricPower;
    }

    public BigDecimal getAvgDailyRunTime() {
        return avgDailyRunTime;
    }

    public void setAvgDailyRunTime(BigDecimal avgDailyRunTime) {
        this.avgDailyRunTime = avgDailyRunTime;
    }

    public BigDecimal getHundredsKmusePower() {
        return hundredsKmusePower;
    }

    public void setHundredsKmusePower(BigDecimal hundredsKmusePower) {
        this.hundredsKmusePower = hundredsKmusePower;
    }
}
