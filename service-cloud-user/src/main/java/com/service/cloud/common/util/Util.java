package com.service.cloud.common.util;

import com.xlhy.servicecenter.common.constants.ErrorCode;
import com.xlhy.servicecenter.common.exception.ServiceException;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {
	public static void validateEmpty(String key, String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new ServiceException(ErrorCode.PARAM_FORMAT_ILLEGAL, key + "的值为空!");
		}
	}

	public static void validateEmpty(String key, List<?> value) {
		if (value == null || value.isEmpty()) {
			throw new ServiceException(ErrorCode.PARAM_FORMAT_ILLEGAL, key + "的值为空!");
		}
	}

	public static void validateEmpty(String key, Integer value) {
		if (value == null || value == 0) {
			throw new ServiceException(ErrorCode.PARAM_FORMAT_ILLEGAL, key + "的值为空!");
		}
	}

	public static List<Integer> toIntegerList(String str) {
		List<Integer> ret = new ArrayList<Integer>();

		if (str == null || str.trim().isEmpty()) {
			return ret;
		}

		String[] splits = str.trim().split("\\s*,\\s*|\\s*;\\s*");

		for (int i = 0; i < splits.length; i++) {
			if (splits[i].isEmpty()) {
				continue;
			}

			try {
				ret.add(Integer.parseInt(splits[i]));
			} catch (NumberFormatException e) {
				throw new ServiceException(ErrorCode.PARAM_FORMAT_ILLEGAL, "不支持的参数类型，" + e.getMessage());
			}
		}

		return ret;
	}

	public static List<String> toStringList(String str) {
		List<String> ret = new ArrayList<String>();

		if (str == null || str.trim().isEmpty()) {
			return ret;
		}

		String[] splits = str.trim().split("\\s*,\\s*|\\s*;\\s*");

		for (int i = 0; i < splits.length; i++) {
			if (splits[i].isEmpty()) {
				continue;
			}

			ret.add(splits[i]);
		}

		return ret;
	}

	/**
	 * 将日期字符串转化为时间戳毫秒数
	 * 
	 * @param datestr
	 *            日期格式串，默认格式为2017-01-02
	 * @param format
	 *            格式化字符串
	 * @return
	 */
	public static Long toDateTime(String datestr, String format) {
		String finalformat = (format == null || format.trim().isEmpty()) ? "yyyy-MM-dd" : format;

		try {
			return new SimpleDateFormat(finalformat).parse(datestr).getTime();
		} catch (ParseException e) {
			throw new ServiceException(ErrorCode.PARAM_FORMAT_ILLEGAL, "解析日期错误, datestr = " + datestr + ", format = " + finalformat + ", exception = " + e.toString());
		}
	}

	/**
	 * 将日期字符串转化为日期类型
	 * 
	 * @param datestr
	 * @param format
	 * @return
	 */
	public static void checkFormat(String datestr, String format) {
		if (datestr == null || datestr.trim().isEmpty()) {
			return;
		}

		String finalformat = (format == null || format.trim().isEmpty()) ? "yyyy-MM-dd" : format;

		try {
			new SimpleDateFormat(finalformat).parse(datestr);
		} catch (ParseException e) {
			throw new ServiceException(ErrorCode.PARAM_FORMAT_ILLEGAL, "解析日期错误, datestr = " + datestr + ", format = " + finalformat + ", exception = " + e.toString());
		}
	}

	public static void checkIn(String col, Set<String> sportDailyStatCols) {
		if (col != null && !col.trim().isEmpty()) {
			if (!sportDailyStatCols.contains(col.toUpperCase())) {
				throw new ServiceException(ErrorCode.PARAM_ILLEGAL, "字段不被支持, col = " + col + ", 仅支持 = " + sportDailyStatCols);
			}
		}
	}

	public static Set<String> toSet(String[] arrays) {
		Set<String> ret = new HashSet<String>();

		for (String s : arrays) {
			ret.add(s);
		}

		return ret;
	}

	public static final int getProcessId() {
		return Integer.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]).intValue();
	}

	public static String getMD5Sign(TreeMap<String, Object> map, String scrit) {
		StringBuilder secret = new StringBuilder();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			secret.append(entry.getKey()).append(entry.getValue());
		}

		secret.append(scrit);
		return MD5.md5(secret.toString());
	}
	
	public static String base64(String str) throws UnsupportedEncodingException{
		return new String(Base64.encodeBase64(str.getBytes("utf-8")),"utf-8");
	}


}
