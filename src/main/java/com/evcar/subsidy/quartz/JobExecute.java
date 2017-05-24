package com.evcar.subsidy.quartz;

import com.evcar.subsidy.agg.Agg;
import com.evcar.subsidy.entity.ESBean;
import com.evcar.subsidy.entity.MonthCountData;
import com.evcar.subsidy.service.MonthCountDataService;
import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.ZonedDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * 每天凌晨计算
 * Created by Kong on 2017/4/21.
 */
@Component
@Configurable
@EnableScheduling
public class JobExecute {

    private static Logger s_logger = LoggerFactory.getLogger(JobExecute.class);

    private static ESBean esBean;
    @Autowired
    void setEsBean(ESBean esBean) { this.esBean = esBean;}

    /**
     * 每天凌晨执行任务
     * 执行每个任务等待5秒缓冲时间(否则会导致数据不全)
     */
//    @Scheduled(cron = "21 16 2 * * ?")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute(){
        s_logger.info("启动数据服务");
        try {
            Date lastDate = null ;
            List<MonthCountData> lastMonthCountData = MonthCountDataService.getLastMonthCountData() ;
            if (lastMonthCountData.size() > 0) {
                MonthCountData monthCountData = lastMonthCountData.get(0);
                lastDate = monthCountData.getTm() ;
            }
            int startDay = esBean.getStartDate() ;
            int endDay = esBean.getEndDate() ;
            int monthDay = esBean.getMonthDay() ;
            if (lastDate != null) {
                startDay = DateUtil.diffDate(lastDate,new Date()) ;
                /** 最多只计算三十天前的数据 */
                if (startDay > 30){
                    startDay = 30+startDay ;
                }
            }
            Agg agg = new Agg() ;
            agg.takeTarget(startDay,endDay,monthDay,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        s_logger.info("结束时间："+DateUtil.getDateStr());
    }

}
