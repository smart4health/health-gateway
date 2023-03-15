package pt.uninova.s4h.healthgateway.ittm.message.login;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold a LoginValues Response received in the MQTT topics.
 */
public class LoginValues {

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
