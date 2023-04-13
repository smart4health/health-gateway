package pt.uninova.s4h.healthgateway.box.message;

import com.google.gson.annotations.SerializedName;

/**
 * Class to construct Training Json file to send to B-Health IoT Box.
 */

public class BoxTrainingRequestJson {

    @SerializedName("timestamp")
    private long timestamp;

    @SerializedName("length")
    private long length;    

    @SerializedName("score")
    private float score;

    @SerializedName("repetitions")
    private int repetitions;    
    
    @SerializedName("weight")
    private int weight;    

    @SerializedName("calories")
    private float calories;     
    
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
    
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public float getCalories() {
        return calories;
}

    public void setCalories(float calories) {
        this.calories = calories;
    }    
}
