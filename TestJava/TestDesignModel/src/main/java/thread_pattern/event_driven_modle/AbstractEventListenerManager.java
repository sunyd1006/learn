package thread_pattern.event_driven_modle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @ClassName: AbstractEventListenerManager
 * @Description: 事件监听器管理者基类，通过注解实现事件监听器的自动注册
 * @Author: liulianglin
 * @DateTime 2022年4月21日 下午3:05:43
 *
 * 将要注册事件的监听器，在 AbstractEventListenerManager 子类属性中，通过注解 @EventAnnotation标记监听器所感兴趣的事件。
 * 详细参见EventListenerManager类
 */
public abstract class AbstractEventListenerManager {
  /**
   * 初始化 注册监听器（启动程序时调用）
   */
  @SuppressWarnings({ "unchecked" })
  public void initEventListener () {
    Field[] fields = getClass().getDeclaredFields();
    for (Field f:fields) {
      EventAnnotation evt = f.getAnnotation(EventAnnotation.class);
      if (evt != null) {
        GlobalEventType eventType = evt.eventType();
        Class<?> listenClass = f.getType();
        EventListener<? extends BaseEvent> newInstance;
        try {
          newInstance = (EventListener<? extends BaseEvent>)listenClass.getDeclaredConstructor().newInstance();
          //注册事件
          EventDispatcher.INSTANCE.registerEvent(eventType, newInstance);
        } catch (InstantiationException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        } catch (SecurityException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
