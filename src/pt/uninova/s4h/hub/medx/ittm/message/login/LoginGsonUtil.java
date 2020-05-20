package pt.uninova.s4h.hub.medx.ittm.message.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Implements an auxiliary class to make the Login json conversion.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class LoginGsonUtil {

    //private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private Gson gson = null;

    public LoginGsonUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        //gsonBuilder.setDateFormat(DATE_FORMAT);
        this.gson = gsonBuilder.create();
    }

    public LoginRequestJson fromRequestJson(String requestJsonString) {
        return gson.fromJson(requestJsonString, LoginRequestJson.class);
    }

    public String toRequestJson(LoginRequestJson requestMessage) {
        return gson.toJson(requestMessage, LoginRequestJson.class);
    }

    public LoginResponseJson fromResponseJson(String responseJsonString) {
        return gson.fromJson(responseJsonString, LoginResponseJson.class);
    }

    public String toResponseJson(LoginResponseJson responseMessage) {
        return gson.toJson(responseMessage, LoginResponseJson.class);
    }
}
