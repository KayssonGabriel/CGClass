package org.gear.framework.core.service.general.exception;

import org.gear.framework.core.service.general.ApplicationService;

public class ServiceInitializationException extends RuntimeException {
    public <T extends ApplicationService> ServiceInitializationException(Class<T> klass) {
        super("Failed to initialize the " + klass.getSimpleName() + " service.");
    }
}
