package pt.uninova.s4h.hub.medx.ittm.message.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold a ParametersValues Response received in the MQTT topics.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class ParametersValues {

    @SerializedName("citizen_id")
    private String citizenId;

    @SerializedName("training_ID")
    private String trainingId;

    @SerializedName("session_number")
    private int sessionNumber;

    @SerializedName("training_number")
    private int trainingNumber;

    @SerializedName("LE_counterweight")
    private float leCounterWeight;

    @SerializedName("LE_trainingweight")
    private float leTrainingWeight;

    @SerializedName("LE_ROM_extension")
    private int leRomExtension;

    @SerializedName("LE_ROM_flexion")
    private int leRomFlexion;

    @SerializedName("LE_zeroposition")
    private int leZeroPosition;

    @SerializedName("LE_tightposition")
    private int leTightPosition;

    @SerializedName("LE_seatcushion")
    private String leSeatCushion;

    @SerializedName("create_date")
    private String createDate;

    @SerializedName("measurement_date")
    private String measurementDate;

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public int getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(int sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public int getTrainingNumber() {
        return trainingNumber;
    }

    public void setTrainingNumber(int trainingNumber) {
        this.trainingNumber = trainingNumber;
    }

    public float getLeCounterWeight() {
        return leCounterWeight;
    }

    public void setLeCounterWeight(float leCounterWeight) {
        this.leCounterWeight = leCounterWeight;
    }

    public float getLeTrainingWeight() {
        return leTrainingWeight;
    }

    public void setLeTrainingWeight(float leTrainingWeight) {
        this.leTrainingWeight = leTrainingWeight;
    }

    public int getLeRomExtension() {
        return leRomExtension;
    }

    public void setLeRomExtension(int leRomExtension) {
        this.leRomExtension = leRomExtension;
    }

    public int getLeRomFlexion() {
        return leRomFlexion;
    }

    public void setLeRomFlexion(int leRomFlexion) {
        this.leRomFlexion = leRomFlexion;
    }

    public int getLeZeroPosition() {
        return leZeroPosition;
    }

    public void setLeZeroPosition(int leZeroPosition) {
        this.leZeroPosition = leZeroPosition;
    }

    public int getLeTightPosition() {
        return leTightPosition;
    }

    public void setLeTightPosition(int leTightPosition) {
        this.leTightPosition = leTightPosition;
    }

    public String getLeSeatCushion() {
        return leSeatCushion;
    }

    public void setLeSeatCushion(String leSeatCushion) {
        this.leSeatCushion = leSeatCushion;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

}
