package pt.uninova.s4h.healthgateway.ittm.message.training;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the Sensor data Json file to be sent to the ITTM servers.
 */
public class SensorData {

    @SerializedName("timestamp")
    private long timestamp;
    
    @SerializedName("angle")
    private float angle;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

}
