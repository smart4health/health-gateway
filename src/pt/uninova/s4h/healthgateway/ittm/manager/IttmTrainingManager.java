package pt.uninova.s4h.healthgateway.ittm.manager;

import pt.uninova.s4h.healthgateway.ittm.api.*;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import pt.uninova.s4h.healthgateway.box.api.BoxApi;
import pt.uninova.s4h.healthgateway.box.api.BoxApiException;
import pt.uninova.s4h.healthgateway.ittm.message.training.Score;
import pt.uninova.s4h.healthgateway.ittm.message.training.SensorData;
import pt.uninova.s4h.healthgateway.ittm.message.training.TrainingRequestJson;
import pt.uninova.s4h.healthgateway.util.listeners.EventDispatcher;
import pt.uninova.s4h.healthgateway.util.message.HubHgMessage;
import pt.uninova.s4h.healthgateway.util.message.MessagesUtil.EventMessageType;

/**
 * Class to manage ITTM training events.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 05 June 2020 - First version.
 */
public class IttmTrainingManager {

    private EventMessageType trainingStatus;
    private ArrayList<SensorData> sensorDataList;
    private TrainingRequestJson trainingRequestJson;
    private int trainingWeight;

    private static IttmTrainingManager ittmTrainingManager = null;

    private final EventDispatcher<HubHgMessage> onHubHgMessage;
    
    public EventDispatcher<HubHgMessage> onHubHgMessage() {
        return onHubHgMessage;       
    }

    private static final ch.qos.logback.classic.Logger ITTM_MANAGER_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private IttmTrainingManager() {
        trainingStatus = EventMessageType.FINISH_TRAINING;
        sensorDataList = null;
        trainingRequestJson = new TrainingRequestJson();
        this.onHubHgMessage = new EventDispatcher<>();
    }

    public static synchronized IttmTrainingManager getInstance() {
        if (ittmTrainingManager == null) {
            ittmTrainingManager = new IttmTrainingManager();
        }
        return ittmTrainingManager;
    }

    public void newCitizenId(String citizenId) {
        if (trainingStatus == EventMessageType.START_TRAINING) {
            return;
        }
        trainingRequestJson.setCitizenId(citizenId);
        trainingStatus = EventMessageType.CITIZEN_ID;
    }

    public void newTrainingId(String trainingId) {
        if (trainingStatus == EventMessageType.START_TRAINING) {
            return;
        }
        trainingRequestJson.setTrainingId(trainingId);
        trainingStatus = EventMessageType.TRAINING_ID;
    }

    public void newTrainingWeight(float trainingweight) {
        trainingWeight = (int)trainingweight;
    }    
    
    public void newStartTrainig() {
        if (trainingStatus == EventMessageType.START_TRAINING) {
            return;
        }
        sensorDataList = new ArrayList<>();
        trainingRequestJson.setStartTime(Instant.now().toEpochMilli());
        trainingStatus = EventMessageType.START_TRAINING;
    }

    public void newStopTraining() {
        if (trainingStatus != EventMessageType.START_TRAINING) {
            return;
        }
        trainingRequestJson.setStopTime(Instant.now().toEpochMilli());
        trainingStatus = EventMessageType.STOP_TRAINING;
    }

    public void newAngle(float angle) {
        if (trainingStatus != EventMessageType.START_TRAINING) {
            return;
        }
        SensorData sensorData = new SensorData();
        sensorData.setAngle(angle);
        sensorData.setTimestamp(Instant.now().toEpochMilli());
        sensorDataList.add(sensorData);
    }

    public void newKbzMessage(String message) {
        if (message.contains("#") && trainingStatus == EventMessageType.STOP_TRAINING) {
            String[] scores = message.split("#");
            Score score = new Score();
            score.setPercentage(Float.parseFloat(scores[1].split(":")[1]));
            score.setCalories(0);
            score.setSteps(0);
            trainingRequestJson.setScore(score);
            trainingRequestJson.setRepetitions((int) Float.parseFloat(scores[3].split(":")[1]));
            trainingRequestJson.setSensorData(sensorDataList.toArray(new SensorData[sensorDataList.size()]));
            trainingStatus = EventMessageType.TRAINING_SCORE;
            //finishTraining();
            IttmApi.getInstance().saveStop(trainingRequestJson);
        }
        
    }

    public void newUploadTraining() {
        
        if (trainingStatus != EventMessageType.TRAINING_SCORE) {
            return;
        }
        ITTM_MANAGER_LOGGER.info("Finish training");
        try {            
            BoxApi.getInstance().uploadTraining(trainingRequestJson,trainingWeight);
        } catch (BoxApiException ex) {
            ITTM_MANAGER_LOGGER.error(ex.getMessage());
        }
        try {            
            IttmApi.getInstance().uploadTrainingData(trainingRequestJson);
        } catch (IttmApiException ex) {
            ITTM_MANAGER_LOGGER.error(ex.getMessage());
            //documentController.showPopupIttmError(ex.getMessage());
            onHubHgMessage.dispatch(new HubHgMessage(EventMessageType.TRAINING_API_ERROR, ex.getMessage(), null));
        } catch (IttmApi500Exception ex) {
            ITTM_MANAGER_LOGGER.error(ex.getMessage());
            onHubHgMessage.dispatch(new HubHgMessage(EventMessageType.TRAINING_500_ERROR, ex.getMessage(), null));             
        } catch (IttmApiTokenException ex) {
            ITTM_MANAGER_LOGGER.warn(ex.getMessage());
            //documentController.healthMonitorConnected(false);
            onHubHgMessage.dispatch(new HubHgMessage(EventMessageType.TRAINING_TOKEN_ERROR, null, null));
        }

        trainingRequestJson = new TrainingRequestJson();
        trainingStatus = EventMessageType.FINISH_TRAINING;
    }

    private void finishTraining() {
        try {
            ITTM_MANAGER_LOGGER.info("Finish training");
            IttmApi.getInstance().uploadTrainingData(trainingRequestJson);
        } catch (IttmApiException ex) {
            ITTM_MANAGER_LOGGER.error(ex.getMessage());
            //documentController.showPopupIttmError(ex.getMessage());
            onHubHgMessage.dispatch(new HubHgMessage(EventMessageType.TRAINING_API_ERROR, ex.getMessage(), null));
        } catch (IttmApi500Exception ex) {
            ITTM_MANAGER_LOGGER.error(ex.getMessage());
            onHubHgMessage.dispatch(new HubHgMessage(EventMessageType.TRAINING_500_ERROR, ex.getMessage(), null));            
        } catch (IttmApiTokenException ex) {
            ITTM_MANAGER_LOGGER.warn(ex.getMessage());
            //documentController.healthMonitorConnected(false);
            onHubHgMessage.dispatch(new HubHgMessage(EventMessageType.TRAINING_TOKEN_ERROR, null, null));
        }
        trainingRequestJson = new TrainingRequestJson();
        trainingStatus = EventMessageType.FINISH_TRAINING;
    }
}
