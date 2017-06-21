package com.evcar.subsidy.controller;

import com.alibaba.fastjson.JSONObject;
import com.evcar.subsidy.entity.*;
import com.evcar.subsidy.poi.ExcelInfo;
import com.evcar.subsidy.poi.ExcelUtil;

import com.evcar.subsidy.service.*;
import com.evcar.subsidy.util.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.client.Client;
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

import static com.evcar.subsidy.service.HisCountDataService.*;

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
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void printVehicleL(@RequestParam Date startDay ,
                              @RequestParam Date endDate ,
                              @RequestParam String vinCode)throws IOException {
        Client client = ESTools.getClient() ;
        List<HisCountDataL2> hisCountDatas = getVehicleL2(startDay,endDate,vinCode,client);
        List<HisCountData> hisCountDatal1 = getHisCountData(vinCode,startDay,endDate,client);
        Long sizeNum = ObcDataService.getHisObcDataNum(vinCode, startDay, endDate, client);
        List<ObcData> hisObcData = ObcDataService.getHisObcData(vinCode, startDay, endDate, sizeNum,client);
        Long sizeNum1 = BmsDataService.getHisBmsDataNum(vinCode, startDay, endDate, client);
        List<BmsData> hisBmsData = BmsDataService.getHisBmsData(vinCode, startDay, endDate, sizeNum1, client);
        Long sizeNum2 =GpsDataService.getHisGpsDataNum(vinCode, startDay, endDate, client);
        List<HisGpsData> hisGpsData = GpsDataService.getHisGpsData(vinCode, startDay, endDate, sizeNum1, client);
        Long hisVehicleMotorNum = VehicleMotorService.getHisVehicleMotorNum(vinCode, startDay, endDate, client);
        List<HisVehicleMotor> hisVehicleMotor = VehicleMotorService.getHisVehicleMotor(vinCode, startDay, endDate, sizeNum1, client);
        //L2
        if (hisCountDatas.size() > 0) {
            String fileName = "知豆汽车近30天指标平均L2-" + DateUtil.getDateStr() + ".xlsx";
            String sheetName = "知豆汽车近30天指标平均L2";
            String[] titles = new String[]{"id", "数据时间", "VIN码", "车辆类型", "交车日期", "GPS数据条数","CAN数据条数","GPS历史数据条数","CAN历史数据条数","总里程","近似线性中段充电SOC - Model1","近似线性中段充电时间(单位：S) - Model1","近似线性中段充电SOC - Model2","近似线性中段充电时间(单位：S) - Model2","近似线性中段充电SOC - Model3","近似线性中段充电时间(单位：S) - Model3","放电总时长(单位：S)", "最大输入电功率", "最大输出电功率", "近似线性中段当日总放电SOC", "近似线性中段当日总行驶里程", "计算时间", "版本","里程","续驶里程","一次满电最少时间（单位：h） - Model1","一次满电最少时间（单位：h） - Model2", "一次满电最少时间（单位：h） - Model3","最大充电功率","平均单日运行时间","百公里耗电","Can标注0 近期无数据, 1 正常, -1 无数据","GPS标注0 近期无数据, 1 正常, -1 无数据","里程标注-1 无效, 0 偏低, 1 正常, 2 偏高","续驶里程标注1 无效, 0 偏低, 1 正常, 2 偏高","满电最少时间标注-1 无效, 0 偏低, 1 正常, 2 偏高","最大充电功率标注-1 无效, 0 偏低, 1 正常, 2 偏高","平均单日运行时间标注-1 无效, 0 偏低, 1 正常, 2 偏高","百公里耗电标注-1 无效, 0 偏低, 1 正常, 2 偏高"};
            String[] fields = new String[]{"id", "tm", "vinCode", "carType", "veDeliveredDate", "gpsCount", "canCount","gpshisCount","canhisCount","mileageTotal","chargeMidSoc1","chargeMidSec1","chargeMidSoc2","chargeMidSec2","chargeMidSoc3","chargeMidSec3","dischargeTotalSec","maxInChargerPower","maxOutChargerPower","dischargeMidSoc","dischargeMidMileage","calcTime","version","mileage","limitMileage","maxEnergyTime1","maxEnergyTime2","maxEnergyTime3","maxElectricPower","avgDailyRunTime","hundredsKmusePower","canMark","gpsMark","mileageMark","limitMileageMark","maxEnergyTimeMark","maxElectricPowerMark","avgDailyRunTimeMark","hundredsKmusePowerMark"};
            List<Map<String, Object>> list = new ArrayList<>();
            for (HisCountDataL2 hisCountData : hisCountDatas) {
                Map<String, Object> map = JacksonUtil.transBean2Map(hisCountData) ;
                Date tm = (Date)map.get("tm");
                if (tm != null ){
                    map.put("tm", DateUtil.dateToStr(tm,DateUtil.DATEFORMATYYYYMMDD)) ;
                }
                Date veDeliveredDate = (Date)map.get("veDeliveredDate");
                if (veDeliveredDate != null ){
                    map.put("veDeliveredDate", DateUtil.dateToStr(veDeliveredDate,DateUtil.DATEFORMATYYYYMMDD)) ;
                }
                Date calcTime = (Date)map.get("calcTime");
                if (calcTime != null ){
                    map.put("calcTime", DateUtil.dateToStr(calcTime,DateUtil.DATEFORMATYYYYMMDD)) ;
                }

                list.add(map);
            }
            ExcelInfo excelInfo = new ExcelInfo(fileName, sheetName, titles, fields, list);
            File file = new File("E:\\autotemp");
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
            OutputStream outStream = new FileOutputStream(new File(file + "\\" + fileName));
            ExcelUtil.write(excelInfo, outStream);

        }
        //L1
        if (hisCountDatal1.size() > 0) {
            String fileName = "知豆汽车指标当日各项指标L1-" + DateUtil.getDateStr() + ".xlsx";
            String sheetName = "知豆汽车当日各项指标L1";
            String[] titlesl1 = new String[]{"id", "数据时间", "VIN码", "车辆类型", "交车日期", "GPS数据条数","CAN数据条数","总里程","近似线性中段充电SOC - Model1","近似线性中段充电时间(单位：S) - Model1","近似线性中段充电SOC - Model2","近似线性中段充电时间(单位：S) - Model2","近似线性中段充电SOC - Model3","近似线性中段充电时间(单位：S) - Model3","放电总时长(单位：S)", "最大输入电功率", "最大输出电功率", "近似线性中段当日总放电SOC", "近似线性中段当日总行驶里程", "计算时间", "版本"};
            String[] fieldsl1 = new String[]{"id", "tm", "vinCode", "carType", "veDeliveredDate", "gpsCount", "canCount","mileageTotal","chargeMidSoc1","chargeMidSec1","chargeMidSoc2","chargeMidSec2","chargeMidSoc3","chargeMidSec3","dischargeTotalSec","maxInChargerPower","maxOutChargerPower","dischargeMidSoc","dischargeMidMileage","calcTime","version"};
            List<Map<String, Object>> list = new ArrayList<>();
            for (HisCountData hisCountData : hisCountDatal1) {
                Map<String, Object> map = JacksonUtil.transBean2Map(hisCountData);

                Date tm = (Date)map.get("tm");
                if (tm != null ){
                    map.put("tm", DateUtil.dateToStr(tm,DateUtil.DATEFORMATYYYYMMDD)) ;
                }
                Date veDeliveredDate = (Date)map.get("veDeliveredDate");
                if (veDeliveredDate != null ){
                    map.put("veDeliveredDate", DateUtil.dateToStr(veDeliveredDate,DateUtil.DATEFORMATYYYYMMDD)) ;
                }
                Date calcTime = (Date)map.get("calcTime");
                if (calcTime != null ){
                    map.put("calcTime", DateUtil.dateToStr(calcTime,DateUtil.DATEFORMATYYYYMMDD)) ;
                }
                list.add(map);
            }
            ExcelInfo excelInfo = new ExcelInfo(fileName, sheetName, titlesl1, fieldsl1, list);
            File file = new File("E:\\autotemp");
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
            OutputStream outStream = new FileOutputStream(new File(file + "\\" + fileName));
            ExcelUtil.write(excelInfo, outStream);

        }
        //BMS
        if (hisBmsData.size() > 0) {
            String fileName = "知豆汽车指标BMS-" + DateUtil.getDateStr() + ".xlsx";
            String sheetName = "知豆汽车BMS";
            String[] titlesbms = new String[]{"VIN", "采集时间", "接受时间", "GPRS号","电池组当前状态" };
            String[] fieldsbms = new String[]{"vinCode", "collectTime", "reciveTime", "gprsCode","batteryGroupStatus"};
            List<Map<String, Object>> list = new ArrayList<>();
            for (BmsData bmsData : hisBmsData) {
                BmsData Bms=new BmsData();
                Bms.setVinCode(bmsData.getVinCode());
                Bms.setCollectTime(bmsData.getCollectTime());
                Bms.setReciveTime(bmsData.getReciveTime());
                Bms.setGprsCode(bmsData.getGprsCode());
                Bms.setBatteryGroupStatus(bmsData.getBatteryGroupStatus());
                Map<String, Object> map = JacksonUtil.transBean2Map(Bms);

                Date collectTime = (Date)map.get("collectTime");
                if (collectTime != null ){
                    map.put("collectTime", DateUtil.dateToStr(collectTime,DateUtil.DATEFORMATYYYYMMDDHHmmss)) ;
                }
                Date reciveTime = (Date)map.get("reciveTime");
                if (reciveTime != null ){
                    map.put("reciveTime", DateUtil.dateToStr(reciveTime,DateUtil.DATEFORMATYYYYMMDDHHmmss)) ;
                }

                list.add(map);
            }
            ExcelInfo excelInfo = new ExcelInfo(fileName, sheetName, titlesbms, fieldsbms, list);
            File file = new File("E:\\autotemp");
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
            OutputStream outStream = new FileOutputStream(new File(file + "\\" + fileName));
            ExcelUtil.write(excelInfo, outStream);

        }

        //OBC
        if (hisObcData.size() > 0) {
            String fileName = "知豆汽车指标OBC数据-" + DateUtil.getDateStr() + ".xlsx";
            String sheetName = "知豆汽车OBC数据";
            String[] titlesobc = new String[]{"VIN", "采集时间", "接受时间", "GPRS号","充电机输出电压" ,"充电机输出电流","输入电压","输入电流"};
            String[] fieldsobc = new String[]{"vinCode", "collectTime", "reciveTime", "gprsCode","outVoltage","outCurrent","inVoltage","inCurrent"};
            List<Map<String, Object>> list = new ArrayList<>();
            for (ObcData obcData : hisObcData) {
                ObcData obc=new ObcData();
                obc.setVinCode(obcData.getVinCode());
                obc.setCollectTime(obcData.getCollectTime());
                obc.setReciveTime(obcData.getReciveTime());
                obc.setGprsCode(obcData.getGprsCode());
                obc.setOutVoltage(obcData.getOutVoltage());
                obc.setOutCurrent(obcData.getOutCurrent());
                obc.setInOutvoltage(obcData.getInOutvoltage());
                obc.setInCurrent(obcData.getInCurrent());
                Map<String, Object> map = JacksonUtil.transBean2Map(obc);
                Date collectTime = (Date)map.get("collectTime");
                if (collectTime != null ){
                    map.put("collectTime", DateUtil.dateToStr(collectTime,DateUtil.DATEFORMATYYYYMMDDHHmmss)) ;
                }
                Date reciveTime = (Date)map.get("reciveTime");
                if (reciveTime != null ){
                    map.put("reciveTime", DateUtil.dateToStr(reciveTime,DateUtil.DATEFORMATYYYYMMDDHHmmss)) ;
                }
                list.add(map);
            }
            ExcelInfo excelInfo = new ExcelInfo(fileName, sheetName, titlesobc, fieldsobc, list);
            File file = new File("E:\\autotemp");
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
            OutputStream outStream = new FileOutputStream(new File(file + "\\" + fileName));
            ExcelUtil.write(excelInfo, outStream);

        }

        //GPS
        if (hisGpsData.size() > 0) {
            String fileName = "知豆汽车指标GPS数据-" + DateUtil.getDateStr() + ".xlsx";
            String sheetName = "知豆汽车GPS数据";
            String[] titlesobcgps = new String[]{"VIN", "采集时间", "接受时间", "GPRS号","是否补传0 实时上传,1 盲区补传" ,"定位标识0 GPS未定位，1 GPS已定位"};
            String[] fieldsobcgps = new String[]{"vinCode", "collectTime", "reciveTime", "gprsCode","isOntime","locationSign"};
            List<Map<String, Object>> list = new ArrayList<>();
            for (HisGpsData hisGps : hisGpsData) {
                HisGpsData gps=new HisGpsData();
                gps.setVinCode(hisGps.getVinCode());
                gps.setCollectTime(hisGps.getCollectTime());
                gps.setReciveTime(hisGps.getReciveTime());
                gps.setGprsCode(hisGps.getGprsCode());
                gps.setIsOntime(hisGps.getIsOntime());
                gps.setLocationSign(hisGps.getLocationSign());
                Map<String, Object> map = JacksonUtil.transBean2Map(gps);
                Date collectTime = (Date)map.get("collectTime");
                if (collectTime != null ){
                    map.put("collectTime", DateUtil.dateToStr(collectTime,DateUtil.DATEFORMATYYYYMMDDHHmmss)) ;
                }
                Date reciveTime = (Date)map.get("reciveTime");
                if (reciveTime != null ){
                    map.put("reciveTime", DateUtil.dateToStr(reciveTime,DateUtil.DATEFORMATYYYYMMDDHHmmss)) ;
                }
                list.add(map);
            }
            ExcelInfo excelInfo = new ExcelInfo(fileName, sheetName, titlesobcgps, fieldsobcgps, list);
            File file = new File("E:\\autotemp");
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
            OutputStream outStream = new FileOutputStream(new File(file + "\\" + fileName));
            ExcelUtil.write(excelInfo, outStream);

        }
       //历史整车-电机数据
        if (hisVehicleMotor.size() > 0) {
            String fileName = "知豆汽车指标历史机车-电机数据" + DateUtil.getDateStr() + ".xlsx";
            String sheetName = "知豆汽车历史机车-电机数据";
            String[] titlesobcVeh = new String[]{"VIN", "采集时间", "接受时间", "GPRS号","里程" ,"SOC","总电压","总电流"};
            String[] fieldsobcVeh = new String[]{"vinCode", "collectTime", "reciveTime", "gprsCode","mileage","SOC","totalVoltage","totalCurrent"};
            List<Map<String, Object>> list = new ArrayList<>();
            for (HisVehicleMotor HisVeh : hisVehicleMotor) {
                HisVehicleMotor HisVehicleMo=new HisVehicleMotor();
                HisVehicleMo.setVinCode(HisVeh.getVinCode());
                HisVehicleMo.setCollectTime(HisVeh.getCollectTime());
                HisVehicleMo.setReciveTime(HisVeh.getReciveTime());
                HisVehicleMo.setGprsCode(HisVeh.getGprsCode());
                HisVehicleMo.setMileage(HisVeh.getMileage());
                HisVehicleMo.setSoc(HisVeh.getSoc());
                HisVehicleMo.setTotalVoltage(HisVeh.getTotalVoltage());
                HisVehicleMo.setTotalCurrent(HisVeh.getTotalCurrent());
                Map<String, Object> map = JacksonUtil.transBean2Map(HisVehicleMo);
                Date collectTime = (Date)map.get("collectTime");
                if (collectTime != null ){
                    map.put("collectTime", DateUtil.dateToStr(collectTime,DateUtil.DATEFORMATYYYYMMDDHHmmss)) ;
                }
                Date reciveTime = (Date)map.get("reciveTime");
                if (reciveTime != null ){
                    map.put("reciveTime", DateUtil.dateToStr(reciveTime,DateUtil.DATEFORMATYYYYMMDDHHmmss)) ;
                }
                list.add(map);
            }
            ExcelInfo excelInfo = new ExcelInfo(fileName, sheetName, titlesobcVeh, fieldsobcVeh, list);
            File file = new File("E:\\autotemp");
            if (!file.exists() && !file.isDirectory()) {
                file.mkdir();
            }
            OutputStream outStream = new FileOutputStream(new File(file + "\\" + fileName));
            ExcelUtil.write(excelInfo, outStream);

        }


    }
























    @RequestMapping(value = "/exportoo", method = RequestMethod.GET)
    public void upload(@RequestParam Date startDay ,@RequestParam Date  endDay ,@RequestParam String vinCode) throws IOException{
        Client client = ESTools.getClient() ;
        System.out.print(startDay+"l1"+endDay+"l1"+vinCode);
        List<HisCountDataL2> hisCountDataL2s = getVehicleL2(startDay, endDay ,vinCode,client);
        List<HisCountData> HisCountData = getHisCountData( vinCode ,startDay, endDay,client);
        Long sizeNum = ObcDataService.getHisObcDataNum(vinCode, startDay, endDay, client);
        List<ObcData> hisObcData = ObcDataService.getHisObcData(vinCode, startDay, endDay, sizeNum,client);
        Long sizeNum1 = BmsDataService.getHisBmsDataNum(vinCode, startDay, endDay, client);
        List<BmsData> hisBmsData = BmsDataService.getHisBmsData(vinCode, startDay, endDay, sizeNum1, client);



        String fileName = "知豆汽车sss指标-" + DateUtil.getDateStr() + ".xls";

        String outFilePath = "E:\\autotemp\\"+fileName;
        OutputStream outStream = new FileOutputStream(new File(outFilePath));
        // 前缀prefix，后缀suffix
//		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String suffix = StringUtil.getSuffix(fileName);
        // 创建Workbook对象(excel的文档对象)
        Workbook workbook = null;
        if ("xls".equals(suffix)) {
            workbook = new HSSFWorkbook();
        } else if ("xlsx".equals(suffix)) {
            workbook = new XSSFWorkbook();
        }

        //具体内

        int sheetNum = 3; //指定sheet的页数
        String[] titles=null;
        String[] fields=null;
        List<Map<String, Object>> list1= null;
        List<Map<String, Object>> list2= null;
        List<Map<String, Object>> list3= null;
        List<Map<String, Object>> list4= null;
        Map<String, Object> map=null;
        Map<String, Object> map1=null;
        for (int i = 1; i <= sheetNum; i++) {
            // 建立新的sheet对象（excel的表单）
            Sheet sheet = workbook.createSheet("第" + i+"页");
//			// 设置缺省列宽8.5,行高为设置的20
//			sheet.setDefaultRowHeightInPoints(20);
            if(i==1){
                 titles = new String[]{"id", "数据时间", "VIN码", "车辆类型", "交车日期", "GPS数据条数","CAN数据条数","GPS历史数据条数","CAN历史数据条数","总里程","近似线性中段充电SOC - Model1","近似线性中段充电时间(单位：S) - Model1","近似线性中段充电SOC - Model2","近似线性中段充电时间(单位：S) - Model2","近似线性中段充电SOC - Model3","近似线性中段充电时间(单位：S) - Model3","放电总时长(单位：S)", "最大输入电功率", "最大输出电功率", "近似线性中段当日总放电SOC", "近似线性中段当日总行驶里程", "计算时间", "版本","里程","续驶里程","一次满电最少时间（单位：h） - Model1","一次满电最少时间（单位：h） - Model2", "一次满电最少时间（单位：h） - Model3","最大充电功率","平均单日运行时间","百公里耗电"};
                 fields = new String[]{"id", "tm", "vinCode", "carType", "veDeliveredDate", "gpsCount", "canCount","gpshisCount","canhisCount","mileageTotal","chargeMidSoc1","chargeMidSec1","chargeMidSoc2","chargeMidSec2","chargeMidSoc3","chargeMidSec3","dischargeTotalSec","maxInChargerPower","maxOutChargerPower","dischargeMidSoc","dischargeMidMileage","calcTime","version","mileage","limitMileage","maxEnergyTime1","maxEnergyTime2","maxEnergyTime3","maxElectricPower","avgDailyRunTime","hundredsKmusePower"};

                  for (HisCountDataL2 hisCountDataL2 : hisCountDataL2s) {
                      //改变导出的日期格式
                        map = JacksonUtil.transBean2Map(hisCountDataL2);
                        Date tm = (Date) map.get("tm");
                        map.put("tm",ZonedDateTimeUtil.dateToStr(tm)) ;
                        list1.add(map);
            }
            }
            if(i==2){
                 titles = new String[]{"id", "数据时间", "VIN码", "车辆类型", "交车日期", "GPS数据条数","CAN数据条数","总里程","近似线性中段充电SOC - Model1","近似线性中段充电时间(单位：S) - Model1","近似线性中段充电SOC - Model2","近似线性中段充电时间(单位：S) - Model2","近似线性中段充电SOC - Model3","近似线性中段充电时间(单位：S) - Model3","放电总时长(单位：S)", "最大输入电功率", "最大输出电功率", "近似线性中段当日总放电SOC", "近似线性中段当日总行驶里程", "计算时间", "版本"};
                 fields = new String[]{"id", "tm", "vinCode", "carType", "veDeliveredDate", "gpsCount", "canCount","mileageTotal","chargeMidSoc1","chargeMidSec1","chargeMidSoc2","chargeMidSec2","chargeMidSoc3","chargeMidSec3","dischargeTotalSec","maxInChargerPower","maxOutChargerPower","dischargeMidSoc","dischargeMidMileage","calcTime","version"};
                  for (HisCountData hisCountData : HisCountData) {
                        map = JacksonUtil.transBean2Map(hisCountData);
                        list2.add(map);
            }

            }
            if(i==3){
                titles = new String[]{"VIN", "采集时间", "接受时间", "GPRS号","电池组当前状态" };
                fields = new String[]{"vinCode", "collectTime", "reciveTime", "gprsCode","batteryGroupStatus"};
                for (BmsData bmsData : hisBmsData) {
                    BmsData Bms=new BmsData();
                    Bms.setVinCode(bmsData.getVinCode());
                    Bms.setCollectTime(bmsData.getCollectTime());
                    Bms.setReciveTime(bmsData.getReciveTime());
                    Bms.setGprsCode(bmsData.getGprsCode());
                    Bms.setBatteryGroupStatus(bmsData.getBatteryGroupStatus());
                    map = JacksonUtil.transBean2Map(Bms);
                    list3.add(map);
                }
            }

            Row header = sheet.createRow(0);
            header.setHeightInPoints(25);
            // 创建单元格并设置单元格内容
            for (int j = 0, max = titles.length; j < max; j++) {
                header.createCell(j).setCellValue(titles[j]);
            }


            Row row = sheet.createRow(2);
            row.setHeightInPoints(20);

            for (int j = 0, max = fields.length; j < max; j++) {
                if(i==1){
                   map1 = list1.get(0) ;
                }
                if(i==2){
                    map1 = list2.get(0) ;
                }
                if(i==3){
                    map1 = list3.get(0) ;
                }


                String titleValue = titles[j];
                String value = map1.get(fields[j]).toString() ;
                int valueLength = value.getBytes().length;
                int titleValueLength = titleValue.getBytes().length;
//				int cellLength = (valueLength >= titleValueLength) ? valueLength : titleValueLength;
                int cellLength = Math.max(valueLength, titleValueLength);

                //由于utf-8一个中文字符有3个长度
                sheet.setColumnWidth(j,(cellLength + 3) * 256);//手动设置列宽
                row.createCell(j).setCellValue(value);
//				Cell cell = row.createCell(j);
//				cell.setCellType(Cell.CELL_TYPE_STRING);
//				cell.setCellValue(value);
            }
        }
        workbook.write(outStream);
        outStream.close();
    }

}