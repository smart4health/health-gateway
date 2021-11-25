package pt.uninova.s4h.healthgateway.util.listeners;

import pt.uninova.s4h.healthgateway.gui.FXMLDocumentController;
import pt.uninova.s4h.healthgateway.ittm.api.IttmApiTokenException;
import pt.uninova.s4h.healthgateway.ittm.manager.IttmTrainingManager;
import pt.uninova.s4h.healthgateway.util.message.HubHgMessage;
import pt.uninova.s4h.healthgateway.util.message.MessagesUtil;

/**
 * Listener to Hub to Health Gateway Messages.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class HubHgMessageListener implements EventListener<HubHgMessage> {

    private final FXMLDocumentController controller;
    private final IttmTrainingManager ittmTrainingManager;

    public HubHgMessageListener(FXMLDocumentController controller, IttmTrainingManager ittmTrainingManager) {
        this.controller = controller;
        this.ittmTrainingManager = ittmTrainingManager;
    }

    @Override
    public void onEvent(HubHgMessage e) {
        if (controller == null) {
            return;
        }

        switch (e.getMessageType()) {
            case CONNECTION_RESPONSE:
                controller.receiveConnectionResponse(Boolean.parseBoolean(e.getStringValue()));
                break;
            case KBZ_MESSAGE:
                controller.addKbzMessage(e.getStringValue());
                ittmTrainingManager.newKbzMessage(e.getStringValue());
                break;
            case KBZ_IMAGE:
                controller.addKbzImage(e.getStringValue());
                break;
            case COMMUNICATION_ERROR:
                controller.communicationError();
                break;
            case AUTO_CONNECTION_RESPONSE:
                if (e.getValue() == 1) {
                    controller.receiveAutoConnect(true, null);
                } else {
                    controller.receiveAutoConnect(false, null);
                }
                break;
            case FORCE:
                controller.receiveForceValue(e.getValue());
                break;
            case ANGLE:
                controller.receiveAngleValue(e.getValue());
                ittmTrainingManager.newAngle(e.getValue());
                break;
            case HEADL:
                controller.receiveHeadValueL(e.getValue());
                break;
            case HEADR:
                controller.receiveHeadValueR(e.getValue());
                break;                
            case HANDSL:
                controller.receiveHandsValueL(e.getValue());
                break;
            case HANDSR:
                controller.receiveHandsValueR(e.getValue());
                break;                
            case BACK:
                controller.receiveBackValue(e.getValue());
                break;
            case LEGSL:
                controller.receiveLegsValueL(e.getValue());
                break;
            case LEGSR:
                controller.receiveLegsValueR(e.getValue());
                break;                
            case FOOTL:
                controller.receiveFootValueL(e.getValue());
                break;
            case FOOTR:
                controller.receiveFootValueR(e.getValue());
                break;                
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
                //replace by separate events?
            case USER_INFORMATION_RESPONSE:
                controller.updateUser();
                break;
                //replace by separate events?
            case USER_ERROR:
                break;
            case TRAINING_API_ERROR:
                controller.showPopupIttmError(e.getStringValue());
                break;
            case TRAINING_TOKEN_ERROR:
                controller.healthMonitorConnected(false);
                break;
            case TRAINING_500_ERROR:
                controller.showPopupIttmError(e.getStringValue());
                controller.healthMonitorConnected(false);
                break;                
//            case COMM_PORTS_RESPONSE:
//                controller.receiveCommPorts(e.getStringValue());
//                break;
            case CHECK_PORT_RESPONSE:
                controller.receiveCheckPort(Boolean.parseBoolean(e.getStringValue()));
            default:
                break;
        }
    }
}
