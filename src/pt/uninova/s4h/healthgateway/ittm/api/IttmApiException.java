package pt.uninova.s4h.healthgateway.ittm.api;

/**
 * Class to define an ITTM API Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
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
