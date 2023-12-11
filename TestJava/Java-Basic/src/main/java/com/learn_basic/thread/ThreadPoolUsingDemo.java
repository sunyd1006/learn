package com.learn_basic.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class UserThreadFactory implements ThreadFactory {
	private final String namePrefix;
	private final AtomicInteger nextId = new AtomicInteger(1);
	
	// 定义线程组名称，在 jstack 问题排查时，非常有帮助
	UserThreadFactory(String whatFeaturOfGroup) {
		namePrefix = "[ UserThreadFactory " + whatFeaturOfGroup + "-Worker-";
	}
	
	@Override
	public Thread newThread(Runnable task) {
		String name = namePrefix + nextId.getAndIncrement() + " ]";
		Thread thread = new Thread(task, name);
		// System.out.println(thread.getName());
		return thread;
	}
}

/**
 * 1. 建议直接使用ThreadPoolExecutor
 * 2. 建议自定义线程工厂类，给线程重命名，方便故障排查。线程可命名为 XXXXClass XXXFeatur XXXworker i
 */
public class ThreadPoolUsingDemo {
	public static void main(String[] args) throws InterruptedException {
		/**
		 * 自定义线程工厂：  UserThreadFactory
		 */
		ThreadPoolExecutor pool = new ThreadPoolExecutor(3,
						3, 2000, TimeUnit.SECONDS, new LinkedBlockingDeque<>(),
						new UserThreadFactory("testDemo"),
						new ThreadPoolExecutor.AbortPolicy());
		
		PrintNumAtSameTime_CyclicBarrier demoCyc = new PrintNumAtSameTime_CyclicBarrier();
		for (int i = 1; i < 4; i++) {
			int num = i;
			pool.execute(() -> {
				try {
					demoCyc.printNum(num);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		
		pool.shutdown();
	}
}

class PrintNumAtSameTime_CyclicBarrier {
	CyclicBarrier startCyc, endCyc;
	
	PrintNumAtSameTime_CyclicBarrier() {
		startCyc = new CyclicBarrier(3, () -> {
			System.out.println("all start!");
		});
		
		endCyc = new CyclicBarrier(3, () -> {
			System.out.println("all end!");
		});
	}
	
	public void printNum(int x) throws InterruptedException, BrokenBarrierException {
		startCyc.await();
		System.out.println(Thread.currentThread().getName() + " " + x);
		endCyc.await();
	}
}