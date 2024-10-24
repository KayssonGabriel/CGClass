package org.gear.framework.core.service.event;

import java.lang.FunctionalInterface;

@FunctionalInterface
public interface EventCallbackFn {
    boolean call();
}
