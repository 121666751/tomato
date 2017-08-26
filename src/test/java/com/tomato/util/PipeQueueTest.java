package com.tomato.util;

import com.tomato.util.struct.PipeQueue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author CaiBo
 * @version $Id$
 * @since 2017/8/26 下午8:23
 */
public class PipeQueueTest {

	@Test
	public void testAdd() {
		PipeQueue<String> p = new PipeQueue<>(3);
		p.add("1");
		p.add("2");
		p.add("3");
		System.out.println(p.toString());
		assertEquals("[1,2,3]", p.toString());
		p.add("4");
		System.out.println(p.toString());
		assertEquals("[2,3,4]", p.toString());
		p.add("5");
		System.out.println(p.toString());
		assertEquals("[3,4,5]", p.toString());
		p.add("6");
		System.out.println(p.toString());
		assertEquals("[4,5,6]", p.toString());
		p.add("7");
		System.out.println(p.toString());
		assertEquals("[5,6,7]", p.toString());
		p.add("8");
		System.out.println(p.toString());
		assertEquals("[6,7,8]", p.toString());
		p.add("8");
		System.out.println(p.toString());
		assertEquals("[7,8,8]", p.toString());
		p.add("8");
		System.out.println(p.toString());
		assertEquals("[8,8,8]", p.toString());
	}

	@Test
	public void testPoll() {
		PipeQueue<String> p = new PipeQueue<>(3);
		String s1 = new String("1");
		p.add(s1);
		p.add("2");
		p.add("3");
		assertEquals("[1,2,3]", p.toString());
		String e = p.poll();
		assertTrue(e == s1);
		System.out.println(s1 == e);
		e = p.poll();
		System.out.println("2" == e);
		assertTrue("2" == e);
		p.add("4");
		assertEquals("[3,4]", p.toString());
	}

	@Test
	public void testPeek() {
		PipeQueue<String> p = new PipeQueue<>(3);
		String s1 = new String("1");
		p.add(s1);
		p.add("2");
		p.add("3");
		String e = p.peek();
		assertTrue(s1 == e);
		e = p.peek();
		assertTrue(s1 == e);
	}

}
