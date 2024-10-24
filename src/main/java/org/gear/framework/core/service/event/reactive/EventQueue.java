package org.gear.framework.core.service.event.reactive;

import org.gear.framework.core.service.event.exception.EmptyEventQueueException;

import java.util.ArrayList;
import java.util.List;

public class EventQueue {
    private final List<ReactiveEvent> events;

    public EventQueue() {
        this.events = new ArrayList<>();
    }

    public void pushEvent(ReactiveEvent event) {
        this.events.add(event);
    }

    public void clearEvents() {
        this.events.clear();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    public ReactiveEvent popEvent() {
        if(!isEmpty()) {
            return events.removeFirst();
        }

        throw new EmptyEventQueueException();
    }
}
