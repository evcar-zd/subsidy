package com.evcar.subsidy.util;

import com.evcar.subsidy.entity.ESBean;
import com.evcar.subsidy.entity.EsBeanObj;
import com.evcar.subsidy.entity.LgAndLt;
import com.evcar.subsidy.entity.Statistical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.evcar.subsidy.util.Constant.AVGDAILYRUNTIME;
import static javafx.scene.input.KeyCode.K;

/**
 * 计算共用类
 * Created by Kong on 2017/4/20.
 */
@Component
public class CountUtil {

    private static ESBean esBean;
    @Autowired
    void setEsBean(ESBean value) { this.esBean = value;}

    private static Logger s_logger = LoggerFactory.getLogger(CountUtil.class);

    /**
     * 单个验证
     * @param carType   车类型
     * @param num   验算值
     * @param targeType 指标类型
     * @return
     */
    public static Integer targeVerify(String carType,BigDecimal num ,Integer targeType){
        /** 与车型无关 */
        EsBeanObj esBeanObj = esBean.getTarget().get(carType) ;
        if (esBeanObj == null) {
            esBeanObj = esBean.getTarget().get(Constant.DEFAULT) ;
        }

        if (esBeanObj == null) return -2 ;
        LgAndLt lgAndLt = null;
        switch (targeType){
            case Constant.MILEAGE :
                lgAndLt = new LgAndLt(esBeanObj.getMileageMin(),esBeanObj.getMileageMax()) ;
                break;
            case Constant.LIMITMILEAGE :
                lgAndLt = new LgAndLt(esBeanObj.getLimitMileageMin(),esBeanObj.getLimitMileageMax()) ;
                break;
            case Constant.MAXENERGYTIME1 :
                lgAndLt = new LgAndLt(esBeanObj.getEnergyTimeMinModel1(),esBeanObj.getEnergyTimeMaxModel1()) ;
                break;
            case Constant.MAXENERGYTIME2 :
                lgAndLt = new LgAndLt(esBeanObj.getEnergyTimeMinModel2(),esBeanObj.getEnergyTimeMaxModel2()) ;
                break;
            case Constant.MAXENERGYTIME3 :
                lgAndLt = new LgAndLt(esBeanObj.getEnergyTimeMinModel3(),esBeanObj.getEnergyTimeMaxModel3()) ;
                break;
            case Constant.MAXELECTRICPOWER :
                lgAndLt = new LgAndLt(esBeanObj.getElectricPowerMin(),esBeanObj.getElectricPowerMax()) ;
                break;
            case AVGDAILYRUNTIME :
                lgAndLt = new LgAndLt(esBeanObj.getAvgDailyRunTimeMin(),esBeanObj.getAvgDailyRunTimeMax()) ;
                break;
            case Constant.HUNDREDSKMUSEPOWER :
                lgAndLt = new LgAndLt(esBeanObj.getHundredsPowerMin(),esBeanObj.getHundredsPowerMax()) ;
                break;
        }
        return countTarge(num,lgAndLt) ;
    }

    /**
     * @param num
     * @param lgAndLt
     * @return
     */
    private static Integer countTarge(BigDecimal num , LgAndLt lgAndLt) {
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


    public static BigDecimal CHARGE_MODEL1 = BigDecimal.valueOf(9) ;
    public static BigDecimal CHARGE_MODELMIN2 = BigDecimal.valueOf(13) ;
    public static BigDecimal CHARGE_MODELMAX2 = BigDecimal.valueOf(16) ;
    public static BigDecimal CHARGE_MODEL3 = BigDecimal.valueOf(18) ;
    /**
     * 获取充电模式
     * @param alowableCurrent
     * @return
     */
    public static Integer getChargeModel(BigDecimal alowableCurrent){
        alowableCurrent = alowableCurrent == null ? BigDecimal.ZERO : alowableCurrent ;
        int model = 0 ;
        if (alowableCurrent.compareTo(CHARGE_MODEL1) == 0 ){
            model = 1 ;
        }else if(alowableCurrent.compareTo(CHARGE_MODELMIN2) != -1 && alowableCurrent.compareTo(CHARGE_MODELMAX2) != 1){
            model = 2 ;
        }else if (alowableCurrent.compareTo(CHARGE_MODEL3) == 0){
            model = 3 ;
        }
        return model ;
    }
}
