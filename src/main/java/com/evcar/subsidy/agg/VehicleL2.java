package com.evcar.subsidy.agg;

import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.OrganizationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Kong on 2017/5/12.
 */
@Component
public class VehicleL2 extends VehicleBase{

    private static ESBean esBean;
    @Autowired
    void setEsBean(ESBean value) { this.esBean = value;}

    private static Logger s_logger = LoggerFactory.getLogger(VehicleL2.class);

    protected HisCountDataL2 hisCountData ;

    /**
     * 计算单车每N天的统计方法
     * L2两种计算方式处理
     * 默认方式一：获取L1的start的前一天数据和startDate当天的数据
     *           再获取L2endDate前一天的数据。获取的L2数据减去L1前一天数据再加上startDate当天数据
     *           如果获取数据不满足以上需求，则取第二种方式
     * 方式二：获取start到endDate的L1的数据
     * @param vehicleVo
     * @param startDate
     * @param endDate
     */
    protected void calc(VehicleVo vehicleVo, Date startDate , Date endDate, int monthDay) {
        String vinCode = vehicleVo.getVinCode() ;
        /** 获取 startDate 前 monthDay的数据*/
        this.loadL1(vinCode,startDate,endDate,monthDay);

        boolean endResult =  this.calcVehicleL2One() ;
        if (!endResult){
            this.calcVehicleL2(vehicleVo,startDate, endDate, monthDay);
        }
    }


    public void preFetch(String vin, Date startDate, Date endDate , Integer monthDay){
        loaderL2.preFetch(vin, startDate, endDate,monthDay);
    }


    public void stopQueue(){
        loaderL2.stop();
    }

