package com.evcar.subsidy.entity;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Kong on 2017/4/19.
 */
@Component
@ConfigurationProperties(prefix="elasticsearch")
public class ESBean {

    private List<String> host;
    private Integer port ;
    private String clusterName ;
    private Boolean clientTransportSniff ;
    private Integer minSoc ;
    private Integer maxSoc ;
    private Integer linearSoc ;         //线性SOC
    private Integer linearMileage ;     //线性里程
    private Integer startDate ;
    private Integer endDate ;
    private Integer monthDay ;
    private Map<String,EsBeanObj> target ;

    public ESBean(){
    }

    public List<String> getHost() { return host; }

    public void setHost(List<String> host) { this.host = host; }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Boolean getClientTransportSniff() {
        return clientTransportSniff;
    }

    public void setClientTransportSniff(Boolean clientTransportSniff) {
        this.clientTransportSniff = clientTransportSniff;
    }

    public Integer getMinSoc() {
        return minSoc;
    }

    public void setMinSoc(Integer minSoc) {
        this.minSoc = minSoc;
    }

    public Integer getMaxSoc() {
        return maxSoc;
    }

    public Integer getLinearSoc() {
        return linearSoc;
    }

    public void setLinearSoc(Integer linearSoc) {
        this.linearSoc = linearSoc;
    }

    public Integer getLinearMileage() {
        return linearMileage;
    }

    public void setLinearMileage(Integer linearMileage) {
        this.linearMileage = linearMileage;
    }

    public void setMaxSoc(Integer maxSoc) {
        this.maxSoc = maxSoc;
    }

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public Integer getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(Integer monthDay) {
        this.monthDay = monthDay;
    }

    public Map<String, EsBeanObj> getTarget() {
        return target;
    }

    public void setTarget(Map<String, EsBeanObj> target) {
        this.target = target;
    }
}
