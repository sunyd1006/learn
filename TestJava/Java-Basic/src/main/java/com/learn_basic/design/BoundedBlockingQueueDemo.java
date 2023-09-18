package com.learn_basic.design;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// BoundedBlockingQueue
class BoundedBlockingQueueDemo {
	private Queue<Integer> queue;
	private int capacity;
	
	public BoundedBlockingQueueDemo(int capacity) {
		this.queue = new LinkedList<>();
		this.capacity = capacity;
	}
	
	public void enqueue(int element) throws InterruptedException {
		synchronized (queue) {
			while (queue.size() == capacity) {
				queue.wait();
			}
			
			queue.add(element);
			System.out.println(Thread.currentThread().getName() + " put: " + element);
		}
	}
	
	public int dequeue() throws InterruptedException {
		int res = -1;
		synchronized (queue) {
			while (queue.size() == 0) {
				queue.wait();
			}
			
			res = queue.poll();
			System.out.println(Thread.currentThread().getName() + " get: " + res);
		}
		return res;
	}
	
	public int size() {
		return queue.size();
	}
}


class BoundedBlockingQueue_Synchronized {
	private Deque<Integer> queue;
	private int capacity;
	
	public BoundedBlockingQueue_Synchronized(int capacity) {
		this.queue = new LinkedList<>();
		this.capacity = capacity;
	}
	
	public void enqueue(int element) throws InterruptedException {
		synchronized (queue) {
			while (queue.size() == capacity) {
				queue.wait();
			}
			
			queue.addLast(element);
			queue.notifyAll(); // NOTE: error 记得去唤醒
		}
	}
	
	public int dequeue() throws InterruptedException {
		int res = -1;
		synchronized (queue) {
			while (queue.size() == 0) {
				queue.wait();
			}
			
			res = queue.pollFirst();
			queue.notifyAll(); // NOTE: error 记得去唤醒
		}
		return res;
	}
	
	public int size() {
		return queue.size();
	}
}

class BoundedBlockingQueue_ReentrantLock {
	private Deque<Integer> queue;
	private int capacity;
	private ReentrantLock lock;
	private Condition emptyCond;
	private Condition fullCond;
	
	public BoundedBlockingQueue_ReentrantLock(int capacity) {
		this.queue = new LinkedList<>();
		this.capacity = capacity;
		this.lock = new ReentrantLock();
		this.emptyCond = lock.newCondition();
		this.fullCond = lock.newCondition();
	}
	
	public void enqueue(int element) throws InterruptedException {
		lock.lock();
		int res = -1;
		try {
			while (queue.size() == this.capacity){
				fullCond.await();
			}
			
			queue.addLast(element);
			emptyCond.signal(); // NOTE: error signal not notify
		}finally {
			lock.unlock();
		}
	}
	
	public int dequeue() throws InterruptedException {
		lock.lock();
		int res = -1;
		try {
			while (queue.size() == 0){
				emptyCond.await();
			}
			
			res = queue.pollFirst();
			fullCond.signal(); // NOTE: error signal not notify
		}finally {
			lock.unlock();
		}
		return res;
	}
	
	public int size() {
		return queue.size();
	}
}