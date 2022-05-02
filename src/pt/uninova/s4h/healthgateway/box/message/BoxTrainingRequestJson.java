package pt.uninova.s4h.healthgateway.box.message;

import pt.uninova.s4h.healthgateway.ittm.message.training.*;
import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the Training Json file to be sent to the ITTM servers.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
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
}
