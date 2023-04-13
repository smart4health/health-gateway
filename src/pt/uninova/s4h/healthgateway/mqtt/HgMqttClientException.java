package pt.uninova.s4h.healthgateway.mqtt;

/**
 * Class to define a MQTT Client Exception.
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
