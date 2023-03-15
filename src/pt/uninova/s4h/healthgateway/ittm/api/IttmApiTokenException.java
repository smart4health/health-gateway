package pt.uninova.s4h.healthgateway.ittm.api;

/**
 * Class to define an ITTM API Token Exception.
 */
public class IttmApiTokenException extends Exception {

    /**
     * Constructor of ITTM API Token Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public IttmApiTokenException() {
        super("Warning: Invalid Token");
    }
}
