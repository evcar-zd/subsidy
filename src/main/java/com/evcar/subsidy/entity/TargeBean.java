package com.evcar.subsidy.entity;

import com.evcar.subsidy.util.Constant;

import java.math.BigDecimal;

/**
 * Created by Kong on 2017/4/20.
 */
public class TargeBean {
    private LgAndLt mileage ;                   //车辆累积行驶里程
    private LgAndLt limitMileage ;              //续驶里程
    private LgAndLt maxEnergyTime ;             //一次充满电所用最少时间
    private LgAndLt maxElectricPower ;          //最大充电功率
    private LgAndLt avgDailyRunTime ;           //平均单日运行时间
    private LgAndLt hundredsKmusePower ;        //百公里耗电

    public TargeBean(){

    }

    public TargeBean(String carType){
        switch (carType){
            case Constant.VEHICLE_V18 :
                this.mileage = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(220));
                this.limitMileage = new LgAndLt(BigDecimal.valueOf(155),BigDecimal.valueOf(180)) ;
                this.maxEnergyTime = new LgAndLt(BigDecimal.valueOf(6),BigDecimal.valueOf(9)) ;
                this.maxElectricPower = new LgAndLt(BigDecimal.valueOf(1300),BigDecimal.valueOf(3400)) ;
                this.avgDailyRunTime = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(5)) ;
                this.hundredsKmusePower = new LgAndLt(BigDecimal.valueOf(10.749),BigDecimal.valueOf(11.05)) ;
                break;
            case Constant.VEHICLE_V19 :
                this.mileage = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(220));
                this.limitMileage = new LgAndLt(BigDecimal.valueOf(155),BigDecimal.valueOf(180)) ;
                this.maxEnergyTime = new LgAndLt(BigDecimal.valueOf(6),BigDecimal.valueOf(9)) ;
                this.maxElectricPower = new LgAndLt(BigDecimal.valueOf(1300),BigDecimal.valueOf(3400)) ;
                this.avgDailyRunTime = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(5)) ;
                this.hundredsKmusePower = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(13.9)) ;
                break;
            case Constant.VEHICLE_V22 :
                this.mileage = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(220));
                this.limitMileage = new LgAndLt(BigDecimal.valueOf(155),BigDecimal.valueOf(180)) ;
                this.maxEnergyTime = new LgAndLt(BigDecimal.valueOf(6),BigDecimal.valueOf(9)) ;
                this.maxElectricPower = new LgAndLt(BigDecimal.valueOf(1300),BigDecimal.valueOf(3400)) ;
                this.avgDailyRunTime = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(5)) ;
                this.hundredsKmusePower = new LgAndLt(BigDecimal.valueOf(9.81),BigDecimal.valueOf(11.05)) ;
                break;
            case Constant.VEHICLE_V23 :
                this.mileage = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(220));
                this.limitMileage = new LgAndLt(BigDecimal.valueOf(155),BigDecimal.valueOf(180)) ;
                this.maxEnergyTime = new LgAndLt(BigDecimal.valueOf(6),BigDecimal.valueOf(9)) ;
                this.maxElectricPower = new LgAndLt(BigDecimal.valueOf(1300),BigDecimal.valueOf(3400)) ;
                this.avgDailyRunTime = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(5)) ;
                this.hundredsKmusePower = new LgAndLt(BigDecimal.valueOf(9.34),BigDecimal.valueOf(11.35)) ;
                break;
            case Constant.VEHICLE_V60 :
                this.mileage = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(220));
                this.limitMileage = new LgAndLt(BigDecimal.valueOf(161),BigDecimal.valueOf(180)) ;
                this.maxEnergyTime = new LgAndLt(BigDecimal.valueOf(6),BigDecimal.valueOf(9)) ;
                this.maxElectricPower = new LgAndLt(BigDecimal.valueOf(1300),BigDecimal.valueOf(3400)) ;
                this.avgDailyRunTime = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(5)) ;
                this.hundredsKmusePower = new LgAndLt(BigDecimal.valueOf(10.43),BigDecimal.valueOf(11.05)) ;
                break;
            case Constant.VEHICLE_V61 :
                this.mileage = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(220));
                this.limitMileage = new LgAndLt(BigDecimal.valueOf(155),BigDecimal.valueOf(180)) ;
                this.maxEnergyTime = new LgAndLt(BigDecimal.valueOf(6),BigDecimal.valueOf(9)) ;
                this.maxElectricPower = new LgAndLt(BigDecimal.valueOf(1300),BigDecimal.valueOf(3400)) ;
                this.avgDailyRunTime = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(5)) ;
                this.hundredsKmusePower = new LgAndLt(BigDecimal.valueOf(10.419),BigDecimal.valueOf(11.35)) ;
                break;
            case Constant.VEHICLE_V34 :
                this.mileage = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(220));
                this.limitMileage = new LgAndLt(BigDecimal.valueOf(161),BigDecimal.valueOf(180)) ;
                this.maxEnergyTime = new LgAndLt(BigDecimal.valueOf(6),BigDecimal.valueOf(9)) ;
                this.maxElectricPower = new LgAndLt(BigDecimal.valueOf(1300),BigDecimal.valueOf(3400)) ;
                this.avgDailyRunTime = new LgAndLt(BigDecimal.valueOf(0),BigDecimal.valueOf(5)) ;
                this.hundredsKmusePower = new LgAndLt(BigDecimal.valueOf(10.29),BigDecimal.valueOf(11.05)) ;
                break;
        }
    }

    public LgAndLt getMileage() {
        return mileage;
    }

    public void setMileage(LgAndLt mileage) {
        this.mileage = mileage;
    }

    public LgAndLt getLimitMileage() {
        return limitMileage;
    }

    public void setLimitMileage(LgAndLt limitMileage) {
        this.limitMileage = limitMileage;
    }

    public LgAndLt getMaxEnergyTime() {
        return maxEnergyTime;
    }

    public void setMaxEnergyTime(LgAndLt maxEnergyTime) {
        this.maxEnergyTime = maxEnergyTime;
    }

    public LgAndLt getMaxElectricPower() {
        return maxElectricPower;
    }

    public void setMaxElectricPower(LgAndLt maxElectricPower) {
        this.maxElectricPower = maxElectricPower;
    }

    public LgAndLt getAvgDailyRunTime() {
        return avgDailyRunTime;
    }

    public void setAvgDailyRunTime(LgAndLt avgDailyRunTime) {
        this.avgDailyRunTime = avgDailyRunTime;
    }

    public LgAndLt getHundredsKmusePower() {
        return hundredsKmusePower;
    }

    public void setHundredsKmusePower(LgAndLt hundredsKmusePower) {
        this.hundredsKmusePower = hundredsKmusePower;
    }
}
