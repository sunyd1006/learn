package thread_pattern.event_driven_modle;

/**
 * <li>xxxxxxxxxx</li>
 *
 * @Author: sunyindong.syd
 * @Date: 2023/1/5 17:22
 */

public interface EventListener<E> {

  /**
   * 事件触发后，处理具体逻辑
   * @param event
   */
  void handleEvent(E event);
}