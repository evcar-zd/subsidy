package com.evcar.subsidy.util;

import com.evcar.subsidy.entity.ESBean;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Kong on 2017/4/19.
 */
@Component
public class ESTools {
    private static Logger s_logger = LoggerFactory.getLogger(ESTools.class);

    private static Client s_client = null;

    private static ESBean esBean;
    @Autowired
    void setEsBean(ESBean value) { this.esBean = value;}

    /**
     * 创建一次Client
     * @return
     */
    public static synchronized Client getClient(){
        try {
            if (s_client == null && esBean != null) {
                Settings settings = Settings.settingsBuilder()
                        .put("cluster.name", esBean.getClusterName())
                        .put("client.transport.sniff", esBean.getClientTransportSniff())
                        .build();
                TransportClient tc = TransportClient.builder().settings(settings).build();

                if(esBean.getHost().size() == 0){
                    throw new RuntimeException("Missing config entry: elasticsearch.host");
                }

                for (String host : esBean.getHost()) {
                    s_logger.info("ElasticSearch Host: {}", host);
                    s_client = tc.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), esBean.getPort()));
                }
            }
        }
        catch(UnknownHostException ex){
            throw new RuntimeException(ex);
        }
        return s_client;
    }


    /**
     * 关闭Client
     */
    public static synchronized void clearClient(){
        if(s_client != null){
            try {
                s_client.close();
                s_client = null;
            } catch (Exception e) {
                s_logger.error(Helper.printStackTrace(e));
            }
        }
    }

    /**
     * 连接失败，等待3S
     */
    public static void connectionError(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
