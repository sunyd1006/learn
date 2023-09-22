package thread_pattern.event_driven_model_simple.demo;

import thread_pattern.event_driven_model_simple.AbstractEventHandlerManager;
import thread_pattern.event_driven_model_simple.BaseEvent;
import thread_pattern.event_driven_model_simple.EventAnnotation;

/**
 *
 * @ClassName: UserEventHandlerManager
 * @Description: 事件监听器管理类
 * @Author: liulianglin
 * @DateTime 2022年4月21日 下午3:08:38
 *
 * 不同的业务模块可以创建属于自己的EventListenerManager
 */

public class UserEventHandlerManager extends AbstractEventHandlerManager {

  /**
   * 在构造器中调用父类的initEventListener，完成下方被注解修饰的所有事件监听器自动注册到EventDispatcher
   */
  public UserEventHandlerManager() {}


  /**
   * 注册 UserEventHandlerManager 感兴趣的事件，常用的方式
   * a. 基于注解的事件注册方法
   * b. 可以实现 addEventHandler()
   */
  @EventAnnotation(eventType= BaseEvent.GlobalEventType.LOGIN)
  public UserLoginEventHandler loginEvent;

  @EventAnnotation(eventType= BaseEvent.GlobalEventType.EXIT)
  public UserLoginEventHandler exitEvent;
}