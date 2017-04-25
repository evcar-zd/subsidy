package com.evcar.subsidy.controller;

import com.evcar.subsidy.entity.ESBean;
import com.evcar.subsidy.entity.MonthCountData;
import com.evcar.subsidy.service.HisCountDataService;
import com.evcar.subsidy.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        Date startDate = DateUtil.getDate(date,-esBean.getMonthDay()) ;
        List<MonthCountData> monthCountDatas = HisCountDataService.getMonthCountData(startDate,date) ;
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
    public List<MonthCountData> getTarget(){
        Date date = new Date() ;
        Date startDate = DateUtil.getDate(date,-esBean.getMonthDay()) ;
        Long months = HisCountDataService.getMonthCountDataNumber(startDate,date) ;
        List<MonthCountData> monthCountDatas = new ArrayList<>();
        if (months > 0){
            monthCountDatas = HisCountDataService.getMonthCountData(startDate,date,months) ;
        }
        return monthCountDatas ;
    }

}
