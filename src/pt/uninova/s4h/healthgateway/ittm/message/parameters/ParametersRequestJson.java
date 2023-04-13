package pt.uninova.s4h.healthgateway.ittm.message.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the Parameters Json file to be sent to the ITTM servers.
 */
public class ParametersRequestJson {

    @SerializedName("machine_ID")
    private String machineId;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
}
