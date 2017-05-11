package com.evcar.subsidy.util;

import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.evcar.subsidy.service.VehicleService.getVehicleNum;
import static com.evcar.subsidy.util.DateUtil.date;


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

    /**
     * 每MAX_SIZE分为一组，避免数据量大出现内存不足情况
     * 后期可以使用多线程去处理（快速）
     */
    public static final Integer MAX_SIZE = 500 ;

    /**
     * 六项指标计算
     */
    public static void targeCount(int startDay,int endDay,List<String> vinCodes){

//        HisCountDataService.deleteByIndex() ;
//        HisCountDataService.createHisCountIndex();

        Date date = new Date() ;
        Date startDate = DateUtil.getDate(date,startDay) ;
        Date endDate = DateUtil.getDate(date,endDay) ;

        Integer diffNum = DateUtil.diffDate(endDate,startDate) ;
        for (int i = 0 ; i < diffNum ; i++ ){

            Date start = DateUtil.getDate(startDate,i) ;
            Date end = DateUtil.getDate(startDate,i+1) ;

            if (vinCodes != null && vinCodes.size() >0){     //查询集合内的车辆信息
                for (String vinCodeStr : vinCodes){
                    Vehicle vehicle = VehicleService.getVehicle(vinCodeStr) ;
                    if (vehicle == null) continue;
                    HisCountData hisCountData = countData(vehicle.getVinCode(),vehicle.getGprsNo(),start,end) ;
                    hisCountData.setVeDeliveredDate(vehicle.getVeDeliveredDate());
                    hisCountData.setCarType(vehicle.getCarType());
                    hisCountData.setRunDate(start);
                    HisCountDataService.addHisCountData(hisCountData) ;
                }
            }else{
                /** 车辆分组--避免数据辆过大 */
                Long vehicleNum = getVehicleNum() ;
                Integer num = Integer.valueOf(String.valueOf(vehicleNum)) ;
                int groupNum = num/MAX_SIZE ;
                groupNum = num%MAX_SIZE > 0 ? groupNum+1 : groupNum ;
                Integer currentPage = 1 ;
                Integer pageSize = MAX_SIZE ;
                for (int j = 0 ; j < groupNum ; j++ ) {
                    List<Vehicle> vehicleList = VehicleService.getVehicleByPage(currentPage, pageSize);
                    for (Vehicle vehicle : vehicleList){
                        HisCountData hisCountData = countData(vehicle.getVinCode(),vehicle.getGprsNo(),start,end) ;
                        hisCountData.setVeDeliveredDate(vehicle.getVeDeliveredDate());
                        hisCountData.setCarType(vehicle.getCarType());
                        hisCountData.setRunDate(end);

                        /** 删除有过的记录重新计算 */
                        HisCountDataService.deleteHisCountData(hisCountData.getId()) ;

                        HisCountDataService.addHisCountData(hisCountData) ;
                    }
                    currentPage ++ ;
                }
            }
        }
    }

    /***
     * 六项指标详细算法
     * @param vinCode
     * @param gprsCode
     * @return
     */
    public static HisCountData countData(String vinCode,String gprsCode,Date startDate ,Date endDate){
        /** 查询历史整车和电机数据 */
        long sizeNum = VehicleMotorService.getHisVehicleMotorNum(vinCode,startDate,endDate) ;
        List<HisVehicleMotor> hisVehicleMotors = null ;
        if (sizeNum > 0)
            hisVehicleMotors = VehicleMotorService.getHisVehicleMotor(vinCode,startDate,endDate,sizeNum) ;

        /** 查询BMS数据 */
        long bmsNum = BmsDataService.getHisBmsDataNum(vinCode,startDate,endDate);
        List<BmsData> bmsDatas = null ;
        if (bmsNum > 0 )
            bmsDatas = BmsDataService.getHisBmsData(vinCode,startDate,endDate,bmsNum) ;

        /** 查询OBS数据 */
        long obcNum = ObcDataService.getHisObcDataNum(vinCode,startDate,endDate);
        List<ObcData> obcDatas = null ;
        if (bmsNum > 0 )
            obcDatas = ObcDataService.getHisObcData(vinCode,startDate,endDate,obcNum) ;

        /**
         * 初始化数据
         * 每辆车对应一个指标数据
         */
        HisCountData countData = new HisCountData() ;
        /**定时任务一天只能执行一次*/
        String id = vinCode + DateUtil.getDateStryyyyMMdd(startDate);
        countData.setId(id);                                //id
        countData.setGprsCode(gprsCode);                    //gpsCode
        countData.setVinCode(vinCode);                      //VINCode
        Integer consumeSoc = 0 ;                            //消耗SOC
        Integer chargeSoc = 0 ;                             //充电SOC
        Integer surplusSoc = null ;                         //剩余SOC
        BigDecimal mileageTotal = null ;                    //总里程
        Long chargeTime = Long.valueOf(0);                  //充电时间(单位：S)
        Long dischargeTime = Long.valueOf(0);               //放电时间(单位：S)
        BigDecimal maxInElectricPower = BigDecimal.ZERO;    //最大电功率1
        BigDecimal maxOutElectricPower = BigDecimal.ZERO;   //最大电功率1
        Integer canNumber = 0 ;                             //Can数据
        Integer linearSoc = 0 ;                             //线性放电SOC
        BigDecimal linearMileage = BigDecimal.ZERO ;        //线性行驶里程
        /** 获取GPS数据条数 */
        Integer gpsNumber = (int) GpsDataService.getHisGpsDataNum(vinCode,startDate,endDate) ;

        /** 计算SOC&Time相关数据 */
        if(bmsDatas != null ){
            BmsData beforeBmsData = null ;
            for (BmsData bmsData : bmsDatas){
                /**
                 * 计算区间内：消耗SOC、充电SOC、剩余SOC
                 * 充电时间(单位：S)、放电时间(单位：S)
                 */
                if (beforeBmsData != null){
                    if (bmsData.getSoc() <= esBean.getMaxSoc() && bmsData.getSoc() >= esBean.getMinSoc()){

                        if (bmsData.getBatteryGroupStatus() == 1 || bmsData.getBatteryGroupStatus() == 3){
                            Integer soc = bmsData.getSoc() - beforeBmsData.getSoc() ;
                            /** 如果soc大于0，说明正在充电
                             *  如果soc小于0，说明正在放电
                             *  soc等于0，说明静止不动
                             */
                            consumeSoc -= soc < 0 && bmsData.getBatteryGroupStatus() == 1 ?  soc : 0 ;//放电
                            chargeSoc += soc > 0 && bmsData.getBatteryGroupStatus() == 3 ? soc : 0 ;//充电

                            /** 时间算法 */
                            if(bmsData.getCollectTime() != null && beforeBmsData.getCollectTime() != null){
                                Long time =  DateUtil.getSeconds(beforeBmsData.getCollectTime(),bmsData.getCollectTime());
                                /**
                                 * 如果time大于0，正常
                                 * 如果time小于0，说明出现补传
                                 */
                                /** 充电时差 */
                                chargeTime += (time > 0 && bmsData.getBatteryGroupStatus() == 3) ? time : 0 ;
                                /** 放电时差 */
                                dischargeTime += (time > 0 && bmsData.getBatteryGroupStatus() == 1) ? time : 0 ;
                                if (time < 0){
                                    s_logger.info("replenish data :{}",vinCode);
                                }
                            }
                        }
                    }
                }
                beforeBmsData = bmsData ;
            }
        }
        /** 计算剩余SOC、判断是为去空处理 */
        int num = bmsDatas != null ? bmsDatas.size() - 1 : -1;
        while (num >= 0 ){
            if (surplusSoc == null){
                surplusSoc = bmsDatas.get(num).getSoc() ;
            }
            if (surplusSoc != null) break;
            num -- ;
        }
        surplusSoc = surplusSoc== null ? 0 : surplusSoc ;

        /** 计算最大电功率 */
        if(obcDatas != null){
            for (ObcData obcData : obcDatas){
                /** 最大电功率 1 2 */
                BigDecimal inTotalVoltage = obcData.getInVoltage() != null ? obcData.getInVoltage() : BigDecimal.ZERO ;
                BigDecimal inTotalCurrent = obcData.getInCurrent() != null ? obcData.getInCurrent() : BigDecimal.ZERO ;
                BigDecimal inElectricPower = inTotalVoltage.multiply(inTotalCurrent) ;
                maxInElectricPower = inElectricPower.compareTo(maxInElectricPower) == 1 ? inElectricPower : maxInElectricPower ;

                BigDecimal outTotalVoltage = obcData.getOutVoltage() != null ? obcData.getOutVoltage() : BigDecimal.ZERO ;
                BigDecimal outTotalCurrent = obcData.getOutCurrent() != null ? obcData.getOutCurrent() : BigDecimal.ZERO ;
                BigDecimal outElectricPower = outTotalVoltage.multiply(outTotalCurrent) ;
                maxOutElectricPower = outElectricPower.compareTo(maxOutElectricPower) == 1 ? outElectricPower : maxOutElectricPower ;
            }
        }

        /** 计算线性SOC、近线性里程相关数据 */
        if (hisVehicleMotors != null ){
            HisVehicleMotor beforeVehicleMotor = null ;

            /** 变量定义 放电SOC & 行驶里程 */
            Integer lineSoc = 0 ;
            BigDecimal lineMileage = BigDecimal.ZERO ;
            for (HisVehicleMotor vehicleMotor : hisVehicleMotors){
                if (beforeVehicleMotor != null){

                    /** 里程算法 */
                    if (vehicleMotor.getMileage() != null && beforeVehicleMotor.getMileage() != null){
                        BigDecimal mileage = vehicleMotor.getMileage().subtract(beforeVehicleMotor.getMileage());
                        /**
                         * 如果mileage大于0，说明正在运动
                         * 如果mileage小于0，说明里程跳变，后期处理
                         */
                        if (mileage.compareTo(BigDecimal.ZERO) == 1){
                            lineMileage = lineMileage.add(mileage) ;
                        }else if (mileage.compareTo(BigDecimal.ZERO) == -1){
                            s_logger.info("mileage jump :{}",vinCode);
                        }
                    }

                    /** SOC处理 */
                    if (vehicleMotor.getSoc() <= esBean.getMaxSoc() && vehicleMotor.getSoc() >= esBean.getMinSoc()){
                        Integer soc = vehicleMotor.getSoc() - beforeVehicleMotor.getSoc() ;

                        if( soc <= 0 ){    //放电中
                            lineSoc -= soc ;
                        }else{ //充电中
                            /** 无效数据处理 */
                            if( lineSoc > esBean.getLinearSoc() && lineMileage.compareTo(BigDecimal.valueOf(esBean.getLinearMileage())) == 1){
                                linearSoc += lineSoc ;
                                linearMileage = linearMileage.add(lineMileage) ;
                            }
                            lineSoc = 0 ;
                            lineMileage = BigDecimal.ZERO ;
                        }
                    }
                }
                beforeVehicleMotor = vehicleMotor ;
                /** CAN数据条数计算 */
                canNumber += vehicleMotor.getMileage()!=null && vehicleMotor.getMileage().compareTo(BigDecimal.ZERO)==1 ? 1 : 0 ;
            }

            /** 最后线性SOC、近线性里程处理*/
            if( lineSoc > esBean.getLinearSoc() && lineMileage.compareTo(BigDecimal.valueOf(esBean.getLinearMileage())) == 1){
                linearSoc += lineSoc ;
                linearMileage = linearMileage.add(lineMileage) ;
            }
        }

        /** 总里程算法 去空*/
        int mileageNum = hisVehicleMotors != null ? hisVehicleMotors.size() - 1 : -1;
        while (mileageNum >= 0 ){
            if (mileageTotal == null){
                mileageTotal = hisVehicleMotors.get(mileageNum).getMileage() ;
            }
            if (mileageTotal != null) break;
            mileageNum -- ;
        }
        mileageTotal = mileageTotal == null ? BigDecimal.ZERO : mileageTotal ;

        countData.setConsumeSoc(consumeSoc);
        countData.setChargeSoc(chargeSoc);
        countData.setSurplusSoc(surplusSoc);
        countData.setMileageTotal(mileageTotal);
        countData.setChargeTime(chargeTime) ;
        countData.setDischargeTime(dischargeTime) ;
        countData.setMaxInElectricPower(maxInElectricPower);
        countData.setMaxOutElectricPower(maxOutElectricPower);
        countData.setLinearSoc(linearSoc) ;
        countData.setLinearMileage(linearMileage);
        countData.setCanNumber(canNumber);
        countData.setGpsNumber(gpsNumber);
        countData.setCountDate(new Date());
        return countData ;
    }


    /**
     * 计算每月统计
     */
    public static void countMonthData(int monthDay,int countDay){

//        HisCountDataService.deleteHisCountDataL2() ;
//        HisCountDataService.createHisCountIndexL2();

        /**
         * 根据countDay重新计算之前几天的数据
         * 删除并保存
         * */
        for (int n = 0 ; n < countDay ; n++){
            /**
             * HisCountL2 计算
             */
            Date date = new Date() ;
            Date countDate = DateUtil.getDate(date,-n) ;

            String id = "ZD"+DateUtil.getDateStryyyyMMdd(countDate); //根据时间+随机数生成
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

            Date startDate = DateUtil.getDate(date,monthDay-n) ;
            Long dataNum = HisCountDataService.getHisCountDataNumber(startDate,countDate);

            Integer num = Integer.valueOf(String.valueOf(dataNum)) ;

            /** 无数据不计算指标 */
            if(num == 0 ) continue;

            int groupNum = num/MAX_SIZE ;
            groupNum = num%MAX_SIZE > 0 ? groupNum+1 : groupNum ;

            Integer currentPage = 1 ;
            Integer pageSize = MAX_SIZE ;

            for (int i = 0 ; i < groupNum ; i++ ){
                List<HisCountData> hisCountDatas = HisCountDataService.getHisCountData(startDate, date,currentPage,pageSize) ;

                for (HisCountData hisCountData : hisCountDatas){
                    String carType = hisCountData.getCarType() ;
                    /**
                     * 1.车辆累积行驶里程
                     * 算法：当日行驶里程<=N
                     */
                    BigDecimal onedaymileage = hisCountData.getLinearMileage();
                    mileage = CountUtil.getTarget(mileage,carType,onedaymileage,Constant.MILEAGE) ;

                    /**
                     * 2.续驶里程
                     * 算法：里程差/消耗SOC * 100
                     */
                    if (BigDecimal.valueOf(hisCountData.getConsumeSoc()).compareTo(BigDecimal.ZERO)== 1 ){
                        BigDecimal limitedDriving = hisCountData.getLinearMileage().divide(BigDecimal.valueOf(hisCountData.getLinearSoc()), 2, BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100)) ;
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
                    BigDecimal maxInElectricPower = hisCountData.getMaxInElectricPower() ;
                    BigDecimal maxOutElectricPower = hisCountData.getMaxOutElectricPower() ;

                    maxElectricPower = maxInElectricPower.compareTo(maxOutElectricPower) == 1 ?
                            CountUtil.getTarget(maxElectricPower,carType,maxInElectricPower,Constant.MAXELECTRICPOWER) :
                            CountUtil.getTarget(maxElectricPower,carType,maxOutElectricPower,Constant.MAXELECTRICPOWER) ;

//                    if (maxInElectricPower.compareTo(maxOutElectricPower) == 1){
//                        maxElectricPower = CountUtil.getTarget(maxElectricPower,carType,maxInElectricPower,Constant.MAXELECTRICPOWER) ;
//                    }else{
//                        maxElectricPower = CountUtil.getTarget(maxElectricPower,carType,maxOutElectricPower,Constant.MAXELECTRICPOWER) ;
//                    }
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
                        BigDecimal hundredskmusepower = hisCountData.getLinearMileage().divide(BigDecimal.valueOf(hisCountData.getLinearSoc()), 2, BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100)) ;
                        hundredsKmusePower = CountUtil.getTarget(hundredsKmusePower,carType,hundredskmusepower,Constant.HUNDREDSKMUSEPOWER) ;
                    }else{
                        hundredsKmusePower.setInvalids(hundredsKmusePower.getInvalids()+1);
                    }

                    /**
                     * GPS正常、近期有数据、无数据
                     * CAN正常、近期有数据、无数据
                     */
                    if (hisCountData.getGpsNumber() > 0){
                        gpsNormal ++ ;
                    }else{
                        gpsNearNoData ++ ;
                        Long gpsNumber = GpsDataService.getHisGpsDataNum(hisCountData.getVinCode(),startDate,date) ;
                        gpsNoData += gpsNumber > 0 ? 0 : 1 ;
                    }
                    if (hisCountData.getCanNumber() > 0 ){
                        canNormal ++ ;
                    }else{
                        canNearNoData++ ;
                        Long CanNumber = VehicleMotorService.getHisVehicleMotorNumber(hisCountData.getVinCode());
                        canNoData += CanNumber > 0 ? 0 : 1 ;
                    }
                }

                currentPage ++ ;
            }

            /**
             * 指标数据使用：
             * 每天生成一条六项指标数据
             */
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
            /** 计算车辆数 */
            Long vehicleNum = getVehicleNum() ;
            monthCountData.setVehicleNum(Integer.valueOf(String.valueOf(vehicleNum)));

            /**
             * 删除之前数据
             * 避免重复
             * */
            HisCountDataService.deleteHisCountDataL2(id) ;

            HisCountDataService.addHisCountDataL2(monthCountData) ;
        }
    }

}
