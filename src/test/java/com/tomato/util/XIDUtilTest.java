package com.tomato.util; /**
 * Copyright(C) 2016 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 *
 */


import junit.framework.TestCase;
import org.junit.Test;

/**
 * @since Oct 19, 2016 8:56:12 PM
 * @version $Id: com.tomato.util.XIDUtilTest.java 36524 2017-06-21 05:36:33Z CaiBo $
 * @author WuJianqiang
 * 
 */
public final class XIDUtilTest extends TestCase {

	@Test
	public void testMobile() {
		assertTrue(XIDUtil.isMobile("13906523981"));
		assertEquals(XIDUtil.getMobile("13906523981"), "13906523981");
		assertEquals(XIDUtil.getMobile("13906523981"), "13906523981");
		assertEquals(XIDUtil.getMobile(" 13906523981 "), "13906523981");
		assertEquals(XIDUtil.getMobile("0571-88265911 137 0581 9762"), "13705819762");
		assertEquals(XIDUtil.getMobile("0571-88265911 13705819762"), "13705819762");
		assertEquals(XIDUtil.getMobile("13336016069 13336016069"), "13336016069");
		assertEquals(XIDUtil.getMobile("86603086 13868002201"), "13868002201");
		assertEquals(XIDUtil.getMobile("13958081016,88285228"), "13958081016");
		assertEquals(XIDUtil.getMobile("15906633904,13396587846"), "15906633904");
		assertEquals(XIDUtil.getMobile("88037777-8131 13588780972"), "13588780972");
		assertEquals(XIDUtil.getMobile("13666628826#85970390"), "13666628826");
		assertEquals(XIDUtil.getMobile("88480557-805 13777404043"), "13777404043");
		assertEquals(XIDUtil.getMobile("13575797968#15868812295"), "13575797968");
		assertEquals(XIDUtil.getMobile("88058547#13588061311#88260189"), "13588061311");
		assertEquals(XIDUtil.getMobile("外13777761360#15088707166"), "13777761360");
		assertEquals(XIDUtil.getMobile("057189969581 18657198509 13073611466"), "18657198509");
	}

	@Test
	public void testGetNsrmcPyjc() {
		assertEquals("HZWLKJYXGS", XIDUtil.getNsrmcPyjc("杭州网络科技有限公司"));
		assertEquals("HZWLKJYXGS", XIDUtil.getNsrmcPyjc(" 杭州 (网 络 科 技有 限 公  司   )"));
		assertEquals("HZWLKJYXGS", XIDUtil.getNsrmcPyjc(" 杭 州 （网 络）！ 科 技有 限 公  司   )"));
		assertEquals("HZWLKJYXGS", XIDUtil.getNsrmcPyjc("1 杭 州 （网 络）！ 科 技有 限 公  司   )"));
		assertEquals("HZWLKJYXGS", XIDUtil.getNsrmcPyjc("1asf 杭 州 sdf（sd网 络）！ 科 技sdfasd有 限 dd公  司   )"));
		assertEquals("HZWLKJYXGS", XIDUtil.getNsrmcPyjc("HZWLKJYXGS"));
		assertEquals("1HZWLKJYXGS", XIDUtil.getNsrmcPyjc("1HZWLKJYXGS"));
		assertEquals("FJ", XIDUtil.getNsrmcPyjc("HZWLKJYXGS孚嘉"));

		assertEquals("HZSPYXGS", XIDUtil.getNsrmcPyjc("杭州食品有限公司"));
		assertEquals("HZSPYXGS", XIDUtil.getNsrmcPyjc(" 杭州 (食品有 限 公  司   )"));
		assertEquals("HZSPYXGS", XIDUtil.getNsrmcPyjc(" 杭 州 （食品）！有 限 公  司   )"));
		assertEquals("HZSPYXGS", XIDUtil.getNsrmcPyjc("1 杭 州 （食品）！ 有 限 公  司   )"));
		assertEquals("HZSPYXGS", XIDUtil.getNsrmcPyjc("1asf 杭 州 sdf（sd食品）！ sdfasd有 限 dd公  司   )"));
		assertEquals("HZSPYXGS", XIDUtil.getNsrmcPyjc("HZSpyXGS"));
		assertEquals("1HZSPYXGS", XIDUtil.getNsrmcPyjc("1HZSpYXGS"));
		assertEquals("SP", XIDUtil.getNsrmcPyjc("HZWLKJYXGS食品"));
	}

	@Test
	public void testIsNsrsbh() {
		assertFalse(XIDUtil.isNsrsbh("91130500MA07KGGKKG"));
		assertTrue(XIDUtil.isNsrsbh("130522667718790"));
		assertTrue(XIDUtil.isNsrsbh("13052280732273X"));
		assertTrue(XIDUtil.isNsrsbh("91130522662208898P"));
		assertTrue(XIDUtil.isNsrsbh("91130500MA07KMFD4F"));
		assertTrue(XIDUtil.isNsrsbh("91130522723374612N"));
		assertTrue(XIDUtil.isNsrsbh("91130522807322748W"));
		assertTrue(XIDUtil.isNsrsbh("91130522723392589Y"));
		assertTrue(XIDUtil.isNsrsbh("130522807322633"));
		assertTrue(XIDUtil.isNsrsbh("91130522MA07MWGP7N"));
		assertTrue(XIDUtil.isNsrsbh("130522807322721"));
		assertTrue(XIDUtil.isNsrsbh("130522667718918"));
		assertTrue(XIDUtil.isNsrsbh("130522601156633"));
		assertTrue(XIDUtil.isNsrsbh("130522667718678"));
		assertTrue(XIDUtil.isNsrsbh("130522667720313"));
		assertTrue(XIDUtil.isNsrsbh("130522667718491"));
		assertTrue(XIDUtil.isNsrsbh("130522560477309"));
		assertTrue(XIDUtil.isNsrsbh("130522682751863"));
		assertTrue(XIDUtil.isNsrsbh("91130522MA07KFT911"));
		assertTrue(XIDUtil.isNsrsbh("913304216912612170"));
		assertTrue(XIDUtil.isNsrsbh("91330102MA280M6J1R"));
		assertFalse(XIDUtil.isNsrsbh("91330102MA281M6J1R"));
		assertFalse(XIDUtil.isNsrsbh("913304213912612170"));
		assertTrue(XIDUtil.isNsrsbh("91330100MA27X1RQ4M"));
		assertTrue(XIDUtil.isNsrsbh("91330106MA27XG019J"));

		assertTrue(XIDUtil.isNsrsbh("91130522MA07KYNX1B"));
		assertTrue(XIDUtil.isNsrsbh("130522329731191"));
		assertFalse(XIDUtil.isNsrsbh("91130521MA07KYNX1B"));
		assertFalse(XIDUtil.isNsrsbh("130521329731192"));
		assertFalse(XIDUtil.isNsrsbh("123"));
		assertFalse(XIDUtil.isNsrsbh("913301095739669"));

	}

}
