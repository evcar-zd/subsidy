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
            System.out.print("{\"vinCode\":\""+vinCode+"\",");
            List<HisCountData> hisCountDatas = this.loadL2(vinCode,startDate ,endDate);
            if (hisCountDatas.size()==0) continue;
            /** 数据处理 */
            this.calcHisCountData(hisCountDatas,vinCode,startDate ,endDate);

            vehicleNum ++ ;
        }
        monthCountData.setCalcTime(new Date());
        monthCountData.setVehicleNum(vehicleNum);

    }



    private void calcHisCountData(List<HisCountData> hisCountDatas,String vinCode,Date startDate , Date endDate){
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

        int dischargeMidSoc = 0 ;                                   //近似线性中段当日总放电SOC
        BigDecimal dischargeMidMileage = BigDecimal.ZERO ;          //近似线性中段当日总行驶里程
        long chargeMidSec = 0 ;                                     //近似线性中段充电时间(单位：S)
        int ChargeMidSoc = 0 ;                                      //近似线性中段充电SOC
        BigDecimal maxInChargerPower = BigDecimal.ZERO ;            //最大输入电功率
        BigDecimal maxOutChargerPower = BigDecimal.ZERO ;           //最大输出电功率
        for (HisCountData hisCountData : hisCountDatas){
            if (carType == null){
                carType = hisCountData.getCarType() ;
            }
            if (hisCountData.getCanCount()>0){
                canNormal ++ ;
            }
            if (hisCountData.getGpsCount()>0){
                gpsNormal ++ ;
            }
            dischargeMidSoc += hisCountData.getDischargeMidSoc() ;
            dischargeMidMileage = dischargeMidMileage.add(hisCountData.getDischargeMidMileage()) ;
            chargeMidSec += hisCountData.getChargeMidSec() ;
            ChargeMidSoc += hisCountData.getChargeMidSoc() ;

            maxInChargerPower = maxInChargerPower.compareTo(hisCountData.getMaxInChargerPower()) == 1 ?
                    maxInChargerPower : hisCountData.getMaxInChargerPower() ;
            maxOutChargerPower = maxOutChargerPower.compareTo(hisCountData.getMaxOutChargerPower()) == 1 ?
                    maxOutChargerPower : hisCountData.getMaxOutChargerPower() ;
        }

        /**1.车辆累计行驶里程*/
        mileage = this.getMileage(mileage,hisCountDatas,endDate);
        /**2.续驶里程*/
        limitMileage = this.getLimitMileage(limitMileage,dischargeMidSoc,dischargeMidMileage,carType) ;
        /**3.一次充满电所用最少时间*/
        maxEnergyTime = this.getMaxEnergyTime(maxEnergyTime,chargeMidSec,ChargeMidSoc,carType) ;
        /**4.最大充电功率*/
        maxElectricPower = this.getMaxElectricPower(maxElectricPower,maxInChargerPower,maxOutChargerPower,carType) ;
        /**5.平均单日运行时间*/
        avgDailyRunTime = this.getAvgDailyRunTime(avgDailyRunTime,hisCountDatas) ;
        /**6.百公里耗电*/
        hundredsKmusePower = this.getHundredsKmusePower(hundredsKmusePower,dischargeMidSoc,dischargeMidMileage,carType) ;

        if (gpsNormal == 0 ){
            gpsNearNoData ++ ;
            gpsNoData = this.getGpsL1(vinCode) ? 0 : 1 ;
        }

        if (canNormal == 0 ){
            canNearNoData ++ ;
            canNoData = this.getCanL1(vinCode) ? 0 : 1 ;
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
     * @param hisCountDatas
     * @param endDate
     * @param mileage
     * @return
     */
    private Statistical getMileage(Statistical mileage,List<HisCountData> hisCountDatas,Date endDate){
        int mileageNum = hisCountDatas != null ? hisCountDatas.size() - 1 : -1;
        BigDecimal mileageTotal = null ;
        String carType = null ;
        Date veDeliveredDate = null ;
        Date releaseTime = null ;
        while (mileageNum >= 0 ){
            if (carType == null )
                carType = hisCountDatas.get(mileageNum).getCarType() ;
            if (veDeliveredDate == null )
                veDeliveredDate = hisCountDatas.get(mileageNum).getVeDeliveredDate() ;
            if (mileageTotal == null)
                mileageTotal = hisCountDatas.get(mileageNum).getMileageTotal() ;
            if (releaseTime == null)
                releaseTime = hisCountDatas.get(mileageNum).getReleaseTime() ;
            if (mileageTotal != null && veDeliveredDate != null && carType != null && releaseTime != null)
                break;
            mileageNum -- ;
        }
        mileageTotal = mileageTotal == null ? BigDecimal.ZERO : mileageTotal ;

        if ((releaseTime !=null || veDeliveredDate != null) && carType != null ){
            int diffNum ;
            if (veDeliveredDate != null){
                diffNum = DateUtil.diffDate(endDate,veDeliveredDate) ;
            }else{
                diffNum = DateUtil.diffDate(endDate,releaseTime) ;
            }

            BigDecimal onedaymileage = BigDecimal.ZERO ;
            if (diffNum != 0 ){
                onedaymileage = mileageTotal.divide(BigDecimal.valueOf(diffNum),2, BigDecimal.ROUND_UP);
            }
            System.out.print("\"mileage\":"+onedaymileage.toString()+",");
            mileage = CountUtil.getTarget(mileage,carType,onedaymileage,Constant.MILEAGE) ;
        }

        return mileage ;
    }

    /**
     * 续驶里程
     * 算法：里程差/消耗SOC * 100
     * @param dischargeMidSoc
     * @param dischargeMidMileage
     * @return
     */
    private Statistical getLimitMileage(Statistical limitMileage,int dischargeMidSoc,BigDecimal dischargeMidMileage,String carType){
        BigDecimal limitedDriving = BigDecimal.ZERO ;
        if (dischargeMidSoc != 0 ){
            limitedDriving = dischargeMidMileage.divide(BigDecimal.valueOf(dischargeMidSoc), 2, BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100)) ;
        }
        System.out.print("\"limitMileage\":"+limitedDriving.toString()+",");
        limitMileage = getTarget(limitMileage,carType,limitedDriving,Constant.LIMITMILEAGE) ;
        return limitMileage ;
    }

    /**
     * 一次充满电所用最少时间
     * 算法：充电时间（S）/充电SOC /3600 * 100
     * @param chargeMidSec
     * @param ChargeMidSoc
     * @param maxEnergyTime
     * @return
     */
    private Statistical getMaxEnergyTime( Statistical maxEnergyTime,long chargeMidSec, int ChargeMidSoc ,String carType){
        BigDecimal maxenergytime = BigDecimal.ZERO ;
        if(ChargeMidSoc != 0 ){
            maxenergytime = BigDecimal.valueOf(chargeMidSec).divide(BigDecimal.valueOf(ChargeMidSoc), 2, BigDecimal.ROUND_UP)
                    .divide(BigDecimal.valueOf(36),2, BigDecimal.ROUND_UP);
        }
        System.out.print("\"maxEnergyTime\":"+maxenergytime.toString()+",");
        maxEnergyTime = getTarget(maxEnergyTime,carType,maxenergytime,Constant.MAXENERGYTIME) ;
        return maxEnergyTime ;
    }

    /**
     * 最大充电功率
     * 算法：最大充电功率
     * @param maxElectricPower
     * @param maxInChargerPower
     * @param maxOutChargerPower
     * @param carType
     * @return
     */
    private Statistical getMaxElectricPower(Statistical maxElectricPower,BigDecimal maxInChargerPower,BigDecimal maxOutChargerPower,String carType){
        maxElectricPower = maxInChargerPower.compareTo(maxOutChargerPower) == 1 ?
                getTarget(maxElectricPower,carType,maxInChargerPower,Constant.MAXELECTRICPOWER) :
                getTarget(maxElectricPower,carType,maxOutChargerPower,Constant.MAXELECTRICPOWER) ;

        BigDecimal power = maxInChargerPower.compareTo(maxOutChargerPower) == 1 ?
                maxInChargerPower : maxOutChargerPower ;
        System.out.print("\"maxElectricPower\":"+power.toString()+",");
        return maxElectricPower ;
    }


    /**
     * 平均单日运行时间
     * 算法：放电时间（S）/3600
     * @param hisCountDatas
     * @param avgDailyRunTime
     * @return
     */
    private Statistical getAvgDailyRunTime(Statistical avgDailyRunTime,List<HisCountData> hisCountDatas){
        int num = hisCountDatas != null ? hisCountDatas.size() - 1 : -1;
        Long dischargeTotalSec = null ;
        String carType = null ;
        while (num >= 0 ){
            if (carType == null )
                carType = hisCountDatas.get(num).getCarType() ;
            if (dischargeTotalSec == null )
                dischargeTotalSec = hisCountDatas.get(num).getDischargeTotalSec() ;
            if (dischargeTotalSec != null && carType != null)
                break;
            num -- ;
        }
        dischargeTotalSec = dischargeTotalSec == null ? Long.valueOf(0) : dischargeTotalSec ;
        BigDecimal dischargeSec = BigDecimal.valueOf(dischargeTotalSec).divide(BigDecimal.valueOf(3600),2,BigDecimal.ROUND_UP) ;
        System.out.print("\"avgDailyRunTime\":"+dischargeSec.toString()+",");
        avgDailyRunTime = CountUtil.getTarget(avgDailyRunTime,carType,dischargeSec,Constant.AVGDAILYRUNTIME) ;
        return avgDailyRunTime ;
    }

    /**
     * 百公里耗电
     * 算法：消耗电量/里程差 * 百分百电能(常量->每个车型都不一样)
     * @param hundredsKmusePower
     * @param dischargeMidSoc
     * @param dischargeMidMileage
     * @param carType
     * @return
     */
    private Statistical getHundredsKmusePower(Statistical hundredsKmusePower,int dischargeMidSoc,BigDecimal dischargeMidMileage,String carType){
        /** 获取配置文件中的电池组总容量 */
        EsBeanObj esBeanObj = esBean.getTarget().get(carType) ;
        BigDecimal socTotalcapacity = BigDecimal.ZERO ;
        if (esBeanObj != null){
            socTotalcapacity = esBean.getTarget().get(carType).getSocTotalcapacity() ;
        }

        BigDecimal hundredskmusepower = BigDecimal.ZERO ;
        if(dischargeMidSoc != 0){
            hundredskmusepower = BigDecimal.valueOf(dischargeMidSoc).divide(dischargeMidMileage, 2, BigDecimal.ROUND_UP).multiply(socTotalcapacity) ;
        }
        System.out.println("\"hundredsKmusePower\":"+hundredskmusepower.toString()+"},");
        hundredsKmusePower = getTarget(hundredsKmusePower,carType,hundredskmusepower,Constant.HUNDREDSKMUSEPOWER) ;
        return hundredsKmusePower ;
    }


}
