package com.learn_basic.threadLeecode;

import java.util.concurrent.*;

public class PrintNumAtSameTimeDemo {
	public static void main(String[] args) throws InterruptedException {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(3,
						3, 2000, TimeUnit.SECONDS, new LinkedBlockingDeque<>(),
						new ThreadPoolExecutor.AbortPolicy());
		
		PrintNumAtSameTime_CyclicBarrier demoCyc = new PrintNumAtSameTime_CyclicBarrier();
		for (int i = 1; i < 4; i++) {
			int num = i;
			pool.execute(()-> {
				try {
					demoCyc.printNum(num);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		
		PrintNumAtSameTime_Semaphore demoSema = new PrintNumAtSameTime_Semaphore();
		for (int i = 1; i < 4; i++) {
			int num = i;
			pool.execute(()-> {
				try {
					demoSema.printNum(num);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		demoSema.start();
		Thread.sleep(1000);
		demoSema.end();
		// Thread.sleep(1000);
		
		

		
		pool.shutdown();
	}
}


class PrintNumAtSameTime_Semaphore {
	private Semaphore startSema;
	private Semaphore endSema;
	PrintNumAtSameTime_Semaphore(){
		startSema = new Semaphore(0);
		endSema = new Semaphore(0);
	}
	
	// public void printNum(int x) throws InterruptedException {
	// 	startSema.acquire();
	// 	System.out.println(x);
	// 	endSema.acquire();
	// }
	
	public void printNum(int x) throws InterruptedException {
		startSema.acquire();
		System.out.println(Thread.currentThread().getName() + " " + x);
		endSema.acquire();
	}
	public void start(){
		startSema.release(3);
	}
	
	public void end(){
		endSema.release(3);
	}
}

class PrintNumAtSameTime_CyclicBarrier{
	CyclicBarrier startCyc, endCyc;
	PrintNumAtSameTime_CyclicBarrier(){
		startCyc = new CyclicBarrier(3, ()->{
			System.out.println("all start!");
		});
		
		endCyc = new CyclicBarrier(3, ()->{
			System.out.println("all end!");
		});
	}
	
	public void printNum(int x) throws InterruptedException, BrokenBarrierException {
		startCyc.await();
		System.out.println(x);
		endCyc.await();
	}
}