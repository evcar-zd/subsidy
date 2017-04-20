package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实时电池数据
 * Created by Kong on 2017/4/20.
 */
public class RtBattery implements Serializable {

    private Date collectTime ;              //采集时间
    private Date reciveTime ;               //接收时间
    private String vinCode ;                //VIN
    private String gprsCode ;               //GPRS号
    private Integer isOntime ;              //是否补传  0 实时上传,1 盲区补传
    private BigDecimal totalVoltage ;       //动力蓄电池电压
    private BigDecimal totalCurrent ;       //动力蓄电池电流
    private Integer cellTotal ;             //单体电池总数
    private Integer tempProbeTotal ;        //温度探针个数
    private BigDecimal voltage ;            //单体电池电压数组
    private Integer temprature ;            //探头温度数组
    private BigDecimal voltageHighest ;     //最高单体电压
    private Integer voltageHighestNo ;      //最高单体电池号
    private BigDecimal voltageLowest ;      //最低单体电压
    private Integer voltageLowestNo ;       //最低单体电池号
    private Integer tempHighest ;           //最高温度点温度
    private Integer tempHighestNo ;         //最高温度点探针序号
    private Integer tempLowest ;            //最低温度点温度
    private Integer tempLowestNo ;          //最低温度点探针序号

    public RtBattery(){

    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public Date getReciveTime() {
        return reciveTime;
    }

    public void setReciveTime(Date reciveTime) {
        this.reciveTime = reciveTime;
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

    public Integer getIsOntime() {
        return isOntime;
    }

    public void setIsOntime(Integer isOntime) {
        this.isOntime = isOntime;
    }

    public BigDecimal getTotalVoltage() {
        return totalVoltage;
    }

    public void setTotalVoltage(BigDecimal totalVoltage) {
        this.totalVoltage = totalVoltage;
    }

    public BigDecimal getTotalCurrent() {
        return totalCurrent;
    }

    public void setTotalCurrent(BigDecimal totalCurrent) {
        this.totalCurrent = totalCurrent;
    }

    public Integer getCellTotal() {
        return cellTotal;
    }

    public void setCellTotal(Integer cellTotal) {
        this.cellTotal = cellTotal;
    }

    public Integer getTempProbeTotal() {
        return tempProbeTotal;
    }

    public void setTempProbeTotal(Integer tempProbeTotal) {
        this.tempProbeTotal = tempProbeTotal;
    }

    public BigDecimal getVoltage() {
        return voltage;
    }

    public void setVoltage(BigDecimal voltage) {
        this.voltage = voltage;
    }

    public Integer getTemprature() {
        return temprature;
    }

    public void setTemprature(Integer temprature) {
        this.temprature = temprature;
    }

    public BigDecimal getVoltageHighest() {
        return voltageHighest;
    }

    public void setVoltageHighest(BigDecimal voltageHighest) {
        this.voltageHighest = voltageHighest;
    }

    public Integer getVoltageHighestNo() {
        return voltageHighestNo;
    }

    public void setVoltageHighestNo(Integer voltageHighestNo) {
        this.voltageHighestNo = voltageHighestNo;
    }

    public BigDecimal getVoltageLowest() {
        return voltageLowest;
    }

    public void setVoltageLowest(BigDecimal voltageLowest) {
        this.voltageLowest = voltageLowest;
    }

    public Integer getVoltageLowestNo() {
        return voltageLowestNo;
    }

    public void setVoltageLowestNo(Integer voltageLowestNo) {
        this.voltageLowestNo = voltageLowestNo;
    }

    public Integer getTempHighest() {
        return tempHighest;
    }

    public void setTempHighest(Integer tempHighest) {
        this.tempHighest = tempHighest;
    }

    public Integer getTempHighestNo() {
        return tempHighestNo;
    }

    public void setTempHighestNo(Integer tempHighestNo) {
        this.tempHighestNo = tempHighestNo;
    }

    public Integer getTempLowest() {
        return tempLowest;
    }

    public void setTempLowest(Integer tempLowest) {
        this.tempLowest = tempLowest;
    }

    public Integer getTempLowestNo() {
        return tempLowestNo;
    }

    public void setTempLowestNo(Integer tempLowestNo) {
        this.tempLowestNo = tempLowestNo;
    }
}
