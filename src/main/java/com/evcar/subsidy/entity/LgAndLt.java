package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Kong on 2017/4/20.
 */
public class LgAndLt implements Serializable{

    private BigDecimal min ;
    private BigDecimal max ;

    private LgAndLt(){

    }

    public LgAndLt(BigDecimal min ,BigDecimal max){
        this.min = min ;
        this.max = max ;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }
}
