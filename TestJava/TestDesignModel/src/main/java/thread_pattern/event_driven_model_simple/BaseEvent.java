package thread_pattern.event_driven_model_simple;

/**
 * <li>xxxxxxxxxx</li>
 *
 * @Author: sunyindong.syd
 * @Date: 2023/1/5 17:21
 */
public class BaseEvent {

  public enum GlobalEventType {
    LOGIN,// 登陆事件
    EXIT,// 退出事件
    ;
  }

  /**
   * 是否在消息主线程(一般就是注册事件的线程）同步执行
   */
  private boolean sync = true;

  /**
   * 事件类型
   */
  private final GlobalEventType evtType;

  public BaseEvent (GlobalEventType evtType) {
    this.evtType = evtType;
  }

  public BaseEvent (GlobalEventType evtType, boolean sync) {
    this.evtType = evtType;
    this.sync = sync;
  }

  public GlobalEventType getEvtType() {
    return evtType;
  }

  /**
   * 是否在消息主线程同步执行
   * @return
   */
  public boolean isSync() {
    return sync;
  }

  public void setSync (boolean sync) {
    this.sync = sync;
  }
}