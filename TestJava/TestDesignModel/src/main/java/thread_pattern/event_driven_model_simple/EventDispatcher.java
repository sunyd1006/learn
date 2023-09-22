package thread_pattern.event_driven_model_simple;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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
  private final Map<BaseEvent.GlobalEventType, List<EventHandler<? extends BaseEvent>>> eventHandlers = new ConcurrentHashMap<>();
  // 异步执行的事件队列
  private LinkedBlockingQueue<BaseEvent> eventQueue = new LinkedBlockingQueue<>();
  private ExecutorService eventExecPool;

  EventDispatcher() {
    eventExecPool = Executors.newSingleThreadExecutor();
    eventExecPool.execute(new EventWorker());
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
          if (event.getEvtType() == BaseEvent.GlobalEventType.EXIT) {
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
   * @param handler 具体监听器
   */
  public void registerEvent(BaseEvent.GlobalEventType evtType, EventHandler<? extends BaseEvent> handler) {
    List<EventHandler<? extends BaseEvent>> handlers = eventHandlers.getOrDefault(evtType, new ArrayList<>());
    handlers.add(handler);
    eventHandlers.put(evtType, handlers);
  }

  public void descEvents() {
    eventHandlers.forEach((k, v) -> {
      StringBuilder sb = new StringBuilder(k + ": {");
      for (EventHandler<? extends BaseEvent> eventHandler : v) {
        sb.append(eventHandler.getClass().getName() + ", ");
      }
      System.out.println(sb.substring(0, sb.length() - 1) + "}");
    });
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
    BaseEvent.GlobalEventType evtType = event.getEvtType();
    List<EventHandler<? extends BaseEvent>> listeners = eventHandlers.get(evtType);
    if (listeners != null) {
      // 一个事件可能被多个事件处理器关注及待处理
      for (EventHandler listener : listeners) {
        try {
          listener.handleEvent(event);
        } catch (Exception e) {
          // 防止其中一个listener报异常而中断其他逻辑
          e.printStackTrace();
        }
      }
    } else {
      throw new RuntimeException("can not find the event handler with the event type is " + event.getEvtType());
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
    eventQueue.add(new BaseEvent(BaseEvent.GlobalEventType.EXIT, false));
  }

}