package pt.uninova.s4h.healthgateway.ittm.api;

/**
 * Class to define an ITTM API Exception.
 */
public class IttmApiException extends Exception {

    /**
     * Constructor of ITTM API Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public IttmApiException(String message) {
        super(message);
    }
}
