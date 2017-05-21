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
    private BigDecimal energyTimeMinModel1 ;
    private BigDecimal energyTimeMaxModel1 ;
    private BigDecimal energyTimeMinModel2 ;
    private BigDecimal energyTimeMaxModel2 ;
    private BigDecimal energyTimeMinModel3 ;
    private BigDecimal energyTimeMaxModel3 ;
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

    public BigDecimal getEnergyTimeMinModel1() {
        return energyTimeMinModel1;
    }

    public void setEnergyTimeMinModel1(BigDecimal energyTimeMinModel1) {
        this.energyTimeMinModel1 = energyTimeMinModel1;
    }

    public BigDecimal getEnergyTimeMaxModel1() {
        return energyTimeMaxModel1;
    }

    public void setEnergyTimeMaxModel1(BigDecimal energyTimeMaxModel1) {
        this.energyTimeMaxModel1 = energyTimeMaxModel1;
    }

    public BigDecimal getEnergyTimeMinModel2() {
        return energyTimeMinModel2;
    }

    public void setEnergyTimeMinModel2(BigDecimal energyTimeMinModel2) {
        this.energyTimeMinModel2 = energyTimeMinModel2;
    }

    public BigDecimal getEnergyTimeMaxModel2() {
        return energyTimeMaxModel2;
    }

    public void setEnergyTimeMaxModel2(BigDecimal energyTimeMaxModel2) {
        this.energyTimeMaxModel2 = energyTimeMaxModel2;
    }

    public BigDecimal getEnergyTimeMinModel3() {
        return energyTimeMinModel3;
    }

    public void setEnergyTimeMinModel3(BigDecimal energyTimeMinModel3) {
        this.energyTimeMinModel3 = energyTimeMinModel3;
    }

    public BigDecimal getEnergyTimeMaxModel3() {
        return energyTimeMaxModel3;
    }

    public void setEnergyTimeMaxModel3(BigDecimal energyTimeMaxModel3) {
        this.energyTimeMaxModel3 = energyTimeMaxModel3;
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
