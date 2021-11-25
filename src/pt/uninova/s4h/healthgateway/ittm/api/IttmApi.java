package pt.uninova.s4h.healthgateway.ittm.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.slf4j.LoggerFactory;
import pt.uninova.s4h.healthgateway.ittm.message.file.SaveMedxEvents;
import pt.uninova.s4h.healthgateway.ittm.message.file.SaveMedxEventsException;
import pt.uninova.s4h.healthgateway.ittm.message.force.ForceTestGsonUtil;
import pt.uninova.s4h.healthgateway.ittm.message.force.ForceTestRequestJson;
import pt.uninova.s4h.healthgateway.ittm.message.force.ForceTestResponseJson;
import pt.uninova.s4h.healthgateway.ittm.message.login.LoginGsonUtil;
import pt.uninova.s4h.healthgateway.ittm.message.login.LoginRequestJson;
import pt.uninova.s4h.healthgateway.ittm.message.login.LoginResponseJson;
import pt.uninova.s4h.healthgateway.ittm.message.parameters.ParametersGsonUtil;
import pt.uninova.s4h.healthgateway.ittm.message.parameters.ParametersRequestJson;
import pt.uninova.s4h.healthgateway.ittm.message.parameters.ParametersResponseJson;
import pt.uninova.s4h.healthgateway.ittm.message.training.TrainingGsonUtil;
import pt.uninova.s4h.healthgateway.ittm.message.training.TrainingRequestJson;
import pt.uninova.s4h.healthgateway.ittm.message.training.TrainingResponseJson;

/**
 * Class to define an ITTM API Interface.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 10 October 2019 - First version.
 */
public class IttmApi {

    private static final String CONTENT_TYPE = "application/json";
    private static final String AUTHORIZATION_PREFIX = "Bearer ";
    private static final String URL_DO_LOGIN = "/api/dologin/";
    private static final String URL_ADD_BATCH_DATA = "/api/add_sensor_data_machine/";
    private static final String URL_ADD_FORCE_TEST = "/api/add_sensor_data_forcetest/";
    private static final String URL_RETRIEVE_TRAINING_PARAMETERS = "/api/retrieve_training_parameters_latest/";
    private static final String INVALID_TOKEN = "-1";
    private final String hostUrl;
    private final String machineId;
    private final String directory;
    //private String username = "";
    //private String password = "";
    private String token = INVALID_TOKEN;

    private static IttmApi ittmApi = null;

    private static final ch.qos.logback.classic.Logger ITTM_API_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private IttmApi(String hostUrl, String machineId, String directory) {
        this.hostUrl = hostUrl;
        this.machineId = machineId;
        this.directory = directory;
    }

    public static synchronized IttmApi getFirstInstance(String hostUrl, String machineId, String directory) {
        if (ittmApi == null) {
            ittmApi = new IttmApi(hostUrl, machineId, directory);
        }
        return ittmApi;
    }

    public static synchronized IttmApi getInstance() {
        return ittmApi;
    }

    public String getIttmHost() {
        return this.hostUrl;
    }

