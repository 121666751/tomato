package com.tomato.util;

import java.util.concurrent.locks.Lock;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author caibo
 * @version $Id$
 * @since 2017/10/7 下午11:03
 */
public class SectionLockTest {

	@Test
	public void testCapacity() {
		SectionLock sectionLock;
		sectionLock = new SectionLock(3);
		assertEquals(4, sectionLock.getSlotNumber());
		sectionLock = new SectionLock(5);
		assertEquals(8, sectionLock.getSlotNumber());
		sectionLock = new SectionLock(8);
		assertEquals(8, sectionLock.getSlotNumber());
	}

	@Test
	public void testMultiStringLock() {
		String m[] = { "a", "b", "c", "d", "f", "h", "G", "D", "Y", "u", "Z", "o", "p", "Q", "r", "k" };
		for (int i = 1; i < 1000; i++) {
			String k1 = Math.random() * i + m[i & 15];
			String k2 = Math.random() * i + m[i & 15];
			SectionLock sl = new SectionLock((i + 5) / 2);
			Lock l1 = sl.get(k1);
			Lock l2 = sl.get(k2);
			int h1 = k1.hashCode() & (sl.getSlotNumber() - 1);
			int h2 = k2.hashCode() & (sl.getSlotNumber() - 1);
			System.out.println("h1=" + h1 + ",h2=" + h2);
			assertTrue((h1 == h2) == (l1 == l2));
		}
	}

}
