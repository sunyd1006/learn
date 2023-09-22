package thread_pattern.event_driven_model.event;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dispatches {@link EventCommonInfo}s in a separate thread. Currently only single thread
 * does that. Potentially there could be multiple channels for each event type
 * class and a thread pool can be used to dispatch the events.
 * 异步调度器：
 * <ui>
 * <li>每一个事件类型允许注册一个或多个事件处理器</li>
 * <li>事件循环运行在单独的线程中</li>
 * <li>事件处理器与事件循环运行在不同的线程中（异步），同一个事件的多个事件处理器运行在同一线程中（同步）</li>
 * </ui>
 * @author
 */
@Slf4j
public class AsyncDispatcher implements Dispatcher {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncDispatcher.class);
    private static final int EVENT_PRINT_THRESHOLD = 1000;

    private final BlockingQueue<EventCommonInfo<?>> eventQueue;
    private volatile boolean stopped = false;

    private Thread eventHandlingThread;
    private final ExecutorService eventHandlingPool;

    //
    protected final Map<Object, EventHandler<?, ?>> eventDispatchers;

    public AsyncDispatcher(ExecutorService eventHandlingPool) {
        this(eventHandlingPool, new LinkedBlockingQueue<>());
    }

    public AsyncDispatcher(ExecutorService eventHandlingPool, BlockingQueue<EventCommonInfo<?>> eventQueue) {
        this.eventQueue = eventQueue;
        this.eventDispatchers = new HashMap<>();
        this.eventHandlingPool = eventHandlingPool;
    }

    /**
     * 启动dispatcher
     */
    public void startDispatcher() {
        // 创建一个单线程的线程池
        ThreadPoolExecutor singleThread = new ThreadPoolExecutor(1, 1, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), runnable -> {
            eventHandlingThread = new Thread(runnable);
            eventHandlingThread.setName("AsyncDispatcher event handler");
            return eventHandlingThread;
        });
        singleThread.execute(eventExecWorker());
    }

    /**
     * 关闭事件循环
     */
    public void stopDispatcher() {
        stopped = true;
        if (eventHandlingThread != null) {
            eventHandlingThread.interrupt();
            try {
                eventHandlingThread.join();
            } catch (InterruptedException ie) {
                log.warn("Interrupted Exception while stopping", ie);
            }
        }
    }

    /**
     * 事件循环逻辑
     */
    Runnable eventExecWorker() {
        return () -> {
            while (!stopped && !Thread.currentThread().isInterrupted()) {
                EventCommonInfo<?> event;
                try {
                    event = eventQueue.take();
                } catch (InterruptedException ie) {
                    if (!stopped) {
                        log.warn("AsyncDispatcher thread interrupted", ie);
                    }
                    return;
                }
                dispatch(event);
            }
        };
    }


    /**
     * 分发函数，事件循环调用
     */
    protected <T extends Enum<T>, E extends EventCommonInfo<T>> void dispatch(E event) {
        if (log.isDebugEnabled()) {
            log.debug("Dispatching the event " + event.getClass().getName() + "."
                    + event.toString());
        }

        T type = event.getType();
        try {
            @SuppressWarnings("unchecked")
            EventHandler<T, E> handler1 = (EventHandler<T, E>) eventDispatchers.get(type.getClass());
            @SuppressWarnings("unchecked")
            EventHandler<T, E> handler2 = (EventHandler<T, E>) eventDispatchers.get(type);
            if (handler1 == null && handler2 == null) {
                throw new Exception("No handler for registered for " + type);
            }
            // 提交事件处理到 worker 线程池
            eventHandlingPool.execute(() -> {
                if (handler1 != null) {
                    handler1.handle(event);
                }
                if (handler2 != null) {
                    handler2.handle(event);
                }
            });
        } catch (Throwable t) {
            log.error("something happen in handle", t);
        }
    }

    @Override
    public <T extends Enum<T>, E extends EventCommonInfo<T>> void dispatchEvent(E event) {
        int queueSize = eventQueue.size();
        if (queueSize != 0 && queueSize % EVENT_PRINT_THRESHOLD == 0) {
            log.info("Size of event-queue is " + queueSize);
        }
        int remCapacity = eventQueue.remainingCapacity();
        if (remCapacity < EVENT_PRINT_THRESHOLD) {
            log.warn("Very low remaining capacity in the event-queue: " + remCapacity);
        }
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            log.error("interrupted while put in event queue");
            throw new RuntimeException(e);
        }
    }

    /**
     * 注册函数，某事件类型注册第一个处理器时直接注册，注册第二个时创建使用 {@link MultiHandler} 进行包裹
     */
    @Override
    public <T extends Enum<T>, E extends EventCommonInfo<T>> void register(T eventType,
                                                                           EventHandler<T, E> handler) {
        this.registerInternal(eventType, handler);
    }


    @Override
    public <T extends Enum<T>, E extends EventCommonInfo<T>> void register(Class<T> eventTypeClazz, EventHandler<T, E> handler) {
        this.registerInternal(eventTypeClazz, handler);
    }

    private <T extends Enum<T>, E extends EventCommonInfo<T>> void registerInternal(Object eventType, EventHandler<T, E> handler){
        // check to see if we have a listener registered
        // 检查是否已注册侦听器
        @SuppressWarnings("unchecked")
        EventHandler<T, E> registeredHandler = (EventHandler<T, E>) eventDispatchers.get(eventType);

        log.info("Registering " + eventType + " for " + handler.getClass());

        if (registeredHandler == null) {
            eventDispatchers.put(eventType, handler);
        } else if (!(registeredHandler instanceof MultiHandler)) {
            MultiHandler<T, E> multiHandler = new MultiHandler<>();
            multiHandler.addHandler(registeredHandler);
            multiHandler.addHandler(handler);
            eventDispatchers.put(eventType, multiHandler);
        } else {
            // already a multiListener, just add to it
            // 已经是 multiListener，只需添加即可
            MultiHandler<T, E> multiHandler = (MultiHandler<T, E>) registeredHandler;
            multiHandler.addHandler(handler);
        }
    }


    /**
     * Multiplexing an event. Sending it to different handlers that
     * are interested in the event.
     */
    static class MultiHandler<T extends Enum<T>, E extends EventCommonInfo<T>> implements EventHandler<T, E> {
        List<EventHandler<T, E>> allRelatedHandlers;

        public MultiHandler() {
            allRelatedHandlers = new ArrayList<>();
        }

        @Override
        public void handle(E event) {
            for (EventHandler<T, E> handler : allRelatedHandlers) {
                handler.handle(event);
            }
        }

        void addHandler(EventHandler<T, E> handler) {
            allRelatedHandlers.add(handler);
        }
    }

}
