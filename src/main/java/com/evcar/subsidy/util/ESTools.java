package com.evcar.subsidy.util;

import com.evcar.subsidy.entity.ESBean;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * Created by Kong on 2017/4/19.
 */
@Component
public class ESTools {
    private static Logger s_logger = LoggerFactory.getLogger(ESTools.class);

    /**
     * 创建一次Client
     * @return
     */
    public static Client build(ESBean esBean){
        Client client = null;
        try {
            Settings settings = Settings.settingsBuilder()
                    .put("cluster.name", esBean.getClusterName())
                    .put("client.transport.sniff", esBean.getClientTransportSniff())
                    .build();
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esBean.getHostOne()), esBean.getPort()))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esBean.getHostTwo()), esBean.getPort()))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esBean.getHostThree()), esBean.getPort()));
        } catch (Exception e) {
            s_logger.error(Helper.printStackTrace(e));
        }
        return client;
    }


    /**
     * 关闭Client
     */
    public static void close(Client client){
        if(null != client){
            try {
                client.close();
            } catch (Exception e) {
                s_logger.error(Helper.printStackTrace(e));
            }
        }
    }



}
