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
    private Integer consumeSoc ;                //消耗SOC
    private Integer chargeSoc ;                 //充电SOC
    private Integer surplusSoc ;                //剩余SOC
    private BigDecimal mileagePoor ;            //里程差
    private BigDecimal mileageTotal ;           //总里程
    private Long chargeTime ;                   //充电时间(单位：S)
    private Long dischargeTime ;                //放电时间(单位：S)
    private BigDecimal maxElectricPower ;       //最大电功率
    private Integer gpsNumber ;                 //GPS数据条数
    private Integer canNumber ;                 //CAN数据条数
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

    public BigDecimal getMileagePoor() {
        return mileagePoor;
    }

    public void setMileagePoor(BigDecimal mileagePoor) {
        this.mileagePoor = mileagePoor;
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

    public BigDecimal getMaxElectricPower() {
        return maxElectricPower;
    }

    public void setMaxElectricPower(BigDecimal maxElectricPower) {
        this.maxElectricPower = maxElectricPower;
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

    public Date getCountDate() {
        return countDate;
    }

    public void setCountDate(Date countDate) {
        this.countDate = countDate;
    }
}
