package com.tomato.util;

import com.tomato.util.checkdigits.CheckDigit;
import com.tomato.util.checkdigits.CheckISO7064Mod11_2;

public final class PINUtil {
	public static final int MAX_PRCPIN_G1 = 15;
	public static final int MAX_PRCPIN_G2 = 18;
	private static final int MAX_PRCPIN_G1_YEAR2_OFFSET = 6;
	private static final int MAX_PRCPIN_G1_YEAR2_VALUE = 19;
	private static final CheckDigit pin = new CheckISO7064Mod11_2();

	// Prevent instantiation
	private PINUtil() {
		super();
	}

	/**
	 * 
	 * @param digits
	 * @return
	 */
	public static boolean verify(String digits) {
		if (null != digits && digits.length() == MAX_PRCPIN_G2) {
			return pin.verify(digits);
		}
		return false;
	}

	/**
	 * 
	 * @param digits
	 * @return
	 */
	public static String upgrade(String digits) {
		if (null != digits) {
			int len = digits.length();
			if (len > MAX_PRCPIN_G2) {
				throw new RuntimeException("第二代公民身份号码标准长度是18位！");
			} else if (len > 0 && len != MAX_PRCPIN_G2) {
				if (len != MAX_PRCPIN_G1) {
					throw new RuntimeException("第一代公民身份号码标准长度是15位！");
				}
				StringBuilder sb = new StringBuilder(MAX_PRCPIN_G2);
				sb.append(digits.substring(0, MAX_PRCPIN_G1_YEAR2_OFFSET));
				sb.append(MAX_PRCPIN_G1_YEAR2_VALUE);
				sb.append(digits.substring(MAX_PRCPIN_G1_YEAR2_OFFSET));
				digits = pin.encode(sb.toString());
			}
		}
		return digits;
	}

	/**
	 * 
	 * @param digits
	 * @return
	 */
	public static String convertNative(String digits) {
		return StringUtil.convertNativeLetterOrDigit(digits, false, true, true, MAX_PRCPIN_G2);
	}

	/**
	 * 
	 * @param digits1
	 * @param digits2
	 * @return
	 */
	public static boolean visualEquals(String digits1, String digits2) {
		if (null == digits1 || digits1.isEmpty()) {
			return (null == digits2 || digits2.isEmpty());
		} else if (null == digits2 || digits2.isEmpty()) {
			return false;
		} else if (digits1.equals(digits2)) {
			return true;
		}

		/*
		 * 清洗空白字符、转换本地字符、转成大写后再比较
		 */
		try {
			digits1 = convertNative(digits1);
			digits2 = convertNative(digits2);
			if (digits1.equals(digits2)) {
				return true;
			}
		} catch (Exception ignore) {
			return false;
		}

		/*
		 * 第一代升级为第二代号码后再比较
		 */
		int len1 = digits1.length();
		int len2 = digits2.length();
		if (len1 == MAX_PRCPIN_G1 && len2 == MAX_PRCPIN_G2) {
			digits1 = upgrade(digits1);
			return digits1.equals(digits2);
		} else if (len1 == MAX_PRCPIN_G2 && len2 == MAX_PRCPIN_G1) {
			digits2 = upgrade(digits2);
			return digits1.equals(digits2);
		}
		return false;
	}

}
