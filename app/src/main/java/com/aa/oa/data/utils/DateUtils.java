package com.aa.oa.data.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 * 
 * @author numberone
 */
public class DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";
    public static String YYYYMMDD = "yyyyMMdd";
    public static String YYYYMM = "yyyyMM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String HH_MM_SS = "HH:mm:ss";
    
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }



    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }
    /**
     * 计算两个时间差,返回小时数
     * @param: @param endDate
     * @param: @param nowDate
     * @param: @return 参数说明
     * @return: String 返回类型
     * @throws
     */
    public static double getDateIntervalInHours(Date startDate, Date endDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        // 计算差多少小时
        double hour = (diff % nd)*1.0 / nh;
        // 计算差多少分钟
        return hour;
    }
    /**
     * 计算两个时间差,返回天数
     * @param: @param startDate
     * @param: @param endDate
     * @param: @return
     * @return: double
     */
    public static double getDateIntervalInDays(Date startDate, Date endDate)
    {
    	long nd = 1000 * 24 * 60 * 60;
    	// 获得两个时间的毫秒时间差异
    	long diff = endDate.getTime() - startDate.getTime();
    	// 计算差多少天
        double day = diff*1.0 / nd;
    	// 计算差多少分钟
    	return day;
    }

    /**
     * 获取周围时间
     * @param: @param date
     * @param: @param amount
     * @param: @return
     * @return: Date
     */
    public static Date getAroundDate(Date date,int amount){
    	if(date!=null){
    		Calendar calendar = initCalendar(date);
    		calendar.add(Calendar.DATE, amount);
    		return calendar.getTime();
    	}
    	return date;
    }
    
    /**
     * 获取周围时间 返回指定格式的日期
     * @param date
     * @param amount
     * @return
     */
    public static Date getAroundDate(Date date,int amount,String formatStr){
    	if(formatStr==null) return getAroundDate(date,amount);
    	return DateUtils.formatDateToDate(DateUtils.getAroundDate(date, amount), formatStr);
    }
    /**
     * 获取星期
     * @param: @param attendDate
     * @param: @return
     * @return: Integer
     */
	public static Integer dayOfWeek(Date attendDate) {
		Calendar calendar = initCalendar(attendDate);
		return Integer.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
	}
	
	public static Calendar initCalendar(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	/**
	 * 截取日期
	 * @param: @param date
	 * @param: @return
	 * @return: Date
	 */
	public static Date getOnlyDate(Date date){
		SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
		try {
			return format.parse(format.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 按指定格式解析日期 并返回解析后的日期
	 * @param: @param date
	 * @param: @param formatStr
	 * @param: @return
	 * @return: Date
	 */
	public static Date formatDateToDate(Date date,String formatStr){
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try {
			return format.parse(format.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取指定日期的指定时间
	 * @param: @param date
	 * @param: @param hourOfDay
	 * @param: @param minute
	 * @param: @param second
	 * @param: @return
	 * @return: Date
	 */
	public static Date getDateSomeTime(Date date, int hourOfDay, int minute, int second){
		Calendar calendar = initCalendar(date);
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute, second);
		return calendar.getTime();
	}
	
	/**
	 * 当前日期是周末
	 * @param: @param date
	 * @param: @return
	 * @return: Boolean
	 */
	public static Boolean isWeekend(Date date){
		Boolean isWeekend = false;
		Integer week = dayOfWeek(date);
		//星期六或者星期天
		if(week==7 || week==1){
			isWeekend = true;
		}
		return isWeekend;
	}
	
	/**
	 * 将一段日期区间拆分为有区间内每一天组成的map
	 * @param: @param startDate
	 * @param: @param endDate
	 * @param: @return
	 * @return: List<String>
	 */
	public static List<String> splitDate(Date startDate,Date endDate){
		//获取两个日期的间隔天数
		double intervalDays = getDateIntervalInDays(startDate, endDate);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i <= intervalDays; i++) {
			Date date = getAroundDate(startDate, i);
			list.add(parseDateToStr(YYYY_MM_DD, date));
		}
		
		return list;
	}
	
	/**
	 * 获取一个月的天数
	 * @param date
	 * @return
	 */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * 获取工作日天数
     * @return
     */
    @SuppressWarnings("deprecation")
	public static int getWorkDays(Date lastMonth) {
		// 计算指定月有多少工作日
		int workDays = 0;
		Calendar cal = Calendar.getInstance();
		lastMonth.setDate(1);
		cal.setTime(lastMonth);
		int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 0; i < days; i++) {
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
				workDays++;
			}
			cal.add(Calendar.DATE, 1);
		}
		return workDays;
	}
}
