package pt.uninova.s4h.healthgateway.ittm.message.force;

/**
 * Class to define a Training Json Exception.
 */
public class ForceTestJsonException extends Exception {

    /**
     * Constructor of Training Json Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public ForceTestJsonException(String message) {
        super(message);
    }
}