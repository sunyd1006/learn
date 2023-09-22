package thread_pattern.event_driven_model.event;

/**
 * Interface defining events api.
 * 事件接口
 * @author
 */
public interface EventCommonInfo<T extends Enum<T>> {

    /**
     * 事件类型
     * @return
     */
    T getType();

    /**
     * 事件触发的时间戳
     * @return 时间戳
     */
    long getTimestamp();

    /**
     * 获取分发器
     * 备注：如果事件想知道自己被谁分发出去的话
     * 
     * @return {@link Dispatcher}
     */
    Dispatcher getDispatcher();

    /**
     * human string of event
     * @return
     */
    @Override
    String toString();

}