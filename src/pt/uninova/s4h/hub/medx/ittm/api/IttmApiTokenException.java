package pt.uninova.s4h.hub.medx.ittm.api;

/**
 * Class to define an ITTM API Token Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 04 March 2020 - First version.
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
