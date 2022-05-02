package pt.uninova.s4h.healthgateway.box.api;

import pt.uninova.s4h.healthgateway.ittm.api.*;

/**
 * Class to define an ITTM API Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class BoxApiException extends Exception {

    /**
     * Constructor of ITTM API Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public BoxApiException(String message) {
        super(message);
    }
}
