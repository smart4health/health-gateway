package pt.uninova.s4h.healthgateway.util.message.deprecated;

import pt.uninova.s4h.healthgateway.util.message.MessagesUtil;

/**
 * Class to hold HealthGateway value message.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class HgValueMessage {

    private MessagesUtil.EventMessageType messageType;
    private short value;

    public HgValueMessage(MessagesUtil.EventMessageType messageType, short value) {
        this.messageType = messageType;
        this.value = value;
    }

    public MessagesUtil.EventMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessagesUtil.EventMessageType messageType) {
        this.messageType = messageType;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }
}
