package com.evcar.subsidy.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆档案信息
 * Created by Kong on 2017/4/19.
 */
public class Vehicle implements Serializable{
    private String carId ;                      //车辆编号
    private String vinCode ;                    //VIN编码
    private String carSeries ;                  //车系
    private String carType ;                    //车辆型号-型号编码
    private String carColor ;                   //车辆颜色-颜色名称
    private Integer configuration ;             //配置
    private Integer batteryType ;               //电池类型
    private String machineNo ;                  //电机号
    private String controlNo ;                  //控制器号
    private String gearBoxNo ;                  //变速箱号
    private String batteryPackNumber ;          //电池包编号
    private String gprsNo ;                     //GPRS号
    private String no3g ;                       //3G卡号
    private Integer factoryId ;                 //产地标识
    private Date produceTime ;                  //上线日期
    private Date releaseTime ;                  //下线日期
    private Date storageTime ;                  //入库日期
    private Integer sendTo ;                    //销往
    private Integer status ;                    //车辆状态
    private String areaCode ;                   //所属地区
    @JsonProperty("carProvince")
    private CarProvince carProvince ;     //所属省
    @JsonProperty("carCity")
    private CarCity carCity ;             //所属市

    private String deviceType ;                 //设备型号-型号编码
    private Integer deviceClasses ;             //设备类别
    private String pseudoIp ;                   //伪IP
    private String simNumber ;                  //sim卡号
    private String iccid ;                      //ICCID
    private String customerName ;               //客户名称(车主名称)
    private String customerShortName ;          //客户简称
    private String spellingCode ;               //拼音代码
    private String customerType ;               //客户类型
    private String customerCategory ;           //客户分类
    private String customerStatus ;             //客户状态

    private String country ;                    //国家
    private String province ;                   //省份
    private String city ;                       //城市
    private String county ;                     //区县
    private String address ;                    //详细地址
    private String mobileNo ;                   //手机号码
    private String phoneNo ;                    //固定电话
    private String otherChannel ;               //其它联系方式
    private String email ;                      //电子邮箱
    private String zipCode ;                    //邮政编码
    private String gender ;                     //性别
    private Date birthday ;                     //生日
    private Integer certificateType ;           //证件类型
    private String idNo ;                       //证件号码
    private Integer vcCount ;                   //车辆数量
    private String dealerId ;                   //经销商ID
    private String dealerCode ;                 //经销商代码
    private String dealerName ;                 //经销商名称
    private Integer[] orgIds ;                  //组织机构ID数组
    private String plateNo ;                    //车牌号
    private Date plateDate ;                    //上牌日期
    private String vehicleUses ;                //车辆用途(车辆性质)
    private String vehicleCategory ;            //车辆类别
    private Date certificateDate ;              //合格证日期
    private String certificateNo ;              //合格证号
    private Date soldDate ;                     //销售日期(购车日期)
    private Date soldInvoiceDate ;              //销售发票日期
    private String soldNivoiceNo ;              //销售发票编号
    private Date veDeliveredDate ;              //交车日期
    private Integer flag ;                      //老通讯协议标记       1老通讯协议，0新通讯协议

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

    public String getGearBoxNo() {
        return gearBoxNo;
    }

    public void setGearBoxNo(String gearBoxNo) {
        this.gearBoxNo = gearBoxNo;
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

    public Date getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(Date storageTime) {
        this.storageTime = storageTime;
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

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getDeviceClasses() {
        return deviceClasses;
    }

    public void setDeviceClasses(Integer deviceClasses) {
        this.deviceClasses = deviceClasses;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getOtherChannel() {
        return otherChannel;
    }

    public void setOtherChannel(String otherChannel) {
        this.otherChannel = otherChannel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public Integer getVcCount() {
        return vcCount;
    }

    public void setVcCount(Integer vcCount) {
        this.vcCount = vcCount;
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

    public Date getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(Date certificateDate) {
        this.certificateDate = certificateDate;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
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
