package com.tomato.util; /**
 * Copyright(C) 2011 Fugle Technology Co. Ltd. All rights reserved.
 * 
 */

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * 按数字优先排序。若要倒序，则使用 Collections.reverseOrder(Comparator<T> cmp) 强行反转指定比较器的顺序
 * 
 * @since 2011-1-11 下午09:39:24
 * @version $Id: com.tomato.util.StringComparator.java 15463 2016-05-28 07:44:34Z WuJianqiang $
 * @author WuJianqiang
 * 
 */
public class StringComparator implements Comparator<Object> {
	private static final int LESS = -1;
	private static final int EQUAL = 0;
	private static final int GREATER = 1;
	private final Collator collator;
	private final HashMap<Object, NumberCollationKey> numberCollationCache;

	/**
	 * 默认使用中文语言环境，忽略大小写
	 */
	public StringComparator() {
		this(Collator.SECONDARY);
	}

	/**
	 * 默认使用中文语言环境
	 * 
	 * @param newStrength
	 *            the new strength value.
	 * @see Collator#getStrength
	 * @see Collator#PRIMARY
	 * @see Collator#SECONDARY
	 * @see Collator#TERTIARY
	 * @see Collator#IDENTICAL
	 */
	public StringComparator(int newStrength) {
		collator = Collator.getInstance(java.util.Locale.CHINA);
		collator.setStrength(newStrength);
		numberCollationCache = new HashMap<Object, NumberCollationKey>(2048);
	}

	/**
	 * 
	 */
	public void clear() {
		numberCollationCache.clear();
	}

	/**
	 * 
	 * @param newStrength
	 */
	public void setStrength(int newStrength) {
		collator.setStrength(newStrength);
	}

	/**
	 * 
	 * @author WuJianqiang
	 * @date 2011-3-28 下午03:54:21
	 * 
	 */
	private class NumberCollationKey {
		final Number pre;
		final String all;
		final String sub;
		private CollationKey allKey;
		private CollationKey subKey;

		/**
		 * 
		 * @param source
		 */
		public NumberCollationKey(Object source) {
			all = StringUtil.valueOf(source, null);
			if (source instanceof Number) {
				pre = (Number) source;
				sub = null;
			} else if (NumberUtil.startsWithDigit(all, true, true)) {
				// 比较时忽略前导空格，在Java中文语言环境比较时，ASCII空格比数字和字母还大
				int beginIndex = NumberUtil.indexOfDigit(all, true);
				int endIndex = NumberUtil.indexOfNonDigit(all, true, false, beginIndex + 1);
				if (endIndex > 0) {
					pre = NumberUtil.numberOf(all.substring(beginIndex, endIndex));
					sub = all.substring(endIndex);
				} else {
					if (beginIndex > 0) {
						pre = NumberUtil.numberOf(all.substring(beginIndex));
					} else {
						pre = NumberUtil.numberOf(all);
					}
					sub = null;
				}
			} else {
				pre = null;
				sub = all;
			}
		}

		/**
		 * 
		 * @return
		 */
		public CollationKey getAllKey() {
			if (null == allKey && null != all) {
				allKey = collator.getCollationKey(all);
			}
			return allKey;
		}

		/**
		 * 
		 * @return
		 */
		public CollationKey getSubKey() {
			if (null == subKey && null != sub) {
				subKey = collator.getCollationKey(sub);
			}
			return subKey;
		}
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public NumberCollationKey getNumberCollationKey(Object source) {
		NumberCollationKey nck = numberCollationCache.get(source);
		if (null == nck) {
			nck = new NumberCollationKey(source);
			numberCollationCache.put(source, nck);
		}
		return nck;
	}

	/**
	 * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer
	 * as the first argument is less than, equal to, or greater than the second.
	 * <p>
	 * 
	 * In the foregoing description, the notation <tt>sgn(</tt><i>expression</i> <tt>)</tt>
	 * designates the mathematical <i>signum</i> function, which is defined to return one of
	 * <tt>-1</tt>, <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i> is
	 * negative, zero or positive.
	 * <p>
	 * 
	 * The implementor must ensure that <tt>sgn(compare(x, y)) ==
	 * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This implies that
	 * <tt>compare(x, y)</tt> must throw an exception if and only if <tt>compare(y, x)</tt> throws
	 * an exception.)
	 * <p>
	 * 
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
	 * <tt>compare(x, z)&gt;0</tt>.
	 * <p>
	 * 
	 * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt> implies that
	 * <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all <tt>z</tt>.
	 * <p>
	 * 
	 * It is generally the case, but <i>not</i> strictly required that
	 * <tt>(compare(x, y)==0) == (x.equals(y))</tt>. Generally speaking, any comparator that
	 * violates this condition should clearly indicate this fact. The recommended language is
	 * "Note: this comparator imposes orderings that are inconsistent with equals."
	 * 
	 * @param o1
	 *            the first object to be compared.
	 * @param o2
	 *            the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 *         equal to, or greater than the second.
	 * @throws ClassCastException
	 *             if the arguments' types prevent them from being compared by this comparator.
	 */
	public int compare(Object o1, Object o2) {
		int result;
		if (o1 == o2) {
			result = EQUAL;
		} else if (null == o1) {
			result = LESS;
		} else if (null == o2) {
			result = GREATER;
		} else if (o1 instanceof Number && o2 instanceof Number) {
			result = NumberUtil.compare((Number) o1, (Number) o2);
		} else if (o1 instanceof Date && o2 instanceof Date) {
			result = ((Date) o1).compareTo((Date) o2);
		} else { // 若可能，字符串前面数字部分先转换成数值进行比较
			NumberCollationKey nck1 = getNumberCollationKey(o1);
			NumberCollationKey nck2 = getNumberCollationKey(o2);
			if (null != nck1.pre && null != nck2.pre) {
				result = NumberUtil.compare(nck1.pre, nck2.pre);
				if (EQUAL == result) { // 前面数字相等，继续比较后面字符串
					String sub1 = nck1.sub;
					String sub2 = nck2.sub;
					if (sub1 != sub2) {
						if (null == sub1) {
							result = LESS;
						} else if (null == sub2) {
							result = GREATER;
						} else {
							CollationKey key1 = nck1.getSubKey();
							CollationKey key2 = nck2.getSubKey();
							result = key1.compareTo(key2);
						}
					}
				}
			} else {
				CollationKey key1 = nck1.getAllKey();
				CollationKey key2 = nck2.getAllKey();
				result = key1.compareTo(key2);
			}
		}
		return result;
	}

}
