package pt.uninova.s4h.healthgateway.util.message.deprecated;

import pt.uninova.s4h.healthgateway.util.message.MessagesUtil;

/**
 * Class to hold HealthGateway event message.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class HgEventMessage {

    private MessagesUtil.EventMessageType messageType;
    
    public HgEventMessage(MessagesUtil.EventMessageType messageType) {
        this.messageType = messageType;
    }

    public MessagesUtil.EventMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessagesUtil.EventMessageType messageType) {
        this.messageType = messageType;
    }
}
