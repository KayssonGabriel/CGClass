package org.gear.framework.application;

import org.gear.framework.core.design_patterns.Singleton;
import org.gear.framework.core.service.event.EventAPI;
import org.gear.framework.core.service.event.EventFlag;
import org.gear.framework.core.service.event.reactive.ReactiveEvent;

public abstract class Application {

    private static boolean running = true;
    private static Singleton<Application> instance;
    private static final EventAPI eventAPI = new EventAPI();

    public static void launch(Application application) {
        instance = new Singleton<>(application);

        eventAPI.init();
        eventAPI.registerApplicationCallbackFlag(EventFlag.APPLICATION_SHUTDOWN, instance.get()::isShutdown);
        eventAPI.subscribe(instance.get());

        instance.get().start();

        while (!eventAPI.getFlag(EventFlag.APPLICATION_SHUTDOWN)) {
            eventAPI.update();
            instance.get().update();
        }

        instance.get().shutdown();
    }

    public abstract void start();

    public abstract void update();

    public final void shutdown() {
        running = false;
    }

    public final boolean isShutdown() {
        return !running;
    }

    public static void dispatchEvent(ReactiveEvent event) {
        eventAPI.dispatchEvent(event);
    }
}
