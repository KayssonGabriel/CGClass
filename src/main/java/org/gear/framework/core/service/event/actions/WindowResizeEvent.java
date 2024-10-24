package org.gear.framework.core.service.event.actions;

public class WindowResizeEvent extends Event {

    public int width;
    public int height;

    public WindowResizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return this.name + ": size{" + this.width + ", " + this.height + "}";
    }
}
