package com.evcar.subsidy.agg;

import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.OrganizationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Kong on 2017/5/12.
 */
public class VehicleL2 extends VehicleBase{

    private static Logger s_logger = LoggerFactory.getLogger(VehicleL2.class);

    protected HisCountData hisCountData ;

    /**
     * 计算单车每N天的统计方法
     * @param vehicleVo
     * @param startDate
     * @param endDate
     */
    protected void calc(VehicleVo vehicleVo, Date startDate , Date endDate, int monthDay){

        this.loadL1(vehicleVo.getVinCode(),startDate,endDate);

        this.calcVehicleL2(vehicleVo,startDate, endDate, monthDay);

    }

    private void calcVehicleL2(VehicleVo vehicleVo,Date startDate,Date endDate,int monthDay){

        int diffNum = DateUtil.diffDate(startDate,endDate) ;

        for (int i = 0 ; i < diffNum ;i++){
            Date start = DateUtil.getStartDate(startDate,i) ;
            Date end = DateUtil.getEndDate(startDate,monthDay-1) ;

            /** 当不满足monthDay天数时，执行最后一次 */
            if (DateUtil.compare(end,endDate) || DateUtil.diffDate(end,endDate) == 0){
                i = diffNum ;
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
     * 计算
     * @param vehicleVo
     * @param startDate
     * @param endDate
     */
    private void calcTargetL2(VehicleVo vehicleVo,Date startDate ,Date endDate,List<HisCountData> hisCountDatas){

        hisCountData = new HisCountData() ;

        String vinCode = vehicleVo.getVinCode() ;
        String id = vinCode + DateUtil.getDateStryyyyMMdd(endDate);
        String carType = vehicleVo.getCarType() ;
        Date veDeliveredDate = vehicleVo.getVeDeliveredDate() ;
        Date releaseTime = vehicleVo.getReleaseTime() ;
        this.hisCountData = new HisCountData() ;

        this.hisCountData.setId(id);
        this.hisCountData.setTm(endDate);
        this.hisCountData.setVinCode(vinCode);
        this.hisCountData.setCarType(carType);
        this.hisCountData.setVeDeliveredDate(veDeliveredDate);
        this.hisCountData.setReleaseTime(releaseTime);

        Integer gpsCount = 0 ;                                  //GPS数据条数
        Integer canCount = 0 ;                                  //CAN数据条数
        BigDecimal mileageTotal = BigDecimal.ZERO;              //总里程
        Integer chargeMidSoc = 0 ;                              //近似线性中段充电SOC
        Long chargeMidSec = Long.valueOf(0);                    //近似线性中段充电时间(单位：S)
        Long dischargeTotalSec = Long.valueOf(0) ;              //放电总时长(单位：S)
        BigDecimal maxInChargerPower = BigDecimal.ZERO ;        //最大输入电功率
        BigDecimal maxOutChargerPower = BigDecimal.ZERO ;       //最大输出电功率
        Integer dischargeMidSoc = 0 ;                           //近似线性中段当日总放电SOC
        BigDecimal dischargeMidMileage = BigDecimal.ZERO ;      //近似线性中段当日总行驶里程

        int hisSize = hisCountDatas.size() ;
        if(hisSize > 0){
            for(HisCountData hisCountData : hisCountDatas){
                gpsCount += hisCountData.getGpsCount() ;
                canCount += hisCountData.getCanCount() ;
                chargeMidSoc += hisCountData.getChargeMidSoc() ;
                chargeMidSec += hisCountData.getChargeMidSec() ;
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


        /** 平均单日运行时间 */
        int diffNum = DateUtil.diffDate(startDate,endDate)+ 1 ;
        dischargeTotalSec = dischargeTotalSec / diffNum ;

        this.hisCountData.setGpsCount(gpsCount);
        this.hisCountData.setCanCount(canCount);
        this.hisCountData.setMileageTotal(mileageTotal);
        this.hisCountData.setChargeMidSoc(chargeMidSoc);
        this.hisCountData.setChargeMidSec(chargeMidSec);
        this.hisCountData.setDischargeTotalSec(dischargeTotalSec);
        this.hisCountData.setMaxInChargerPower(maxInChargerPower);
        this.hisCountData.setMaxOutChargerPower(maxOutChargerPower);
        this.hisCountData.setDischargeMidSoc(dischargeMidSoc);
        this.hisCountData.setDischargeMidMileage(dischargeMidMileage);
        this.hisCountData.setCalcTime(new Date());

    }

}
