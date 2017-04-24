package com.evcar.subsidy.util;

import com.evcar.subsidy.entity.LgAndLt;
import com.evcar.subsidy.entity.TargeBean;
import com.evcar.subsidy.entity.Vehicle;
import com.evcar.subsidy.service.VehicleService;

import java.math.BigDecimal;
import java.util.List;

import static com.evcar.subsidy.service.VehicleService.getVehicleNum;

/**
 * 指标验证工具类
 * Created by Kong on 2017/4/20.
 */
public class TargetUtil {

    /**
     * 单个验证
     */
    public static Boolean targeVerify(String carType,BigDecimal num ,Integer targeType){
        TargeBean targeBean = Constant.targetmap.get(carType) ;
        LgAndLt lgAndLt = null;
        switch (targeType){
            case Constant.MILEAGE :
                lgAndLt = targeBean.getMileage() ;
                break;
            case Constant.LIMITMILEAGE :
                lgAndLt = targeBean.getLimitMileage() ;
                break;
            case Constant.MAXENERGYTIME :
                lgAndLt = targeBean.getMaxEnergyTime() ;
                break;
            case Constant.MAXELECTRICPOWER :
                lgAndLt = targeBean.getMaxElectricPower() ;
                break;
            case Constant.AVGDAILYRUNTIME :
                lgAndLt = targeBean.getAvgDailyRunTime() ;
                break;
            case Constant.HUNDREDSKMUSEPOWER :
                lgAndLt = targeBean.getHundredsKmusePower() ;
                break;
        }
        return CountUtil.countTarge(num,lgAndLt) ;
    }

    public static final Integer MAX_SIZE = 100 ;

    /**
     * 六项指标计算
     */
    public static void targeCount(){
        Long vehicleNum = VehicleService.getVehicleNum() ;
        Integer num = Integer.valueOf(String.valueOf(vehicleNum)) ;

        int groupNum = num/MAX_SIZE ;
        groupNum = num%MAX_SIZE > 0 ? groupNum+1 : groupNum ;

        Integer currentPage = 1 ;
        Integer pageSize = MAX_SIZE ;

        Vehicle lastVehicle =
        for (int i = 0 ; i < groupNum ; i++ ){
            List<Vehicle> vehicleList = VehicleService.getVehicleByPage(currentPage,pageSize) ;

            for (Vehicle vehicle : vehicleList){

            }
            currentPage ++ ;
        }
    }

}
