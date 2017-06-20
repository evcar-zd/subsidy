package com.evcar.subsidy.controller;

import com.evcar.subsidy.TargetVo;
import com.evcar.subsidy.agg.Agg;
import com.evcar.subsidy.agg.SegmentResult;
import com.evcar.subsidy.agg.VehicleL3;
import com.evcar.subsidy.entity.ESBean;
import com.evcar.subsidy.entity.HisCountDataL2;
import com.evcar.subsidy.entity.MonthCountData;
import com.evcar.subsidy.service.HisCountDataService;
import com.evcar.subsidy.service.MonthCountDataService;
import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.StringUtil;
import com.evcar.subsidy.util.ZonedDateTimeUtil;
import com.evcar.subsidy.vo.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.evcar.subsidy.service.MonthCountDataService.getLastMonthCountData;

/**
 * Created by Kong on 2017/4/24.
 */
@RestController
@RequestMapping("/api")
public class TargetController {

    private static Logger s_logger = LoggerFactory.getLogger(TargetController.class);

    private static ESBean esBean;
    @Autowired
    void setEsBean(ESBean value) { this.esBean = value;}


    /**
     * 最新一条数据指标
     * @return
     */
    @RequestMapping(value = "/getLastTarget" ,method = RequestMethod.GET)
    public MonthCountData getLastTarget(){
        List<MonthCountData> monthCountDatas = getLastMonthCountData();
        if (monthCountDatas.size() > 0){
            return monthCountDatas.get(0) ;
        }
        return new MonthCountData() ;
    }

    /**
     * 近期数据指标
     * 近30天数据
     * @return
     */
    @RequestMapping(value = "/getTarget" ,method = RequestMethod.GET)
    public List<MonthCountData> getTarget(HttpServletRequest request){
        List<MonthCountData> monthCountDatas = MonthCountDataService.getMonthCountData() ;
        return monthCountDatas ;
    }


    @RequestMapping(value = "/getTargetParams" ,method = RequestMethod.GET)
    public List<TargetVo> getTargetParams(HttpServletRequest request){
        String target = request.getParameter("target") ;
        List<MonthCountData> monthCountDatas = MonthCountDataService.getMonthCountData() ;

        List<TargetVo> targetVos = new ArrayList<>() ;
        for (MonthCountData monthCountData : monthCountDatas){
            Integer vehicleNum = monthCountData.getVehicleNum() ;
            BigDecimal targetNum = BigDecimal.ZERO;
            TargetVo targetVo = new TargetVo() ;
            if("totalCount".equals(target)){            //车辆总数
                targetNum = BigDecimal.valueOf(vehicleNum) ;
            }else if("totalMileage".equals(target)){    //累计行驶里程(km)
                targetNum = BigDecimal.valueOf(monthCountData.getMileage().getNormal()*100).divide(BigDecimal.valueOf(vehicleNum),2,BigDecimal.ROUND_UP);
            }else if("limitMileage".equals(target)){    //续驶里程
                targetNum = BigDecimal.valueOf(monthCountData.getLimitMileage().getNormal()*100).divide(BigDecimal.valueOf(vehicleNum),2,BigDecimal.ROUND_UP);
            }else if("maxEnergyTime".equals(target)){   //一次充满电所用最少时间
                targetNum = BigDecimal.valueOf(monthCountData.getMaxEnergyTime().getNormal()*100).divide(BigDecimal.valueOf(vehicleNum),2,BigDecimal.ROUND_UP);
            }else if("maxElectricPower".equals(target)){    //最大充电功率
                targetNum = BigDecimal.valueOf(monthCountData.getMaxElectricPower().getNormal()*100).divide(BigDecimal.valueOf(vehicleNum),2,BigDecimal.ROUND_UP);
            }else if("avgDailyRunTime".equals(target)){     //平均单日运行时间
                targetNum = BigDecimal.valueOf(monthCountData.getAvgDailyRunTime().getNormal()*100).divide(BigDecimal.valueOf(vehicleNum),2,BigDecimal.ROUND_UP);
            }else if("hundredsKmusePower".equals(target)){  //百公里耗电
                targetNum = BigDecimal.valueOf(monthCountData.getHundredsKmusePower().getNormal()*100).divide(BigDecimal.valueOf(vehicleNum),2,BigDecimal.ROUND_UP);
            }
            targetVo.setTargetNum(targetNum);
            targetVo.setCalcTime(monthCountData.getTm().getTime());
            targetVos.add(targetVo);
        }
        return targetVos ;
    }

    private static Integer MAX_QUERY_SIZE = 500 ;

    @RequestMapping(value = "/getTargetSteps" ,method = RequestMethod.GET)
    public List<SegmentResult> getTargetSteps(HttpServletRequest request){
        String target = request.getParameter("target") ;

        Map<String,List<SegmentResult>> map = VehicleL3.map ;

        List<SegmentResult> segmentResults = map.get(target) ;
        if (segmentResults == null){
            List<MonthCountData> lastMonthCountData =  MonthCountDataService.getLastMonthCountData() ;
            if (lastMonthCountData.size() > 0){
                MonthCountData monthCountData = lastMonthCountData.get(0) ;
                Date tm = monthCountData.getTm() ;
                Date startDate = ZonedDateTimeUtil.getStartDate(tm,0);
                Date endDate = ZonedDateTimeUtil.getEndDate(tm,0);
                List<HisCountDataL2> hisCountDataL2s = new ArrayList<>() ;
                Long size = HisCountDataService.getHisCountDataNumberL2(startDate,endDate);
                Integer num = Integer.valueOf(String.valueOf(size)) ;
                int groupNum = num/MAX_QUERY_SIZE ;
                groupNum = num%MAX_QUERY_SIZE > 0 ? groupNum+1 : groupNum ;
                Integer currentPage = 1 ;
                Integer pageSize = MAX_QUERY_SIZE ;
                for (int j = 0 ; j < groupNum ; j++ ) {
                    List<HisCountDataL2> result = HisCountDataService.getHisCountDataL2(startDate,endDate,currentPage,pageSize) ;
                    hisCountDataL2s.addAll(result) ;
                    currentPage ++ ;
                }

                if (hisCountDataL2s.size() > 0){
                    VehicleL3.getTargetStep(hisCountDataL2s) ;
                    segmentResults = VehicleL3.map.get(target) ;
                }
            }
        }
        if (segmentResults == null){
            segmentResults = new ArrayList<>() ;
        }

        return segmentResults ;
    }


