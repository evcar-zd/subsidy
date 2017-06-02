package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 计算数据L2
 * Created by Kong on 2017/4/21.
 */
public class HisCountDataL2 implements Serializable {

    private String id ;                         //VIN+时间串
    private Date tm ;                           //数据时间
    private String vinCode ;                    //VIN
    private String carType ;                    //车类型
    private Date veDeliveredDate ;              //交车日期
    private Date releaseTime ;                  //下线日期
    private Integer gpsCount ;                  //GPS数据条数
    private Integer canCount ;                  //CAN数据条数
    private Integer gpshisCount ;               //GPS历史数据条数
    private Integer canhisCount ;               //CAN历史数据条数
    private BigDecimal mileageTotal ;           //总里程
    private Integer chargeMidSoc1 ;             //近似线性中段充电SOC - Model1
    private Long chargeMidSec1 ;                //近似线性中段充电时间(单位：S) - Model1
    private Integer chargeMidSoc2 ;             //近似线性中段充电SOC - Model2
    private Long chargeMidSec2 ;                //近似线性中段充电时间(单位：S) - Model2
    private Integer chargeMidSoc3 ;             //近似线性中段充电SOC - Model3
    private Long chargeMidSec3 ;                //近似线性中段充电时间(单位：S) - Model3
    private Long dischargeTotalSec ;            //放电总时长(单位：S)
    private BigDecimal maxInChargerPower ;      //最大输入电功率
    private BigDecimal maxOutChargerPower ;     //最大输出电功率
    private Integer dischargeMidSoc ;           //近似线性中段当日总放电SOC
    private BigDecimal dischargeMidMileage ;    //近似线性中段当日总行驶里程
    private Date calcTime ;                     //计算时间
    private String version ;                    //版本
    private BigDecimal mileage ;                //里程
    private BigDecimal limitMileage ;           //续驶里程
    private BigDecimal maxEnergyTime1 ;         //一次满电最少时间（单位：h） - Model1
    private BigDecimal maxEnergyTime2 ;         //一次满电最少时间（单位：h） - Model2
    private BigDecimal maxEnergyTime3 ;         //一次满电最少时间（单位：h） - Model3
    private BigDecimal maxElectricPower ;       //最大充电功率
    private BigDecimal avgDailyRunTime ;        //平均单日运行时间
    private BigDecimal hundredsKmusePower ;     //百公里耗电

    private Integer canMark ;                   //Can标注 0 近期无数据, 1 正常, -1 无数据
    private Integer gpsMark ;                   //GPS标注 0 近期无数据, 1 正常, -1 无数据
    private Integer mileageMark ;               //里程标注 -1 无效, 0 偏低, 1 正常, 2 偏高
    private Integer limitMileageMark ;          //续驶里程标注 -1 无效, 0 偏低, 1 正常, 2 偏高
    private Integer maxEnergyTimeMark ;         //满电最少时间标注 -1 无效, 0 偏低, 1 正常, 2 偏高
    private Integer maxElectricPowerMark ;      //最大充电功率标注 -1 无效, 0 偏低, 1 正常, 2 偏高
    private Integer avgDailyRunTimeMark ;       //平均单日运行时间标注 -1 无效, 0 偏低, 1 正常, 2 偏高
    private Integer hundredsKmusePowerMark ;    //百公里耗电标注 -1 无效, 0 偏低, 1 正常, 2 偏高

