package pt.uninova.s4h.healthgateway.ittm.message.parameters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Implements an auxiliary class to make the Parameters json conversion.
 */
public class ParametersGsonUtil {

    private Gson gson = null;

    public ParametersGsonUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.create();
    }

    public ParametersRequestJson fromRequestJson(String requestJsonString) {
        return gson.fromJson(requestJsonString, ParametersRequestJson.class);
    }

    public String toRequestJson(ParametersRequestJson requestMessage) {
        return gson.toJson(requestMessage, ParametersRequestJson.class);
    }

    public ParametersResponseJson fromResponseJson(String responseJsonString) {
        return gson.fromJson(responseJsonString, ParametersResponseJson.class);
    }

    public String toResponseJson(ParametersResponseJson responseMessage) {
        return gson.toJson(responseMessage, ParametersResponseJson.class);
    }
}
