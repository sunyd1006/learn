package thread_pattern.event_driven_model_simple.demo;

import org.junit.jupiter.api.Test;
import thread_pattern.event_driven_model_simple.BaseEvent;
import thread_pattern.event_driven_model_simple.EventDispatcher;

/**
 * reference: https://blog.csdn.net/liulianglin/article/details/124325950
 * <p>
 * - eventListener 也可以称为 eventHandler ，意思是一样的
 * - UserEventHandlerManager 同理也可以称为 EventHandlerManager。 如果是Spring可以用@Component单例创建一个bean；如果非Spring项目需要手动创建
 * <p>
 * 整体思路
 * 1. 有些事件(login, exit) 是一些人(AbstractEventHandlerManager)关心的
 * 2. Reactor的主线程收到(或者产生)了一些事件，交给dispatcher分发
 * 3. dispatcher 根据事件排序及时性的要求dispatchEvent（handler event 就行把一个event 交给这些 listener 调用自己的 handleEvent处理）
 * <li>如果消息线程要求事件被同步处理, 则消息线程调用 instance.dispatchEvent(), 进一步就调用了 handler， 即 dispatchEvent {handler} </li>
 * <li>如果消息线程不要求事件被同步处理, 则会由 Dispatcher内部的worker-thread 异步调用 handler 方法</li>
 * 4. dispatcher 可以开始，也可以被关闭（调用dispatcher.stop)
 *
 * @author sunyindong.syd
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