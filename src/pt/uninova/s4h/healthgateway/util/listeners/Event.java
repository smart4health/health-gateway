package pt.uninova.s4h.healthgateway.util.listeners;

/**
 * Class to hold message events.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
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
