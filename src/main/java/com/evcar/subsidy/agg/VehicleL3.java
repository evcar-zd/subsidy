package com.evcar.subsidy.agg;

import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.util.Constant;
import com.evcar.subsidy.util.CountUtil;
import com.evcar.subsidy.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.evcar.subsidy.util.CountUtil.getTarget;

/**
 * Created by Kong on 2017/5/12.
 */
@Component
public class VehicleL3 extends VehicleBase {

    private static ESBean esBean;
    @Autowired
    void setEsBean(ESBean value) { this.esBean = value;}

    protected MonthCountData monthCountData ;

    /**
     * 计算L3数据
     * @param vehicleVos
     * @param startDate
     * @param endDate
     */
    public void calc(List<VehicleVo> vehicleVos, Date startDate , Date endDate){

        this.calcVehicle(vehicleVos,startDate,endDate) ;

        this.saveL3(monthCountData);
    }

    private void calcVehicle(List<VehicleVo> vehicleVos, Date startDate , Date endDate){
        monthCountData = new MonthCountData() ;

        String id = Constant.LOGO + DateUtil.getDateStryyyyMMdd(endDate);     //根据时间生成
        monthCountData.setId(id);
        monthCountData.setTm(endDate);
        Integer vehicleNum = 0 ;                //车辆数量

        for (VehicleVo vehicleVo : vehicleVos){
            String vinCode = vehicleVo.getVinCode() ;
            List<HisCountDataL2> hisCountDatas = this.loadL2(vinCode,startDate ,endDate);
            if (hisCountDatas.size()==0) continue;
            /** 数据处理 */
            this.calcHisCountData(hisCountDatas,vinCode,startDate ,endDate);

            vehicleNum ++ ;
        }
        monthCountData.setCalcTime(new Date());
        monthCountData.setVehicleNum(vehicleNum);

    }



