package com.evcar.subsidy.controller;

import com.evcar.subsidy.TargetVo;
import com.evcar.subsidy.agg.Agg;
import com.evcar.subsidy.entity.ESBean;
import com.evcar.subsidy.entity.MonthCountData;
import com.evcar.subsidy.service.HisCountDataService;
import com.evcar.subsidy.service.MonthCountDataService;
import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.StringUtil;
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
        Date date = new Date() ;
        Date startDate = DateUtil.getDate(date,esBean.getMonthDay()) ;
        List<MonthCountData> monthCountDatas = MonthCountDataService.getMonthCountData(startDate,date) ;
        if (monthCountDatas.size() > 0){
            return monthCountDatas.get(0) ;
        }
        return new MonthCountData() ;
    }

    /**
     * 近期数据指标
     * @return
     */
    @RequestMapping(value = "/getTarget" ,method = RequestMethod.GET)
    public List<MonthCountData> getTarget(HttpServletRequest request){
        Date date = new Date() ;
        Date startDate = DateUtil.getDate(date,esBean.getMonthDay()) ;
        Long months = MonthCountDataService.getMonthCountDataNumber(startDate,date) ;
        List<MonthCountData> monthCountDatas = new ArrayList<>();
        if (months > 0){
            monthCountDatas = MonthCountDataService.getMonthCountData(startDate,date) ;
        }
        return monthCountDatas ;
    }


    @RequestMapping(value = "/getTargetParams" ,method = RequestMethod.GET)
    public List<TargetVo> getTargetParams(HttpServletRequest request){
        String target = request.getParameter("target") ;
        Date date = new Date() ;
        Date startDate = DateUtil.getDate(date,esBean.getMonthDay()) ;
        Long months = MonthCountDataService.getMonthCountDataNumber(startDate,date) ;
        List<MonthCountData> monthCountDatas = new ArrayList<>();
        if (months > 0){
            monthCountDatas = MonthCountDataService.getMonthCountData(startDate,date) ;
        }

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
            targetVo.setCountDate(monthCountData.getCalcTime());
            targetVos.add(targetVo);
        }
        return targetVos ;
    }

    /** 避免重复请求 */
    public static Boolean REPEAT_REQUEST = true ;

    /**
     * 手动计算
     * @param startDate 为空。默认
     * @param endDate   为空。默认
     * @param monthDay
     * @param sign
     * @param clearIndex
     */
    @RequestMapping(value = "/handCount" , method = RequestMethod.GET)
    public void HandCount(@RequestParam(value = "startDate" , required = false) String startDate,
                          @RequestParam(value = "endDate" , required = false) String endDate,
                          @RequestParam(value = "monthDay" , required = false) Integer monthDay,
                          @RequestParam(value = "v" , required = false) String sign,
                          @RequestParam(value = "clearIndex" ,required = false) String clearIndex){
        try {
            String dateStr = DateUtil.getDateStryyyyMMdd(new Date()) ;

            if (REPEAT_REQUEST){
                REPEAT_REQUEST = false ;

                /** 是否删除全部索引重新建立数据 */
                String deleteIndexL1 = "deleteL1" + dateStr ;
                String deleteIndexL2 = "deleteL2" + dateStr ;
                String deleteIndexAll = "deleteAll" + dateStr ;
                if (clearIndex.equals(deleteIndexL1)){
                    HisCountDataService.deleteByIndex();
                }else if(clearIndex.equals(deleteIndexL2)){
                    HisCountDataService.deleteHisCountDataL2();
                }else if(clearIndex.equals(deleteIndexAll)){
                    HisCountDataService.deleteByIndex();
                    HisCountDataService.deleteHisCountDataL2();
                    MonthCountDataService.deleteByIndex();
                }

                if (sign.equals(dateStr)){
                    int startDay = esBean.getStartDate() ;
                    int endDay = esBean.getEndDate() ;
                    if (!StringUtil.isEmpty(startDate)){
                        startDay = DateUtil.diffDate(DateUtil.parseDate(startDate),new Date());
                        if (!StringUtil.isEmpty(endDate)){
                            endDay = DateUtil.diffDate(DateUtil.parseDate(endDate),new Date());
                        }else{
                            endDay = startDay + 1 ;
                        }
                    }

                    if (monthDay == null || monthDay > 0 ) monthDay = esBean.getMonthDay() ;

                    List<String> vinCodes = new ArrayList<>() ;
//                    vinCodes.add("LJU70W1Z1GG082321") ;
//                    vinCodes.add("LJU70W1ZXFG076113") ;
//                    vinCodes.add("LJU70W1Z7FG070348") ;
//                    vinCodes.add("LJU70W1ZXFG073499") ;
//                    vinCodes.add("LJU70W1Z0FG076637") ;
                    Agg agg = new Agg() ;
                    agg.takeAgg(startDay,endDay,vinCodes);

                    if (monthDay != null ){
                        Thread.sleep(2000);
                        agg.takeVehicleL2(startDay,endDay,monthDay,vinCodes);

                        Thread.sleep(3000);
                        agg.takeVehicleL3(startDay,endDay,monthDay,vinCodes);
                    }
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            REPEAT_REQUEST = true ;
        }
    }

}
