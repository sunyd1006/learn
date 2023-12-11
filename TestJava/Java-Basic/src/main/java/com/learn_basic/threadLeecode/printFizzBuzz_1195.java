package com.learn_basic.threadLeecode;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

public class printFizzBuzz_1195 {
	public static void main(String[] args) throws InterruptedException {
		FizzBuzzSemaphore demo = new FizzBuzzSemaphore(15);
		
		new Thread(() -> {
			try {
				demo.fizz(() -> System.out.print("fizz "));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		
		new Thread(() -> {
			try {
				demo.buzz(() -> System.out.print("buzz "));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		
		new Thread(() -> {
			try {
				demo.fizzbuzz(() -> System.out.print("fizzbuzz "));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		
		new Thread(() -> {
			try {
				demo.number((x) -> System.out.print(x + " "));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
}


class FizzBuzzSemaphore {
	private int n;
	private volatile int curValue;
	private Semaphore fizz, buzz, fizzbuzz, number;
	public FizzBuzzSemaphore(int n) { // constructor
		this.n = n;
		fizz = new Semaphore(0);
		buzz = new Semaphore(0);
		fizzbuzz = new Semaphore(0);
		number = new Semaphore(1);
	}
	public void fizz(Runnable printFizz) throws InterruptedException {
		while (curValue <= n) {
			fizz.acquire();
			printFizz.run();
			number.release();
		}
	}
	public void buzz(Runnable printBuzz) throws InterruptedException {
		while (curValue <= n) {
			buzz.acquire();
			printBuzz.run();
			number.release();
		}
	}
	public void fizzbuzz(Runnable printFizzbuzz) throws InterruptedException {
		while (curValue <= n) {
			fizzbuzz.acquire();
			printFizzbuzz.run();
			number.release();
		}
	}
	public void number(IntConsumer consumer) throws InterruptedException {
		for (curValue = 1; curValue <= n; curValue++) {
			number.acquire();
			if (curValue % 3 == 0 && curValue % 5 == 0) {
				fizzbuzz.release();
			} else if (curValue % 3 == 0) {
				fizz.release();
			} else if (curValue % 5 == 0) {
				buzz.release();
			} else {
				consumer.accept(curValue);
				number.release();
			}
		}
	}
}