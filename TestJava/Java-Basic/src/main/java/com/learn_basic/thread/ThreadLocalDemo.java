package com.learn_basic.thread;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * <p>
 *   static, static volatile 都不能保证 对改变量的原子性，故而发生了覆盖写。  应该加 synchronized.
 *   formatterNotThreadLocal = 97, 98, 99 , 100
 * </p>
 */
public class ThreadLocalDemo implements Runnable {

	// SimpleDateFormat 不是线程安全的，所以每个线程都要有自己独立的副本
	private static int formatterNotThreadLocal = 0;  // NOTE: 100个线程，可能出现 formatterNotThreadLocal = 97, 98, 99, 100
	private static final ThreadLocal<SimpleDateFormat> formatter = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd HHmm"));

	public static void main(String[] args) throws InterruptedException {
		ThreadLocalDemo obj = new ThreadLocalDemo();
		for (int i = 0; i < 100; i++) {  // formatterNotThreadLocal != 100, 发生了覆盖写
			Thread t = new Thread(obj, "" + i);
			// Thread.sleep(new Random().nextInt(1000));
			t.start();
		}
	}

	@Override
	public void run() {
		System.out.println("Thread Name= " + Thread.currentThread().getName() + " default Formatter = " + formatter.get().toPattern() +
						"formatterNotThreadLocal  " + formatterNotThreadLocal);
		try {
			Thread.sleep(new Random().nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//formatter pattern is changed here by thread, but it won't reflect to other threads
		formatter.set(new SimpleDateFormat());
		formatterNotThreadLocal = formatterNotThreadLocal + 1;

		System.out.println("Thread Name= " + Thread.currentThread().getName() + " default Formatter = " + formatter.get().toPattern() +
						"formatterNotThreadLocal  " + formatterNotThreadLocal);
		assert formatterNotThreadLocal==2;
	}

}
