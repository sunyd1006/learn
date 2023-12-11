package com.learn_basic.threadLeecode;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 参考：https://leetcode-cn.com/problems/print-foobar-alternately/solution/duo-xian-cheng-liu-mai-shen-jian-ni-xue-d220n/
 * BlockingQueue
 * Semaphore
 *
 * volatile 标志位 + CyclicBarrier通知      // 标志位控制执行顺序
 * volatile 标志位 + yield 通知             todo (自旋, syd 不认为是自旋)
 * volatile 标志位 + Synchronized 对象锁通知
 * volatile 标志位 + 可重入锁 + Condition 		todo 感觉不需要 重入锁
 *
 *
 * 线程名 Thread-0 foo
 * 线程名 Thread-1 bar
 * 线程名 Thread-0 foo
 * 线程名 Thread-1 bar
 * 线程名 Thread-0 foo
 * 线程名 Thread-1 bar
 * main
 */
public class printOneByOne_1115 {
	public static void main(String[] args) throws InterruptedException {
		FooBarBlockingQueue fooBar = new FooBarBlockingQueue(3);
		Thread first = new Thread(() -> {
			try {
				fooBar.foo(() -> {
					System.out.println("线程名 " + Thread.currentThread().getName() + " foo");
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		Thread second = new Thread(() -> {
			try {
				fooBar.bar(() -> {
					System.out.println("线程名 " + Thread.currentThread().getName() + " bar");
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		first.start();
		second.start();
		first.join();
		second.join();
		System.out.println(Thread.currentThread().getName());
	}
}

// BlockingQueue
class FooBarBlockingQueue {
	private int n;
	private BlockingQueue<Integer> bar = new LinkedBlockingQueue<>(1);
	private BlockingQueue<Integer> foo = new LinkedBlockingQueue<>(1);
	public FooBarBlockingQueue(int n) {
		this.n = n;
	}
	
	public void foo(Runnable printFoo) throws InterruptedException {
		for (int i = 0; i < n; i++) {
			foo.put(i);
			printFoo.run();
			bar.put(i);
		}
	}
	
	public void bar(Runnable printBar) throws InterruptedException {
		for (int i = 0; i < n; i++) {
			bar.take();
			printBar.run();
			foo.take();
		}
	}
}

// Semaphore
class FooBarSemaphore {
	private int n;
	private Semaphore foo = new Semaphore(1);
	private Semaphore bar = new Semaphore(0);

	public FooBarSemaphore(int n) {
		this.n = n;
	}
	public void foo(Runnable printFoo) throws InterruptedException {
		for (int i = 0; i < n; i++) {
			foo.acquire();
			printFoo.run();
			bar.release();
		}
	}

	public void bar(Runnable printBar) throws InterruptedException {
		for (int i = 0; i < n; i++) {
			bar.acquire();
			printBar.run();
			foo.release();
		}
	}
}

// volatile锁 +  CyclicBarrier 通知
class FooBarCyclicBarrier {
	private int n;
	public FooBarCyclicBarrier(int n) {
		this.n = n;
	}
	CyclicBarrier cb = new CyclicBarrier(2);
	volatile boolean fin = true;
	public void foo(Runnable printFoo) throws InterruptedException {
		for (int i = 0; i < n; i++) {
			while (!fin) ;
			printFoo.run();
			fin = false;
			try {
				cb.await();
			} catch (BrokenBarrierException e) {
			}
		}
	}
	
	public void bar(Runnable printBar) throws InterruptedException {
		for (int i = 0; i < n; i++) {
			try {
				cb.await();
			} catch (BrokenBarrierException e) {
			}
			printBar.run();
			fin = true;
		}
	}
}

// volatile + yield 通知
class FooBarYield {
	private int n;
	public FooBarYield(int n) {
		this.n = n;
	}
	volatile boolean permitFoo = true;
	public void foo(Runnable printFoo) throws InterruptedException {
		for (int i = 0; i < n; ) {
			if (permitFoo) {
				printFoo.run();
				i++;
				permitFoo = false;
			} else {
				Thread.yield();
			}
		}
	}
	
	public void bar(Runnable printBar) throws InterruptedException {
		for (int i = 0; i < n; ) {
			if (!permitFoo) {
				printBar.run();
				i++;
				permitFoo = true;
			} else {
				Thread.yield();
			}
		}
	}
}

//  volatile标志位 + synchronized唤醒
class FooBarSynchronized {
	private int n;
	// 标志位，控制执行顺序，true执行 printFoo，false执行printBar
	private volatile boolean type = true;
	private final Object foo = new Object(); // 锁标志
	public FooBarSynchronized(int n) {
		this.n = n;
	}
	
	public void foo(Runnable printFoo) throws InterruptedException {
		for (int i = 0; i < n; i++) {
			synchronized (foo) {
				while (!type) {
					foo.wait();
				}
				printFoo.run();
				type = false;
				foo.notifyAll();
			}
		}
	}
	
	public void bar(Runnable printBar) throws InterruptedException {
		for (int i = 0; i < n; i++) {
			synchronized (foo) {
				while (type) {
					foo.wait();
				}
				printBar.run();
				type = true;
				foo.notifyAll();
			}
		}
	}
}

// volatile 标志位 + 可重入锁 + Condition
class FooBarReentrantLock {
	private int n;
	public FooBarReentrantLock(int n) {
		this.n = n;
	}
	Lock lock = new ReentrantLock(true);
	private final Condition foo = lock.newCondition();
	volatile boolean flag = true;

	public void foo(Runnable printFoo) throws InterruptedException {
		for (int i = 0; i < n; i++) {
			lock.lock();
			try {
				while (!flag) {
					foo.await();
				}
				printFoo.run();
				flag = false;
				foo.signal();
			} finally {
				lock.unlock();
			}
		}
	}

	public void bar(Runnable printBar) throws InterruptedException {
		for (int i = 0; i < n; i++) {
			lock.lock();
			try {
				while (flag) {
					foo.await();
				}
				printBar.run();
				flag = true;
				foo.signal();
			} finally {
				lock.unlock();
			}
		}
	}
}