    private void calcHisCountData(List<HisCountDataL2> hisCountDatas,String vinCode,Date startDate , Date endDate){
        Integer gpsNormal = 0 ;                 //gps正常车辆
        Integer gpsNearNoData = 0 ;             //gps近期数据
        Integer gpsNoData = 0 ;                 //gps无数据
        Integer canNormal = 0 ;                 //CAN正常车辆
        Integer canNearNoData = 0 ;             //CAN近期数据
        Integer canNoData = 0 ;                 //CAN无数据

        Statistical mileage = new Statistical();               //车辆累计行驶里程
        Statistical limitMileage = new Statistical();          //续驶里程
        Statistical maxEnergyTime = new Statistical();         //一次充满电所用最少时间
        Statistical maxElectricPower = new Statistical();      //最大充电功率
        Statistical avgDailyRunTime = new Statistical();       //平均单日运行时间
        Statistical hundredsKmusePower = new Statistical();    //百公里耗电

        String carType = null ;

//        int dischargeMidSoc = 0 ;                                   //近似线性中段当日总放电SOC
//        BigDecimal dischargeMidMileage = BigDecimal.ZERO ;          //近似线性中段当日总行驶里程
//        long chargeMidSec = 0 ;                                     //近似线性中段充电时间(单位：S)
//        int ChargeMidSoc = 0 ;                                      //近似线性中段充电SOC
//        BigDecimal maxInChargerPower = BigDecimal.ZERO ;            //最大输入电功率
//        BigDecimal maxOutChargerPower = BigDecimal.ZERO ;           //最大输出电功率

        BigDecimal mileageTarget = BigDecimal.ZERO ;                //里程
        int mileageNumber = 0 ;
        BigDecimal limitMileageTarget = BigDecimal.ZERO ;           //续驶里程
        int limitMileageNumber = 0 ;
        BigDecimal maxEnergyTimeTarget = BigDecimal.ZERO ;          //一次满电最少时间（单位：h）
        int maxEnergyTimeNumber = 0 ;
        BigDecimal maxElectricPowerTarget = BigDecimal.ZERO ;       //最大充电功率
        int maxElectricPowerNumber = 0 ;
        BigDecimal avgDailyRunTimeTarget = BigDecimal.ZERO ;        //平均单日运行时间
        int avgDailyRunTimeNumber = 0 ;
        BigDecimal hundredsKmusePowerTarget = BigDecimal.ZERO ;     //百公里耗电
        int hundredsKmusePowerNumber = 0 ;
        for (HisCountDataL2 hisCountData : hisCountDatas){
            if (carType == null){
                carType = hisCountData.getCarType() ;
            }
            if (hisCountData.getCanCount()>0){
                canNormal ++ ;
            }
            if (hisCountData.getGpsCount()>0){
                gpsNormal ++ ;
            }
//            dischargeMidSoc += hisCountData.getDischargeMidSoc() ;
//            dischargeMidMileage = dischargeMidMileage.add(hisCountData.getDischargeMidMileage()) ;
//            chargeMidSec += hisCountData.getChargeMidSec() ;
//            ChargeMidSoc += hisCountData.getChargeMidSoc() ;
//
//            maxInChargerPower = maxInChargerPower.compareTo(hisCountData.getMaxInChargerPower()) == 1 ?
//                    maxInChargerPower : hisCountData.getMaxInChargerPower() ;
//            maxOutChargerPower = maxOutChargerPower.compareTo(hisCountData.getMaxOutChargerPower()) == 1 ?
//                    maxOutChargerPower : hisCountData.getMaxOutChargerPower() ;
            if (hisCountData.getMileage().compareTo(BigDecimal.ZERO) == 1){
                mileageNumber ++ ;
                mileageTarget = mileageTarget.add(hisCountData.getMileage()) ;
            }
            if (hisCountData.getLimitMileage().compareTo(BigDecimal.ZERO) == 1){
                limitMileageNumber ++ ;
                limitMileageTarget = limitMileageTarget.add(hisCountData.getLimitMileage()) ;
            }

            /** ???? **/
            if (hisCountData.getMaxEnergyTime3().compareTo(BigDecimal.ZERO) == 1){
                maxEnergyTimeNumber ++ ;
                maxEnergyTimeTarget = maxEnergyTimeTarget.add(hisCountData.getMaxEnergyTime3()) ;
            }


            if (hisCountData.getMaxElectricPower().compareTo(BigDecimal.ZERO) == 1){
                maxElectricPowerNumber ++ ;
                maxElectricPowerTarget = maxElectricPowerTarget.add(hisCountData.getMaxElectricPower()) ;
            }
            if (hisCountData.getAvgDailyRunTime().compareTo(BigDecimal.ZERO) == 1){
                avgDailyRunTimeNumber ++ ;
                avgDailyRunTimeTarget = avgDailyRunTimeTarget.add(hisCountData.getAvgDailyRunTime()) ;
            }
            if (hisCountData.getHundredsKmusePower().compareTo(BigDecimal.ZERO) == 1){
                hundredsKmusePowerNumber ++ ;
                hundredsKmusePowerTarget = hundredsKmusePowerTarget.add(hisCountData.getHundredsKmusePower()) ;
            }
        }
        /**1.车辆累计行驶里程*/
        mileage = this.getMileage(mileage,mileageTarget,carType,mileageNumber);
        /**2.续驶里程*/
        limitMileage = this.getLimitMileage(limitMileage,limitMileageTarget,carType,limitMileageNumber) ;
        /**3.一次充满电所用最少时间*/
        maxEnergyTime = this.getMaxEnergyTime(maxEnergyTime,maxEnergyTimeTarget,carType,maxEnergyTimeNumber) ;
        /**4.最大充电功率*/
        maxElectricPower = this.getMaxElectricPower(maxElectricPower,maxElectricPowerTarget,carType,maxElectricPowerNumber) ;
        /**5.平均单日运行时间*/
        avgDailyRunTime = this.getAvgDailyRunTime(avgDailyRunTime,avgDailyRunTimeTarget,carType,avgDailyRunTimeNumber) ;
        /**6.百公里耗电*/
        hundredsKmusePower = this.getHundredsKmusePower(hundredsKmusePower,hundredsKmusePowerTarget,carType,hundredsKmusePowerNumber) ;

        if (gpsNormal == 0 ){
            if (this.getGpsL1(vinCode)){
                gpsNearNoData ++ ;
            }else{
                gpsNoData ++ ;
            }
        }

        if (canNormal == 0 ){
            if (this.getCanL1(vinCode)){
                canNearNoData ++ ;
            }else{
                canNoData ++ ;
            }
        }

        if (canNormal > 0 ) monthCountData.setCanNormal(monthCountData.getCanNormal()+1) ;
        if (canNearNoData > 0 ) monthCountData.setCanNearNoData(monthCountData.getGpsNearNoData()+1);
        if (canNoData > 0 ) monthCountData.setCanNoData(monthCountData.getCanNoData()+1);
        if (gpsNormal > 0 ) monthCountData.setGpsNormal(monthCountData.getGpsNormal()+1);
        if (gpsNearNoData > 0 ) monthCountData.setGpsNearNoData(monthCountData.getGpsNearNoData()+1);
        if (gpsNoData > 0 ) monthCountData.setGpsNoData(monthCountData.getGpsNoData()+1);

        monthCountData.setMileage(CountUtil.getStatistical(monthCountData.getMileage(),mileage));
        monthCountData.setLimitMileage(CountUtil.getStatistical(monthCountData.getLimitMileage(),limitMileage)) ;
        monthCountData.setMaxEnergyTime(CountUtil.getStatistical(monthCountData.getMaxEnergyTime(),maxEnergyTime));
        monthCountData.setMaxElectricPower(CountUtil.getStatistical(monthCountData.getMaxElectricPower(),maxElectricPower)) ;
        monthCountData.setAvgDailyRunTime(CountUtil.getStatistical(monthCountData.getAvgDailyRunTime(),avgDailyRunTime)) ;
        monthCountData.setHundredsKmusePower(CountUtil.getStatistical(monthCountData.getHundredsKmusePower(),hundredsKmusePower));

    }

