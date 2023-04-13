package pt.uninova.s4h.healthgateway.senml;

/**
 * Class to define a MedX Json Exception.
 */
public class MedxJsonException extends Exception {

    /**
     * Constructor of MedX Json Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public MedxJsonException(String message) {
        super(message);
    }
}