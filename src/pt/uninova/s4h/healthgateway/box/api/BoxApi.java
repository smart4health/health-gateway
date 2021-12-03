package pt.uninova.s4h.healthgateway.box.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.slf4j.LoggerFactory;
import pt.uninova.s4h.healthgateway.box.message.BoxTrainingGsonUtil;
import pt.uninova.s4h.healthgateway.box.message.BoxTrainingRequestJson;
import pt.uninova.s4h.healthgateway.ittm.message.training.TrainingRequestJson;
import pt.uninova.s4h.healthgateway.box.message.BoxTrainingResponseJson;

/**
 * Class to define an ITTM API Interface.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class BoxApi {

    private static final String CONTENT_TYPE = "application/json";
    private final String hostUrl;

    private static BoxApi boxApi = null;

    private static final ch.qos.logback.classic.Logger BOX_API_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private BoxApi(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public static synchronized BoxApi getFirstInstance(String hostUrl) {
        if (boxApi == null) {
            boxApi = new BoxApi(hostUrl);
        }
        return boxApi;
    }

    public static synchronized BoxApi getInstance() {
        return boxApi;
    }

    public String getBoxHost() {
        return this.hostUrl;
    }

    private String boxPostRequest(String contentType, String jsonRequest) throws BoxApiException {
        try {
            URL url = new URL(hostUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", contentType);
            urlConnection.setDoOutput(true);
            OutputStream outputStream = urlConnection.getOutputStream();
            byte[] input = jsonRequest.getBytes();
            outputStream.write(input, 0, input.length);
            BOX_API_LOGGER.trace("JsonRequest= " + jsonRequest);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            StringBuilder responseBuilder = new StringBuilder();
            String responseLine = null;
            while ((responseLine = bufferedReader.readLine()) != null) {
                responseBuilder.append(responseLine.trim());
            }
            BOX_API_LOGGER.trace("Received Response= " + responseBuilder.toString());
            return responseBuilder.toString();

        } catch (MalformedURLException ex) {
            BOX_API_LOGGER.error("MalformedURL=" + hostUrl + ", = " + ex.getMessage());
            throw new BoxApiException("Invalid host");
        } catch (IOException ex) {
            BOX_API_LOGGER.error("IOException=" + ex.getMessage());
            throw new BoxApiException("Error reading(writing HTTP request");
        }
    }

    public void uploadTraining(TrainingRequestJson trainingRequestJson) throws BoxApiException {

        BoxTrainingRequestJson boxTrainingRequestJson = new BoxTrainingRequestJson();
        boxTrainingRequestJson.setTimestamp(trainingRequestJson.getStartTime()/1000);
        boxTrainingRequestJson.setLength((trainingRequestJson.getStartTime()-trainingRequestJson.getStopTime())/1000);
        boxTrainingRequestJson.setScore(trainingRequestJson.getScore().getPercentage());
        boxTrainingRequestJson.setRepetitions(trainingRequestJson.getRepetitions());
        
        BoxTrainingGsonUtil boxTrainingGsonUtil = new BoxTrainingGsonUtil();

        String boxTrainingRequestStr = boxTrainingGsonUtil.toRequestJson(boxTrainingRequestJson);
        String boxTrainingResponseStr = boxPostRequest(CONTENT_TYPE, boxTrainingRequestStr);

        BoxTrainingResponseJson boxTrainingResponseJson = boxTrainingGsonUtil.fromResponseJson(boxTrainingResponseStr);
        if (boxTrainingResponseJson.getStatusCode() != 200) {
            throw new BoxApiException("Error Uploading Training Data: Status " + boxTrainingResponseJson.getStatusCode() + " = " + boxTrainingResponseJson.getMessage());
        }

    }

}
