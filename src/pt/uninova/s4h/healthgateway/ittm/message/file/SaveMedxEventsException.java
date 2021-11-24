package pt.uninova.s4h.healthgateway.ittm.message.file;

/**
 * Class to define a Save MedX Events Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 21 November 2019 - First version.
 */
public class SaveMedxEventsException extends Exception {

    /**
     * Constructor of Save MedX Events Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public SaveMedxEventsException(String message) {
        super(message);
    }
}
