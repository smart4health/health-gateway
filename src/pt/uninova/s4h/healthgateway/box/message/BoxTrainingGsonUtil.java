package pt.uninova.s4h.healthgateway.box.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Implements an auxiliary class to make the training json conversion.
 */
public class BoxTrainingGsonUtil {
    private Gson gson = null;

    public BoxTrainingGsonUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.create();
    }

    public BoxTrainingRequestJson fromRequestJson(String requestJsonString) {
        return gson.fromJson(requestJsonString, BoxTrainingRequestJson.class);
    }

    public String toRequestJson(BoxTrainingRequestJson requestMessage) {
        return gson.toJson(requestMessage, BoxTrainingRequestJson.class);
    }

    public BoxTrainingResponseJson fromResponseJson(String responseJsonString) {
        return gson.fromJson(responseJsonString, BoxTrainingResponseJson.class);
    }

    public String toResponseJson(BoxTrainingResponseJson responseMessage) {
        return gson.toJson(responseMessage, BoxTrainingResponseJson.class);
    }
}