    /**
     * 车辆累计行驶里程
     * 算法：累积行驶里程/（查询结束时间-交车时间）<=N
     * @param mileage
     * @param onedaymileage
     * @param carType
     * @param diffNum
     * @return
     */
    private Statistical getMileage(Statistical mileage ,BigDecimal onedaymileage ,String carType ,int diffNum){
        if (onedaymileage.compareTo(BigDecimal.ZERO) == 0 ){
            mileage.setInvalids(mileage.getInvalids() + 1);
        }else{
            onedaymileage = onedaymileage.divide(BigDecimal.valueOf(diffNum),2,BigDecimal.ROUND_UP ) ;
            mileage = CountUtil.getTarget(mileage,carType,onedaymileage,Constant.MILEAGE) ;
        }
        return mileage ;
    }

    /**
     * 续驶里程
     * 算法：里程差/消耗SOC * 100
     * @param limitMileage
     * @param limitedDriving
     * @param carType
     * @param diffNum
     * @return
     */
    private Statistical getLimitMileage(Statistical limitMileage,BigDecimal limitedDriving,String carType,int diffNum){
        if (limitedDriving.compareTo(BigDecimal.ZERO) == 0 ){
            limitMileage.setInvalids(limitMileage.getInvalids() + 1);
        }else{
            limitedDriving = limitedDriving.divide(BigDecimal.valueOf(diffNum),2,BigDecimal.ROUND_UP ) ;
            limitMileage = CountUtil.getTarget(limitMileage,carType,limitedDriving,Constant.LIMITMILEAGE) ;
        }
        return limitMileage ;
    }

