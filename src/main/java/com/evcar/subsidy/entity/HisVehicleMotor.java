package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 历史整车和电机数据
 * Created by Kong on 2017/4/20.
 */
public class HisVehicleMotor implements Serializable {

    private Date collectTime ;                  //采集时间
    private Date reciveTime ;                   //接收时间
    private String vinCode ;                    //VIN
    private String gprsCode ;                   //GPRS号
    private Integer isOntime ;                  //是否补传      0 实时上传,1 盲区补传
    private Integer vehicleStatus ;             //车辆状态（Ready信号） 1启动，2熄火,3其他状态
    private Integer chargeStatus ;              //充电状态      1停车充电，2行驶充电，3未充电状态，4充电完成
    private BigDecimal vehicleSpeed ;           //车速
    private BigDecimal mileage ;                //里程
    private BigDecimal totalVoltage ;           //总电压
    private BigDecimal totalCurrent ;           //总电流
    private Integer soc ;                       //SOC
    private Integer dcdcStatus ;                //DC-DC状态   1工作，2断开
    private Integer gearStatus ;                //档位        0：N档，1：D档，2：R档
    private Integer driveBrakeStatus ;          //驱动力制动力状态  0：无驱动力无制动力（00），1：无驱动力有制动力（01）,2：有驱动力无制动力（10）,3：有驱动力有制动力（11）
    private Integer insuResistance ;            //绝缘电阻      单位 kΩ
    private Integer motorStatus ;               //驱动电机状态    1耗电，2发电，3关闭状态，4准备状态
    private Integer mcTemprature ;              //电机控制器温度
    private Integer motorRpm ;                  //电机转速
    private BigDecimal mcNm ;                   //电机转矩
    private Integer motorTemprature ;           //电机温度
    private BigDecimal voltageRange ;           //电机输入电压
    private BigDecimal busCurrent ;             //电机母线电流

    public HisVehicleMotor(){

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

    public Integer getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(Integer vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public BigDecimal getVehicleSpeed() {
        return vehicleSpeed;
    }

    public void setVehicleSpeed(BigDecimal vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
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

    public Integer getSoc() {
        return soc;
    }

    public void setSoc(Integer soc) {
        this.soc = soc;
    }

    public Integer getDcdcStatus() {
        return dcdcStatus;
    }

    public void setDcdcStatus(Integer dcdcStatus) {
        this.dcdcStatus = dcdcStatus;
    }

    public Integer getGearStatus() {
        return gearStatus;
    }

    public void setGearStatus(Integer gearStatus) {
        this.gearStatus = gearStatus;
    }

    public Integer getDriveBrakeStatus() {
        return driveBrakeStatus;
    }

    public void setDriveBrakeStatus(Integer driveBrakeStatus) {
        this.driveBrakeStatus = driveBrakeStatus;
    }

    public Integer getInsuResistance() {
        return insuResistance;
    }

    public void setInsuResistance(Integer insuResistance) {
        this.insuResistance = insuResistance;
    }

    public Integer getMotorStatus() {
        return motorStatus;
    }

    public void setMotorStatus(Integer motorStatus) {
        this.motorStatus = motorStatus;
    }

    public Integer getMcTemprature() {
        return mcTemprature;
    }

    public void setMcTemprature(Integer mcTemprature) {
        this.mcTemprature = mcTemprature;
    }

    public Integer getMotorRpm() {
        return motorRpm;
    }

    public void setMotorRpm(Integer motorRpm) {
        this.motorRpm = motorRpm;
    }

    public BigDecimal getMcNm() {
        return mcNm;
    }

    public void setMcNm(BigDecimal mcNm) {
        this.mcNm = mcNm;
    }

    public Integer getMotorTemprature() {
        return motorTemprature;
    }

    public void setMotorTemprature(Integer motorTemprature) {
        this.motorTemprature = motorTemprature;
    }

    public BigDecimal getVoltageRange() {
        return voltageRange;
    }

    public void setVoltageRange(BigDecimal voltageRange) {
        this.voltageRange = voltageRange;
    }

    public BigDecimal getBusCurrent() {
        return busCurrent;
    }

    public void setBusCurrent(BigDecimal busCurrent) {
        this.busCurrent = busCurrent;
    }
}
