package pt.uninova.s4h.healthgateway.senml;

/**
 * Class to define a MedX Json Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 07 September 2019 - First version.
 */
public class MedxJsonException extends Exception {

    /**
     * Constructor of MedX Json Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public MedxJsonException(String message) {
        super(message);
    }
}