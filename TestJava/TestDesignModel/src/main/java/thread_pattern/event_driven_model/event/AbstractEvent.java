package thread_pattern.event_driven_model.event;

import lombok.Getter;

/**
 * Parent class of all the events. All events extend this class.
 * 所有事件的父类：包含类型和时间戳
 * 
 * @author
 */
@Getter
public abstract class AbstractEvent<T extends Enum<T>> implements EventCommonInfo<T> {
    private final T type;
    private final long timestamp;
    private final Dispatcher dispatcher;
    public AbstractEvent(Dispatcher dispatcher, T type) {
        // We're not generating a real timestamp here. It's too expensive.
        // System.currentTimeMillis 存在一定性能问题 https://www.jianshu.com/p/3fbe607600a5
        this(dispatcher, type, -1L);
    }
    public Dispatcher getDispatcher() {
        return dispatcher;
    }
    public AbstractEvent(Dispatcher dispatcher, T type, long timestamp) {
        this.dispatcher = dispatcher;
        this.type = type;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "EventType: " + getType();
    }

}
