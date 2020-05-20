package pt.uninova.s4h.hub.medx.mqtt;

/**
 * Class to define a MQTT Client Exception.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 07 September 2019 - First version.
 */
public class MedxMqttClientException extends Exception {

    /**
     * Constructor of MQTT Client Exception.
     *
     * @param message The message with the Exception's cause.
     */
    public MedxMqttClientException(String message) {
        super(message);
    }
}
