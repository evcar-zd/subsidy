package com.evcar.subsidy;

import com.evcar.subsidy.entity.ESBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lyg on 2017/5/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 指定spring-boot的启动类
@SpringBootTest(classes=Application.class)
//@SpringApplicationConfiguration(classes = Application.class)// 1.4.0 前版本  注意启动类不要搞错了
public class test {

    @Autowired
     private ESBean bean;


    @Test
    public void propsTest() throws JsonProcessingException {
        System.out.println( bean.getClusterName());
        System.out.println( bean.getClientTransportSniff());
        System.out.println( bean.getEndDate());
        System.out.println( bean.getHost());
        System.out.println( bean.getLinearMileage());
        System.out.println( bean.getLinearSoc());
        System.out.println( bean.getMonthDay());
        System.out.println( bean.getPort());
        System.out.println( bean.getStartDate()+"+++++++++++++++++++++++++++");
        System.out.println( bean.getTarget().get("SMA7001BEV18").getAvgDailyRunTimeMax());
    }
}
