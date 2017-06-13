package com.tomato.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since 2017年6月13日 下午6:06:44
 * @version $Id$
 * @author CaiBo
 *
 */
public class XIDUtil {

	private static final Pattern PATTERN_MOBILE_VALID = Pattern.compile("^1[34578]\\d{9}$");
	private static final Pattern PATTERN_MOBILE_GROUP = Pattern.compile("(^|\\D+)(1[34578]\\d[ ]?\\d{4}[ ]?\\d{4})(\\D+|$)");
	public static final int MAX_NSRSBH_15 = 15;
	public static final int MAX_NSRSBH_18 = 18;
	public static final HashMap<Character, Integer> NSRSBH_CODE_15;
	public static final HashMap<Character, Integer> NSRSBH_CODE_18;
	public static final int[] W_15 = { 3, 7, 9, 10, 5, 8, 4, 2 };

	static {
		NSRSBH_CODE_15 = new HashMap<>();
		NSRSBH_CODE_15.put('0', 0);
		NSRSBH_CODE_15.put('1', 1);
		NSRSBH_CODE_15.put('2', 2);
		NSRSBH_CODE_15.put('3', 3);
		NSRSBH_CODE_15.put('4', 4);
		NSRSBH_CODE_15.put('5', 5);
		NSRSBH_CODE_15.put('6', 6);
		NSRSBH_CODE_15.put('7', 7);
		NSRSBH_CODE_15.put('8', 8);
		NSRSBH_CODE_15.put('9', 9);
		NSRSBH_CODE_15.put('A', 10);
		NSRSBH_CODE_15.put('B', 11);
		NSRSBH_CODE_15.put('C', 12);
		NSRSBH_CODE_15.put('D', 13);
		NSRSBH_CODE_15.put('E', 14);
		NSRSBH_CODE_15.put('F', 15);
		NSRSBH_CODE_15.put('G', 16);
		NSRSBH_CODE_15.put('H', 17);
		NSRSBH_CODE_15.put('I', 18);
		NSRSBH_CODE_15.put('J', 19);
		NSRSBH_CODE_15.put('K', 20);
		NSRSBH_CODE_15.put('L', 21);
		NSRSBH_CODE_15.put('M', 22);
		NSRSBH_CODE_15.put('N', 23);
		NSRSBH_CODE_15.put('O', 24);
		NSRSBH_CODE_15.put('P', 25);
		NSRSBH_CODE_15.put('Q', 26);
		NSRSBH_CODE_15.put('R', 27);
		NSRSBH_CODE_15.put('S', 28);
		NSRSBH_CODE_15.put('T', 29);
		NSRSBH_CODE_15.put('U', 30);
		NSRSBH_CODE_15.put('V', 31);
		NSRSBH_CODE_15.put('W', 32);
		NSRSBH_CODE_15.put('X', 33);
		NSRSBH_CODE_15.put('Y', 34);
		NSRSBH_CODE_15.put('Z', 35);

		NSRSBH_CODE_18 = new HashMap<>();
		NSRSBH_CODE_18.put('0', 0);
		NSRSBH_CODE_18.put('1', 1);
		NSRSBH_CODE_18.put('2', 2);
		NSRSBH_CODE_18.put('3', 3);
		NSRSBH_CODE_18.put('4', 4);
		NSRSBH_CODE_18.put('5', 5);
		NSRSBH_CODE_18.put('6', 6);
		NSRSBH_CODE_18.put('7', 7);
		NSRSBH_CODE_18.put('8', 8);
		NSRSBH_CODE_18.put('9', 9);
		NSRSBH_CODE_18.put('A', 10);
		NSRSBH_CODE_18.put('B', 11);
		NSRSBH_CODE_18.put('C', 12);
		NSRSBH_CODE_18.put('D', 13);
		NSRSBH_CODE_18.put('E', 14);
		NSRSBH_CODE_18.put('F', 15);
		NSRSBH_CODE_18.put('G', 16);
		NSRSBH_CODE_18.put('H', 17);
		NSRSBH_CODE_18.put('J', 18);
		NSRSBH_CODE_18.put('K', 19);
		NSRSBH_CODE_18.put('L', 20);
		NSRSBH_CODE_18.put('M', 21);
		NSRSBH_CODE_18.put('N', 22);
		NSRSBH_CODE_18.put('P', 23);
		NSRSBH_CODE_18.put('Q', 24);
		NSRSBH_CODE_18.put('R', 25);
		NSRSBH_CODE_18.put('T', 26);
		NSRSBH_CODE_18.put('U', 27);
		NSRSBH_CODE_18.put('W', 28);
		NSRSBH_CODE_18.put('X', 29);
		NSRSBH_CODE_18.put('Y', 30);
	}

