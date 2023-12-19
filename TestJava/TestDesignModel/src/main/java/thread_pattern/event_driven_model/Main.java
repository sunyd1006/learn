package thread_pattern.event_driven_model;

import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

import thread_pattern.event_driven_model.base.BaseEvent;
import thread_pattern.event_driven_model.base.BaseEventEnum;
import thread_pattern.event_driven_model.event.AsyncDispatcher;

/**
 * 讲解：https://www.rectcircle.cn/posts/java-event-driver-model-impl/
 * code：https://github.com/rectcircle/java-event-driver-learn/tree/master/src/main/java/cn/rectcircle/learn/main
 *
 * 泛型参考：https://pdai.tech/md/java/basic/java-basic-x-generic.html
 *
 */
@SuppressWarnings({"PMD.ThreadPoolCreationRule", "PMD.UndefineMagicConstantRule"})
@Slf4j
public class Main {

    public static void main(String[] args) {
        
        AsyncDispatcher dispatcher = new AsyncDispatcher(
            Executors.newSingleThreadExecutor()
        );

        // 注册处理器
        dispatcher.register(BaseEventEnum.HELLO, (BaseEvent e) -> {
            System.out.println("hello");
            log.info("Hello " + e.getWord());
        });
        dispatcher.register(BaseEventEnum.HI, (BaseEvent e) -> {
            System.out.println("hi");
            log.info("Hi " + e.getWord());
        });
        dispatcher.register(BaseEventEnum.class, (BaseEvent e) -> {
            log.info("通过 class 注册 " + e.getWord());
        });

        // 启动事件循环
        dispatcher.startDispatcher();

        // 发送事件
        for (int i = 0; i < 10; i++) {
            dispatcher.dispatchEvent(new BaseEvent(dispatcher, BaseEventEnum.HELLO, "World"));
            dispatcher.dispatchEvent(new BaseEvent(dispatcher, BaseEventEnum.HI, "世界"));
        }
    }
}