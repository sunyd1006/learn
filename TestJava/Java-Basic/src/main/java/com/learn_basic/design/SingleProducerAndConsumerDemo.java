package com.learn_basic.design;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleProducerAndConsumerDemo {
    private static AtomicInteger atomicInteger = new AtomicInteger();
    private final int MAX_LEN = 10;
    private Queue<Integer> queue = new LinkedList<Integer>();

    class Producer extends Thread {
        @Override
        public void run() {
            producer();
        }

        private void producer() {
            while (true) {
                synchronized (queue) {
                    // 如果队列已满
                    while (queue.size() == MAX_LEN) {
                        System.out.println("当前队列满，等待消费者消费");
                        try {
                            queue.wait(); // 等待消费者消费
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 添加元素
                    int produced = atomicInteger.incrementAndGet();
                    queue.add(produced);
                    System.out.println("produce ===> : " + produced + " current size: " + queue.size());
                    queue.notify(); // 通知消费者
                }
                try {
                    Thread.sleep(500); // 模拟生产耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Consumer extends Thread {
        @Override
        public void run() {
            consumer();
        }

        private void consumer() {
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        System.out.println("队列空，等待生产者生产");
                        try {
                            queue.wait(); // 等待生产者生产
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 消费元素
                    int consumed = queue.poll();
                    System.out.println("consume ===> : " + consumed + " current size: " + queue.size());
                    queue.notify(); // 通知生产者
                }
                try {
                    Thread.sleep(700); // 模拟消费耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        // 单个生产者，单个消费者
        SingleProducerAndConsumerDemo demo = new SingleProducerAndConsumerDemo();
        Producer producer = demo.new Producer();
        Consumer consumer = demo.new Consumer();

        producer.start();
        consumer.start();
    }
}