	// DO NOT CHANGE IT!
	private static final String KEY_UNION_ID = "UnionId";
	private static final String KEY_OPEN_ID = "OpenId";
	private static final String KEY_PLATFORM_ID = "PlatformId";
	private static final String KEY_PROVIDER_ID = "ProviderId";
	private static final String KEY_MOBILE_ID = "MobileId";
	private static final String KEY_EMAIL_ID = "EmailId";
	private static final String KEY_PIN_ID = "PinId";
	private static final String KEY_NSRSBH_ID = "NsrsbhId";
	private static final String KEY_NSRMC_ID = "NsrmcId";

	// Prevent instantiation
	private XIDUtil() {
		super();
	}

	/**
	 * @param userId
	 * @param platformId
	 * @param providerId
	 * @return
	 */
	public static String getUnionId(long userId, String platformId, String providerId) {
		return IDUtil.getBase58ID(KEY_UNION_ID, userId, platformId, providerId);
	}

	/**
	 * @param userId
	 * @param platformId
	 * @param providerId
	 * @param appId
	 * @return
	 */
	public static String getOpenId(long userId, String platformId, String providerId, String appId) {
		String unionId = getUnionId(userId, platformId, providerId);
		return IDUtil.getBase58ID(KEY_OPEN_ID, userId, unionId, appId);
	}

	/**
	 * @param userId
	 * @param unionId
	 * @param appId
	 * @return
	 */
	public static String getOpenId(long userId, String unionId, String appId) {
		return IDUtil.getBase58ID(KEY_OPEN_ID, userId, unionId, appId);
	}

	/**
	 * @param vendorName
	 * @param platformName
	 * @return
	 */
	public static String getPlatformId(String vendorName, String platformName) {
		return IDUtil.getBase58ID(KEY_PLATFORM_ID, vendorName, platformName);
	}

	/**
	 * @param vendorName
	 * @param providerName
	 * @return
	 */
	public static String getProviderId(String vendorName, String providerName) {
		return IDUtil.getBase58ID(KEY_PROVIDER_ID, vendorName, providerName);
	}

	/**
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		if (null != mobile && mobile.length() >= 11) {
			return PATTERN_MOBILE_VALID.matcher(mobile).matches();
		}
		return false;
	}

	/**
	 * 清洗手机号码
	 * 
	 * @param mobile
	 *            手机号码
	 * @return
	 */
	public static String getMobile(String mobile) {
		if (null != mobile) {
			mobile = HanziUtil.symbol2Ascii(mobile);
			Matcher matcher = PATTERN_MOBILE_GROUP.matcher(mobile);
			if (matcher.find()) {
				mobile = matcher.group(2);
				if (mobile.indexOf(' ') > 0) {
					mobile = StringUtil.replaceAll(mobile, " ", "");
				}
			} else {
				mobile = StringUtil.rightTrim(mobile);
				mobile = StringUtil.right(mobile, 11);
			}
		}
		return mobile;
	}

	/**
	 * 手机号码经清洗后产生UUID并Base58编码
	 * 
	 * @param mobile
	 *            手机号码
	 * @return
	 */
	public static String getMobileId(String mobile) {
		if (null != mobile) {
			mobile = IDUtil.getBase58ID(KEY_MOBILE_ID, getMobile(mobile));
		}
		return mobile;
	}

	/**
	 * 清洗电子邮件地址
	 * 
	 * @param email
	 *            电子邮件地址
	 * @return
	 */
	public static String getEmail(String email) {
		if (null != email) {
			email = HanziUtil.symbol2Ascii(email);
		}
		return email;
	}

	/**
	 * 电子邮件地址经清洗后产生UUID并Base58编码
	 * 
	 * @param email
	 *            电子邮件地址
	 * @return
	 */
	public static String getEmailId(String email) {
		if (null != email) {
			email = IDUtil.getBase58ID(KEY_EMAIL_ID, getEmail(email));
		}
		return email;
	}

	/**
	 * 清洗及升级公民身份号码（居民身份证号码）
	 * 
	 * @param pin
	 *            公民身份号码（居民身份证号码）
	 * @return
	 */
	public static String getPin(String pin) {
		if (null != pin) {
			pin = HanziUtil.cleanCode(pin, true);
			pin = PINUtil.upgrade(pin);
		}
		return pin;
	}

	/**
	 * 公民身份号码（居民身份证号码）经清洗及升级后产生UUID并Base58编码
	 * 
	 * @param pin
	 *            公民身份号码（居民身份证号码）
	 * @return
	 */
	public static String getPinId(String pin) {
		if (null != pin) {
			pin = IDUtil.getBase58ID(KEY_PIN_ID, getPin(pin));
		}
		return pin;
	}

