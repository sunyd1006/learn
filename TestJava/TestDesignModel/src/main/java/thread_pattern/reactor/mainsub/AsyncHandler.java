/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2019 All Rights Reserved.
 */
package thread_pattern.reactor.mainsub;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author sunqinwen
 * @version \: AsyncHandler.java,v 0.1 2019-04-01 11:14
 */
public class AsyncHandler implements Runnable {

    private final Selector selector;

    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;

    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(2048);

    private final static int READ = 0; //读取就绪
    private final static int SEND = 1; //响应就绪
    private final static int PROCESSING = 2; //处理中

    private int status = READ; //所有连接完成后都是从一个读取动作开始的

    private int num; //从反应堆序号

    //开启线程数为4的异步处理线程池
    private static final ExecutorService workers = Executors.newFixedThreadPool(5);

    AsyncHandler(SocketChannel socketChannel, Selector selector, int num) throws IOException {
        this.num = num; //为了区分Handler被哪个从反应堆触发执行做的标记
        this.socketChannel = socketChannel; //接收客户端连接
        this.socketChannel.configureBlocking(false); //置为非阻塞模式（selector仅允非阻塞模式）
        selectionKey = socketChannel.register(selector, 0); //将该客户端注册到selector，得到一个SelectionKey，以后的select到的就绪动作全都是由该对象进行封装
        selectionKey.attach(this); //附加处理对象，当前是Handler对象，run是对象处理业务的方法
        selectionKey.interestOps(SelectionKey.OP_READ); //走到这里，说明之前Acceptor里的建连已完成，那么接下来就是读取动作，因此这里首先将读事件标记为“感兴趣”事件
        this.selector = selector;
        this.selector.wakeup();
    }

    @Override
    public void run() { //如果一个任务正在异步处理，那么这个run是直接不触发任何处理的，read和send只负责简单的数据读取和响应，业务处理完全不阻塞这里的处理
        switch (status) {
            case READ:
                read();
                break;
            case SEND:
                send();
                break;
            default:
        }
    }

    private void read() {
        if (selectionKey.isValid()) {
            try {
                readBuffer.clear();
                int count = socketChannel.read(readBuffer); //read方法结束，意味着本次"读就绪"变为"读完毕"，标记着一次就绪事件的结束
                if (count > 0) {
                    status = PROCESSING; //置为处理中，处理完成后该状态为响应，表示读入处理完成，接下来可以响应客户端了
                    workers.execute(this::readWorker); //异步处理
                } else {
                    selectionKey.cancel();
                    socketChannel.close();
                    System.out.println(String.format("%d号SubReactor触发：read时-------连接关闭", num));
                }
            } catch (IOException e) {
                System.err.println("处理read业务时发生异常！异常信息：" + e.getMessage());
                selectionKey.cancel();
                try {
                    socketChannel.close();
                } catch (IOException e1) {
                    System.err.println("处理read业务关闭通道时发生异常！异常信息：" + e.getMessage());
                }
            }
        }
    }

    void send() {
        if (selectionKey.isValid()) {
            status = PROCESSING; //置为执行中
            workers.execute(this::sendWorker); //异步处理
            selectionKey.interestOps(SelectionKey.OP_READ); //重新设置为读
        }
    }

    //读入信息后的业务处理
    private void readWorker() {
        try {
            Thread.sleep(5000L); //性能瓶颈模拟
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(String.format("%d号SubReactor触发：收到来自客户端%s的消息: %s",
                    num, socketChannel.getRemoteAddress(), new String(readBuffer.array())));
        } catch (IOException e) {
            System.err.println("异步处理read业务时发生异常！异常信息：" + e.getMessage());
        }
        status = SEND;
        selectionKey.interestOps(SelectionKey.OP_WRITE); //注册写事件
        this.selector.wakeup(); //唤醒阻塞在select的线程
    }

    private void sendWorker() {
        try {
            sendBuffer.clear();
            sendBuffer.put(String.format("%d号SubReactor触发：我收到来自%s的信息辣：%s,  200ok;",
                    num, socketChannel.getRemoteAddress(),
                    new String(readBuffer.array())).getBytes());
            sendBuffer.flip();

            int count = socketChannel.write(sendBuffer); //write方法结束，意味着本次写就绪变为写完毕，标记着一次事件的结束

            if (count < 0) {
                //同上，write场景下，取到-1，也意味着客户端断开连接
                selectionKey.cancel();
                socketChannel.close();
                System.out.println(String.format("%d号SubReactor触发：send时-------连接关闭", num));
            }

            //没断开连接，则再次切换到读
            status = READ;
        } catch (IOException e) {
            System.err.println("异步处理send业务时发生异常！异常信息：" + e.getMessage());
            selectionKey.cancel();
            try {
                socketChannel.close();
            } catch (IOException e1) {
                System.err.println("异步处理send业务关闭通道时发生异常！异常信息：" + e.getMessage());
            }
        }
    }
}
