package org.gear.framework.core.service.general.provider;

import org.gear.framework.core.service.general.ApplicationService;
import org.gear.framework.core.design_patterns.Singleton;
import org.gear.framework.core.service.general.factory.ApplicationServiceFactory;

public class ApplicationServiceProvider {
    private final ApplicationServiceFactory serviceFactory = new ApplicationServiceFactory();
    private static final Singleton<ApplicationServiceProvider> providerSingleton = new Singleton<>(ApplicationServiceProvider.class);
    public static ApplicationService getService(ServiceType type) {
        return providerSingleton.get().serviceFactory.createService(type);
    }
}
