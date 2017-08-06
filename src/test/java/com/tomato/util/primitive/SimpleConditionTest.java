package com.tomato.util.primitive;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author CaiBo
 * @version $Id$
 * @since 2017/8/5 下午3:01
 */
public class SimpleConditionTest {

	private static Lock lock = new ReentrantLock();
	private static Condition notEmpty = lock.newCondition();
	private static Condition notFull = lock.newCondition();
	private static Condition inWorktime = lock.newCondition();
	private static Queue<String> queue = new LinkedList<>();
	private static AtomicInteger createCount = new AtomicInteger(0);
	private static AtomicInteger eatCount = new AtomicInteger(0);
	private static int CREATE_BREAD_ONE_DAY = 25;
	private static int ONE_DAY = 5;
	private static int timeCount = 1;

	public static void main(String[] args) throws Exception {
		new Thread(new MakeBread()).start();
		new Thread(new EatBread()).start();
		new Thread(new CookerManger()).start();
	}

	private static class CookerManger implements Runnable {

		@Override
		public void run() {
			while (true) {
				timeCount++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("现在时间是：" + timeCount);
				try {
					try {
						lock.lockInterruptibly();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (timeCount % ONE_DAY == 0) {
						System.out.println("新的一天，进入工作时间");
						inWorktime.signal();
					}
				} finally {
					lock.unlock();
				}
			}
		}
	}

	private static class MakeBread implements Runnable {
		@Override
		public void run() {
			while (true) {
				long random = (long) Math.random() * 1000;
				try {
					Thread.sleep(random);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					try {
						lock.lockInterruptibly();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (queue.size() > 10) {
						System.out.println("面包多了，等会再做...");
						try {
							notFull.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					synchronized (queue) {
						queue.add("bread");
						System.out.println("添加面包，目前总计做了" + createCount.incrementAndGet() + "个面包, 还剩" + queue.size() + "个面包");
					}
					if (createCount.get() % CREATE_BREAD_ONE_DAY == 0) {
						try {
							System.out.println("完成一天制造面包数量，等第二天上班");
							inWorktime.await();
							System.out.println("新的一天，开始工作...");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					notEmpty.signal();
				} finally {
					lock.unlock();
				}
			}
		}
	}

	private static class EatBread implements Runnable {

		@Override
		public void run() {
			while (true) {
				long random = (long) Math.random() * 1000;
				try {
					Thread.sleep(random);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					try {
						lock.lockInterruptibly();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (queue.isEmpty()) {
						System.out.println("面包吃完了，等有面包再说...");
						try {
							notEmpty.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					synchronized (queue) {
						queue.poll();
						System.out.println("吃了一个面包，总共吃了" + eatCount.incrementAndGet() + "个面包，还剩" + queue.size() + "个面包");
					}
					notFull.signal();
				} finally {
					lock.unlock();
				}
			}
		}
	}

}
