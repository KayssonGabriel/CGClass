package org.gear.framework.core.service.event.actions;

public abstract class KeyEvent extends Event {

    private final int keyCode;

    public KeyEvent(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    @Override
    public String toString() {
        return this.name + ": " + (char)keyCode;
    }
}