    /***
     * 计算方式一
     * 不满足需求，计算方式二执行
     * @return
     */
    private boolean calcVehicleL2One(){
        if (this.hisCountDatasL1.size() != 2 && this.hisCountDataL2s.size() != 1){
            return false ;
        }else {
            try{
                HisCountData hisCountData1 = this.hisCountDatasL1.get(0) ;
                HisCountData hisCountData2 = this.hisCountDatasL1.get(1) ;
                HisCountDataL2 hisCountDataL2 = this.hisCountDataL2s.get(0) ;


                Date endDate = hisCountData2.getTm() ;
                Date startDate = hisCountData1.getTm() ;
                String vinCode = hisCountData2.getVinCode() ;
                String id = vinCode + DateUtil.getDateStryyyyMMdd(endDate);
                String carType = hisCountData2.getCarType() ;
                Date veDeliveredDate = hisCountData2.getVeDeliveredDate() ;
                Date releaseTime = hisCountData2.getReleaseTime() ;

                this.hisCountData = new HisCountDataL2() ;
                this.hisCountData.setId(id);
                this.hisCountData.setTm(endDate);
                this.hisCountData.setVinCode(vinCode);
                this.hisCountData.setCarType(carType);
                this.hisCountData.setVeDeliveredDate(veDeliveredDate);
                this.hisCountData.setReleaseTime(releaseTime);
                this.hisCountData.setGpshisCount(Integer.valueOf(String.valueOf(this.gpshisCount)));
                this.hisCountData.setCanhisCount(Integer.valueOf(String.valueOf(this.canhisCount)));

                /**GPS数据条数*/
                Integer gpsCount = hisCountDataL2.getGpsCount() - hisCountData1.getGpsCount() + hisCountData2.getGpsCount() ;
                /**CAN数据条数*/
                Integer canCount = hisCountDataL2.getCanCount() - hisCountData1.getCanCount() + hisCountData2.getCanCount() ;
                /**总里程*/
                BigDecimal mileageTotal = hisCountData2.getMileageTotal();
                /**近似线性中段充电SOC -Model1*/
                Integer chargeMidSoc1 = hisCountDataL2.getChargeMidSoc1() - hisCountData1.getChargeMidSoc1() + hisCountData2.getChargeMidSoc1() ;
                /** 近似线性中段充电时间(单位：S) -Model1 */
                Long chargeMidSec1 = hisCountDataL2.getChargeMidSec1() - hisCountData1.getChargeMidSec1() + hisCountData2.getChargeMidSec1();
                /** 近似线性中段充电SOC -Model2 */
                Integer chargeMidSoc2 = hisCountDataL2.getChargeMidSoc2() - hisCountData1.getChargeMidSoc2() + hisCountData2.getChargeMidSoc2() ;
                /** 近似线性中段充电时间(单位：S) -Model2 */
                Long chargeMidSec2 = hisCountDataL2.getChargeMidSec2() - hisCountData1.getChargeMidSec2() + hisCountData2.getChargeMidSec2();
                /** 近似线性中段充电SOC -Model3 */
                Integer chargeMidSoc3 = hisCountDataL2.getChargeMidSoc3() - hisCountData1.getChargeMidSoc3() + hisCountData2.getChargeMidSoc3() ;
                /** 近似线性中段充电时间(单位：S) -Model3 */
                Long chargeMidSec3 = hisCountDataL2.getChargeMidSec3() - hisCountData1.getChargeMidSec3() + hisCountData2.getChargeMidSec3();
                /** 放电总时长(单位：S) */
                Long dischargeTotalSec = hisCountDataL2.getDischargeTotalSec() - hisCountData1.getDischargeTotalSec() + hisCountData2.getDischargeTotalSec();
                /** 最大输入电功率 */
                BigDecimal maxInChargerPower = hisCountDataL2.getMaxInChargerPower().compareTo(hisCountData2.getMaxInChargerPower()) == 1?
                        hisCountDataL2.getMaxInChargerPower() : hisCountData2.getMaxInChargerPower();
                /** 最大输出电功率 */
                BigDecimal maxOutChargerPower = hisCountDataL2.getMaxOutChargerPower().compareTo(hisCountData2.getMaxOutChargerPower()) == 1?
                        hisCountDataL2.getMaxOutChargerPower() : hisCountData2.getMaxOutChargerPower();
                /** 近似线性中段当日总放电SOC */
                Integer dischargeMidSoc = hisCountDataL2.getDischargeMidSoc() - hisCountData1.getDischargeMidSoc() + hisCountData2.getDischargeMidSoc();
                /** 近似线性中段当日总行驶里程 */
                BigDecimal dischargeMidMileage = hisCountDataL2.getDischargeMidMileage().subtract(hisCountData1.getDischargeMidMileage()).add(hisCountData2.getDischargeMidMileage());


                /** 类型转换 */
                dischargeMidMileage = dischargeMidMileage.divide(BigDecimal.ONE,2 , BigDecimal.ROUND_UP) ;
                maxOutChargerPower = maxOutChargerPower.divide(BigDecimal.ONE,2 , BigDecimal.ROUND_UP) ;
                maxOutChargerPower = maxOutChargerPower.divide(BigDecimal.ONE,2 , BigDecimal.ROUND_UP) ;
                mileageTotal = mileageTotal.divide(BigDecimal.ONE,2 , BigDecimal.ROUND_UP) ;
                /** 平均单日运行时间 */
                int diffNum = DateUtil.diffDate(startDate,endDate) ;
                dischargeTotalSec = dischargeTotalSec / diffNum ;

                this.hisCountData.setGpsCount(gpsCount);
                this.hisCountData.setCanCount(canCount);
                this.hisCountData.setMileageTotal(mileageTotal);
                this.hisCountData.setChargeMidSoc1(chargeMidSoc1);
                this.hisCountData.setChargeMidSec1(chargeMidSec1);
                this.hisCountData.setChargeMidSoc2(chargeMidSoc2);
                this.hisCountData.setChargeMidSec2(chargeMidSec2);
                this.hisCountData.setChargeMidSoc3(chargeMidSoc3);
                this.hisCountData.setChargeMidSec3(chargeMidSec3);
                this.hisCountData.setDischargeTotalSec(dischargeTotalSec);
                this.hisCountData.setMaxInChargerPower(maxInChargerPower);
                this.hisCountData.setMaxOutChargerPower(maxOutChargerPower);
                this.hisCountData.setDischargeMidSoc(dischargeMidSoc);
                this.hisCountData.setDischargeMidMileage(dischargeMidMileage);
                this.hisCountData.setCalcTime(new Date());
                /** 计算指标数据 */
                this.getTargetData() ;

                this.saveL2(this.hisCountData);
                return true ;
            }catch (Exception e){
                return false ;
            }
        }

    }


