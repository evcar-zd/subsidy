package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Kong on 2017/5/16.
 */
public class HvacData implements Serializable {

    private Date collectTime ;              //采集时间
    private Date reciveTime ;               //接收时间
    private String vinCode ;                //VIN码
    private String gprsCode ;               //GPRS号
    private Integer runStatus ;             //空调启动状态
    private Integer hvacLevel ;             //空调风机档位
    private Integer power ;                 //空调功率
    private Integer exTemp ;                //车外温度
    private Integer innerTemp ;             //车内温度
    private Integer crondDirection ;        //空调风向状态
    private Integer cirleModel ;            //空调循环模式状态
    private Integer errModel ;              //模式电机故障
    private Integer errTemp ;               //温度电机故障
    private Integer errEvalsensor ;         //蒸发器传感器故障
    private Integer errTempSensor ;         //回风温度传感器故障

    public HvacData(){

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

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public Integer getHvacLevel() {
        return hvacLevel;
    }

    public void setHvacLevel(Integer hvacLevel) {
        this.hvacLevel = hvacLevel;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getExTemp() {
        return exTemp;
    }

    public void setExTemp(Integer exTemp) {
        this.exTemp = exTemp;
    }

    public Integer getInnerTemp() {
        return innerTemp;
    }

    public void setInnerTemp(Integer innerTemp) {
        this.innerTemp = innerTemp;
    }

    public Integer getCrondDirection() {
        return crondDirection;
    }

    public void setCrondDirection(Integer crondDirection) {
        this.crondDirection = crondDirection;
    }

    public Integer getCirleModel() {
        return cirleModel;
    }

    public void setCirleModel(Integer cirleModel) {
        this.cirleModel = cirleModel;
    }

    public Integer getErrModel() {
        return errModel;
    }

    public void setErrModel(Integer errModel) {
        this.errModel = errModel;
    }

    public Integer getErrTemp() {
        return errTemp;
    }

    public void setErrTemp(Integer errTemp) {
        this.errTemp = errTemp;
    }

    public Integer getErrEvalsensor() {
        return errEvalsensor;
    }

    public void setErrEvalsensor(Integer errEvalsensor) {
        this.errEvalsensor = errEvalsensor;
    }

    public Integer getErrTempSensor() {
        return errTempSensor;
    }

    public void setErrTempSensor(Integer errTempSensor) {
        this.errTempSensor = errTempSensor;
    }
}
