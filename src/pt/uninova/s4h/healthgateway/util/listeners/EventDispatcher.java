package pt.uninova.s4h.healthgateway.util.listeners;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Class to dispatch message events.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class EventDispatcher<T> {

    private final Collection<EventListener<T>> eventListeners;

    public EventDispatcher() {
        eventListeners = new LinkedList<>();
    }

    public void addListener(EventListener<T> l) {
        synchronized (eventListeners) {
            eventListeners.add(l);
        }
    }

    public void clearListeners() {
        synchronized (eventListeners) {
            eventListeners.clear();
        }
    }

    public void dispatch(T e) {
        eventListeners.forEach((i) -> {
            i.onEvent(e);
        });
    }

    public void removeListener(EventListener<T> l) {
        synchronized (eventListeners) {
            eventListeners.remove(l);
        }
    }
}