    /**
     * 算法二
     * @param vehicleVo
     * @param startDate
     * @param endDate
     * @param monthDay
     */
    private void calcVehicleL2(VehicleVo vehicleVo,Date startDate,Date endDate,int monthDay){

        int diffNum = DateUtil.diffDate(startDate,endDate) ;
        diffNum = diffNum == 0 ? 1 : diffNum ;
        for (int i = 0 ; i < diffNum ;i++){
            Date start = DateUtil.getStartDate(startDate,i) ;
            Date end = DateUtil.getEndDate(startDate,monthDay-1) ;
            /** 当不满足monthDay天数时，执行最后一次 */
            if (DateUtil.compare(end,endDate) || DateUtil.diffDate(end,endDate) == 0){
                i = diffNum ;
                end = DateUtil.getEndDate(endDate) ;
            }

            /**
             * 数据处理，避免多次请求
             */
            List<HisCountData> hisCountDatas = OrganizationUtil.getHisCountData(this.hisCountDatas,start,end);

            this.calcTargetL2(vehicleVo,start,end,hisCountDatas);

            this.saveL2(this.hisCountData);
        }
    }

    /**
     * 去除每天充电小于20SOC的数据
     */
    public static final Integer MIN_SOC = 0 ;

    /**
     * 计算
     * @param vehicleVo
     * @param startDate
     * @param endDate
     */
    private void calcTargetL2(VehicleVo vehicleVo,Date startDate ,Date endDate,List<HisCountData> hisCountDatas){

        String vinCode = vehicleVo.getVinCode() ;
        String id = vinCode + DateUtil.getDateStryyyyMMdd(endDate);
        String carType = vehicleVo.getCarType() ;
        Date veDeliveredDate = vehicleVo.getVeDeliveredDate() ;
        Date releaseTime = vehicleVo.getReleaseTime() ;

        this.hisCountData = new HisCountDataL2() ;
        this.hisCountData.setId(id);
        this.hisCountData.setTm(endDate);
        this.hisCountData.setVinCode(vinCode);
        this.hisCountData.setCarType(carType);
        this.hisCountData.setVeDeliveredDate(veDeliveredDate);
        this.hisCountData.setReleaseTime(releaseTime);
        this.hisCountData.setGpshisCount(Integer.valueOf(String.valueOf(this.gpshisCount)));
        this.hisCountData.setCanhisCount(Integer.valueOf(String.valueOf(this.canhisCount)));

        Integer gpsCount = 0 ;                                  //GPS数据条数
        Integer canCount = 0 ;                                  //CAN数据条数
        BigDecimal mileageTotal = BigDecimal.ZERO;              //总里程
        Integer chargeMidSoc1 = 0 ;                             //近似线性中段充电SOC -Model1
        Long chargeMidSec1 = 0L;                                //近似线性中段充电时间(单位：S) -Model1
        Integer chargeMidSoc2 = 0 ;                             //近似线性中段充电SOC -Model2
        Long chargeMidSec2 = 0L;                                //近似线性中段充电时间(单位：S) -Model2
        Integer chargeMidSoc3 = 0 ;                             //近似线性中段充电SOC -Model3
        Long chargeMidSec3 = 0L;                                //近似线性中段充电时间(单位：S) -Model3
        Long dischargeTotalSec = 0L ;                           //放电总时长(单位：S)
        BigDecimal maxInChargerPower = BigDecimal.ZERO ;        //最大输入电功率
        BigDecimal maxOutChargerPower = BigDecimal.ZERO ;       //最大输出电功率
        Integer dischargeMidSoc = 0 ;                           //近似线性中段当日总放电SOC
        BigDecimal dischargeMidMileage = BigDecimal.ZERO ;      //近似线性中段当日总行驶里程

        int hisSize = hisCountDatas.size() ;
        if(hisSize > 0){
            for(HisCountData hisCountData : hisCountDatas){
                gpsCount += hisCountData.getGpsCount() ;
                canCount += hisCountData.getCanCount() ;
                /** 去除每天充电小于MIN_SOC的数据*/
                if (hisCountData.getChargeMidSoc1() > MIN_SOC){
                    chargeMidSoc1 += hisCountData.getChargeMidSoc1() ;
                    chargeMidSec1 += hisCountData.getChargeMidSec1() ;
                }
                if (hisCountData.getChargeMidSoc2() > MIN_SOC){
                    chargeMidSoc2 += hisCountData.getChargeMidSoc2() ;
                    chargeMidSec2 += hisCountData.getChargeMidSec2() ;
                }
                if (hisCountData.getChargeMidSoc3() > MIN_SOC){
                    chargeMidSoc3 += hisCountData.getChargeMidSoc3() ;
                    chargeMidSec3 += hisCountData.getChargeMidSec3() ;
                }

                dischargeTotalSec += hisCountData.getDischargeTotalSec() ;
                maxInChargerPower = maxInChargerPower.compareTo(hisCountData.getMaxInChargerPower()) == 1 ?
                        maxInChargerPower : hisCountData.getMaxInChargerPower() ;
                maxOutChargerPower = maxOutChargerPower.compareTo(hisCountData.getMaxOutChargerPower()) == 1 ?
                        maxOutChargerPower : hisCountData.getMaxOutChargerPower() ;
                dischargeMidSoc += hisCountData.getDischargeMidSoc() ;
                dischargeMidMileage = dischargeMidMileage.add(hisCountData.getDischargeMidMileage());
            }

            /** 总里程取最后一条数据 */
            int mileageNum = hisCountDatas != null ? hisCountDatas.size() - 1 : -1;
            while (mileageNum >= 0 ){
                if (mileageTotal == null || mileageTotal.compareTo(BigDecimal.ZERO) == 0 )
                    mileageTotal = hisCountDatas.get(mileageNum).getMileageTotal() ;
                if (mileageTotal != null && mileageTotal.compareTo(BigDecimal.ZERO) == 1)
                    break;
                mileageNum -- ;
            }
        }

        /** 类型转换 */
        dischargeMidMileage = dischargeMidMileage.divide(BigDecimal.ONE,2 , BigDecimal.ROUND_UP) ;
        maxOutChargerPower = maxOutChargerPower.divide(BigDecimal.ONE,2 , BigDecimal.ROUND_UP) ;
        maxOutChargerPower = maxOutChargerPower.divide(BigDecimal.ONE,2 , BigDecimal.ROUND_UP) ;
        mileageTotal = mileageTotal.divide(BigDecimal.ONE,2 , BigDecimal.ROUND_UP) ;
        /** 平均单日运行时间 */
        int diffNum = DateUtil.diffDate(startDate,endDate)+ 1 ;
        dischargeTotalSec = dischargeTotalSec / diffNum ;

        this.hisCountData.setGpsCount(gpsCount);
        this.hisCountData.setCanCount(canCount);
        this.hisCountData.setMileageTotal(mileageTotal);
        this.hisCountData.setChargeMidSoc1(chargeMidSoc1);
        this.hisCountData.setChargeMidSec1(chargeMidSec1);
        this.hisCountData.setChargeMidSoc2(chargeMidSoc2);
        this.hisCountData.setChargeMidSec2(chargeMidSec2);
        this.hisCountData.setChargeMidSoc3(chargeMidSoc3);
        this.hisCountData.setChargeMidSec3(chargeMidSec3);
        this.hisCountData.setDischargeTotalSec(dischargeTotalSec);
        this.hisCountData.setMaxInChargerPower(maxInChargerPower);
        this.hisCountData.setMaxOutChargerPower(maxOutChargerPower);
        this.hisCountData.setDischargeMidSoc(dischargeMidSoc);
        this.hisCountData.setDischargeMidMileage(dischargeMidMileage);
        this.hisCountData.setCalcTime(new Date());

        /** 计算指标数据 */
        this.getTargetData() ;

    }

