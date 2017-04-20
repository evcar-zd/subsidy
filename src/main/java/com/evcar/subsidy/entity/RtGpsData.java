package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实时GPRS数据
 * Created by Kong on 2017/4/20.
 */
public class RtGpsData implements Serializable {

    private Date collectTime;               //采集时间
    private Date reciveTime;                //接收时间
    private String vinCode;                 //VIN
    private String gprsCode;                //GPRS号
    private Integer isOntime;               //是否补传      0 实时上传,1 盲区补传
    private Point coodinate;                //经纬度坐标
    private BigDecimal speed;               //速度
    private Integer direction;              //方位         000——359度,正北为0度，顺时针方向计数
    private BigDecimal altitude;            //海拔
    private Integer locationSign;           //定位标识      0 GPS未定位，1 GPS已定位
    private Point marsCoodinate;            //火星坐标
    private Integer status;                 //车辆状态
    private CarProvince carProvince;        //所属省
    private CarCity carCity;                //所属市
    private String dealerId;                //经销商ID
    private Integer[] orgIds;               //组织机构ID数组
    private String batteryPackNumber ;      //电池包编号

    public RtGpsData(){

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

    public Point getCoodinate() {
        return coodinate;
    }

    public void setCoodinate(Point coodinate) {
        this.coodinate = coodinate;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public BigDecimal getAltitude() {
        return altitude;
    }

    public void setAltitude(BigDecimal altitude) {
        this.altitude = altitude;
    }

    public Integer getLocationSign() {
        return locationSign;
    }

    public void setLocationSign(Integer locationSign) {
        this.locationSign = locationSign;
    }

    public Point getMarsCoodinate() {
        return marsCoodinate;
    }

    public void setMarsCoodinate(Point marsCoodinate) {
        this.marsCoodinate = marsCoodinate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public CarProvince getCarProvince() {
        return carProvince;
    }

    public void setCarProvince(CarProvince carProvince) {
        this.carProvince = carProvince;
    }

    public CarCity getCarCity() {
        return carCity;
    }

    public void setCarCity(CarCity carCity) {
        this.carCity = carCity;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public Integer[] getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(Integer[] orgIds) {
        this.orgIds = orgIds;
    }

    public String getBatteryPackNumber() {
        return batteryPackNumber;
    }

    public void setBatteryPackNumber(String batteryPackNumber) {
        this.batteryPackNumber = batteryPackNumber;
    }
}