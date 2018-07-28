package com.tomato.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tomato.util.checkdigits.CheckDigit;
import com.tomato.util.checkdigits.CheckGB11714Mod11_2;
import com.tomato.util.checkdigits.CheckGB32100Mod31_3;

public final class XIDUtil {

    private static final Pattern PATTERN_MOBILE_VALID = Pattern.compile("^1[34578]\\d{9}$");
    private static final Pattern PATTERN_MOBILE_GROUP = Pattern.compile("(^|\\D+)(1[34578]\\d[ ]?\\d{4}[ ]?\\d{4})(\\D+|$)");
    private static final String KEY_UNION_ID = "UnionId";
    private static final String KEY_OPEN_ID = "OpenId";
    private static final String KEY_PLATFORM_ID = "PlatformId";
    private static final String KEY_PROVIDER_ID = "ProviderId";
    private static final String KEY_MOBILE_ID = "MobileId";
    private static final String KEY_EMAIL_ID = "EmailId";
    private static final String KEY_PIN_ID = "PinId";
    private static final String KEY_NSRSBH_ID = "NsrsbhId";
    private static final String KEY_NSRMC_ID = "NsrmcId";

    private static final int MIN_NSRSBH_9 = 9;
    private static final int MAX_NSRSBH_15 = 15;
    private static final int MAX_NSRSBH_18 = 18;
    private static final CheckDigit ZZJGDM_CHECKER = new CheckGB11714Mod11_2();
    private static final CheckDigit SHXYDM_CHECKER = new CheckGB32100Mod31_3();
    private static Object PYJC_TRANSLATOR;

    private XIDUtil() {
        super();
    }

    /**
     * Gets union id.
     *
     * @param userId
     *         the user id
     * @param platformId
     *         the platform id
     * @param providerId
     *         the provider id
     *
     * @return union id
     */
    public static String getUnionId(long userId, String platformId, String providerId) {
        return IDUtil.getBase58ID(KEY_UNION_ID, userId, platformId, providerId);
    }

    /**
     * Gets open id.
     *
     * @param userId
     *         the user id
     * @param platformId
     *         the platform id
     * @param providerId
     *         the provider id
     * @param appId
     *         the app id
     *
     * @return open id
     */
    public static String getOpenId(long userId, String platformId, String providerId, String appId) {
        String unionId = getUnionId(userId, platformId, providerId);
        return IDUtil.getBase58ID(KEY_OPEN_ID, userId, unionId, appId);
    }

    /**
     * Gets open id.
     *
     * @param userId
     *         the user id
     * @param unionId
     *         the union id
     * @param appId
     *         the app id
     *
     * @return open id
     */
    public static String getOpenId(long userId, String unionId, String appId) {
        return IDUtil.getBase58ID(KEY_OPEN_ID, userId, unionId, appId);
    }

    /**
     * Gets platform id.
     *
     * @param vendorName
     *         the vendor name
     * @param platformName
     *         the platform name
     *
     * @return platform id
     */
    public static String getPlatformId(String vendorName, String platformName) {
        return IDUtil.getBase58ID(KEY_PLATFORM_ID, vendorName, platformName);
    }

    /**
     * Gets provider id.
     *
     * @param vendorName
     *         the vendor name
     * @param providerName
     *         the provider name
     *
     * @return provider id
     */
    public static String getProviderId(String vendorName, String providerName) {
        return IDUtil.getBase58ID(KEY_PROVIDER_ID, vendorName, providerName);
    }

    /**
     * Is mobile boolean.
     *
     * @param mobile
     *         the mobile
     *
     * @return boolean
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
     *         手机号码
     *
     * @return mobile
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
     *         手机号码
     *
     * @return mobile id
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
     *         电子邮件地址
     *
     * @return email
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
     *         电子邮件地址
     *
     * @return email id
     */
    public static String getEmailId(String email) {
        if (null != email) {
            email = IDUtil.getBase58ID(KEY_EMAIL_ID, getEmail(email));
        }
        return email;
    }

