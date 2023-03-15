package pt.uninova.s4h.healthgateway.ittm.message.training;

/**
 * Class to define a Training Json Exception.
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