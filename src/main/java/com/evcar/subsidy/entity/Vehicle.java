package com.evcar.subsidy.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * Created by Kong on 2017/4/19.
 */
public class Vehicle {
    private String carId ;
    private String vinCode ;
    private String carSeries ;
    private String carType ;
    private String carColor ;
    private Integer configuration ;
    private Integer batteryType ;
    private String machineNo ;
    private String controlNo ;
    private String batteryPackNumber ;
    private String gprsNo ;
    private String no3g ;
    private Integer factoryId ;
    private Date produceTime ;
    private Date releaseTime ;
    private Integer sendTo ;
    private Integer status ;
    private String areaCode ;
    @JsonProperty("carProvince")
    private List<CarProvince> carProvince ;
    @JsonProperty("carCity")
    private List<CarCity> carCity ;
    private String pseudoIp ;
    private String simNumber ;
    private String iccid ;
    private String customerName ;
    private String customerShortName ;
    private String spellingCode ;
    private String customerType ;
    private String customerCategory ;
    private String customerStatus ;
    private String province ;
    private String city ;
    private String county ;
    private String address ;
    private String mobileNo ;
    private Integer certificateType ;
    private String idNo ;
    private String dealerId ;
    private String dealerCode ;
    private String dealerName ;
    private Integer[] orgIds ;
    private String plateNo ;
    private Date plateDate ;
    private String vehicleUses ;
    private String vehicleCategory ;
    private Date soldDate ;
    private Date soldInvoiceDate ;
    private String soldNivoiceNo ;
    private Date veDeliveredDate ;
    private Integer flag ;

    public Vehicle(){

    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getCarSeries() {
        return carSeries;
    }

    public void setCarSeries(String carSeries) {
        this.carSeries = carSeries;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public Integer getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Integer configuration) {
        this.configuration = configuration;
    }

    public Integer getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(Integer batteryType) {
        this.batteryType = batteryType;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getControlNo() {
        return controlNo;
    }

    public void setControlNo(String controlNo) {
        this.controlNo = controlNo;
    }

    public String getBatteryPackNumber() {
        return batteryPackNumber;
    }

    public void setBatteryPackNumber(String batteryPackNumber) {
        this.batteryPackNumber = batteryPackNumber;
    }

    public String getGprsNo() {
        return gprsNo;
    }

    public void setGprsNo(String gprsNo) {
        this.gprsNo = gprsNo;
    }

    public String getNo3g() {
        return no3g;
    }

    public void setNo3g(String no3g) {
        this.no3g = no3g;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public Date getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(Date produceTime) {
        this.produceTime = produceTime;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getSendTo() {
        return sendTo;
    }

    public void setSendTo(Integer sendTo) {
        this.sendTo = sendTo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public List<CarProvince> getCarProvince() {
        return carProvince;
    }

    public void setCarProvince(List<CarProvince> carProvince) {
        this.carProvince = carProvince;
    }

    public List<CarCity> getCarCity() {
        return carCity;
    }

    public void setCarCity(List<CarCity> carCity) {
        this.carCity = carCity;
    }

    public String getPseudoIp() {
        return pseudoIp;
    }

    public void setPseudoIp(String pseudoIp) {
        this.pseudoIp = pseudoIp;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerShortName() {
        return customerShortName;
    }

    public void setCustomerShortName(String customerShortName) {
        this.customerShortName = customerShortName;
    }

    public String getSpellingCode() {
        return spellingCode;
    }

    public void setSpellingCode(String spellingCode) {
        this.spellingCode = spellingCode;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Integer getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(Integer certificateType) {
        this.certificateType = certificateType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Integer[] getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(Integer[] orgIds) {
        this.orgIds = orgIds;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public Date getPlateDate() {
        return plateDate;
    }

    public void setPlateDate(Date plateDate) {
        this.plateDate = plateDate;
    }

    public String getVehicleUses() {
        return vehicleUses;
    }

    public void setVehicleUses(String vehicleUses) {
        this.vehicleUses = vehicleUses;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public void setVehicleCategory(String vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    public Date getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(Date soldDate) {
        this.soldDate = soldDate;
    }

    public Date getSoldInvoiceDate() {
        return soldInvoiceDate;
    }

    public void setSoldInvoiceDate(Date soldInvoiceDate) {
        this.soldInvoiceDate = soldInvoiceDate;
    }

    public String getSoldNivoiceNo() {
        return soldNivoiceNo;
    }

    public void setSoldNivoiceNo(String soldNivoiceNo) {
        this.soldNivoiceNo = soldNivoiceNo;
    }

    public Date getVeDeliveredDate() {
        return veDeliveredDate;
    }

    public void setVeDeliveredDate(Date veDeliveredDate) {
        this.veDeliveredDate = veDeliveredDate;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
