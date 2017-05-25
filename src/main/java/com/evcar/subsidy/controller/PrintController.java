package com.evcar.subsidy.controller;

import com.evcar.subsidy.entity.BmsData;
import com.evcar.subsidy.entity.HisCountData;
import com.evcar.subsidy.entity.HisCountDataL2;
import com.evcar.subsidy.entity.ObcData;
import com.evcar.subsidy.poi.ExcelInfo;
import com.evcar.subsidy.poi.ExcelUtil;

import com.evcar.subsidy.service.BmsDataService;
import com.evcar.subsidy.service.HisCountDataService;
import com.evcar.subsidy.service.ObcDataService;
import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.ESTools;
import com.evcar.subsidy.util.JacksonUtil;
import com.evcar.subsidy.util.StringUtil;
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
        List<HisCountDataL2> hisCountDatas = HisCountDataService.getVehicleL2(startDay, endDate ,vinCode);
        if (hisCountDatas.size() > 0) {
            String fileName = "知豆汽车指标-" + DateUtil.getDateStr() + ".xls";
            String sheetName = "知豆汽车";
            String[] titles = new String[]{"id", "数据时间", "VIN码", "车辆类型", "交车日期", "GPS数据条数", "CAN数据条数", "总里程", "近似线性中段充电SOC", "近似线性中段充电时间(单位：S)", "放电总时长(单位：S)", "最大输入电功率", "最大输出电功率", "近似线性中段当日总放电SOC", "近似线性中段当日总行驶里程", "计算时间", "版本"};
            String[] fields = new String[]{"id", "tm", "vinCode", "carType", "veDeliveredDate", "gpsCount", "canCount", "mileageTotal", "chargeMidSoc", "chargeMidSec", "dischargeTotalSec", "maxInChargerPower", "maxOutChargerPower", "dischargeMidSoc", "dischargeMidMileage", "calcTime", "version"};
            List<Map<String, Object>> list = new ArrayList<>();
            for (HisCountDataL2 hisCountData : hisCountDatas) {
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


//    @RequestMapping(value = "/exportoo", method = RequestMethod.GET)
//    public void printVehicleL3() throws IOException{
//        String fileName = "知豆汽车指标-"+ DateUtil.getDateStr()+".xls";
//        String sheetName = "知豆汽车";
//        String[] titles = new String[]{"vinCode","里程","VIN码","车辆类型","近似线性中段当日总行驶里程","计算时间","版本"};
//        String[] fields = new String[]{"vinCode","mileage","limitMileage","maxEnergyTime","maxElectricPower","avgDailyRunTime","hundredsKmusePower"};
//        List<Map<String, Object>> list=new ArrayList<>();
//        String json="";
//        List<Export> ExportList = JSON.parseArray(json,Export.class);
//        for(Export export:ExportList){
//            Map<String, Object> map = JacksonUtil.transBean2Map(export);
//            list.add(map);
//        }
//        ExcelInfo excelInfo=new ExcelInfo(fileName,sheetName,titles,fields,list);
//        File file =new File("E:\\autotemp") ;
//        if(!file .exists()  && !file .isDirectory()){
//            file.mkdir();
//        }
//        OutputStream outStream = new FileOutputStream(new File(file+"\\"+fileName));
//        ExcelUtil.write2Page(excelInfo, outStream);
//
//    }

    @RequestMapping(value = "/exportoo", method = RequestMethod.GET)
    public void upload(@RequestParam Date startDay ,@RequestParam Date  endDay ,@RequestParam String vinCode) throws IOException{
        Client client = ESTools.getClient() ;
        System.out.print(startDay+"l1"+endDay+"l1"+vinCode);
        List<HisCountDataL2> hisCountDataL2s = HisCountDataService.getVehicleL2(startDay, endDay ,vinCode);
        List<HisCountData> HisCountData = HisCountDataService.getHisCountData( vinCode ,startDay, endDay,client);
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
                        map = JacksonUtil.transBean2Map(hisCountDataL2);
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