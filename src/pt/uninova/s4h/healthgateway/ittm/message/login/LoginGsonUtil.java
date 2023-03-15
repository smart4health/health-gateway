package pt.uninova.s4h.healthgateway.ittm.message.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Implements an auxiliary class to make the Login json conversion.
 */
public class LoginGsonUtil {

    private Gson gson = null;

    public LoginGsonUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
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
