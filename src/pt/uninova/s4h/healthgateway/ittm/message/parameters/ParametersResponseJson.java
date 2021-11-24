package pt.uninova.s4h.healthgateway.ittm.message.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold the Login response received from the ITTM servers.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class ParametersResponseJson {

    @SerializedName("message")
    private String message;

    @SerializedName("statuscode")
    private int statusCode;

    @SerializedName("values")
    private ParametersValues values;

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

    public ParametersValues getValues() {
        return values;
    }

    public void setValues(ParametersValues values) {
        this.values = values;
    }

    public String toStringConsole() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Message= ").append(message).append("\n");
        stringBuilder.append("Status Code= ").append(statusCode).append("\n");
        stringBuilder.append("LE ROM extension= ").append(values.getLeRomExtension()).append("\n");
        stringBuilder.append("LE ROM Flexio= ").append(values.getLeRomFlexion()).append("\n");
        stringBuilder.append("LE Counter Weight= ").append(values.getLeCounterWeight()).append("\n");
        stringBuilder.append("LE Seat Cushion= ").append(values.getLeSeatCushion()).append("\n");
        stringBuilder.append("LE Tight Position= ").append(values.getLeTightPosition()).append("\n");
        stringBuilder.append("LE Training Weight= ").append(values.getLeTrainingWeight()).append("\n");
        stringBuilder.append("LE Zero Position= ").append(values.getLeZeroPosition()).append("\n");
        stringBuilder.append("Session Number= ").append(values.getSessionNumber()).append("\n");
        stringBuilder.append("Training ID= ").append(values.getTrainingId()).append("\n");
        stringBuilder.append("Training Number= ").append(values.getTrainingNumber()).append("\n");
        return stringBuilder.toString();
    }
}
