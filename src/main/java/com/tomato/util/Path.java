package com.tomato.util;

import java.util.Calendar;
import java.util.Date;

/**
 * The type Path.
 */
public class Path {
    /**
     * The constant CAPACITY.
     */
    protected static final int CAPACITY = 32;
    /**
     * The constant SLASH.
     */
    protected static final char SLASH = '/';
    /**
     * The constant BACKSLASH.
     */
    protected static final char BACKSLASH = '\\';
    private char separator = SLASH;
    private char revSeparator = BACKSLASH;
    private boolean allowDuplicatedSeparator = true;
    private final StringBuilder path;

    /**
     * Instantiates a new Path.
     */
    public Path() {
        path = new StringBuilder(CAPACITY);
    }

    /**
     * Instantiates a new Path.
     *
     * @param capacity
     *         the capacity
     */
    public Path(int capacity) {
        if (capacity <= 0) {
            capacity = CAPACITY;
        }
        this.path = new StringBuilder(capacity);
    }

    /**
     * Instantiates a new Path.
     *
     * @param p
     *         the p
     */
    public Path(String p) {
        if (null != p) {
            path = new StringBuilder(p.length() + CAPACITY);
            append(p);
        } else {
            path = new StringBuilder(CAPACITY);
            path.append(separator);
        }
    }

    /**
     * Sets separator.
     *
     * @param separator
     *         the separator
     * @param revSeparator
     *         the rev separator
     */
    public void setSeparator(char separator, char revSeparator) {
        this.separator = separator;
        this.revSeparator = revSeparator;
    }

    /**
     * Is allow duplicated separator boolean.
     *
     * @return the allowDuplicatedSeparator
     */
    public boolean isAllowDuplicatedSeparator() {
        return allowDuplicatedSeparator;
    }

    /**
     * Sets allow duplicated separator.
     *
     * @param allowDuplicatedSeparator
     *         the allowDuplicatedSeparator to set
     */
    public void setAllowDuplicatedSeparator(boolean allowDuplicatedSeparator) {
        this.allowDuplicatedSeparator = allowDuplicatedSeparator;
    }

    /**
     * Is empty boolean.
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return (path.length() <= 0);
    }

    /**
     * Start with separator boolean.
     *
     * @return boolean
     */
    public boolean startWithSeparator() {
        if (path.length() > 0) {
            if (path.charAt(0) == separator) {
                return true;
            }
        }
        return false;
    }

    /**
     * End with separator boolean.
     *
     * @return boolean
     */
    public boolean endWithSeparator() {
        int len = path.length();
        if (len > 0) {
            if (path.charAt(len - 1) == separator) {
                return true;
            }
        }
        return false;
    }

    /**
     * Append separator path.
     *
     * @return path
     */
    public Path appendSeparator() {
        if (allowDuplicatedSeparator || !endWithSeparator()) {
            path.append(separator);
        }
        return this;
    }

    /**
     * 将指定 CharSequence 的子序列追加到此序列。
     * <p>
     * 将参数 s 中从索引 start 到索引 end 的字符按顺序追加到此序列。序列的长度将增加 end - start。
     * <p>
     * 在追加第一个字符前，如果 separator 允许重复或者此字符序列不是以 separator 结尾，则追加一个 separator。
     * <p>
     * 如果 s 为 null 或空字符 "" 或者自动调整后 start 不小于 end，则不追加字符。如果 separator 允许重复则追加一个 separator。
     *
     * @param s
     *         要追加的序列。
     * @param start
     *         要追加的子序列的起始索引。允许负数为倒数计数，此时增加 s.length() 自动调整为正向索引。如果 start 仍然为负，则自动调整为 0
     * @param end
     *         要追加的子序列的结束索引。如果 end 为负或者 end 大于 s.length() 则自动调整为 s.length()。通常可用 -1 表示序列尾部，这样不必检查            s 是否为 null
     *
     * @return 此对象的一个引用 。
     */
    public Path append(CharSequence s, int start, int end) {
        int len;
        if (null != s && (len = s.length()) > 0) {
            if (end < 0 || end > len) {
                end = len; // 溢出自动调整，不抛异常
            }
            if (start < 0) {
                start += end; // 允许负数为倒数计数，自动调整为正向索引
                if (start < 0) {
                    start = 0; // 溢出自动调整，不抛异常
                }
            }
            if (start < end) {
                boolean separated = (isEmpty() || (!allowDuplicatedSeparator && endWithSeparator()));
                char c;
                if (separated) {
                    c = s.charAt(start);
                    if (c == separator || c == revSeparator) {
                        separated = false;
                    }
                }
                for (int i = start; i < end; ++i) {
                    c = s.charAt(i);
                    if (c != separator && c != revSeparator) {
                        if (!separated) {
                            path.append(separator);
                            separated = true;
                        }
                        path.append(c);
                    } else if (separated) {
                        separated = false;
                    }
                }
                return this;
            }
        }
        if (allowDuplicatedSeparator) {
            path.append(separator);
        }
        return this;
    }

    /**
     * 将指定 CharSequence 的序列追加到此序列。
     * <p>
     * 在追加第一个字符前，如果 separator 允许重复或者此字符序列不是以 separator 结尾，则追加一个 separator。
     * <p>
     * 如果 s 为 null 或空字符 ""，则不追加字符。如果 separator 允许重复则追加一个 separator。
     *
     * @param s
     *         要追加的序列。
     *
     * @return 此对象的一个引用 。
     */
    public Path append(CharSequence s) {
        return append(s, 0, -1);
    }

