package com.evcar.subsidy.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Kong on 2017/4/19.
 */
public class CarProvince implements Serializable{
    private String name ;
    private BigDecimal longitude ;
    private BigDecimal latitude ;

    public CarProvince(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}
