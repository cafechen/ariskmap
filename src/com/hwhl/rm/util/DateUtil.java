package com.hwhl.rm.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.TextUtils;

/** 日期工具类 */
public class DateUtil {
	final String logFlag = "DateUtil";

	/**
	 * 格式化传入时间字符串，返回Date对象
	 * 
	 * @param dateStr
	 *            要格式化的日期字符串
	 * @param format
	 *            格式，传入NULL默认为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date getDate(String dateStr, String format) {
		if (TextUtils.isEmpty(dateStr)) {
			return null;
		}
		if (TextUtils.isEmpty(format)) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 格式化传入时间字符串，返回Date对象
	 * 
	 * @param dateStr
	 *            要格式化的日期字符串
	 * @param format
	 *            格式，传入NULL默认为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date getFormatDate(Date date, String format) {
		if (date == null) {
			date = new Date();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 格式化传入时间字符串，返回Date对象
	 * 
	 * @param dateStr
	 *            要格式化的日期字符串（默认为yyyy-MM-dd HH:mm:ss）
	 * @return
	 */
	public static Date getDate(String dateStr) {
		return getDate(dateStr, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化传入时间字符串，返回Date对象
	 * 
	 * @param dateStr
	 *            要格式化的日期字符串（默认为yyyy-MM-dd）
	 * @return
	 */
	public static Date getDateYmd(String dateStr) {
		return getDate(dateStr, "yyyy-MM-dd");
	}

	/**
	 * 格式化系统当前时间
	 * 
	 * @param format
	 * @return
	 */
	public static Date getDateNow(String format) {
		String str = null;
		if (format == null) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		str = sdf.format(new Date());
		return getDate(str, format);
	}
	
	/**
	 * 格式化系统当前时间
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateName() {
		String format = "yyyy-MM-ddHH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	/**
	 * 格式化系统当前时间
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateStr(String format) {
		String str = null;
		if (format == null) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		str = sdf.format(new Date());
		return str;
	}

	/**
	 * 格式化时间
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateStr(String dateStr, String format) {
		// System.out.println(dateStr);
		if (StrUtil.nullToStr(dateStr).equals("")) {
			return null;
		}
		String str = null;
		if (format == null) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			str = sdf.format(sdf.parse(dateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 得到传入日期的年月日时分秒
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateStrYmdHms(Date date) {
		String str = null;
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		str = sdf.format(date);
		return str;
	}

	/**
	 * 得到传入日期的年月日时分秒
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateStr(Date date, String format) {
		String str = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			str = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 得到两个时间 相差小时数
	 */
	public static double getDyscXs(Date starttime, Date endtime) {
		double dysc = 0.0;
		try {
			double tt = (endtime.getTime() - starttime.getTime()) / 1000.00 / 60 / 60;
			BigDecimal b = new BigDecimal(tt);
			dysc = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dysc;
	}

	/**
	 * 得到两个时间 相差分钟数
	 */
	public static int getDyscFz(Date starttime, Date endtime) {
		int dysc = 0;
		try {
			double tt = (endtime.getTime() - starttime.getTime()) / 1000.00 / 60;
			BigDecimal b = new BigDecimal(tt);
			dysc = b.setScale(1, BigDecimal.ROUND_HALF_UP).intValue();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dysc;
	}

	/**
	 * 获取当前日期是星期几
	 * 
	 * @return
	 */
	public static int week() {
		Calendar aCalendar = Calendar.getInstance();
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		int y = x - 1;
		if (y == 0)
			y = 7;
		return y;
	}

	public static boolean bijiao(int year, int mouth, int day, int year2,
			int mouth2, int day2) {
		return ((year - year2) * 100 + (mouth - mouth2)) * 100 + day >= day2;
	}

	public static String getNextDay() {
		Date date = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
		return getDateStr(
				new Date(date.getYear(), date.getMonth(), date.getDate(), 10,
						0, 0), "yyyy-MM-dd HH:mm:ss");

	}
}
