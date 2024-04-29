package thread_pattern.event_driven_model_simple.demo;

import org.junit.jupiter.api.Test;
import thread_pattern.event_driven_model_simple.BaseEvent;
import thread_pattern.event_driven_model_simple.EventDispatcher;

/**
 * reference: https://blog.csdn.net/liulianglin/article/details/124325950
 * <li>
 * 背景：
 * - (event, eventListener) 本质是一个事件和事件处理器
 * - EventHandlerManager 管理EventHandler。
 * -  如果非Spring项目，可以EventHandlerManager注册, manager进一步调用EventDispatcher.INSTANCE.registerEvent(event, eventHandler)注册
 * -  如果是Spring可以用@Component单例创建一个bean, 此demo通过AbstractEventHandlerManager实现自动注册(event, eventHandler)
 * - Reactor/Dispatcher 主线程（此demo 是单Reactor多线程模式的简化版本）
 * </li>
 *
 * 整体步骤
 * 1. client: 发送很多事件
 * 2. 管理员注册一些事件(eg, login, exit) 到EventHandlerManager里面;
 * 3. 主线程 （reactor线程，也称为 dispatcher）收到一些事件，根据事件排序和及时性要求来 dispatchEvent。
 *    （handler event 就行把一个event 交给这些 listener 调用自己的 handleEvent处理）
 * <li>如果消息线程要求事件被同步处理, 则消息线程调用 instance.dispatchEvent(), 进一步就调用了 handler， 即 dispatchEvent {handler} </li>
 * <li>如果消息线程不要求事件被同步处理, 则会由 Dispatcher内部的worker-thread 异步调用 handler 方法</li>
 * 4. dispatcher 可以开始，也可以被关闭（调用dispatcher.stop)
 */
public class UserEventDrivenDemo {

  @Test
  void main() throws InterruptedException {

    Thread server = new Thread(this::server);
    server.start();

    Thread.sleep(1000);

    Thread client = new Thread(this::client);
    client.start();

    client.join();
    server.join();
  }

  void client() {
    // 2. Server 收到了一个Login事件
    BaseEvent event = new UserLoginEvent(BaseEvent.GlobalEventType.LOGIN, "超级管理员");
    event.setSync(false);

    for (int i = 0; i < 10; i++) {
      EventDispatcher.INSTANCE.dispatchEvent(event);
    }
  }

  void server() {
    // 1. Server注册了感兴趣的事件类型
    UserEventHandlerManager login = new UserEventHandlerManager();

    try {
      System.out.println("模拟业务处理1秒钟....");
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // EventDispatcher.INSTANCE.shutdown();
  }
}
