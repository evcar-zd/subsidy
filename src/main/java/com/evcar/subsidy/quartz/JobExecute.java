package com.evcar.subsidy.quartz;

import com.evcar.subsidy.util.DateUtil;
import com.evcar.subsidy.util.TargetUtil;
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

    TargetUtil targetUtil ;
    @Autowired
    void setTargetUtil(TargetUtil targetUtil) { this.targetUtil = targetUtil;}

    @Scheduled(cron = "0 0 0 * * ?")
    public void execute(){
        targetUtil.targeCount() ;
        System.out.println("结束时间:"+ DateUtil.getDateStr());
    }

}
