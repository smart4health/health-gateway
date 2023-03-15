package pt.uninova.s4h.healthgateway.ittm.message.training;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the Training Json file to be sent to the ITTM servers.
 */
public class TrainingRequestJson {

    @SerializedName("citizen_id")
    private String citizenId;

    @SerializedName("training_ID")
    private String trainingId;

    @SerializedName("machine_ID")
    private String machineId;

    @SerializedName("start_time")
    private long startTime;

    @SerializedName("stop_time")
    private long stopTime;

    @SerializedName("repetitions")
    private int repetitions;

    @SerializedName("training_weight")
    private int trainingWeight;    

    @SerializedName("training_number")
    private int trainingNumber; 
    
    @SerializedName("training_session")
    private int trainingSession;     
    
    @SerializedName("score")
    private Score score;

    @SerializedName("sensor_data")
    private SensorData[] sensorData;

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

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getTrainingWeight() {
        return trainingWeight;
    }

    public void setTrainingWeight(int trainingWeight) {
        this.trainingWeight = trainingWeight;
    }    
    
    public int getTrainingNumber() {
        return trainingNumber;
    }

    public void setTrainingNumber(int trainingNumber) {
        this.trainingNumber = trainingNumber;
    }      
    
    public int getTrainingSession() {
        return trainingSession;
    }

    public void setTrainingSession(int trainingSession) {
        this.trainingSession = trainingSession;
    }      
    
    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public SensorData[] getSensorData() {
        return sensorData;
    }

    public void setSensorData(SensorData[] sensorData) {
        this.sensorData = sensorData;
    }
}
