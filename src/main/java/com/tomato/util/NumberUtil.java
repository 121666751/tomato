package com.tomato.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.regex.Pattern;


public final class NumberUtil {
	public static final int LESS = -1;
	public static final int EQUAL = 0;
	public static final int GREATER = 1;
	public static final int LONG_SIZE = 64;
	public static final InvalidNumber INVALID_NUMBER = new InvalidNumber();
	public static final Byte ZERO_BYTE = Byte.valueOf((byte) 0);
	public static final Short ZERO_SHORT = Short.valueOf((short) 0);
	public static final Integer ZERO_INTEGER = Integer.valueOf(0);
	public static final Long ZERO_LONG = Long.valueOf(0L);
	public static final Float ZERO_FLOAT = Float.valueOf(0.0F);
	public static final Double ZERO_DOUBLE = Double.valueOf(0.0D);
	public static final int POOR_SCALE_MIN = -100;
	/**
	 * @see java.math.MathContext#DECIMAL64
	 */
	public static final int DECIMAL64_SCALE_MAX = 15;
	private static volatile Pattern floatPattern = null;
	/**
	 * All possible chars for representing a number as a String
	 */
	static final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', //
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', //
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	static final long[] MULTIPLIERS_1000 = { 1000L, 1000L * 1000L, //
			1000L * 1000L * 1000L, 1000L * 1000L * 1000L * 1000L, //
			1000L * 1000L * 1000L * 1000L * 1000L };

	static final long[] MULTIPLIERS_1024 = { 1024L, 1024L * 1024L, //
			1024L * 1024L * 1024L, 1024L * 1024L * 1024L * 1024L, //
			1024L * 1024L * 1024L * 1024L * 1024L };

	// Prevent instantiation
	private NumberUtil() {
		super();
	}

	/**
	 * @since Apr 26, 2016 8:20:32 PM
	 * @author WuJianqiang
	 *
	 */
	public static class InvalidNumber extends BigDecimal {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		private InvalidNumber() {
			super(0);
		}

	}

	/**
	 * @return 1.6 --> 160; 1.7 --> 170; 1.8 --> 180; and so on
	 */
	public static int getJavaVersion() {
		Number version = firstNumberOf(System.getProperty("java.version"));
		return multiply(version, 100).intValue();
	}