    public HisCountDataL2(){

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

    public Integer getGpsCount() {
        return gpsCount;
    }

    public void setGpsCount(Integer gpsCount) {
        this.gpsCount = gpsCount;
    }

    public Integer getCanCount() {
        return canCount;
    }

    public void setCanCount(Integer canCount) {
        this.canCount = canCount;
    }

    public Integer getGpshisCount() {
        return gpshisCount;
    }

    public void setGpshisCount(Integer gpshisCount) {
        this.gpshisCount = gpshisCount;
    }

    public Integer getCanhisCount() {
        return canhisCount;
    }

    public void setCanhisCount(Integer canhisCount) {
        this.canhisCount = canhisCount;
    }

    public BigDecimal getMileageTotal() {
        return mileageTotal;
    }

    public void setMileageTotal(BigDecimal mileageTotal) {
        this.mileageTotal = mileageTotal;
    }

    public Integer getChargeMidSoc1() {
        return chargeMidSoc1;
    }

    public void setChargeMidSoc1(Integer chargeMidSoc1) {
        this.chargeMidSoc1 = chargeMidSoc1;
    }

    public Long getChargeMidSec1() {
        return chargeMidSec1;
    }

    public void setChargeMidSec1(Long chargeMidSec1) {
        this.chargeMidSec1 = chargeMidSec1;
    }

    public Integer getChargeMidSoc2() {
        return chargeMidSoc2;
    }

    public void setChargeMidSoc2(Integer chargeMidSoc2) {
        this.chargeMidSoc2 = chargeMidSoc2;
    }

    public Long getChargeMidSec2() {
        return chargeMidSec2;
    }

    public void setChargeMidSec2(Long chargeMidSec2) {
        this.chargeMidSec2 = chargeMidSec2;
    }

    public Integer getChargeMidSoc3() {
        return chargeMidSoc3;
    }

    public void setChargeMidSoc3(Integer chargeMidSoc3) {
        this.chargeMidSoc3 = chargeMidSoc3;
    }

    public Long getChargeMidSec3() {
        return chargeMidSec3;
    }

    public void setChargeMidSec3(Long chargeMidSec3) {
        this.chargeMidSec3 = chargeMidSec3;
    }

    public Long getDischargeTotalSec() {
        return dischargeTotalSec;
    }

    public void setDischargeTotalSec(Long dischargeTotalSec) {
        this.dischargeTotalSec = dischargeTotalSec;
    }

    public BigDecimal getMaxInChargerPower() {
        return maxInChargerPower;
    }

    public void setMaxInChargerPower(BigDecimal maxInChargerPower) {
        this.maxInChargerPower = maxInChargerPower;
    }

    public BigDecimal getMaxOutChargerPower() {
        return maxOutChargerPower;
    }

    public void setMaxOutChargerPower(BigDecimal maxOutChargerPower) {
        this.maxOutChargerPower = maxOutChargerPower;
    }

    public Integer getDischargeMidSoc() {
        return dischargeMidSoc;
    }

    public void setDischargeMidSoc(Integer dischargeMidSoc) {
        this.dischargeMidSoc = dischargeMidSoc;
    }

    public BigDecimal getDischargeMidMileage() {
        return dischargeMidMileage;
    }

    public void setDischargeMidMileage(BigDecimal dischargeMidMileage) {
        this.dischargeMidMileage = dischargeMidMileage;
    }

    public Date getCalcTime() {
        return calcTime;
    }

    public void setCalcTime(Date calcTime) {
        this.calcTime = calcTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public BigDecimal getMaxEnergyTime1() {
        return maxEnergyTime1;
    }

    public void setMaxEnergyTime1(BigDecimal maxEnergyTime1) {
        this.maxEnergyTime1 = maxEnergyTime1;
    }

    public BigDecimal getMaxEnergyTime2() {
        return maxEnergyTime2;
    }

    public void setMaxEnergyTime2(BigDecimal maxEnergyTime2) {
        this.maxEnergyTime2 = maxEnergyTime2;
    }

    public BigDecimal getMaxEnergyTime3() {
        return maxEnergyTime3;
    }

    public void setMaxEnergyTime3(BigDecimal maxEnergyTime3) {
        this.maxEnergyTime3 = maxEnergyTime3;
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

    public Integer getCanMark() {
        return canMark;
    }

    public void setCanMark(Integer canMark) {
        this.canMark = canMark;
    }

    public Integer getGpsMark() {
        return gpsMark;
    }

    public void setGpsMark(Integer gpsMark) {
        this.gpsMark = gpsMark;
    }

    public Integer getMileageMark() {
        return mileageMark;
    }

    public void setMileageMark(Integer mileageMark) {
        this.mileageMark = mileageMark;
    }

    public Integer getLimitMileageMark() {
        return limitMileageMark;
    }

    public void setLimitMileageMark(Integer limitMileageMark) {
        this.limitMileageMark = limitMileageMark;
    }

    public Integer getMaxEnergyTimeMark() {
        return maxEnergyTimeMark;
    }

    public void setMaxEnergyTimeMark(Integer maxEnergyTimeMark) {
        this.maxEnergyTimeMark = maxEnergyTimeMark;
    }

    public Integer getMaxElectricPowerMark() {
        return maxElectricPowerMark;
    }

    public void setMaxElectricPowerMark(Integer maxElectricPowerMark) {
        this.maxElectricPowerMark = maxElectricPowerMark;
    }

    public Integer getAvgDailyRunTimeMark() {
        return avgDailyRunTimeMark;
    }

    public void setAvgDailyRunTimeMark(Integer avgDailyRunTimeMark) {
        this.avgDailyRunTimeMark = avgDailyRunTimeMark;
    }

    public Integer getHundredsKmusePowerMark() {
        return hundredsKmusePowerMark;
    }

    public void setHundredsKmusePowerMark(Integer hundredsKmusePowerMark) {
        this.hundredsKmusePowerMark = hundredsKmusePowerMark;
    }
}
