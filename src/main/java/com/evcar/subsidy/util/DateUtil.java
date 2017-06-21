package com.evcar.subsidy.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

	public static Date date = null;

	public static DateFormat dateFormat = null;

	public static Calendar calendar = null;

	public static String DATEFORMATYYYYMMDD = "yyyy-MM-dd" ;
	public static String DATEFORMATYYYYMMDDHHmmss = "yyyy-MM-dd HH:mm:ss" ;
	/**
	 * 功能描述：格式化日期
	 * 
	 * @param dateStr  字符型日期
	 * @param format   格式
	 * @return Date 日期
	 */
	public static Date parseDate(String dateStr, String format) {
		try {
			dateFormat = new SimpleDateFormat(format);
			String dt = dateStr.replaceAll("-", "/");
			if ((!dt.equals("")) && (dt.length() < format.length())) {
				dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]",
						"0");
			}

			date = dateFormat.parse(dt);
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	/**
	 * 功能描述：格式化日期
	 * @param dateStr    字符型日期：YYYY/MM/DD 格式
	 * @return Date 日期
	 */
	public static Date parseDate(String dateStr) {
		return parseDate(dateStr, "yyyy/MM/dd");
	}


	/**
	 * 功能描述：格式化日期
	 * @param dateStr 字符型日期：YYYY-MM-DD 格式
	 * @return Date 日期
	 */
	public static Date parseStrToDate(String dateStr) {
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = dateFormat.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	/**
	 * 功能描述：格式化输出日期
	 * @param date 日期
	 * @return 返回字符型日期
	 */
	public static String format(Date date) {
		String result = "";
		try {
			if (date != null) {
				dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				result = dateFormat.format(date);
			}
		} catch (Exception e) {
			return null ;
		}
		return result;
	}

	/**
	 * 功能描述：计算该日期的上个月日期
	 * @param date 日期
	 * @return 返回字符型日期
	 */
	public static String lastMonthDateFormat(Date date){
		Calendar calendar = Calendar.getInstance() ;
		calendar.setTime(date);
		calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH) - 1);
		return format(calendar.getTime()) ;
	}

	/**
	 * 获取两个时间的秒数
	 * @param startDate
	 * @param endDate
     * @return
     */
	public static long getSeconds(Date startDate ,Date endDate){
		long seconds = (endDate.getTime()-startDate.getTime())/1000;//除以1000是为了转换成秒
		return seconds ;
	}


	/**
	 * 将指定日期(Date)对象转换成 格式化字符串 (String)
	 *
	 * @param date	Date 日期对象
	 * @param datePattern	String 日期格式
	 * @return String
	 */
	public static String dateToStr(Date date, String datePattern) {
		SimpleDateFormat sd = new SimpleDateFormat(datePattern);
		return sd.format(date);
	}

	/**
	 * 获得当前时间的str格式，格式为yyyyMMddHHmmss
	 *
	 * @Title: todayStr
	 * @param @return    设定文件
	 * @return String    返回类型
	 */
	public static String getDateStr(){
		Date date = new Date();
		return dateToStr(date, "yyyyMMddHHmmss");
	}

	/**
	 * 获得当前时间的str格式，格式为yyyyMMdd
	 *
	 * @Title: todayStr
	 * @param @return    设定文件
	 * @return String    返回类型
	 */
	public static String getDateStryyyyMMdd(Date date){
		return dateToStr(date, "yyyyMMdd");
	}


	/**
	 * 获取某天的前几天的日期
	 * @return
	 */
	public static Date getDate(Date date,Integer num){
		Calendar calendar = Calendar.getInstance() ;
		calendar.setTime(date);
		calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) - num);
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = dft.format(calendar.getTime())+" 00:00:00";
		return parseStrToDate(startDate) ;
	}



	/**
	 * 获取某天的后几天的起始日期
	 * @return
	 */
	public static Date getStartDate(Date date,Integer num){
		Calendar calendar = Calendar.getInstance() ;
		calendar.setTime(date);
		calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) + num);
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = dft.format(calendar.getTime())+" 00:00:00";
		return parseStrToDate(startDate) ;
	}

	/**
	 * 获取当天的起始日期
	 * @return
	 */
	public static Date getStartDate(Date date){
		Calendar calendar = Calendar.getInstance() ;
		calendar.setTime(date);
		calendar.set(Calendar.DATE,calendar.get(Calendar.DATE));
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = dft.format(calendar.getTime())+" 00:00:00";
		return parseStrToDate(startDate) ;
	}

	/**
	 * 获取某天的后几天的结束日期
	 * @return
	 */
	public static Date getEndDate(Date date,Integer num){
		Calendar calendar = Calendar.getInstance() ;
		calendar.setTime(date);
		calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) + num);
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = dft.format(calendar.getTime())+" 23:59:59";
		return parseStrToDate(startDate) ;
	}

	/**
	 * 获取当天的结束日期
	 * @return
	 */
	public static Date getEndDate(Date date){
		Calendar calendar = Calendar.getInstance() ;
		calendar.setTime(date);
		calendar.set(Calendar.DATE,calendar.get(Calendar.DATE));
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = dft.format(calendar.getTime())+" 23:59:59";
		return parseStrToDate(startDate) ;
	}

	/**
	 * 功能描述：返回毫秒
	 * @param date 日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}


	/**
	 * 功能描述：日期相减
	 * @param date 日期
	 * @param date1 日期
	 * @return 返回相减后的天数
	 */
	public static int diffDate(Date date, Date date1) {
		int dateNum = (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
		return Math.abs(dateNum);
	}


	/**
	 * 功能描述：日期相减
	 * @param date 日期
	 * @param date1 日期
	 * @return 返回相减后的天数
	 */
	public static boolean compare(Date date, Date date1) {
		int dateNum = (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
		return dateNum > 0 ? true : false ;
	}


	/**
	 * 格式化日期：
	 * @param str
	 * @return YYYY-MM—DD
     */
	private static Date formatDate(String str){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMATYYYYMMDD) ;
		Date date ;
		try {
			date = dateFormat.parse(str) ;
		} catch (ParseException e) {
			date = null ;
		}
		return date ;
	}


}
