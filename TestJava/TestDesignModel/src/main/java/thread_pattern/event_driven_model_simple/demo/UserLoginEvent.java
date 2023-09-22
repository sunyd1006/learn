package thread_pattern.event_driven_model_simple.demo;


import thread_pattern.event_driven_model_simple.BaseEvent;

/**
 *
 * @ClassName: UserLoginEvent
 * @Description: 示例事件（用于演示如何使用该事件驱动框架）
 * @Author: liulianglin
 * @DateTime 2022年4月21日 下午2:50:54
 */
public class UserLoginEvent extends BaseEvent {
  private String userName;

  public UserLoginEvent(GlobalEventType evtType, String userName) {
    super(evtType);
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

}