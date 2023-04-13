package pt.uninova.s4h.healthgateway.ittm.message.training;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold a SENML event received in the MQTT topics.
 */
public class Score {

    @SerializedName("percentage")
    private float percentage;
    
    @SerializedName("steps")
    private int steps;
    
    @SerializedName("calories")
    private float calories;

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }
    
}
