package pt.uninova.s4h.healthgateway.ittm.message.force;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the Force test response received from the ITTM servers.
 */
public class ForceTestResponseJson {

    @SerializedName("message")
    private String message;

    @SerializedName("statuscode")
    private int statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
