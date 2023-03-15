package pt.uninova.s4h.healthgateway.util.listeners;

/**
 * Class to listen to message events.
 */
public interface EventListener<T> {

    void onEvent(T e);
}
