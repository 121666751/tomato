package com.tomato.util.misc;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author CaiBo
 * @version $Id$
 * @since 2017/8/2 下午9:41
 */
public class RapNamedActuatorComparatorTest extends TestCase {

	@Test
	public void testSortByOrder() {
		TestFilter t1 = new TestFilter(1, "t1");
		TestFilter t2 = new TestFilter(2, "t2");
		TestFilter t3 = new TestFilter(3, "t3");
		TestFilter t4 = new TestFilter(4, "t4");

		List<TestFilter> list = new ArrayList<>();
		list.add(t3);
		list.add(t4);
		list.add(t2);
		list.add(t1);
		Collections.sort(list, RapNamedActuatorComparator.LOW_FIRST);
		assertEquals("[t1, t2, t3, t4]", list.toString());
		Collections.sort(list, RapNamedActuatorComparator.HIGH_FIRST);
		assertEquals("[t4, t3, t2, t1]", list.toString());
	}

	class TestFilter implements RapNamedActuator {

		private int order;
		private String name;

		public TestFilter(int order, String name) {
			this.order = order;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public int order() {
			return order;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