    /**
     * @param pin
     *         公民身份号码
     * @param allowFullWidth
     *         允许全角字符转换为半角
     *
     * @return
     */
    public static boolean isPin(String pin, boolean allowFullWidth) {
        if (null != pin && pin.length() >= PINUtil.MAX_PRCPIN_G1) {
            boolean result = PINUtil.verify(pin);
            if (!result && allowFullWidth) {
                pin = HanziUtil.cleanCode(pin, true, false, true);
                result = PINUtil.verify(pin);
            }
            return result;
        }
        return false;
    }

    /**
     * @param pin
     *         公民身份号码
     *
     * @return
     */
    public static boolean isPin(String pin) {
        return isPin(pin, false);
    }

    /**
     * 清洗及升级公民身份号码（居民身份证号码）
     *
     * @param pin
     *         公民身份号码（居民身份证号码）
     *
     * @return pin
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
     *         公民身份号码（居民身份证号码）
     *
     * @return pin id
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
     *         纳税人识别号
     *
     * @return nsrsbh
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
     *         行政区划数字代码
     * @param nsrsbh
     *         纳税人识别号
     *
     * @return nsrsbh id
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
     *         纳税人名称
     *
     * @return nsrmc
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
     *         纳税人名称
     *
     * @return nsrmc id
     */
    public static String getNsrmcId(String nsrmc) {
        if (null != nsrmc) {
            nsrmc = IDUtil.getBase58ID(KEY_NSRMC_ID, getNsrmc(nsrmc));
        }
        return nsrmc;
    }

    /**
     * 获取纳税人名称的拼音简称<br>
     *
     * @return
     */
    public static String getNsrmcPyjc(String nsrmc) {
        if (nsrmc == null) {
            return null;
        }
        char[] charArray = nsrmc.toCharArray();
        StringBuilder sb = new StringBuilder(nsrmc.length());
        // 根据《企业名称登记管理实施办法》第八条 企业名称应当使用符合国家规范的汉字，不得使用汉语拼音字母、阿拉伯数字。
        for (char c : charArray) {
            if (c > 127) {
                sb.append(c);
            }
        }
        if (sb.length() == 0) {
            return nsrmc.toUpperCase();
        }
        nsrmc = sb.toString();
        if (null == PYJC_TRANSLATOR) {
            synchronized (XIDUtil.class) {
                if (null == PYJC_TRANSLATOR) {
                    try {
                        PYJC_TRANSLATOR = BeanUtil.callMethod("com.ibm.icu.text.Transliterator", "getInstance",
                                "Han-Latin;NFD;[:Nonspacing Mark:] Remove;[:Punctuation:] Remove; Upper();");
                    } catch (Exception e) {
                        throw new RuntimeException("试图加载 ICU4J 汉语拼音转换器失败！", e);
                    }
                }
            }
        }
        String py = ((com.ibm.icu.text.Transliterator) PYJC_TRANSLATOR).transliterate(nsrmc);
        String[] arr = py.split(" ");
        sb.setLength(0);
        for (String s : arr) {
            if (s.length() > 0) {
                sb.append(s.charAt(0));
            }
        }
        return sb.toString();
    }

    /**
     * @param shxydm
     *         社会信用代码，兼容以组织机构代码为基础的 15 位号码
     *
     * @return
     */
    public static boolean isShxydm(String shxydm) {
        int len;
        if (null != shxydm && ((len = shxydm.length()) >= MAX_NSRSBH_15) && StringUtil.isAsciiAlphanumeric(shxydm, true, true)) {
            boolean result;
            try {
                if (len == MAX_NSRSBH_18) {
                    result = SHXYDM_CHECKER.verify(shxydm);
                } else if (len == MAX_NSRSBH_15) {
                    result = ZZJGDM_CHECKER.verify(shxydm.substring(6));
                } else {
                    result = false;
                }
            } catch (Exception ignore) {
                result = false;
            }
            return result;
        }
        return false;
    }

