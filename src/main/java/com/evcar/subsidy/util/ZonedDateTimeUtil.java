package com.evcar.subsidy.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static com.evcar.subsidy.util.DateUtil.parseStrToDate;

/**
 * Created by Kong on 2017/5/22.
 */
public class ZonedDateTimeUtil {

    public static Date getDate(Date date,Integer num){
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(defaultZoneId);
        zonedDateTime = zonedDateTime.plusDays(-num) ;
        zonedDateTime = ZonedDateTime.of(zonedDateTime.getYear(), zonedDateTime.getMonthValue(),
                zonedDateTime.getDayOfMonth() , 0, 0,
                0, 0, defaultZoneId) ;
        return Date.from(zonedDateTime.toInstant()) ;
    }

    /**
     * 获取某天的后几天的起始日期
     * @return
     */
    public static Date getStartDate(Date date,Integer num){
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(defaultZoneId);
        zonedDateTime = zonedDateTime.plusDays(num) ;
        zonedDateTime = ZonedDateTime.of(zonedDateTime.getYear(), zonedDateTime.getMonthValue(),
                zonedDateTime.getDayOfMonth(), 0, 0,
                0, 0, defaultZoneId) ;
        return Date.from(zonedDateTime.toInstant()) ;
    }

    /**
     * 日期格式化
     * @param date
     * @return
     */
    public static String dateToStr(Date date) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(defaultZoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") ;
        return zonedDateTime.format(formatter);
    }

}
