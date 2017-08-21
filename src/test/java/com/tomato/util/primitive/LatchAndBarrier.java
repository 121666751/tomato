package com.tomato.util.primitive;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 写代码的时候总是容易想混，虽然知道该用哪一个确只是死记硬背<br>
 * <p>
 * 闭锁用于等待事件的发生，谁完成这件事情无所谓，只要这件事完成了，闭锁会通知所有等待在该闭锁上的线程<br>
 * 栅栏用于等待线程，当线程到达栅栏时将被挂起，直到指定数量的线程到达栅栏后再同时继续执行<br>
 * <p>
 * 具体可查看两个简单的示例，希望不要总在这块搞不清楚原理，只知道蒙头写<br>
 * 他们都是利用AQS来实现，独占式和共享式的区别，栅栏更多的采用了自己的实现
 *
 * @author CaiBo
 * @version $Id$
 * @since 2017/8/21 下午9:21
 */
public class LatchAndBarrier {

	/**
	 * 闭锁等待的是事件，某些事情做好了，也就满足了闭锁的条件<br>
	 * 既然是等待事件的发生，那么发生的就是发生了，也没有回头的余地，所以闭锁一旦到达条件则不能再使用<br>
	 * 某件事情的发生是大家都可以关心的，所以闭锁上可以有多个等待者，当事件发生，他们都将被唤醒<br>
	 * 闭锁对应的事件任务的执行者，在执行完任务之后只需告诉闭锁一部分事情已做完，接着他可以继续做其他事<br>
	 *
	 * @throws Exception
	 */
	@Test
	public void testSimpleLatch() throws Exception {
		final CountDownLatch cdl = new CountDownLatch(2);
		new Thread(() -> {
			sleep(3_000);
			System.out.println("3秒钟的事情做完了");
			cdl.countDown();
		}).start();
		new Thread(() -> {
			sleep(1_000);
			System.out.println("1秒钟的事情做完了");
			cdl.countDown();
		}).start();
		new Thread(() -> {
			System.out.println("我和主线程一样在等事情做完");
			try {
				cdl.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("我发现主线程的事情做完了");
		}).start();
		cdl.await();
		System.out.println("事情全部完成");
	}

	/**
	 * 栅栏等待的是人而不是事件，它的行为是凑齐了相应个数的线程（人）就可以做某件事情<br>
	 * 每凑齐相应的人就可以去做某件事，每一波人是相互独立的，栅栏可以反复用<br>
	 * 当某些线程提前到达栅栏时，它会被挂起，等待直到凑齐相应数量的线程
	 */
	@Test
	public void testSimpleBarrier() {
		CyclicBarrier cb = new CyclicBarrier(4);
		final AtomicInteger count = new AtomicInteger();
		// 打麻将需要4个人，每来4个人就可以凑齐一桌麻将
		new Thread(() -> {
			while (count.incrementAndGet() < 100) {
				sleep(1_000);
				System.out.println("张家村的人喜欢每1秒来一个人");
				barrierWait(cb);
				System.out.println("张家人说：凑齐一桌打麻将咯");
			}
		}).start();
		new Thread(() -> {
			while (count.incrementAndGet() < 100) {
				sleep(6_000);
				System.out.println("王家坝的人喜欢每6秒来一个人");
				barrierWait(cb);
				System.out.println("王家人说：凑齐一桌打麻将咯");
			}
		}).start();
		new Thread(() -> {
			while (count.incrementAndGet() < 100) {
				sleep(4_000);
				System.out.println("李家口的人喜欢每4秒来一个人");
				barrierWait(cb);
				System.out.println("李家人说：凑齐一桌打麻将咯");
			}
		}).start();
		new Thread(() -> {
			while (count.incrementAndGet() < 100) {
				sleep(5_000);
				System.out.println("刘家湾的人喜欢每5秒来一个人");
				barrierWait(cb);
				System.out.println("刘家人说：凑齐一桌打麻将咯");
			}
		}).start();
		// 茶馆仅开业30秒
		sleep(30_000);
	}

	public static void barrierWait(CyclicBarrier cb) {
		try {
			cb.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