    /**
     * 计算指标数据
     */
    private void getTargetData(){
        String carType = this.hisCountData.getCarType() ;
        Date veDeliveredDate = this.hisCountData.getVeDeliveredDate() ;
        Date releaseTime = this.hisCountData.getReleaseTime() ;
        Date endDate = this.hisCountData.getTm() ;
        BigDecimal mileageTotal = this.hisCountData.getMileageTotal();              //总里程
        Integer chargeMidSoc1 = this.hisCountData.getChargeMidSoc1() ;              //近似线性中段充电SOC -Model1
        Long chargeMidSec1 = this.hisCountData.getChargeMidSec1();                  //近似线性中段充电时间(单位：S) -Model1
        Integer chargeMidSoc2 = this.hisCountData.getChargeMidSoc2() ;              //近似线性中段充电SOC -Model2
        Long chargeMidSec2 = this.hisCountData.getChargeMidSec2();                  //近似线性中段充电时间(单位：S) -Model2
        Integer chargeMidSoc3 = this.hisCountData.getChargeMidSoc3() ;              //近似线性中段充电SOC -Model3
        Long chargeMidSec3 = this.hisCountData.getChargeMidSec3();                  //近似线性中段充电时间(单位：S) -Model3
        Long dischargeTotalSec = this.hisCountData.getDischargeTotalSec() ;         //放电总时长(单位：S)
        BigDecimal maxInChargerPower = this.hisCountData.getMaxInChargerPower() ;   //最大输入电功率
        BigDecimal maxOutChargerPower = this.hisCountData.getMaxOutChargerPower() ; //最大输出电功率
        Integer dischargeMidSoc = this.hisCountData.getDischargeMidSoc() ;          //近似线性中段当日总放电SOC
        BigDecimal dischargeMidMileage = this.hisCountData.getDischargeMidMileage() ;//近似线性中段当日总行驶里程
        /**1.车辆累计行驶里程*/
        BigDecimal mileage = this.getMileage(mileageTotal,veDeliveredDate,releaseTime,endDate);
        /**2.续驶里程*/
        BigDecimal limitMileage = this.getLimitMileage(dischargeMidSoc,dischargeMidMileage,carType) ;
        /**3.一次充满电所用最少时间*/
        BigDecimal maxEnergyTime1 = this.getMaxEnergyTime(chargeMidSec1,chargeMidSoc1,carType) ;
        BigDecimal maxEnergyTime2 = this.getMaxEnergyTime(chargeMidSec2,chargeMidSoc2,carType) ;
        BigDecimal maxEnergyTime3 = this.getMaxEnergyTime(chargeMidSec3,chargeMidSoc3,carType) ;
        /**4.最大充电功率*/
        BigDecimal maxElectricPower = this.getMaxElectricPower(maxInChargerPower,maxOutChargerPower,carType) ;
        /**5.平均单日运行时间*/
        BigDecimal avgDailyRunTime = this.getAvgDailyRunTime(dischargeTotalSec) ;
        /**6.百公里耗电*/
        BigDecimal hundredsKmusePower = this.getHundredsKmusePower(dischargeMidSoc,dischargeMidMileage,carType) ;

        this.hisCountData.setMileage(mileage);
        this.hisCountData.setLimitMileage(limitMileage);
        this.hisCountData.setMaxEnergyTime1(maxEnergyTime1);
        this.hisCountData.setMaxEnergyTime2(maxEnergyTime2);
        this.hisCountData.setMaxEnergyTime3(maxEnergyTime3);
        this.hisCountData.setMaxElectricPower(maxElectricPower);
        this.hisCountData.setAvgDailyRunTime(avgDailyRunTime);
        this.hisCountData.setHundredsKmusePower(hundredsKmusePower);
    }


