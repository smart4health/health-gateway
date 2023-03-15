package pt.uninova.s4h.healthgateway.ittm.message.force;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Implements an auxiliary class to make the force test json conversion.
 */
public class ForceTestGsonUtil {

    private Gson gson = null;

    public ForceTestGsonUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.create();
    }

    public ForceTestRequestJson fromRequestJson(String requestJsonString) {
        return gson.fromJson(requestJsonString, ForceTestRequestJson.class);
    }

    public String toRequestJson(ForceTestRequestJson requestMessage) {
        return gson.toJson(requestMessage, ForceTestRequestJson.class);
    }

    public ForceTestResponseJson fromResponseJson(String responseJsonString) {
        return gson.fromJson(responseJsonString, ForceTestResponseJson.class);
    }

    public String toResponseJson(ForceTestResponseJson responseMessage) {
        return gson.toJson(responseMessage, ForceTestResponseJson.class);
    }
}
