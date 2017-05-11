package com.evcar.subsidy.quartz;

import com.evcar.subsidy.agg.Agg;
import com.evcar.subsidy.entity.ESBean;
import com.evcar.subsidy.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 每天凌晨计算
 * Created by Kong on 2017/4/21.
 */
@Component
@Configurable
@EnableScheduling
public class JobExecute {

//    TargetUtil targetUtil ;
//    @Autowired
//    void setTargetUtil(TargetUtil targetUtil) { this.targetUtil = targetUtil;}

    private static ESBean esBean;
    @Autowired
    void setEsBean(ESBean esBean) { this.esBean = esBean;}

    @Scheduled(cron = "0 0 0 * * ?")
    public void execute(){
        Agg agg = new Agg() ;
        agg.takeAgg(esBean.getStartDate(),esBean.getEndDate(),null);

//        targetUtil.targeCount(esBean.getStartDate(),esBean.getEndDate(),null) ;
//        targetUtil.countMonthData(esBean.getMonthDay(),Math.abs(esBean.getStartDate())) ;
        System.out.println("结束时间:"+ DateUtil.getDateStr());
    }

}
