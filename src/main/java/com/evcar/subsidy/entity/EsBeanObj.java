package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Kong on 2017/5/15.
 */
public class EsBeanObj implements Serializable {


    private BigDecimal mileageMin ;
    private BigDecimal mileageMax ;
    private BigDecimal limitMileageMin ;
    private BigDecimal limitMileageMax ;
    private BigDecimal energyTimeMin ;
    private BigDecimal energyTimeMax ;
    private BigDecimal electricPowerMin ;
    private BigDecimal electricPowerMax ;
    private BigDecimal avgDailyRunTimeMin ;
    private BigDecimal avgDailyRunTimeMax ;
    private BigDecimal hundredsPowerMin ;
    private BigDecimal hundredsPowerMax ;
    private BigDecimal socTotalcapacity ;

    public EsBeanObj() {

    }

    public BigDecimal getMileageMin() {
        return mileageMin;
    }

    public void setMileageMin(BigDecimal mileageMin) {
        this.mileageMin = mileageMin;
    }

    public BigDecimal getMileageMax() {
        return mileageMax;
    }

    public void setMileageMax(BigDecimal mileageMax) {
        this.mileageMax = mileageMax;
    }

    public BigDecimal getLimitMileageMin() {
        return limitMileageMin;
    }

    public void setLimitMileageMin(BigDecimal limitMileageMin) {
        this.limitMileageMin = limitMileageMin;
    }

    public BigDecimal getLimitMileageMax() {
        return limitMileageMax;
    }

    public void setLimitMileageMax(BigDecimal limitMileageMax) {
        this.limitMileageMax = limitMileageMax;
    }

    public BigDecimal getEnergyTimeMin() {
        return energyTimeMin;
    }

    public void setEnergyTimeMin(BigDecimal energyTimeMin) {
        this.energyTimeMin = energyTimeMin;
    }

    public BigDecimal getEnergyTimeMax() {
        return energyTimeMax;
    }

    public void setEnergyTimeMax(BigDecimal energyTimeMax) {
        this.energyTimeMax = energyTimeMax;
    }

    public BigDecimal getElectricPowerMin() {
        return electricPowerMin;
    }

    public void setElectricPowerMin(BigDecimal electricPowerMin) {
        this.electricPowerMin = electricPowerMin;
    }

    public BigDecimal getElectricPowerMax() {
        return electricPowerMax;
    }

    public void setElectricPowerMax(BigDecimal electricPowerMax) {
        this.electricPowerMax = electricPowerMax;
    }

    public BigDecimal getAvgDailyRunTimeMin() {
        return avgDailyRunTimeMin;
    }

    public void setAvgDailyRunTimeMin(BigDecimal avgDailyRunTimeMin) {
        this.avgDailyRunTimeMin = avgDailyRunTimeMin;
    }

    public BigDecimal getAvgDailyRunTimeMax() {
        return avgDailyRunTimeMax;
    }

    public void setAvgDailyRunTimeMax(BigDecimal avgDailyRunTimeMax) {
        this.avgDailyRunTimeMax = avgDailyRunTimeMax;
    }

    public BigDecimal getHundredsPowerMin() {
        return hundredsPowerMin;
    }

    public void setHundredsPowerMin(BigDecimal hundredsPowerMin) {
        this.hundredsPowerMin = hundredsPowerMin;
    }

    public BigDecimal getHundredsPowerMax() {
        return hundredsPowerMax;
    }

    public void setHundredsPowerMax(BigDecimal hundredsPowerMax) {
        this.hundredsPowerMax = hundredsPowerMax;
    }

    public BigDecimal getSocTotalcapacity() {
        return socTotalcapacity;
    }

    public void setSocTotalcapacity(BigDecimal socTotalcapacity) {
        this.socTotalcapacity = socTotalcapacity;
    }
}
