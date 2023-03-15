package pt.uninova.s4h.healthgateway.ittm.manager;

import static java.lang.Math.round;
import pt.uninova.s4h.healthgateway.ittm.api.*;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.ArrayList;
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
 */
public class IttmTrainingManager {

    private EventMessageType trainingStatus;
    private ArrayList<SensorData> sensorDataList;
    private TrainingRequestJson trainingRequestJson;
    private int trainingWeight;
    private int extensionAngle;
    private int flexionAngle;
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

    public void newExtensionAngle(float extension) {
        if (trainingStatus == EventMessageType.START_TRAINING) {
            return;
        }
        extensionAngle = (int)extension;
        trainingStatus = EventMessageType.EXTENSION_ANGLE;
    }

    public void newFlexionAngle(float flexion) {
        if (trainingStatus == EventMessageType.START_TRAINING) {
            return;
        }
        flexionAngle = (int)flexion;
        trainingStatus = EventMessageType.FLEXION_ANGLE;
    }
    
    public void newTrainingId(String trainingId) {
        if (trainingStatus == EventMessageType.START_TRAINING) {
            return;
        }
        trainingRequestJson.setTrainingId(trainingId);
        trainingStatus = EventMessageType.TRAINING_ID;
    }

    public void newTrainingNumber(float trainingNumber) {
        if (trainingStatus == EventMessageType.START_TRAINING) {
            return;
        }
        trainingRequestJson.setTrainingNumber((int)trainingNumber);
        trainingStatus = EventMessageType.TRAINING_NUMBER;
    }    

    public void newTrainingSession(float trainingSession) {
        if (trainingStatus == EventMessageType.START_TRAINING) {
            return;
        }
        trainingRequestJson.setTrainingSession((int)trainingSession);
        trainingStatus = EventMessageType.TRAINING_SESSION;
    }
    
    public void newTrainingWeight(float trainingweight) {
        if (trainingStatus == EventMessageType.START_TRAINING) {
            return;
        }
        trainingWeight = (int)trainingweight;
        trainingRequestJson.setTrainingWeight(trainingWeight);
        trainingStatus = EventMessageType.TRAINING_WEIGHT;
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
            int repetitions = (int) Float.parseFloat(scores[3].split(":")[1]);
            Score score = new Score();
            score.setPercentage(Float.parseFloat(scores[1].split(":")[1]));
            int calories = (int)round((flexionAngle-extensionAngle)*0.0023*9.8*trainingWeight*repetitions*0.000239006*10);
            score.setCalories(calories);
            score.setSteps(0);
            trainingRequestJson.setScore(score);
            trainingRequestJson.setRepetitions(repetitions);
            trainingRequestJson.setSensorData(sensorDataList.toArray(new SensorData[sensorDataList.size()]));
            trainingStatus = EventMessageType.TRAINING_SCORE;
            IttmApi.getInstance().saveStop(trainingRequestJson);
        }
    }

    public void newUploadTraining() {
        
        if (trainingStatus != EventMessageType.TRAINING_SCORE) {
            return;
        }
        ITTM_MANAGER_LOGGER.info("Finish training");
        try {            
            BoxApi.getInstance().uploadTraining(trainingRequestJson);
        } catch (BoxApiException ex) {
            ITTM_MANAGER_LOGGER.error(ex.getMessage());
        }
        try {            
            IttmApi.getInstance().uploadTrainingData(trainingRequestJson);
        } catch (IttmApiException ex) {
            ITTM_MANAGER_LOGGER.error(ex.getMessage());
            onHubHgMessage.dispatch(new HubHgMessage(EventMessageType.TRAINING_API_ERROR, ex.getMessage(), null));
        } catch (IttmApi500Exception ex) {
            ITTM_MANAGER_LOGGER.error(ex.getMessage());
            onHubHgMessage.dispatch(new HubHgMessage(EventMessageType.TRAINING_500_ERROR, ex.getMessage(), null));             
        } catch (IttmApiTokenException ex) {
            ITTM_MANAGER_LOGGER.warn(ex.getMessage());
            onHubHgMessage.dispatch(new HubHgMessage(EventMessageType.TRAINING_TOKEN_ERROR, null, null));
        }
        trainingRequestJson = new TrainingRequestJson();
        trainingStatus = EventMessageType.FINISH_TRAINING;
    }

}
