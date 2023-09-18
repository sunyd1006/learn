package com.learn_basic.design;

import java.util.Deque;
import java.util.LinkedList;

class BoundedBlockingQueue {
	private Deque<Integer> queue;
	private int capacity;
	public BoundedBlockingQueue(int capacity) {
		this.queue = new LinkedList<>();
		this.capacity = capacity;
	}
	
	public void enqueue(int element) throws InterruptedException {
		synchronized (queue){
			while (queue.size() == capacity){
				queue.wait();
			}
			
			queue.addLast(element);
			queue.notifyAll(); // NOTE: error 记得去唤醒
		}
	}
	
	public int dequeue() throws InterruptedException {
		int res = -1;
		synchronized (queue){
			while (queue.size() == 0){
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