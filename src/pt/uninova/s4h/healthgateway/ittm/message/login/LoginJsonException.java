package pt.uninova.s4h.healthgateway.ittm.message.login;

/**
 * Class to define a Login Json Exception.
 */
public class LoginJsonException extends Exception {

    /**
     * Constructor of Login Json Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public LoginJsonException(String message) {
        super(message);
    }
}
