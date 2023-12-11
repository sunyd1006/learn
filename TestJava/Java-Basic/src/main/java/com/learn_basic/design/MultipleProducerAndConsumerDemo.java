package com.learn_basic.design;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1. 比如用 while(), 保证生产者不覆盖未消费的消息（没有阻塞生产者去写入满列表）；
 *    消费者不消费太快（没有阻塞消费者去消费空列表）
 */
public class MultipleProducerAndConsumerDemo {
    private static AtomicInteger num = new AtomicInteger();
    private static final int MAX_LEN = 10;
    private Deque<Integer> pool = new LinkedList<>();

    public static void main(String[] args) {
        MultipleProducerAndConsumerDemo sunProducerAndConsumer = new MultipleProducerAndConsumerDemo();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                sunProducerAndConsumer.produce();
            }, "producer" + i).start();
        }

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                sunProducerAndConsumer.consume();
            }, "consumer" + i).start();
        }
    }

    public void produce() {
        while (true) {                                  // 生产者要一直生产
            try {
                Thread.sleep(3000);               // 控制生产速率
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized(pool) {
                /**
                 * NOTE: if (pool.size() == MAX_LEN) {
                 * done 这里用 if 是错误的. 否则不是 自旋阻塞。
                 * 被人唤醒后直接开始执行写入消息队列，消息队列里的数据会被覆盖，造成消息丢失
                 *
                 * Thread[consumer1,5,main] ===> consume : 864 [poll size]: 570
                 * Thread[producer1,5,main] produce ===> : 1435 [poll size]: 571
                 * 说明：生产都1435，consumer才864，而队列只有10个大小，说明会遗漏消费，如果不是while 阻塞的话
                 */
                while (pool.size() == MAX_LEN) {
                    try {
                        System.out.println("pool is full, please wait.");
                        pool.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                pool.add(num.incrementAndGet());
                System.out.println(Thread.currentThread()  + " produce ===> : "+ num.get()+ " [poll size]: " + pool.size());
                pool.notify(); // or notifyAll()
            }
        }
    }

    public void consume() {
        while (true) {                             // 消费者要一直消费
            try {
                Thread.sleep(2000);         // 控制消费速率
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized(pool) {
                /**
                 * if (pool.size() == 0) {
                 *
                 * 注意着一行报错：int used = pool.pollFirst();
                 * Exception in thread "consumer1" Exception in thread "consumer2" java.lang.NullPointerException
                 * 	at com.design.MultipleProducerAndConsumerDemo.consume(MultipleProducerAndConsumerDemo.java:70)
                 * 	at com.design.MultipleProducerAndConsumerDemo.lambda$main$1(MultipleProducerAndConsumerDemo.java:21)
                 */
                while (pool.size() == 0) {
                    try {
                        System.out.println("pool is empty, please wait.");
                        pool.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int used = pool.pollFirst();
                System.out.println(Thread.currentThread()  + " ===> consume : "+ used+" [poll size]: " + pool.size());
                pool.notify(); // or notifyAll()
            }
        }
    }
}