package pt.uninova.s4h.healthgateway.util.message.deprecated;

import pt.uninova.s4h.healthgateway.mqtt.HgMqttClient;
import pt.uninova.s4h.healthgateway.senml.MedxEvent;
import pt.uninova.s4h.healthgateway.util.listeners.EventListener;
import pt.uninova.s4h.healthgateway.util.message.MessagesUtil.EventMessageType;

/**
 * Listener to Medx ConnectionMessages.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class HgConnectionListener implements EventListener<HgConnectionMessage> {

    private final HgMqttClient hgMqttClient;

    public HgConnectionListener(HgMqttClient hgMqttClient) {
        this.hgMqttClient = hgMqttClient;
    }

    @Override
    public void onEvent(HgConnectionMessage e) {
        if (hgMqttClient == null) {
            return;
        }

        MedxEvent medxEvents[] = new MedxEvent[1];
        medxEvents[0] = new MedxEvent(EventMessageType.AUTO_CONNECTION_REQUEST.name(), e.getCommPort());
        hgMqttClient.publishMedxEvents(medxEvents);
    }
}
