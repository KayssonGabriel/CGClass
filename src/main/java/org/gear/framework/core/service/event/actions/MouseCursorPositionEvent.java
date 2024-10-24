package org.gear.framework.core.service.event.actions;

public class MouseCursorPositionEvent extends Event {

    private final float x;
    private final float y;

    public MouseCursorPositionEvent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return this.name + ": pos{" + this.x + ", " + this.y + "}";
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}
