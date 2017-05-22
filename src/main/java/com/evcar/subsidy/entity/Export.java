package com.evcar.subsidy.entity;

import java.math.BigDecimal;

/**
 * Created by lyg on 2017/5/16.
 */
public class Export {

    private String vinCode;
    private BigDecimal mileage  ;
    private BigDecimal limitMileage;
    private BigDecimal maxEnergyTime;
    private BigDecimal maxElectricPower;
    private BigDecimal avgDailyRunTime;
    private BigDecimal hundredsKmusePower;

    public Export(String vinCode, BigDecimal mileage, BigDecimal limitMileage, BigDecimal maxEnergyTime, BigDecimal maxElectricPower, BigDecimal avgDailyRunTime, BigDecimal hundredsKmusePower) {
        this.vinCode = vinCode;
        this.mileage = mileage;
        this.limitMileage = limitMileage;
        this.maxEnergyTime = maxEnergyTime;
        this.maxElectricPower = maxElectricPower;
        this.avgDailyRunTime = avgDailyRunTime;
        this.hundredsKmusePower = hundredsKmusePower;
    }

    public Export() {
        super();
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

    @Override
    public String toString() {
        return "Export{" +
                "vinCode='" + vinCode + '\'' +
                ", mileage=" + mileage +
                ", limitMileage=" + limitMileage +
                ", maxEnergyTime=" + maxEnergyTime +
                ", maxElectricPower=" + maxElectricPower +
                ", avgDailyRunTime=" + avgDailyRunTime +
                ", hundredsKmusePower=" + hundredsKmusePower +
                '}';
    }
}
