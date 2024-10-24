package org.gear.framework.core.service.event.exception;

public class UnknownMouseActionException extends RuntimeException {
    public UnknownMouseActionException(int action) {
        super("Unknown mouse action: " + action);
    }
}