    /**
     * @param nsrsbh
     *         纳税人识别号
     * @param allowFullWidth
     *         允许全角字符转换为半角
     *
     * @return
     */
    public static boolean isNsrsbh(String nsrsbh, boolean allowFullWidth) {
        if (null != nsrsbh && (nsrsbh.length() >= MIN_NSRSBH_9)) {
            boolean result = isNsrsbh(nsrsbh);
            if (!result && allowFullWidth) {
                nsrsbh = HanziUtil.cleanCode(nsrsbh, true, false, true);
                result = isNsrsbh(nsrsbh);
            }
            return result;
        }
        return false;
    }

    /**
     * 校验是否为纳税人识别号，纳税人识别号可能是社会信用代码、公民身份号码、回乡证、通行证和护照等。
     *
     * @param nsrsbh
     *         调用者应保证该纳税人识别号是干净的（不应含有空格、特殊符号等）否者一律认为无效
     *
     * @return
     */
    public static boolean isNsrsbh(String nsrsbh) {
        int len;
        if (null != nsrsbh && ((len = nsrsbh.length()) >= MIN_NSRSBH_9) && StringUtil.isAsciiAlphanumeric(nsrsbh, true, false)) {
            // 办理税务登记纳税人 或 自然人登记纳税人
            boolean result = isShxydm(nsrsbh) || isPin(nsrsbh);
            if (!result) {
                /**
                 * 税总发[2013]41号 国家税务总局关于发布纳税人识别号代码标准的通知 <br/>
                 * SW 5-2013 纳税人识别号代码标准
                 */
                char ch = nsrsbh.charAt(0);
                switch (ch) {
                case 'L':
                    /**
                     * 临时登记纳税人（L）：以组织机构代码证、居民身份证、回乡证、通行证、护照等为有效身份证明的临时纳税的纳税人，其纳税人识别号由“L” “身份证件号码”组成。
                     */
                    result = (len >= (1 + 8));
                    break;
                case 'F':
                    /**
                     * 临时登记纳税人（F）：无常设机构的非居民企业的纳税人识别号由“F” “操作员所在税务机关的6位行政区划码” “3位纳税人居民身份所在国家或地区代码”
                     * “5位顺序码”组成。
                     */
                    result = (len >= (1 + 6 + 3 + 5));
                    break;
                case 'C':
                    /**
                     * 自然人登记纳税人（C）：以中国护照为有效身份证明的自然人，其纳税人识别号由“C” “4位年份码” “156” “9位顺序号” “1位校验码”组成。
                     */
                case 'W':
                    /**
                     * 自然人登记纳税人（W）：以外国护照为有效身份证明的自然人，其纳税人识别号由“W” “4位年份码” “3位国籍或地区数字码” “9位顺序号”
                     * “1位校验码”组成。
                     */
                    result = (len >= (1 + 4 + 3 + 9 + 1));
                    break;
                case 'J':
                    /**
                     * 自然人登记纳税人（J）： 以军官证、士兵证为有效身份证明的自然人，其纳税人识别号由“J” 行政区划码 8位顺序码。
                     */
                    result = (len >= (1 + 8));
                    break;
                case 'H': // 香港
                case 'M': // 澳门
                case 'T': // 台湾
                    /**
                     * 自然人登记纳税人（H/M/T）：以港澳居民来往内地通行证、台湾居民来往大陆通行证等为有效身份证明的港、澳、台地区自然人，其纳税人识别号由“
                     * 所在地区拉丁字母码”首位字母 “4位年份码（登记年份）” “3位国籍或地区数字码” “9位顺序号” “1位校验码”组成。
                     */
                    result = (len >= (1 + 4 + 3 + 9 + 1));
                    break;
                default:
                    /**
                     * 办理税务登记纳税人：
                     * 未取得统一社会信用代码的个体工商户以及以居民身份证、回乡证、通行证、护照等有效身份证明办理税务登记的纳税人，其纳税人识别号由“身份证件号码”+“2位顺序码
                     * ”组成。<br/>
                     * 一般证件至少8位，回乡证、通行证、护照通常都是字母开始，但是美国护照是全数字。
                     */
                    result = (len >= (8 + 2));
                    break;
                }
            }
            return result;
        }
        return false;
    }
}
