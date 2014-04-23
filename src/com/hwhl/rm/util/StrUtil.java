package com.hwhl.rm.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * 字符串工具类
 */
public class StrUtil {
	/**
	 * 格式化数字为int
	 * 
	 * @param v
	 * @return
	 */
	public static int nullToInt(Object vStr) {
		int str = 0;
		String v = StrUtil.nullToStr(vStr);
		if ("".equals(v)) {
			return str;
		}
		try {
			str = Integer.valueOf(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 格式化数字为int
	 * 
	 * @param v
	 * @return
	 */
	public static Long nullToLong(Object vStr) {
		Long str = 0L;
		String v = StrUtil.nullToStr(vStr);
		if ("".equals(v)) {
			return str;
		}
		try {
			str = Long.valueOf(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 格式化数字为double
	 * 
	 * @param v
	 * @return
	 */
	public static Double nullToDouble(Object vStr) {
		Double str = 0.00;
		String v = StrUtil.nullToStr(vStr);
		if ("".equals(v)) {
			return str;
		}
		try {
			str = Double.valueOf(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 格式化数字为Boolean
	 * 
	 * @param v
	 * @return
	 */
	public static boolean nullToBoolean(Object vStr) {
		boolean str = false;
		String v = StrUtil.nullToStr(vStr);
		if ("".equals(v)) {
			return str;
		}
		try {
			str = Boolean.valueOf(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 假如obj对象 是null返回""
	 * 
	 * @param obj
	 * @return
	 */
	public static String nullToStr(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	public static int StringToInt(String s) {
		int tmp = 0;
		if (s == null)
			return 0;
		try {
			tmp = Integer.parseInt(s);
		} catch (Exception e) {
			tmp = 0;
		}
		return tmp;
	}

	public static float StringToFloat(String s) {
		float tmp = 0;
		if (s == null)
			return 0;
		try {
			tmp = Float.parseFloat(s);
		} catch (Exception e) {
			tmp = 0;
		}
		return tmp;
	}

	/**
	 * 把字符串转换成BigDecimal,并用format进行格式化操作
	 * 
	 * @param obj
	 * @param format
	 * @return
	 */
	public static BigDecimal formatBigDecimal(Number bd, String format) {
		DecimalFormat df = new DecimalFormat(format);
		return new BigDecimal(df.format(bd));
	}

	/**
	 * 把字符串转换成BigDecimal
	 * 
	 * @param obj
	 * @param format
	 * @return
	 */
	public static BigDecimal nullToBigDecimal(Object obj) {
		if ("".equals(StrUtil.nullToStr(obj))) {
			obj = "0.00";
		}
		BigDecimal bd = new BigDecimal(obj.toString());
		return bd;
	}
	
	public static String encode(String str)
	{
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	public static String decode(String str)
	{
		String result = str;
		try {
			result = URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
