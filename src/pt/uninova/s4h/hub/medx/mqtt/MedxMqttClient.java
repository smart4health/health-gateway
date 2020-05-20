package pt.uninova.s4h.hub.medx.mqtt;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.LoggerFactory;
import pt.uninova.s4h.hub.medx.ittm.api.IttmApi;
import pt.uninova.s4h.hub.medx.ittm.api.IttmApiException;
import pt.uninova.s4h.hub.medx.ittm.api.IttmApiTokenException;
import pt.uninova.s4h.hub.medx.ittm.message.file.SaveMedxEvents;
import pt.uninova.s4h.hub.medx.ittm.message.file.SaveMedxEventsException;
import pt.uninova.s4h.hub.medx.ittm.message.training.Score;
import pt.uninova.s4h.hub.medx.ittm.message.training.SensorData;
import pt.uninova.s4h.hub.medx.ittm.message.training.TrainingRequestJson;
import pt.uninova.s4h.hub.medx.physiotherapy.FXMLDocumentController;
import pt.uninova.s4h.hub.medx.senml.MedxEvent;
import pt.uninova.s4h.hub.medx.senml.MedxGsonUtil;

/**
 * Class to implement a MQTT client.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 07 September 2019 - First version.
 */
public class MedxMqttClient implements MqttCallback {
    
    private enum SensorName {
        ANGLE, FORCE, START_TRAINING, STOP_TRAINING, CITIZEN_ID, TRAINING_ID, TIMES, MESSAGE, IMAGE;
    }
    
    private enum TrainingStatus {
        CITIZEN_ID, TRAINING_ID, START_TRAINING, STOP_TRAINING, SCORE, FINISH;
    }
    
    private static final ch.qos.logback.classic.Logger MEDX_MQTT_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private final String directory;
    private final String mqttPublishTopic;
    private final String[] mqttSubscribeTopics;
    private final String mqttBrokerUrl;
    private final String mqttClientId;
    private MedxGsonUtil medxGsonUtil = null;
    private MqttClient mqttClient = null;
    private FXMLDocumentController documentController = null;
    
    private TrainingStatus trainingStatus = TrainingStatus.FINISH;
    private ArrayList<SensorData> sensorDataList = null;
    private TrainingRequestJson trainingRequestJson = new TrainingRequestJson();
    
    private static MedxMqttClient medxMqttClient = null;
    
    private MedxMqttClient(String[] mqttSubscribeTopics, String mqttPublishTopic, String mqttBrokerUrl, String mqttClientId, String directory) {
        this.mqttSubscribeTopics = mqttSubscribeTopics;
        this.mqttPublishTopic = mqttPublishTopic;
        this.mqttBrokerUrl = mqttBrokerUrl;
        this.mqttClientId = mqttClientId;
        this.medxGsonUtil = new MedxGsonUtil();
        this.directory = directory;
    }
    
    public static synchronized MedxMqttClient getFirstInstance(String[] mqttSubscribeTopics, String mqttPublishTopic, String mqttBrokerUrl, String mqttClientId, String directory) {
        if (medxMqttClient == null) {
            medxMqttClient = new MedxMqttClient(mqttSubscribeTopics, mqttPublishTopic, mqttBrokerUrl, mqttClientId, directory);
        }
        return medxMqttClient;
    }
    
    public static synchronized MedxMqttClient getInstance() {
        return medxMqttClient;
    }
    
