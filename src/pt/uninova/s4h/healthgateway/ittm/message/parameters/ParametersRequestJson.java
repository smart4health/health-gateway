package pt.uninova.s4h.healthgateway.ittm.message.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the Parameters Json file to be sent to the ITTM servers.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
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
