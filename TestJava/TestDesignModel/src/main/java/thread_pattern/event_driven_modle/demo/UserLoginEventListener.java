package thread_pattern.event_driven_modle.demo;


import thread_pattern.event_driven_modle.EventListener;

/**
 * @ClassName: UserLoginEventListener
 * @Description: 样例事件监听器（用于演示使用）
 * @Author: liulianglin
 * @DateTime 2022年4月21日 下午3:36:17
 */
public class UserLoginEventListener implements EventListener<UserLoginEvent> {

  @Override
  public void handleEvent(UserLoginEvent event) {
    System.out.println("开始处理" + event.getEvtType() + "事件, 当前登陆用户名称=" + event.getUserName());
  }

}