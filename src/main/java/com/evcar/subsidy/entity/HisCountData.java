package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 计算数据
 * Created by Kong on 2017/4/21.
 */
public class HisCountData implements Serializable {

    private String id ;                         //VIN+时间串
    private Date tm ;                           //数据时间
    private String vinCode ;                    //VIN
    private String carType ;                    //车类型
    private Date veDeliveredDate ;              //交车日期
    private Date releaseTime ;                  //下线日期
    private Integer gpsCount ;                  //GPS数据条数
    private Integer canCount ;                  //CAN数据条数
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

    public HisCountData(){

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
}