    public void initialise(FXMLDocumentController fxmlDocumentController) throws MedxMqttClientException {
        
        try {
            MqttDefaultFilePersistence mqttDefaultFilePersistence = new MqttDefaultFilePersistence(directory);
            mqttClient = new MqttClient(mqttBrokerUrl, mqttClientId, null);//mqttDefaultFilePersistence);
            mqttClient.setCallback(this);
            /**
             * This is necessary to avoid "too many publishes in progress" error
             * message Default value is 10.
             */
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setMaxInflight(100000);
            mqttConnectOptions.setCleanSession(true);
            mqttClient.connect(mqttConnectOptions);
            for (String mqttSubscribeTopic : mqttSubscribeTopics) {
                MEDX_MQTT_LOGGER.debug("Subscribing to the following topics:");
                mqttClient.subscribe(mqttSubscribeTopic);
                MEDX_MQTT_LOGGER.info(mqttSubscribeTopic);
            }
            MEDX_MQTT_LOGGER.info("== End.");
            
            documentController = fxmlDocumentController;
            
        } catch (MqttException ex) {
            MEDX_MQTT_LOGGER.error("Error starting MEDX MQTT Client= " + ex.getMessage());
            throw new MedxMqttClientException(ex.getMessage());
        }
    }
    
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        
        String messageString = new String(mqttMessage.getPayload());
        MEDX_MQTT_LOGGER.debug("New message: " + messageString);
        MEDX_MQTT_LOGGER.debug("Topic: " + topic);
        
