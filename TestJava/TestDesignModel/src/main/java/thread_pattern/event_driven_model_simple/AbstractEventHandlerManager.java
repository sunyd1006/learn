package thread_pattern.event_driven_model_simple;

import java.lang.reflect.Field;

/**
 * @ClassName: AbstractEventHandlerManager
 * @Description: 事件监听器管理者基类，通过注解实现事件监听器的自动注册
 * @Author: liulianglin
 * @DateTime 2022年4月21日 下午3:05:43
 * <p>
 * 将要注册事件的监听器，在 AbstractEventHandlerManager 子类属性中，通过注解 @EventAnnotation标记监听器所感兴趣的事件。
 * 详细参见EventListenerManager类
 */
public abstract class AbstractEventHandlerManager {
  /**
   * 初始化 注册监听器（启动程序时调用）
   */
  @SuppressWarnings({"unchecked"})
  public AbstractEventHandlerManager() {
    Field[] fields = getClass().getDeclaredFields();
    for (Field f : fields) {
      EventAnnotation evt = f.getAnnotation(EventAnnotation.class);
      if (evt != null) {
        BaseEvent.GlobalEventType eventType = evt.eventType();
        Class<?> listenClass = f.getType();
        try {
          EventHandler<? extends BaseEvent> handler = (EventHandler<? extends BaseEvent>) listenClass.getDeclaredConstructor().newInstance();
          // 注册事件
          EventDispatcher.INSTANCE.registerEvent(eventType, handler);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    EventDispatcher.INSTANCE.descEvents();
  }

}
