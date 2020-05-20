package pt.uninova.s4h.hub.medx.senml;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the SenML Json file received in the MQTT topics.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 07 September 2019 - First version.
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