    /** 避免重复请求 */
    public static Boolean REPEAT_REQUEST = true ;

    /**
     * 手动计算
     * @param startDate 为空。默认
     * @param endDate   为空。默认
     * @param monthDay
     * @param sign
     */
    @RequestMapping(value = "/handCount" , method = RequestMethod.GET)
    public void HandCount(@RequestParam(value = "startDate" , required = false) String startDate,
                          @RequestParam(value = "endDate" , required = false) String endDate,
                          @RequestParam(value = "monthDay" , required = false) Integer monthDay,
                          @RequestParam(value = "v" , required = false) String sign){
        try {
            String dateStr = DateUtil.getDateStryyyyMMdd(new Date()) ;

            if (REPEAT_REQUEST){
                REPEAT_REQUEST = false ;

                System.out.println("启动数据服务");
                long startRun = System.currentTimeMillis() ;
                if (sign.equals(dateStr)){
                    int startDay = esBean.getStartDate() ;
                    int endDay = esBean.getEndDate() ;
                    if (!StringUtil.isEmpty(startDate)){
                        startDay = DateUtil.diffDate(DateUtil.parseDate(startDate),new Date());
                        if (!StringUtil.isEmpty(endDate)){
                            endDay = DateUtil.diffDate(DateUtil.parseDate(endDate),new Date())-1;
                        }else{
                            endDay = startDay;
                        }
                    }

                    if (monthDay == null || monthDay > 0 ) monthDay = esBean.getMonthDay() ;

                    List<String> vinCodes = new ArrayList<>() ;

//                    vinCodes.add("LJU70W1ZXGG030024") ;
//                    vinCodes.add("LJU70W1Z3GG083289") ;
//                    vinCodes.add("LJU70W1Z2GG100275") ;
//                    vinCodes.add("LJU70W1Z0GG083122") ;
//                    vinCodes.add("LB370X1Z5GJ053159") ;
//                    vinCodes.add("LJU70W1ZXGG082785") ;
//                    vinCodes.add("LB370X1Z6GJ051887") ;
//                    vinCodes.add("LJU70W1Z1GG100459") ;
//                    vinCodes.add("LJU70W1Z1GG083405") ;
//                    vinCodes.add("LJU70W1Z5GG083603") ;

                    Agg agg = new Agg() ;
                    agg.takeTarget(startDay,endDay,monthDay,vinCodes);
                }
                System.out.println("运行时长："+(System.currentTimeMillis() - startRun) );
                REPEAT_REQUEST = true ;
            }
        } catch (Exception e) {
            REPEAT_REQUEST = true ;
            e.printStackTrace();
        }
    }


    /**
     * 检查是否是离线模式
     * @return
     */
    @RequestMapping(value = "/getOfflineMode" , method = RequestMethod.GET)
    public Boolean getOfflineMode(){
        Boolean offlineMode = esBean.getOfflineMode() ;
        if (offlineMode == null)offlineMode = false ;
        return offlineMode ;
    }

    /**
     * 车辆信息
     * @return
     */
    @RequestMapping(value = "/getVechicleInfo",method = RequestMethod.GET)
    public PageResult getVechicleInfo(@RequestParam(value = "tm",required = false)String tm,
                                      @RequestParam(value = "mark",required = false)Integer mark,
                                      @RequestParam(value = "target",required = false)String target,
                                      @RequestParam(value = "vinCode",required = false)String vinCode,
                                      @RequestParam(value = "currentPage",required = false)Integer currentPage,
                                      @RequestParam(value = "pageSize",required = false)Integer pageSize){

        if (StringUtil.isEmpty(tm) || StringUtil.isEmpty(target) || mark == null){
            return new PageResult();
        }

        currentPage = currentPage == null ? 1 : currentPage ;
        pageSize = pageSize == null ? 10 : pageSize ;

        PageResult pageResult = new PageResult() ;
        try{
            Long timetimeStamp = Long.valueOf(tm) ;
            Date date = new Date(timetimeStamp) ;
            pageResult = HisCountDataService.getLastHisCountDataL2(date,target, mark, vinCode, currentPage , pageSize) ;
        }catch (Exception e){
            s_logger.error("日期转换错误");
        }

//        long total = 32L ;
//        List<VehicleVo> voList = new ArrayList<>() ;
//        for (int i=0 ; i<10;i++){
//            VehicleVo vehicleVo = new VehicleVo();
//            Integer num = MathUtil.randomMax10(6);
//            String id = "ZD"+num ;
//            String carType = "type"+num ;
//            String vinCode = "Vin"+num ;
//            vehicleVo.setId(id);
//            vehicleVo.setCarType(carType);
//            vehicleVo.setVinCode(vinCode);
//            vehicleVo.setTm(new Date());
//            vehicleVo.setReleaseTime(new Date());
//            vehicleVo.setVeDeliveredDate(new Date());
//            voList.add(vehicleVo) ;
//        }
//        pageResult = new PageResult(voList,currentPage,total) ;

        return  pageResult ;
    }
}
