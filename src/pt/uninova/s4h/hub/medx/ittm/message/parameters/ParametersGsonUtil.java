package pt.uninova.s4h.hub.medx.ittm.message.parameters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Implements an auxiliary class to make the Parameters json conversion.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class ParametersGsonUtil {

    //private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private Gson gson = null;

    public ParametersGsonUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        //gsonBuilder.setDateFormat(DATE_FORMAT);
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
