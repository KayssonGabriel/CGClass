package org.gear.framework.core.service.event.reactive;

import org.gear.framework.core.log.Logger;
import org.gear.framework.core.log.annotation.GenerateCriticalFile;
import org.gear.framework.core.log.annotation.LogAlias;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * This class is responsible for organizing events that propagate reactions in objects.
 * In short, it allows you to register any instance of any class to listen to reactive
 * events, and, when this is done, it will search for methods annotated with @Reactive
 * in its structure. According to the signatures of the reactive methods of the class
 * corresponding to the instance that was registered, a corresponding event queue is
 * created. When dispatching events, the listeners corresponding to that type of
 * reactive event will have their correct methods called and the event that occurred
 * will be passed as a parameter. When the EventAPI is updated, all event queues are
 * dispatched at once. Furthermore, it is also possible to unsubscribe an instance,
 * and by doing so it will no longer be a valid listener and its reactive methods
 * will not stop.
 */
@LogAlias("EventAPI")
@GenerateCriticalFile
public class QueueDispatcher extends Logger {

    /**
     * Mapping of event classes to reactive methods and their corresponding listeners.
     */
    private final Map<Class<? extends ReactiveEvent>, Map<Method, List<Object>>> classMapMap;

    /**
     * Mapping of event classes to their associated event queues.
     */
    private final Map<Class<? extends ReactiveEvent>, EventQueue> eventQueueMap;

    /**
     * Default constructor for the QueueDispatcher class.
     * Initializes necessary data structures.
     */
    public QueueDispatcher() {
        this.classMapMap = new HashMap<>();
        this.eventQueueMap = new HashMap<>();
    }

    /**
     * Subscribes a listener to receive events.
     *
     * @param listener The listener to be subscribed.
     */
    @SuppressWarnings("unchecked")
    public synchronized void subscribe(Object listener) {
        List<Method> klassMethods = getReactiveMethods(listener);

        for (Method method : klassMethods) {
            Class<?>[] parameters = method.getParameterTypes();

            if (ReactiveEvent.class.isAssignableFrom(parameters[0])) {
                Map<Method, List<Object>> batch = classMapMap.get(parameters[0]);

                if (batch == null) {
                    batch = new HashMap<>();
                    classMapMap.put((Class<? extends ReactiveEvent>) parameters[0], batch);
                }

                if (!batch.containsKey(method)) {
                    batch.put(method, new ArrayList<>());
                    batch.get(method).add(listener);
                }
            }
        }
    }

    /**
     * Unsubscribes a listener from receiving events.
     *
     * @param listener The listener to be unsubscribed.
     */
    public synchronized void unsubscribe(Object listener) {
        Class<?> klass = listener.getClass();
        Method[] klassMethods = klass.getDeclaredMethods();

        for (Method method : klassMethods) {
            removeListener(method, klass, listener);
        }
    }

    /**
     * Dispatches an event by putting it into the appropriate queue.
     *
     * @param event The event to be dispatched.
     */
    public synchronized void dispatchEvent(ReactiveEvent event) {
        Class<? extends ReactiveEvent> eventClass = event.getClass();

        if (!eventQueueMap.containsKey(event.getClass())) {
            eventQueueMap.put(eventClass, new EventQueue());
        }

        eventQueueMap.get(eventClass).pushEvent(event);
    }

    /**
     * Dispatches all event queues, notifying corresponding listeners.
     */
    public synchronized void dispatchQueues() {
        for (Class<? extends ReactiveEvent> klass : classMapMap.keySet()) {
            EventQueue eventQueue = eventQueueMap.get(klass);

            if (eventQueue == null || eventQueue.isEmpty()) {
                continue;
            }

            notifyListeners(eventQueue, klass);
        }
    }

    /**
     * Removes a method and its associated listener from the data structures.
     *
     * @param method The method to be removed.
     * @param klass  The class to which the method belongs.
     */
    private synchronized void removeListener(Method method, Class<?> klass, Object instance) {
        if (method.isAnnotationPresent(Reactive.class)) {
            if (checkForValidMethod(method, klass)) {
                Class<?>[] parameters = method.getParameterTypes();

                if (ReactiveEvent.class.isAssignableFrom(parameters[0])) {
                    Map<Method, List<Object>> batch = classMapMap.get(parameters[0]);

                    if (batch != null) {
                        batch.get(method).remove(instance);
                        if(batch.get(method).isEmpty()) batch.remove(method);
                    }
                }
            }
        }
    }

    /**
     * Checks if a method marked as reactive is valid.
     *
     * @param method The method to be checked.
     * @param klass  The class to which the method belongs.
     * @return true if the method is valid, false otherwise.
     */
    private synchronized boolean checkForValidMethod(Method method, Class<?> klass) {
        if (!Modifier.isPublic(method.getModifiers())) {
            critical("Method '" + method.getName() + "' in class '" + klass.getSimpleName() + "' must be PUBLIC.");
        }

        if (method.getParameterCount() != 1) {
            critical("Reactive methods must have one parameter.", new RuntimeException("Invalid method declaration."));
        }

        return true;
    }

    /**
     * Retrieves all reactive methods from a listener.
     *
     * @param listener The listener from which reactive methods will be retrieved.
     * @return A list of reactive methods.
     */
    private synchronized List<Method> getReactiveMethods(Object listener) {
        Class<?> klass = listener.getClass();
        Method[] methods = klass.getDeclaredMethods();
        List<Method> result = new ArrayList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Reactive.class)) {
                if (checkForValidMethod(method, klass)) {
                    result.add(method);
                }
            }
        }

        return result;
    }

    /**
     * Notifies registered listeners for a specific type of event.
     *
     * @param queue The event queue associated with the event type.
     * @param klass The class of the event type.
     */
    private synchronized void notifyListeners(EventQueue queue, Class<? extends ReactiveEvent> klass) {
        while (!queue.isEmpty()) {
            ReactiveEvent event = queue.popEvent();

            for (Method method : classMapMap.get(klass).keySet()) {
                Map<Method, List<Object>> batch = classMapMap.get(klass);
                List<Object> instances = batch.get(method);

                if (instances.isEmpty()) {
                    continue;
                }

                if (event.isHandled()) {
                    break;
                }

                for (Object instance : instances) {
                    invokeMethod(method, instance, event);
                }
            }
        }
    }

    /**
     * Invokes a reactive method on a listener.
     *
     * @param method   The method to be invoked.
     * @param instance The listener instance.
     * @param param    The method parameter.
     */
    private synchronized void invokeMethod(Method method, Object instance, ReactiveEvent param) {
        try {
            if (!Modifier.isPublic(method.getModifiers())) {
                critical("Method '" + method.getName() + "' in class '" + instance.getClass().getSimpleName() + "' must be PUBLIC.");
            } else {
                method.invoke(instance, param);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            critical("Cannot invoke method '" + method.getName() + "'", new RuntimeException(e));
        }
    }
}