	/**
	 * 清洗纳税人识别号
	 * 
	 * @param nsrsbh
	 *            纳税人识别号
	 * @return
	 */
	public static String getNsrsbh(String nsrsbh) {
		if (null != nsrsbh) {
			nsrsbh = HanziUtil.cleanCode(nsrsbh, true);
		}
		return nsrsbh;
	}

	/**
	 * 纳税人识别号和行政区划数字代码经清洗后产生UUID并Base58编码
	 * 
	 * @param xzqhszDm
	 *            行政区划数字代码
	 * @param nsrsbh
	 *            纳税人识别号
	 * @return
	 */
	public static String getNsrsbhId(String xzqhszDm, String nsrsbh) {
		if (null == xzqhszDm) {
			throw new IllegalArgumentException(xzqhszDm);
		}
		if (null == nsrsbh) {
			throw new IllegalArgumentException(nsrsbh);
		}
		xzqhszDm = HanziUtil.cleanCode(xzqhszDm, true);
		// 如果是城区，则统一归到城市，只截取前四位代码
		if ((NumberUtil.parseInt(xzqhszDm) % 100) <= 20) {
			xzqhszDm = StringUtil.left(xzqhszDm, 4);
		}
		nsrsbh = getNsrsbh(nsrsbh);
		return IDUtil.getBase58ID(KEY_NSRSBH_ID, xzqhszDm, nsrsbh);
	}

	/**
	 * 清洗纳税人名称
	 * 
	 * @param nsrmc
	 *            纳税人名称
	 * @return
	 */
	public static String getNsrmc(String nsrmc) {
		if (null != nsrmc) {
			nsrmc = HanziUtil.cleanCode(nsrmc);
		}
		return nsrmc;
	}

	/**
	 * 纳税人名称经清洗后产生UUID并Base58编码
	 * 
	 * @param nsrmc
	 *            纳税人名称
	 * @return
	 */
	public static String getNsrmcId(String nsrmc) {
		if (null != nsrmc) {
			nsrmc = IDUtil.getBase58ID(KEY_NSRMC_ID, getNsrmc(nsrmc));
		}
		return nsrmc;
	}

	public static boolean isNsrsbh(String nsrsbh) {
		if (nsrsbh == null || !nsrsbh.equals(getNsrsbh(nsrsbh))) {
			return false;
		}
		if (nsrsbh.length() == MAX_NSRSBH_18) {
			return isNsrsbh18(nsrsbh);
		} else if (nsrsbh.length() == MAX_NSRSBH_15) {
			return isNsrsbh15(nsrsbh);
		}
		return false;
	}

	/**
	 * @param nsrsbh
	 * @return
	 */
	private static boolean isNsrsbh15(String nsrsbh) {
		if (nsrsbh.length() != MAX_NSRSBH_15) {
			return false;
		}
		int mod = 0;
		char last = nsrsbh.charAt(nsrsbh.length() - 1);
		nsrsbh = nsrsbh.substring(6, nsrsbh.length() - 1);
		char[] charArray = nsrsbh.toCharArray();
		Integer lastCode15 = NSRSBH_CODE_15.get(last);
		if (lastCode15 == null) {
			return false;
		} else if (lastCode15 == 33) {// 为X时
			lastCode15 = 10;
		} else if (lastCode15 == 0) {
			lastCode15 = 11;
		}
		for (int i = 0; i < charArray.length; i++) {
			Integer c = NSRSBH_CODE_15.get(charArray[i]);
			if (c == null) {
				return false;
			}
			mod += (W_15[i] * c);
		}
		return lastCode15 == 11 - mod % 11;
	}

	/**
	 * @param nsrsbh
	 * @return
	 */
	private static boolean isNsrsbh18(String nsrsbh) {
		if (nsrsbh.length() != MAX_NSRSBH_18) {
			return false;
		}
		int mod = 0;
		char last = nsrsbh.charAt(nsrsbh.length() - 1);
		nsrsbh = nsrsbh.substring(0, nsrsbh.length() - 1);
		char[] charArray = nsrsbh.toCharArray();
		Integer lastCode18 = NSRSBH_CODE_18.get(last);
		if (lastCode18 == null) {
			return false;
		}
		for (int i = 0; i < charArray.length; i++) {
			Integer c = NSRSBH_CODE_18.get(charArray[i]);
			if (c == null) {
				return false;
			}
			mod += (Math.pow(3, i) % 31 * c);
		}
		return lastCode18 == 31 - mod % 31;
	}

}
