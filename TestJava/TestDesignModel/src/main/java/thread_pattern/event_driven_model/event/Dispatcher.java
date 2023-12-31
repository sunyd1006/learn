package thread_pattern.event_driven_model.event;

/**
 * EventCommonInfo Dispatcher interface. It dispatches events to registered event handlers
 * based on event types. 事件分发器。用于将事件分发到注册到 该 事件 eventType 的 EventHandler
 *
 * @author
 */
public interface Dispatcher {

    /**
     * 触发一个事件
     * @param event 一个事件
     */
    <T extends Enum<T>, E extends EventCommonInfo<T>> void dispatchEvent(E event);

    /**
     * 针对一种事件类型，注册一个消息处理器
     * @param eventType 事件类型
     * @param handler 事件处理器
     */
    <T extends Enum<T>, E extends EventCommonInfo<T>> void register(T eventType, EventHandler<T, E> handler);

    /**
     * 针对一种事件类型的类型，注册一个消息处理器，即所有该 class 的类型都会触发该处理器
     * 
     * @param eventTypeClazz 事件类型的类型
     * @param handler        事件处理器
     */
    <T extends Enum<T>, E extends EventCommonInfo<T>> void register(Class<T> eventTypeClazz, EventHandler<T, E> handler);
}
