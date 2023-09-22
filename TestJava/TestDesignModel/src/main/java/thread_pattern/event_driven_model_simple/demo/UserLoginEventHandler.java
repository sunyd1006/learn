package thread_pattern.event_driven_model_simple.demo;


import thread_pattern.event_driven_model_simple.EventHandler;

/**
 * @ClassName: UserLoginEventHandler
 * @Description: 样例事件监听器（用于演示使用）
 * @Author: liulianglin
 * @DateTime 2022年4月21日 下午3:36:17
 */
public class UserLoginEventHandler implements EventHandler<UserLoginEvent> {

  @Override
  public void handleEvent(UserLoginEvent event) {
    System.out.println("开始处理" + event.getEvtType() + "事件, 当前登陆用户名称=" + event.getUserName());
  }

}