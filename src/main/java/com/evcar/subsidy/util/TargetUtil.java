package com.evcar.subsidy.util;

import com.evcar.subsidy.entity.LgAndLt;
import com.evcar.subsidy.entity.TargeBean;

import java.math.BigDecimal;

/**
 * 指标验证工具类
 * Created by Kong on 2017/4/20.
 */
public class TargetUtil {

    /**
     * 单个验证
     */
    public static Boolean mileageVerify(String carType,BigDecimal num ,Integer targeType){
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

    

}
