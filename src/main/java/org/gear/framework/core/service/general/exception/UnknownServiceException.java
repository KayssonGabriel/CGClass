package org.gear.framework.core.service.general.exception;

import org.gear.framework.core.service.general.ApplicationService;
import org.gear.framework.core.service.general.provider.ServiceType;

public class UnknownServiceException extends RuntimeException {

    public UnknownServiceException(ServiceType type) {
        super("Unknown provided service: " + type.name());
    }

    public UnknownServiceException(Class<? extends ApplicationService> service) {
        super("Unknown provided service: " + service.getSimpleName());
    }
}
