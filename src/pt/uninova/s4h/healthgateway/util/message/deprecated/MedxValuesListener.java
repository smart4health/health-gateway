package pt.uninova.s4h.healthgateway.util.message.deprecated;

import pt.uninova.s4h.healthgateway.gui.FXMLDocumentController;
import pt.uninova.s4h.healthgateway.util.listeners.EventListener;

/**
 * Listener to MedxValuesMessages.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class MedxValuesListener implements EventListener<MedxValuesMessage> {

    private final FXMLDocumentController controller;

    public MedxValuesListener(FXMLDocumentController controller) {
        this.controller = controller;
    }

    @Override
    public void onEvent(MedxValuesMessage e) {

        if (controller == null) {
            return;
        }
//        controller.receiveValues(0, 0, 0, 0, 0, 0, 0, 0);
//
//        MedxEvent medxEvents[] = new MedxEvent[7];
//        medxEvents[0] = new MedxEvent(EventMessageType.FORCE.name(), e.getForce());
//        medxEvents[1] = new MedxEvent(EventMessageType.ANGLE.name(), e.getAngle());
//        medxEvents[2] = new MedxEvent(EventMessageType.HEAD.name(), e.getHead());
//        medxEvents[3] = new MedxEvent(EventMessageType.HANDS.name(), e.getHands());
//        medxEvents[4] = new MedxEvent(EventMessageType.BACK.name(), e.getBack());
//        medxEvents[5] = new MedxEvent(EventMessageType.LEGS.name(), e.getLegs());
//        medxEvents[6] = new MedxEvent(EventMessageType.FOOT.name(), e.getFoot());
//        hgMqttClient.hubPublishMedxEvents(medxEvents);
    }
}
