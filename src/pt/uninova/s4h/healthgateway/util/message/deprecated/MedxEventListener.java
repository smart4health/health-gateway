package pt.uninova.s4h.healthgateway.util.message.deprecated;

import pt.uninova.s4h.healthgateway.gui.FXMLDocumentController;
import pt.uninova.s4h.healthgateway.util.listeners.EventListener;

/**
 * Listener to MedxEventMessages.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class MedxEventListener implements EventListener<MedxEventMessage> {

    private final FXMLDocumentController controller;

    public MedxEventListener(FXMLDocumentController controller) {
        this.controller = controller;
    }

    @Override
    public void onEvent(MedxEventMessage e) {
        if (controller == null) {
            return;
        }
        switch (e.getMessageType()) {
            case FLEXION_ANGLE_CONF:
                controller.receiveFlexionAngleConf();
                break;
            case EXTENSION_ANGLE_CONF:
                controller.receiveExtensionAngleConf();
                break;
            case ZERO_ANGLE_CONF:
                controller.receiveIntAngleConf();
                break;
            case PRESSURE_CONF:
                controller.receivePressureConf();
                break;
            case START_CONF:
                controller.receiveStartConf();
                break;
            case STOP_CONF:
                controller.receiveStopConf();
                break;                
            case BUTTON:
                controller.receiveButton();
                break;
        }
    }
}
