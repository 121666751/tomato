package com.tomato.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public final class StringUtil {
	public static final String EMPTY = "";
	public static final String SPACE = " ";
	public static final String NULL = "null";
	public static final String ARRAY_SEPARATOR = ",";
	public static final char SINGLE_QUOTE = '\'';
	public static final char DOUBLE_QUOTE = '"';
	public static final String SINGLE_QUOTES = "'";
	public static final String DOUBLE_QUOTES = "\"";
	private static final int CHAR_DIGIT_OFFSET = '0';
	private static final int CHAR_UPPER_OFFSET = 'A' - 10;
	private static final int CHAR_LOWER_OFFSET = 'a' - 10;
	private static final Charset CHARSET_ISO_1 = Charset.forName("ISO-8859-1");
	private static final Charset CHARSET_GB18030 = Charset.forName("GB18030");
	private static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
	private static final String[] ESCAPE_OLD_SINGLE = { EMPTY, "\\", "'", "\r", "\n", EMPTY };
	private static final String[] ESCAPE_NEW_SINGLE = { "'", "\\\\", "\\'", "\\r", "\\n", "'" };
	private static final String[] ESCAPE_OLD_DOUBLE = { EMPTY, "\\", "\"", "\r", "\n", EMPTY };
	private static final String[] ESCAPE_NEW_DOUBLE = { "\"", "\\\\", "\\\"", "\\r", "\\n", "\"" };

	// Prevent instantiation
	private StringUtil() {
		super();
	}

	/**
	 * 
	 * @param source
	 * @param index
	 * @param escape
	 *            例如：'\\'
	 * @return
	 */
	public static boolean isEscaped(String source, int index, char escape) {
		if (index > 0) {
			int i = index - 1;
			for (; i >= 0; --i) {
				if (escape != source.charAt(i)) {
					break;
				}
			}
			i = index - i - 1;
			return ((i % 2) == 1);
		}
		return false;
	}

	/**
	 * 
	 * @param source
	 * @param index
	 * @param escape
	 *            例如：'\\'
	 * @return
	 */
	public static boolean isEscaped(StringBuilder source, int index, char escape) {
		if (index > 0) {
			int i = index - 1;
			for (; i >= 0; --i) {
				if (escape != source.charAt(i)) {
					break;
				}
			}
			i = index - i - 1;
			return ((i % 2) == 1);
		}
		return false;
	}

	/**
	 * 
	 * @param testCode
	 * @return
	 */
	public static boolean containsWhitespace(final String testCode) {
		if (null != testCode) {
			int len = testCode.length();
			for (int i = 0; i < len; ++i) {
				if (Character.isWhitespace(testCode.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param source
	 * @param ch
	 * @param beginIndex
	 * @param escape
	 *            例如：'\\'
	 * @return
	 */
	public static int indexOf(String source, char ch, int fromIndex, char escape) {
		int index = source.indexOf(ch, fromIndex);
		for (; index > 0 && isEscaped(source, index, escape);) {
			index = source.indexOf(ch, index + 1);
		}
		return index;
	}

	/**
	 * 
	 * @param source
	 * @param str
	 * @param caseInsensitive
	 * @return
	 */
	public static int indexOf(String source, String str, boolean caseInsensitive) {
		return indexOf(source, str, 0, caseInsensitive);
	}

	/**
	 * 
	 * @param source
	 * @param str
	 * @param fromIndex
	 * @param caseInsensitive
	 * @return
	 */
	public static int indexOf(String source, String str, int fromIndex, boolean caseInsensitive) {
		int index, len;
		String low, sub;
		if (null == source || null == str) {
			index = -1;
		} else if (caseInsensitive && (len = str.length()) > 0 && source.length() >= len && !str.toUpperCase().equals((low = str.toLowerCase()))) {
			int cl = low.charAt(0);
			int cu = Character.toUpperCase(cl);
			for (;;) {
				index = source.indexOf(cl, fromIndex);
				if (index < 0) {
					if (cl != cu) {
						index = source.indexOf(cu, fromIndex);
						if (index < 0) {
							break;
						}
					} else {
						break;
					}
				}
				sub = source.substring(index, index + len).toLowerCase();
				if (sub.equals(low)) {
					break;
				}
			}
		} else { // 大小写敏感
			index = source.indexOf(str, fromIndex);
		}
		return index;
	}

	/**
	 * 
	 * @param source
	 * @param ch
	 * @param escape
	 *            例如：'\\'
	 * @return
	 */
	public static int indexOf(String source, char ch, char escape) {
		int index = source.indexOf(ch, 0);
		for (; index > 0 && isEscaped(source, index, escape);) {
			index = source.indexOf(ch, index + 1);
		}
		return index;
	}

	/**
	 * 
	 * @param source
	 * @param ch
	 * @param beginIndex
	 * @param escape
	 *            例如：'\\'
	 * @return
	 */
	public static int indexOf(StringBuilder source, String ch, int fromIndex, char escape) {
		int index = source.indexOf(ch, fromIndex);
		for (; index > 0 && isEscaped(source, index, escape);) {
			index = source.indexOf(ch, index + 1);
		}
		return index;
	}

	/**
	 * 
	 * @param source
	 * @param ch
	 * @param escape
	 *            例如：'\\'
	 * @return
	 */
	public static int indexOf(StringBuilder source, String ch, char escape) {
		int index = source.indexOf(ch, 0);
		for (; index > 0 && isEscaped(source, index, escape);) {
			index = source.indexOf(ch, index + 1);
		}
		return index;
	}

	/**
	 * @param str
	 * @param prefix
	 * @return
	 */
	public static boolean startsWith(String str, String prefix) {
		if (null == str || null == prefix) {
			return (null == str && null == prefix);
		}

		final int prefixLen = prefix.length();
		if (prefixLen <= 0) {
			return true;
		}

		final int strLen = str.length();
		if (strLen < prefixLen) {
			return false;
		}

		return str.startsWith(prefix);
	}

	/**
	 * @param str
	 * @param prefix
	 * @return
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (null == str || null == prefix) {
			return (null == str && null == prefix);
		}

		final int prefixLen = prefix.length();
		if (prefixLen <= 0) {
			return true;
		}

		final int strLen = str.length();
		if (strLen < prefixLen) {
			return false;
		}

		return str.regionMatches(true, 0, prefix, 0, prefixLen);
	}

	/**
	 * @param str
	 * @param suffix
	 * @return
	 */
	public static boolean endsWith(String str, String suffix) {
		if (null == str || null == suffix) {
			return (null == str && null == suffix);
		}

		final int suffixLen = suffix.length();
		if (suffixLen <= 0) {
			return true;
		}

		final int strLen = str.length();
		if (strLen < suffixLen) {
			return false;
		}

		return str.endsWith(suffix);
	}

	/**
	 * @param str
	 * @param suffix
	 * @return
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		if (null == str || null == suffix) {
			return (null == str && null == suffix);
		}

		final int suffixLen = suffix.length();
		if (suffixLen <= 0) {
			return true;
		}

		final int strLen = str.length();
		if (strLen < suffixLen) {
			return false;
		}

		final int strOffset = strLen - suffixLen;
		return str.regionMatches(true, strOffset, suffix, 0, suffixLen);
	}

	/**
	 * <p>
	 * 不使用正则表达式全局替换（提升性能：测试将两个SPACE替换成一个中文空格，时间缩短为String.replaceAll的50%）
	 * </p>
	 * 
	 * @param source
	 * @param oldSub
	 *            必须存在有效内容
	 * @param newSub
	 *            必须存在有效内容，允许空串("")
	 * @return
	 */
	public static String replaceAll(String source, String oldSub, String newSub) {
		if (null != source) {
			int oldLen = oldSub.length();
			if (oldLen > 0) {
				int index = source.indexOf(oldSub, 0);
				if (index >= 0) {
					int newLen = newSub.length();
					StringBuilder sb = new StringBuilder(source.length() - oldLen + newLen);
					sb.append(source);
					for (; index >= 0;) {
						sb.replace(index, index + oldLen, newSub);
						index = sb.indexOf(oldSub, index + newLen);
					}
					return sb.toString();
				}
			}
		}
		return source;
	}

	/**
	 * <p>
	 * 不使用正则表达式全局替换（数组支持）
	 * </p>
	 * 
	 * @param source
	 * @param oldSub
	 *            必须存在有效内容
	 * @param newSub
	 *            若null则使用空串("")代替，若数组长度不足oldSub，则重复最后一个
	 * @return
	 */
	public static String replaceAll(String source, String[] oldSub, String[] newSub) {
		if (null != source) {
			StringBuilder sb = null;
			String lastNewSub = null;
			int fromIndex = 0;
			int srcLen = source.length();
			int oldSubLen = oldSub.length;
			int newSubLen = newSub.length;
			int oldLen, newLen;
			for (int i = 0; i < oldSubLen; ++i) {
				oldLen = oldSub[i].length();
				if (oldLen > 0) {
					int index;
					if (null != sb) {
						index = sb.indexOf(oldSub[i], fromIndex);
					} else {
						index = source.indexOf(oldSub[i], fromIndex);
					}
					if (index >= 0) {
						if (null != newSub) {
							if (i < newSubLen) {
								lastNewSub = newSub[i];
							} else if (newSubLen > 0) {
								lastNewSub = newSub[newSubLen - 1];
							}
						}
						if (null != lastNewSub) {
							newLen = lastNewSub.length();
						} else {
							lastNewSub = EMPTY;
							newLen = 0;
						}
						if (null == sb) {
							sb = new StringBuilder(srcLen - oldLen + newLen);
							sb.append(source);
						}
						for (; index >= 0;) {
							sb.replace(index, index + oldLen, lastNewSub);
							index = sb.indexOf(oldSub[i], index + newLen);
						}
					}
				} else if (0 == i) {
					// 串首直接插入
					if (null != newSub && 0 < newSubLen) {
						lastNewSub = newSub[0];
						if (null != lastNewSub && lastNewSub.length() > 0) {
							fromIndex = lastNewSub.length();
							sb = new StringBuilder(fromIndex + srcLen);
							sb.append(lastNewSub).append(source);
						}
					}
				} else if (oldSubLen - 1 == i) {
					// 串尾直接追加
					if (null != newSub) {
						if (i < newSubLen) {
							lastNewSub = newSub[i];
						} else if (newSubLen > 0) {
							lastNewSub = newSub[newSubLen - 1];
						}
					}
					if (null != lastNewSub && lastNewSub.length() > 0) {
						if (null == sb) {
							sb = new StringBuilder(srcLen + lastNewSub.length());
							sb.append(source);
						}
						sb.append(lastNewSub);
					}
				}
			}
			if (null != sb) {
				return sb.toString();
			}
		}
		return source;
	}

	/**
	 * @param source
	 * @param ch
	 * @return
	 */
	public static String deleteAll(String source, char ch) {
		if (null != source && !source.isEmpty()) {
			int index = source.lastIndexOf(ch);
			if (index >= 0) {
				StringBuilder sb = new StringBuilder(source);
				for (; index >= 0;) {
					sb.deleteCharAt(index);
					index = source.lastIndexOf(ch, index - 1);
				}
				return sb.toString();
			}
		}
		return source;
	}

	/**
	 * @param source
	 * @param subStr
	 * @return
	 */
	public static String deleteAll(String source, String subStr) {
		if (null != source && !source.isEmpty()) {
			int subLen = subStr.length();
			if (subLen > 0) {
				int index = source.indexOf(subStr);
				if (index >= 0) {
					StringBuilder sb = new StringBuilder(source);
					for (; index >= 0;) {
						sb.delete(index, index + subLen);
						index = sb.indexOf(subStr, index);
					}
					return sb.toString();
				}
			}
		}
		return source;
	}

	/**
	 * 前后使用单引号封闭，中间遇斜杠、单引号、回车、换行符用斜杠转义
	 * <p>
	 * TODO 先简单处理
	 * 
	 * @param source
	 * @return
	 */
	public static String singleQuotes(String source) {
		return replaceAll(source, ESCAPE_OLD_SINGLE, ESCAPE_NEW_SINGLE);
	}

	/**
	 * 前后使用双引号封闭，中间遇斜杠、双引号、回车、换行符用斜杠转义
	 * <p>
	 * TODO 先简单处理
	 * 
	 * @param source
	 * @return
	 */
	public static String doubleQuotes(String source) {
		return replaceAll(source, ESCAPE_OLD_DOUBLE, ESCAPE_NEW_DOUBLE);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of
	 * elements.
	 * </p>
	 *
	 * <p>
	 * No separator is added to the joined String. Null objects or empty strings within the array
	 * are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.join(null)            = null
	 * StringUtil.join([])              = ""
	 * StringUtil.join([null])          = ""
	 * StringUtil.join(["a", "b", "c"]) = "abc"
	 * StringUtil.join([null, "", "a"]) = "a"
	 * </pre>
	 * 
	 * @param objs
	 *            the values to join together, may be null
	 * @return the joined String, {@code null} if null array input
	 * @see #join(String[])
	 * @see #join(String[], String)
	 */
	public static String join(Object... objs) {
		if (null == objs) {
			return null;
		} else if (objs.length == 0) {
			return EMPTY;
		}
		StringBuilder sb = new StringBuilder();
		for (Object obj : objs) {
			if (null == obj) {
				continue;
			} else if (obj instanceof CharSequence) {
				sb.append((CharSequence) obj);
			} else if (obj instanceof Character) {
				sb.append(((Character) obj).charValue());
			} else if (obj instanceof Integer) {
				sb.append(((Integer) obj).intValue());
			} else if (obj instanceof Long) {
				sb.append(((Long) obj).longValue());
			} else if (obj instanceof Boolean) {
				sb.append(((Boolean) obj).booleanValue());
			} else if (obj instanceof Double) {
				sb.append(((Double) obj).doubleValue());
			} else if (obj instanceof Float) {
				sb.append(((Float) obj).floatValue());
			} else {
				sb.append(obj);
			}
		}
		if (sb.length() > 0) {
			return sb.toString();
		} else {
			return EMPTY;
		}
	}

	/**
	 * 
	 * @param source
	 * @param separator
	 *            若null则缺省是逗号
	 * @param doubleQuotes
	 *            表示是否强制使用双引号
	 * @param escape
	 *            表示若遇到双引号则使用转义字符（如'\\'），默认0为重复（双）引号
	 * @return
	 */
	public static String join(String[] source, String separator, boolean doubleQuotes, char escape) {
		if (null != source) {
			int len = source.length;
			if (len > 0) {
				if (null == separator) {
					separator = ARRAY_SEPARATOR;
				}
				if (0 == escape) {
					escape = DOUBLE_QUOTE;
				}
				int total = separator.length() * len;
				if (doubleQuotes) {
					total += 2 * len;
				}
				for (int i = 0; i < len; ++i) {
					total += source[i].length();
				}
				StringBuilder sb = new StringBuilder(total);
				String escapeQuotes = null;
				boolean quoted = false;
				int index;
				for (int i = 0; i < len; ++i) {
					if (null != source[i]) {
						index = indexOf(source[i], DOUBLE_QUOTE, escape);
						if (index >= 0 || doubleQuotes || source[i].indexOf(separator) >= 0) {
							quoted = true;
							sb.append(DOUBLE_QUOTE).append(source[i]);
							if (index >= 0) {
								index += sb.length() - source[i].length();
								if (null == escapeQuotes) {
									char[] replace = { escape, DOUBLE_QUOTE };
									escapeQuotes = String.valueOf(replace);
								}
								for (; index >= 0;) {
									sb.replace(index, index + 1, escapeQuotes);
									index = indexOf(sb, DOUBLE_QUOTES, index + 2, escape);
								}
							}
						} else {
							sb.append(source[i]);
						}
						if (quoted) {
							quoted = false;
							sb.append(DOUBLE_QUOTE);
						}
					}
					sb.append(separator);
				}
				sb.setLength(sb.length() - separator.length());
				return sb.toString();
			}
			return EMPTY;
		}
		return null;
	}

	/**
	 * 
	 * @param source
	 * @param separator
	 *            若null则缺省是逗号
	 * @param doubleQuotes
	 *            表示是否强制使用双引号
	 * @return
	 */
	public static String join(String[] source, String separator, boolean doubleQuotes) {
		return join(source, separator, doubleQuotes, (char) 0);
	}

	/**
	 * 
	 * @param source
	 * @param separator
	 *            若null则缺省是逗号
	 * @return
	 */
	public static String join(String[] source, String separator) {
		return join(source, separator, false, (char) 0);
	}

	/**
	 * 
	 * @param source
	 *            默认使用逗号分隔
	 * @return
	 */
	public static String join(String[] source) {
		return join(source, null, false, (char) 0);
	}

	/**
	 * 
	 * @param source
	 * @param separator
	 *            若null则缺省是逗号
	 * @return
	 */
	public static String join(int[] source, String separator) {
		if (null != source) {
			int len = source.length;
			if (len > 0) {
				if (null == separator) {
					separator = ARRAY_SEPARATOR;
				}
				int total = (separator.length() + 11) * len;
				StringBuilder sb = new StringBuilder(total);
				for (int i = 0; i < len; ++i) {
					sb.append(source[i]).append(separator);
				}
				sb.setLength(sb.length() - separator.length());
				return sb.toString();
			}
			return EMPTY;
		}
		return null;
	}

	/**
	 * 
	 * @param source
	 *            默认使用逗号分隔
	 * @return
	 */
	public static String join(int[] source) {
		return join(source, null);
	}

	/**
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] fromHexString(String hexString) {
		if (null != hexString) {
			byte b[] = new byte[hexString.length() / 2];
			int len = b.length;
			String byteString;
			for (int i = 0; i < len; i++) {
				byteString = hexString.substring(2 * i, 2 * i + 2);
				b[i] = (byte) Integer.parseInt(byteString, 16);
			}
			return b;
		}
		return null;
	}

	/**
	 * 
	 * @param prefix
	 * @param b
	 * @return
	 */
	public static String toHexString(String prefix, byte b[]) {
		if (null != b) {
			int len = b.length;
			if (len > 0) {
				int offset;
				char hex[];
				if (null != prefix && (offset = prefix.length()) > 0) {
					hex = new char[offset + len * 2];
					prefix.getChars(0, offset, hex, 0);
				} else {
					offset = 0;
					hex = new char[len * 2];
				}
				byte n;
				char[] digits = NumberUtil.digits;
				for (int i = 0; i < len; i++) {
					n = b[i];
					hex[offset++] = digits[(n >>> 4) & 0x0f];
					hex[offset++] = digits[n & 0x0f];
				}
				return String.valueOf(hex);
			}
			return EMPTY;
		}
		return null;
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public static String toHexString(byte b[]) {
		return toHexString(null, b);
	}

	/**
	 * ISO_1 (ISO-8859-1) 字符集通常是大部分平台的默认字符集，以此字符集存储的中文需要做转码处理。
	 * 
	 * @param source
	 * @return
	 */
	public static String fromISO1WithGBK(String source) {
		if (null == source || source.isEmpty()) {
			return source;
		} else {
			return new String(source.getBytes(CHARSET_ISO_1), CHARSET_GB18030);
		}
	}

	/**
	 * ISO_1 (ISO-8859-1) 字符集通常是大部分平台的默认字符集，以此字符集存储的中文需要做转码处理。
	 * 
	 * @param source
	 * @return
	 */
	public static String toISO1WithGBK(String source) {
		if (null == source || source.isEmpty()) {
			return source;
		} else {
			return new String(source.getBytes(CHARSET_GB18030), CHARSET_ISO_1);
		}
	}

	/**
	 * ISO_1 (ISO-8859-1) 字符集通常是大部分平台的默认字符集，以此字符集存储的中文需要做转码处理。
	 * 
	 * @param source
	 * @return
	 */
	public static String fromISO1WithUTF8(String source) {
		if (null == source || source.isEmpty()) {
			return source;
		} else {
			return new String(source.getBytes(CHARSET_ISO_1), CHARSET_UTF_8);
		}
	}

	/**
	 * ISO_1 (ISO-8859-1) 字符集通常是大部分平台的默认字符集，以此字符集存储的中文需要做转码处理。
	 * 
	 * @param source
	 * @return
	 */
	public static String toISO1WithUTF8(String source) {
		if (null == source || source.isEmpty()) {
			return source;
		} else {
			return new String(source.getBytes(CHARSET_UTF_8), CHARSET_ISO_1);
		}
	}

	/**
	 * 剥去首尾匹配的引号
	 * 
	 * @param source
	 * @param escape
	 * @return
	 */
	public static String stripQuotes(String source, char escape) {
		int index;
		if (null != source && (index = source.length()) > 0) {
			char ch = source.charAt(0);
			if (('\'' == ch || '"' == ch) && source.charAt(--index) == ch && !isEscaped(source, index, escape)) {
				source = source.substring(1, index);
				if (source.indexOf(ch, 1) > 0) {
					String oldSub = new String(new char[] { escape, ch });
					String newSub = new String(new char[] { ch });
					source = StringUtil.replaceAll(source, oldSub, newSub);
				}
			}
		}
		return source;
	}

	/**
	 * 剥去首尾匹配的引号
	 * 
	 * @param source
	 * @return
	 */
	public static String stripQuotes(String source) {
		return stripQuotes(source, '\\');
	}

	/**
	 * 分析格式串"key1=value1[;,& etc.]key2=value2[...]"，并转换为 Properties 对象
	 * 
	 * @param source
	 * @param regex
	 * @param excludeEmpty
	 *            若true则剔除value为空的，缺省为false
	 * @param upperCase
	 *            若true则设置key为大写，缺省为false
	 * @return 如果一个 key 存在多个 value 的则使用最后一个有效 value。
	 *         <p>
	 *         永远不会返回空值(null)
	 */
	public static Properties parseProperties(String source, String regex, boolean excludeEmpty, boolean upperCase) {
		Properties prop = new Properties();
		if (null != source) {
			int index;
			String key, value;
			String[] pairs = source.split(regex);
			int len = pairs.length;
			for (int i = 0; i < len; ++i) {
				index = pairs[i].indexOf('=');
				if (index <= 0) {
					continue; // 无效key=value键值对
				}
				key = pairs[i].substring(0, index).trim();
				if (key.length() <= 0) {
					continue; // 剔除空key
				}
				value = pairs[i].substring(index + 1).trim();
				value = stripQuotes(value); // 剥去引号
				if (excludeEmpty && value.length() <= 0) {
					continue; // 剔除空value
				}
				if (upperCase) {
					key = key.toUpperCase(); // 转换key为大写
				}
				prop.setProperty(key, value);
			}
		}
		return prop;
	}

	/**
	 * 分析格式串"key1=value1[;,& etc.]key2=value2[...]"，并转换为 Properties 对象
	 * 
	 * @param source
	 * @param regex
	 * @return 如果一个 key 存在多个 value 的则使用最后一个有效 value。
	 *         <p>
	 *         永远不会返回空值(null)
	 */
	public static Properties parseProperties(String source, String regex) {
		return parseProperties(source, regex, false, false);
	}

	/**
	 * 分析格式串"key1=value1[;,& etc.]key2=value2[...]"，并转换为 Map 对象
	 * 
	 * @param source
	 * @param regex
	 * @param multiValue
	 *            如果一个 key 存在多个 value 的则使用 List&lt;String&gt; 类型，否则为 String 类型，缺省为false
	 * @param excludeEmpty
	 *            若true则剔除value为空的，缺省为false
	 * @param caseInsensitive
	 *            若true则设置key为大写，缺省为false
	 * @return 永远不会返回空值(null)
	 */
	public static StringMap<Object> parseKeyValues(String source, String regex, boolean multiValue, boolean excludeEmpty, boolean caseInsensitive) {
		StringMap<Object> keyValues = new StringLinkedHashMap<Object>();
		if (null != source) {
			int index;
			Object objValue;
			String key, strValue;
			String[] pairs = source.split(regex);
			int len = pairs.length;
			keyValues.setCaseInsensitive(caseInsensitive);
			for (int i = 0; i < len; ++i) {
				index = pairs[i].indexOf('=');
				if (index <= 0) {
					continue; // 无效key=value键值对
				}
				key = pairs[i].substring(0, index).trim();
				if (key.length() <= 0) {
					continue; // 剔除空key
				}
				strValue = pairs[i].substring(index + 1).trim();
				strValue = stripQuotes(strValue); // 剥去引号
				if (excludeEmpty && strValue.length() <= 0) {
					continue; // 剔除空value
				}
				if (multiValue && keyValues.containsKey(key)) {
					objValue = keyValues.get(key);
					if (objValue instanceof List) {
						@SuppressWarnings("unchecked")
						List<String> values = (List<String>) objValue;
						values.add(strValue);
						keyValues.put(key, values);
					} else {
						List<String> values = new ArrayList<String>();
						values.add((String) objValue);
						values.add(strValue);
						keyValues.put(key, values);
					}
				} else {
					keyValues.put(key, strValue);
				}
			}
		}
		return keyValues;
	}

	/**
	 * 分析格式串"key1=value1[;,& etc.]key2=value2[...]"，并转换为 Map 对象
	 * 
	 * @param source
	 * @param regex
	 * @param multiValue
	 *            如果一个 key 存在多个 value 的则使用 List&lt;String&gt; 类型，否则为 String 类型。
	 * @return 永远不会返回空值(null)
	 */
	public static StringMap<Object> parseKeyValues(String source, String regex, boolean multiValue) {
		return parseKeyValues(source, regex, multiValue, false, false);
	}

	/**
	 * 分析格式串"key1=value1[;,& etc.]key2=value2[...]"，并转换为 Map 对象
	 * 
	 * @param source
	 * @param regex
	 * @return 永远不会返回空值(null)
	 */
	public static StringMap<Object> parseKeyValues(String source, String regex) {
		return parseKeyValues(source, regex, false, false, false);
	}

	/**
	 * 追加重复 repeatStr 并达到 totalLength 长度，超过长度的最后 repeatStr 部分截除
	 * 
	 * @param sb
	 *            在此基础上追加，本长度不计入 totalLength
	 * @param repeatStr
	 *            不允许为空值(null)或空串("")，否则不做任何操作
	 * @param totalLength
	 *            应该大于 0，否则不做任何操作
	 * @return
	 */
	public static void padding(StringBuilder sb, String repeatStr, int totalLength) {
		final int repeatLen;
		if (null != repeatStr && (repeatLen = repeatStr.length()) > 0) {
			final int total = totalLength / repeatLen;
			for (int i = 0; i < total; ++i) {
				sb.append(repeatStr);
			}
			final int remainder = totalLength % repeatLen;
			if (remainder > 0) {
				sb.append(repeatStr.substring(0, remainder));
			}
		}
	}

	/**
	 * @param repeatStr
	 *            不允许为空值(null)或空串("")，否则直接返回空串("")
	 * @param totalLength
	 *            应该大于 0，否则直接返回空串("")
	 * @return
	 */
	public static String padding(String repeatStr, int totalLength) {
		if (totalLength <= 0 || null == repeatStr || repeatStr.length() <= 0) {
			return EMPTY;
		} else {
			StringBuilder sb = new StringBuilder(totalLength);
			padding(sb, repeatStr, totalLength);
			return sb.toString();
		}
	}

	/**
	 * 
	 * @param source
	 *            若null，则返回null
	 * @param leadding
	 *            若true，则前面填补，否则后面填补
	 * @param repeatStr
	 *            不允许为空值(null)或空串("")，否则直接返回source
	 * @param totalLength
	 *            应该大于source长度，否则直接返回source
	 * @return
	 */
	public static String padding(String source, boolean leadding, String repeatStr, int totalLength) {
		if (null == source || source.length() >= totalLength || null == repeatStr || repeatStr.length() <= 0) {
			return source;
		}
		StringBuilder sb = new StringBuilder(totalLength);
		if (!leadding) {
			sb.append(source);
		}
		totalLength -= source.length();
		padding(sb, repeatStr, totalLength);
		if (leadding) {
			sb.append(source);
		}
		return sb.toString();
	}

	/**
	 * @param repeatChar
	 *            不允许为空值(null)或空串("")，否则直接返回source
	 * @param totalLength
	 *            应该大于source长度，否则直接返回source
	 * @return
	 */
	public static String padding(char repeatChar, int totalLength) {
		if (totalLength <= 0) {
			return EMPTY;
		}
		StringBuilder sb = new StringBuilder(totalLength);
		if (totalLength >= 8) {
			for (int i = 0; i < 8; ++i) {
				sb.append(repeatChar);
			}
			totalLength -= 8;
			if (totalLength >= 8) {
				padding(sb, sb.toString(), totalLength);
				return sb.toString();
			}
		}
		for (int j = 0; j < totalLength; ++j) {
			sb.append(repeatChar);
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static int getChars(String source, char[] target) {
		if (null != source) {
			int inputLen = source.length();
			if (inputLen > 0) {
				int outputLen = target.length;
				if (inputLen > outputLen) {
					inputLen = outputLen;
				}
				source.getChars(0, inputLen, target, 0);
			}
			return inputLen;
		}
		return (0);
	}

	/**
	 * 
	 * @param source
	 * @param outputLen
	 * @return
	 */
	public static char[] getChars(String source, int outputLen) {
		if (null != source) {
			int inputLen = source.length();
			if (outputLen <= 0) {
				outputLen = inputLen;
			} else if (inputLen > outputLen) {
				inputLen = outputLen;
			}
			char[] target = new char[outputLen];
			if (inputLen > 0) {
				source.getChars(0, inputLen, target, 0);
			}
			return target;
		}
		return null;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static char[] getChars(String source) {
		return getChars(source, 0);
	}

	/**
	 * 
	 * @param charset
	 *            不允许null
	 * @return
	 */
	public static CharsetEncoder getCharsetEncoder(Charset charset) {
		return charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
	}

	/**
	 * 
	 * @param charsetName
	 * @return
	 */
	public static CharsetEncoder getCharsetEncoder(String charsetName) {
		return getCharsetEncoder(Charset.forName(charsetName));
	}

	/**
	 * 从<b>cb</b>字符缓冲区中读取字符并编码到<b>bb</b>字节缓冲区，直至尽可能填满<b>bb</b>字节缓冲区为止，
	 * 但不截断CJK等任何多字节字符集。也即从字符串中读取前n个字节但不截断汉字。
	 * 
	 * @param cb
	 *            输入字符缓冲区，不允许null，最后读取位置为 cb.position()
	 * @param bb
	 *            输出字节缓冲区，不允许null，最后写入位置为 bb.position()
	 * @param ce
	 *            不局限于GB2312、GB18030等任何字符集，不允许null
	 */
	public static void getBytes(CharBuffer cb, ByteBuffer bb, CharsetEncoder ce) {
		try {
			CoderResult cr = ce.encode(cb, bb, true);
			if (!cr.isUnderflow()) {
				if (!cr.isOverflow()) {
					cr.throwException();
				}
			}
			cr = ce.flush(bb);
			if (!cr.isUnderflow()) {
				if (!cr.isOverflow()) {
					cr.throwException();
				}
			}
		} catch (CharacterCodingException x) {
			// Substitution is always enabled,
			// so this shouldn't happen
			throw new RuntimeException(x);
		}
	}

	/**
	 * 类似 new String(byte[] bytes, String charsetName), 但隐藏罕见的UnsupportedEncodingException异常。
	 * 
	 * @param bytes
	 * @param charsetName
	 * @return
	 */
	public static String valueOf(byte[] bytes, String charsetName) {
		if (null != bytes) {
			try {
				return new String(bytes, charsetName);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/**
	 * 类似 new String(byte[] bytes, int offset, int length, String charsetName),
	 * 但隐藏罕见的UnsupportedEncodingException异常。
	 * 
	 * @param bytes
	 * @param offset
	 * @param length
	 * @param charsetName
	 * @return
	 */
	public static String valueOf(byte[] bytes, int offset, int length, String charsetName) {
		if (null != bytes) {
			try {
				return new String(bytes, offset, length, charsetName);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/**
	 * 类似 new String(byte[] bytes, String charsetName), 但隐藏罕见的UnsupportedEncodingException异常。
	 * 
	 * @param bb
	 *            不允许null
	 * @param charsetName
	 * @return
	 */
	public static String valueOf(ByteBuffer bb, String charsetName) {
		if (null != bb) {
			try {
				return new String(bb.array(), 0, bb.position(), charsetName);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/**
	 * 类似 String.valueOf(Object obj), 但对Number和Date类型做了兼容JavaScript等ECMAScript语言的处理。
	 * 
	 * @param obj
	 *            an Object, may be null
	 * @param defaultValue
	 *            the default value to return, may be null
	 * @return obj.toString() if it is not null, defaultValue otherwise
	 */
	public static String valueOf(Object obj, String defaultValue) {
		if (null == obj) {
			return defaultValue;
		} else if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof Number) {
			return NumberUtil.toString((Number) obj);
		} else if (obj instanceof Date) {
		} else if (obj instanceof Calendar) {
		} else {
			return obj.toString();
		}
		// TODO
		return null;
	}

	/**
	 * 类似 String.valueOf(Object obj), 但对Number和Date类型做了兼容JavaScript等ECMAScript语言的处理。
	 * 
	 * @param obj
	 *            an Object, may be null
	 * @return if the argument is null, then a string equal to "null"; otherwise, the value of
	 *         obj.toString() is returned.
	 */
	public static String valueOf(Object obj) {
		return valueOf(obj, "null");
	}

	/**
	 * 类似 String.valueOf(Object obj), 但对Number和Date类型做了兼容JavaScript等ECMAScript语言的处理。
	 * 
	 * @param obj
	 *            an Object, may be null
	 * @return if the argument is null, then a string equal to "null"; otherwise, the value of
	 *         obj.toString() is returned.
	 */
	public static String valueOf(Object obj, int ellipsisLen) {
		String text = valueOf(obj, "null");
		if (ellipsisLen > 3 && text.length() > ellipsisLen) {
			StringBuilder sb = new StringBuilder(text.substring(0, ellipsisLen - 3));
			sb.append("...");
			return sb.toString();
		} else {
			return text;
		}
	}

	/**
	 * 类似 String.toCharArray()
	 * 
	 * @param sb
	 *            不允许null
	 * @return 永远不会返回null
	 */
	public static char[] toCharArray(StringBuilder sb) {
		int count = sb.length();
		char result[] = new char[count];
		sb.getChars(0, count, result, 0);
		return result;
	}

	/**
	 * 类似 String.getBytes(String charsetName), 但隐藏罕见的UnsupportedEncodingException异常。
	 * 
	 * @param sb
	 *            不允许null
	 * @param charsetName
	 * @return 永远不会返回null
	 */
	public static byte[] getBytes(final StringBuilder sb, final String charsetName) {
		if (sb.length() > 0) {
			try {
				return sb.toString().getBytes(charsetName);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		} else {
			return (new byte[0]);
		}
	}

	/**
	 * 类似 String.getBytes(Charset charset)
	 * 
	 * @param sb
	 *            不允许null
	 * @param charset
	 * @return 永远不会返回null
	 */
	public static byte[] getBytes(final StringBuilder sb, final Charset charset) {
		if (sb.length() > 0) {
			return sb.toString().getBytes(charset);
		} else {
			return (new byte[0]);
		}
	}

	/**
	 * 同 String.getBytes(String charsetName), 但隐藏罕见的UnsupportedEncodingException异常。
	 * 
	 * @param s
	 *            允许null
	 * @param charsetName
	 * @return
	 */
	public static byte[] getBytes(final String s, final String charsetName) {
		if (null != s) {
			if (s.length() > 0) {
				try {
					return s.getBytes(charsetName);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			} else {
				return (new byte[0]);
			}
		} else {
			return null;
		}
	}

	/**
	 * 同 String.getBytes(Charset charset)
	 * 
	 * @param s
	 *            允许null
	 * @param charset
	 * @return
	 */
	public static byte[] getBytes(final String s, final Charset charset) {
		if (null != s) {
			if (s.length() > 0) {
				return s.getBytes(charset);
			} else {
				return (new byte[0]);
			}
		} else {
			return null;
		}
	}

	/**
	 * 按指定字符集编码的最大字节长度分段截取为数组
	 * 
	 * @param source
	 * @param maxBytes
	 * @param charsetName
	 * @param firstMD5
	 *            若firstMD5为真，则返回数组的首项是所有字节的MD5
	 * @return
	 */
	public static String[] split(final String source, final int maxBytes, final String charsetName, final boolean firstMD5) {
		CharsetEncoder ce = getCharsetEncoder(charsetName);
		char[] chars = source.toCharArray();
		CharBuffer cb = CharBuffer.wrap(chars);
		byte[] bytes = new byte[maxBytes];
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		ArrayList<String> list = new ArrayList<String>((chars.length / maxBytes) + 10);
		MessageDigest digest = null;
		try {
			if (firstMD5) {
				digest = MD5Util.getInstance();
				list.add(digest.getAlgorithm());
			}
			int len = cb.remaining();
			if (len <= 0) {
				list.add(EMPTY);
			}
			for (boolean first = true; len > 0;) {
				if (first) {
					first = false;
				} else {
					ce.reset();
					bb.clear();
				}
				StringUtil.getBytes(cb, bb, ce);
				len = bb.position();
				if (len > 0) {
					if (null != digest) {
						digest.update(bytes, 0, len);
					}
					list.add(new String(bytes, 0, len, charsetName));
					len = cb.remaining();
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		String[] sa = list.toArray(new String[list.size()]);
		if (null != digest) {
			sa[0] = toHexString(digest.digest());
		}
		return sa;
	}

	/**
	 * 按指定字符集编码的最大字节长度分段截取为数组
	 * 
	 * @param source
	 * @param maxBytes
	 * @param charsetName
	 * @return
	 */
	public static String[] split(final String source, final int maxBytes, final String charsetName) {
		return split(source, maxBytes, charsetName, false);
	}

	/**
	 * 
	 * @param source
	 * @param len
	 *            如果 len < 0 则表示从右侧（结尾部分）裁减 len 个字符
	 * @return
	 */
	public static String left(final String source, final int len) {
		final int endIndex;
		if (null != source && (endIndex = source.length()) > 0) {
			if (len == 0 || (endIndex + len) <= 0) {
				return EMPTY;
			} else if (len < 0) {
				return source.substring(0, endIndex + len);
			} else if (endIndex > len) {
				return source.substring(0, len);
			}
		}
		return source;
	}

	/**
	 * 
	 * @param source
	 * @param beginIndex
	 *            若 beginIndex < 0 则忽略为 0
	 * @param len
	 *            如果 len < 0 则表示从右侧（结尾部分）裁减 len 个字符
	 * @return
	 */
	public static String mid(final String source, final int beginIndex, final int len) {
		int endIndex;
		if (null != source && (endIndex = source.length()) > 0) {
			if (len == 0 || (endIndex - beginIndex + len) <= 0) {
				return EMPTY;
			}
			if (beginIndex > 0) {
				if (endIndex <= beginIndex) {
					return EMPTY;
				} else if (len < 0) {
					return source.substring(beginIndex, endIndex + len);
				} else {
					final int midEndIndex = beginIndex + len;
					if (endIndex > midEndIndex) {
						endIndex = midEndIndex;
					}
					return source.substring(beginIndex, endIndex);
				}
			} else if (len < 0) {
				return source.substring(0, endIndex + len);
			} else if (endIndex > len) {
				return source.substring(0, len);
			}
		}
		return source;
	}

	/**
	 * 
	 * @param source
	 * @param beginIndex
	 *            若 beginIndex < 0 则忽略为 0
	 * @return
	 */
	public static String mid(final String source, final int beginIndex) {
		int endIndex;
		if (beginIndex > 0 && null != source && (endIndex = source.length()) > 0) {
			if (endIndex <= beginIndex) {
				return EMPTY;
			} else {
				return source.substring(beginIndex, endIndex);
			}
		}
		return source;
	}

	/**
	 * 
	 * @param source
	 * @param len
	 *            如果 len < 0 则表示从左侧（开始部分）裁减 len 个字符
	 * @return
	 */
	public static String right(final String source, final int len) {
		int endIndex;
		if (null != source && (endIndex = source.length()) > 0) {
			if (len == 0 || (endIndex + len) <= 0) {
				return EMPTY;
			} else if (len < 0) {
				return source.substring(-len, endIndex);
			} else if (endIndex > len) {
				return source.substring(endIndex - len, endIndex);
			}
		}
		return source;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static String leftTrim(final String source) {
		int len;

		if (null != source && (len = source.length()) > 0) {
			int index = 0;
			while ((index < len) && (source.charAt(index) <= ' ')) {
				++index;
			}
			if (index > 0) {
				if (index < len) {
					return source.substring(index, len);
				} else {
					return EMPTY;
				}
			}
		}
		return source;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static String rightTrim(final String source) {
		int len;

		if (null != source && (len = source.length()) > 0) {
			int lastIndex = len - 1;
			int index = lastIndex;
			while ((index >= 0) && (source.charAt(index) <= ' ')) {
				--index;
			}
			if (index < lastIndex) {
				if (index < 0) {
					return EMPTY;
				} else {
					return source.substring(0, index + 1);
				}
			}
		}
		return source;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static String trim(final String source) {
		if (null != source && source.length() > 0) {
			return source.trim();
		}
		return source;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static String trimToNull(final String source) {
		if (null != source && source.length() > 0) {
			final String trimedSource = source.trim();
			if (NullUtil.isNotNull(trimedSource)) {
				return trimedSource;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static String trimToEmpty(final String source) {
		if (null != source && source.length() > 0) {
			final String trimedSource = source.trim();
			if (trimedSource.length() > 0) {
				return trimedSource;
			}
		}
		return EMPTY;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isEmpty(final String source) {
		if (null != source && source.length() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 转换本地数字和字母（如中文数字和字母）为ASCII数字和字母
	 * 
	 * @param source
	 * @param underscore
	 * @param removeWhitespace
	 * @param upperOffset
	 * @param lowerOffset
	 * @param maxCount
	 * @return
	 */
	private static String convertNativeLetterOrDigit(String source, boolean underscore, boolean removeWhitespace, int upperOffset, int lowerOffset,
			int maxCount) {
		if (null == source || source.isEmpty()) {
			return source;
		}
		int len = source.length();
		if (maxCount <= 0 || maxCount > len) {
			maxCount = len;
		}

		int ch, val;
		boolean whitespace = false;
		StringBuilder sb = new StringBuilder(maxCount + (removeWhitespace ? 0 : 16));
		for (int i = 0, count = 0; i < len && count < maxCount; ++i) {
			ch = source.charAt(i);
			if (Character.isWhitespace(ch)) {
				if (!removeWhitespace && !whitespace && count > 0) {
					whitespace = true;
					sb.append(' ');
				}
			} else {
				if (whitespace) {
					whitespace = false;
				}
				if (Character.isDigit(ch)) {
					val = Character.getNumericValue(ch);
					if (val >= 0) {
						sb.append((char) (val + CHAR_DIGIT_OFFSET));
						++count;
					}
				} else if (Character.isUpperCase(ch)) {
					val = Character.getNumericValue(ch);
					if (val >= 0) {
						sb.append((char) (val + upperOffset));
						++count;
					}
				} else if (Character.isLowerCase(ch)) {
					val = Character.getNumericValue(ch);
					if (val >= 0) {
						sb.append((char) (val + lowerOffset));
						++count;
					}
				} else if (underscore && ch == '_') {
					val = 0;
					sb.append((char) ch);
					++count;
				} else {
					val = -1;
				}
				if (val < 0) {
					throw new IllegalArgumentException("发现字符不是数字(0-9)和字母(A-Z)！");
				}
			}
		}
		if (whitespace) {
			// 去除尾部的空格
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 转换本地数字和字母（如中文数字和字母）为ASCII数字和字母，大小写不敏感
	 * 
	 * @param source
	 * @param underscore
	 * @param removeWhitespace
	 * @param upperCase
	 * @param maxCount
	 * @return
	 */
	public static String convertNativeLetterOrDigit(String source, boolean underscore, boolean removeWhitespace, boolean upperCase, int maxCount) {
		if (upperCase) {
			return convertNativeLetterOrDigit(source, underscore, removeWhitespace, CHAR_UPPER_OFFSET, CHAR_UPPER_OFFSET, maxCount);
		} else {
			return convertNativeLetterOrDigit(source, underscore, removeWhitespace, CHAR_LOWER_OFFSET, CHAR_LOWER_OFFSET, maxCount);
		}
	}

	/**
	 * 转换本地数字和字母（如中文数字和字母）为ASCII数字和字母，大小写不敏感
	 * 
	 * @param source
	 * @param underscore
	 * @param removeWhitespace
	 * @param upperCase
	 * @return
	 */
	public static String convertNativeLetterOrDigit(String source, boolean underscore, boolean removeWhitespace, boolean upperCase) {
		if (upperCase) {
			return convertNativeLetterOrDigit(source, underscore, removeWhitespace, CHAR_UPPER_OFFSET, CHAR_UPPER_OFFSET, 0);
		} else {
			return convertNativeLetterOrDigit(source, underscore, removeWhitespace, CHAR_LOWER_OFFSET, CHAR_LOWER_OFFSET, 0);
		}
	}

	/**
	 * 转换本地数字和字母（如中文数字和字母）为ASCII数字和字母，大小写敏感
	 * 
	 * @param source
	 * @param underscore
	 * @param removeWhitespace
	 * @param maxCount
	 * @return
	 */
	public static String convertNativeLetterOrDigit(String source, boolean underscore, boolean removeWhitespace, int maxCount) {
		return convertNativeLetterOrDigit(source, underscore, removeWhitespace, CHAR_UPPER_OFFSET, CHAR_LOWER_OFFSET, maxCount);
	}

	/**
	 * 转换本地数字和字母（如中文数字和字母）为ASCII数字和字母，大小写敏感
	 * 
	 * @param source
	 * @param underscore
	 * @param removeWhitespace
	 * @return
	 */
	public static String convertNativeLetterOrDigit(String source, boolean underscore, boolean removeWhitespace) {
		return convertNativeLetterOrDigit(source, underscore, removeWhitespace, CHAR_UPPER_OFFSET, CHAR_LOWER_OFFSET, 0);
	}

	/**
	 * 转换本地字母（如中文字母）为ASCII字母
	 * 
	 * @param source
	 * @param removeWhitespace
	 * @param upperOffset
	 * @param lowerOffset
	 * @param maxCount
	 * @return
	 */
	private static String convertNativeLetter(String source, boolean removeWhitespace, int upperOffset, int lowerOffset, int maxCount) {
		if (null == source || source.isEmpty()) {
			return source;
		}
		int len = source.length();
		if (maxCount <= 0 || maxCount > len) {
			maxCount = len;
		}

		int ch, val;
		boolean whitespace = false;
		StringBuilder sb = new StringBuilder(maxCount + (removeWhitespace ? 0 : 16));
		for (int i = 0, count = 0; i < len && count < maxCount; ++i) {
			ch = source.charAt(i);
			if (Character.isWhitespace(ch)) {
				if (!removeWhitespace && !whitespace && count > 0) {
					whitespace = true;
					sb.append(' ');
				}
			} else {
				if (whitespace) {
					whitespace = false;
				}
				if (Character.isUpperCase(ch)) {
					val = Character.getNumericValue(ch);
					if (val >= 0) {
						sb.append((char) (val + upperOffset));
						++count;
					}
				} else if (Character.isLowerCase(ch)) {
					val = Character.getNumericValue(ch);
					if (val >= 0) {
						sb.append((char) (val + lowerOffset));
						++count;
					}
				} else {
					val = -1;
				}
				if (val < 0) {
					throw new IllegalArgumentException("发现字符不是字母(A-Z)！");
				}
			}
		}
		if (whitespace) {
			// 去除尾部的空格
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 转换本地字母（如中文字母）为ASCII字母，大小写不敏感
	 * 
	 * @param source
	 * @param removeWhitespace
	 * @param upperCase
	 * @param maxCount
	 * @return
	 */
	public static String convertNativeLetter(String source, boolean removeWhitespace, boolean upperCase, int maxCount) {
		if (upperCase) {
			return convertNativeLetter(source, removeWhitespace, CHAR_UPPER_OFFSET, CHAR_UPPER_OFFSET, maxCount);
		} else {
			return convertNativeLetter(source, removeWhitespace, CHAR_LOWER_OFFSET, CHAR_LOWER_OFFSET, maxCount);
		}
	}

	/**
	 * 转换本地字母（如中文字母）为ASCII字母，大小写不敏感
	 * 
	 * @param source
	 * @param removeWhitespace
	 * @param upperCase
	 * @return
	 */
	public static String convertNativeLetter(String source, boolean removeWhitespace, boolean upperCase) {
		if (upperCase) {
			return convertNativeLetter(source, removeWhitespace, CHAR_UPPER_OFFSET, CHAR_UPPER_OFFSET, 0);
		} else {
			return convertNativeLetter(source, removeWhitespace, CHAR_LOWER_OFFSET, CHAR_LOWER_OFFSET, 0);
		}
	}

	/**
	 * 转换本地字母（如中文字母）为ASCII字母，大小写敏感
	 * 
	 * @param source
	 * @param removeWhitespace
	 * @param maxCount
	 * @return
	 */
	public static String convertNativeLetter(String source, boolean removeWhitespace, int maxCount) {
		return convertNativeLetter(source, removeWhitespace, CHAR_UPPER_OFFSET, CHAR_LOWER_OFFSET, maxCount);
	}

	/**
	 * 转换本地字母（如中文字母）为ASCII字母，大小写敏感
	 * 
	 * @param source
	 * @param removeWhitespace
	 * @return
	 */
	public static String convertNativeLetter(String source, boolean removeWhitespace) {
		return convertNativeLetter(source, removeWhitespace, CHAR_UPPER_OFFSET, CHAR_LOWER_OFFSET, 0);
	}

	/**
	 * 转换本地数字（如中文数字）为ASCII数字
	 * 
	 * @param source
	 * @param removeWhitespace
	 * @param maxCount
	 * @return
	 */
	public static String convertNativeDigit(String source, boolean removeWhitespace, int maxCount) {
		if (null == source || source.isEmpty()) {
			return source;
		}
		int len = source.length();
		if (maxCount <= 0 || maxCount > len) {
			maxCount = len;
		}

		int ch, val;
		boolean whitespace = false;
		StringBuilder sb = new StringBuilder(maxCount + (removeWhitespace ? 0 : 16));
		for (int i = 0, count = 0; i < len && count < maxCount; ++i) {
			ch = source.charAt(i);
			if (Character.isWhitespace(ch)) {
				if (!removeWhitespace && !whitespace && count > 0) {
					whitespace = true;
					sb.append(' ');
				}
			} else {
				if (whitespace) {
					whitespace = false;
				}
				if (Character.isDigit(ch)) {
					val = Character.getNumericValue(ch);
					if (val >= 0) {
						sb.append((char) (val + CHAR_DIGIT_OFFSET));
						++count;
					}
				} else {
					val = -1;
				}
				if (val < 0) {
					throw new IllegalArgumentException("发现字符不是数字(0-9)！");
				}
			}
		}
		if (whitespace) {
			// 去除尾部的空格
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 转换本地数字（如中文数字）为ASCII数字
	 * 
	 * @param source
	 * @param removeWhitespace
	 * @return
	 */
	public static String convertNativeDigit(String source, boolean removeWhitespace) {
		return convertNativeDigit(source, removeWhitespace, 0);
	}

	/**
	 * @param source
	 * @param maskingChar
	 *            如 '*', 'x', 'X'
	 * @param leftReserved
	 *            左侧保留字符数，负数表示弱，若中间至少数不足则可占用
	 * @param rightReserved
	 *            右侧保留字符数，负数表示弱，若中间至少数不足则可占用，右侧比左侧优先占用，也优先补贴
	 * @param middleLeast
	 *            中间保留字符数，负数表示至少，正数表示最多，0表示无特殊要求
	 * @return
	 */
	public static String masking(String source, char maskingChar, int leftReserved, int rightReserved, int middleLeast) {
		int len;
		if (null != source && (len = source.length()) > 0) {
			boolean leftWeak, rightWeak;
			if (leftReserved <= 0) {
				// 负数表示弱
				leftReserved = -leftReserved;
				leftWeak = true;
			} else {
				leftWeak = false;
			}
			if (rightReserved <= 0) {
				// 负数表示弱
				rightReserved = -rightReserved;
				rightWeak = true;
			} else {
				rightWeak = false;
			}
			int index = len - rightReserved;
			if (middleLeast < 0) {
				// 负数表示至少，计算不足部分
				middleLeast = -middleLeast;
				int delta = leftReserved + middleLeast - index;
				// 中间至少数未达到，占用弱右侧
				if (delta > 0 && rightWeak) {
					if (rightReserved >= delta) {
						index += delta;
						delta = 0;
					} else {
						index += rightReserved;
						delta -= rightReserved;
					}
				}
				if (delta > 0 && leftWeak) {
					// 中间至少数未达到，占用弱左侧
					if (leftReserved >= delta) {
						leftReserved -= delta;
						delta = 0;
					} else {
						delta -= leftReserved;
						leftReserved = 0;
					}
				}
			} else if (middleLeast > 0) {
				// 正数表示最多，计算多余部分
				int delta = index - leftReserved - middleLeast;
				if (delta > 0) {
					if (!rightWeak || leftWeak) {
						// 中间最多数超出，补贴强右侧
						index -= delta;
					} else {
						// 中间最多数超出，补贴强左侧
						leftReserved += delta;
					}
				}
			}
			if (leftReserved < index) {
				StringBuilder sb = new StringBuilder(source);
				for (int i = leftReserved; i < index; ++i) {
					sb.setCharAt(i, maskingChar);
				}
				source = sb.toString();
			}
		}
		return source;
	}

	/**
	 * @param source
	 * @param leftReserved
	 *            左侧保留字符数，负数表示弱，若中间至少数不足则可占用
	 * @param rightReserved
	 *            右侧保留字符数，负数表示弱，若中间至少数不足则可占用，右侧比左侧优先占用，也优先补贴
	 * @param middleLeast
	 *            中间保留字符数，负数表示至少，正数表示最多，0表示无特殊要求
	 * @return
	 */
	public static String masking(String source, int leftReserved, int rightReserved, int middleLeast) {
		return masking(source, '*', leftReserved, rightReserved, middleLeast);
	}

	/**
	 * @param source
	 * @param leftReserved
	 *            左侧保留字符数，负数表示弱，若中间至少数不足则可占用
	 * @param rightReserved
	 *            右侧保留字符数，负数表示弱，若中间至少数不足则可占用，右侧比左侧优先占用，也优先补贴
	 * @return
	 */
	public static String masking(String source, int leftReserved, int rightReserved) {
		return masking(source, leftReserved, rightReserved, 0);
	}

	/**
	 * 可用于隐藏姓（包括复姓）
	 * 
	 * @param source
	 * @return 至少隐藏一个汉字姓，最多保留两个汉字名
	 */
	public static String masking021(String source) {
		return masking(source, 0, -2, -1);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking11(String source) {
		return masking(source, 1, 1, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking22(String source) {
		return masking(source, 2, 2, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking32(String source) {
		return masking(source, 3, 2, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking33(String source) {
		return masking(source, 3, 3, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking34(String source) {
		return masking(source, 3, 4, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking42(String source) {
		return masking(source, 4, 2, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking43(String source) {
		return masking(source, 4, 3, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking44(String source) {
		return masking(source, 4, 4, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking52(String source) {
		return masking(source, 5, 2, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking53(String source) {
		return masking(source, 5, 3, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking54(String source) {
		return masking(source, 5, 4, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking63(String source) {
		return masking(source, 6, 3, 0);
	}

	/**
	 * @param source
	 * @return
	 */
	public static String masking64(String source) {
		return masking(source, 6, 4, 0);
	}

	/**
	 * 比较两个String数组,返回相同的并且无重复String数组
	 * 
	 * @param target1
	 * @param target2
	 * @Date 2014年06月24日
	 * @return 返回相同的并且无重复String数组
	 * @author CaiBo
	 */

	public static String[] comparingSame(String[] target1, String[] target2) {
		Set<String> set = new HashSet<String>();
		Arrays.sort(target1);
		Arrays.sort(target2);
		for (int i = 0, k = 0; i < target1.length && k < target2.length;) {
			String pointerFront = target1[i];
			String pointerLater = target2[k];
			if (pointerFront.compareTo(pointerLater) == 0) {
				set.add(pointerFront);
				i++;
			} else if (pointerFront.compareTo(pointerLater) < 0) {
				i++;
			} else {
				k++;
			}
		}
		return set.toArray(new String[0]);
	}

	/**
	 * 得到指定字节长度的字符串
	 * <p>
	 * 当{@link#newLength}小于原字符串的长度时,原字符串将被截断,中文不会因为截断而产生乱码<br>
	 * 将以“ ”(空格)来填充剩余
	 * 
	 * @param original
	 *            原始字符串
	 * @param newLength
	 *            新的长度(字节数)
	 * @param charset
	 *            字符编码;如果为<code>null</code>,将采用当前文件系统编码或者“UTF-8”
	 * @return 指定字节数的字符串
	 * @author CaiBo
	 */
	public static String getStrAsNewLength(final String original, final int newLength, Charset charset) {
		if (null == original || newLength < 0) {
			return null;
		}
		if (null == charset) {
			charset = Charset.defaultCharset();
		}
		byte[] bytes = new byte[newLength];
		byte[] originalBytes = original.getBytes(charset);
		if (originalBytes.length < newLength) {
			int byteOfSpace = SPACE.getBytes(charset)[0];
			for (int i = 0; i < newLength; i++) {
				if (i < originalBytes.length) {
					bytes[i] = originalBytes[i];
				} else {
					bytes[i] = (byte) byteOfSpace;
				}
			}
		} else if (originalBytes.length > newLength) {
			String first = split(original, newLength, charset.name())[0];
			byte[] newStrBytes = first.getBytes(charset);
			if (newStrBytes.length < newLength) {
				int byteOfSpace = SPACE.getBytes(charset)[0];
				for (int i = 0; i < newLength; i++) {
					if (i <= newStrBytes.length) {
						bytes[i] = newStrBytes[i];
					} else {
						bytes[i] = (byte) byteOfSpace;
					}
				}
			}
		} else {
			return original;
		}
		return new String(bytes, charset);
	}
}
