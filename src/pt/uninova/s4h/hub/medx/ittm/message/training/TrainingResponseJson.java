package pt.uninova.s4h.hub.medx.ittm.message.training;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the Response Training Json file received from the ITTM servers.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class TrainingResponseJson {

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