    /**
     * 车辆累计行驶里程
     * 算法：累积行驶里程/（查询结束时间-交车时间）<=N
     * @param mileageTotal
     * @param veDeliveredDate
     * @param releaseTime
     * @param endDate
     * @return
     */
    private BigDecimal getMileage(BigDecimal mileageTotal,Date veDeliveredDate,Date releaseTime, Date endDate){
        mileageTotal = mileageTotal == null ? BigDecimal.ZERO : mileageTotal ;
        BigDecimal onedaymileage = BigDecimal.ZERO ;
        if (releaseTime !=null || veDeliveredDate != null){
            int diffNum ;
            if (veDeliveredDate != null){
                diffNum = DateUtil.diffDate(endDate,veDeliveredDate) ;
            }else{
                diffNum = DateUtil.diffDate(endDate,releaseTime) ;
            }
            if (diffNum != 0 ){
                onedaymileage = mileageTotal.divide(BigDecimal.valueOf(diffNum),2, BigDecimal.ROUND_UP);
            }
        }
        onedaymileage = onedaymileage.divide(BigDecimal.ONE, 2 ,BigDecimal.ROUND_UP) ;
        return onedaymileage ;
    }

    /**
     * 续驶里程
     * 算法：里程差/消耗SOC * 100
     * @param dischargeMidSoc
     * @param dischargeMidMileage
     * @return
     */
    private BigDecimal getLimitMileage(int dischargeMidSoc,BigDecimal dischargeMidMileage,String carType){
        BigDecimal limitedDriving = BigDecimal.ZERO ;
        if (dischargeMidSoc != 0 ){
            limitedDriving = dischargeMidMileage.divide(BigDecimal.valueOf(dischargeMidSoc), 2, BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100)) ;
        }
        limitedDriving = limitedDriving.divide(BigDecimal.ONE, 2 ,BigDecimal.ROUND_UP) ;
        return limitedDriving ;
    }

    /**
     * 一次充满电所用最少时间
     * 算法：充电时间（S）/充电SOC /3600 * 100
     * @param chargeMidSec
     * @param chargeMidSoc
     * @return
     */
    private BigDecimal getMaxEnergyTime(long chargeMidSec, int chargeMidSoc ,String carType){
        BigDecimal maxenergytime = BigDecimal.ZERO ;
        if(chargeMidSoc != 0 ){
            maxenergytime = BigDecimal.valueOf(chargeMidSec).divide(BigDecimal.valueOf(chargeMidSoc), 2, BigDecimal.ROUND_UP)
                    .divide(BigDecimal.valueOf(36),2, BigDecimal.ROUND_UP);
        }
        maxenergytime = maxenergytime.divide(BigDecimal.ONE, 2 ,BigDecimal.ROUND_UP) ;
        return maxenergytime ;
    }

    /**
     * 最大充电功率
     * 算法：最大充电功率
     * @param maxInChargerPower
     * @param maxOutChargerPower
     * @param carType
     * @return
     */
    private BigDecimal getMaxElectricPower(BigDecimal maxInChargerPower,BigDecimal maxOutChargerPower,String carType){
        BigDecimal power = maxInChargerPower.compareTo(maxOutChargerPower) == 1 ?
                maxInChargerPower : maxOutChargerPower ;
        /** 数据装换 */
        power = power.divide(BigDecimal.ONE, 2 ,BigDecimal.ROUND_UP) ;
        return power ;
    }


    /**
     * 平均单日运行时间
     * 算法：放电时间（S）/3600
     * @param dischargeTotalSec
     * @return
     */
    private BigDecimal getAvgDailyRunTime(Long dischargeTotalSec){
        dischargeTotalSec = dischargeTotalSec == null ? 0L : dischargeTotalSec ;
        BigDecimal dischargeSec = BigDecimal.valueOf(dischargeTotalSec).divide(BigDecimal.valueOf(3600),2,BigDecimal.ROUND_UP) ;
        return dischargeSec ;
    }

    /**
     * 百公里耗电
     * 算法：消耗电量/里程差 * 百分百电能(常量->每个车型都不一样)
     * @param dischargeMidSoc
     * @param dischargeMidMileage
     * @param carType
     * @return
     */
    private BigDecimal getHundredsKmusePower(int dischargeMidSoc,BigDecimal dischargeMidMileage,String carType){
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
        hundredskmusepower = hundredskmusepower.divide(BigDecimal.ONE,2,BigDecimal.ROUND_UP) ;
        return hundredskmusepower ;
    }

}
