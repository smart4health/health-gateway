package pt.uninova.s4h.healthgateway.ittm.manager;

/**
 * Class to define an ITTM Training Manager Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 05 June 2020 - First version.
 */
public class IttmTrainingManagerException extends Exception {

    /**
     * Constructor of ITTM Training Manager Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public IttmTrainingManagerException(String message) {
        super(message);
    }
}
