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
    private String vinCode ;                    //VIN
    private String gprsCode ;                   //GPRS号
    private String carType ;                    //车类型
    private Integer consumeSoc ;                //消耗SOC
    private Integer chargeSoc ;                 //充电SOC
    private Integer surplusSoc ;                //剩余SOC
    private BigDecimal mileageTotal ;           //总里程
    private Long chargeTime ;                   //充电时间(单位：S)
    private Long dischargeTime ;                //放电时间(单位：S)
    private BigDecimal maxInElectricPower ;     //最大电功率1
    private BigDecimal maxOutElectricPower ;    //最大电功率2
    private Integer gpsNumber ;                 //GPS数据条数
    private Integer canNumber ;                 //CAN数据条数
    private Integer linearSoc ;                 //近似线性中段当日总放电SOC
    private BigDecimal linearMileage ;          //近似线性中段当日总行驶里程
    private Date veDeliveredDate ;              //交车日期
    private Date runDate ;                      //数据时间
    private Date countDate ;                    //计算时间


    public HisCountData(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getGprsCode() {
        return gprsCode;
    }

    public void setGprsCode(String gprsCode) {
        this.gprsCode = gprsCode;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public Integer getConsumeSoc() {
        return consumeSoc;
    }

    public void setConsumeSoc(Integer consumeSoc) {
        this.consumeSoc = consumeSoc;
    }

    public Integer getChargeSoc() {
        return chargeSoc;
    }

    public void setChargeSoc(Integer chargeSoc) {
        this.chargeSoc = chargeSoc;
    }

    public Integer getSurplusSoc() {
        return surplusSoc;
    }

    public void setSurplusSoc(Integer surplusSoc) {
        this.surplusSoc = surplusSoc;
    }

    public BigDecimal getMileageTotal() {
        return mileageTotal;
    }

    public void setMileageTotal(BigDecimal mileageTotal) {
        this.mileageTotal = mileageTotal;
    }

    public Long getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Long chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Long getDischargeTime() {
        return dischargeTime;
    }

    public void setDischargeTime(Long dischargeTime) {
        this.dischargeTime = dischargeTime;
    }

    public BigDecimal getMaxInElectricPower() {
        return maxInElectricPower;
    }

    public void setMaxInElectricPower(BigDecimal maxInElectricPower) {
        this.maxInElectricPower = maxInElectricPower;
    }

    public BigDecimal getMaxOutElectricPower() {
        return maxOutElectricPower;
    }

    public void setMaxOutElectricPower(BigDecimal maxOutElectricPower) {
        this.maxOutElectricPower = maxOutElectricPower;
    }

    public Integer getGpsNumber() {
        return gpsNumber;
    }

    public void setGpsNumber(Integer gpsNumber) {
        this.gpsNumber = gpsNumber;
    }

    public Integer getCanNumber() {
        return canNumber;
    }

    public void setCanNumber(Integer canNumber) {
        this.canNumber = canNumber;
    }

    public Integer getLinearSoc() {
        return linearSoc;
    }

    public void setLinearSoc(Integer linearSoc) {
        this.linearSoc = linearSoc;
    }

    public BigDecimal getLinearMileage() {
        return linearMileage;
    }

    public void setLinearMileage(BigDecimal linearMileage) {
        this.linearMileage = linearMileage;
    }

    public Date getVeDeliveredDate() {
        return veDeliveredDate;
    }

    public void setVeDeliveredDate(Date veDeliveredDate) {
        this.veDeliveredDate = veDeliveredDate;
    }

    public Date getRunDate() {
        return runDate;
    }

    public void setRunDate(Date runDate) {
        this.runDate = runDate;
    }

    public Date getCountDate() {
        return countDate;
    }

    public void setCountDate(Date countDate) {
        this.countDate = countDate;
    }
}
