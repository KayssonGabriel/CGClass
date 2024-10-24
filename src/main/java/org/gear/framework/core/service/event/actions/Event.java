package org.gear.framework.core.service.event.actions;

public abstract class Event {

    protected final String name;
    private boolean isHandled = false;

    public Event() {
        this.name = getClass().getSimpleName();
    }

    @Override
    public abstract String toString();

    public void setHandled(boolean handled) {
        isHandled = handled;
    }

    public boolean isHandled() {
        return isHandled;
    }
}
