package pt.uninova.s4h.healthgateway.ittm.message.training;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Implements an auxiliary class to make the training json conversion.
 */
public class TrainingGsonUtil {

    private Gson gson = null;

    public TrainingGsonUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.create();
    }

    public TrainingRequestJson fromRequestJson(String requestJsonString) {
        return gson.fromJson(requestJsonString, TrainingRequestJson.class);
    }

    public String toRequestJson(TrainingRequestJson requestMessage) {
        return gson.toJson(requestMessage, TrainingRequestJson.class);
    }

    public TrainingResponseJson fromResponseJson(String responseJsonString) {
        return gson.fromJson(responseJsonString, TrainingResponseJson.class);
    }

    public String toResponseJson(TrainingResponseJson responseMessage) {
        return gson.toJson(responseMessage, TrainingResponseJson.class);
    }
}
