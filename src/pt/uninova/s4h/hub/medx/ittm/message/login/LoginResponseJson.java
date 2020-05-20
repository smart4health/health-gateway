package pt.uninova.s4h.hub.medx.ittm.message.login;

import com.google.gson.annotations.SerializedName;

/**
* Class to hold the Login response received from the ITTM servers.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class LoginResponseJson {

    @SerializedName("statuscode")
    private int statusCode;

    @SerializedName("values")
    private LoginValues values;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public LoginValues getValues() {
        return values;
    }

    public void setValues(LoginValues values) {
        this.values = values;
    }

}
