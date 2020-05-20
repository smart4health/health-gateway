package pt.uninova.s4h.hub.medx.ittm.message.parameters;

/**
 * Class to define a Parameters Json Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
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
