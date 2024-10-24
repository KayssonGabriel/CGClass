package org.gear.framework.core.service.event;

import org.gear.framework.core.service.event.actions.Event;

public interface EventListener {
    void onEvent(Event e);
}
