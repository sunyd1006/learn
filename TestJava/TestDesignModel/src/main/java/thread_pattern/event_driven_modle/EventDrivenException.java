package thread_pattern.event_driven_modle;


/**
 *
 * @ClassName: EventDrivenException
 * @Description: 事件驱动内部处理异常
 * @Author: liulianglin
 * @DateTime 2022年4月21日 下午3:39:16
 */
public class EventDrivenException  extends RuntimeException{
  public EventDrivenException(String message) {
    super(message);
  }
}