    private String ittmPostRequest(String urlString, String contentType, String authorization, String jsonRequest) throws IttmApiException {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", contentType);
            if (authorization != null) {
                urlConnection.setRequestProperty("Authorization", authorization);
            }
            urlConnection.setDoOutput(true);
            OutputStream outputStream = urlConnection.getOutputStream();
            byte[] input = jsonRequest.getBytes();
            outputStream.write(input, 0, input.length);
            ITTM_API_LOGGER.trace("JsonRequest= " + jsonRequest);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            StringBuilder responseBuilder = new StringBuilder();
            String responseLine = null;
            while ((responseLine = bufferedReader.readLine()) != null) {
                responseBuilder.append(responseLine.trim());
            }
            ITTM_API_LOGGER.trace("Received Response= " + responseBuilder.toString());
            return responseBuilder.toString();

        } catch (MalformedURLException ex) {
            ITTM_API_LOGGER.error("MalformedURL=" + urlString + ", = " + ex.getMessage());
            throw new IttmApiException("Invalid host");
        } catch (IOException ex) {
            ITTM_API_LOGGER.error("IOException=" + ex.getMessage());
            throw new IttmApiException("Error reading(writing HTTP request");
        }
    }

    /**
     * Function for session login in the ITTM server.
     *
     * @param loginRequestJson The Json to login, containing the username and
     * password.
     * @return The login response.
     * @throws IttmApiException If the HTTP request fails.
     */
    private LoginResponseJson doLogin(LoginRequestJson loginRequestJson) throws IttmApiException {

        LoginGsonUtil loginGsonUtil = new LoginGsonUtil();

        String urlString = hostUrl + URL_DO_LOGIN;

        String loginRequestStr = loginGsonUtil.toRequestJson(loginRequestJson);
        String loginResponseStr = ittmPostRequest(urlString, CONTENT_TYPE, null, loginRequestStr);

        return loginGsonUtil.fromResponseJson(loginResponseStr);
    }

    /**
     * Function to insert training batch data
     *
     * @param token The authentication token.
     * @param trainingRequestJson The training data to be inserted
     * @return The training insertion result.
     * @throws IttmApiException If the HTTP request fails.
     */
    private TrainingResponseJson addSensorDataMachine(String token, TrainingRequestJson trainingRequestJson) throws IttmApiException {

        TrainingGsonUtil trainingGsonUtil = new TrainingGsonUtil();

        String urlString = hostUrl + URL_ADD_BATCH_DATA;

        String trainingRequestStr = trainingGsonUtil.toRequestJson(trainingRequestJson);
        String trainingResponseStr = ittmPostRequest(urlString, CONTENT_TYPE, AUTHORIZATION_PREFIX + token, trainingRequestStr);

        return trainingGsonUtil.fromResponseJson(trainingResponseStr);
    }

    /**
     * Function to insert force test data.
     *
     * @param token The authentication token.
     * @param forceTestRequestJson The force test data to be inserted.
     * @return The force test insertion result.
     * @throws IttmApiException IttmApiException If the HTTP request fails.
     */
    private ForceTestResponseJson addSensorDataForceTest(String token, ForceTestRequestJson forceTestRequestJson) throws IttmApiException {

        ForceTestGsonUtil forceTestGsonUtil = new ForceTestGsonUtil();

        String urlString = hostUrl + URL_ADD_FORCE_TEST;

        String forceTestRequestStr = forceTestGsonUtil.toRequestJson(forceTestRequestJson);
        String forceTestResponseStr = ittmPostRequest(urlString, CONTENT_TYPE, AUTHORIZATION_PREFIX + token, forceTestRequestStr);

        return forceTestGsonUtil.fromResponseJson(forceTestResponseStr);
    }

    /**
     *
     * @param token the authentication token.
     * @param parametersRequestJson The training data to be retrieved.
     * @return The retrieve training parameters result.
     * @throws IttmApiException IttmApiException If the HTTP request fails.
     */
    private ParametersResponseJson retrieveTrainingParameters(String token, ParametersRequestJson parametersRequestJson) throws IttmApiException {

        ParametersGsonUtil parametersGsonUtil = new ParametersGsonUtil();

        String urlString = hostUrl + URL_RETRIEVE_TRAINING_PARAMETERS;

        String parametersRequestStr = parametersGsonUtil.toRequestJson(parametersRequestJson);
        String parametersResponseStr = ittmPostRequest(urlString, CONTENT_TYPE, AUTHORIZATION_PREFIX + token, parametersRequestStr);

        return parametersGsonUtil.fromResponseJson(parametersResponseStr);
    }

    public String login(String username, String password) throws IttmApiException {

        LoginRequestJson loginRequestJson = new LoginRequestJson();
        loginRequestJson.setUsername(username);
        loginRequestJson.setPassword(password);

        LoginResponseJson loginResponseJson = doLogin(loginRequestJson);

        this.token = loginResponseJson.getValues().getToken();

        if (loginResponseJson.getStatusCode() != 200 || token.equalsIgnoreCase(INVALID_TOKEN)) {
            throw new IttmApiException("Error doLogin: Status " + loginResponseJson.getStatusCode() + " = Internal Server Error");
        }

        ITTM_API_LOGGER.debug("token = " + token);
        return this.token;
    }

