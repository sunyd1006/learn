package thread_pattern.event_driven_model.base;

import thread_pattern.event_driven_model.event.AbstractEvent;
import thread_pattern.event_driven_model.event.Dispatcher;

/**
 * @author rectcircle
 */
public class BaseEvent extends AbstractEvent<BaseEventEnum> {

    private final String word;

    public BaseEvent(final Dispatcher dispatcher, final BaseEventEnum type, final String word) {
        super(dispatcher, type);
        this.word = word;
    }

    public String getWord() {
        return word;
    }

}