package com.tomato.util.primitive;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author CaiBo
 * @version $Id$
 * @since 2017/8/1 下午9:07
 */

public class StringBuilderTest extends TestCase {

	@Test
	public void testAppendCharOrShortString() {
		int times = 10000000;
		int win = 0;
		for (int i = 0; i < times; i++) {
			StringBuilder sb = new StringBuilder(64);
			long t1 = System.nanoTime();
			sb.append('c').append('b');
			long t2 = System.nanoTime();
			sb.append("cb");
			long t3 = System.nanoTime();
			if ((t3 - t2) > (t2 - t1)) {
				++win;
			}
		}
		double per = win / (double) times;
		System.out.println("添加两次char快于添加一次两字符字符串次数: " + win + " 胜率: " + per);
		assertTrue(per > 0.7);
	}

}
