package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * OBC历史数据
 * Created by Kong on 2017/4/20.
 */
public class RtObcData implements Serializable {

    private Date collectTime ;              //采集时间
    private Date reciveTime ;               //接收时间
    private String vinCode ;                //VIN
    private String gprsCode ;               //GPRS号
    private BigDecimal outVoltage ;         //充电机输出电压
    private BigDecimal outCurrent ;         //充电机输出电流
    private Integer isHardErr ;             //硬件故障
    private Integer isTempHigh ;            //充电机温度状态
    private Integer isVoltageErr ;          //输入电压状态
    private Integer isRunning ;             //启动状态
    private Integer isCommected ;           //通信状态
    private Integer isReady ;               //充电准备就绪
    private BigDecimal inVoltage ;          //输入电压
    private BigDecimal inCurrent ;          //输入电流
    private BigDecimal pfcVoltage ;         //PEC电压
    private BigDecimal v12Voltage ;         //12V电压
    private BigDecimal v12Current ;         //12V电流
    private Integer hardOutkwLevel ;        //硬件输出功率等级
    private Integer hardOutcurrentLevel ;   //硬件输出电流等级
    private Integer temprature1 ;           //温度1
    private Integer temprature2 ;           //温度2
    private Integer temprature3 ;           //温度3
    private Integer fanStatus ;             //风扇状态
    private Integer chargerStatus ;         //充电状态
    private Integer tempratureError ;       //充电机温度异常监控
    private Integer inUpdervoltage1 ;       //输入欠压1
    private Integer inUpdervoltage2 ;       //输入欠压2
    private Integer inOutvoltage ;          //输入过压
    private Integer highvolOutUpdervol ;    //高压输出欠压
    private Integer highvolOutOutdervol ;   //高压输出过压
    private Integer outOutcurrent ;         //输出过流
    private Integer pfcVolError ;           //PFC电压异常
    private Integer v12OutvolError ;        //充电机12V过压异常
    private Integer v12UpdervolError ;      //充电机12V欠压异常


    public RtObcData(){

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

    public BigDecimal getOutVoltage() {
        return outVoltage;
    }

    public void setOutVoltage(BigDecimal outVoltage) {
        this.outVoltage = outVoltage;
    }

    public BigDecimal getOutCurrent() {
        return outCurrent;
    }

    public void setOutCurrent(BigDecimal outCurrent) {
        this.outCurrent = outCurrent;
    }

    public Integer getIsHardErr() {
        return isHardErr;
    }

    public void setIsHardErr(Integer isHardErr) {
        this.isHardErr = isHardErr;
    }

    public Integer getIsTempHigh() {
        return isTempHigh;
    }

    public void setIsTempHigh(Integer isTempHigh) {
        this.isTempHigh = isTempHigh;
    }

    public Integer getIsVoltageErr() {
        return isVoltageErr;
    }

    public void setIsVoltageErr(Integer isVoltageErr) {
        this.isVoltageErr = isVoltageErr;
    }

    public Integer getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(Integer isRunning) {
        this.isRunning = isRunning;
    }

    public Integer getIsCommected() {
        return isCommected;
    }

    public void setIsCommected(Integer isCommected) {
        this.isCommected = isCommected;
    }

    public Integer getIsReady() {
        return isReady;
    }

    public void setIsReady(Integer isReady) {
        this.isReady = isReady;
    }

    public BigDecimal getInVoltage() {
        return inVoltage;
    }

    public void setInVoltage(BigDecimal inVoltage) {
        this.inVoltage = inVoltage;
    }

    public BigDecimal getInCurrent() {
        return inCurrent;
    }

    public void setInCurrent(BigDecimal inCurrent) {
        this.inCurrent = inCurrent;
    }

    public BigDecimal getPfcVoltage() {
        return pfcVoltage;
    }

    public void setPfcVoltage(BigDecimal pfcVoltage) {
        this.pfcVoltage = pfcVoltage;
    }

    public BigDecimal getV12Voltage() {
        return v12Voltage;
    }

    public void setV12Voltage(BigDecimal v12Voltage) {
        this.v12Voltage = v12Voltage;
    }

    public BigDecimal getV12Current() {
        return v12Current;
    }

    public void setV12Current(BigDecimal v12Current) {
        this.v12Current = v12Current;
    }

    public Integer getHardOutkwLevel() {
        return hardOutkwLevel;
    }

    public void setHardOutkwLevel(Integer hardOutkwLevel) {
        this.hardOutkwLevel = hardOutkwLevel;
    }

    public Integer getHardOutcurrentLevel() {
        return hardOutcurrentLevel;
    }

    public void setHardOutcurrentLevel(Integer hardOutcurrentLevel) {
        this.hardOutcurrentLevel = hardOutcurrentLevel;
    }

    public Integer getTemprature1() {
        return temprature1;
    }

    public void setTemprature1(Integer temprature1) {
        this.temprature1 = temprature1;
    }

    public Integer getTemprature2() {
        return temprature2;
    }

    public void setTemprature2(Integer temprature2) {
        this.temprature2 = temprature2;
    }

    public Integer getTemprature3() {
        return temprature3;
    }

    public void setTemprature3(Integer temprature3) {
        this.temprature3 = temprature3;
    }

    public Integer getFanStatus() {
        return fanStatus;
    }

    public void setFanStatus(Integer fanStatus) {
        this.fanStatus = fanStatus;
    }

    public Integer getChargerStatus() {
        return chargerStatus;
    }

    public void setChargerStatus(Integer chargerStatus) {
        this.chargerStatus = chargerStatus;
    }

    public Integer getTempratureError() {
        return tempratureError;
    }

    public void setTempratureError(Integer tempratureError) {
        this.tempratureError = tempratureError;
    }

    public Integer getInUpdervoltage1() {
        return inUpdervoltage1;
    }

    public void setInUpdervoltage1(Integer inUpdervoltage1) {
        this.inUpdervoltage1 = inUpdervoltage1;
    }

    public Integer getInUpdervoltage2() {
        return inUpdervoltage2;
    }

    public void setInUpdervoltage2(Integer inUpdervoltage2) {
        this.inUpdervoltage2 = inUpdervoltage2;
    }

    public Integer getInOutvoltage() {
        return inOutvoltage;
    }

    public void setInOutvoltage(Integer inOutvoltage) {
        this.inOutvoltage = inOutvoltage;
    }

    public Integer getHighvolOutUpdervol() {
        return highvolOutUpdervol;
    }

    public void setHighvolOutUpdervol(Integer highvolOutUpdervol) {
        this.highvolOutUpdervol = highvolOutUpdervol;
    }

    public Integer getHighvolOutOutdervol() {
        return highvolOutOutdervol;
    }

    public void setHighvolOutOutdervol(Integer highvolOutOutdervol) {
        this.highvolOutOutdervol = highvolOutOutdervol;
    }

    public Integer getOutOutcurrent() {
        return outOutcurrent;
    }

    public void setOutOutcurrent(Integer outOutcurrent) {
        this.outOutcurrent = outOutcurrent;
    }

    public Integer getPfcVolError() {
        return pfcVolError;
    }

    public void setPfcVolError(Integer pfcVolError) {
        this.pfcVolError = pfcVolError;
    }

    public Integer getV12OutvolError() {
        return v12OutvolError;
    }

    public void setV12OutvolError(Integer v12OutvolError) {
        this.v12OutvolError = v12OutvolError;
    }

    public Integer getV12UpdervolError() {
        return v12UpdervolError;
    }

    public void setV12UpdervolError(Integer v12UpdervolError) {
        this.v12UpdervolError = v12UpdervolError;
    }
}
