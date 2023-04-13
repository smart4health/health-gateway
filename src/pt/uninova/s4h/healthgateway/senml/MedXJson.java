package pt.uninova.s4h.healthgateway.senml;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the SenML Json file received in the MQTT topics.
 */
public class MedXJson {

    @SerializedName("e")
    public MedxEvent[] medxEvent;

    public MedxEvent[] getMedXEvent() {
        return medxEvent;
    }

    public void setMedXEvent(MedxEvent[] event) {
        this.medxEvent = event;
    }
}
