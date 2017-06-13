package com.tomato.util;

import java.util.regex.Pattern;


public final class BooleanUtil {
	private static final Pattern TRUE = Pattern.compile("(?:true|t|yes|y|on|1|真|是|开)", Pattern.CASE_INSENSITIVE);
	private static final Pattern FALSE = Pattern.compile("(?:false|f|no|n|off|0|假|否|关)", Pattern.CASE_INSENSITIVE);

	// Allow instantiation for Spring Bean (applicationContext-appConfig.xml)
	public BooleanUtil() {
		super();
	}

	/**
	 * Returns <code>true</code> if and only if the system property named by the argument exists and
	 * is equal to the string {@code "true" "t" "yes" "y" "on" "1" "真" "是" "开"}. (the test of this
	 * string is case insensitive.) A system property is accessible through <code>getProperty</code>
	 * , a method defined by the <code>System</code> class.
	 * <p>
	 * If there is no property with the specified name, or if the specified name is empty or null,
	 * then <code>defaultValue</code> is returned.
	 * 
	 * @param name
	 *            the system property name.
	 * @param defaultValue
	 *            the default value.
	 * @return the <code>Boolean</code> value of the system property.
	 * @see java.lang.System#getProperty(java.lang.String)
	 * @see java.lang.System#getProperty(java.lang.String, java.lang.String)
	 */
	public static Boolean getBoolean(String name, Boolean defaultValue) {
		Boolean result;
		try {
			result = valueOf(System.getProperty(name), defaultValue);
		} catch (IllegalArgumentException e) {
			result = defaultValue;
		} catch (NullPointerException e) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * Returns <code>true</code> if and only if the system property named by the argument exists and
	 * is equal to the string {@code "true" "t" "yes" "y" "on" "1" "真" "是" "开"}. (the test of this
	 * string is case insensitive.) A system property is accessible through <code>getProperty</code>
	 * , a method defined by the <code>System</code> class.
	 * <p>
	 * If there is no property with the specified name, or if the specified name is empty or null,
	 * then <code>defaultValue</code> is returned.
	 * 
	 * @param name
	 *            the system property name.
	 * @param defaultValue
	 *            the default value.
	 * @return the <code>boolean</code> value of the system property.
	 * @see java.lang.System#getProperty(java.lang.String)
	 * @see java.lang.System#getProperty(java.lang.String, java.lang.String)
	 */
	public static boolean getBoolean(String name, boolean defaultValue) {
		boolean result;
		try {
			result = parseBoolean(System.getProperty(name), defaultValue);
		} catch (IllegalArgumentException e) {
			result = defaultValue;
		} catch (NullPointerException e) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * Returns <code>true</code> if and only if the system property named by the argument exists and
	 * is equal to the string {@code "true" "t" "yes" "y" "on" "1" "真" "是" "开"}. (the test of this
	 * string is case insensitive.) A system property is accessible through <code>getProperty</code>
	 * , a method defined by the <code>System</code> class.
	 * <p>
	 * If there is no property with the specified name, or if the specified name is empty or null,
	 * then <code>false</code> is returned.
	 * 
	 * @param name
	 *            the system property name.
	 * @return the <code>boolean</code> value of the system property.
	 * @see java.lang.System#getProperty(java.lang.String)
	 * @see java.lang.System#getProperty(java.lang.String, java.lang.String)
	 */
	public static boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	/**
	 * 
	 * @param text
	 * @param defaultValue
	 * @return
	 */
	public static Boolean valueOf(String text, Boolean defaultValue) {
		if (null == text || text.isEmpty() || text.trim().isEmpty()) {
			return defaultValue;
		}

		if (TRUE.matcher(text).matches()) {
			return Boolean.TRUE;
		}

		if (FALSE.matcher(text).matches()) {
			return Boolean.FALSE;
		}

		return defaultValue;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static Boolean valueOf(String text) {
		return valueOf(text, null);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static Boolean valueOf(Object value, Boolean defaultValue) {
		if (null == value) {
			return defaultValue;
		} else if (value instanceof Boolean) {
			return ((Boolean) value);
		} else if (value instanceof Number) {
			return Boolean.valueOf((((Number) value).intValue() != 0));
		} else if (value instanceof String) {
			return valueOf((String) value, defaultValue);
		} else {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static Boolean valueOf(Object value) {
		return valueOf(value, null);
	}

	/**
	 * 
	 * @param text
	 * @param defaultValue
	 * @return
	 */
	public static boolean parseBoolean(String text, boolean defaultValue) {
		if (null == text || text.isEmpty() || text.trim().isEmpty()) {
			return defaultValue;
		}

		if (TRUE.matcher(text).matches()) {
			return true;
		}

		if (FALSE.matcher(text).matches()) {
			return false;
		}

		return defaultValue;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static boolean parseBoolean(String text) {
		return parseBoolean(text, false);
	}

	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static boolean parseBoolean(Object value, boolean defaultValue) {
		if (null == value) {
			return defaultValue;
		} else if (value instanceof Boolean) {
			return ((Boolean) value).booleanValue();
		} else if (value instanceof Number) {
			return (((Number) value).intValue() != 0);
		} else if (value instanceof String) {
			return parseBoolean((String) value, defaultValue);
		} else {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean parseBoolean(Object value) {
		return parseBoolean(value, false);
	}

	/**
	 * 
	 * @param value
	 * @param trueValue
	 * @param falseValue
	 * @param nullValue
	 * @return
	 */
	public static String toString(Boolean value, String trueValue, String falseValue, String nullValue) {
		if (null != value) {
			return (value.booleanValue() ? trueValue : falseValue);
		} else {
			return nullValue;
		}
	}

	/**
	 * 
	 * @param value
	 * @param trueValue
	 * @param falseValue
	 * @return
	 */
	public static String toString(Boolean value, String trueValue, String falseValue) {
		if (null != value) {
			return (value.booleanValue() ? trueValue : falseValue);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param value
	 * @param trueValue
	 * @param falseValue
	 * @return
	 */
	public static String toString(boolean value, String trueValue, String falseValue) {
		return (value ? trueValue : falseValue);
	}

}
