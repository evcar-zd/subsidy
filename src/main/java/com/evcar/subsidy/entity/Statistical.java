package com.evcar.subsidy.entity;

import java.io.Serializable;

/**
 * Created by Kong on 2017/4/24.
 */
public class Statistical implements Serializable {

    private Integer normal ;
    private Integer lowSide ;
    private Integer highSide ;
    private Integer invalids ;

    public Statistical(){
        this.normal = 0 ;
        this.lowSide = 0 ;
        this.highSide = 0 ;
        this.invalids = 0 ;
    }

    public Integer getNormal() {
        return normal;
    }

    public void setNormal(Integer normal) {
        this.normal = normal;
    }

    public Integer getLowSide() {
        return lowSide;
    }

    public void setLowSide(Integer lowSide) {
        this.lowSide = lowSide;
    }

    public Integer getHighSide() {
        return highSide;
    }

    public void setHighSide(Integer highSide) {
        this.highSide = highSide;
    }

    public Integer getInvalids() {
        return invalids;
    }

    public void setInvalids(Integer invalids) {
        this.invalids = invalids;
    }
}
