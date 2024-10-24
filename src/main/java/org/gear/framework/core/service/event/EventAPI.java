package org.gear.framework.core.service.event;

import org.gear.framework.core.design_patterns.injection.DependencyInjector;
import org.gear.framework.core.design_patterns.injection.DependencyManager;
import org.gear.framework.core.service.event.reactive.QueueDispatcher;
import org.gear.framework.core.service.event.reactive.ReactiveEvent;
import org.gear.framework.core.service.general.ApplicationService;
import org.gear.framework.core.log.LogLevel;
import org.gear.framework.core.log.Logger;
import org.gear.framework.core.log.annotation.LogInfo;
import org.gear.framework.core.log.annotation.NotDebugLog;

import java.util.HashMap;
import java.util.Map;

@NotDebugLog
@LogInfo(level = LogLevel.INFO)
public class EventAPI extends Logger implements ApplicationService {

    Map<EventFlag, EventCallbackFn> flags;
    QueueDispatcher queueDispatcher;
    DependencyInjector dependencyInjector;

    @Override
    public boolean init() {
        this.flags = new HashMap<>();
        this.queueDispatcher = new QueueDispatcher();
        this.dependencyInjector = new DependencyInjector();
        return true;
    }

    public void resolveDependencies(Object target) {
        dependencyInjector.resolveDependencies(target);
    }

    public void injectDependencies(Object dependency) {
        DependencyManager.injectDependency(dependency);
    }

    public boolean getFlag(EventFlag flag) {
        return flags.get(flag).call();
    }

    public void registerApplicationCallbackFlag(EventFlag flag, EventCallbackFn function) {
        this.flags.put(flag, function);
    }

    public void dispatchEvent(ReactiveEvent e) {
        queueDispatcher.dispatchEvent(e);
    }

    public void subscribe(Object object) {
        queueDispatcher.subscribe(object);
    }

    public void unsubscribe(Object object) {
        queueDispatcher.unsubscribe(object);
    }

    @Override
    public void update() {
        queueDispatcher.dispatchQueues();
    }

    @Override
    public void shutdown() {
        
    }
}
