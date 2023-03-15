package pt.uninova.s4h.healthgateway.ittm.message.login;

import com.google.gson.annotations.SerializedName;

/**
* Class to hold the Login response received from the ITTM servers.
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
