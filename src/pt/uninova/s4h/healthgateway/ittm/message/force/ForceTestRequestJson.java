package pt.uninova.s4h.healthgateway.ittm.message.force;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the Force Json file to be sent to the ITTM servers.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class ForceTestRequestJson {

    @SerializedName("citizen_id")
    private String citizenId;
    
    @SerializedName("forcetest_ID")
    private String forceTestId;
    
    @SerializedName("machine_ID")
    private String machineId;
    
    @SerializedName("test_time")
    private long testTime;
    
    @SerializedName("recommended_trainingweight")
    private float recomendedWeight;
    
    @SerializedName("force_test")
    private ForceTestData[] forceTestData;

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getForceTestId() {
        return forceTestId;
    }

    public void setForceTestId(String forceTestId) {
        this.forceTestId = forceTestId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public long getTestTime() {
        return testTime;
    }

    public void setTestTime(long testTime) {
        this.testTime = testTime;
    }

    public float getRecomendedWeight() {
        return recomendedWeight;
    }

    public void setRecomendedWeight(float recomendedWeight) {
        this.recomendedWeight = recomendedWeight;
    }

    public ForceTestData[] getForceTestData() {
        return forceTestData;
    }

    public void setForceTestData(ForceTestData[] forceTestData) {
        this.forceTestData = forceTestData;
    }
    
}
