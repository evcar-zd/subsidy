package com.evcar.subsidy.entity;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

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


}
