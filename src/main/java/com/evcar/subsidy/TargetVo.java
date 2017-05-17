package com.evcar.subsidy;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Kong on 2017/5/9.
 */
public class TargetVo implements Serializable {

    private BigDecimal targetNum ;
    private Date calcTime ;

    public TargetVo(){

    }

    public BigDecimal getTargetNum() {
        return targetNum;
    }

    public void setTargetNum(BigDecimal targetNum) {
        this.targetNum = targetNum;
    }

    public Date getCalcTime() {
        return calcTime;
    }

    public void setCalcTime(Date calcTime) {
        this.calcTime = calcTime;
    }
}
