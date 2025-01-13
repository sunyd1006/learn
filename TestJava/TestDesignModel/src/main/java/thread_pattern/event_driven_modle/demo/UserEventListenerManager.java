package thread_pattern.event_driven_modle.demo;

import thread_pattern.event_driven_modle.AbstractEventListenerManager;
import thread_pattern.event_driven_modle.EventAnnotation;
import thread_pattern.event_driven_modle.GlobalEventType;

/**
 *
 * @ClassName: UserEventListenerManager
 * @Description: 事件监听器管理类
 * @Author: liulianglin
 * @DateTime 2022年4月21日 下午3:08:38
 *
 * 不同的业务模块可以创建属于自己的EventListenerManager
 */

// @Component
public class UserEventListenerManager extends AbstractEventListenerManager {

  /**
   * 在构造器中调用父类的initEventListener，完成下方被注解修饰的所有事件监听器自动注册到EventDispatcher
   */
  public UserEventListenerManager() {
    super.initEventListener();
  }

  /**
   * 通过@EventAnnotation定义该事件监听器感兴趣的事件类型
   */
  @EventAnnotation(eventType= GlobalEventType.LOGIN)
  public UserLoginEventListener exampleEvent;

  //这里继续添加其他事件监听器
  //@Evt(eventType=GlobalEventType.EXIT)
  //public UserLoginEventListener exampleEvent2;
}