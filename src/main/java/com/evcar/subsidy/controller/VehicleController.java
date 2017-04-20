package com.evcar.subsidy.controller;

import com.evcar.subsidy.entity.ESBean;
import com.evcar.subsidy.entity.Vehicle;
import com.evcar.subsidy.util.*;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Kong on 2017/4/20.
 */
@RestController
@RequestMapping("/api")
public class VehicleController {
    private static Logger s_logger = LoggerFactory.getLogger(VehicleController.class);

    private ESBean esBean;
    @Autowired
    void setEsBean(ESBean value) { this.esBean = value;}

    /**
     * 获取所有车辆信息
     * @return
     */
    @RequestMapping(value = "/getAllVehicle", method = RequestMethod.GET)
    public List<Vehicle> getAllVehicle() {
        List<Vehicle> list = new ArrayList<>() ;
        Client client = null;
            //创建client
            client = ESTools.getClient(esBean) ;

            list = SelectVehicle.getVehicleList(client) ;
        } finally {
            /** 用完关闭client */
            if(client != null)
                ESTools.close(client);

        }
        return list;
    }
}
