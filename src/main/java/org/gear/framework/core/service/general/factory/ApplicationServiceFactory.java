package org.gear.framework.core.service.general.factory;


import org.gear.framework.core.service.general.ApplicationService;
import org.gear.framework.core.service.event.EventAPI;
import org.gear.framework.core.service.general.provider.ServiceType;
import org.gear.framework.core.service.general.exception.UnknownServiceException;

public class ApplicationServiceFactory {

    public ApplicationServiceFactory() {
    }

    public ApplicationService createService(ServiceType type) {
        return switch (type) {
            case EVENT_API -> new EventAPI();
            default -> throw new UnknownServiceException(type);
        };
    }
}
