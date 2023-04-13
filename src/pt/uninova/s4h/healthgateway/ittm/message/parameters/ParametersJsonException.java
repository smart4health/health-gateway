package pt.uninova.s4h.healthgateway.ittm.message.parameters;

/**
 * Class to define a Parameters Json Exception.
 */
public class ParametersJsonException extends Exception {

    /**
     * Constructor of Parameters Json Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public ParametersJsonException(String message) {
        super(message);
    }
}
