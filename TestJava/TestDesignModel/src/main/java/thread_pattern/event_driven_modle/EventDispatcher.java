package thread_pattern.event_driven_modle;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName: EventDispatcher
 * @Description: 事件分发器
 * @Author: liulianglin
 * @DateTime 2022年4月21日 下午2:57:35
 */
public enum EventDispatcher {
  // 采用枚举实现单例模式
  INSTANCE;

  // 事件类型与事件处理器列表的映射关系
  private final Map<GlobalEventType, List<EventListener<? extends BaseEvent>>> observers = new HashMap<>();
  // 异步执行的事件队列
  private LinkedBlockingQueue<BaseEvent> eventQueue = new LinkedBlockingQueue<>();
  private ExecutorService executorService;

  EventDispatcher() {
    executorService = Executors.newSingleThreadExecutor();
    executorService.execute(new EventWorker());
  }

  /**
   * @ClassName: EventWorker
   * @Description: 异步执行线程
   * @Author: liulianglin
   * @DateTime 2022年4月21日 下午3:43:24
   */
  private class EventWorker implements Runnable {
    @Override
    public void run() {
      while (true) {
        try {
          BaseEvent event = eventQueue.take();
          if (event.getEvtType() == GlobalEventType.EXIT) {
            break;
          }
          handler(event);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 注册事件
   *
   * @param evtType  事件类型
   * @param listener 具体监听器
   */
  public void registerEvent(GlobalEventType evtType, EventListener<? extends BaseEvent> listener) {
    List<EventListener<? extends BaseEvent>> listeners = observers.get(evtType);
    if (listeners == null) {
      listeners = new ArrayList<>();
      observers.put(evtType, listeners);
    }
    listeners.add(listener);
  }

  /**
   * 派发事件
   *
   * @param event
   */
  public void dispatchEvent(BaseEvent event) {
    if (event == null) {
      throw new NullPointerException("the event cannot be null");
    }
    if (event.isSync()) {
      // 如果事件是同步的，那么就在消息主线程执行逻辑
      handler(event);
    } else {
      // 否则，就丢到事件线程异步执行
      eventQueue.add(event);
    }
  }

  /**
   * dispatcher 的 handler
   * <li>如果消息线程要求同步, 则消息线程调用 instance.dispatchEvent(), 进一步就调用了 handler， 即 dispatchEvent {handler} </li>
   * <li>如果消息线程不要求同步处理, 则会由 Dispatcher 的worker 线程异步调用 handler 方法</li>
   *
   * @param event
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void handler(BaseEvent event) {
    GlobalEventType evtType = event.getEvtType();
    List<EventListener<? extends BaseEvent>> listeners = observers.get(evtType);
    if (listeners != null) {
      // 一个事件可能被多个事件处理器关注及待处理
      for (EventListener listener : listeners) {
        try {
          listener.handleEvent(event);
        } catch (Exception e) {
          // 防止其中一个listener报异常而中断其他逻辑
          e.printStackTrace();
        }
      }
    } else {
      throw new EventDrivenException("can not find the event handler with the event type is " + event.getEvtType());
    }
  }


  /**
   * @return void
   * @throws
   * @Description: 停止异步事件分发线程
   * @Author: liulianglin
   * @Datetime: 2022年5月19日 下午7:29:17
   */
  public void stopSyncEventDispatchThread() {
    eventQueue.add(new BaseEvent(GlobalEventType.EXIT, false));
  }

}