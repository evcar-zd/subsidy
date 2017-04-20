package com.evcar.subsidy.controller;

import com.evcar.subsidy.entity.ESBean;
import com.evcar.subsidy.entity.Vehicle;
import com.evcar.subsidy.util.ESTools;
import com.evcar.subsidy.util.SelectVehicle;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Kong on 2017/4/20.
 */
@RestController
@RequestMapping("/api")
public class VehicleController {

    @Autowired
    ESBean esBean ;

    public static SelectVehicle selectManager = new SelectVehicle();

    /**
     * 获取所有车辆信息
     * @return
     */
    @RequestMapping(value = "/getAllVehicle", method = RequestMethod.GET)
    public List<Vehicle> getAllVehicle() {
        List<Vehicle> list = new ArrayList<>() ;
        try {
            //创建client
            Client client = ESTools.build(esBean) ;

            //创建测试数据
//            List<String> jsonData = DataFactory.getInitJsonData();
//            for (int i = 0; i < jsonData.size(); i++) {
//                IndexResponse response = client.prepareIndex("blog", "article",String.valueOf(i+1)).setSource(jsonData.get(i)).get();
//                if (response.isCreated()) {
//                    System.out.println("创建成功!");
//                }
//            }


            list = selectManager.getVehicleList(client) ;

            /** 用完关闭client */
            ESTools.close(client);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
