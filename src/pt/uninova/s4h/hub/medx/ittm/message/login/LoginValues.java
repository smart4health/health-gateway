package pt.uninova.s4h.hub.medx.ittm.message.login;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold a LoginValues Response received in the MQTT topics.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
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
