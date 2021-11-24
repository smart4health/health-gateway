package pt.uninova.s4h.healthgateway.senml;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold a SENML event received in the MQTT topics.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 07 September 2019 - First version.
 */
public class MedxEvent {

    @SerializedName("v")
    private float value;
    @SerializedName("sv")
    private String stringValue;
    @SerializedName("u")
    private String units;
    @SerializedName("t")
    private long time;
    @SerializedName("n")
    private String name;

    public MedxEvent() {
    }

    public MedxEvent(String name) {
        this.name = name;
    }

    public MedxEvent(String name, float value) {
        this.name = name;
        this.value = value;
    }

    public MedxEvent(String name, String stringValue) {
        this.name = name;
        this.stringValue = stringValue;
    }
    
    public MedxEvent(String name, String stringValue, float value) {
        this.name = name;
        this.value = value;
        this.stringValue = stringValue;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float newValue) {
        this.value = newValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String newStringValue) {
        this.stringValue = newStringValue;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String newUnits) {
        this.units = newUnits;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long newTime) {
        this.time = newTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }
}
