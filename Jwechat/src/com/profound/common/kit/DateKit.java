package com.profound.common.kit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.log4j.Logger;

public class DateKit {
	private static Logger logger = Logger.getLogger(DateKit.class);
	private static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";//日期
	private static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";//时间
	private static String DEFAULT_TIME_FORMAT_STRING = "yyyyMMddHHmmss";//时间
	
	private static SimpleDateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat(DEFAULT_DATE_FORMAT);//日期
	private static SimpleDateFormat DEFAULT_TIME_FORMATTER = new SimpleDateFormat(DEFAULT_TIME_FORMAT);//时间
	private static SimpleDateFormat DEFAULT_TIME_FORMATTER_STRING = new SimpleDateFormat(DEFAULT_TIME_FORMAT_STRING);//时间

	/**
	 * 格式化日期类型
	 * @param date
	 * @return String （yyyy-MM-dd）
	 */
	public static String formatDate(Date d) {
		return DEFAULT_DATE_FORMATTER.format(d);
	}
	/**
	 * 格式化日期类型,参数为时间的整型表示
	 * @return String （yyyy-MM-dd）
	 */
	public static String formatDate(long d) {
		return DEFAULT_DATE_FORMATTER.format(d);
	}
	/**
	 * 格式化日期类型,若date为空则采用当前时间
	 * @param date 需要格式日期
	 * @return String 格式化后的日期字符串
	 */
	public static String formatDate(Date d,String fmt) {
		SimpleDateFormat simpleDateFormat = fmt==null?DEFAULT_TIME_FORMATTER:new SimpleDateFormat(fmt);
		return simpleDateFormat.format(d==null?new Date():d);
	}
	/**
	 * 格式化日期类型
	 * @param date 需要格式日期
	 * @return String 格式化后的日期字符串
	 */
	public static String formatDate(long d,String fmt) {
		SimpleDateFormat simpleDateFormat = fmt==null?DEFAULT_TIME_FORMATTER:new SimpleDateFormat(fmt);
		return simpleDateFormat.format(d);
	}
	/**
	 * 格式化长日期类型 带时分秒
	 * @param date
	 * @return String （yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date d) {
		return DEFAULT_TIME_FORMATTER.format(d);
	}
	/**
	 * 格式化长日期类型 带时分秒,参数为时间的整型表示
	 * @return String （yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(long d) {
		return DEFAULT_TIME_FORMATTER.format(d);
	}
	/**
	 * 格式化长日期类型 带时分秒
	 * @param date
	 * @return String （yyyyMMddHHmmss）
	 */
	public static String formatDateTimeString(Date d) {
		return DEFAULT_TIME_FORMATTER_STRING.format(d);
	}

	/**
	 * 字符串转换成日期对象
	 * @param str
	 * @return date （yyyy-MM-dd）
	 */
	public static Date getDate(String str) {
		try {
			return DEFAULT_DATE_FORMATTER.parse(str);
		} catch (ParseException e) {
			logger.error("字符串转换日期格式异常：" + str);
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 字符串转换成日期时间对象
	 * @param str 
	 * @return date（yyyy-MM-dd HH:mm:ss）
	 */
	public static Date getTime(String str) {
		try {
			return DEFAULT_TIME_FORMATTER.parse(str);
		} catch (ParseException e) {
			logger.error("字符串转换日期格式异常：" + str);
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 根据格式化信息得到日期对象
	 * @param str 需要转换的字符串
	 * @param parse 需要转换成的格式
	 * @return date
	 */
	public static Date getDate(String str, String parse) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(parse);
		try {
			return simpleDateFormat.parse(str);
		} catch (ParseException e) {
			logger.error("字符串转换日期格式异常：需要格式化的字符串=" + str + "，转化后的格式=" + parse);
			logger.error(e.getMessage());
			return null;
		}

	}
	/**
	 * 采用给定的格式化方式返回当前日期的字符串表示,
	 * 若dateFormat为空则返回yyyy-MM-dd HH:mm:ss表示的时间
	 * @param dateFormat 格式化字符串(如:yyyy-MM-dd)
	 * @return 格式化后的当前日期字符串
	 */
	public static String getTodayDate(String dateFormat)
	{
		if(StringKit.notBlank(dateFormat))
			return new SimpleDateFormat(dateFormat).format(new Date());
		else
			return getStringDate();
	}
	/**
	 * 获取当前日期对象,不包含时分秒
	 * @return
	 */
	public static Date getTodayDateNoSFM()
	{
		try {
			return DEFAULT_DATE_FORMATTER.parse(DEFAULT_DATE_FORMATTER.format(new Date()));
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 获取现在时间
	 * @return 返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		return DEFAULT_TIME_FORMATTER.format(new Date());
	}

	/**
	 * 获取现在时间
	 * @return 返回短时间字符串格式 yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		return DEFAULT_DATE_FORMATTER.format(new Date());
	}

	/**
	 * 当前时间 推移几天的日期
	 * @param day
	 *            需要推移的天数，正表示前移，负表示后移
	 * @return date
	 */
	public static Date getLastDate(long day) {
		Date date = new Date();
		long date_3_hm = date.getTime() - 3600000 * 24 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		return date_3_hm_date;
	}
	
	/**
	 *指定日期 推移几天的日期
	 * @param day
	 *            需要推移的天数，正表示前移，负表示后移
	 * @return date
	 */
	public static Date getLastDate(Date date,long day) {
		long date_3_hm = date.getTime() - 3600000 * 24 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		return date_3_hm_date;
	}

	/**
	 * 判断是否润年
	 * @param date
	 * @return true:是润年  false：不是润年
	 */
	public static boolean isLeapYear(String ddate) {

		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
		 * 3.能被4整除同时能被100整除则不是闰年
		 */
		Date d = getDate(ddate);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		int year = gc.get(Calendar.YEAR);
		if ((year % 400) == 0)
			return true;
		else if ((year % 4) == 0) {
			if ((year % 100) == 0)
				return false;
			else
				return true;
		} else
			return false;
	}

	/**
	 * 产生周序列,即得到当前时间所在的年度是第几周
	 * @return String
	 */
	public static String getSeqWeek() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1)
			week = "0" + week;
		return week;
	}

	/**
	 * 根据传入的日期格式的字符串，判断是本年度的第几个周
	 * @param date
	 * @return string 
	 */
	public static String getSeqWeek(String date) {
		Date d = getDate(date);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		String week = Integer.toString(gc.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1)
			week = "0" + week;
		return week;
	}

}
