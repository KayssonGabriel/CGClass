package org.gear.framework.core.service.event.actions;

public abstract class MouseButtonEvent extends Event {

    int button;

    public MouseButtonEvent(int button) {
        this.button = button;
    }

    @Override
    public String toString() {
        return this.name + ": " + button;
    }
}
