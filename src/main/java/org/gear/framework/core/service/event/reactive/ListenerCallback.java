package org.gear.framework.core.service.event.reactive;

import org.gear.framework.core.service.event.actions.Event;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface ListenerCallback<T extends Event> {

    void onEvent(T event);
}
