package com.evcar.subsidy.util;

import com.evcar.subsidy.entity.LgAndLt;

import java.math.BigDecimal;

/**
 * 计算共用类
 * Created by Kong on 2017/4/20.
 */
public class CountUtil {

    public static Boolean countTarge(BigDecimal num , LgAndLt lgAndLt) {
        boolean flag = false ;
        if (num!=null && lgAndLt != null){
            if (num.compareTo(lgAndLt.getMin())!=-1 && num.compareTo(lgAndLt.getMax()) != 1){
                flag = true ;
            }
        }
        return flag ;
    }
}