        try {
            MedxEvent[] medxEvents = medxGsonUtil.fromJson(messageString);
            for (MedxEvent medxevent : medxEvents) {
                try {
                    switch (SensorName.valueOf(medxevent.getName().toUpperCase())) {
                        case MESSAGE:
                            String stringValue = medxevent.getStringValue();
                            documentController.addKbzMessage(stringValue);
                            
                            if (stringValue.contains("#") && trainingStatus == TrainingStatus.STOP_TRAINING) {
                                String[] scores = stringValue.split("#");
                                Score score = new Score();
                                score.setPercentage(Float.parseFloat(scores[1].split(":")[1]));
                                score.setCalories(0);
                                score.setSteps(0);
                                trainingRequestJson.setScore(score);
                                trainingRequestJson.setRepetitions((int) Float.parseFloat(scores[3].split(":")[1]));
                                trainingRequestJson.setSensorData(sensorDataList.toArray(new SensorData[sensorDataList.size()]));
                                trainingStatus = TrainingStatus.SCORE;
                                finishTraining();
                            }
                            break;
                        
                        case IMAGE:
                            documentController.addKbzImage(medxevent.getStringValue());
                            break;
                        
                        case FORCE:
                            break;
                        case CITIZEN_ID:
                            if (trainingStatus == TrainingStatus.START_TRAINING) {
                                break;
                            }
                            trainingRequestJson.setCitizenId(medxevent.getStringValue());
                            trainingStatus = TrainingStatus.CITIZEN_ID;
                            break;
                        case TRAINING_ID:
                            if (trainingStatus == TrainingStatus.START_TRAINING) {
                                break;
                            }
                            trainingRequestJson.setTrainingId(medxevent.getStringValue());
                            trainingStatus = TrainingStatus.TRAINING_ID;
                            break;
                        case START_TRAINING:
                            if (trainingStatus == TrainingStatus.START_TRAINING) {
                                break;
                            }
                            sensorDataList = new ArrayList<>();
                            trainingRequestJson.setStartTime(medxevent.getTime());
                            trainingStatus = TrainingStatus.START_TRAINING;
                            break;
                        case ANGLE:
                            if (trainingStatus != TrainingStatus.START_TRAINING) {
                                break;
                            }
                            SensorData sensorData = new SensorData();
                            sensorData.setAngle(medxevent.getValue());
                            sensorData.setTimestamp(medxevent.getTime());
                            sensorDataList.add(sensorData);
                            break;
                        case STOP_TRAINING:
                            if (trainingStatus != TrainingStatus.START_TRAINING) {
                                break;
                            }
                            trainingRequestJson.setStopTime(medxevent.getTime());
                            trainingStatus = TrainingStatus.STOP_TRAINING;
                            break;
                        default:
                        //Do nothing;
                    }
                } catch (Exception ex) {
                    MEDX_MQTT_LOGGER.debug("Invalid Sensor Name or message value is null");
                }
            }
        } catch (Exception ex) {
            MEDX_MQTT_LOGGER.error("Messaged Arrived Exception = " + ex.getMessage());
        }
    }
    
    private void finishTraining() {
        
        try {
            MEDX_MQTT_LOGGER.info("Finish training");
            SaveMedxEvents.saveTrainingData(documentController.getSaveDirectory(), trainingRequestJson);
            IttmApi.getInstance().uploadTrainingData(trainingRequestJson);
        } catch (SaveMedxEventsException ex) {
            MEDX_MQTT_LOGGER.error("Error saving training = " + ex.getMessage());
        } catch (IttmApiException ex) {
            MEDX_MQTT_LOGGER.error(ex.getMessage());
            documentController.showPopupIttmError(ex.getMessage());
        } catch (IttmApiTokenException ex) {
            MEDX_MQTT_LOGGER.warn(ex.getMessage());
            documentController.healthMonitorConnected(false);
        }
        trainingRequestJson = new TrainingRequestJson();
        trainingStatus = TrainingStatus.FINISH;
    }
    
    public void publishAngleMeasurement(float value) {
        if (Float.isNaN(value)) {
            value = 0.0f;
        }
        publishMqttValue(SensorName.ANGLE, value, "Degrees");
    }
    
    public void publishStartTraining() {
        publishMqttValue(SensorName.START_TRAINING, 0, "N/A");
    }
    
    public void publishStopTraining() {
        publishMqttValue(SensorName.STOP_TRAINING, 0, "N/A");
    }
    
    public void publishAnglesTimesTraining(int startAngle, int stopAngle, int backTime, int frontTime, int holdTime) {
        publishMqttStringValue(SensorName.TIMES, startAngle + ";" + stopAngle + ";" + backTime + ";" + frontTime + ";" + holdTime + ";", "N/A");
    }
    
    public void publishCitizenId(String citizenId) {
        publishMqttStringValue(SensorName.CITIZEN_ID, citizenId, "N/A");
    }
    
    public void publishTrainingId(String trainingId) {
        publishMqttStringValue(SensorName.TRAINING_ID, trainingId, "N/A");
    }
    
    private void publishMqttValue(SensorName sensorName, float value, String units) {
        try {
            MedxEvent medxEvent = new MedxEvent();
            medxEvent.setName(sensorName.name());
            medxEvent.setTime(Instant.now().toEpochMilli());
            medxEvent.setUnits(units);
            medxEvent.setValue(value);
            
            MedxEvent medxEvents[] = new MedxEvent[1];
            medxEvents[0] = medxEvent;
            
            MqttMessage mqttMessage = new MqttMessage(medxGsonUtil.toJson(medxEvents).getBytes());
            mqttClient.publish(mqttPublishTopic, mqttMessage);
        } catch (MqttException ex) {
            MEDX_MQTT_LOGGER.error("Error publishing value: " + sensorName.name() + "= " + ex.getMessage());
        }
    }
    
    private void publishMqttStringValue(SensorName sensorName, String stringValue, String units) {
        try {
            MedxEvent medxEvent = new MedxEvent();
            medxEvent.setName(sensorName.name());
            medxEvent.setTime(Instant.now().toEpochMilli());
            medxEvent.setUnits(units);
            medxEvent.setStringValue(stringValue);
            
            MedxEvent medxEvents[] = new MedxEvent[1];
            medxEvents[0] = medxEvent;
            
            MqttMessage mqttMessage = new MqttMessage(medxGsonUtil.toJson(medxEvents).getBytes());
            mqttClient.publish(mqttPublishTopic, mqttMessage);
        } catch (MqttException ex) {
            MEDX_MQTT_LOGGER.error("Error publishing string value: " + sensorName.name() + "= " + ex.getMessage());
        }
    }
    
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Not implemented using QoS = 0;
        //MEDX_MQTT_LOGGER.warn("DeliveryComplete: Token = " + token);
    }
    
    public void stopClient() throws MedxMqttClientException {
        try {
            mqttClient.disconnect();
            mqttClient.close();
        } catch (MqttException ex) {
            throw new MedxMqttClientException(ex.getMessage());
        }
    }
    
    @Override
    public void connectionLost(Throwable cause) {
        MEDX_MQTT_LOGGER.error("The MQTT connection is lost = " + cause.getMessage());
    }
}
