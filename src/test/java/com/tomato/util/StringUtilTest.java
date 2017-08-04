package com.tomato.util;


import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 *
 */
public class StringUtilTest {

	@Test
	public void testConvertNative() {
		String source = "  12３４５　　ａｂｃＤＥＦ　ghiJKL  ";
		String result = StringUtil.convertNativeLetterOrDigit(source, false, true);
		System.out.println(result);
		assertEquals(result, "12345abcDEFghiJKL");
		result = StringUtil.convertNativeLetterOrDigit(source, false, true, 10);
		System.out.println(result);
		assertEquals(result, "12345abcDE");
		result = StringUtil.convertNativeLetterOrDigit(source, false, false);
		System.out.println(result);
		assertEquals(result, "12345 abcDEF ghiJKL");
		result = StringUtil.convertNativeLetterOrDigit(source, false, false, 10);
		System.out.println(result);
		assertEquals(result, "12345 abcDE");
		result = StringUtil.convertNativeLetterOrDigit(source, false, false, true);
		System.out.println(result);
		assertEquals(result, "12345 ABCDEF GHIJKL");
		result = StringUtil.convertNativeLetterOrDigit(source, false, false, false);
		System.out.println(result);
		assertEquals(result, "12345 abcdef ghijkl");
	}

	@Test
	public void testConvertNativeLetter() {
		String source = "  ａｂｃＤＥＦ　ghiJKL  ";
		String result = StringUtil.convertNativeLetter(source, true);
		System.out.println(result);
		assertEquals(result, "abcDEFghiJKL");
		result = StringUtil.convertNativeLetter(source, true, 10);
		System.out.println(result);
		assertEquals(result, "abcDEFghiJ");
		result = StringUtil.convertNativeLetter(source, false);
		System.out.println(result);
		assertEquals(result, "abcDEF ghiJKL");
		result = StringUtil.convertNativeLetter(source, false, 10);
		System.out.println(result);
		assertEquals(result, "abcDEF ghiJ");
		result = StringUtil.convertNativeLetter(source, false, true);
		System.out.println(result);
		assertEquals(result, "ABCDEF GHIJKL");
		result = StringUtil.convertNativeLetter(source, false, false);
		System.out.println(result);
		assertEquals(result, "abcdef ghijkl");
	}

	@Test
	public void testConvertNativeDigit() {
		String source = "  12３４５　　7890  ";
		String result = StringUtil.convertNativeDigit(source, true);
		System.out.println(result);
		assertEquals(result, "123457890");
		result = StringUtil.convertNativeDigit(source, true, 7);
		System.out.println(result);
		assertEquals(result, "1234578");
		result = StringUtil.convertNativeDigit(source, false);
		System.out.println(result);
		assertEquals(result, "12345 7890");
		result = StringUtil.convertNativeDigit(source, false, 7);
		System.out.println(result);
		assertEquals(result, "12345 78");
	}

	@Test
	public void testInstanceof() {
		String s0 = "";
		String[] s1 = { "" };
		String[][] s2 = { { "" } };
		Object test = s0;
		assertTrue(test instanceof CharSequence);
		assertFalse(test instanceof CharSequence[]);
		assertFalse(test instanceof CharSequence[][]);
		assertFalse(test instanceof CharSequence[][][]);
		test = s1;
		assertFalse(test instanceof CharSequence);
		assertTrue(test instanceof CharSequence[]);
		assertFalse(test instanceof CharSequence[][]);
		assertFalse(test instanceof CharSequence[][][]);
		test = s2;
		assertFalse(test instanceof CharSequence);
		assertFalse(test instanceof CharSequence[]);
		assertTrue(test instanceof CharSequence[][]);
		assertFalse(test instanceof CharSequence[][][]);
	}

	@Test
	public void testDeleteAll() {
		String result = StringUtil.deleteAll("ASNPJE_XXWLQYJMSDSE_LJ", '_');
		System.out.println(result);
		assertEquals(result, "ASNPJEXXWLQYJMSDSELJ");
		result = StringUtil.deleteAll("ASNPJE_XXWLQYJMSDSE_LJ", "_");
		System.out.println(result);
		assertEquals(result, "ASNPJEXXWLQYJMSDSELJ");
	}

	@Test
	public void testMasking() {
		String source = "testMasking";
		System.out.println();
		System.out.println(StringUtil.masking11(source));
		System.out.println(StringUtil.masking22(source));
		System.out.println(StringUtil.masking52(source));
		System.out.println(StringUtil.masking64(source));
		System.out.println();
		System.out.println(StringUtil.masking(source, 6, 6, -2));
		System.out.println(StringUtil.masking(source, 6, -6, -2));
		System.out.println(StringUtil.masking(source, -6, 6, -2));
		System.out.println(StringUtil.masking(source, -6, -6, -2));
		System.out.println();
		System.out.println(StringUtil.masking(source, 6, 6, -8));
		System.out.println(StringUtil.masking(source, 6, -6, -8));
		System.out.println(StringUtil.masking(source, -6, 6, -8));
		System.out.println(StringUtil.masking(source, -6, -6, -8));
		System.out.println();
		System.out.println(StringUtil.masking(source, 2, 3, 4));
		System.out.println(StringUtil.masking(source, 2, -3, 4));
		System.out.println(StringUtil.masking(source, -2, 3, 4));
		System.out.println(StringUtil.masking(source, -2, -3, 4));
		System.out.println();

		String name = "唐僧";
		String result = StringUtil.masking(name, 0, -2, -1);
		System.out.println(result);
		assertEquals(result, "*僧");
		name = "孙悟空";
		result = StringUtil.masking(name, 0, -2, -1);
		System.out.println(result);
		assertEquals(result, "*悟空");
		name = "诸葛孔明";
		result = StringUtil.masking(name, 0, -2, -1);
		System.out.println(result);
		assertEquals(result, "**孔明");
		name = "诸葛小孔明";
		result = StringUtil.masking(name, 0, -2, -1);
		System.out.println(result);
		assertEquals(result, "***孔明");
		System.out.println();
	}

	/**
	 * 测试2个含有一千万元素的数组 查找出2个数组中相同的String
	 * 
	 */
	@Test
	public void testCompareSame()  {
		// 定义测试级别
		int n = 10000000;
		String a[] = new String[n];
		String b[] = new String[n];
		for (int i = 0; i < n; i++) {
			Random rd = new Random();
			a[i] = rd.nextInt() + "c";
			b[i] = rd.nextInt() + "c";
		}
		Long t1 = System.currentTimeMillis();
		String[] rs = StringUtil.comparingSame(a, b);
		System.out.println("总共耗时:" + (System.currentTimeMillis() - t1));
		System.out.println(rs.toString());
	}

}
