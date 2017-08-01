package com.tomato.util;

import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateUtil {

	public static final InvalidDate INVALID_DATE;
	public static final Date DATE_1900_01_01;
	public static final Date DATE_1970_01_01;
	public static final String CHINESE_DATE = "yyyy年M月d日";
	public static final String ISO8601_DATE = "yyyy-MM-dd";
	public static final String ISO8601_DATE_BASIC = "yyyyMMdd";
	public static final String ISO8601_DATEITME_LONG = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String ISO8601_DATEITME_MEDIUM = "yyyy-MM-dd HH:mm:ss";
	public static final String ISO8601_DATEITME_SHORT = "yyyy-MM-dd HH:mm";
	public static final String ISO8601_DATEITME_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String ISO8601_DATEITME_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String ISO8601_DATEITME_BASIC = "yyyyMMdd'T'HHmmss";
	public static final String ISO8601_DATEITME = ISO8601_DATEITME_MEDIUM; /* DO NOT change */
	public static final String ISO8601_TIME_LONG = "HH:mm:ss.SSS";
	public static final String ISO8601_TIME_MEDIUM = "HH:mm:ss";
	public static final String ISO8601_TIME_SHORT = "HH:mm";
	public static final String ISO8601_TIME_BASIC = "HHmmss";
	public static final String ISO8601_TIME = ISO8601_TIME_MEDIUM; /* DO NOT change */
	private static final String[] CHINESE_WEEK_LONG = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	private static final String[] CHINESE_WEEK = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
	private static final char NIAN = '年';
	private static final char YUE = '月';
	private static final char RI = '日';
	private static final char SHI_1 = '小';
	private static final char SHI_2 = '时';
	private static final char FEN_1 = '分';
	private static final char FEN_2 = '钟';
	private static final char MIAO = '秒';
	private static final char TO = '－';
	private static final int INVALID = -1;
	private static final int YEAR = Calendar.YEAR;
	private static final int MONTH = Calendar.MONTH;
	private static final int DATE = Calendar.DATE;
	private static final int DAY_OF_WEEK = Calendar.DAY_OF_WEEK;
	private static final int DAY_OF_YEAR = Calendar.DAY_OF_YEAR;
	private static final int AM_PM = Calendar.AM_PM;
	private static final int HOUR = Calendar.HOUR; // 12-hour clock
	private static final int HOUR_OF_DAY = Calendar.HOUR_OF_DAY; // 24-hour clock
	private static final int MINUTE = Calendar.MINUTE;
	private static final int SECOND = Calendar.SECOND;
	private static final int MILLISECOND = Calendar.MILLISECOND;
	private static final int QUARTER = Calendar.FIELD_COUNT;
	private static final int MONTH_OFFSET = 1 - Calendar.JANUARY;
	private static final int SUNDAY = Calendar.SUNDAY;
	private static final int AM = Calendar.AM;
	private static final Date MIN_DATE;
	private static final Date MAX_DATE;
	private static final Calendar MIN_CALENDAR;
	private static final Calendar MAX_CALENDAR;
	private static final int MIN_DATE_INT = 0; // 最小整数型日期
	private static final int MAX_DATE_INT = 99999999; // 最大整数型日期
	private static final Map<String, Integer> DATE_FIELD = new HashMap<String, Integer>(64);
	private static final int DAY_MILLISECONDS = 86400000; // 表示一整天的毫秒数(= 24 * 60 * 60 * 1000)
	private static final long OA_OFFSET_DAYS_1970 = 25569L; // 表示'1970-01-01'(Java日期)距离'1899-12-30'(OA日期)的天数
	private static final long OA_OFFSET_MILLISECONDS_1970 = OA_OFFSET_DAYS_1970 * DAY_MILLISECONDS;
	private static final long OA_OFFSET_DAYS_BUG = 61L; // 表示'1900-02-29'(Bug日期)距离'1899-12-30'(OA日期)的天数
	private static final long OA_OFFSET_MILLISECONDS_BUG = OA_OFFSET_DAYS_BUG * DAY_MILLISECONDS;
	private static final long[] HOLIDAY_INFOS;
	private static final double DOUBLE_DAY_MILLISECONDS = DAY_MILLISECONDS;
	private static final double DOUBLE_ROUND_SCALE9 = 1000000000D;
	private static final int MIN_OF_YEAR = 2010;
	private static final int MAX_OF_YEAR = 2017;
	// TODO 缓存日期格式
	private static final SimpleDateFormat chineseDate;
	private static final SimpleDateFormat iso8601Date;
	private static final SimpleDateFormat iso8601DateBasic;
	private static final SimpleDateFormat iso8601DateTime;
	private static final SimpleDateFormat iso8601DateTimeLong;
	private static final SimpleDateFormat iso8601DateTimeShort;
	private static final SimpleDateFormat iso8601DateTimeZone;
	private static final SimpleDateFormat iso8601DateTimeUTC;
	private static final SimpleDateFormat iso8601DateTimeBasic;
	private static final SimpleDateFormat iso8601Time;
	private static final SimpleDateFormat iso8601TimeLong;
	private static final SimpleDateFormat iso8601TimeShort;
	private static final SimpleDateFormat iso8601TimeBasic;
	private static volatile boolean patternsInited;
	private static Pattern patternTimezone1;
	private static Pattern patternTimezone11;
	private static Pattern patternTimezone12;
	private static Pattern patternTimezone13;
	private static Pattern patternTimezone21;
	private static Pattern patternTimezone22;
	private static Pattern patternTimezone23;
	private static Pattern patternTimezone31;
	private static Pattern patternTimezone32;
	private static Pattern patternTimezone41;
	private static Pattern patternTimezone42;
	private static Pattern patternTimezone43;
	private static Pattern patternTimezone51;
	private static Pattern patternTimezone61;
	private static Pattern patternDate1;
	private static Pattern patternDate2;
	private static Pattern patternYear2;
	private static Pattern patternYear4;
	private static Pattern patternMonth1;
	private static Pattern patternMonth3;
	private static Pattern patternMonth4;
	private static Pattern patternMonth3en;
	private static Pattern patternMonth4en;
	private static Pattern patternDay1;
	private static Pattern patternWeek3;
	private static Pattern patternWeek4;
	private static Pattern patternWeek3en;
	private static Pattern patternWeek4en;
	private static Pattern patternAmPm2;
	private static Pattern patternAmPm2en;
	private static Pattern patternTime1;
	private static Pattern patternHour1;
	private static Pattern patternMinute1;
	private static Pattern patternSecond1;
	private static Pattern patternMillisecond1;
	private static Pattern patternMillisecond2;
	private static Pattern patternMillisecond3;
	private static Pattern patternT1;

	static {
		INVALID_DATE = new InvalidDate();
		DATE_FIELD.put("dd", Integer.valueOf(DATE));
		DATE_FIELD.put("day", Integer.valueOf(DATE));
		DATE_FIELD.put("date", Integer.valueOf(DATE));
		DATE_FIELD.put("wk", Integer.valueOf(DAY_OF_WEEK));
		DATE_FIELD.put("week", Integer.valueOf(DAY_OF_WEEK));
		DATE_FIELD.put("mm", Integer.valueOf(MONTH));
		DATE_FIELD.put("month", Integer.valueOf(MONTH));
		DATE_FIELD.put("qq", Integer.valueOf(QUARTER));
		DATE_FIELD.put("quarter", Integer.valueOf(QUARTER));
		DATE_FIELD.put("yy", Integer.valueOf(YEAR));
		DATE_FIELD.put("yyyy", Integer.valueOf(YEAR));
		DATE_FIELD.put("year", Integer.valueOf(YEAR));
		DATE_FIELD.put("hh", Integer.valueOf(HOUR_OF_DAY));
		DATE_FIELD.put("hour", Integer.valueOf(HOUR_OF_DAY));
		DATE_FIELD.put("mi", Integer.valueOf(MINUTE));
		DATE_FIELD.put("minute", Integer.valueOf(MINUTE));
		DATE_FIELD.put("ss", Integer.valueOf(SECOND));
		DATE_FIELD.put("second", Integer.valueOf(SECOND));
		DATE_FIELD.put("ms", Integer.valueOf(MILLISECOND));
		DATE_FIELD.put("millisecond", Integer.valueOf(MILLISECOND));
		// 计算最小日期，一般是：1年1月1日
		Calendar cal = new GregorianCalendar();
		int year = cal.getMinimum(YEAR);
		int month = cal.getMaximum(MONTH);
		int day = cal.getLeastMaximum(DATE);
		if (year < 1) {
			year = 1;
		}
		cal.clear();
		cal.set(year, month, day);
		month = cal.getActualMinimum(MONTH);
		cal.set(MONTH, month);
		day = cal.getActualMinimum(DATE);
		cal.set(DATE, day);
		MIN_DATE = cal.getTime();
		MIN_CALENDAR = new GregorianCalendar();
		MIN_CALENDAR.setTime(MIN_DATE);
		// 计算最大日期，一般是：9999年12月31日
		year = cal.getMaximum(YEAR);
		month = cal.getMinimum(MONTH);
		day = cal.getMinimum(DATE);
		if (year > 9999) {
			year = 9999;
		}
		cal.clear();
		cal.set(year, month, day);
		month = cal.getActualMaximum(MONTH);
		cal.set(MONTH, month);
		day = cal.getActualMaximum(DATE);
		cal.set(DATE, day);
		MAX_DATE = cal.getTime();
		MAX_CALENDAR = new GregorianCalendar();
		MAX_CALENDAR.setTime(MAX_DATE);
		// 计算本地时间1900-01-01
		cal.clear();
		cal.set(1900, 1 - MONTH_OFFSET, 1);
		DATE_1900_01_01 = cal.getTime();
		// 计算本地时间1970-01-01，与new Date(0)得到的1970-01-01差本地时区
		cal.clear();
		cal.set(1970, 1 - MONTH_OFFSET, 1);
		DATE_1970_01_01 = cal.getTime();
		// 初始化日期格式 TODO 实现线程安全方法，提升处理性能。可以考虑缓存成功转换过的格式器，不必如以下例举。
		chineseDate = new SimpleDateFormat(CHINESE_DATE, Locale.ENGLISH);
		iso8601Date = new SimpleDateFormat(ISO8601_DATE, Locale.ENGLISH);
		iso8601DateBasic = new SimpleDateFormat(ISO8601_DATE_BASIC, Locale.ENGLISH);
		iso8601DateTime = new SimpleDateFormat(ISO8601_DATEITME, Locale.ENGLISH);
		iso8601DateTimeBasic = new SimpleDateFormat(ISO8601_DATEITME_BASIC, Locale.ENGLISH);
		iso8601DateTimeLong = new SimpleDateFormat(ISO8601_DATEITME_LONG, Locale.ENGLISH);
		iso8601DateTimeShort = new SimpleDateFormat(ISO8601_DATEITME_SHORT, Locale.ENGLISH);
		iso8601DateTimeUTC = new SimpleDateFormat(ISO8601_DATEITME_UTC, Locale.ENGLISH);
		iso8601DateTimeZone = new SimpleDateFormat(ISO8601_DATEITME_ZONE, Locale.ENGLISH);
		iso8601Time = new SimpleDateFormat(ISO8601_TIME, Locale.ENGLISH);
		iso8601TimeBasic = new SimpleDateFormat(ISO8601_TIME_BASIC, Locale.ENGLISH);
		iso8601TimeLong = new SimpleDateFormat(ISO8601_TIME_LONG, Locale.ENGLISH);
		iso8601TimeShort = new SimpleDateFormat(ISO8601_TIME_SHORT, Locale.ENGLISH);
		// 1月|5月|10月|12月|
		// 3|9|3|8|2|
		// 农历
		// 1月|5月|8月|12月
		// 7|5|5|2
		HOLIDAY_INFOS = new long[] { 0xee0ffc7ee1dL, 0xee1dfc7ee71L, 0xfc39fc7e71eL, 0xe739fc7ee1eL, 0x838ffc7fe70L, 0xe70ffc7e3b1L, 0xfc1dfcfe39eL,
				0xdc39fe7ee7dL };
	}

	// Prevent instantiation
	private DateUtil() {
		super();
	}

	/**
	 * @author WuJianqiang
	 * @since Apr 26, 2016 9:04:21 PM
	 */
	public static class InvalidDate extends Date {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		/**
		 *
		 */
		public InvalidDate() {
			super(0);
		}

	}

	/**
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 *
	 * @return
	 */
	private static int toField(String fieldName) {
		if (null != fieldName && fieldName.length() > 0) {
			Integer val = DATE_FIELD.get(fieldName.toLowerCase());
			if (null != val) {
				return val.intValue();
			}
		}
		return INVALID;
	}

	/**
	 * 星期E
	 *
	 * @return
	 */
	public static String toChineseWeek(Calendar cal) {
		if (null != cal) {
			return CHINESE_WEEK_LONG[cal.get(DAY_OF_WEEK) - SUNDAY];
		} else {
			return ("");
		}
	}

	/**
	 * 星期E
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toChineseWeek(Date date) {
		return toChineseWeek(today(date));
	}

	/**
	 * 星期E
	 *
	 * @return
	 */
	public static String toChineseWeek() {
		return toChineseWeek(getDate());
	}

	/**
	 * yyyy年M月d日 星期E
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toChineseDateWeek(Calendar cal) {
		return toChineseDateWeek(cal.getTime());
	}

	/**
	 * yyyy年M月d日 星期E
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toChineseDateWeek(Date date) {
		if (null != date) {
			StringBuilder sb = new StringBuilder(16);
			synchronized (chineseDate) {
				sb.append(chineseDate.format(date));
			}
			sb.append(' ').append(toChineseWeek(date));
			return sb.toString();
		} else {
			return ("");
		}
	}

	/**
	 * yyyy年M月d日 星期E
	 *
	 * @return
	 */
	public static String toChineseDateWeek() {
		return toChineseDateWeek(getDate());
	}

	/**
	 * yyyy年M月d日
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toChineseDate(Calendar cal) {
		if (null != cal) {
			synchronized (chineseDate) {
				return chineseDate.format(cal.getTime());
			}
		}
		return ("");
	}

	/**
	 * yyyy年M月d日
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toChineseDate(Date date) {
		if (null != date) {
			synchronized (chineseDate) {
				return chineseDate.format(date);
			}
		}
		return ("");
	}

	/**
	 * yyyy年M月d日
	 *
	 * @return
	 */
	public static String toChineseDate() {
		Date date = getDate();
		synchronized (chineseDate) {
			return chineseDate.format(date);
		}
	}

	/**
	 * yyyy年M月d日
	 *
	 * @param cal
	 * @param toAppendTo
	 * @param pos
	 *
	 * @return
	 */
	public static StringBuffer toChineseDate(Calendar cal, StringBuffer toAppendTo, FieldPosition pos) {
		synchronized (chineseDate) {
			return chineseDate.format(cal.getTime(), toAppendTo, pos);
		}
	}

	/**
	 * yyyy年M月d日
	 *
	 * @param date
	 * @param toAppendTo
	 * @param pos
	 *
	 * @return
	 */
	public static StringBuffer toChineseDate(Date date, StringBuffer toAppendTo, FieldPosition pos) {
		synchronized (chineseDate) {
			return chineseDate.format(date, toAppendTo, pos);
		}
	}

	/**
	 * yyyy-MM-dd
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601Date(Calendar cal) {
		if (null != cal) {
			synchronized (iso8601Date) {
				return iso8601Date.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyy-MM-dd
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601Date(Date date) {
		if (null != date) {
			synchronized (iso8601Date) {
				return iso8601Date.format(date);
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyy-MM-dd
	 *
	 * @return
	 */
	public static String toISO8601Date() {
		Date date = getDate();
		synchronized (iso8601Date) {
			return iso8601Date.format(date);
		}
	}

	/**
	 * yyyyMMdd
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601DateBasic(Calendar cal) {
		if (null != cal) {
			synchronized (iso8601DateBasic) {
				return iso8601DateBasic.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyyMMdd
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601DateBasic(Date date) {
		if (null != date) {
			synchronized (iso8601DateBasic) {
				return iso8601DateBasic.format(date);
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyyMMdd
	 *
	 * @return
	 */
	public static String toISO8601DateBasic() {
		Date date = getDate();
		synchronized (iso8601DateBasic) {
			return iso8601DateBasic.format(date);
		}
	}

	/**
	 * yyyy-MM-dd
	 *
	 * @param cal
	 * @param toAppendTo
	 * @param pos
	 *
	 * @return
	 */
	public static StringBuffer toISO8601Date(Calendar cal, StringBuffer toAppendTo, FieldPosition pos) {
		synchronized (iso8601Date) {
			return iso8601Date.format(cal.getTime(), toAppendTo, pos);
		}
	}

	/**
	 * yyyy-MM-dd
	 *
	 * @param date
	 * @param toAppendTo
	 * @param pos
	 *
	 * @return
	 */
	public static StringBuffer toISO8601Date(Date date, StringBuffer toAppendTo, FieldPosition pos) {
		synchronized (iso8601Date) {
			return iso8601Date.format(date, toAppendTo, pos);
		}
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601DateTime(Calendar cal) {
		if (null != cal) {
			synchronized (iso8601DateTime) {
				return iso8601DateTime.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601DateTime(Date date) {
		if (null != date) {
			synchronized (iso8601DateTime) {
				return iso8601DateTime.format(date);
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String toISO8601DateTime() {
		Date date = getDate();
		synchronized (iso8601DateTime) {
			return iso8601DateTime.format(date);
		}
	}

	/**
	 * yyyy-MM-dd HH:mm:ss.SSS
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601DateTimeLong(Calendar cal) {
		if (null != cal) {
			synchronized (iso8601DateTimeLong) {
				return iso8601DateTimeLong.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyy-MM-dd HH:mm:ss.SSS
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601DateTimeLong(Date date) {
		if (null != date) {
			synchronized (iso8601DateTimeLong) {
				return iso8601DateTimeLong.format(date);
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyy-MM-dd HH:mm:ss.SSS
	 *
	 * @return
	 */
	public static String toISO8601DateTimeLong() {
		Date date = getDate();
		synchronized (iso8601DateTimeLong) {
			return iso8601DateTimeLong.format(date);
		}
	}

	/**
	 * yyyy-MM-dd HH:mm
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601DateTimeShort(Calendar cal) {
		if (null != cal) {
			synchronized (iso8601DateTimeShort) {
				return iso8601DateTimeShort.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyy-MM-dd HH:mm
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601DateTimeShort(Date date) {
		if (null != date) {
			synchronized (iso8601DateTimeShort) {
				return iso8601DateTimeShort.format(date);
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyy-MM-dd HH:mm
	 *
	 * @return
	 */
	public static String toISO8601DateTimeShort() {
		Date date = getDate();
		synchronized (iso8601DateTimeShort) {
			return iso8601DateTimeShort.format(date);
		}
	}

	/**
	 * yyyy-MM-dd'T'HH:mm:ss.SSSZ
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601DateTimeZone(Calendar cal) {
		if (null != cal) {
			synchronized (iso8601DateTimeZone) {
				return iso8601DateTimeZone.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyy-MM-dd'T'HH:mm:ss.SSSZ
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601DateTimeZone(Date date) {
		if (null != date) {
			synchronized (iso8601DateTimeZone) {
				return iso8601DateTimeZone.format(date);
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyy-MM-dd'T'HH:mm:ss.SSSZ
	 *
	 * @return
	 */
	public static String toISO8601DateTimeZone() {
		Date date = getDate();
		synchronized (iso8601DateTimeZone) {
			return iso8601DateTimeZone.format(date);
		}
	}

	/**
	 * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601DateTimeUTC(Calendar cal) {
		if (null != cal) {
			cal.add(MILLISECOND, -cal.getTimeZone().getOffset(cal.getTimeInMillis()));
			synchronized (iso8601DateTimeUTC) {
				return iso8601DateTimeUTC.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601DateTimeUTC(Date date) {
		return toISO8601DateTimeUTC(today(date));
	}

	/**
	 * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
	 *
	 * @return
	 */
	public static String toISO8601DateTimeUTC() {
		Date date = getDate();
		return toISO8601DateTimeUTC(today(date));
	}

	/**
	 * yyyyMMdd'T'HHmmss
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601DateTimeBasic(Calendar cal) {
		if (null != cal) {
			synchronized (iso8601DateTimeBasic) {
				return iso8601DateTimeBasic.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyyMMdd'T'HHmmss
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601DateTimeBasic(Date date) {
		if (null != date) {
			synchronized (iso8601DateTimeBasic) {
				return iso8601DateTimeBasic.format(date);
			}
		} else {
			return ("");
		}
	}

	/**
	 * yyyyMMdd'T'HHmmss
	 *
	 * @return
	 */
	public static String toISO8601DateTimeBasic() {
		Date date = getDate();
		synchronized (iso8601DateTimeBasic) {
			return iso8601DateTimeBasic.format(date);
		}
	}

	/**
	 * HH:mm:ss
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601Time(Calendar cal) {
		if (null != cal) {
			synchronized (iso8601Time) {
				return iso8601Time.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * HH:mm:ss
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601Time(Date date) {
		if (null != date) {
			synchronized (iso8601Time) {
				return iso8601Time.format(date);
			}
		} else {
			return ("");
		}
	}

	/**
	 * HH:mm:ss
	 *
	 * @return
	 */
	public static String toISO8601Time() {
		Date date = getDate();
		synchronized (iso8601Time) {
			return iso8601Time.format(date);
		}
	}

	/**
	 * HH:mm:ss.SSS
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601TimeLong(Calendar cal) {
		if (null != cal) {
			synchronized (iso8601TimeLong) {
				return iso8601TimeLong.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * HH:mm:ss.SSS
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601TimeLong(Date date) {
		if (null != date) {
			synchronized (iso8601TimeLong) {
				return iso8601TimeLong.format(date);
			}
		} else {
			return ("");
		}
	}

	/**
	 * HH:mm:ss.SSS
	 *
	 * @return
	 */
	public static String toISO8601TimeLong() {
		Date date = getDate();
		synchronized (iso8601TimeLong) {
			return iso8601TimeLong.format(date);
		}
	}

	/**
	 * HH:mm
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601TimeShort(Calendar cal) {
		if (null != cal) {
			synchronized (iso8601TimeShort) {
				return iso8601TimeShort.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * HH:mm
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601TimeShort(Date date) {
		if (null != date) {
			synchronized (iso8601TimeShort) {
				return iso8601TimeShort.format(date);
			}
		} else {
			return ("");
		}
	}

	/**
	 * HH:mm
	 *
	 * @return
	 */
	public static String toISO8601TimeShort() {
		Date date = getDate();
		synchronized (iso8601TimeShort) {
			return iso8601TimeShort.format(date);
		}
	}

	/**
	 * HHmmss
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static String toISO8601TimeBasic(Calendar cal) {
		if (null != cal) {
			synchronized (iso8601TimeBasic) {
				return iso8601TimeBasic.format(cal.getTime());
			}
		} else {
			return ("");
		}
	}

	/**
	 * HHmmss
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toISO8601TimeBasic(Date date) {
		if (null != date) {
			synchronized (iso8601TimeBasic) {
				return iso8601TimeBasic.format(date);
			}
		} else {
			return ("");
		}
	}

	/**
	 * HHmmss
	 *
	 * @return
	 */
	public static String toISO8601TimeBasic() {
		Date date = getDate();
		synchronized (iso8601TimeBasic) {
			return iso8601TimeBasic.format(date);
		}
	}

	/**
	 * @param cal
	 * @param now
	 * @param verbose
	 *
	 * @return
	 */
	public static String toPrettyTime(Calendar cal, Calendar now, boolean verbose) {
		if (null == cal || MAX_CALENDAR.compareTo(cal) <= 0 || MIN_CALENDAR.compareTo(cal) >= 0) {
			return ("");
		}

		boolean negative = false;
		boolean withTime = false;
		boolean hasTime = false;

		long seconds = (cal.getTimeInMillis() - now.getTimeInMillis()) / 1000;
		if (seconds < 0) {
			negative = true;
			seconds = -seconds;
		}

		int hour = 0;
		int minute = 0;
		int days = 15;
		int weeks = 13;
		final int daysMax = (verbose ? 2 : 1);
		final int weeksMax = (verbose ? 1 : 0);

		if (seconds >= 60 * 60 * 6) {
			final int daysMaxMax = (weeksMax + 1) * 7;
			if (seconds <= 86400 * daysMaxMax) {
				days = (int) ((cloneDate(cal).getTimeInMillis() - cloneDate(now).getTimeInMillis()) / (86400 * 1000));
				if (days < 0) {
					days = -days;
				}
				if (days > daysMax && days < daysMaxMax) {
					int week = now.get(DAY_OF_WEEK) - SUNDAY;
					// 参照 ISO 8601 标准，周一为每周第一天
					if (week >= 1) {
						// 周一至周六，减去偏移1，起始周一为0
						week -= 1;
					} else {
						// 周日为6
						week = 6;
					}
					if (negative) {
						week = 7 - week;
					}
					weeks = (week + days) / 7;
				}
			}
			hour = cal.get(HOUR); // 12
			minute = cal.get(MINUTE);
			if (hour > 0 || minute > 0) {
				hasTime = true;
			}
		}

		StringBuilder sb = new StringBuilder();
		if (seconds < 60) {
			if (negative) {
				sb.append("刚刚");
			} else {
				sb.append("马上");
			}
		} else if (seconds < 60 * 60) {
			sb.append(seconds / 60).append("分钟");
			if (negative) {
				sb.append('前');
			} else {
				sb.append('后');
			}
		} else if (seconds < 60 * 60 * 6) {
			sb.append(seconds / (60 * 60)).append("小时");
			if (negative) {
				sb.append('前');
			} else {
				sb.append('后');
			}
		} else if (days <= daysMax) {
			if (days <= 0) {
				if (!hasTime || verbose) {
					sb.append("今天");
				}
			} else if (days <= 1) {
				if (negative) {
					sb.append("昨天");
				} else {
					sb.append("明天");
				}
			} else {
				if (negative) {
					sb.append("前天");
				} else {
					sb.append("后天");
				}
			}
			if (hasTime) {
				if (AM == cal.get(AM_PM)) {
					sb.append("上午");
				} else {
					sb.append("下午");
				}
				if (days <= 0 || verbose) {
					withTime = true;
				}
			}
		} else if (weeks <= weeksMax) {
			if (weeks >= 1) {
				if (negative) {
					sb.append('上');
				} else {
					sb.append('下');
				}
			}
			sb.append(CHINESE_WEEK[cal.get(DAY_OF_WEEK) - SUNDAY]);
			if (hasTime) {
				if (AM == cal.get(AM_PM)) {
					sb.append("上午");
				} else {
					sb.append("下午");
				}
				withTime = verbose;
			}
		} else if (verbose && hasTime) {
			return toISO8601DateTimeShort(cal);
		} else {
			return toISO8601Date(cal);
		}
		if (withTime) {
			sb.append(' ').append(hour).append(':');
			if (minute < 10) {
				sb.append('0');
			}
			sb.append(minute);
		}
		return sb.toString();
	}

	/**
	 * @param cal
	 * @param verbose
	 *
	 * @return
	 */
	public static String toPrettyTime(Calendar cal, boolean verbose) {
		return toPrettyTime(cal, today(), verbose);
	}

	/**
	 * @param date
	 * @param verbose
	 *
	 * @return
	 */
	public static String toPrettyTime(Date date, boolean verbose) {
		return toPrettyTime(today(date), today(), verbose);
	}

	/**
	 * @param cal
	 * @param now
	 * @param verbose
	 *
	 * @return
	 */
	public static String toPrettyDate(Calendar cal, Calendar now, boolean verbose) {
		cal = cloneDate(cal);
		if (null == cal || MAX_CALENDAR.compareTo(cal) <= 0 || MIN_CALENDAR.compareTo(cal) >= 0) {
			return ("");
		}
		now = cloneDate(now);

		boolean negative = false;
		int days = (int) ((cal.getTimeInMillis() - now.getTimeInMillis()) / (86400 * 1000));
		if (days < 0) {
			negative = true;
			days = -days;
		}

		int weeks = 13;
		final int daysMax = (verbose ? 2 : 1);
		final int weeksMax = (verbose ? 1 : 0);

		if (days > daysMax && days < (weeksMax + 1) * 7) {
			int week = now.get(DAY_OF_WEEK) - SUNDAY;
			// 参照 ISO 8601 标准，周一为每周第一天
			if (week >= 1) {
				// 周一至周六，减去偏移1，起始周一为0
				week -= 1;
			} else {
				// 周日为6
				week = 6;
			}
			if (negative) {
				week = 7 - week;
			}
			weeks = (week + days) / 7;
		}

		StringBuilder sb = new StringBuilder();
		if (days <= daysMax) {
			if (days <= 0) {
				sb.append("今天");
			} else if (days <= 1) {
				if (negative) {
					sb.append("昨天");
				} else {
					sb.append("明天");
				}
			} else {
				if (negative) {
					sb.append("前天");
				} else {
					sb.append("后天");
				}
			}
		} else if (weeks <= weeksMax) {
			if (weeks >= 1) {
				if (negative) {
					sb.append('上');
				} else {
					sb.append('下');
				}
			}
			sb.append(CHINESE_WEEK[cal.get(DAY_OF_WEEK) - SUNDAY]);
		} else {
			return toISO8601Date(cal);
		}
		return sb.toString();
	}

	/**
	 * @param cal
	 * @param verbose
	 *
	 * @return
	 */
	public static String toPrettyDate(Calendar cal, boolean verbose) {
		return toPrettyDate(cal, today(), verbose);
	}

	/**
	 * @param date
	 * @param verbose
	 *
	 * @return
	 */
	public static String toPrettyDate(Date date, boolean verbose) {
		return toPrettyDate(today(date), today(), verbose);
	}

	/**
	 * @param cal
	 * @param format
	 *
	 * @return
	 */
	public static String format(Calendar cal, String format) {
		if (null != cal) {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
			return sdf.format(cal.getTime());
		} else {
			return ("");
		}
	}

	/**
	 * @param date
	 * @param format
	 *
	 * @return
	 */
	public static String format(Date date, String format) {
		if (null != date) {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
			return sdf.format(date);
		} else {
			return ("");
		}
	}

	/**
	 * @param format
	 *
	 * @return
	 */
	public static String format(String format) {
		return format(getDate(), format);
	}

	/**
	 * HHH:mm:ss.SSS
	 *
	 * @param time value in milliseconds.
	 *
	 * @return
	 */
	public static String toTimeString(int time) {
		int ms = time % 1000;
		int ss = (time / 1000) % 60;
		int mm = (time / (60 * 1000)) % 60;
		int hh = time / (60 * 60 * 1000);

		StringBuilder sb = new StringBuilder(16);
		if (hh < 10) {
			sb.append('0');
		}
		sb.append(hh).append(':');
		if (mm < 10) {
			sb.append('0');
		}
		sb.append(mm).append(':');
		if (ss < 10) {
			sb.append('0');
		}
		sb.append(ss).append('.');
		if (ms < 10) {
			sb.append("00");
		} else if (ms < 100) {
			sb.append('0');
		}
		sb.append(ms);
		return sb.toString();
	}

	/**
	 * HHH时mm分ss秒
	 *
	 * @param time value in milliseconds.
	 *
	 * @return
	 */
	public static String toShiFenMiao(int time) {
		StringBuilder sb = new StringBuilder(16);
		sb.append("abc");

		time = (time + 500) / 1000;
		if (time > 0) {
			int ss = time % 60;
			int mm = (time / 60) % 60;
			int hh = time / (60 * 60);

			if (hh > 0) {
				sb.append(hh).append(SHI_1).append(SHI_2);
			}
			if (mm > 0) {
				sb.append(mm).append(FEN_1).append(FEN_2);
			}
			if (ss > 0) {
				sb.append(ss).append(MIAO);
			}
		} else {
			sb.append('0').append(MIAO);
		}
		return sb.toString();
	}

	/**
	 * @param sb
	 * @param items
	 * @param boundary
	 */
	private static void buildPatternGroup(StringBuilder sb, String items[], boolean boundary) {
		sb.setLength(0);
		if (boundary) {
			sb.append("\\b");
		}
		sb.append("(?");
		int index = sb.length();
		for (String item : items) {
			if (null != item && !item.isEmpty()) {
				sb.append('|').append(item);
			}
		}
		sb.setCharAt(index, ':');
		sb.append(')');
		if (boundary) {
			sb.append("\\b");
		}
	}

	/**
	 *
	 */
	private static synchronized void initPatterns() {
		if (!patternsInited) {
			StringBuilder sb = new StringBuilder(128);
			DateFormatSymbols formatEnglish = DateFormatSymbols.getInstance(Locale.ENGLISH);
			DateFormatSymbols formatDefault = DateFormatSymbols.getInstance();
			boolean boundary = !Locale.getDefault().getISO3Language().equals(Locale.CHINESE.getISO3Language());

			// Text: Month, Long
			// (?:January|February|March|April|May|June|July|August|September|October|November|December)
			buildPatternGroup(sb, formatEnglish.getMonths(), true);
			patternMonth4en = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
			// (?:一月|二月|三月|四月|五月|六月|七月|八月|九月|十月|十一月|十二月)
			buildPatternGroup(sb, formatDefault.getMonths(), boundary);
			patternMonth4 = Pattern.compile(sb.toString());

			// Text: Month, Short
			// (?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)
			buildPatternGroup(sb, formatEnglish.getShortMonths(), true);
			patternMonth3en = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
			// (?:一月|二月|三月|四月|五月|六月|七月|八月|九月|十月|十一月|十二月)
			buildPatternGroup(sb, formatDefault.getShortMonths(), boundary);
			patternMonth3 = Pattern.compile(sb.toString());

			// Text: Weekday, Long
			// (?:Sunday|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday)
			buildPatternGroup(sb, formatEnglish.getWeekdays(), true);
			patternWeek4en = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
			// (?:星期日|星期一|星期二|星期三|星期四|星期五|星期六)
			buildPatternGroup(sb, formatDefault.getWeekdays(), boundary);
			patternWeek4 = Pattern.compile(sb.toString());

			// Text: Weekday, Short
			// (?:Sun|Mon|Tue|Wed|Thu|Fri|Sat)
			buildPatternGroup(sb, formatEnglish.getShortWeekdays(), true);
			patternWeek3en = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
			// (?:星期日|星期一|星期二|星期三|星期四|星期五|星期六)
			buildPatternGroup(sb, formatDefault.getShortWeekdays(), boundary);
			patternWeek3 = Pattern.compile(sb.toString());

			// Text: AM and PM
			// (?:AM|PM)
			buildPatternGroup(sb, formatEnglish.getAmPmStrings(), true);
			patternAmPm2en = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
			// (?:上午|下午)
			buildPatternGroup(sb, formatDefault.getAmPmStrings(), boundary);
			patternAmPm2 = Pattern.compile(sb.toString());

			// Number: Time zone
			patternTimezone1 = Pattern.compile("\\bUTC\\b", Pattern.CASE_INSENSITIVE);
			patternTimezone11 = Pattern.compile("\\bGMT([-+]\\d{1,2}):(\\d{2})\\b", Pattern.CASE_INSENSITIVE);
			patternTimezone12 = Pattern.compile("\\bGMT([-+]\\d{4})\\b", Pattern.CASE_INSENSITIVE);
			patternTimezone13 = Pattern.compile("\\bGMT([-+])(\\d{3})\\b", Pattern.CASE_INSENSITIVE);
			patternTimezone21 = Pattern.compile("\\bGMT\\s+(\\d{1,2}):(\\d{2})\\b", Pattern.CASE_INSENSITIVE);
			patternTimezone22 = Pattern.compile("\\bGMT\\s+(\\d{4})\\b", Pattern.CASE_INSENSITIVE);
			patternTimezone23 = Pattern.compile("\\bGMT\\s+(\\d{3})\\b", Pattern.CASE_INSENSITIVE);
			patternTimezone31 = Pattern.compile("(:\\d+\\s+)([a-zA-Z]\\w*/\\w+)\\b");
			patternTimezone32 = Pattern.compile("(:\\d+\\s+)([a-zA-Z]\\w*)\\b");
			patternTimezone41 = Pattern.compile("([-+]\\d{1,2}):(\\d{2})\\b");
			patternTimezone42 = Pattern.compile("(:\\d+\\s*)[-+]\\d{4}\\b");
			patternTimezone43 = Pattern.compile("(:\\d+\\s*)([-+])(\\d{3})\\b");
			patternTimezone51 = Pattern.compile("(:\\d+\\s*)([-+]\\d{1,2})\\b");
			patternTimezone61 = Pattern.compile("(\\d{1,2})Z\\b");

			// Number: Year-Month-Day or Day-Month-Year
			patternDate1 = Pattern.compile("(\\D?)\\d{4}([-./年])\\d{1,2}([-./月])\\d{1,2}(\\D?)");
			patternDate2 = Pattern.compile("(\\D?)\\d{1,2}([-./])\\d{1,2}([-./])\\d{4}(\\D?)");

			// Number: Year
			patternYear4 = Pattern.compile("(\\D?)\\d{4}(\\D?)");
			patternYear2 = Pattern.compile("(\\D?)\\d{2}(\\D?)");

			// Number: Month
			patternMonth1 = Pattern.compile("(\\D?)\\d{1,2}(\\D?)");

			// Number: Day
			patternDay1 = Pattern.compile("(\\D?)\\d{1,2}(\\D?)");

			// Number: HH:mm:ss
			patternTime1 = Pattern.compile("(\\D?)\\d{1,2}:\\d{1,2}:\\d{1,2}(\\D?)");

			// Number: Hour
			patternHour1 = Pattern.compile("(\\D?)\\d{1,2}(\\D?)");

			// Number: Minute
			patternMinute1 = Pattern.compile("(\\D?)\\d{1,2}(\\D?)");

			// Number: Second
			patternSecond1 = Pattern.compile("(\\D?)\\d{1,2}(\\D?)");

			// Number: Millisecond
			patternMillisecond1 = Pattern.compile("([.:])\\d{1}(\\D?)");
			patternMillisecond2 = Pattern.compile("([.:])\\d{2}(\\D?)");
			patternMillisecond3 = Pattern.compile("([.:])\\d{3}(\\D?)");

			// Number: ISO8601
			patternT1 = Pattern.compile("([\\sdhH]+)T([\\sdhH]+)");

			patternsInited = true;
		}
	}

	/**
	 * @param text
	 * @param defaultValue
	 *
	 * @return 如果 date 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 defaultValue
	 */
	public static Date dateOf(String text, Date defaultValue) {
		// TODO 1、根据输入长度快速列出可能的格式进行解析
		// TODO 2、如果没有可能格式或者可能格式无效，那么开始智能识别（当前方法）
		// TODO 3、如果智能识别解析成功，则把输入长度和格式信息缓存起来，供前面第1步快速调用
		// TODO 4、替代同步机制，格式实例使用HashMap缓存在线程本地变量(TheadLocal)中
		// TODO 5、为了线程安全，继承 SimpleDateFormat 并记录当前线程ID，运行时检查ID
		if (NullUtil.isNotNull(text)) {
			Locale locale = Locale.getDefault();

			if (!patternsInited) {
				initPatterns();
			}

			// 替换单词"UTC"为"GMT"，两者标准相同，前者是“科学”名称，后者是“民间”名称
			// "\\bUTC\\b"
			String data = patternTimezone1.matcher(text).replaceAll("GMT");
			String format = data;
			int englishCount = 0;
			int defaultCount = 0;
			boolean month = false;
			boolean amPm = false;
			boolean time = false;
			Matcher matcher;

			// 识别文本月份
			if ((matcher = patternMonth4en.matcher(data)).find()) {
				format = patternMonth4en.matcher(format).replaceAll("MMMM");
				++englishCount;
				month = true;
			} else if ((matcher = patternMonth3en.matcher(data)).find()) {
				format = patternMonth3en.matcher(format).replaceAll("MMM");
				++englishCount;
				month = true;
			} else if ((matcher = patternMonth4.matcher(data)).find()) {
				format = patternMonth4.matcher(format).replaceAll("MMMM");
				++defaultCount;
				month = true;
			} else if ((matcher = patternMonth3.matcher(data)).find()) {
				format = patternMonth3.matcher(format).replaceAll("MMM");
				++defaultCount;
				month = true;
			}

			// 识别文本星期
			if ((matcher = patternWeek4en.matcher(data)).find()) {
				format = patternWeek4en.matcher(format).replaceAll("EEEE");
				++englishCount;
			} else if ((matcher = patternWeek3en.matcher(data)).find()) {
				format = patternWeek3en.matcher(format).replaceAll("EEE");
				++englishCount;
			} else if ((matcher = patternWeek4.matcher(data)).find()) {
				format = patternWeek4.matcher(format).replaceAll("EEEE");
				++defaultCount;
			} else if ((matcher = patternWeek3.matcher(data)).find()) {
				format = patternWeek3.matcher(format).replaceAll("EEE");
				++defaultCount;
			}

			// 识别 AM/PM 标记
			if ((matcher = patternAmPm2en.matcher(data)).find()) {
				format = patternAmPm2en.matcher(format).replaceAll("a");
				++englishCount;
				amPm = true;
			} else if ((matcher = patternAmPm2.matcher(data)).find()) {
				format = patternAmPm2.matcher(format).replaceAll("a");
				++defaultCount;
				amPm = true;
			}

			if (englishCount > 0) {
				if (defaultCount > 0) {
					throw new RuntimeException("试图解析具有混合语言的日期时间：" + text);
				}
				locale = Locale.ENGLISH;
			}

			// 开始识别时区信息（兼容多种格式）
			if ((matcher = patternTimezone11.matcher(data)).find()) {
				// 按 General time zone 时区格式
				// "\\bGMT([-+]\\d{1,2}):(\\d{2})\\b"
				format = patternTimezone11.matcher(format).replaceAll("z");
			} else if ((matcher = patternTimezone12.matcher(data)).find()) {
				// 按 RFC 822 4-digit 时区格式
				// "\\bGMT([-+]\\d{4})\\b"
				data = matcher.replaceAll("$1");
				format = patternTimezone12.matcher(format).replaceAll("Z");
			} else if ((matcher = patternTimezone13.matcher(data)).find()) {
				// 按 RFC 822 4-digit 时区格式
				// "\\bGMT([-+])(\\d{3})\\b"
				data = matcher.replaceAll("$10$2");
				format = patternTimezone13.matcher(format).replaceAll("Z");
			} else if ((matcher = patternTimezone21.matcher(data)).find()) {
				// 按 General time zone 时区格式
				// "\\bGMT\\s+(\\d{1,2}):(\\d{2})\\b"
				data = matcher.replaceAll("GMT+$1$2");
				format = patternTimezone21.matcher(format).replaceAll("z");
			} else if ((matcher = patternTimezone22.matcher(data)).find()) {
				// 按 RFC 822 4-digit 时区格式
				// "\\bGMT\\s+(\\d{4})\\b"
				data = matcher.replaceAll("+$1");
				format = patternTimezone22.matcher(format).replaceAll("Z");
			} else if ((matcher = patternTimezone23.matcher(data)).find()) {
				// 按 RFC 822 4-digit 时区格式，3位时分前面补0
				// "\\bGMT\\s+(\\d{3})\\b"
				data = matcher.replaceAll("+0$1");
				format = patternTimezone23.matcher(format).replaceAll("Z");
			} else if ((matcher = patternTimezone31.matcher(data)).find()) {
				// 按 General time zone 时区格式
				// "(:\\d+\\s+)([a-zA-Z]\\w*/\\w+)\\b"
				format = patternTimezone31.matcher(format).replaceAll("$1zzzz");
			} else if ((matcher = patternTimezone32.matcher(data)).find()) {
				// 按 General time zone 时区格式
				// "(:\\d+\\s+)([a-zA-Z]\\w*)\\b"
				format = patternTimezone32.matcher(format).replaceAll("$1z");
			} else if ((matcher = patternTimezone41.matcher(data)).find()) {
				// 按 General time zone 时区格式
				// "([-+]\\d{1,2}):(\\d{2})\\b"
				data = matcher.replaceAll("GMT$1:$2");
				format = patternTimezone41.matcher(format).replaceAll("z");
			} else if ((matcher = patternTimezone42.matcher(data)).find()) {
				// 按 RFC 822 4-digit 时区格式
				// "(:\\d+\\s*)[-+]\\d{4}\\b"
				format = patternTimezone42.matcher(format).replaceAll("$1Z");
			} else if ((matcher = patternTimezone43.matcher(data)).find()) {
				// 按 RFC 822 4-digit 时区格式，3位时分前面补0
				// "(:\\d+\\s*)([-+])(\\d{3})\\b"
				data = matcher.replaceAll("$1$20$3");
				format = patternTimezone43.matcher(format).replaceAll("$1Z");
			} else if ((matcher = patternTimezone51.matcher(data)).find()) {
				// SimpleDateFormat 无法识别2位时区，按 General time zone 时区格式补足时区分钟"00"
				// "(:\\d+\\s*)([-+]\\d{1,2})\\b"
				data = matcher.replaceAll("$1GMT$2:00");
				format = patternTimezone51.matcher(format).replaceAll("$1z");
			} else if ((matcher = patternTimezone61.matcher(data)).find()) {
				// SimpleDateFormat 无法识别UTC时区，按 RFC 822 4-digit 时区格式替换"Z"为"+0000"
				// "(\\d{1,2})Z\\b"
				data = matcher.replaceAll("$1+0000");
				format = patternTimezone61.matcher(format).replaceAll("$1Z");
			}

			// 开始识别日期（先整体快速识别）
			if ((matcher = patternDate1.matcher(format)).find()) {
				format = matcher.replaceFirst("$1yyyy$2MM$3dd$4");
			} else if ((matcher = patternDate2.matcher(format)).find()) {
				format = matcher.replaceFirst("$1dd$2MM$3yyyy$4");
			} else {
				// 开始识别4或2位年
				if ((matcher = patternYear4.matcher(format)).find()) {
					// "(\\D?)\\d{4}(\\D?)"
					format = matcher.replaceFirst("$1yyyy$2");
				} else {
					// "(\\D?)\\d{2}(\\D?)"
					format = patternYear2.matcher(format).replaceFirst("$1yy$2");
				}

				if (!month) {
					// 开始识别1~2位月
					// "(\\D?)\\d{1,2}(\\D?)"
					format = patternMonth1.matcher(format).replaceFirst("$1MM$2");
				}

				// 开始识别1~2位日
				// "(\\D?)\\d{1,2}(\\D?)"
				format = patternDay1.matcher(format).replaceFirst("$1dd$2");
			}

			// 开始识别时间（先整体快速识别）
			if ((matcher = patternTime1.matcher(format)).find()) {
				format = matcher.replaceFirst(amPm ? "$1hh:mm:ss$2" : "$1HH:mm:ss$2");
				time = true;
			} else {
				// 开始识别时分秒
				if ((matcher = patternHour1.matcher(format)).find()) {
					// 开始识别1~2位时
					// "(\\D?)\\d{1,2}(\\D?)"
					format = matcher.replaceFirst(amPm ? "$1hh$2" : "$1HH$2");

					// 开始识别1~2位分
					// "(\\D?)\\d{1,2}(\\D?)"
					format = patternMinute1.matcher(format).replaceFirst("$1mm$2");

					// 开始识别1~2位秒
					// "(\\D?)\\d{1,2}(\\D?)"
					format = patternSecond1.matcher(format).replaceFirst("$1ss$2");

					time = true;
				}
			}

			// 若有时间则开始识别毫秒等
			if (time) {
				if ((matcher = patternMillisecond3.matcher(format)).find()) {
					// 开始识别3位毫秒
					// "([.:])\\d{3}(\\D?)"
					format = matcher.replaceFirst("$1SSS$2");
				} else if ((matcher = patternMillisecond2.matcher(format)).find()) {
					// 开始识别2位毫秒
					// "([.:])\\d{2}(\\D?)"
					format = matcher.replaceFirst("$1SS$2");
				} else if ((matcher = patternMillisecond1.matcher(format)).find()) {
					// 开始识别1位毫秒
					// "([.:])\\d{1}(\\D?)"
					format = matcher.replaceFirst("$1S$2");
				}

				// 开始识别 ISO8601 格式的"T"
				// "([\\sdhH]+)T([\\sdhH]+)"
				format = patternT1.matcher(format).replaceFirst("$1'T'$2");
			}

			try {
				DateFormat dateFormat = new SimpleDateFormat(format, locale);
				try {
					return dateFormat.parse(data);
				} catch (ParseException e) {
					return defaultValue;
				}
			} catch (IllegalArgumentException e) {
				StringBuilder sb = new StringBuilder(128);
				sb.append("text=\"").append(text);
				if (!text.equals(data)) {
					sb.append("\", data=\"").append(data);
				}
				sb.append("\", format=\"").append(format);
				sb.append("\", 建议符合ISO8601标准格式：yyyy-MM-dd[[ T]HH:mm:ss[.SSS][Z]]");
				throw new RuntimeException(sb.toString(), e);
			}
		}
		return null;
	}

	/**
	 * @param text
	 *
	 * @return 如果 date 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回null
	 */
	public static Date dateOf(String text) {
		return dateOf(text, null);
	}

	/**
	 * @param year 1-9999
	 * @param month 1-12
	 * @param day 1-31
	 *
	 * @return
	 */
	public static Date dateOf(int year, int month, int day) {
		Calendar cal = new GregorianCalendar(year, month - MONTH_OFFSET, 1);
		if (day > 1) {
			int max = cal.getActualMaximum(DATE);
			if (day > max) {
				day = max;
			}
			cal.set(DATE, day);
		}
		return cal.getTime();
	}

	/**
	 * 将指定时区时间转换为标准UTC时间
	 *
	 * @param date
	 * @param tz
	 *
	 * @return
	 */
	public static Date convertDate(long date, TimeZone tz) {
		final int zoneOffset = tz.getOffset(date - tz.getRawOffset());
		return new Date(date - zoneOffset);
	}

	/**
	 * 将JVM运行时默认时区时间转换为标准UTC时间
	 *
	 * @param date
	 *
	 * @return
	 */
	public static Date convertDate(long date) {
		return convertDate(date, TimeZone.getDefault());
	}

	/**
	 * 将标准UTC时间转换为指定时区时间
	 *
	 * @param date
	 * @param tz
	 *
	 * @return
	 */
	public static long convertDate(Date date, TimeZone tz) {
		long time = date.getTime();
		time += tz.getOffset(time);
		return time;
	}

	/**
	 * 将标准UTC时间转换为JVM运行时默认时区时间
	 *
	 * @param date
	 *
	 * @return
	 */
	public static long convertDate(Date date) {
		return convertDate(date, TimeZone.getDefault());
	}

	/**
	 * 将指定时区时间（OA日期）转换为标准UTC时间
	 * <p>
	 * OA日期格式存储采用浮点数，相关工具或语言有: Lotus 1-2-3, Excel, Access, Delphi etc.
	 *
	 * @param date
	 * @param tz
	 *
	 * @return
	 */
	public static Date convertOADate(double date, TimeZone tz) {
		long time = Math.round(date * DOUBLE_DAY_MILLISECONDS);
		if (time < OA_OFFSET_MILLISECONDS_BUG) {
			time += DAY_MILLISECONDS;
		}
		time -= OA_OFFSET_MILLISECONDS_1970;
		return convertDate(time, tz);
	}

	/**
	 * 将JVM运行时默认时区时间（OA日期）转换为标准UTC时间
	 * <p>
	 * OA日期格式存储采用浮点数，相关工具或语言有: Lotus 1-2-3, Excel, Access, Delphi etc.
	 *
	 * @param date
	 *
	 * @return
	 */
	public static Date convertOADate(double date) {
		return convertOADate(date, TimeZone.getDefault());
	}

	/**
	 * 将标准UTC时间转换为指定时区时间（OA日期）
	 * <p>
	 * OA日期格式存储采用浮点数，相关工具或语言有: Lotus 1-2-3, Excel, Access, Delphi etc.
	 *
	 * @param date
	 *
	 * @return
	 */
	public static double convertOADate(Date date, TimeZone tz) {
		long time = convertDate(date, tz) + OA_OFFSET_MILLISECONDS_1970;
		if (time < OA_OFFSET_MILLISECONDS_BUG) {
			time -= DAY_MILLISECONDS;
		}
		// 计算不会溢出: date(4637-11-25 23:59:59.999) = long(84190809599999) = double(999999.999999989)
		return Math.round(time * (DOUBLE_ROUND_SCALE9 / DOUBLE_DAY_MILLISECONDS)) / DOUBLE_ROUND_SCALE9;
	}

	/**
	 * 将标准UTC时间转换为JVM运行时默认时区时间（OA日期）
	 * <p>
	 * OA日期格式存储采用浮点数，相关工具或语言有: Lotus 1-2-3, Excel, Access, Delphi etc.
	 *
	 * @param date
	 *
	 * @return
	 */
	public static double convertOADate(Date date) {
		return convertOADate(date, TimeZone.getDefault());
	}

	/**
	 * @return
	 */
	public static Calendar today() {
		Calendar cal = new GregorianCalendar();
		return cal;
	}

	/**
	 * @param date
	 *
	 * @return
	 */
	public static Calendar today(Date date) {
		if (null != date) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			return cal;
		} else {
			return null;
		}
	}

	/**
	 * @param date
	 *
	 * @return
	 */
	public static Calendar today(int date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDate(date));
		return cal;
	}

	/**
	 * @return
	 */
	public static final Date getDate() {
		return new Date();
	}

	/**
	 * @param cal
	 *
	 * @return
	 */
	public static final Date getDate(Calendar cal) {
		if (null != cal) {
			return cal.getTime();
		} else {
			return null;
		}
	}

	/**
	 * @return 返回最小日期，一般是：1年1月1日
	 */
	public static Calendar getMinCalendar() {
		return MIN_CALENDAR;
	}

	/**
	 * @return 返回最大日期，一般是：9999年12月31日
	 */
	public static Calendar getMaxCalendar() {
		return MAX_CALENDAR;
	}

	/**
	 * @return 返回最小日期，一般是：1年1月1日
	 */
	public static Date getMinDate() {
		return MIN_DATE;
	}

	/**
	 * @return 返回最大日期，一般是：9999年12月31日
	 */
	public static Date getMaxDate() {
		return MAX_DATE;
	}

	/**
	 * @return 返回最小日期（整数型），一般是：0
	 */
	public static int getMinDateInt() {
		return MIN_DATE_INT;
	}

	/**
	 * @return 返回最大日期（整数型），一般是：99999999
	 */
	public static int getMaxDateInt() {
		return MAX_DATE_INT;
	}

	/**
	 * @param date
	 *
	 * @return
	 */
	public static Date getDate(int date) {
		if (date >= MAX_DATE_INT) {
			return MAX_DATE;
		} else if (date <= MIN_DATE_INT) {
			return MIN_DATE;
		} else {
			int year = getYear(date);
			int month = getMonth(date);
			int day = getDay(date);

			if (month < 1) {
				month = 1;
			} else if (month > 12) {
				month = 12;
			}

			return dateOf(year, month, day);
		}
	}

	/**
	 * 复制日期对象，不含时间
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static Calendar cloneDate(Calendar cal) {
		if (null != cal) {
			int year = cal.get(YEAR);
			int month = cal.get(MONTH);
			int date = cal.get(DATE);
			cal = new GregorianCalendar();
			cal.clear();
			cal.set(year, month, date);
			return cal;
		} else {
			return null;
		}
	}

	/**
	 * 复制日期对象，不含时间
	 *
	 * @param date
	 *
	 * @return
	 */
	public static Date cloneDate(Date date) {
		return getDate(cloneDate(today(date)));
	}

	/**
	 * 复制日期对象，包含时间
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static Calendar cloneTime(Calendar cal) {
		if (null != cal) {
			Calendar clone = new GregorianCalendar();
			clone.setTimeInMillis(cal.getTimeInMillis());
			return clone;
		} else {
			return null;
		}
	}

	/**
	 * 复制日期对象，包含时间
	 *
	 * @param date
	 *
	 * @return
	 */
	public static Date cloneTime(Date date) {
		if (null != date) {
			return new Date(date.getTime());
		} else {
			return null;
		}
	}

	/**
	 * 清除日期对象中的时间
	 *
	 * @param cal
	 *
	 * @return
	 */
	public static void clearTime(Calendar cal) {
		if (null != cal) {
			int year = cal.get(YEAR);
			int month = cal.get(MONTH);
			int date = cal.get(DATE);
			cal.clear();
			cal.set(year, month, date);
		}
	}

	/**
	 * 清除日期对象中的时间
	 *
	 * @param date
	 *
	 * @return
	 */
	public static void clearTime(Date date) {
		if (null != date) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			int year = cal.get(YEAR);
			int month = cal.get(MONTH);
			int day = cal.get(DATE);
			cal.clear();
			cal.set(year, month, day);
			date.setTime(cal.getTimeInMillis());
		}
	}

	/**
	 * @param cal1
	 * @param cal2
	 *
	 * @return 返回两个日期相差年份数，完全忽略月份、日期和时分秒
	 */
	public static int yearsAfter(Calendar cal1, Calendar cal2) {
		if (null == cal1) {
			throw new IllegalArgumentException("cal1");
		}
		if (null == cal2) {
			throw new IllegalArgumentException("cal2");
		}
		int years1 = getYear(cal1);
		int years2 = getYear(cal2);
		return (years2 - years1);
	}

	/**
	 * @param date1
	 * @param date2
	 *
	 * @return 返回两个日期相差年份数，完全忽略月份、日期和时分秒
	 */
	public static int yearsAfter(Date date1, Date date2) {
		if (null == date1) {
			throw new IllegalArgumentException("date1");
		}
		if (null == date2) {
			throw new IllegalArgumentException("date2");
		}
		return yearsAfter(today(date1), today(date2));
	}

	/**
	 * @param cal1
	 * @param cal2
	 *
	 * @return 返回两个日期相差月份数，完全忽略日期和时分秒
	 */
	public static int monthsAfter(Calendar cal1, Calendar cal2) {
		if (null == cal1) {
			throw new IllegalArgumentException("cal1");
		}
		if (null == cal2) {
			throw new IllegalArgumentException("cal2");
		}
		int months1 = getYear(cal1) * 12 + getMonth(cal1);
		int months2 = getYear(cal2) * 12 + getMonth(cal2);
		return (months2 - months1);
	}

	/**
	 * @param date1
	 * @param date2
	 *
	 * @return 返回两个日期相差月份数，完全忽略日期和时分秒
	 */
	public static int monthsAfter(Date date1, Date date2) {
		if (null == date1) {
			throw new IllegalArgumentException("date1");
		}
		if (null == date2) {
			throw new IllegalArgumentException("date2");
		}
		return monthsAfter(today(date1), today(date2));
	}

	/**
	 * @param cal1
	 * @param cal2
	 *
	 * @return 返回两个日期相差天数，完全忽略时分秒
	 */
	public static int daysAfter(Calendar cal1, Calendar cal2) {
		if (null == cal1) {
			throw new IllegalArgumentException("cal1");
		}
		if (null == cal2) {
			throw new IllegalArgumentException("cal2");
		}
		cal1 = cloneDate(cal1);
		cal2 = cloneDate(cal2);
		long ms = cal2.getTimeInMillis() - cal1.getTimeInMillis();
		return (int) (ms / (86400 * 1000));
	}

	/**
	 * @param date1
	 * @param date2
	 *
	 * @return 返回两个日期相差天数，完全忽略时分秒
	 */
	public static int daysAfter(Date date1, Date date2) {
		if (null == date1) {
			throw new IllegalArgumentException("date1");
		}
		if (null == date2) {
			throw new IllegalArgumentException("date2");
		}
		return daysAfter(today(date1), today(date2));
	}

	/**
	 * @param date1
	 * @param date2
	 *
	 * @return
	 */
	public static long secondsAfter(Date date1, Date date2) {
		if (null == date1) {
			throw new IllegalArgumentException("date1");
		}
		if (null == date2) {
			throw new IllegalArgumentException("date2");
		}
		long ms = date2.getTime() - date1.getTime();
		return (ms / 1000);
	}

	/**
	 * 计算距离1970-01-01的秒数
	 *
	 * @param date
	 *
	 * @return
	 */
	public static long secondsAfter(Date date) {
		if (null == date) {
			throw new IllegalArgumentException("date");
		}
		return secondsAfter(DATE_1970_01_01, date);
	}

	/**
	 * 计算距离1970-01-01的秒数，有效时间范围应该在 1901-12-13 20:45:52 和 2038-01-19 03:14:07 之间
	 *
	 * @return 超出范围则返回相当于Integer.MAX_VALUE或Integer.MIN_VALUE
	 */
	public static int seconds() {
		int result;
		long ss = secondsAfter(DATE_1970_01_01, getDate());
		if (ss > Integer.MAX_VALUE) {
			result = Integer.MAX_VALUE;
		} else if (ss < Integer.MIN_VALUE) {
			result = Integer.MIN_VALUE;
		} else {
			result = (int) ss;
		}
		return result;
	}

	/**
	 * @param cal
	 * @param chinese
	 *
	 * @return
	 */
	public static String dateName(Calendar cal, boolean chinese) {
		if (chinese) {
			return toChineseDate(cal);
		} else {
			return toISO8601Date(cal);
		}
	}

	/**
	 * @param date
	 * @param chinese
	 *
	 * @return
	 */
	public static String dateName(Date date, boolean chinese) {
		if (chinese) {
			return toChineseDate(date);
		} else {
			return toISO8601Date(date);
		}
	}

	/**
	 * @param cal
	 *
	 * @return
	 */
	public static String dateName(Calendar cal) {
		if (null != cal) {
			if (MAX_CALENDAR.compareTo(cal) > 0 && MIN_CALENDAR.compareTo(cal) < 0) {
				return toChineseDate(cal);
			}
		}
		return ("");
	}

	/**
	 * @param date
	 *
	 * @return
	 */
	public static String dateName(Date date) {
		if (null != date) {
			if (MAX_DATE.compareTo(date) > 0 && MIN_DATE.compareTo(date) < 0) {
				return toChineseDate(date);
			}
		}
		return ("");
	}

	/**
	 * @param begin
	 * @param end
	 *
	 * @return
	 */
	public static String dateName(Calendar begin, Calendar end) {
		if (null != begin && begin.equals(end)) {
			return dateName(begin);
		}
		StringBuffer sb = new StringBuffer(32);
		FieldPosition fp = new FieldPosition(0);
		if (null != begin) {
			if (MAX_CALENDAR.compareTo(begin) > 0 && MIN_CALENDAR.compareTo(begin) < 0) {
				toChineseDate(begin, sb, fp);
			}
		}
		sb.append(TO);
		if (null != end) {
			if (MAX_CALENDAR.compareTo(end) > 0 && MIN_CALENDAR.compareTo(end) < 0) {
				toChineseDate(end, sb, fp);
			}
		}
		return sb.toString();
	}

	/**
	 * @param begin
	 * @param end
	 *
	 * @return
	 */
	public static String dateName(Date begin, Date end) {
		if (null != begin && begin.equals(end)) {
			return dateName(begin);
		}
		StringBuffer sb = new StringBuffer(32);
		FieldPosition fp = new FieldPosition(0);
		if (null != begin) {
			if (MAX_DATE.compareTo(begin) > 0 && MIN_DATE.compareTo(begin) < 0) {
				toChineseDate(begin, sb, fp);
			}
		}
		sb.append(TO);
		if (null != end) {
			if (MAX_DATE.compareTo(end) > 0 && MIN_DATE.compareTo(end) < 0) {
				toChineseDate(end, sb, fp);
			}
		}
		return sb.toString();
	}

	/**
	 * @param sb
	 * @param date
	 *
	 * @return
	 */
	public static StringBuilder dateName(StringBuilder sb, int date) {
		if (date >= 1000000) { // yyyyMMdd
			sb.append(getYear(date)).append(NIAN).append(getMonth(date)).append(YUE).append(getDay(date)).append(RI);
		} else if (date >= 10000) { // yyyyMM
			sb.append(getYear(date)).append(NIAN).append(getMonth(date)).append(YUE);
		} else { // yyyy
			sb.append(date).append(NIAN);
		}
		return sb;
	}

	/**
	 * @param date
	 *
	 * @return
	 */
	public static String dateName(int date) {
		if (date > MIN_DATE_INT && date < MAX_DATE_INT) {
			StringBuilder sb = new StringBuilder();
			return dateName(sb, date).toString();
		}
		return ("");
	}

	/**
	 * @param begin
	 * @param end
	 *
	 * @return
	 */
	public static String dateName(int begin, int end) {
		if (begin == end) {
			return dateName(begin);
		}
		StringBuilder sb = new StringBuilder(32);
		if (begin > MIN_DATE_INT && begin < MAX_DATE_INT) {
			dateName(sb, begin);
		}
		sb.append(TO);
		if (end > MIN_DATE_INT && end < MAX_DATE_INT) {
			dateName(sb, end);
		}
		return sb.toString();
	}

	/**
	 * @param cal
	 *
	 * @return yyyyMMdd
	 */
	public static int getDateInt(Calendar cal) {
		if (null != cal) {
			if (MAX_CALENDAR.compareTo(cal) <= 0) {
				return MAX_DATE_INT;
			} else if (MIN_CALENDAR.compareTo(cal) >= 0) {
				return MIN_DATE_INT;
			}
			return cal.get(YEAR) * 10000 + (cal.get(MONTH) + MONTH_OFFSET) * 100 + cal.get(DATE);
		}
		return MIN_DATE_INT;
	}

	/**
	 * @param date
	 *
	 * @return yyyyMMdd
	 */
	public static int getDateInt(Date date) {
		return getDateInt(today(date));
	}

	/**
	 * @return yyyyMMdd
	 */
	public static int getDateInt() {
		return getDateInt(today());
	}

	/**
	 * @return
	 */
	public static int getDay() {
		return getDay(today());
	}

	/**
	 * @param cal
	 *
	 * @return
	 */
	public static int getDay(Calendar cal) {
		return cal.get(DATE);
	}

	/**
	 * @param date
	 *
	 * @return
	 */
	public static int getDay(Date date) {
		return getDay(today(date));
	}

	/**
	 * @param date
	 *
	 * @return
	 */
	public static int getDay(int date) {
		if (date >= 1000000) { // yyyyMMdd
			date = date % 100;
		} else { // yyyyMM, yyyy
			date = 0;
		}
		return date;
	}

	/**
	 * @return yyyyDDD {@code DDD} 指示当前年中的天数。一年中第一天的值为 1。
	 */
	public static int getYearDay() {
		return getYearDay(today());
	}

	/**
	 * @param cal
	 *
	 * @return yyyyDDD {@code DDD} 指示当前年中的天数。一年中第一天的值为 1。
	 */
	public static int getYearDay(Calendar cal) {
		return cal.get(YEAR) * 1000 + cal.get(DAY_OF_YEAR);
	}

	/**
	 * @param date
	 *
	 * @return yyyyDDD {@code DDD} 指示当前年中的天数。一年中第一天的值为 1。
	 */
	public static int getYearDay(Date date) {
		return getYearDay(today(date));
	}

	/**
	 * @return yyyyMM
	 */
	public static int getYearMonth() {
		return getYearMonth(today());
	}

	/**
	 * @param cal
	 *
	 * @return yyyyMM
	 */
	public static int getYearMonth(Calendar cal) {
		return cal.get(YEAR) * 100 + (cal.get(MONTH) + MONTH_OFFSET);
	}

	/**
	 * @param date
	 *
	 * @return yyyyMM
	 */
	public static int getYearMonth(Date date) {
		return getYearMonth(today(date));
	}

	/**
	 * @param date
	 *
	 * @return yyyyMM
	 */
	public static int getYearMonth(int date) {
		if (date >= 1000000) { // yyyyMMdd
			date /= 100;
		} else if (date <= 9999) { // yyyy
			date *= 100;
			++date; // 默认为1月
		}
		return date;
	}

	/**
	 * 获取当前时间的季度的第一个月
	 *
	 * @param date
	 *
	 * @return yyyyMM
	 */
	public static int getYearQuarter(Date date) {
		Date firstDate = firstDay(date, "quarter", 0);
		return getYearMonth(firstDate);
	}

	/**
	 * @return
	 */
	public static int getMonth() {
		return getMonth(today());
	}

	/**
	 * @param cal
	 *
	 * @return
	 */
	public static int getMonth(Calendar cal) {
		return cal.get(MONTH) + MONTH_OFFSET;
	}

	/**
	 * @param date
	 *
	 * @return
	 */
	public static int getMonth(Date date) {
		return getMonth(today(date));
	}

	/**
	 * @param date
	 *
	 * @return
	 */
	public static int getMonth(int date) {
		if (date >= 1000000) { // yyyyMMdd
			date = (date / 100) % 100;
		} else if (date >= 10000) { // yyyyMM
			date = date % 100;
		} else { // yyyy
			date = 0;
		}
		return date;
	}

	/**
	 * @return yyyy
	 */
	public static int getYear() {
		return getYear(today());
	}

	/**
	 * @param cal
	 *
	 * @return yyyy
	 */
	public static int getYear(Calendar cal) {
		return cal.get(YEAR);
	}

	/**
	 * @param date
	 *
	 * @return yyyy
	 */
	public static int getYear(Date date) {
		return getYear(today(date));
	}

	/**
	 * @param date
	 *
	 * @return
	 */
	public static int getYear(int date) {
		if (date >= 1000000) { // yyyyMMdd
			date = date / 10000;
		} else if (date >= 10000) { // yyyyMM
			date = date / 100;
		}
		return date;
	}

	/**
	 * @param cal
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 */
	private static void InternalDateAdd(Calendar cal, String fieldName, int amount) {
		int field = toField(fieldName);
		if (field == QUARTER) {
			cal.add(MONTH, amount * 3);
		} else if (field != INVALID) {
			cal.add(field, amount);
		}
	}

	/**
	 * @param cal
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return
	 */
	public static Calendar dateAdd(Calendar cal, String fieldName, int amount) {
		if (null != cal) {
			cal = cloneTime(cal);
			InternalDateAdd(cal, fieldName, amount);
		}
		return cal;
	}

	/**
	 * @param date
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return
	 */
	public static Date dateAdd(Date date, String fieldName, int amount) {
		if (null != date) {
			Calendar cal = today(date);
			InternalDateAdd(cal, fieldName, amount);
			return cal.getTime();
		}
		return date;
	}

	/**
	 * @param date
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return
	 */
	public static int dateAdd(int date, String fieldName, int amount) {
		int deep;
		if (date >= 1000000) { // yyyyMMdd
			deep = DATE;
		} else if (date >= 10000) { // yyyyMM
			deep = MONTH;
			date = date * 100 + 1;
		} else { // yyyy
			deep = YEAR;
			date = date * 10000 + 101;
		}
		date = getDateInt(dateAdd(getDate(date), fieldName, amount));
		if (DATE == deep) {
		} else if (MONTH == deep) {
			date /= 100;
		} else if (YEAR == deep) {
			date /= 10000;
		}
		return date;
	}

	/**
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return
	 */
	public static Date dateAdd(String fieldName, int amount) {
		Calendar cal = today();
		cal = dateAdd(cal, fieldName, amount);
		return cal.getTime();
	}

	/**
	 * 获取在加减年月日之后的最初日期，不含时间
	 * <p>
	 * 若指定年份或月份，则设置为加减年月之后那年或者那月的第一天；否则设置为加减日期后的那天。
	 *
	 * @param cal
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 */
	private static void internalFirstDay(Calendar cal, String fieldName, int amount) {
		int field = toField(fieldName);
		if (INVALID != field) {
			if (QUARTER == field) {
				amount *= 3;
				amount -= (cal.get(MONTH) % 3);
				cal.add(MONTH, amount);
				cal.set(DATE, 1);
			} else {
				cal.add(field, amount);
				if (YEAR == field) {
					cal.set(MONTH, 0);
					cal.set(DATE, 1);
				} else if (MONTH == field) {
					cal.set(DATE, 1);
				} else if (DAY_OF_WEEK == field) {
					int offset = cal.get(DAY_OF_WEEK) - SUNDAY;
					// 参照 ISO 8601 标准，周一为每周第一天
					if (offset > 0) {
						// 周一至周六，减去偏移1，起始周一为0，减去此值即为至本周一的偏移量
						offset = -(offset - 1);
					} else {
						// 周日至本周一的偏移量为-6
						offset = -6;
					}
					cal.add(DATE, offset);
				}
			}
		}
	}

	/**
	 * 获取在加减年月日之后的最初日期，不含时间
	 *
	 * @param cal
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return 若指定年份或月份，则返回加减年月之后那年或者那月的第一天；否则是返回加减日期后的那天。
	 */
	public static Calendar firstDay(Calendar cal, String fieldName, int amount) {
		if (null != cal) {
			cal = cloneDate(cal);
			internalFirstDay(cal, fieldName, amount);
		}
		return cal;
	}

	/**
	 * 获取在加减年月日之后的最初日期，不含时间
	 *
	 * @param date
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return 若指定年份或月份，则返回加减年月之后那年或者那月的第一天；否则是返回加减日期后的那天。
	 */
	public static Date firstDay(Date date, String fieldName, int amount) {
		if (null != date) {
			Calendar cal = today(date);
			clearTime(cal);
			internalFirstDay(cal, fieldName, amount);
			return cal.getTime();
		}
		return date;
	}

	/**
	 * @param date
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return
	 */
	public static int firstDay(int date, String fieldName, int amount) {
		int deep;
		if (date >= 1000000) { // yyyyMMdd
			deep = DATE;
		} else if (date >= 10000) { // yyyyMM
			deep = MONTH;
			date = date * 100 + 1;
		} else { // yyyy
			deep = YEAR;
			date = date * 10000 + 101;
		}
		date = getDateInt(firstDay(getDate(date), fieldName, amount));
		if (DATE == deep) {
		} else if (MONTH == deep) {
			date /= 100;
		} else if (YEAR == deep) {
			date /= 10000;
		}
		return date;
	}

	/**
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return
	 */
	public static Date firstDay(String fieldName, int amount) {
		Calendar cal = today();
		cal = firstDay(cal, fieldName, amount);
		return cal.getTime();
	}

	/**
	 * 获取在加减年月日之后的最后日期，包含时间(23:59:59)
	 * <p>
	 * 若指定年份或月份，则设置为加减年月之后那年或者那月的最后一天及最后秒；否则设置为加减日期后的那天最后秒。
	 *
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 */
	private static void internalLastDay(Calendar cal, String fieldName, int amount) {
		int field = toField(fieldName);
		if (INVALID != field) {
			if (QUARTER == field) {
				amount *= 3;
				amount += 2 - (cal.get(MONTH) % 3);
				cal.add(MONTH, amount);
				cal.set(DATE, 1);
				cal.add(MONTH, 1);
			} else {
				cal.add(field, amount);
				if (YEAR == field) {
					cal.set(DATE, 1);
					cal.set(MONTH, 0);
					cal.add(YEAR, 1);
				} else if (MONTH == field) {
					cal.set(DATE, 1);
					cal.add(MONTH, 1);
				} else if (DAY_OF_WEEK == field) {
					int offset = cal.get(DAY_OF_WEEK) - SUNDAY;
					// 参照 ISO 8601 标准，周一为每周第一天
					if (offset > 0) {
						// 周一至周六，减去偏移1，起始周一为0，7再减去此值即为至下周一偏移量
						offset = 7 - (offset - 1);
					} else {
						// 周日至下周一的偏移量为1
						offset = 1;
					}
					cal.add(DATE, offset);
				} else {
					cal.add(DATE, 1);
				}
			}
		} else {
			cal.add(DATE, 1);
		}
		cal.add(SECOND, -1);
	}

	/**
	 * 获取在加减年月日之后的最后日期，包含时间(23:59:59)
	 *
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return 若指定年份或月份，则返回加减年月之后那年或者那月的最后一天及最后秒；否则是返回加减日期后的那天最后秒。
	 */
	public static Calendar lastDay(Calendar cal, String fieldName, int amount) {
		if (null != cal) {
			cal = cloneDate(cal);
			internalLastDay(cal, fieldName, amount);
		}
		return cal;
	}

	/**
	 * 获取在加减年月日之后的最后日期，包含时间(23:59:59)
	 *
	 * @param date
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return 若指定年份或月份，则返回加减年月之后那年或者那月的最后一天及最后秒；否则是返回加减日期后的那天最后秒。
	 */
	public static Date lastDay(Date date, String fieldName, int amount) {
		if (null != date) {
			Calendar cal = today(date);
			clearTime(cal);
			internalLastDay(cal, fieldName, amount);
			return cal.getTime();
		}
		return date;
	}

	/**
	 * @param date
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return
	 */
	public static int lastDay(int date, String fieldName, int amount) {
		int deep;
		if (date >= 1000000) { // yyyyMMdd
			deep = DATE;
		} else if (date >= 10000) { // yyyyMM
			deep = MONTH;
			date = date * 100 + 1;
		} else { // yyyy
			deep = YEAR;
			date = date * 10000 + 101;
		}
		date = getDateInt(lastDay(getDate(date), fieldName, amount));
		if (DATE == deep) {
		} else if (MONTH == deep) {
			date /= 100;
		} else if (YEAR == deep) {
			date /= 10000;
		}
		return date;
	}

	/**
	 * @param fieldName <ul>
	 * <li>年： {@code "yy"}, {@code "yyyy"}, {@code "year"}</li>
	 * <li>月： {@code "mm"}, {@code "month"}</li>
	 * <li>日： {@code "dd"}, {@code "day"}, {@code "date"}</li>
	 * <li>时： {@code "hh"}, {@code "hour"}</li>
	 * <li>分： {@code "mi"}, {@code "minute"}</li>
	 * <li>秒： {@code "ss"}, {@code "second"}</li>
	 * <li>毫秒： {@code "ms"}, {@code "millisecond"}</li>
	 * <li>星期： {@code "wk"}, {@code "week"}</li>
	 * <li>季度： {@code "qq"}, {@code "quarter"}</li>
	 * </ul>
	 * @param amount
	 *
	 * @return
	 */
	public static Date lastDay(String fieldName, int amount) {
		Calendar cal = today();
		cal = lastDay(cal, fieldName, amount);
		return cal.getTime();
	}

	/**
	 * ISO8601格式化处理
	 *
	 * @param cal
	 * @param ellipsis 省略日期时间中“00:00:00.000”等时间为0的部分
	 *
	 * @return
	 */
	public static String toString(Calendar cal, boolean ellipsis) {
		String value;
		if (null == cal) {
			value = null;
		} else {
			value = DateUtil.toISO8601DateTimeLong(cal);
			if (ellipsis && value.endsWith(" 00:00:00.000")) {
				value = value.substring(0, value.length() - 13);
			}
		}
		return value;
	}

	/**
	 * 根据 java.sql.Timestamp， java.sql.Date， java.sql.Time 和 java.util.Date类型分别做ISO8601格式化处理
	 *
	 * @param date
	 * @param ellipsis 省略日期时间中“00:00:00.000”等时间为0的部分
	 *
	 * @return
	 */
	public static String toString(Date date, boolean ellipsis) {
		String value;
		if (null == date) {
			value = null;
		} else if (date instanceof java.sql.Timestamp) {
			value = date.toString();
			if (ellipsis && value.endsWith(" 00:00:00.0")) {
				value = value.substring(0, value.length() - 11);
			}
		} else if (date instanceof java.sql.Date) {
			value = date.toString();
		} else if (date instanceof java.sql.Time) {
			value = date.toString();
		} else {
			value = DateUtil.toISO8601DateTimeLong(date);
			if (ellipsis && value.endsWith(" 00:00:00.000")) {
				value = value.substring(0, value.length() - 13);
			}
		}
		return value;
	}

	/**
	 * ISO8601格式化处理
	 *
	 * @return
	 */
	public static String toString(Calendar cal) {
		return toString(cal, true);
	}

	/**
	 * 根据 java.sql.Timestamp， java.sql.Date， java.sql.Time 和 java.util.Date类型分别做ISO8601格式化处理
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String toString(Date date) {
		return toString(date, true);
	}

	/**
	 * 返回当前周的第一天
	 *
	 * @param currentDate
	 *
	 * @return
	 */
	public static Date getFirstDay(Calendar currentDate) {
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);

		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return currentDate.getTime();
	}

	/**
	 * 返回当前周的周后一天
	 *
	 * @param currentDate
	 *
	 * @return
	 */
	public static Date getLastDay(Calendar currentDate) {
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);

		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return currentDate.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param smdate 较小的时间
	 * @param bdate 较大的时间
	 *
	 * @return 相差天数
	 *
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * Parse a date <code>Date</code> whether is a holiday
	 *
	 * @param date
	 *
	 * @return A <code>Boolean</code> parsed from {@link #isHoliday(Calendar)}
	 */
	public static boolean isHoliday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return isHoliday(calendar);
	}

	/**
	 * Parse a date <code>String</code> whether is a holiday using the format
	 * "<code>yyyy-MM-dd</code>"
	 *
	 * @param time yyyy-MM-dd
	 *
	 * @return A <code>Boolean</code> parsed from {@link #isHoliday(Calendar)}
	 */
	public static boolean isHoliday(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return isHoliday(calendar);
		} catch (ParseException e) {
			throw new RuntimeException("解析日期失败", e);
		}
	}

	/**
	 * Parse a date whether or not is a holiday
	 * <p>
	 * The method support the date from 2010 to 2017,and continually update the version.
	 *
	 * @param cal <p>
	 * The calendar will be parsed
	 *
	 * @return A <code>Boolean</code> result describing whether the day is a holiday
	 *
	 * @author HuYu
	 */
	public static boolean isHoliday(Calendar cal) {
		int year = cal.get(Calendar.YEAR);
		long holidayInfo;
		if (year < MIN_OF_YEAR || year > MAX_OF_YEAR) {
			holidayInfo = 0xfffffffffffL;
		} else {
			holidayInfo = HOLIDAY_INFOS[year - MIN_OF_YEAR];
		}
		/**
		 * 1、阳历节日判断
		 */
		long jan = holidayInfo >> 41;
		long apr = (holidayInfo >> 32) & 0x1ff;
		long may = (holidayInfo >> 29) & 7;
		long oct = (holidayInfo >> 21) & 0xff;
		long dec = (holidayInfo >> 19) & 3;
		int month = cal.get(Calendar.MONTH);
		if (month < 0 || month > 11) {
			throw new RuntimeException("月份应该在1-12范围之内");
		}
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String calStr = String.format("日期：%d-%d-%d 为{}休假。", year, month + 1, day);
		StringBuilder fest = new StringBuilder();
		boolean result = false;
		switch (month) {
		case 0:
			if ((day <= 3) && ((8 >> (day - 1)) & jan) != 0) {
				fest.append("元旦节、");
				result = true;
			}
			break;
		case 3:
			if ((day > 1 && day < 9) && ((64 >> (day - 2)) & (apr >> 2)) != 0) {
				fest.append("清明节、");
				result = true;
			} else if ((day >= 29 && day <= 30) && ((2 >> (day - 29)) & (apr << 7)) != 0) {
				fest.append("劳动节调休、");
				result = true;
			}
			break;
		case 4:
			if ((day <= 3) && ((8 >> (day - 1)) & may) != 0) {
				fest.append("劳动节、");
				result = true;
			}
			break;
		case 9:
			if ((day < 9) && ((128 >> (day - 1)) & oct) != 0) {
				fest.append("国庆节、");
				result = true;
			}
			break;
		case 11:
			if ((day >= 30) && ((2 >> (day - 30)) & dec) != 0) {
				fest.append("元旦节调休、");
				result = true;
			}
		}
		/**
		 * 2、阴历节日判断
		 */
		long janL = (holidayInfo >> 12) & 0x7f;
		long mayL = (holidayInfo >> 7) & 0x1f;
		long augL = (holidayInfo >> 2) & 0x1f;
		long decL = holidayInfo & 2;
		Lunar lunar = new Lunar(cal);
		month = lunar.getMonth();
		day = lunar.getDay();
		switch (month) {
		case 1:
			if ((day <= 7) && ((64 >> (day - 1)) & janL) != 0) {
				fest.append("春节、");
				result = true;
			}
			break;
		case 5:
			if ((day > 2 && day < 8) && ((16 >> (day - 3)) & mayL) != 0) {
				fest.append("端午节、");
				result = true;
			}
			break;
		case 8:
			if ((day > 12 && day < 18) && ((16 >> (day - 13)) & augL) != 0) {
				fest.append("中秋节、");
				result = true;
			}
			break;
		case 12:
			if ((day >= 29) && ((2 >> (day - 29)) & decL) != 0) {
				fest.append("除夕、");
				result = true;
			}
		}
		if (result) {
			fest.setLength(fest.length() - 1);
		} else {
			calStr = calStr.replace("为{}", "无");
		}
		return result;
	}

}