	/**
	 * 
	 * @param s
	 * @param defaultValue
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static byte parseByte(String s, byte defaultValue) {
		Number n = numberOf(s, Byte.valueOf(defaultValue));
		if (null != n) {
			return n.byteValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param s
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0
	 */
	public static byte parseByte(String s) {
		Number n = numberOf(s, ZERO_BYTE);
		if (null != n) {
			return n.byteValue();
		}
		return (byte) (0);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static byte parseByte(Object value, byte defaultValue) {
		Number n = numberOf(value, Byte.valueOf(defaultValue));
		if (null != n) {
			return n.byteValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0
	 */
	public static byte parseByte(Object value) {
		Number n = numberOf(value, ZERO_BYTE);
		if (null != n) {
			return n.byteValue();
		}
		return (byte) (0);
	}

	/**
	 * 
	 * @param s
	 * @param defaultValue
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static short parseShort(String s, short defaultValue) {
		Number n = numberOf(s, Short.valueOf(defaultValue));
		if (null != n) {
			return n.shortValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param s
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0
	 */
	public static short parseShort(String s) {
		Number n = numberOf(s, ZERO_SHORT);
		if (null != n) {
			return n.shortValue();
		}
		return (short) (0);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static short parseShort(Object value, short defaultValue) {
		Number n = numberOf(value, Short.valueOf(defaultValue));
		if (null != n) {
			return n.shortValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0
	 */
	public static short parseShort(Object value) {
		Number n = numberOf(value, ZERO_SHORT);
		if (null != n) {
			return n.shortValue();
		}
		return (short) (0);
	}

	/**
	 * 
	 * @param s
	 * @param defaultValue
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static int parseInt(String s, int defaultValue) {
		Number n = numberOf(s, Integer.valueOf(defaultValue));
		if (null != n) {
			return n.intValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param s
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0
	 */
	public static int parseInt(String s) {
		Number n = numberOf(s, ZERO_INTEGER);
		if (null != n) {
			return n.intValue();
		}
		return (0);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static int parseInt(Object value, int defaultValue) {
		Number n = numberOf(value, Integer.valueOf(defaultValue));
		if (null != n) {
			return n.intValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0
	 */
	public static int parseInt(Object value) {
		Number n = numberOf(value, ZERO_INTEGER);
		if (null != n) {
			return n.intValue();
		}
		return (0);
	}

	/**
	 * 
	 * @param s
	 * @param defaultValue
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static long parseLong(String s, long defaultValue) {
		Number n = numberOf(s, Long.valueOf(defaultValue));
		if (null != n) {
			return n.longValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param s
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0L
	 */
	public static long parseLong(String s) {
		Number n = numberOf(s, ZERO_LONG);
		if (null != n) {
			return n.longValue();
		}
		return (0L);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static long parseLong(Object value, long defaultValue) {
		Number n = numberOf(value, Long.valueOf(defaultValue));
		if (null != n) {
			return n.longValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0L
	 */
	public static long parseLong(Object value) {
		Number n = numberOf(value, ZERO_LONG);
		if (null != n) {
			return n.longValue();
		}
		return (0L);
	}

	/**
	 * 
	 * @param s
	 * @param defaultValue
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static float parseFloat(String s, float defaultValue) {
		Number n = numberOf(s, Float.valueOf(defaultValue));
		if (null != n) {
			return n.floatValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param s
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static float parseFloat(String s) {
		Number n = numberOf(s, ZERO_FLOAT);
		if (null != n) {
			return n.floatValue();
		}
		return (0.0F);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static float parseFloat(Object value, float defaultValue) {
		Number n = numberOf(value, Float.valueOf(defaultValue));
		if (null != n) {
			return n.floatValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static float parseFloat(Object value) {
		Number n = numberOf(value, ZERO_FLOAT);
		if (null != n) {
			return n.floatValue();
		}
		return (0.0F);
	}

	/**
	 * 
	 * @param s
	 * @param defaultValue
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static double parseDouble(String s, double defaultValue) {
		Number n = numberOf(s, Double.valueOf(defaultValue));
		if (null != n) {
			return n.doubleValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param s
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0.0D
	 */
	public static double parseDouble(String s) {
		Number n = numberOf(s, ZERO_DOUBLE);
		if (null != n) {
			return n.doubleValue();
		}
		return (0.0D);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回缺省值
	 */
	public static double parseDouble(Object value, double defaultValue) {
		Number n = numberOf(value, Double.valueOf(defaultValue));
		if (null != n) {
			return n.doubleValue();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0.0D
	 */
	public static double parseDouble(Object value) {
		Number n = numberOf(value, ZERO_DOUBLE);
		if (null != n) {
			return n.doubleValue();
		}
		return (0.0D);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 defaultValue
	 */
	public static Integer integerOf(Object value, Integer defaultValue) {
		Number n = numberOf(value, defaultValue);
		if (null != n) {
			if (n instanceof Integer) {
				return (Integer) n;
			} else {
				return Integer.valueOf(n.intValue());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 0
	 */
	public static Integer integerOf(Object value) {
		return integerOf(value, ZERO_INTEGER);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 defaultValue
	 */
	public static Byte byteOf(Object value, Byte defaultValue) {
		Number n = numberOf(value, defaultValue);
		if (null != n) {
			if (n instanceof Byte) {
				return (Byte) n;
			} else {
				return Byte.valueOf(n.byteValue());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 0
	 */
	public static Byte byteOf(Object value) {
		return byteOf(value, ZERO_BYTE);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 defaultValue
	 */
	public static Short shortOf(Object value, Short defaultValue) {
		Number n = numberOf(value, defaultValue);
		if (null != n) {
			if (n instanceof Short) {
				return (Short) n;
			} else {
				return Short.valueOf(n.shortValue());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 0
	 */
	public static Short shortOf(Object value) {
		return shortOf(value, ZERO_SHORT);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 defaultValue
	 */
	public static Long longOf(Object value, Long defaultValue) {
		Number n = numberOf(value, defaultValue);
		if (null != n) {
			if (n instanceof Long) {
				return (Long) n;
			} else {
				return Long.valueOf(n.longValue());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 0L
	 */
	public static Long longOf(Object value) {
		return longOf(value, ZERO_LONG);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 defaultValue
	 */
	public static Float floatOf(Object value, Float defaultValue) {
		Number n = numberOf(value, defaultValue);
		if (null != n) {
			if (n instanceof Float) {
				return (Float) n;
			} else if (n instanceof Double) {
				Double d = (Double) n;
				if (d.isInfinite() || d.isNaN()) {
					return Float.valueOf(d.floatValue());
				} else {
					return Float.valueOf(d.toString());
				}
			} else {
				return Float.valueOf(n.floatValue());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 0.0F
	 */
	public static Float floatOf(Object value) {
		return floatOf(value, ZERO_FLOAT);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 defaultValue
	 */
	public static Double doubleOf(Object value, Double defaultValue) {
		Number n = numberOf(value, defaultValue);
		if (null != n) {
			if (n instanceof Double) {
				return (Double) n;
			} else if (n instanceof Float) {
				Float f = (Float) n;
				if (f.isInfinite() || f.isNaN()) {
					return Double.valueOf(f.doubleValue());
				} else {
					return Double.valueOf(f.toString());
				}
			} else {
				return Double.valueOf(n.doubleValue());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 0.0D
	 */
	public static Double doubleOf(Object value) {
		return doubleOf(value, ZERO_DOUBLE);
	}

	/**
	 * 截除小数后面的 0 尾数，并尽量转换为整数
	 * 
	 * @param n
	 * @param scale
	 *            超长小数位数的首先四舍五入
	 * @return
	 */
	public static Number stripTrailingZeros(Number n, int scale) {
		if (null == n) {
			return null;
		} else if (n instanceof Double || n instanceof Float) {
			n = toBigDecimal(n, null);
		}
		if (n instanceof BigDecimal) {
			BigDecimal bigDec = (BigDecimal) n;
			if (bigDec.scale() > scale) {
				bigDec = bigDec.setScale(scale, RoundingMode.HALF_UP);
			}
			scale = bigDec.scale();
			if (scale > 0) {
				bigDec = bigDec.stripTrailingZeros();
				scale = bigDec.scale();
			}
			if (0 == scale) {
				BigInteger bigInt = bigDec.unscaledValue();
				if (bigInt.bitLength() < LONG_SIZE) {
					long value = bigInt.longValue();
					int test = (int) value;
					if (value != test) {
						return Long.valueOf(value);
					} else {
						return Integer.valueOf(test);
					}
				}
				return bigInt;
			}
			return bigDec;
		}
		return n;
	}

	/**
	 * 首先截除 {@code 15} 位小数位后的尾数，然后截除余下小数后面的 0 尾数，并尽量转换为整数
	 * 
	 * @param n
	 * @return
	 * @see java.math.MathContext#DECIMAL64
	 */
	public static Number stripTrailingZeros(Number n) {
		return stripTrailingZeros(n, DECIMAL64_SCALE_MAX);
	}

	/**
	 * @param s
	 * @param defaultValue
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")，则返回null，存在格式异常则返回 defaultValue
	 */
	public static BigDecimal bigDecimalOf(final String s, final BigDecimal defaultValue) {
		if (NullUtil.isNotNull(s)) {
			try {
				final String ns = s.trim();
				BigDecimal bigDec = new BigDecimal(ns);
				int scale = bigDec.scale();
				// 过大指数将非常消耗CPU，且开销并非线性增长，如将“330165E9711606”转换为BigInteger将不知何时完成！
				if (scale <= 0 && scale >= POOR_SCALE_MIN) {
					if (scale < 0) {
						bigDec = bigDec.setScale(0);
					}
				}
				return bigDec;
			} catch (NumberFormatException ignore) {
				return defaultValue;
			}
		}
		return null;
	}

	/**
	 * @param s
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")，则返回null，存在格式异常则返回 0
	 */
	public static BigDecimal bigDecimalOf(final String s) {
		return bigDecimalOf(s, BigDecimal.ZERO);
	}

	/**
	 * 无损数字解析，可能返回类型：Integer, Long, BigInteger, BigDecimal
	 * 
	 * @param s
	 * @param defaultValue
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")，则返回null，存在格式异常则返回 defaultValue
	 */
	public static Number numberOf(final String s, final Number defaultValue) {
		if (NullUtil.isNotNull(s)) {
			final String ns = s.trim();
			int len = ns.length();
			if (len <= 0) {
				return null;
			}
			if (ns.indexOf('.') < 0) {
				try {
					long value;
					if (len > 2 && ns.substring(0, 2).equalsIgnoreCase("0x")) {
						value = Long.parseLong(ns.substring(2), 16);
					} else {
						value = Long.parseLong(ns);
					}
					if (value != (int) value) {
						return Long.valueOf(value);
					} else {
						return Integer.valueOf((int) value);
					}
				} catch (NumberFormatException ignore) {
				}
			}
			try {
				BigDecimal bigDec = new BigDecimal(ns);
				int scale = bigDec.scale();
				// 过大指数将非常消耗CPU，且开销并非线性增长，如将“330165E9711606”转换为BigInteger将不知何时完成！
				if (scale <= 0 && scale >= POOR_SCALE_MIN) {
					if (scale < 0) {
						bigDec = bigDec.setScale(0);
					}
					BigInteger bigInt = bigDec.unscaledValue();
					if (bigInt.bitLength() < LONG_SIZE) {
						long value = bigInt.longValue();
						int test = (int) value;
						if (value != test) {
							return Long.valueOf(value);
						} else {
							return Integer.valueOf(test);
						}
					}
					return bigInt;
				}
				return bigDec;
			} catch (NumberFormatException ignore) {
				return defaultValue;
			}
		}
		return null;
	}

	/**
	 * 无损数字解析，可能返回类型：Integer, Long, BigInteger, BigDecimal
	 * 
	 * @param s
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")，则返回null，存在格式异常则返回 0
	 */
	public static Number numberOf(final String s) {
		return numberOf(s, INVALID_NUMBER);
	}

	/**
	 * 无损数字解析，可能返回类型：Integer, Long, BigInteger, BigDecimal
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")，则返回null，存在格式异常则返回 defaultValue
	 */
	public static Number numberOf(final Object value, final Number defaultValue) {
		if (null != value) {
			if (value instanceof Number) {
				if (value instanceof Integer || value instanceof Long) {
					return (Number) value;
				} else if (value instanceof BigDecimal || value instanceof BigInteger) {
					return (Number) value;
				} else if (value instanceof Double) {
					return numberOf(((Double) value).doubleValue(), defaultValue);
				} else if (value instanceof Float) {
					return numberOf(((Float) value).floatValue(), defaultValue);
				}
				return Integer.valueOf(((Number) value).intValue());
			}
			return numberOf(value.toString(), defaultValue);
		}
		return null;
	}

	/**
	 * 无损数字解析，可能返回类型：Integer, Long, BigInteger, BigDecimal
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")，则返回null，存在格式异常则返回 0
	 */
	public static Number numberOf(final Object value) {
		return numberOf(value, INVALID_NUMBER);
	}

	/**
	 * 无损数字解析，可能返回类型：Integer, Long, BigInteger, BigDecimal
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 d 为 Infinite 或 NaN，则返回 defaultValue
	 */
	public static Number numberOf(final double d, final Number defaultValue) {
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			return defaultValue;
		}
		final long l = (long) d;
		if (l == d) {
			final int i = (int) l;
			if (i == l) {
				return Integer.valueOf(i);
			} else {
				return Long.valueOf(l);
			}
		}
		try {
			BigDecimal bigDec = new BigDecimal(Double.toString(d));
			int scale = bigDec.scale();
			if (scale <= 0 && scale >= POOR_SCALE_MIN) {
				if (scale < 0) {
					bigDec = bigDec.setScale(0);
				}
				return bigDec.unscaledValue();
			}
			return bigDec;
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 无损数字解析，可能返回类型：Integer, Long, BigInteger, BigDecimal
	 * 
	 * @param value
	 * @return 如果 d 为 Infinite 或 NaN，则返回 0
	 */
	public static Number numberOf(final double d) {
		return numberOf(d, INVALID_NUMBER);
	}

	/**
	 * 无损数字解析，可能返回类型：Integer, Long, BigInteger, BigDecimal
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 f 为 Infinite 或 NaN，则返回 defaultValue
	 */
	public static final Number numberOf(final float f, final Number defaultValue) {
		if (Float.isInfinite(f) || Float.isNaN(f)) {
			return defaultValue;
		}
		final long l = (long) f;
		if (l == f) {
			final int i = (int) l;
			if (i == l) {
				return Integer.valueOf(i);
			} else {
				return Long.valueOf(l);
			}
		}
		try {
			BigDecimal bigDec = new BigDecimal(Float.toString(f));
			int scale = bigDec.scale();
			if (scale <= 0 && scale >= POOR_SCALE_MIN) {
				if (scale < 0) {
					bigDec = bigDec.setScale(0);
				}
				return bigDec.unscaledValue();
			}
			return bigDec;
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 无损数字解析，可能返回类型：Integer, Long, BigInteger, BigDecimal
	 * 
	 * @param value
	 * @return 如果 f 为 Infinite 或 NaN，则返回 0
	 */
	public static Number numberOf(final float f) {
		return numberOf(f, INVALID_NUMBER);
	}

	/**
	 * 忽略前导ASCII空格。
	 * 
	 * @param s
	 * @param defaultValue
	 * @return
	 */
	public static Number firstNumberOf(final String s, final Number defaultValue) {
		return numberOf(firstNumber(s, true), defaultValue);
	}

	/**
	 * 忽略前导ASCII空格。
	 * 
	 * @param s
	 * @return
	 */
	public static Number firstNumberOf(final String s) {
		return firstNumberOf(s, INVALID_NUMBER);
	}

	/**
	 * 忽略前导ASCII空格。
	 * 
	 * @param s
	 * @param decimal
	 * @return
	 */
	public static String firstNumber(final String s, final boolean decimal) {
		if (null != s) {
			if (startsWithDigit(s, decimal, true)) {
				int beginIndex = indexOfDigit(s, decimal, 0);
				int endIndex = indexOfNonDigit(s, decimal, beginIndex + 1);
				if (endIndex < 0) {
					return s.substring(beginIndex);
				} else {
					return s.substring(beginIndex, endIndex);
				}
			}
			return "";
		}
		return null;
	}

	/**
	 * 
	 * @param n
	 * @param defaultValue
	 * @return 如果 n 为无穷(Infinite)或非数字(NaN)则返回 defaultValue
	 */
	public static BigDecimal toBigDecimal(Number n, BigDecimal defaultValue) {
		if (null == n) {
			return null;
		} else if (n instanceof BigDecimal) {
			return (BigDecimal) n;
		} else if (n instanceof BigInteger) {
			return new BigDecimal((BigInteger) n);
		} else if (n instanceof Double || n instanceof Float) {
			if (isInfiniteOrNaN(n)) {
				return defaultValue;
			}
			/**
			 * 对于不是 double/float NaN 和 ±Infinity 的值，BigDecimal(String)构造方法与
			 * Double/Float.toString(double/float) 返回的值兼容。这通常是将 double/float 转换为 BigDecimal
			 * 的首选方法，因为它不会遇到 BigDecimal(double) 构造方法的不可预知问题。
			 */
			return new BigDecimal(n.toString());
		} else {
			return BigDecimal.valueOf(n.longValue());
		}
	}

	/**
	 * 
	 * @param n
	 * @return 如果 n 为无穷(Infinite)或非数字(NaN)则返回 0
	 */
	public static BigDecimal toBigDecimal(Number n) {
		return toBigDecimal(n, BigDecimal.ZERO);
	}

	/**
	 * 
	 * @param n
	 * @param scale
	 * @param defaultValue
	 * @return 如果 n 为无穷(Infinite)或非数字(NaN)则返回 defaultValue
	 */
	public static BigDecimal toBigDecimal(Number n, int scale, BigDecimal defaultValue) {
		BigDecimal dec = toBigDecimal(n, defaultValue);
		if (null != dec && dec.scale() > scale) {
			dec = dec.setScale(scale, RoundingMode.HALF_UP);
		}
		return dec;
	}

	/**
	 * 
	 * @param n
	 * @param scale
	 * @return 如果 n 为无穷(Infinite)或非数字(NaN)则返回 0
	 */
	public static BigDecimal toBigDecimal(Number n, int scale) {
		return toBigDecimal(n, scale, BigDecimal.ZERO);
	}

	/**
	 * 
	 * @param n
	 * @param defaultValue
	 * @return 如果 n 为无穷(Infinite)或非数字(NaN)则返回 defaultValue
	 */
	public static BigInteger toBigInteger(Number n, BigInteger defaultValue) {
		if (null == n) {
			return null;
		} else if (n instanceof BigDecimal) {
			return ((BigDecimal) n).toBigInteger();
		} else if (n instanceof BigInteger) {
			return (BigInteger) n;
		} else if (n instanceof Double || n instanceof Float) {
			return toBigDecimal(n, new BigDecimal(defaultValue)).toBigInteger();
		} else {
			return BigInteger.valueOf(n.longValue());
		}
	}

	/**
	 * 
	 * @param n
	 * @return 如果 n 为无穷(Infinite)或非数字(NaN)则返回 0
	 */
	public static BigInteger toBigInteger(Number n) {
		return toBigInteger(n, BigInteger.ZERO);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 defaultValue
	 */
	public static byte byteValue(Object value, byte defaultValue) {
		if (null == value) {
			return defaultValue;
		} else if (value instanceof Byte) {
			return ((Byte) value).byteValue();
		} else if (value instanceof Integer) {
			return ((Integer) value).byteValue();
		} else if (value instanceof Number) {
			if (isInfiniteOrNaN(value)) {
				return defaultValue;
			}
			return ((Number) value).byteValue();
		} else {
			return parseByte(value.toString(), defaultValue);
		}
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0
	 */
	public static byte byteValue(Object value) {
		return byteValue(value, (byte) 0);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 defaultValue
	 */
	public static short shortValue(Object value, short defaultValue) {
		if (null == value) {
			return defaultValue;
		} else if (value instanceof Short) {
			return ((Short) value).shortValue();
		} else if (value instanceof Integer) {
			return ((Integer) value).shortValue();
		} else if (value instanceof Number) {
			if (isInfiniteOrNaN(value)) {
				return defaultValue;
			}
			return ((Number) value).shortValue();
		} else {
			return parseShort(value.toString(), defaultValue);
		}
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0
	 */
	public static short shortValue(Object value) {
		return shortValue(value, (short) 0);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 defaultValue
	 */
	public static int intValue(Object value, int defaultValue) {
		if (null == value) {
			return defaultValue;
		} else if (value instanceof Integer) {
			return ((Integer) value).intValue();
		} else if (value instanceof Number) {
			if (isInfiniteOrNaN(value)) {
				return defaultValue;
			}
			return ((Number) value).intValue();
		} else {
			return parseInt(value.toString(), defaultValue);
		}
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0
	 */
	public static int intValue(Object value) {
		return intValue(value, 0);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 defaultValue
	 */
	public static long longValue(Object value, long defaultValue) {
		if (null == value) {
			return defaultValue;
		} else if (value instanceof Long) {
			return ((Long) value).longValue();
		} else if (value instanceof Integer) {
			return ((Integer) value).longValue();
		} else if (value instanceof Number) {
			if (isInfiniteOrNaN(value)) {
				return defaultValue;
			}
			return ((Number) value).longValue();
		} else {
			return parseLong(value.toString(), defaultValue);
		}
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0L
	 */
	public static long longValue(Object value) {
		return longValue(value, 0L);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 defaultValue
	 */
	public static float floatValue(Object value, float defaultValue) {
		if (null == value) {
			return defaultValue;
		} else if (value instanceof Float) {
			return ((Float) value).floatValue();
		} else if (value instanceof Double) {
			return parseFloat(((Double) value).toString(), defaultValue);
		} else if (value instanceof Number) {
			return ((Number) value).floatValue();
		} else {
			return parseFloat(value.toString(), defaultValue);
		}
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0.0F
	 */
	public static float floatValue(Object value) {
		return floatValue(value, 0.0F);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 defaultValue
	 */
	public static double doubleValue(Object value, double defaultValue) {
		if (null == value) {
			return defaultValue;
		} else if (value instanceof Double) {
			return ((Double) value).doubleValue();
		} else if (value instanceof Float) {
			return parseDouble(((Float) value).toString(), defaultValue);
		} else if (value instanceof Number) {
			return ((Number) value).doubleValue();
		} else {
			return parseDouble(value.toString(), defaultValue);
		}
	}

	/**
	 * 
	 * @param value
	 * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 0.0D
	 */
	public static double doubleValue(Object value) {
		return doubleValue(value, 0.0D);
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] toBytes(long n) {
		byte[] longBytes = new byte[8];
		for (int i = 7; n != 0; --i) {
			longBytes[i] = (byte) n;
			n >>>= 8;
		}
		return longBytes;
	}

	/**
	 * @param n
	 * @param bytes
	 * @param offset
	 * @param len
	 */
	public static void toBytes(long n, byte[] bytes, int offset, int len) {
		if (offset < 0 || len < 0 || len > 8 || bytes.length < (offset + len)) {
			throw new IllegalArgumentException("offset or len");
		}
		for (int i = len - 1; i >= 0; --i) {
			bytes[offset + i] = (byte) n;
			n >>>= 8;
		}
	}

	/**
	 * @param n
	 * @param bytes
	 * @param offset
	 */
	public static void toBytes(long n, byte[] bytes, int offset) {
		toBytes(n, bytes, offset, 8);
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] toBytes(int n) {
		byte[] intBytes = new byte[4];
		for (int i = 3; n != 0; --i) {
			intBytes[i] = (byte) n;
			n >>>= 8;
		}
		return intBytes;
	}

	/**
	 * @param n
	 * @param bytes
	 * @param offset
	 * @param len
	 */
	public static void toBytes(int n, byte[] bytes, int offset, int len) {
		if (offset < 0 || len < 0 || len > 4 || bytes.length < (offset + len)) {
			throw new IllegalArgumentException("offset or len");
		}
		for (int i = len - 1; i >= 0; --i) {
			bytes[offset + i] = (byte) n;
			n >>>= 8;
		}
	}

	/**
	 * @param n
	 * @param bytes
	 * @param offset
	 */
	public static void toBytes(int n, byte[] bytes, int offset) {
		toBytes(n, bytes, offset, 4);
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] toBytes(short n) {
		byte[] shortBytes = new byte[2];
		shortBytes[1] = (byte) n;
		// 移位操作只支持 int 和 long 类型，否则隐式转换
		shortBytes[0] = (byte) (n >>> 8);
		return shortBytes;
	}

	/**
	 * @param n
	 * @param bytes
	 * @param offset
	 * @param len
	 */
	public static void toBytes(short n, byte[] bytes, int offset, int len) {
		if (offset < 0 || len < 0 || len > 2 || bytes.length < (offset + len)) {
			throw new IllegalArgumentException("offset or len");
		}
		for (int i = len - 1; i >= 0; --i) {
			bytes[offset + i] = (byte) n;
			n >>>= 8;
		}
	}

	/**
	 * @param n
	 * @param bytes
	 * @param offset
	 */
	public static void toBytes(short n, byte[] bytes, int offset) {
		toBytes(n, bytes, offset, 2);
	}

	/**
	 * 
	 * @param b
	 * @param offset
	 * @param len
	 * @return
	 */
	public static long toLong(byte[] b, int offset, int len) {
		if (offset < 0 || offset >= b.length) {
			throw new IllegalArgumentException("offset");
		}

		len += offset;
		if (len > b.length) {
			len = b.length;
		}

		long n = 0;
		for (int i = offset; i < len; ++i) {
			n <<= 8;
			n += (b[i] & 255);
		}
		return n;
	}

	/**
	 * 
	 * @param b
	 * @param offset
	 * @return
	 */
	public static long toLong(byte[] b, int offset) {
		return toLong(b, offset, 8);
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public static long toLong(byte[] b) {
		return toLong(b, 0, 8);
	}

	/**
	 * 
	 * @param b
	 * @param offset
	 * @return
	 */
	public static int toInt(byte[] b, int offset) {
		return (int) toLong(b, offset, 4);
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public static int toInt(byte[] b) {
		return (int) toLong(b, 0, 4);
	}

	/**
	 * 
	 * @param b
	 * @param offset
	 * @return
	 */
	public static short toShort(byte[] b, int offset) {
		return (short) toLong(b, offset, 2);
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public static short toShort(byte[] b) {
		return (short) toLong(b, 0, 2);
	}

	/**
	 * @param n
	 *            a <code>integer</code> to be converted to a string.
	 * @param shift
	 * @param fixedLength
	 * @return
	 */
	private static String toUnsignedString(int n, int shift, int fixedLength) {
		char[] buf = new char[32];
		int charPos = 32;
		int radix = 1 << shift;
		int mask = radix - 1;
		do {
			buf[--charPos] = digits[(n & mask)];
			n >>>= shift;
		} while (n != 0);
		int count = 32 - charPos;
		if (fixedLength > count) {
			int offset = (fixedLength > 32 ? 0 : 32 - fixedLength);
			do {
				buf[--charPos] = '0';
			} while (charPos > offset);
			count = 32 - charPos;
		}
		return new String(buf, charPos, count);
	}

	/**
	 * @param n
	 *            a <code>long</code> to be converted to a string.
	 * @param shift
	 * @param fixedLength
	 * @return
	 */
	private static String toUnsignedString(long n, int shift, int fixedLength) {
		char[] buf = new char[64];
		int charPos = 64;
		int radix = 1 << shift;
		long mask = radix - 1;
		do {
			buf[--charPos] = digits[(int) (n & mask)];
			n >>>= shift;
		} while (n != 0);
		int count = 64 - charPos;
		if (fixedLength > count) {
			int offset = (fixedLength > 64 ? 0 : 64 - fixedLength);
			do {
				buf[--charPos] = '0';
			} while (charPos > offset);
			count = 64 - charPos;
		}
		return new String(buf, charPos, count);
	}

	/**
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toHexString(byte n, int fixedLength) {
		return toUnsignedString(n & 0x0ff, 4, fixedLength);
	}

	/**
	 * @param n
	 * @return
	 */
	public static String toHexString(byte n) {
		return toUnsignedString(n & 0x0ff, 4, 0);
	}

	/**
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toHexString(short n, int fixedLength) {
		return toUnsignedString(n & 0x0ffff, 4, fixedLength);
	}

	/**
	 * @param n
	 * @return
	 */
	public static String toHexString(short n) {
		return toUnsignedString(n & 0x0ffff, 4, 0);
	}

	/**
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toHexString(int n, int fixedLength) {
		return toUnsignedString(n, 4, fixedLength);
	}

	/**
	 * @param n
	 * @return
	 */
	public static String toHexString(int n) {
		return toUnsignedString(n, 4, 0);
	}

	/**
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toHexString(long n, int fixedLength) {
		return toUnsignedString(n, 4, fixedLength);
	}

	/**
	 * @param n
	 * @return
	 */
	public static String toHexString(long n) {
		return toUnsignedString(n, 4, 0);
	}

	/**
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toBinaryString(byte n, int fixedLength) {
		return toUnsignedString(n & 0x0ff, 1, fixedLength);
	}

	/**
	 * @param n
	 * @return
	 */
	public static String toBinaryString(byte n) {
		return toUnsignedString(n & 0x0ff, 1, 0);
	}

	/**
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toBinaryString(short n, int fixedLength) {
		return toUnsignedString(n & 0x0ffff, 1, fixedLength);
	}

	/**
	 * @param n
	 * @return
	 */
	public static String toBinaryString(short n) {
		return toUnsignedString(n & 0x0ffff, 1, 0);
	}

	/**
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toBinaryString(int n, int fixedLength) {
		return toUnsignedString(n, 1, fixedLength);
	}

	/**
	 * @param n
	 * @return
	 */
	public static String toBinaryString(int n) {
		return toUnsignedString(n, 1, 0);
	}

	/**
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toBinaryString(long n, int fixedLength) {
		return toUnsignedString(n, 1, fixedLength);
	}

	/**
	 * @param n
	 * @return
	 */
	public static String toBinaryString(long n) {
		return toUnsignedString(n, 1, 0);
	}

	/**
	 * 
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toUnsignedString(byte n, int fixedLength) {
		return toUnsignedString(n & 0x0ff, fixedLength);
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	public static String toUnsignedString(byte n) {
		return toUnsignedString(n & 0x0ff, 0);
	}

	/**
	 * 
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toUnsignedString(short n, int fixedLength) {
		return toUnsignedString(n & 0x0ffff, fixedLength);
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	public static String toUnsignedString(short n) {
		return toUnsignedString(n & 0x0ffff, 0);
	}

	/**
	 * 
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toUnsignedString(int n, int fixedLength) {
		char[] buf = new char[10];
		int charPos = 10;
		int index;
		do {
			index = (n % 10);
			if (n < 0 && index <= 0) {
				// 负数余数补偿: a % 10 = (b + c) % 10 = b % 10 + c % 10
				// 诸如0x10, 0x100, 0x1000 等2的4n次幂(n > 0)的尾数都是6
				index += 6;
				if (index < 0) {
					index += 10; // 继续补偿为正数
				}
			}
			buf[--charPos] = digits[index];
			n >>>= 1;
			n /= 5;
		} while (n != 0);
		int count = 10 - charPos;
		if (fixedLength > count) {
			int offset = (fixedLength > 10 ? 0 : 10 - fixedLength);
			do {
				buf[--charPos] = '0';
			} while (charPos > offset);
			count = 10 - charPos;
		}
		return new String(buf, charPos, count);
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	public static String toUnsignedString(int n) {
		return toUnsignedString(n, 0);
	}

	/**
	 * 
	 * @param n
	 * @param fixedLength
	 * @return
	 */
	public static String toUnsignedString(long n, int fixedLength) {
		char[] buf = new char[20];
		int charPos = 20;
		int index;
		do {
			index = (int) (n % 10L);
			if (n < 0 && index <= 0) {
				// 负数余数补偿: a % 10 = (b + c) % 10 = b % 10 + c % 10
				// 诸如0x10, 0x100, 0x1000 等2的4n次幂(n > 0)的尾数都是6
				index += 6;
				if (index < 0) {
					index += 10; // 继续补偿为正数
				}
			}
			buf[--charPos] = digits[index];
			n >>>= 1;
			n /= 5;
		} while (n != 0);
		int count = 20 - charPos;
		if (fixedLength > count) {
			int offset = (fixedLength > 20 ? 0 : 20 - fixedLength);
			do {
				buf[--charPos] = '0';
			} while (charPos > offset);
			count = 20 - charPos;
		}
		return new String(buf, charPos, count);
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	public static String toUnsignedString(long n) {
		return toUnsignedString(n, 0);
	}

	/**
	 * @param n
	 * @param blockSize
	 *            blockSize may be (or may be an integer optionally followed by) one of following:
	 *            KB 1000, K 1024, MB 1000*1000, M 1024*1024, and so on for G, T, P.
	 * @return
	 */
	public static String toSize(long n, String blockSize) {
		char unit1 = 0, unit2 = 0;
		long[] multipliers = MULTIPLIERS_1024;
		int len;
		if (null != blockSize && (len = blockSize.length()) > 0) {
			unit1 = Character.toUpperCase(blockSize.charAt(0));
			if (len > 1) {
				unit2 = Character.toUpperCase(blockSize.charAt(1));
				if (unit2 == 'B') {
					multipliers = MULTIPLIERS_1000;
				} else {
					unit2 = 0;
				}
			}
		}

		// print sizes in human readable format
		if (unit1 == 0 || unit1 == 'H') {
			if (n > (multipliers[4] * 10)) {
				unit1 = 'P';
			} else if (n > (multipliers[3] * 10)) {
				unit1 = 'T';
			} else if (n > (multipliers[2] * 10)) {
				unit1 = 'G';
			} else if (n > (multipliers[1] * 10)) {
				unit1 = 'M';
			} else if (n > (multipliers[0] * 10)) {
				unit1 = 'K';
			}
		}

		long remain, multiplier;
		switch (unit1) {
		case 'K':
			multiplier = multipliers[0];
			remain = n % multiplier;
			n /= multiplier;
			if (remain >= (multiplier >> 1)) {
				++n;
			}
			break;
		case 'M':
			multiplier = multipliers[1];
			remain = n % multiplier;
			n /= multiplier;
			if (remain >= (multiplier >> 1)) {
				++n;
			}
			break;
		case 'G':
			multiplier = multipliers[2];
			remain = n % multiplier;
			n /= multiplier;
			if (remain >= (multiplier >> 1)) {
				++n;
			}
			break;
		case 'T':
			multiplier = multipliers[3];
			remain = n % multiplier;
			n /= multiplier;
			if (remain >= (multiplier >> 1)) {
				++n;
			}
			break;
		case 'P':
			multiplier = multipliers[4];
			remain = n % multiplier;
			n /= multiplier;
			if (remain >= (multiplier >> 1)) {
				++n;
			}
			break;
		default:
			unit1 = 0;
			break;
		}

		StringBuilder sb = new StringBuilder(22);
		sb.append(n);
		if (unit1 != 0) {
			sb.append(unit1);
		}
		if (unit2 != 0) {
			sb.append(unit2);
		}
		return sb.toString();
	}

	/**
	 * @param n
	 * @return
	 */
	public static String toSize(long n) {
		return toSize(n, null);
	}

	/**
	 * @param n
	 * @param groupingUsed
	 * @param percent
	 * @return
	 */
	public static String toString(Number n, boolean groupingUsed, boolean percent) {
		if (null != n) {
			NumberFormat format;
			if (percent) {
				format = NumberFormat.getPercentInstance();
			} else if (n instanceof BigDecimal || n instanceof Double || n instanceof Float) {
				format = NumberFormat.getNumberInstance();
			} else {
				format = NumberFormat.getIntegerInstance();
			}
			format.setGroupingUsed(groupingUsed);
			return format.format(n);
		} else {
			return null;
		}
	}

	/**
	 * @param n
	 * @param groupingUsed
	 * @return
	 */
	public static String toString(Number n, boolean groupingUsed) {
		return toString(n, groupingUsed, false);
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	public static String toString(Number n) {
		if (null != n) {
			String s;
			if (n instanceof Double) {
				Double d = (Double) n;
				if (d.isInfinite() || d.isNaN()) {
					s = d.toString();
				} else {
					s = numberOf(d.doubleValue()).toString();
				}
			} else if (n instanceof Float) {
				Float f = (Float) n;
				if (f.isInfinite() || f.isNaN()) {
					s = f.toString();
				} else {
					s = numberOf(((Float) n).floatValue()).toString();
				}
			} else {
				s = n.toString();
			}
			// Shave off trailing zeros and decimal point, if possible.
			if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0) {
				int i = s.length() - 1;
				for (; i > 0; --i) {
					if (s.charAt(i) != '0') {
						break;
					}
				}
				if (i <= 0 || s.charAt(i) != '.') {
					++i;
				}
				if (i < s.length()) {
					s = s.substring(0, i);
				}
			}
			return s;
		} else {
			return null;
		}
	}

	/**
	 * 检查输入字符串格式是否符合Java标准的Double.valueOf()或Float.valueOf()方法可解析格式。
	 * <p>
	 * <p>
	 * Leading and trailing whitespace characters in <code>s</code> are ignored. Whitespace is
	 * removed as if by the {@link String#trim} method; that is, both ASCII space and control
	 * characters are removed. The rest of <code>s</code> should constitute a <i>FloatValue</i> as
	 * described by the lexical syntax rules:
	 * 
	 * <blockquote>
	 * <dl>
	 * <dt><i>FloatValue:</i>
	 * <dd><i>Sign<sub>opt</sub></i> <code>NaN</code>
	 * <dd><i>Sign<sub>opt</sub></i> <code>Infinity</code>
	 * <dd><i>Sign<sub>opt</sub> FloatingPointLiteral</i>
	 * <dd><i>Sign<sub>opt</sub> HexFloatingPointLiteral</i>
	 * <dd><i>SignedInteger</i>
	 * </dl>
	 * 
	 * <p>
	 * 
	 * <dl>
	 * <dt><i>HexFloatingPointLiteral</i>:
	 * <dd><i>HexSignificand BinaryExponent FloatTypeSuffix<sub>opt</sub></i>
	 * </dl>
	 * 
	 * <p>
	 * 
	 * <dl>
	 * <dt><i>HexSignificand:</i>
	 * <dd><i>HexNumeral</i>
	 * <dd><i>HexNumeral</i> <code>.</code>
	 * <dd><code>0x</code> <i>HexDigits<sub>opt</sub> </i><code>.</code><i> HexDigits</i>
	 * <dd><code>0X</code><i> HexDigits<sub>opt</sub> </i><code>.</code> <i>HexDigits</i>
	 * </dl>
	 * 
	 * <p>
	 * 
	 * <dl>
	 * <dt><i>BinaryExponent:</i>
	 * <dd><i>BinaryExponentIndicator SignedInteger</i>
	 * </dl>
	 * 
	 * <p>
	 * 
	 * <dl>
	 * <dt><i>BinaryExponentIndicator:</i>
	 * <dd><code>p</code>
	 * <dd><code>P</code>
	 * </dl>
	 * 
	 * </blockquote>
	 * 
	 * where <i>Sign</i>, <i>FloatingPointLiteral</i>, <i>HexNumeral</i>, <i>HexDigits</i>,
	 * <i>SignedInteger</i> and <i>FloatTypeSuffix</i> are as defined in the lexical structure
	 * sections of the of the <a href="http://java.sun.com/docs/books/jls/html/">Java Language
	 * Specification</a>. If <code>s</code> does not have the form of a <i>FloatValue</i>, then a
	 * <code>NumberFormatException</code> is thrown. Otherwise, <code>s</code> is regarded as
	 * representing an exact decimal value in the usual &quot;computerized scientific notation&quot;
	 * or as an exact hexadecimal value; this exact numerical value is then conceptually converted
	 * to an &quot;infinitely precise&quot; binary value that is then rounded to type
	 * <code>double</code> by the usual round-to-nearest rule of IEEE 754 floating-point arithmetic,
	 * which includes preserving the sign of a zero value. Finally, a <code>Double</code> object
	 * representing this <code>double</code> value is returned.
	 * 
	 * <p>
	 * To interpret localized string representations of a floating-point value, use subclasses of
	 * {@link java.text.NumberFormat}.
	 * 
	 * <p>
	 * Note that trailing format specifiers, specifiers that determine the type of a floating-point
	 * literal ( <code>1.0f</code> is a <code>float</code> value; <code>1.0d</code> is a
	 * <code>double</code> value), do <em>not</em> influence the results of this method. In other
	 * words, the numerical value of the input string is converted directly to the target
	 * floating-point type. The two-step sequence of conversions, string to <code>float</code>
	 * followed by <code>float</code> to <code>double</code>, is <em>not</em> equivalent to
	 * converting a string directly to <code>double</code>. For example, the <code>float</code>
	 * literal <code>0.1f</code> is equal to the <code>double</code> value
	 * <code>0.10000000149011612</code>; the <code>float</code> literal <code>0.1f</code> represents
	 * a different numerical value than the <code>double</code> literal <code>0.1</code>. (The
	 * numerical value 0.1 cannot be exactly represented in a binary floating-point number.)
	 * 
	 * @param s
	 *            the string to be checked.
	 * @return
	 */
	public static boolean isFloatValue(String s) {
		Pattern fp = floatPattern;
		if (null == fp) {
			synchronized (NumberUtil.class) {
				final String Digits = "(\\p{Digit}+)";
				final String HexDigits = "(\\p{XDigit}+)";
				// an exponent is 'e' or 'E' followed by an optionally
				// .
				final String Exp = "[eE][+-]?" + Digits;
				final String fpRegex = ("[\\x00-\\x20]*" + // Optional leading
															// "whitespace"
						"[+-]?(" + // Optional sign character
						"NaN|" + // "NaN" string
						"Infinity|" + // "Infinity" string

						// A decimal floating-point string representing a finite
						// positive
						// number without a leading sign has at most five basic
						// pieces:
						// Digits . Digits ExponentPart FloatTypeSuffix
						//
						// Since this method allows integer-only strings as
						// input
						// in addition to strings of floating-point literals,
						// the
						// two sub-patterns below are simplifications of the
						// grammar
						// productions from the Java Language Specification, 2nd
						// edition, section 3.10.2.

						// Digits ._opt Digits_opt ExponentPart_opt
						// FloatTypeSuffix_opt
						"(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +

						// . Digits ExponentPart_opt FloatTypeSuffix_opt
						"(\\.(" + Digits + ")(" + Exp + ")?)|" +

						// Hexadecimal strings
						"((" +

						// 0[xX] HexDigits ._opt BinaryExponent
						// FloatTypeSuffix_opt
						"(0[xX]" + HexDigits + "(\\.)?)|" +

						// 0[xX] HexDigits_opt . HexDigits BinaryExponent
						// FloatTypeSuffix_opt
						"(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

						")[pP][+-]?" + Digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");// Optional
																						// trailing
																						// "whitespace"
				fp = Pattern.compile(fpRegex);
				floatPattern = fp;
			}
		}
		return fp.matcher(s).matches();
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNaN(Object value) {
		if (value instanceof Double) {
			return ((Double) value).isNaN();
		} else if (value instanceof Float) {
			return ((Float) value).isNaN();
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isInfinite(Object value) {
		if (value instanceof Double) {
			return ((Double) value).isInfinite();
		} else if (value instanceof Float) {
			return ((Float) value).isInfinite();
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isInfiniteOrNaN(Object value) {
		if (value instanceof Double) {
			Double d = (Double) value;
			return (d.isInfinite() || d.isNaN());
		} else if (value instanceof Float) {
			Float f = (Float) value;
			return (f.isInfinite() || f.isNaN());
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param source
	 * @param decimal
	 * @param ignoreSpace
	 * @see #indexOfNonDigit(String, boolean, int)
	 * @return
	 */
	public static boolean startsWithDigit(String source, boolean decimal, boolean ignoreSpace) {
		int len;
		if (null != source && (len = source.length()) > 0) {
			char ch;
			int signIndex = -1;
			int pointIndex = -1;
			for (int i = 0; i < len; ++i) {
				ch = source.charAt(i);
				if (Character.isDigit(ch)) {
					return true;
				} else if (ch == '.') {
					if (!decimal || pointIndex >= 0) {
						return false; // 整数不允许小数点，小数不允许出现多个小数点
					}
					pointIndex = i;
				} else if (pointIndex >= 0 || signIndex >= 0) {
					return false; // 小数点后必须跟数字，或者符号之后必须跟小数点或数字
				} else if (ch == '-' || ch == '+' && decimal) {
					signIndex = i;
				} else if (!ignoreSpace || ch != ' ') { // only, NOT include
														// \t\r\n etc.
					return false; // 非空格或者不允许空格
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param source
	 * @param decimal
	 * @param offset
	 * @see #indexOfNonDigit(String, boolean, int)
	 * @return
	 */
	public static int indexOfDigit(String source, boolean decimal, int offset) {
		int len;
		if (null != source && offset >= 0 && (len = source.length()) > offset) {
			char ch;
			int signIndex = -1;
			int pointIndex = -1;
			for (int i = offset; i < len; ++i) {
				ch = source.charAt(i);
				if (Character.isDigit(ch)) {
					if (pointIndex == i) {
						--i; // 第一个数字前是小数点，允许
					}
					if (signIndex == i) {
						--i; // 第一个数字（或此前是小数点）前是符号，允许
					}
					return i;
				} else if (ch == '.') {
					pointIndex = i + 1;
				} else if (ch == '-' || ch == '+' && decimal) {
					signIndex = i + 1;
				}
			}
		}
		return (-1);
	}

	/**
	 * 
	 * @param source
	 * @param decimal
	 * @see #indexOfDigit(String, boolean, int)
	 * @return
	 */
	public static int indexOfDigit(String source, boolean decimal) {
		return indexOfDigit(source, decimal, 0);
	}

	/**
	 * BigInteger, Long, Integer, Short, Byte:
	 * 符合Java规则，将指定基数的整数的字符串表示形式转换为整数。该字符串表示形式包括一个可选的减号，后跟一个或多个指定基数的数字。字符到数字的映射由 Character.digit
	 * 提供。该字符串不能包含任何其他字符（例如，空格）。
	 * <p>
	 * BigDecimal：符合Java规则，字符串表示形式由可选符号 '+' ('\u002B') 或 '-' ('\u002D')
	 * 组成，后跟零或多个十进制数字（“整数”）的序列，可以选择后跟一个小数，也可以选择后跟一个指数。
	 * <p>
	 * 该小数由小数点以及后跟的零或更多十进制数字组成。字符串必须至少包含整数或小数部分中的一个数字。由符号、整数和小数部分组成的数字称为有效位数。
	 * <p>
	 * 指数由字符 'e'（'\u0075') 或 'E' ('\u0045') 以及后跟的一个或多个十进制数字组成。指数的值必须位于 Integer.MAX_VALUE
	 * (Integer.MIN_VALUE+1) 和 Integer.MAX_VALUE（包括）之间。
	 * <p>
	 * Double, Float: Java规则参照 {@link #isFloatValue(String)} ，这里按兼容BigDecimal规则处理。
	 * 
	 * @param source
	 * @param decimal
	 * @param exponent
	 * @param offset
	 * @return
	 */
	public static int indexOfNonDigit(String source, boolean decimal, boolean exponent, int offset) {
		int len;
		if (null != source && offset >= 0 && (len = source.length()) > offset) {
			char ch;
			int integerPart = 0;
			int fractionPart = 0;
			int exponentPart = 0;
			int exponentSign = 0;
			boolean decimalPoint = false;
			for (int i = offset; i < len; ++i) {
				ch = source.charAt(i);
				if (Character.isDigit(ch)) {
					if (exponentSign > 0) {
						++exponentPart; // 指数部分数字个数
					} else if (decimalPoint) {
						++fractionPart; // 小数部分数字个数
					} else {
						++integerPart; // 整数部分数字个数
					}
				} else if (ch == '-') {
					if (i != offset && i != exponentSign) {
						return i; // 只允许开始或指数后出现负号
					}
				} else if (decimal) {
					if (ch == '+') {
						if (i != offset && i != exponentSign) {
							return i; // 只允许小数开始或指数后出现正号
						}
					} else if (ch == '.') {
						if (decimalPoint || exponentSign > 0) {
							return i; // 只允许在指数出现前出现一个小数点
						}
						decimalPoint = true;
					} else if (exponent && (ch == 'e' || ch == 'E')) {
						if (integerPart <= 0 && fractionPart <= 0) {
							return i; // 出现指数前必须存在有效数字
						} else if (exponentSign > 0) {
							return i; // 只允许出现一个指数标记
						}
						exponentSign = i + 1; // 标识出现指数后允许紧跟正号或负号位置
					} else {
						return i; // 不允许其它非数字字符
					}
				} else {
					return i; // 不允许其它非数字字符
				}
			}
			if (integerPart <= 0 && fractionPart <= 0) {
				return (offset); // 整数或小数部分不存在有效数字
			} else if (exponentSign > 0 && exponentPart <= 0) {
				return (exponentSign - 1); // 出现指数符号，但不存在有效数字
			}
		}
		return (-1);
	}

	/**
	 * BigInteger, Long, Integer, Short, Byte:
	 * 符合Java规则，将指定基数的整数的字符串表示形式转换为整数。该字符串表示形式包括一个可选的减号，后跟一个或多个指定基数的数字。字符到数字的映射由 Character.digit
	 * 提供。该字符串不能包含任何其他字符（例如，空格）。
	 * <p>
	 * BigDecimal：符合Java规则，字符串表示形式由可选符号 '+' ('\u002B') 或 '-' ('\u002D')
	 * 组成，后跟零或多个十进制数字（“整数”）的序列，可以选择后跟一个小数，也可以选择后跟一个指数。
	 * <p>
	 * 该小数由小数点以及后跟的零或更多十进制数字组成。字符串必须至少包含整数或小数部分中的一个数字。由符号、整数和小数部分组成的数字称为有效位数。
	 * <p>
	 * 指数由字符 'e'（'\u0075') 或 'E' ('\u0045') 以及后跟的一个或多个十进制数字组成。指数的值必须位于 Integer.MAX_VALUE
	 * (Integer.MIN_VALUE+1) 和 Integer.MAX_VALUE（包括）之间。
	 * <p>
	 * Double, Float: Java规则参照 {@link #isFloatValue(String)} ，这里按兼容BigDecimal规则处理。
	 * 
	 * @param source
	 * @param decimal
	 * @param offset
	 * @return
	 */
	public static int indexOfNonDigit(String source, boolean decimal, int offset) {
		return indexOfNonDigit(source, decimal, decimal, offset);
	}

	/**
	 * 
	 * @param source
	 * @param decimal
	 * @see #indexOfNonDigit(String, boolean, int)
	 * @return
	 */
	public static int indexOfNonDigit(String source, boolean decimal) {
		return indexOfNonDigit(source, decimal, 0);
	}

	/**
	 * 
	 * @param n1
	 *            不允许为空值(null)
	 * @param n2
	 *            不允许为空值(null)
	 * @return 若 n1 小于 n2 则返回 LESS(-1)； 若 n1 等于 n2 则返回 EQUAL(0)； 若 n1 大于 n2 则返回 GREATER(1)；
	 */
	public static int compare(Number n1, Number n2) {
		if (n1 instanceof Integer && n2 instanceof Integer) {
			int i1 = n1.intValue();
			int i2 = n2.intValue();
			return (i1 < i2 ? LESS : (i1 == i2 ? EQUAL : GREATER));
		}
		if (n1 instanceof BigDecimal || n2 instanceof BigDecimal) {
			BigDecimal bd1 = toBigDecimal(n1);
			BigDecimal bd2 = toBigDecimal(n2);
			return bd1.compareTo(bd2);
		}
		if (n1 instanceof BigInteger || n2 instanceof BigInteger) {
			BigInteger bi1 = toBigInteger(n1);
			BigInteger bi2 = toBigInteger(n2);
			return bi1.compareTo(bi2);
		}
		if (n1 instanceof Long || n2 instanceof Long) {
			if (n1 instanceof Double || n2 instanceof Double) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return bd1.compareTo(bd2);
			}
			if (n1 instanceof Float || n2 instanceof Float) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return bd1.compareTo(bd2);
			}
			long l1 = n1.longValue();
			long l2 = n2.longValue();
			return (l1 < l2 ? LESS : (l1 == l2 ? EQUAL : GREATER));
		}
		if (n1 instanceof Double || n2 instanceof Double) {
			double db1 = doubleValue(n1);
			double db2 = doubleValue(n2);
			return Double.compare(db1, db2);
		}
		if (n1 instanceof Float || n2 instanceof Float) {
			double db1 = doubleValue(n1);
			double db2 = doubleValue(n2);
			return Double.compare(db1, db2);
		}
		int i1 = n1.intValue();
		int i2 = n2.intValue();
		return (i1 < i2 ? LESS : (i1 == i2 ? EQUAL : GREATER));
	}

	/**
	 * add(n1, n2) 方法与 sum(n1, n2) 方法不同之处在于 sum 方法相加时忽略空值(null)
	 * <p>
	 * TODO 尚未检查两数值相加之后是否溢出
	 * 
	 * @param n1
	 * @param n2
	 * @return 若其中有一个参数是空值(null)，则直接返回空值(null)
	 */
	public static Number add(Number n1, Number n2) {
		if (null == n2 || null == n1) {
			return null;
		}
		if (n1 instanceof Integer && n2 instanceof Integer) {
			int i1 = n1.intValue();
			int i2 = n2.intValue();
			return Integer.valueOf(i1 + i2);
		}
		if (n1 instanceof BigDecimal || n2 instanceof BigDecimal) {
			BigDecimal bd1 = toBigDecimal(n1);
			BigDecimal bd2 = toBigDecimal(n2);
			return bd1.add(bd2);
		}
		if (n1 instanceof BigInteger || n2 instanceof BigInteger) {
			BigInteger bi1 = toBigInteger(n1);
			BigInteger bi2 = toBigInteger(n2);
			return bi1.add(bi2);
		}
		if (n1 instanceof Long || n2 instanceof Long) {
			if (n1 instanceof Double || n2 instanceof Double) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return bd1.add(bd2);
			}
			if (n1 instanceof Float || n2 instanceof Float) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return bd1.add(bd2);
			}
			long l1 = n1.longValue();
			long l2 = n2.longValue();
			return Long.valueOf(l1 + l2);
		}
		if (n1 instanceof Double || n2 instanceof Double) {
			double db1 = doubleValue(n1);
			double db2 = doubleValue(n2);
			return Double.valueOf(db1 + db2);
		}
		if (n1 instanceof Float || n2 instanceof Float) {
			float f1 = floatValue(n1);
			float f2 = floatValue(n2);
			return Float.valueOf(f1 + f2);
		}
		int i1 = n1.intValue();
		int i2 = n2.intValue();
		return Integer.valueOf(i1 + i2);
	}

	/**
	 * add(n1, o2) 方法与 sum(n1, o2) 方法不同之处在于 sum 方法相加时忽略空值(null)
	 * 
	 * @param n1
	 * @param o2
	 * @return 若其中有一个参数是空值(null)，则直接返回空值(null)
	 */
	public static Number add(Number n1, Object o2) {
		return add(n1, numberOf(o2));
	}

	/**
	 * sum(n1, n2) 方法与 add(n1, n2) 方法不同之处在于 sum 方法相加时忽略空值(null)
	 * <p>
	 * TODO 尚未检查两数值相加之后是否溢出
	 * 
	 * @param n1
	 * @param n2
	 * @return 若其中有一个参数是空值(null)，则直接返回直接返回另外一个参数值，可能也是空值(null)
	 */
	public static Number sum(Number n1, Number n2) {
		if (null == n2) {
			return n1;
		}
		if (null == n1) {
			return n2;
		}
		return add(n1, n2);
	}

	/**
	 * sum(n1, o2) 方法与 add(n1, o2) 方法不同之处在于 sum 方法相加时忽略空值(null)
	 * 
	 * @param n1
	 * @param o2
	 * @return 若其中有一个参数是空值(null)，则直接返回直接返回另外一个参数值，可能也是空值(null)
	 */
	public static Number sum(Number n1, Object o2) {
		return sum(n1, numberOf(o2));
	}

	/**
	 * TODO 尚未检查两数值相减之后是否溢出
	 * 
	 * @param n1
	 * @param n2
	 * @return 若其中有一个参数是空值(null)，则直接返回空值(null)
	 */
	public static Number subtract(Number n1, Number n2) {
		if (null == n2 || null == n1) {
			return null;
		}
		if (n1 instanceof Integer && n2 instanceof Integer) {
			int i1 = n1.intValue();
			int i2 = n2.intValue();
			return Integer.valueOf(i1 - i2);
		}
		if (n1 instanceof BigDecimal || n2 instanceof BigDecimal) {
			BigDecimal bd1 = toBigDecimal(n1);
			BigDecimal bd2 = toBigDecimal(n2);
			return bd1.subtract(bd2);
		}
		if (n1 instanceof BigInteger || n2 instanceof BigInteger) {
			BigInteger bi1 = toBigInteger(n1);
			BigInteger bi2 = toBigInteger(n2);
			return bi1.subtract(bi2);
		}
		if (n1 instanceof Long || n2 instanceof Long) {
			if (n1 instanceof Double || n2 instanceof Double) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return bd1.subtract(bd2);
			}
			if (n1 instanceof Float || n2 instanceof Float) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return bd1.subtract(bd2);
			}
			long l1 = n1.longValue();
			long l2 = n2.longValue();
			return Long.valueOf(l1 - l2);
		}
		if (n1 instanceof Double || n2 instanceof Double) {
			double db1 = doubleValue(n1);
			double db2 = doubleValue(n2);
			return Double.valueOf(db1 - db2);
		}
		if (n1 instanceof Float || n2 instanceof Float) {
			float f1 = floatValue(n1);
			float f2 = floatValue(n2);
			return Float.valueOf(f1 - f2);
		}
		int i1 = n1.intValue();
		int i2 = n2.intValue();
		return Integer.valueOf(i1 - i2);
	}

	/**
	 * 
	 * @param n1
	 * @param o2
	 * @return 若其中有一个参数是空值(null)，则直接返回空值(null)
	 */
	public static Number subtract(Number n1, Object o2) {
		return subtract(n1, numberOf(o2));
	}

	/**
	 * 
	 * @param n
	 * @return 若参数是空值(null)，则直接返回空值(null)
	 */
	public static Number negate(Number n) {
		if (null == n) {
			return null;
		}
		if (n instanceof Integer) {
			return Integer.valueOf(-n.intValue());
		}
		if (n instanceof BigDecimal) {
			return ((BigDecimal) n).negate();
		}
		if (n instanceof BigInteger) {
			return ((BigInteger) n).negate();
		}
		if (n instanceof Long) {
			return Long.valueOf(-n.longValue());
		}
		if (n instanceof Double) {
			return Double.valueOf(0.0D - n.doubleValue());
		}
		if (n instanceof Float) {
			return Float.valueOf(0.0F - n.floatValue());
		}
		return Integer.valueOf(-n.intValue());
	}

	/**
	 * 
	 * @param n
	 * @return 若参数是空值(null)，则直接返回空值(null)
	 */
	public static Number abs(Number n) {
		if (null == n) {
			return null;
		}
		if (n instanceof Integer) {
			int value = n.intValue();
			if (value < 0) {
				return Integer.valueOf(-value);
			} else {
				return n;
			}
		}
		if (n instanceof BigDecimal) {
			return ((BigDecimal) n).abs();
		}
		if (n instanceof BigInteger) {
			return ((BigInteger) n).abs();
		}
		if (n instanceof Long) {
			long value = n.longValue();
			if (value < 0L) {
				return Long.valueOf(-value);
			} else {
				return n;
			}
		}
		if (n instanceof Double) {
			double value = n.doubleValue();
			if (value <= 0.0D) {
				return Double.valueOf(0.0D - value);
			} else {
				return n;
			}
		}
		if (n instanceof Float) {
			float value = n.floatValue();
			if (value <= 0.0F) {
				return Float.valueOf(0.0F - value);
			} else {
				return n;
			}
		}
		int value = n.intValue();
		if (value < 0) {
			return Integer.valueOf(-value);
		} else {
			return n;
		}
	}

	/**
	 * TODO 尚未检查两数值相乘之后是否溢出
	 * 
	 * @param n1
	 * @param n2
	 * @return 若其中有一个参数是空值(null)，则直接返回空值(null)
	 */
	public static Number multiply(Number n1, Number n2) {
		if (null == n2 || null == n1) {
			return null;
		}
		if (n1 instanceof Integer && n2 instanceof Integer) {
			int i1 = n1.intValue();
			int i2 = n2.intValue();
			return Integer.valueOf(i1 * i2);
		}
		if (n1 instanceof BigDecimal || n2 instanceof BigDecimal) {
			BigDecimal bd1 = toBigDecimal(n1);
			BigDecimal bd2 = toBigDecimal(n2);
			return bd1.multiply(bd2);
		}
		if (n1 instanceof BigInteger || n2 instanceof BigInteger) {
			BigInteger bi1 = toBigInteger(n1);
			BigInteger bi2 = toBigInteger(n2);
			return bi1.multiply(bi2);
		}
		if (n1 instanceof Long || n2 instanceof Long) {
			if (n1 instanceof Double || n2 instanceof Double) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return bd1.multiply(bd2);
			}
			if (n1 instanceof Float || n2 instanceof Float) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return bd1.multiply(bd2);
			}
			long l1 = n1.longValue();
			long l2 = n2.longValue();
			return Long.valueOf(l1 * l2);
		}
		if (n1 instanceof Double || n2 instanceof Double) {
			double db1 = doubleValue(n1);
			double db2 = doubleValue(n2);
			return Double.valueOf(db1 * db2);
		}
		if (n1 instanceof Float || n2 instanceof Float) {
			float f1 = floatValue(n1);
			float f2 = floatValue(n2);
			return Float.valueOf(f1 * f2);
		}
		int i1 = n1.intValue();
		int i2 = n2.intValue();
		return Integer.valueOf(i1 * i2);
	}

	/**
	 * 
	 * @param n1
	 * @param o2
	 * @return 若其中有一个参数是空值(null)，则直接返回空值(null)
	 */
	public static Number multiply(Number n1, Object o2) {
		return multiply(n1, numberOf(o2));
	}

	/**
	 * 
	 * @param bd1
	 * @param bd2
	 * @return
	 */
	public static BigDecimal divide(BigDecimal bd1, BigDecimal bd2) {
		// scale = max(s1 + p2 - s2 + 1, DECIMAL64_SCALE_MAX)
		int scale = bd1.scale() + bd2.precision() - bd2.scale() + 1;
		if (scale < DECIMAL64_SCALE_MAX) {
			scale = DECIMAL64_SCALE_MAX;
		}
		return bd1.divide(bd2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * TODO 尚未检查两数值相除之后是否溢出
	 * 
	 * @param n1
	 * @param n2
	 * @return 若其中有一个参数是空值(null)，则直接返回空值(null)
	 */
	public static Number divide(Number n1, Number n2) {
		if (null == n2 || null == n1) {
			return null;
		}
		if (n1 instanceof Integer && n2 instanceof Integer) {
			int i1 = n1.intValue();
			int i2 = n2.intValue();
			return Integer.valueOf(i1 / i2);
		}
		if (n1 instanceof BigDecimal || n2 instanceof BigDecimal) {
			BigDecimal bd1 = toBigDecimal(n1);
			BigDecimal bd2 = toBigDecimal(n2);
			return divide(bd1, bd2);
		}
		if (n1 instanceof BigInteger || n2 instanceof BigInteger) {
			BigInteger bi1 = toBigInteger(n1);
			BigInteger bi2 = toBigInteger(n2);
			return bi1.divide(bi2);
		}
		if (n1 instanceof Long || n2 instanceof Long) {
			if (n1 instanceof Double || n2 instanceof Double) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return divide(bd1, bd2);
			}
			if (n1 instanceof Float || n2 instanceof Float) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return divide(bd1, bd2);
			}
			long l1 = n1.longValue();
			long l2 = n2.longValue();
			return Long.valueOf(l1 / l2);
		}
		if (n1 instanceof Double || n2 instanceof Double) {
			double db1 = doubleValue(n1);
			double db2 = doubleValue(n2);
			return Double.valueOf(db1 / db2);
		}
		if (n1 instanceof Float || n2 instanceof Float) {
			float f1 = floatValue(n1);
			float f2 = floatValue(n2);
			return Float.valueOf(f1 / f2);
		}
		int i1 = n1.intValue();
		int i2 = n2.intValue();
		return Integer.valueOf(i1 / i2);
	}

	/**
	 * 
	 * @param n1
	 * @param o2
	 * @return 若其中有一个参数是空值(null)，则直接返回空值(null)
	 */
	public static Number divide(Number n1, Object o2) {
		return divide(n1, numberOf(o2));
	}

	/**
	 * 
	 * @param bd1
	 * @param bd2
	 * @return
	 */
	public static BigDecimal mod(BigDecimal bd1, BigDecimal bd2) {
		BigDecimal result = bd1.divideToIntegralValue(bd2);
		result = result.setScale(0);
		result = result.multiply(bd2);
		result = bd1.subtract(result);
		return result;
	}

	/**
	 * 
	 * @param n1
	 * @param n2
	 * @return 若其中有一个参数是空值(null)，则直接返回空值(null)
	 */
	public static Number mod(Number n1, Number n2) {
		if (null == n2 || null == n1) {
			return null;
		}
		if (n1 instanceof Integer && n2 instanceof Integer) {
			int i1 = n1.intValue();
			int i2 = n2.intValue();
			return Integer.valueOf(i1 % i2);
		}
		if (n1 instanceof BigDecimal || n2 instanceof BigDecimal) {
			BigDecimal bd1 = toBigDecimal(n1);
			BigDecimal bd2 = toBigDecimal(n2);
			return mod(bd1, bd2);
		}
		if (n1 instanceof BigInteger || n2 instanceof BigInteger) {
			BigInteger bi1 = toBigInteger(n1);
			BigInteger bi2 = toBigInteger(n2);
			return bi1.mod(bi2);
		}
		if (n1 instanceof Long || n2 instanceof Long) {
			if (n1 instanceof Double || n2 instanceof Double) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return mod(bd1, bd2);
			}
			if (n1 instanceof Float || n2 instanceof Float) {
				BigDecimal bd1 = toBigDecimal(n1);
				BigDecimal bd2 = toBigDecimal(n2);
				return mod(bd1, bd2);
			}
			long l1 = n1.longValue();
			long l2 = n2.longValue();
			return Long.valueOf(l1 % l2);
		}
		if (n1 instanceof Double || n2 instanceof Double) {
			double db1 = doubleValue(n1);
			double db2 = doubleValue(n2);
			return Double.valueOf(db1 % db2);
		}
		if (n1 instanceof Float || n2 instanceof Float) {
			float f1 = floatValue(n1);
			float f2 = floatValue(n2);
			return Float.valueOf(f1 % f2);
		}
		int i1 = n1.intValue();
		int i2 = n2.intValue();
		return Integer.valueOf(i1 % i2);
	}

	/**
	 * 
	 * @param n1
	 * @param o2
	 * @return 若其中有一个参数是空值(null)，则直接返回空值(null)
	 */
	public static Number mod(Number n1, Object o2) {
		return divide(n1, numberOf(o2));
	}

	/**
	 * @param n
	 * @param scale
	 * @return
	 */
	public static Number round(Number n, int scale) {
		if (null == n) {
			return null;
		}
		if (n instanceof BigDecimal) {
			BigDecimal bd = ((BigDecimal) n).setScale(scale, BigDecimal.ROUND_HALF_UP);
			if (scale < 0 && scale >= POOR_SCALE_MIN) {
				bd = bd.setScale(0, BigDecimal.ROUND_UNNECESSARY);
			}
			return bd;
		}
		if (n instanceof Double) {
			BigDecimal bd = BigDecimal.valueOf(((Double) n).doubleValue());
			bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
			if (scale < 0 && scale >= POOR_SCALE_MIN) {
				bd = bd.setScale(0, BigDecimal.ROUND_UNNECESSARY);
			}
			return Double.valueOf(bd.doubleValue());
		}
		if (n instanceof Float) {
			BigDecimal bd = BigDecimal.valueOf(((Float) n).doubleValue());
			bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
			if (scale < 0 && scale >= POOR_SCALE_MIN) {
				bd = bd.setScale(0, BigDecimal.ROUND_UNNECESSARY);
			}
			return Float.valueOf(bd.floatValue());
		}
		if (scale < 0) {
			BigDecimal bd;
			if (n instanceof BigInteger) {
				bd = new BigDecimal((BigInteger) n);
			} else {
				bd = new BigDecimal(n.longValue());
			}
			bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
			bd = bd.setScale(0, BigDecimal.ROUND_UNNECESSARY);
			if (n instanceof Integer) {
				return Integer.valueOf(bd.intValue());
			}
			if (n instanceof Long) {
				return Long.valueOf(bd.longValue());
			}
			if (n instanceof Short) {
				return Short.valueOf(bd.shortValue());
			}
			if (n instanceof Byte) {
				return Byte.valueOf(bd.byteValue());
			}
			if (n instanceof BigInteger) {
				return bd.toBigInteger();
			}
		}
		return n;
	}

}
