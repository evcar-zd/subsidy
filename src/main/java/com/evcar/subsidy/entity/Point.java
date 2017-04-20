package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Kong on 2017/4/20.
 */
public class Point implements Serializable {

    private BigDecimal lat ;        //纬度 “正”表示“北纬”，“负”表示“南纬”
    private BigDecimal lon ;        //经度 “正”表示“东经”，“负”表示“西经”

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }
}
