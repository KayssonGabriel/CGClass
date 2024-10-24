package org.gear.framework.core.service.event.reactive;

public abstract class ReactiveEvent {

    private boolean isHandled = false;

    public ReactiveEvent() {

    }

    public void setHandled(boolean handled) {
        isHandled = handled;
    }

    public boolean isHandled() {
        return isHandled;
    }
}
