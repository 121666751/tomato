/**
 * Copyright(C) 2012 Fugle Technology Co. Ltd. All rights reserved.
 * 
 */
package com.tomato.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Lunar {
	private static final int MIN_OF_YEAR = 1900;
	private static final int MAX_OF_YEAR = 2099;
	private static final int MIN_DAY_OF_MONTH = 29;
	private static final int SHIFT_OFFSET_OF_YEAR = 26;
	private static final int MASK_OFFSET_OF_YEAR = 0x3f; // after shift right
	private static final int MASK_TEST_OF_BIG = 0x2000;
	private static final int SHIFT_LEAP_OF_YEAR = 9;
	private static final int MASK_LEAP_OF_YEAR = 0x0f; // after shift right
	private static final int MASK_TOTAL_OF_YEAR = 0x01ff;
	private static final int[] LUNAR_INFO;
	private static final char NIAN = '年';
	private static final char YUE = '月';
	private static final char RUN = '闰';
	private static final String ZHENG = "正";
	private static final String[] SHU;
	private static final char[] CHU1;
	private static final char[] CHU2;
	private int year;
	private int month;
	private int day;
	private boolean leap;

	static {
		/**
		 * 1900－2099年阴历资料格式：
		 * 
		 * <pre>
		 *  f e d c | b a  9  8 |  7 6 5 4 | 3 2 1 0 | f e d c | b a 9 8 | 7 6 5 4 | 3 2 1 0
		 *          |     12 11 | 10 9 8 7 | 6 5 4 3 | 2 1 M m | m m m d | d d d d | d d d d
		 * (迟于阳历天数) (26 -               14->) (0x2000) ( 0x1e00) (             0x01ff)
		 * 迟于阳历天数: 相同年份，阴历春节迟于阳历元旦的天数，最小20天，最大50天，1900年阴历春节迟于阳历元旦30天。
		 * 测试是否大月: (y >> m) & 0x2000
		 * 返回闰哪个月: (y >> 9) & 0x0f
		 * 测试闰月大月: y & 0x2000
		 * 返回年总天数: y & 0x01ff
		 * </pre>
		 */
		LUNAR_INFO = new int[] { 0x7af49180, 0xc5d48162, 0x9ba94163, 0x72ca8b7f, 0xb992c162, 0x8aa6c163, 0x62aaa980, 0xad5a8162, 0x82d64163, 0x56ea8580,
				0xa1d48162, 0x77694d80, 0xc2c94162, 0x9292c162, 0x6652eb80, 0xb0ab4162, 0x855ac163, 0x596d4580, 0xa76a4163, 0x7fa4af80, 0xcba48162, 0x9b494162,
				0x6f4b4b80, 0xba958162, 0x8cad8162, 0x5eb56981, 0xadb50162, 0x83aa4163, 0x5bd28580, 0xa3a48162, 0x75a98d7f, 0xbd4ac162, 0x9295c163, 0x6655ab80,
				0xb2d68163, 0x89b50162, 0x5dd86780, 0xa5d24162, 0x7ac4ef80, 0xc6a4c162, 0x994ac162, 0x6946ed80, 0xb6ab4163, 0x8d5a8162, 0x63696981, 0xaee90162,
				0x82d24162, 0x5752c580, 0xa2a54162, 0x72ab4f80, 0xbd4d8162, 0x92ab4163, 0x6ab2ab80, 0xb16c8162, 0x87694163, 0x5fa8a780, 0xab528162, 0x7965517f,
				0xc2a5c163, 0x99558162, 0x6d5d4d80, 0xb6b54163, 0x8db48162, 0x61d54980, 0xafa94163, 0x81928161, 0x5193c780, 0x9ea6c163, 0x76b68f80, 0xbd5a8162,
				0x92da4163, 0x6aec8b80, 0xb6d48162, 0x86c94162, 0x5acac980, 0xa692c162, 0x7aaad180, 0xc0ab4162, 0x955b4163, 0x6d6a6d80, 0xbb6a4163, 0x8f648162,
				0x63a54980, 0xaf494162, 0x83935580, 0xca958162, 0x9cad8162, 0x70bd4d80, 0xbdb54163, 0x93aa4163, 0x6bd48b80, 0xb7a48162, 0x8b498162, 0x594b877f,
				0xa295c163, 0x7ab59180, 0xc4d68162, 0x95b54163, 0x6eda4b80, 0xb9d24162, 0x8da4c162, 0x5ea6c980, 0xa94ac162, 0x7e96c163, 0x56ab8580, 0x9d5a8162,
				0x73754f81, 0xc2e90162, 0x96d24162, 0x6754cb80, 0xb2a54162, 0x854b4162, 0x59574980, 0xa2ad4163, 0x7aea9380, 0xc5748162, 0x9b694163, 0x6fa2ad80,
				0xbb528162, 0x8f254162, 0x62a78980, 0xa9558162, 0x7ead4163, 0x56b68580, 0xa1b48162, 0x71d94d80, 0xbdc94162, 0x9192c162, 0x6595cb80, 0xaf2ac163,
				0x85568162, 0x595b8780, 0xa6da4163, 0x7bd49780, 0xc6d48162, 0x9ac94162, 0x6f42ed80, 0xb692c162, 0x892ac162, 0x5caecb80, 0xa96b4163, 0x7eda8163,
				0x576a8580, 0xa3648162, 0x77a94f80, 0xbf494162, 0x92954162, 0x66936b80, 0xb12d8162, 0x816d4163, 0x59b4a780, 0xa7b24163, 0x7fe49180, 0xc7a48162,
				0x9b498162, 0x6d45ad7f, 0xb695c163, 0x89358162, 0x5cd96980, 0xa9d54163, 0x81d24162, 0x51d2c780, 0x9da4c162, 0x72aacf80, 0xbd4ac162, 0x8e96c163,
				0x66ae8b80, 0xb15a8162, 0x86d94163, 0x5aea8980, 0xa6d28162, 0x7b655180, 0xc6a54162, 0x954b4162, 0x695b4d80, 0xb6ad4163, 0x8d6a8162, 0x5d754980,
				0xab694163, 0x83528162, 0x57934780, 0x9f258162, 0x73338f80, 0xbd558162, 0x92ad4163, 0x66b4ab80, 0xb1b48162, 0x87a94163, 0x5dca897f, 0xa192c162,
				0x75a5d180, 0xc12ac162, 0x9556c163, 0x6955ad80, 0xb6da8163, 0x8dd48162, 0x62e54980, 0xaac94162, 0x7ea2c162, 0x5293c580 };
		SHU = new String[] { "〇", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" };
		CHU1 = new char[] { '初', '十', '廿', '卅', '卌' };
		CHU2 = new char[] { '　', '初', '二', '三', '四' };
	}

	public Lunar() {
		setDate(new GregorianCalendar());
	}

	/**
	 * 
	 * @param cal
	 */
	public Lunar(Calendar cal) {
		setDate(cal);
	}

	/**
	 * 
	 * @param date
	 */
	public Lunar(Date date) {
		setDate(date);
	}

	/**
	 * 
	 * @param year
	 */
	public static void checkYear(int year) {
		if (year < MIN_OF_YEAR || year > MAX_OF_YEAR) {
			throw new IllegalArgumentException("尚未支持早于" + MIN_OF_YEAR + "年或迟于" + MAX_OF_YEAR + "年的阴历（农历）年。");
		}
	}

	/**
	 * 
	 * @param month
	 */
	public static void checkMonth(int month) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("月份应该在1-12范围之内");
		}
	}

	/**
	 * 
	 * @param year
	 */
	public static int getDaysOfYear(int year) {
		checkYear(year);
		return (LUNAR_INFO[year - MIN_OF_YEAR] & MASK_TOTAL_OF_YEAR);
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @param leap
	 * @return
	 */
	public static int getDaysOfMonth(int year, int month, boolean leap) {
		checkYear(year);
		checkMonth(month);
		int lunar = LUNAR_INFO[year - MIN_OF_YEAR];
		int days;
		if (leap) {
			int leapMonth = (lunar >> SHIFT_LEAP_OF_YEAR) & MASK_LEAP_OF_YEAR;
			if (month == leapMonth) {
				days = MIN_DAY_OF_MONTH;
				if ((lunar & MASK_TEST_OF_BIG) != 0) {
					++days;
				}
				return days;
			}
		}
		days = MIN_DAY_OF_MONTH;
		if (((lunar >> month) & MASK_TEST_OF_BIG) != 0) {
			++days;
		}
		return days;
	}

	/**
	 * 
	 * @param year
	 */
	public static int getLeapMonth(int year) {
		checkYear(year);
		int lunar = LUNAR_INFO[year - MIN_OF_YEAR];
		return (lunar >> SHIFT_LEAP_OF_YEAR) & MASK_LEAP_OF_YEAR;
	}

	/**
	 * 
	 * @param cal
	 */
	public void setDate(Calendar cal) {
		int year = cal.get(Calendar.YEAR);
		checkYear(year);
		int daysOfYear = cal.get(Calendar.DAY_OF_YEAR);
		// 取得当年阴历资料
		int lunar = LUNAR_INFO[year - MIN_OF_YEAR];
		// 迟于阳历天数: 相同年份，阴历春节迟于阳历元旦的天数
		int offset = (lunar >> SHIFT_OFFSET_OF_YEAR) & MASK_OFFSET_OF_YEAR;
		if (daysOfYear <= offset) {
			if (year <= MIN_OF_YEAR) {
				throw new IllegalArgumentException("尚未支持早于" + MIN_OF_YEAR + "年阴历（农历）春节之前的日期。");
			}
			// 早于迟于阳历天数
			--year;
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, 11); // 12 月
			cal.set(Calendar.DATE, 31); // 31 日
			// 加上上年总天数
			daysOfYear += cal.get(Calendar.DAY_OF_YEAR);
			// 重新取得上年阴历资料
			lunar = LUNAR_INFO[year - MIN_OF_YEAR];
			// 重新取得上年迟于阳历天数
			offset = (lunar >> SHIFT_OFFSET_OF_YEAR) & MASK_OFFSET_OF_YEAR;
		}
		daysOfYear -= offset; // 减去迟于阳历天数，从春节开始计算
		int leapMonth = (lunar >> SHIFT_LEAP_OF_YEAR) & MASK_LEAP_OF_YEAR;
		int month, daysOfMonth;
		boolean leap = false;
		for (month = 1; month <= 12; ++month) {
			daysOfMonth = MIN_DAY_OF_MONTH;
			if (((lunar >> month) & MASK_TEST_OF_BIG) != 0) {
				++daysOfMonth;
			}
			if (daysOfYear <= daysOfMonth) {
				break;
			}
			daysOfYear -= daysOfMonth;
			if (month == leapMonth) {
				daysOfMonth = MIN_DAY_OF_MONTH;
				if ((lunar & MASK_TEST_OF_BIG) != 0) {
					++daysOfMonth;
				}
				if (daysOfYear <= daysOfMonth) {
					leap = true;
					break;
				}
				daysOfYear -= daysOfMonth;
			}
		}
		this.year = year;
		this.month = month;
		this.day = daysOfYear;
		this.leap = leap;
	}

	/**
	 * 
	 * @param date
	 */
	private void setDate(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		setDate(cal);
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @return the leap
	 */
	public boolean isLeap() {
		return leap;
	}

	/**
	 * 
	 * @return
	 */
	public String getYearName() {
		StringBuilder sb = new StringBuilder();
		sb.append(SHU[year / 1000]).append(SHU[year / 100 % 10]).append(SHU[year / 10 % 10]).append(SHU[year % 10]);
		return sb.toString();
	}

	/**
	 * 
	 * @return
	 */
	public String getMonthName() {
		if (leap) {
			StringBuilder sb = new StringBuilder();
			sb.append(RUN).append(SHU[month]);
			return sb.toString();
		} else if (month > 1) {
			return SHU[month];
		} else {
			// 正月、十二月没有闰月
			return ZHENG;
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getDayName() {
		StringBuilder sb = new StringBuilder();
		int m = day / 10, n = day % 10;
		if (n != 0) {
			sb.append(CHU1[m]).append(SHU[n]);
		} else {
			sb.append(CHU2[m]).append(SHU[10]);
		}
		return sb.toString();
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getYearName()).append(NIAN).append(getMonthName()).append(YUE).append(getDayName());
		return sb.toString();
	}

	/**
	 * 
	 * @return
	 */
	public String toNongLi() {
		StringBuilder sb = new StringBuilder();
		sb.append("农历 ").append(getMonthName()).append(YUE).append(getDayName());
		return sb.toString();
	}

}
