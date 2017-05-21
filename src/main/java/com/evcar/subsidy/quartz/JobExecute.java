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

    private static ESBean esBean;
    @Autowired
    void setEsBean(ESBean esBean) { this.esBean = esBean;}

    /**
     * 每天凌晨执行任务
     * 执行每个任务等待5秒缓冲时间(否则会导致数据不全)
     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void execute(){
//        Agg agg = new Agg() ;
//        agg.takeTarget(esBean.getStartDate(),esBean.getEndDate(),esBean.getMonthDay(),null);
//        agg.takeAgg(esBean.getStartDate(),esBean.getEndDate(),null);

//        try {
//            Thread.sleep(5000);
//            agg.takeVehicleL2(esBean.getStartDate(),esBean.getEndDate(),esBean.getMonthDay(),null);
//            Thread.sleep(5000);
//            agg.takeVehicleL3(esBean.getStartDate(),esBean.getEndDate(),esBean.getMonthDay(),null);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        System.out.println("结束时间:"+ DateUtil.getDateStr());
//    }

}
