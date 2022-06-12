package io.vocabulary.utils;

import com.google.common.eventbus.EventBus;

/**
 * @author Ashin
 * @date 2022/6/2
 * @Desc
 */
public class EventBusUtil {

    public EventBusUtil() {
    }

    private final static EventBus EVENT_BUS = new EventBus();

    public static EventBus getEventBus(){
        return EVENT_BUS;
    }
}
