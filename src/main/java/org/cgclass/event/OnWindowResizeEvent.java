package org.cgclass.event;

import org.gear.framework.core.service.event.reactive.ReactiveEvent;

public class OnWindowResizeEvent extends ReactiveEvent {

    private final int width;
    private final int height;

    public OnWindowResizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
