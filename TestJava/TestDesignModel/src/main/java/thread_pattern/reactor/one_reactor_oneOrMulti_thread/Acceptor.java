package thread_pattern.reactor.one_reactor_oneOrMulti_thread;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {

  private final Selector selector;

  private final ServerSocketChannel serverSocketChannel;

  Acceptor(ServerSocketChannel serverSocketChannel, Selector selector) {
    this.serverSocketChannel = serverSocketChannel;
    this.selector = selector;
  }

  @Override
  public void run() {
    SocketChannel socketChannel;
    try {
      socketChannel = serverSocketChannel.accept();
      if (socketChannel != null) {
        System.out.println(String.format("收到来自 %s 的连接",
          socketChannel.getRemoteAddress()));

        // 这里把客户端通道传给Handler，Handler负责接下来的事件处理（除了连接事件以外的事件均可）
        new AsyncHandler(socketChannel, selector);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
