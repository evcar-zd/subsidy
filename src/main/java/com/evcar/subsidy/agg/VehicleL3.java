package com.evcar.subsidy.agg;

import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.service.HisCountDataService;
import com.evcar.subsidy.util.Constant;
import com.evcar.subsidy.util.CountUtil;
import com.evcar.subsidy.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;


/**
 * Created by Kong on 2017/5/12.
 */
@Component
public class VehicleL3 extends VehicleBase {

    private static ESBean esBean;
    @Autowired
    void setEsBean(ESBean value) { this.esBean = value;}

    protected MonthCountData monthCountData ;


    private static Integer MAX_QUERY_SIZE = 1000 ;
    /**
     * L3计算
     * @param startDate
     * @param endDate
     */
    public void calcL3(Date startDate, Date endDate){
        Map<String,HisCountDataL2> map = new HashMap<>() ;

        List<HisCountDataL2> hisCountDataL2s = new ArrayList<>() ;
        Long size = HisCountDataService.getHisCountDataNumberL2(startDate,endDate);
        Integer num = Integer.valueOf(String.valueOf(size)) ;
        int groupNum = num/MAX_QUERY_SIZE ;
        groupNum = num%MAX_QUERY_SIZE > 0 ? groupNum+1 : groupNum ;
        Integer currentPage = 1 ;
        Integer pageSize = MAX_QUERY_SIZE ;
        for (int j = 0 ; j < groupNum ; j++ ) {
            List<HisCountDataL2> result = HisCountDataService.getHisCountDataL2(startDate,endDate,currentPage,pageSize) ;
            hisCountDataL2s.addAll(result) ;
            currentPage ++ ;
        }

        monthCountData = new MonthCountData() ;
        String id = Constant.LOGO + DateUtil.getDateStryyyyMMdd(endDate);     //根据时间生成
        monthCountData.setId(id);
        monthCountData.setTm(endDate);
        Integer vehicleNum = 0 ;                //车辆数量

        for (int i = 0 ; i < hisCountDataL2s.size() ; i++){
            HisCountDataL2 hisCountDataL2 = hisCountDataL2s.get(i) ;
            String vinCode = hisCountDataL2.getVinCode() ;
            if (map.get(vinCode) != null ) continue;
            getTarget2(hisCountDataL2) ;
            vehicleNum ++ ;
            map.put(vinCode,hisCountDataL2) ;
        }

        monthCountData.setCalcTime(new Date());
        monthCountData.setVehicleNum(vehicleNum);
        boolean execute = hisCountDataL2s.size() > 0 ? true : false ;
        if (execute)
            this.saveL3(monthCountData);
    }


