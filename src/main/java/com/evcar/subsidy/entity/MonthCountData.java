package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 计算数据L3
 * Created by Kong on 2017/4/21.
 */
public class MonthCountData implements Serializable {

    private String id ;                         //根据时间生成
    private Date tm ;                           //数据时间
    private Integer gpsNormal ;                 //gps正常车辆
    private Integer gpsNearNoData ;             //gps近期数据
    private Integer gpsNoData ;                 //gps无数据
    private Integer canNormal ;                 //CAN正常车辆
    private Integer canNearNoData ;             //CAN近期数据
    private Integer canNoData ;                 //CAN无数据

    private Statistical mileage ;               //车辆累积行驶里程
    private Statistical limitMileage ;          //续驶里程
    private Statistical maxEnergyTime ;         //一次充满电所用最少时间
    private Statistical maxElectricPower ;      //最大充电功率
    private Statistical avgDailyRunTime ;       //平均单日运行时间
    private Statistical hundredsKmusePower ;    //百公里耗电

    private Date calcTime ;                     //计算时间
    private Integer vehicleNum ;                //车辆数量
    private String version ;                    //版本号

    public MonthCountData(){
        this.gpsNormal = 0 ;
        this.gpsNearNoData = 0 ;
        this.gpsNoData = 0 ;
        this.canNormal = 0 ;
        this.canNearNoData = 0 ;
        this.canNoData = 0 ;
        this.vehicleNum = 0 ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTm() {
        return tm;
    }

    public void setTm(Date tm) {
        this.tm = tm;
    }

    public Integer getGpsNormal() {
        return gpsNormal;
    }

    public void setGpsNormal(Integer gpsNormal) {
        this.gpsNormal = gpsNormal;
    }

    public Integer getGpsNearNoData() {
        return gpsNearNoData;
    }

    public void setGpsNearNoData(Integer gpsNearNoData) {
        this.gpsNearNoData = gpsNearNoData;
    }

    public Integer getGpsNoData() {
        return gpsNoData;
    }

    public void setGpsNoData(Integer gpsNoData) {
        this.gpsNoData = gpsNoData;
    }

    public Integer getCanNormal() {
        return canNormal;
    }

    public void setCanNormal(Integer canNormal) {
        this.canNormal = canNormal;
    }

    public Integer getCanNearNoData() {
        return canNearNoData;
    }

    public void setCanNearNoData(Integer canNearNoData) {
        this.canNearNoData = canNearNoData;
    }

    public Integer getCanNoData() {
        return canNoData;
    }

    public void setCanNoData(Integer canNoData) {
        this.canNoData = canNoData;
    }

    public Statistical getMileage() {
        return mileage;
    }

    public void setMileage(Statistical mileage) {
        this.mileage = mileage;
    }

    public Statistical getLimitMileage() {
        return limitMileage;
    }

    public void setLimitMileage(Statistical limitMileage) {
        this.limitMileage = limitMileage;
    }

    public Statistical getMaxEnergyTime() {
        return maxEnergyTime;
    }

    public void setMaxEnergyTime(Statistical maxEnergyTime) {
        this.maxEnergyTime = maxEnergyTime;
    }

    public Statistical getMaxElectricPower() {
        return maxElectricPower;
    }

    public void setMaxElectricPower(Statistical maxElectricPower) {
        this.maxElectricPower = maxElectricPower;
    }

    public Statistical getAvgDailyRunTime() {
        return avgDailyRunTime;
    }

    public void setAvgDailyRunTime(Statistical avgDailyRunTime) {
        this.avgDailyRunTime = avgDailyRunTime;
    }

    public Statistical getHundredsKmusePower() {
        return hundredsKmusePower;
    }

    public void setHundredsKmusePower(Statistical hundredsKmusePower) {
        this.hundredsKmusePower = hundredsKmusePower;
    }

    public Date getCalcTime() {
        return calcTime;
    }

    public void setCalcTime(Date calcTime) {
        this.calcTime = calcTime;
    }

    public Integer getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(Integer vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
