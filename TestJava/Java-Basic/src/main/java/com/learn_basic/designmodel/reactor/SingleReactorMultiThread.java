package com.learn_basic.designmodel.reactor;

import com.sun.deploy.net.socket.UnixDomainSocket;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleReactorMultiThread {
  public static void main(String[] args) {
    int workCount = Runtime.getRuntime().availableProcessors();
    System.out.println(workCount);
  }
}

//
// class Reactor implements Runnable {
//   private Reactor() throws Exception {
//     SelectionKey sk =
//             serverSocket.register(selector,
//                     SelectionKey.OP_ACCEPT);
//     // attach Acceptor 处理新连接
//     sk.attach(new Acceptor());
//   }
//   public void run() {
//     try {
//       while (!Thread.interrupted()) {
//         selector.select();
//         Set selected = selector.selectedKeys();
//         Iterator it = selected.iterator();
//         while (it.hasNext()) {
//           it.remove();
//           //分发事件处理
//           dispatch((SelectionKey) (it.next()));
//         }
//       }
//     } catch (IOException ex) {
//       //do something
//     }
//   }
//   void dispatch(SelectionKey k) {
//     // 若是连接事件获取是acceptor
//     // 若是IO读写事件获取是handler
//     Runnable runnable = (Runnable) (k.attachment());
//     if (runnable != null) {
//       runnable.run();
//     }
//   }
// }
//
//
// // 多线程处理读写业务逻辑
// class MultiThreadHandler implements Runnable {
//   public static final int READING = 0, WRITING = 1;
//   int state;
//   final SocketChannel socket;
//   final SelectionKey sk;
//   // 多线程处理业务逻辑
//   ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//   public MultiThreadHandler(SocketChannel socket, Selector selector) throws Exception {
//     this.state = READING;
//     this.socket = socket;
//     sk = socket.register(selector, SelectionKey.OP_READ);
//     sk.attach(this);
//     socket.configureBlocking(false);
//   }
//
//   @Override
//   public void run() {
//     if (state == READING) {
//       read();
//     } else if (state == WRITING) {
//       write();
//     }
//   }
//
//   private void read() {
//     // 任务异步处理
//     executorService.submit(() -> process());
//     // 下一步处理写事件
//     sk.interestOps(SelectionKey.OP_WRITE);
//     this.state = WRITING;
//   }
//
//   private void write() {
//     // 任务异步处理
//     executorService.submit(() -> process());
//     // 下一步处理读事件
//     sk.interestOps(SelectionKey.OP_READ);
//     this.state = READING;
//   }
//
//   // task 业务处理
//   public void process() {
//     // do IO ,task,queue something
//   }
// }

/**
 * 连接事件就绪,处理连接事件
 */
// class Acceptor implements Runnable {
//   @Override
//   public void run() {
//     try {
//       UnixDomainSocket serverSocket;
//       SocketChannel c = serverSocket.accept();
//       if (c != null) {// 注册读写
//         new Handler(c, selector);
//       }
//     } catch (Exception e) {
//     }
//   }
// }