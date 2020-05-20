package pt.uninova.s4h.hub.medx.ittm.message.training;

/**
 * Class to define a Training Json Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class TrainingJsonException extends Exception {

    /**
     * Constructor of Training Json Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public TrainingJsonException(String message) {
        super(message);
    }
}