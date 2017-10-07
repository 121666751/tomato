package com.tomato.util;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 该锁类似于利用String.intern()但更具扩展性和可读性，以及避免常量池溢出
 *
 * @author caibo
 * @version $Id$
 * @since 2017/10/7 下午9:31
 */
public class SectionLock {

	private final Lock lockArr[];
	private final int slotNum;

	/**
	 * @param slotNumber
	 * 		期望的槽的总数
	 */
	public SectionLock(int slotNumber) {
		if (slotNumber < 2) {
			throw new RuntimeException("lock slot must greater than 2");
		}
		this.slotNum = Integer.highestOneBit((slotNumber - 1) << 1);
		lockArr = new Lock[this.slotNum];
		for (int i = 0; i < this.slotNum; i++) {
			// 暂时硬编码使用该非公平锁
			lockArr[i] = new ReentrantLock();
		}
	}

	/**
	 * @param key
	 * 		关键字
	 *
	 * @return 该关键字对应的锁
	 */
	public Lock get(String key) {
		return lockArr[(key != null ? key.hashCode() : 0) & (slotNum - 1)];
	}

	/**
	 * @param keys
	 * 		关键字
	 *
	 * @return 该关键字对应的锁
	 */
	public Lock get(Object... keys) {
		return lockArr[Objects.hash(keys) & (slotNum - 1)];
	}

	/**
	 * @return 实际的槽的数量
	 */
	public int getSlotNumber() {
		return slotNum;
	}

}