    private void getTarget2(HisCountDataL2 hisCountDataL2){
        String vinCode = hisCountDataL2.getVinCode() ;
        String carType = hisCountDataL2.getCarType() ;
        Integer gpsNormal = hisCountDataL2.getGpsCount() ;                 //gps正常车辆
        Integer gpsNearNoData = 0 ;             //gps近期数据
        Integer gpsNoData = 0 ;                 //gps无数据
        Integer canNormal = hisCountDataL2.getCanCount() ;                 //CAN正常车辆
        Integer canNearNoData = 0 ;             //CAN近期数据
        Integer canNoData = 0 ;                 //CAN无数据

        Statistical mileage = new Statistical();               //车辆累计行驶里程
        Statistical limitMileage = new Statistical();          //续驶里程
        Statistical maxEnergyTime = new Statistical();         //一次充满电所用最少时间
        Statistical maxElectricPower = new Statistical();      //最大充电功率
        Statistical avgDailyRunTime = new Statistical();       //平均单日运行时间
        Statistical hundredsKmusePower = new Statistical();    //百公里耗电


        BigDecimal mileageTarget = hisCountDataL2.getMileage() ;                        //里程
        int mileageNumber = 1 ;
        BigDecimal limitMileageTarget = hisCountDataL2.getLimitMileage() ;              //续驶里程
        int limitMileageNumber = 1 ;
        BigDecimal maxEnergyTimeTarget1 = hisCountDataL2.getMaxEnergyTime1() ;          //一次满电最少时间（单位：h）- Model1
        int maxEnergyTimeNumber1 = 1 ;
        BigDecimal maxEnergyTimeTarget2 = hisCountDataL2.getMaxEnergyTime2() ;          //一次满电最少时间（单位：h）- Model2
        int maxEnergyTimeNumber2 = 1 ;
        BigDecimal maxEnergyTimeTarget3 = hisCountDataL2.getMaxEnergyTime3() ;          //一次满电最少时间（单位：h）- Model3
        int maxEnergyTimeNumber3 = 1 ;
        BigDecimal maxElectricPowerTarget = hisCountDataL2.getMaxElectricPower() ;       //最大充电功率
        int maxElectricPowerNumber = 1 ;
        BigDecimal avgDailyRunTimeTarget = hisCountDataL2.getAvgDailyRunTime() ;            //平均单日运行时间
        int avgDailyRunTimeNumber = 1 ;
        BigDecimal hundredsKmusePowerTarget = hisCountDataL2.getHundredsKmusePower() ;     //百公里耗电
        int hundredsKmusePowerNumber = 1 ;

        if (canNormal>0){
            canNormal ++ ;
        }
        if (gpsNormal>0){
            gpsNormal ++ ;
        }
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



        /**1.车辆累计行驶里程*/
        mileage = this.getMileage(mileage,mileageTarget,carType,mileageNumber);
        /**2.续驶里程*/
        limitMileage = this.getLimitMileage(limitMileage,limitMileageTarget,carType,limitMileageNumber) ;
        /**3.一次充满电所用最少时间*/
        maxEnergyTime = this.getMaxEnergyTime(maxEnergyTime,carType,maxEnergyTimeTarget1,maxEnergyTimeNumber1,maxEnergyTimeTarget2,maxEnergyTimeNumber2,maxEnergyTimeTarget3,maxEnergyTimeNumber3) ;
        /**4.最大充电功率*/
        maxElectricPower = this.getMaxElectricPower(maxElectricPower,maxElectricPowerTarget,carType,maxElectricPowerNumber) ;
        /**5.平均单日运行时间*/
        avgDailyRunTime = this.getAvgDailyRunTime(avgDailyRunTime,avgDailyRunTimeTarget,carType,avgDailyRunTimeNumber) ;
        /**6.百公里耗电*/
        hundredsKmusePower = this.getHundredsKmusePower(hundredsKmusePower,hundredsKmusePowerTarget,carType,hundredsKmusePowerNumber) ;



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
     * @param carType
     * @param maxEnergyTimeTarget1
     * @param maxEnergyTimeNumber1
     * @param maxEnergyTimeTarget2
     * @param maxEnergyTimeNumber2
     * @param maxEnergyTimeTarget3
     * @param maxEnergyTimeNumber3
     * @return
     */
    private Statistical getMaxEnergyTime( Statistical maxEnergyTime,String carType,
                                          BigDecimal maxEnergyTimeTarget1,int maxEnergyTimeNumber1,
                                          BigDecimal maxEnergyTimeTarget2,int maxEnergyTimeNumber2,
                                          BigDecimal maxEnergyTimeTarget3,int maxEnergyTimeNumber3){
        Statistical maxEnergyTimeMiddle = new Statistical() ;
        if (maxEnergyTimeTarget1.compareTo(BigDecimal.ZERO) == 0 && maxEnergyTimeTarget2.compareTo(BigDecimal.ZERO) == 0
                && maxEnergyTimeTarget3.compareTo(BigDecimal.ZERO) == 0){
            maxEnergyTime.setInvalids(maxEnergyTime.getInvalids() + 1);
        }else{
            if (maxEnergyTimeTarget1.compareTo(BigDecimal.ZERO) == 1){
                maxEnergyTimeTarget1 = maxEnergyTimeTarget1.divide(BigDecimal.valueOf(maxEnergyTimeNumber1),2,BigDecimal.ROUND_UP ) ;
                maxEnergyTimeMiddle = CountUtil.getTarget(maxEnergyTimeMiddle,carType,maxEnergyTimeTarget1,Constant.MAXENERGYTIME1) ;
            }
            if (maxEnergyTimeTarget2.compareTo(BigDecimal.ZERO) == 1){
                maxEnergyTimeTarget2 = maxEnergyTimeTarget2.divide(BigDecimal.valueOf(maxEnergyTimeNumber2),2,BigDecimal.ROUND_UP ) ;
                maxEnergyTimeMiddle = CountUtil.getTarget(maxEnergyTimeMiddle,carType,maxEnergyTimeTarget2,Constant.MAXENERGYTIME2) ;
            }
            if (maxEnergyTimeTarget3.compareTo(BigDecimal.ZERO) == 1){
                maxEnergyTimeTarget3 = maxEnergyTimeTarget3.divide(BigDecimal.valueOf(maxEnergyTimeNumber3),2,BigDecimal.ROUND_UP ) ;
                maxEnergyTimeMiddle = CountUtil.getTarget(maxEnergyTimeMiddle,carType,maxEnergyTimeTarget3,Constant.MAXENERGYTIME3) ;
            }

            if (maxEnergyTimeMiddle.getNormal() > 0){
                maxEnergyTime.setNormal(maxEnergyTime.getNormal() + 1);
            }else if (maxEnergyTimeMiddle.getLowSide() > 0){
                maxEnergyTime.setLowSide(maxEnergyTime.getLowSide() + 1);
            }else if (maxEnergyTimeMiddle.getHighSide() > 0){
                maxEnergyTime.setHighSide(maxEnergyTime.getHighSide() + 1);
            }else if(maxEnergyTimeMiddle.getInvalids() > 0){
                maxEnergyTime.setInvalids(maxEnergyTime.getInvalids() + 1);
            }
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