    /**
     * 将指定的字符串追加到此字符序列。
     * <p>
     * 按顺序追加 String 变量中的字符，此序列将增加该变量的长度。
     * <p>
     * 在追加第一个字符前，如果 separator 允许重复或者此字符序列不是以 separator 结尾，则追加一个 separator。
     * <p>
     * 如果 s 为 null 或空字符 ""，则不追加字符。如果 separator 允许重复则追加一个 separator。
     *
     * @param s
     *         一个 string
     *
     * @return 此对象的一个引用 。
     */
    public Path append(String s) {
        return append(s, 0, -1);
    }

    /**
     * Append path.
     *
     * @param n
     *         the n
     *
     * @return path
     */
    public Path append(Number n) {
        return append(NumberUtil.toString(n));
    }

    /**
     * Append path.
     *
     * @param n
     *         the n
     *
     * @return path
     */
    public Path append(int n) {
        return append(String.valueOf(n));
    }

    /**
     * Append path.
     *
     * @param n
     *         the n
     *
     * @return path
     */
    public Path append(long n) {
        return append(String.valueOf(n));
    }

    /**
     * Append path.
     *
     * @param n
     *         the n
     *
     * @return path
     */
    public Path append(short n) {
        return append(String.valueOf(n));
    }

    /**
     * Append path.
     *
     * @param n
     *         the n
     *
     * @return path
     */
    public Path append(byte n) {
        return append(String.valueOf(n));
    }

    /**
     * Append path.
     *
     * @param c
     *         the c
     *
     * @return path
     */
    public Path append(char c) {
        return append(String.valueOf(c));
    }

    /**
     * Append path.
     *
     * @param b
     *         the b
     *
     * @return path
     */
    public Path append(boolean b) {
        return append(String.valueOf(b));
    }

    /**
     * 日期转换为 yyyyMMdd 数字格式
     *
     * @param d
     *         the d
     *
     * @return path
     */
    public Path append(Date d) {
        return append(DateUtil.getDateInt(d));
    }

    /**
     * Append path.
     *
     * @param o
     *         the o
     *
     * @return path
     */
    public Path append(Object o) {
        if (null == o) {
            return this;
        } else if (o instanceof String) {
            return append((String) o);
        } else if (o instanceof Number) {
            return append(NumberUtil.toString((Number) o));
        } else if (o instanceof Date) {
            return append(DateUtil.getDateInt((Date) o));
        } else if (o instanceof Calendar) {
            return append(DateUtil.getDateInt((Calendar) o));
        } else {
            return append(String.valueOf(o.hashCode()));
        }
    }

    /**
     * 与String.startsWith不同的是，此函数清洗参数 prefix 指定的路径，并且比较完整名称
     *
     * @param prefix
     *         the prefix
     *
     * @return boolean
     */
    public boolean startsWith(Path prefix) {
        String p = prefix.toString();
        String s = path.toString();
        if (s.startsWith(p)) {
            int len = p.length();
            if (s.length() == len || s.charAt(len) == separator) {
                return true;
            }
        }
        return false;
    }

    /**
     * 与String.startsWith不同的是，此函数清洗参数 prefix 指定的路径，并且比较完整名称
     *
     * @param prefix
     *         the prefix
     *
     * @return boolean
     */
    public boolean startsWith(String prefix) {
        int len = prefix.length();
        if (len > 0) {
            Path prePath = new Path(len);
            prePath.setSeparator(separator, revSeparator);
            prePath.setAllowDuplicatedSeparator(allowDuplicatedSeparator);
            prePath.append(prefix);
            return startsWith(prePath);
        }
        return true;
    }

    /**
     * 与String.endsWith不同的是，此函数清洗参数 suffix 指定的路径，并且比较完整名称
     *
     * @param suffix
     *         the suffix
     *
     * @return boolean
     */
    public boolean endsWith(Path suffix) {
        String p = suffix.toString();
        String s = path.toString();
        if (s.endsWith(p)) {
            if (p.charAt(0) == separator) {
                return true;
            }
            int index = s.length() - p.length();
            if (index <= 0 || s.charAt(index - 1) == separator) {
                return true;
            }
        }
        return false;
    }

    /**
     * 与String.endsWith不同的是，此函数清洗参数 suffix 指定的路径，并且比较完整名称
     *
     * @param suffix
     *         the suffix
     *
     * @return boolean
     */
    public boolean endsWith(String suffix) {
        int len = suffix.length();
        if (len > 0) {
            Path sufPath = new Path(len);
            sufPath.setSeparator(separator, revSeparator);
            sufPath.setAllowDuplicatedSeparator(allowDuplicatedSeparator);
            sufPath.append(suffix);
            return endsWith(sufPath);
        }
        return true;
    }

    /**
     * 取相对路径，即去除参数 prefix 指定的前缀，若不存在则返回全部，相当于toString()
     *
     * @param prefix
     *         the prefix
     *
     * @return string
     */
    public String relativeTo(Path prefix) {
        String p = prefix.toString();
        String s = path.toString();
        if (s.startsWith(p)) {
            int len = p.length();
            if (s.length() == len) {
                return "";
            }
            if (s.charAt(len) == separator) {
                return s.substring(len + 1);
            }
        }
        return s;
    }

    /**
     * 取相对路径，即去除参数 prefix 指定的前缀，若不存在则返回全部，相当于toString()
     *
     * @param prefix
     *         the prefix
     *
     * @return string
     */
    public String relativeTo(String prefix) {
        int len = prefix.length();
        if (len > 0) {
            Path prePath = new Path(len);
            prePath.setSeparator(separator, revSeparator);
            prePath.setAllowDuplicatedSeparator(allowDuplicatedSeparator);
            prePath.append(prefix);
            return relativeTo(prePath);
        }
        return path.toString();
    }

    /**
     *
     */
    @Override
    public String toString() {
        return path.toString();
    }

}