//    public ParametersResponseJson downloadTrainingParameters(String citizenId) throws IttmApiException, IttmApiTokenException {
//
//        if (token.equalsIgnoreCase(INVALID_TOKEN)) {
//            throw new IttmApiTokenException();
//        }
//
//        ParametersRequestJson parametersRequestJson = new ParametersRequestJson();
//        parametersRequestJson.setCitizenId(citizenId);
//        ParametersResponseJson parametersResponseJson = ittmApi.retrieveTrainingParameters(token, parametersRequestJson);
//        if (parametersResponseJson.getStatusCode() == 401 || parametersResponseJson.getStatusCode() == 403) {
//            throw new IttmApiTokenException();
//        }
//
//        if (parametersResponseJson.getStatusCode() != 200) {
//            throw new IttmApiException("Error Retrieving Training Parameters: Status " + parametersResponseJson.getStatusCode() + " = " + parametersResponseJson.getMessage());
//        }
//        return parametersResponseJson;
//    }
    public ParametersResponseJson downloadTrainingParametersLatest() throws IttmApiException, IttmApiTokenException, IttmApi500Exception {

        if (token.equalsIgnoreCase(INVALID_TOKEN)) {
            throw new IttmApiTokenException();
        }

        ParametersRequestJson parametersRequestJson = new ParametersRequestJson();
        parametersRequestJson.setMachineId(this.machineId);
        ParametersResponseJson parametersResponseJson = ittmApi.retrieveTrainingParameters(token, parametersRequestJson);
        if (parametersResponseJson.getStatusCode() == 401 || parametersResponseJson.getStatusCode() == 402 || parametersResponseJson.getStatusCode() == 403) {
            throw new IttmApiTokenException();
        }

        if (parametersResponseJson.getStatusCode() == 500) {
            throw new IttmApi500Exception("Error Retrieving Training Parameters: Status " + parametersResponseJson.getStatusCode() + " = " + parametersResponseJson.getMessage() + "\n Make login");
        }
        if (parametersResponseJson.getStatusCode() != 200) {
            throw new IttmApiException("Error Retrieving Training Parameters: Status " + parametersResponseJson.getStatusCode() + " = " + parametersResponseJson.getMessage());
        }
        return parametersResponseJson;
    }

    public void uploadForceTest(ForceTestRequestJson forceTestRequestJson) throws IttmApiException, IttmApiTokenException, IttmApi500Exception {
        try {
            SaveMedxEvents.saveForceTest(directory, forceTestRequestJson);
        } catch (SaveMedxEventsException ex) {
            ITTM_API_LOGGER.error("Error saving force test = " + ex.getMessage());
        }

        if (token.equalsIgnoreCase(INVALID_TOKEN)) {
            throw new IttmApiTokenException();
        }

        forceTestRequestJson.setMachineId(machineId);

        ForceTestResponseJson forceTestResponseJson = ittmApi.addSensorDataForceTest(token, forceTestRequestJson);

        if (forceTestResponseJson.getStatusCode() == 401 || forceTestResponseJson.getStatusCode() == 403) {
            throw new IttmApiTokenException();
        }

        if (forceTestResponseJson.getStatusCode() == 500) {
            throw new IttmApi500Exception("Error Uploading Force Test: Status " + forceTestResponseJson.getStatusCode() + " = " + forceTestResponseJson.getMessage() + "\n Make login");
        }

        if (forceTestResponseJson.getStatusCode() != 200) {
            throw new IttmApiException("Error Uploading Force Test: Status " + forceTestResponseJson.getStatusCode() + " = " + forceTestResponseJson.getMessage());
        }
    }

    public void uploadTrainingData(TrainingRequestJson trainingRequestJson) throws IttmApiException, IttmApiTokenException, IttmApi500Exception {

        try {
            SaveMedxEvents.saveTrainingData(directory, trainingRequestJson);
        } catch (SaveMedxEventsException ex) {
            ITTM_API_LOGGER.error("Error saving training data = " + ex.getMessage());
        }

        if (token.equalsIgnoreCase(INVALID_TOKEN)) {
            throw new IttmApiTokenException();
        }

        trainingRequestJson.setMachineId(machineId);

        TrainingResponseJson trainingResponseJson = ittmApi.addSensorDataMachine(token, trainingRequestJson);

        if (trainingResponseJson.getStatusCode() == 401 || trainingResponseJson.getStatusCode() == 403) {
            throw new IttmApiTokenException();
        }

        if (trainingResponseJson.getStatusCode() == 500) {
            throw new IttmApi500Exception("Error Uploading Training Data: Status " + trainingResponseJson.getStatusCode() + " = " + trainingResponseJson.getMessage() + "\n Make login");
        }

        if (trainingResponseJson.getStatusCode() != 200) {
            throw new IttmApiException("Error Uploading Training Data: Status " + trainingResponseJson.getStatusCode() + " = " + trainingResponseJson.getMessage());
        }
    }
    
    public void saveStop(TrainingRequestJson trainingRequestJson) {

        try {
            SaveMedxEvents.saveTrainingData(directory, trainingRequestJson);
        } catch (SaveMedxEventsException ex) {
            ITTM_API_LOGGER.error("Error saving stop = " + ex.getMessage());
        }
    }    

    /**
     * *
     * Old functions
     */
