package thread_pattern.event_driven_modle.demo;

import org.junit.jupiter.api.Test;
import thread_pattern.event_driven_modle.BaseEvent;
import thread_pattern.event_driven_modle.EventDispatcher;
import thread_pattern.event_driven_modle.GlobalEventType;

/**
 * reference: https://blog.csdn.net/liulianglin/article/details/124325950
 *
 * - eventLister 也可以称为 eventHandler ，意思是一样的
 * - UserEventListenerManager 同理也可以称为 EventHandlerManager。 如果是Spring可以用@Component单例创建一个bean；如果非Spring项目需要手动创建
 *
 * 整体思路
 * 1. 有些事件(login, exit) 是一些人关心的( some listenerManager)
 * 2. 主线程产生了一些时间，交给dispatcher分发
 * 3. dispatcher 根据事件排序及时性的要求处理事件（handler event 就行把一个event 交给这些 listener 调用自己的 handleEvent处理）
 *   <li>如果消息线程要求事件被同步处理, 则消息线程调用 instance.dispatchEvent(), 进一步就调用了 handler， 即 dispatchEvent {handler} </li>
 *   <li>如果消息线程不要求事件被同步处理, 则会由 Dispatcher内部的worker-thread 异步调用 handler 方法</li>
 * 4. dispatcher 可以开始，也可以被关闭（调用dispatcher.stop)
 *
 * @author sunyindong.syd
 */
public class UserEventDrivenDemo {

  @Test
  void test() {
    // 手动创建一个 事件监听管理类
    UserEventListenerManager createByManual = new UserEventListenerManager();

    BaseEvent event = new UserLoginEvent(GlobalEventType.LOGIN, "超级管理员");
    event.setSync(false);
    EventDispatcher.INSTANCE.dispatchEvent(event);

    try {
      System.out.println("模拟业务处理1秒钟....");
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("处理完毕，关闭");
    //EventDispatcher.INSTANCE.shutdown();
  }
}