package com.evcar.subsidy.common;

import java.io.Serializable;

/**
 * Created by Kong on 2017/4/20.
 */
public class TargeObject implements Serializable {

    private Boolean mileage ;                   //车辆累积行驶里程
    private Boolean limitMileage ;              //续驶里程
    private Boolean maxEnergyTime ;             //一次充满电所用最少时间
    private Boolean maxElectricPower ;          //最大充电功率
    private Boolean avgDailyRunTime ;           //平均单日运行时间
    private Boolean hundredsKmusePower ;        //百公里耗电

    public TargeObject(){

    }

    public Boolean getMileage() {
        return mileage;
    }

    public void setMileage(Boolean mileage) {
        this.mileage = mileage;
    }

    public Boolean getLimitMileage() {
        return limitMileage;
    }

    public void setLimitMileage(Boolean limitMileage) {
        this.limitMileage = limitMileage;
    }

    public Boolean getMaxEnergyTime() {
        return maxEnergyTime;
    }

    public void setMaxEnergyTime(Boolean maxEnergyTime) {
        this.maxEnergyTime = maxEnergyTime;
    }

    public Boolean getMaxElectricPower() {
        return maxElectricPower;
    }

    public void setMaxElectricPower(Boolean maxElectricPower) {
        this.maxElectricPower = maxElectricPower;
    }

    public Boolean getAvgDailyRunTime() {
        return avgDailyRunTime;
    }

    public void setAvgDailyRunTime(Boolean avgDailyRunTime) {
        this.avgDailyRunTime = avgDailyRunTime;
    }

    public Boolean getHundredsKmusePower() {
        return hundredsKmusePower;
    }

    public void setHundredsKmusePower(Boolean hundredsKmusePower) {
        this.hundredsKmusePower = hundredsKmusePower;
    }
}
