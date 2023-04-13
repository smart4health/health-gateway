package pt.uninova.s4h.healthgateway.util.listeners;

/**
 * Class to hold message events.
 */
public class Event<T> {

    private final T message;

    public Event(T m) {
        message = m;
    }

    public T getMessage() {
        return message;
    }

}
