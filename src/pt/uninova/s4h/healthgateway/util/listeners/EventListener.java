package pt.uninova.s4h.healthgateway.util.listeners;

/**
 * Class to listen to message events.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public interface EventListener<T> {

    void onEvent(T e);
}
