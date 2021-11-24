package pt.uninova.s4h.healthgateway.util.message.deprecated;

import pt.uninova.s4h.healthgateway.gui.FXMLDocumentController;
import pt.uninova.s4h.healthgateway.util.listeners.EventListener;

/**
 * Listener to Medx Connection Messages.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class MedxConnectionListener implements EventListener<MedxConnectionMessage> {

    private final FXMLDocumentController controller;

    public MedxConnectionListener(FXMLDocumentController controller) {
        this.controller = controller;
    }

    @Override
    public void onEvent(MedxConnectionMessage e) {
        if (controller == null) {
            return;
        }
        
        controller.receiveAutoConnect(e.getConnection(), e.getCommPort());
    }
}
