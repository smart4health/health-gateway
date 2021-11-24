package pt.uninova.s4h.healthgateway.mqtt;

/**
 * Class to define a MQTT Client Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 07 September 2019 - First version.
 */
public class HgMqttClientException extends Exception {

    /**
     * Constructor of MQTT Client Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public HgMqttClientException(String message) {
        super(message);
    }
}
