package com.evcar.subsidy.util;

import com.evcar.subsidy.entity.LgAndLt;
import com.evcar.subsidy.entity.Statistical;
import com.evcar.subsidy.entity.TargeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static com.evcar.subsidy.util.Constant.MILEAGE;
import static com.evcar.subsidy.util.Constant.init;

/**
 * 计算共用类
 * Created by Kong on 2017/4/20.
 */
public class CountUtil {

    private static Logger s_logger = LoggerFactory.getLogger(CountUtil.class);
    /**
     * 单个验证
     * @param carType   车类型
     * @param num   验算值
     * @param targeType 指标类型
     * @return
     */
    public static Integer targeVerify(String carType,BigDecimal num ,Integer targeType){
        TargeBean targeBean = Constant.targetmap.get(carType) ;
        if (targeBean == null) return -2 ;
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

    /**
     * @param num
     * @param lgAndLt
     * @return
     */
    public static Integer countTarge(BigDecimal num , LgAndLt lgAndLt) {
        Integer flag = -1 ;  //默认无效
        if (num!=null && lgAndLt != null){
            if (num.compareTo(lgAndLt.getMin())!=-1 && num.compareTo(lgAndLt.getMax()) != 1){
                flag = 1 ;
            }else if(num.compareTo(lgAndLt.getMin()) == -1){
                flag = 0 ; //低于
            }else if(num.compareTo(lgAndLt.getMax()) == 1){
                flag = 2 ; //高于
            }
        }
        return flag ;
    }

    /**
     * 数据处理
     * @param statistical   对象
     * @param carType   车类型
     * @param num   验算值
     * @param targeType 指标类型
     * @return
     */
    public static Statistical getTarget(Statistical statistical,String carType,BigDecimal num ,Integer targeType){

        Integer number = targeVerify(carType,num,targeType);
        if (number == -2){
            s_logger.error(carType+"不存在计算指标内");
            statistical.setInvalids(statistical.getInvalids()+1) ;
            return statistical ;
        }
        switch (number){
            case -1 :
                statistical.setInvalids(statistical.getInvalids()+1);
                break;
            case 0 :
                statistical.setLowSide(statistical.getLowSide()+1);
                break;
            case 1 :
                statistical.setNormal(statistical.getNormal()+1);
                break;
            case 2 :
                statistical.setHighSide(statistical.getHighSide()+1);
        }
        return statistical ;
    }

    /**
     * L3运算工具类
     * @param model
     * @param statistical
     * @return
     */
    public static Statistical getStatistical(Statistical model ,Statistical statistical){
        if (model == null) model = new Statistical() ;
        model.setNormal(model.getNormal()+statistical.getNormal());
        model.setHighSide(model.getHighSide()+statistical.getHighSide());
        model.setLowSide(model.getLowSide()+statistical.getLowSide());
        model.setInvalids(model.getInvalids()+statistical.getInvalids());
        return model ;
    }
}
