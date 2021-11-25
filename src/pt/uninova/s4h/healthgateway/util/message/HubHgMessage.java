package pt.uninova.s4h.healthgateway.util.message;

/**
 * Class to hold Hub to HealthGateway messages.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 27 May 2020 - First version.
 */
public class HubHgMessage {

    private MessagesUtil.EventMessageType messageType;
    private String stringValue;
    private Float value;

    public HubHgMessage(MessagesUtil.EventMessageType messageType, String stringValue, Float value) {
        this.messageType = messageType;
        this.stringValue = stringValue;
        this.value = value;
    }

    public MessagesUtil.EventMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessagesUtil.EventMessageType messageType) {
        this.messageType = messageType;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
