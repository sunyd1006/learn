package thread_pattern.event_driven_model_simple;

/**
 * <li>xxxxxxxxxx</li>
 */
public interface EventHandler<E> {

  /**
   * 事件触发后，处理具体逻辑
   * @param event
   */
  void handleEvent(E event);
}
