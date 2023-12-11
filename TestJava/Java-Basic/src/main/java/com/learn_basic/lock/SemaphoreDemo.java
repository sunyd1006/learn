package com.learn_basic.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author Snailclimb
 * @date 2018年9月30日
 * @Description: 需要一次性拿一个许可的情况
 */
public class SemaphoreDemo {
	private static final int threadCount = 550; // 请求的数量
	
	public static void main(String[] args) throws InterruptedException {
		// 创建一个具有固定线程数量的线程池对象（如果这里线程池的线程数量给太少的话你会发现执行的很慢）
		ExecutorService threadPool = Executors.newFixedThreadPool(100);
		// 一次只能允许执行的线程数量, permits: 可以理解为资源的数量
		final Semaphore semaphore = new Semaphore(20);
		for (int i = 0; i < threadCount; i++) {
			final int threadnum = i;
			threadPool.execute(() -> { // Lambda 表达式的运用
				try {
					semaphore.acquire(5); // 获取一个许可，所以可运行线程数量为20/5=4
					test(threadnum);
					semaphore.release(5); // 释放一个许可
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			});
		}
		threadPool.shutdown();
		System.out.println("finish");
	}
	
	public static void test(int threadnum) throws InterruptedException {
		Thread.sleep(1000); // 模拟请求的耗时操作
		System.out.println("threadnum:" + threadnum);
		Thread.sleep(1000); // 模拟请求的耗时操作
	}
}
