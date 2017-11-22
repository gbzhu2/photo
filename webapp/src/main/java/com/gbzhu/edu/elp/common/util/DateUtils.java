package com.gbzhu.edu.elp.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.gbzhu.edu.elp.common.exception.ELPSysException;

/**
 * 日期工具类
 * 
 * @author 金丙传
 * 
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

	/**
	 * 格式化日期
	 * @param date 日期
	 * @param pattern 格式化日期格式
	 * @return 日期字符串
	 */
	public static String format(Date date, String pattern) {
		if(StringUtils.isBlank(pattern)){
			throw new IllegalArgumentException("parameter pattern is required ");
		}
		if(date == null){
			throw new IllegalArgumentException("parameter date is required");
		}
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}
	
	/**
	 * 日期字符串转成日期
	 * @param date 日期字符串
	 * @param pattern 格式化日期格式
	 * @return 日期
	 */
	public static Date parse(String date, String pattern) {
		if(StringUtils.isBlank(pattern)){
			throw new IllegalArgumentException("parameter pattern is required ");
		}
		if(date == null){
			throw new IllegalArgumentException("parameter date is required");
		}
		DateFormat df = new SimpleDateFormat(pattern);
		try{
			return df.parse(date);
		} catch (ParseException e) {
			throw new ELPSysException(e.getMessage());
		}
	}
	
	/**
	 * 日期字符串转成日期
	 * @param date 日期字符串
	 * @return 日期
	 */
	public static Date parse(String date) {
		return parse(date,"yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 格式化日期
	 * @param calendar calendar
	 * @param pattern 格式化日期格式
	 * @return 日期字符串
	 */
	public static String format(Calendar calendar,String pattern){
		if(StringUtils.isBlank(pattern)){
			throw new IllegalArgumentException("parameter pattern is required");
		}
		if(calendar == null){
			throw new IllegalArgumentException("parameter calendar is required");
		}
		return format(calendar.getTime(),pattern);
	}
	
	/**
	 * 格式化当前日期
	 * @param pattern 格式化日期格式
	 * @return 日期字符串
	 */
	public static String formatCurrentDate(String pattern){
		if(StringUtils.isBlank(pattern)){
			throw new IllegalArgumentException("parameter pattern is required");
		}
		return format(new Date(),pattern);
	}
	
	/**
	 * 格式化默认日期格式 yyyy-MM-dd HH:mm:ss
	 * @param date 日期
	 * @return 日期字符串
	 */
	public static String formatDefaultPattern(Date date){
		if(date == null){
			throw new IllegalArgumentException("parameter calendar is required");
		}
		return format(date,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 格式化当前日期  yyyy-MM-dd HH:mm:ss
	 * @return 默认格式化日期字符串
	 */
	public static String formatCurrentDefaultPattern(){
		return format(new Date(),"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 时间戳转为date 对象
	 * @param timestamp 时间戳
	 * @return Date
	 */
	public static Date timestampToDate(String timestamp){
		if(StringUtils.isNumeric(timestamp)){
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.parseLong(timestamp));
			return calendar.getTime();
		}
		else{
			throw new IllegalStateException("timestamp 为数字字符串");
		}
	}
	
	/**
	 * 获取最近时间
	 * @param count  
	 * @return Date
	 */
	public static Date getLatestDay(int count){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -count);
		calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 时间转化为时间戳
	 * @param date 
	 * @return 时间戳
	 */
	public static Long dateToTimestamp(Date date){
		Assert.notNull(date);
		return date.getTime();
	}

    /**
     * 获取当天时间定制
     * @param hourOfDay 小时
     * @param minute 分钟
     * @param second 秒
     * @return 当天时间
     */
    public static Date getCustomDate(int hourOfDay,int minute,int second){
        Calendar currentDate =Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
        currentDate.set(Calendar.MINUTE,minute);
        currentDate.set(Calendar.SECOND,second);
        currentDate.set(Calendar.MILLISECOND, 0);
        return currentDate.getTime();
    }

    /**
     * 获取本周起始时间
     * @return 本周起始时间
     */
    public static Date getCurrentWeek(){
        Calendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取本月起始时间
     * @return 本月起始时间
     */
    public static Date getCurrentMonth(){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     *最近几个月
     *@param number 几个月
     *@return 时间
     */
    public static Date getLastMonth(int number){
    	Calendar calendar=Calendar.getInstance();
    	calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH,number*(-1));
        return calendar.getTime();
    }
    
    /**
     * 获取两个时间的相差多少分钟
     * @param begin begin
     * @param end end
     * @return 相差多少分钟
     */
    public static long between(Date begin, Date end) {
    	long beginL = begin.getTime();
    	long endL = end.getTime();
    	long result = (endL - beginL)/(1000*60);    	
    	return result;
    }
}
