package pt.uninova.s4h.healthgateway.box.message;

import pt.uninova.s4h.healthgateway.ittm.message.training.*;

/**
 * Class to define a Training Json Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class BoxTrainingJsonException extends Exception {

    /**
     * Constructor of Training Json Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public BoxTrainingJsonException(String message) {
        super(message);
    }
}