package com.evcar.subsidy.agg;

import com.evcar.subsidy.entity.HisCountData;
import com.evcar.subsidy.entity.VehicleVo;
import com.evcar.subsidy.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

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
    protected void calc(VehicleVo vehicleVo, Date startDate , Date endDate){

        this.loadL1(vehicleVo.getVinCode(),startDate,endDate);

        this.calcVehicleL2(vehicleVo,endDate);

        this.saveL2(this.hisCountData);

    }

    /**
     * 计算
     * @param vehicleVo
     * @param endDate
     */
    private void calcVehicleL2(VehicleVo vehicleVo,Date endDate){
        hisCountData = new HisCountData() ;

        String vinCode = vehicleVo.getVinCode() ;
        String id = vinCode + DateUtil.getDateStryyyyMMdd(endDate);
        String carType = vehicleVo.getCarType() ;
        Date veDeliveredDate = vehicleVo.getVeDeliveredDate() ;
        this.hisCountData = new HisCountData() ;

        this.hisCountData.setId(id);
        this.hisCountData.setTm(endDate);
        this.hisCountData.setVinCode(vinCode);
        this.hisCountData.setCarType(carType);
        this.hisCountData.setVeDeliveredDate(veDeliveredDate);


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
                maxInChargerPower = maxInChargerPower.add(hisCountData.getMaxInChargerPower()) ;
                maxOutChargerPower = maxOutChargerPower.add(hisCountData.getMaxOutChargerPower()) ;
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
