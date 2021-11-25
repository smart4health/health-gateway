package pt.uninova.s4h.healthgateway.util.message.deprecated;

import pt.uninova.s4h.healthgateway.mqtt.HgMqttClient;
import pt.uninova.s4h.healthgateway.senml.MedxEvent;
import pt.uninova.s4h.healthgateway.util.listeners.EventListener;

/**
 * Listener to HealthGateway Event Messages.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class HgEventListener implements EventListener<HgEventMessage> {

    private final HgMqttClient hgMqttClient;

    public HgEventListener(HgMqttClient hgMqttClient) {
        this.hgMqttClient = hgMqttClient;
    }

    @Override
    public void onEvent(HgEventMessage e) {
        if (hgMqttClient == null) {
            return;
        }

        MedxEvent medxEvents[] = new MedxEvent[1];
        medxEvents[0] = new MedxEvent(e.getMessageType().name());
        hgMqttClient.publishMedxEvents(medxEvents);
    }
}
