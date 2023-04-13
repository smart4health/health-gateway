package pt.uninova.s4h.healthgateway.ittm.message.force;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the Sensor data Json file to be sent to the ITTM servers.
 */
public class ForceTestData {

    @SerializedName("angle")
    private float angle;
    
    @SerializedName("force")
    private float force;

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getForce() {
        return force;
    }

    public void setForce(float force) {
        this.force = force;
    }
    
}