//    private IttmApi(String hostUrl, String username, String password) {
//        this.hostUrl = hostUrl;
//        this.username = username;
//        this.password = password;
//    }
//
//    public static synchronized IttmApi getFirstInstance(String hostUrl, String username, String password) {
//        if (ittmApi == null) {
//            ittmApi = new IttmApi(hostUrl, username, password);
//        }
//        return ittmApi;
//    }
//    private String loginNoToken() throws IttmApiException {
//
//        LoginRequestJson loginRequestJson = new LoginRequestJson();
//        loginRequestJson.setUsername(username);
//        loginRequestJson.setPassword(password);
//
//        LoginResponseJson loginResponseJson = doLogin(loginRequestJson);
//
//        String token = loginResponseJson.getValues().getToken();
//
//        if (loginResponseJson.getStatusCode() != 200 || token.equalsIgnoreCase(INVALID_TOKEN)) {
//            throw new IttmApiException("Error doLogin: Status " + loginResponseJson.getStatusCode() + " = Internal Server Error");
//        }
//
//        ITTM_API_LOGGER.debug("token = " + token);
//        return token;
//    }
//
//    public ParametersResponseJson downloadTrainingParametersNoToken(String citizenId) throws IttmApiException {
//
//        String token = this.loginNoToken();
//
//        ParametersRequestJson parametersRequestJson = new ParametersRequestJson();
//        parametersRequestJson.setCitizenId(citizenId);
//        ParametersResponseJson parametersResponseJson = ittmApi.retrieveTrainingParameters(token, parametersRequestJson);
//        if (parametersResponseJson.getStatusCode() != 200) {
//            throw new IttmApiException("Error Retrieving Training Parameters: Status " + parametersResponseJson.getStatusCode() + " = " + parametersResponseJson.getMessage());
//
//        }
//        return parametersResponseJson;
//    }
//
//    public void uploadForceTestNoToken(ForceTestRequestJson forceTestRequestJson) throws IttmApiException {
//
//        String token = this.loginNoToken();
//
//        ForceTestResponseJson forceTestResponseJson = ittmApi.addSensorDataForceTest(token, forceTestRequestJson);
//        if (forceTestResponseJson.getStatusCode() != 200) {
//            throw new IttmApiException("Error Uploading Force Test: Status " + forceTestResponseJson.getStatusCode() + " = " + forceTestResponseJson.getMessage());
//        }
//    }
//
//    public void uploadTrainingDataNoToken(TrainingRequestJson trainingRequestJson) throws IttmApiException {
//
//        String token = this.loginNoToken();
//
//        TrainingResponseJson trainingResponseJson = ittmApi.addSensorDataMachine(token, trainingRequestJson);
//        if (trainingResponseJson.getStatusCode() != 200) {
//            throw new IttmApiException("Error Uploading Training Data: Status " + trainingResponseJson.getStatusCode() + " = " + trainingResponseJson.getMessage());
//        }
//    }
}
