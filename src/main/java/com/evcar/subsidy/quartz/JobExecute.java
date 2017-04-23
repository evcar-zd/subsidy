package com.evcar.subsidy.quartz;

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

    @Scheduled(cron = "0 0 0 * * ?")
    public void execute(){

        System.out.println("定时输出");
    }


}
