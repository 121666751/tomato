package com.tomato.util;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author caibo
 * @version $Id$
 * @since 2017/9/17 上午10:34
 */
public class NumberUtilTest extends TestCase {

	@Test
	public void testIsPositiveInteger() {
		assertTrue(NumberUtil.isPositiveInteger(String.valueOf(Integer.MAX_VALUE)));
		assertFalse(NumberUtil.isPositiveInteger(String.valueOf(Integer.MAX_VALUE + 1)));
		assertFalse(NumberUtil.isPositiveInteger("2147483648"));
		assertTrue(NumberUtil.isPositiveInteger("2147483647"));
		assertTrue(NumberUtil.isPositiveInteger("123"));
		assertTrue(NumberUtil.isPositiveInteger("6542178"));
		assertTrue(NumberUtil.isPositiveInteger("1"));
		assertTrue(NumberUtil.isPositiveInteger("9"));
		assertTrue(NumberUtil.isPositiveInteger("9000"));
		assertFalse(NumberUtil.isPositiveInteger("9000a"));
		assertTrue(NumberUtil.isPositiveInteger("0"));
		assertTrue(NumberUtil.isPositiveInteger("00000"));
		assertFalse(NumberUtil.isPositiveInteger("-0"));
		assertFalse(NumberUtil.isPositiveInteger("-111"));
		assertFalse(NumberUtil.isPositiveInteger("abc"));
		assertFalse(NumberUtil.isPositiveInteger("11c2"));
		assertFalse(NumberUtil.isPositiveInteger("1111111111111111111111"));
		assertFalse(NumberUtil.isPositiveInteger("'112"));

	}

}
