package org.gear.framework.core.service.event.actions;

public class WindowFocusEvent extends Event {

    private final boolean isFocused;

    public WindowFocusEvent(boolean isFocused) {
        this.isFocused = isFocused;
    }


    @Override
    public String toString() {
        return this.name + ": " + (isFocused ? "focused" : "unfocused");
    }
}
