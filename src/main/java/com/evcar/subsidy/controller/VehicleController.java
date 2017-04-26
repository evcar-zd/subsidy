package com.evcar.subsidy.controller;

import com.evcar.subsidy.entity.Vehicle;
import com.evcar.subsidy.service.VehicleService;
import com.evcar.subsidy.util.TargetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * Created by Kong on 2017/4/20.
 */
@RestController
@RequestMapping("/api")
public class VehicleController {
    private static Logger s_logger = LoggerFactory.getLogger(VehicleController.class);


    /***
     * 获取车辆总数
     * @return
     */
    @RequestMapping(value = "/getVehicleNum", method = RequestMethod.GET)
    public Long getVehicleNum(){
        return VehicleService.getVehicleNum() ;
    }

    /**
     * 获取所有车辆信息
     * @return
     */
    @RequestMapping(value = "/getAllVehicle", method = RequestMethod.GET)
    public List<Vehicle> getAllVehicle() {
        return VehicleService.getVehicleList() ;
    }



}
