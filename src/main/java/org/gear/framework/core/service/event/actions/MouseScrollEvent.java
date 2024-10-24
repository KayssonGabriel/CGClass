package org.gear.framework.core.service.event.actions;

public class MouseScrollEvent extends Event {

    float xOffset;
    float yOffset;

    public MouseScrollEvent(float xOffset, float yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public String toString() {
        return this.name + "offset{" + this.xOffset + ", " + this.yOffset + "}";
    }
}
