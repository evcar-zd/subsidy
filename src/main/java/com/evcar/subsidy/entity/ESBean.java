package com.evcar.subsidy.entity;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Kong on 2017/4/19.
 */
@Component
@ConfigurationProperties(prefix="elasticsearch")
public class ESBean {

    private String hostOne ;
    private String hostTwo ;
    private String hostThree ;
    private Integer port ;
    private String clusterName ;
    private Boolean clientTransportSniff ;

    public ESBean(){
    }

    public String getHostOne() {
        return hostOne;
    }

    public void setHostOne(String hostOne) {
        this.hostOne = hostOne;
    }

    public String getHostTwo() {
        return hostTwo;
    }

    public void setHostTwo(String hostTwo) {
        this.hostTwo = hostTwo;
    }

    public String getHostThree() {
        return hostThree;
    }

    public void setHostThree(String hostThree) {
        this.hostThree = hostThree;
    }

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
}
