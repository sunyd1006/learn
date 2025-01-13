package thread_pattern.event_driven_modle;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: EventAnnotation
 * @Description: 事件注解：指定感兴趣的事件类型
 * @Author: liulianglin
 * @DateTime 2022年4月21日 下午3:32:28
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventAnnotation {

  /**
   * 事件类型
   */
  GlobalEventType eventType();
}