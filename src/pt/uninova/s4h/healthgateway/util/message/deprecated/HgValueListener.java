package pt.uninova.s4h.healthgateway.util.message.deprecated;

import pt.uninova.s4h.healthgateway.mqtt.HgMqttClient;
import pt.uninova.s4h.healthgateway.senml.MedxEvent;
import pt.uninova.s4h.healthgateway.util.listeners.EventListener;

/**
 * Listener to Health Gateway Value Messages.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class HgValueListener implements EventListener<HgValueMessage> {

    private final HgMqttClient hgMqttClient;

    public HgValueListener(HgMqttClient hgMqttClient) {
        this.hgMqttClient = hgMqttClient;
    }

    @Override
    public void onEvent(HgValueMessage e) {
        if (hgMqttClient == null) {
            return;
        }

        MedxEvent medxEvents[] = new MedxEvent[1];
        medxEvents[0] = new MedxEvent(e.getMessageType().name(), e.getValue());
        hgMqttClient.publishMedxEvents(medxEvents);

    }
}
