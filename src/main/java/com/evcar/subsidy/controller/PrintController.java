package com.evcar.subsidy.controller;



import com.alibaba.fastjson.JSON;
import com.evcar.subsidy.entity.Export;
import com.evcar.subsidy.entity.HisCountData;
import com.evcar.subsidy.poi.ExcelInfo;
import com.evcar.subsidy.poi.ExcelUtil;

import com.evcar.subsidy.service.HisCountDataService;
import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by lyg on 2017/5/15.
 */
@RestController
@RequestMapping("/api")
public class PrintController {

    private static Logger s_logger = LoggerFactory.getLogger(VehicleController.class);


    /***
     * 打印VehicleL2六项指标数据
     * @return
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void printVehicleL(@RequestParam Date startDay ,String vinCode)throws IOException {

        Date endDate = new Date();
        //Date startday = DateUtil.parseDate(startDay);
        List<HisCountData> hisCountDatas = HisCountDataService.getVehicleL2(startDay, endDate ,vinCode);
        if (hisCountDatas.size() > 0) {
            String fileName = "知豆汽车指标-" + DateUtil.getDateStr() + ".xls";
            String sheetName = "知豆汽车";
            String[] titles = new String[]{"id", "数据时间", "VIN码", "车辆类型", "交车日期", "GPS数据条数", "CAN数据条数", "总里程", "近似线性中段充电SOC", "近似线性中段充电时间(单位：S)", "放电总时长(单位：S)", "最大输入电功率", "最大输出电功率", "近似线性中段当日总放电SOC", "近似线性中段当日总行驶里程", "计算时间", "版本"};
            String[] fields = new String[]{"id", "tm", "vinCode", "carType", "veDeliveredDate", "gpsCount", "canCount", "mileageTotal", "chargeMidSoc", "chargeMidSec", "dischargeTotalSec", "maxInChargerPower", "maxOutChargerPower", "dischargeMidSoc", "dischargeMidMileage", "calcTime", "version"};
            List<Map<String, Object>> list = new ArrayList<>();
            for (HisCountData hisCountData : hisCountDatas) {
                Map<String, Object> map = JacksonUtil.transBean2Map(hisCountData);
                list.add(map);
            }
            ExcelInfo excelInfo = new ExcelInfo(fileName, sheetName, titles, fields, list);
            File file = new File("E:\\autotemp");
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
            OutputStream outStream = new FileOutputStream(new File(file + "\\" + fileName));
            ExcelUtil.write2Page(excelInfo, outStream);

        }

    }


    @RequestMapping(value = "/exportoo", method = RequestMethod.GET)
    public void printVehicleL3() throws IOException{
        String fileName = "知豆汽车指标-"+ DateUtil.getDateStr()+".xls";
        String sheetName = "知豆汽车";
        String[] titles = new String[]{"vinCode","里程","VIN码","车辆类型","近似线性中段当日总行驶里程","计算时间","版本"};
        String[] fields = new String[]{"vinCode","mileage","limitMileage","maxEnergyTime","maxElectricPower","avgDailyRunTime","hundredsKmusePower"};
        List<Map<String, Object>> list=new ArrayList<>();
        String json="";
        List<Export> ExportList = JSON.parseArray(json,Export.class);
        for(Export export:ExportList){
            Map<String, Object> map = JacksonUtil.transBean2Map(export);
            list.add(map);
        }
        ExcelInfo excelInfo=new ExcelInfo(fileName,sheetName,titles,fields,list);
        File file =new File("E:\\autotemp") ;
        if(!file .exists()  && !file .isDirectory()){
            file.mkdir();
        }
        OutputStream outStream = new FileOutputStream(new File(file+"\\"+fileName));
        ExcelUtil.write2Page(excelInfo, outStream);

    }



}
