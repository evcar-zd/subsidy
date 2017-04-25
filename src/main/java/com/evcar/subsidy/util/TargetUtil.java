package com.evcar.subsidy.util;

import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.service.GpsDataService;
import com.evcar.subsidy.service.HisCountDataService;
import com.evcar.subsidy.service.VehicleMotorService;
import com.evcar.subsidy.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 指标验证工具类
 * Created by Kong on 2017/4/20.
 */
@Component
public class TargetUtil {

    private static Logger s_logger = LoggerFactory.getLogger(TargetUtil.class);

    private static ESBean esBean;
    @Autowired
    void setEsBean(ESBean esBean) { this.esBean = esBean;}


    public static final Integer MAX_SIZE = 100 ;

    /**
     * 六项指标计算
     */
    public static void targeCount(){

        Date date = new Date() ;
        Date startDate = DateUtil.getDate(date,esBean.getStartDate()) ;
        Date endDate = DateUtil.getDate(date,esBean.getEndDate()) ;

//        HisCountDataService.deleteByIndex() ;
//        HisCountDataService.deleteHisCountDataL2() ;

        Long vehicleNum = VehicleService.getVehicleNum() ;
        Integer num = Integer.valueOf(String.valueOf(vehicleNum)) ;

        int groupNum = num/MAX_SIZE ;
        groupNum = num%MAX_SIZE > 0 ? groupNum+1 : groupNum ;

        Integer currentPage = 1 ;
        Integer pageSize = MAX_SIZE ;

        /**
         * HisCountL2 计算
         */
        String id = "ZD"+DateUtil.getDateStryyyyMMdd();         //根据时间+随机数生成
        Statistical mileage = new Statistical();                //车辆累积行驶里程
        Statistical limitMileage = new Statistical();           //续驶里程
        Statistical maxEnergyTime = new Statistical();          //一次充满电所用最少时间
        Statistical maxElectricPower = new Statistical();       //最大充电功率
        Statistical avgDailyRunTime = new Statistical();        //平均单日运行时间
        Statistical hundredsKmusePower = new Statistical();     //百公里耗电
        Integer gpsNormal = 0 ;                                 //gps正常车辆
        Integer gpsNearNoData = 0;                              //gps近期数据
        Integer gpsNoData = 0 ;                                 //gps无数据
        Integer canNormal = 0 ;                                 //CAN正常车辆
        Integer canNearNoData = 0 ;                             //CAN近期数据
        Integer canNoData = 0 ;                                 //CAN无数据

        for (int i = 0 ; i < groupNum ; i++ ){
            List<Vehicle> vehicleList = VehicleService.getVehicleByPage(currentPage,pageSize) ;

            if (vehicleList.size() > 0 ){
                for (Vehicle vehicle : vehicleList){
                    HisCountData hisCountData = countData(vehicle.getVinCode(),vehicle.getGprsNo(),startDate,endDate) ;
                    HisCountDataService.addHisCountData(hisCountData) ;

                    String carType = vehicle.getCarType() ;
                    /**
                     * 1.车辆累积行驶里程
                     * 算法：当日行驶里程<=N
                     */
                    BigDecimal onedaymileage = hisCountData.getMileagePoor();
                    mileage = CountUtil.getTarget(mileage,carType,onedaymileage,Constant.MILEAGE) ;

                    /**
                     * 2.续驶里程
                     * 算法：里程差/消耗SOC * 100
                     */
                    if (BigDecimal.valueOf(hisCountData.getConsumeSoc()).compareTo(BigDecimal.ZERO)== 1 ){
                        BigDecimal limitedDriving = hisCountData.getMileagePoor().divide(BigDecimal.valueOf(hisCountData.getConsumeSoc()), 2, BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100)) ;
                        limitMileage = CountUtil.getTarget(limitMileage,carType,limitedDriving,Constant.LIMITMILEAGE) ;
                    }else {
                        limitMileage.setInvalids(limitMileage.getInvalids()+1);
                    }

                    /**
                     * 3.一次充满电所用最少时间
                     * 算法：充电时间（S）*3600/充电SOC
                     */
                    if(BigDecimal.valueOf(hisCountData.getChargeSoc()).compareTo(BigDecimal.ZERO)== 1){
                        BigDecimal maxenergytime = BigDecimal.valueOf(hisCountData.getChargeTime()).multiply(BigDecimal.valueOf(3600))
                                .divide(BigDecimal.valueOf(hisCountData.getChargeSoc()), 2, BigDecimal.ROUND_UP);
                        maxEnergyTime = CountUtil.getTarget(maxEnergyTime,carType,maxenergytime,Constant.MAXENERGYTIME) ;
                    }else{
                        maxEnergyTime.setInvalids(maxEnergyTime.getInvalids()+1);
                    }

                    /**
                     * 4.最大充电功率
                     * 算法：最大充电功率
                     */
                    maxElectricPower = CountUtil.getTarget(maxElectricPower,carType,hisCountData.getMaxElectricPower(),Constant.MAXELECTRICPOWER) ;

                    /**
                     * 5.平均单日运行时间
                     * 算法：放电时间（S）/3600
                     */
                    BigDecimal runTime = BigDecimal.valueOf(hisCountData.getDischargeTime()).divide(BigDecimal.valueOf(3600), 2, BigDecimal.ROUND_UP) ;
                    avgDailyRunTime = CountUtil.getTarget(avgDailyRunTime,carType,runTime,Constant.AVGDAILYRUNTIME) ;

                    /**
                     * 6.百公里耗电
                     * 算法：里程差/消耗电量*100
                     */
                    if(BigDecimal.valueOf(hisCountData.getConsumeSoc()).compareTo(BigDecimal.ZERO)== 1){
                        BigDecimal hundredskmusepower = hisCountData.getMileagePoor().divide(BigDecimal.valueOf(hisCountData.getConsumeSoc()), 2, BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100)) ;
                        hundredsKmusePower = CountUtil.getTarget(hundredsKmusePower,carType,hundredskmusepower,Constant.HUNDREDSKMUSEPOWER) ;
                    }else{
                        hundredsKmusePower.setInvalids(hundredsKmusePower.getInvalids()+1);
                    }

                    if (hisCountData.getGpsNumber() > 0){
                        gpsNormal ++ ;
                    }else{
                        gpsNearNoData ++ ;
                        Long gpsNumber = GpsDataService.getHisGpsDataNum(vehicle.getVinCode(),startDate,endDate) ;
                        gpsNoData += gpsNumber > 0 ? 0 : 1 ;
                    }
                    if (hisCountData.getCanNumber() > 0 ){
                        canNormal ++ ;
                    }else{
                        canNearNoData++ ;
                        Long CanNumber = VehicleMotorService.getHisVehicleMotorNumber(vehicle.getVinCode());
                        canNoData += CanNumber > 0 ? 0 : 1 ;
                    }

                }
            }
            currentPage ++ ;
        }


        MonthCountData monthCountData = new MonthCountData() ;
        monthCountData.setId(id);
        monthCountData.setMileage(mileage) ;
        monthCountData.setLimitMileage(limitMileage) ;
        monthCountData.setMaxEnergyTime(maxEnergyTime) ;
        monthCountData.setMaxElectricPower(maxElectricPower) ;
        monthCountData.setAvgDailyRunTime(avgDailyRunTime) ;
        monthCountData.setHundredsKmusePower(hundredsKmusePower) ;
        monthCountData.setGpsNormal(gpsNormal) ;
        monthCountData.setGpsNearNoData(gpsNearNoData);
        monthCountData.setGpsNoData(gpsNoData);
        monthCountData.setCanNormal(canNormal);
        monthCountData.setCanNearNoData(canNearNoData);
        monthCountData.setCanNoData(canNoData) ;
        monthCountData.setCountDate(new Date()) ;

        HisCountDataService.addHisCountDataL2(monthCountData) ;
    }

    /***
     * 六项指标详细算法
     * @param vinCode
     * @param gprsCode
     * @return
     */
    public static HisCountData countData(String vinCode,String gprsCode,Date startDate ,Date endDate){

        long sizeNum = VehicleMotorService.getHisVehicleMotorNum(vinCode,startDate,endDate) ;
        List<HisVehicleMotor> hisVehicleMotors = null ;
        if (sizeNum > 0)
            hisVehicleMotors = VehicleMotorService.getHisVehicleMotor(vinCode,startDate,endDate,sizeNum) ;

        /**
         * 初始化数据
         * 每辆车对应一个指标数据
         */
        HisCountData countData = new HisCountData() ;
        /**定时任务一天只能执行一次*/
        String id = vinCode + DateUtil.getDateStryyyyMMdd();
        countData.setId(id);
        countData.setGprsCode(gprsCode);
        Integer consumeSoc = 0 ;                            //消耗SOC
        Integer chargeSoc = 0 ;                             //充电SOC
        Integer surplusSoc = null ;                         //剩余SOC
        BigDecimal mileagePoor = BigDecimal.ZERO;           //里程差
        BigDecimal mileageTotal = null ;                    //总里程
        Long chargeTime = Long.valueOf(0);                  //充电时间(单位：S)
        Long dischargeTime = Long.valueOf(0);               //放电时间(单位：S)
        BigDecimal maxElectricPower = BigDecimal.ZERO;      //最大电功率
        Integer canNumber = 0 ;                             //Can数据
        HisVehicleMotor beforeVehicleMotor = null ;
        if (hisVehicleMotors != null ){
            for (HisVehicleMotor vehicleMotor : hisVehicleMotors){
                if (beforeVehicleMotor != null){
                    /** SOC算法 */
                    if (vehicleMotor.getSoc() !=null && beforeVehicleMotor.getSoc() != null){
                        Integer soc = vehicleMotor.getSoc() - beforeVehicleMotor.getSoc() ;
                        /** 如果soc大于0，说明正在充电
                         *  如果soc小于0，说明正在放电
                         *  soc等于0，说明静止不动
                         */
                        if (soc > 0){
                            chargeSoc += soc ;
                            /** 最大电功率 */
                            BigDecimal totalVoltage = vehicleMotor.getTotalVoltage() != null ? vehicleMotor.getTotalVoltage() : BigDecimal.ZERO ;
                            BigDecimal totalCurrent = vehicleMotor.getTotalCurrent() != null ? vehicleMotor.getTotalCurrent() : BigDecimal.ZERO ;
                            BigDecimal electricPower = totalVoltage.multiply(totalCurrent) ;
                            maxElectricPower = electricPower.compareTo(maxElectricPower) == 1 ? electricPower : maxElectricPower ;
                        }else if(soc < 0){
                            consumeSoc -= soc ;
                        }

                        /** 时间算法 */
                        if(vehicleMotor.getReciveTime() != null && beforeVehicleMotor.getReciveTime() != null){
                            Long time =  DateUtil.getSeconds(beforeVehicleMotor.getReciveTime(),vehicleMotor.getReciveTime());
                            /**
                             * 如果time大于0，正常
                             * 如果time小于0，说明出现补传
                             */
                            /** 充电时差 */
                            chargeTime += time > 0 && soc > 0 ? time : 0 ;
                            /** 放电时差 */
                            dischargeTime += time > 0 && soc < 0 ? time : 0 ;

                            if (time < 0){
                                s_logger.info("replenish data :{}",vinCode);
                            }
                        }
                    }

                    /** 里程算法 */
                    if (vehicleMotor.getMileage() != null && beforeVehicleMotor.getMileage() != null){
                        BigDecimal mileage = vehicleMotor.getMileage().subtract(beforeVehicleMotor.getMileage());
                        /**
                         * 如果mileage大于0，说明正在运动
                         * 如果mileage小于0，说明里程跳变，后期处理
                         */
                        if (mileage.compareTo(BigDecimal.ZERO) == 1){
                            mileagePoor = mileagePoor.add(mileage) ;
                        }else if (mileage.compareTo(BigDecimal.ZERO) == -1){
                            s_logger.info("mileage jump :{}",vinCode);
                        }
                    }

                }
                beforeVehicleMotor = vehicleMotor ;

                canNumber += vehicleMotor.getSoc()!=null ? 1 : 0 ;
            }
        }

        /** 剩余SOC、总里程算法*/
        int num = hisVehicleMotors != null ? hisVehicleMotors.size() - 1 : -1;
        while (num >= 0 ){
            if (surplusSoc == null){
                surplusSoc = hisVehicleMotors.get(num).getSoc() ;
            }
            if (mileageTotal == null){
                mileageTotal = hisVehicleMotors.get(num).getMileage() ;
            }
            if (surplusSoc != null && mileageTotal != null) break;
            num -- ;
        }

        surplusSoc = surplusSoc== null ? 0 : surplusSoc ;
        mileageTotal = mileageTotal == null ? BigDecimal.ZERO : mileageTotal ;

        /** 获取GPS数据条数 */
        Integer gpsNumber = (int) GpsDataService.getHisGpsDataNum(vinCode,startDate,endDate) ;

        countData.setConsumeSoc(consumeSoc);
        countData.setChargeSoc(chargeSoc);
        countData.setSurplusSoc(surplusSoc);
        countData.setMileagePoor(mileagePoor);
        countData.setMileageTotal(mileageTotal);
        countData.setChargeTime(chargeTime) ;
        countData.setDischargeTime(dischargeTime) ;
        countData.setMaxElectricPower(maxElectricPower) ;
        countData.setCanNumber(canNumber);
        countData.setGpsNumber(gpsNumber);
        countData.setCountDate(new Date());
        return countData ;
    }


}
