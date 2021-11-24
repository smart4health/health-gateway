package pt.uninova.s4h.healthgateway.ittm.message.login;

/**
 * Class to define a Login Json Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
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