    /**
     * 一次充满电所用最少时间
     * 算法：充电时间（S）/充电SOC /3600 * 100
     * @param maxEnergyTime
     * @param maxenergytimeTarget
     * @param carType
     * @param diffNum
     * @return
     */
    private Statistical getMaxEnergyTime( Statistical maxEnergyTime,BigDecimal maxenergytimeTarget,String carType,int diffNum){
        if (maxenergytimeTarget.compareTo(BigDecimal.ZERO) == 0 ){
            maxEnergyTime.setInvalids(maxEnergyTime.getInvalids() + 1);
        }else{
            maxenergytimeTarget = maxenergytimeTarget.divide(BigDecimal.valueOf(diffNum),2,BigDecimal.ROUND_UP ) ;
            maxEnergyTime = CountUtil.getTarget(maxEnergyTime,carType,maxenergytimeTarget,Constant.MAXENERGYTIME) ;
        }
        return maxEnergyTime ;
    }

    /**
     * 最大充电功率
     * 算法：最大充电功率
     * @param maxElectricPower
     * @param maxChargerPower
     * @param carType
     * @param diffNum
     * @return
     */
    private Statistical getMaxElectricPower(Statistical maxElectricPower,BigDecimal maxChargerPower,String carType,int diffNum){
        if (maxChargerPower.compareTo(BigDecimal.ZERO) == 0 ){
            maxElectricPower.setInvalids(maxElectricPower.getInvalids() + 1);
        }else{
            maxChargerPower = maxChargerPower.divide(BigDecimal.valueOf(diffNum),2,BigDecimal.ROUND_UP ) ;
            maxElectricPower = CountUtil.getTarget(maxElectricPower,carType,maxChargerPower,Constant.MAXELECTRICPOWER) ;
        }
        return maxElectricPower ;
    }


    /**
     * 平均单日运行时间
     * 算法：放电时间（S）/3600
     * @param avgDailyRunTime
     * @param avgDailyRunTimeTarget
     * @param carType
     * @param diffNum
     * @return
     */
    private Statistical getAvgDailyRunTime(Statistical avgDailyRunTime,BigDecimal avgDailyRunTimeTarget,String carType,int diffNum){
        if (avgDailyRunTimeTarget.compareTo(BigDecimal.ZERO) == 0 ){
            avgDailyRunTime.setInvalids(avgDailyRunTime.getInvalids() + 1);
        }else{
            avgDailyRunTimeTarget = avgDailyRunTimeTarget.divide(BigDecimal.valueOf(diffNum),2,BigDecimal.ROUND_UP ) ;
            avgDailyRunTime = CountUtil.getTarget(avgDailyRunTime,carType,avgDailyRunTimeTarget,Constant.AVGDAILYRUNTIME) ;
        }
        return avgDailyRunTime ;
    }

    /**
     * 百公里耗电
     * 算法：消耗电量/里程差 * 百分百电能(常量->每个车型都不一样)
     * @param hundredsKmusePower
     * @param hundredskmusepower
     * @param carType
     * @param diffNum
     * @return
     */
    private Statistical getHundredsKmusePower(Statistical hundredsKmusePower,BigDecimal hundredskmusepower,String carType,int diffNum){

        if (hundredskmusepower.compareTo(BigDecimal.ZERO) == 0 ){
            hundredsKmusePower.setInvalids(hundredsKmusePower.getInvalids() + 1);
        }else{
            hundredskmusepower = hundredskmusepower.divide(BigDecimal.valueOf(diffNum),2,BigDecimal.ROUND_UP ) ;
            hundredsKmusePower = CountUtil.getTarget(hundredsKmusePower,carType,hundredskmusepower,Constant.HUNDREDSKMUSEPOWER) ;
        }
        return hundredsKmusePower ;
    }


}
