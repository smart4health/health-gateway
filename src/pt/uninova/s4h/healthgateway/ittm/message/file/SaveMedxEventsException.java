package pt.uninova.s4h.healthgateway.ittm.message.file;

/**
 * Class to define a Save MedX Events Exception.
 */
public class SaveMedxEventsException extends Exception {

    /**
     * Constructor of Save MedX Events Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public SaveMedxEventsException(String message) {
        super(message);
    }
}
