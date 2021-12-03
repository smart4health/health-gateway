package pt.uninova.s4h.healthgateway.box.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Implements an auxiliary class to make the training json conversion.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class BoxTrainingGsonUtil {

    //private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private Gson gson = null;

    public BoxTrainingGsonUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        //gsonBuilder.setDateFormat(DATE_FORMAT);
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
