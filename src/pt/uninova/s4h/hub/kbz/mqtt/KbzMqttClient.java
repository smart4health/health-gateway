package pt.uninova.s4h.hub.kbz.mqtt;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.LoggerFactory;
import pt.uninova.s4h.hub.kbz.senml.MedxEvent;
import pt.uninova.s4h.hub.kbz.senml.MedxGsonUtil;
import pt.uninova.s4h.hub.kbz.gamification.Ghost;
//import pt.uninova.s4h.hub.kbz.gamification.Consts;

/**
 * Class to implement a MQTT client.
 *
 * @author Vasco Delgado-Gomes
 * @email vmdg@uninova.pt
 * @version 07 September 2019 - First version.
 */
public class KbzMqttClient implements MqttCallback {

    public enum SensorName {
        ANGLE, FORCE, START_TRAINING, STOP_TRAINING, TIMES, IMAGE, MESSAGE;
    }

    private static final ch.qos.logback.classic.Logger KBZ_MQTT_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String[] mqttSubscribeTopics;
    private final String mqttPublishTopic;
    private final String mqttBrokerUrl;
    private final String mqttClientId;
    private MedxGsonUtil medxGsonUtil = null;
    private MqttClient mqttClient = null;
    private Ghost ghost;
    //private String fullMessage = "";
    private final String directory;

    private static KbzMqttClient kbzMqttClient = null;

    public KbzMqttClient(String[] mqttSubscribeTopics, String mqttPublishTopic, String mqttBrokerUrl, String mqttClientId, String directory) {
        this.mqttSubscribeTopics = mqttSubscribeTopics;
        this.mqttPublishTopic = mqttPublishTopic;
        this.mqttBrokerUrl = mqttBrokerUrl;
        this.mqttClientId = mqttClientId;
        this.medxGsonUtil = new MedxGsonUtil();
        this.directory = directory;
    }

    public static synchronized KbzMqttClient getFirstInstance(String[] mqttSubscribeTopics, String mqttPublishTopic, String mqttBrokerUrl, String mqttClientId, String directory) {
        if (kbzMqttClient == null) {
            kbzMqttClient = new KbzMqttClient(mqttSubscribeTopics, mqttPublishTopic, mqttBrokerUrl, mqttClientId, directory);
        }
        return kbzMqttClient;
    }

    public static synchronized KbzMqttClient getInstance() {
        return kbzMqttClient;
    }

    public void initialise() throws KbzMqttClientException {

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
                KBZ_MQTT_LOGGER.debug("Subscribing to the following topics:");
                mqttClient.subscribe(mqttSubscribeTopic);
                KBZ_MQTT_LOGGER.info(mqttSubscribeTopic);
            }
            KBZ_MQTT_LOGGER.info("== End.");

        } catch (MqttException ex) {
            KBZ_MQTT_LOGGER.error("Error starting MEDX MQTT Client= " + ex.getMessage());
            throw new KbzMqttClientException(ex.getMessage());
        }

        ghost = new Ghost(this);

        //TODO initialize Ghost*/
        //ghost.initialize(Consts.startDegree, Consts.endDegree,0,0,0);
        //ghost.initialize(67, 0,5000,5000,1000);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

        String messageString = new String(mqttMessage.getPayload());

        KBZ_MQTT_LOGGER.debug("New message: " + messageString);
        KBZ_MQTT_LOGGER.debug("Topic: " + topic);
        try {
            MedxEvent[] medxEvents = medxGsonUtil.fromJson(messageString);
            for (MedxEvent medxevent : medxEvents) {
                try {
                    switch (SensorName.valueOf(medxevent.getName().toUpperCase())) {
                        case START_TRAINING:
                            //ghost.startTraining();
                            break;
                        case STOP_TRAINING:
                            ghost.stopTraining();
                            break;
                        case TIMES:
                            String[] initMessage = medxevent.getStringValue().split(";");
                            ghost.initializeTraining(Integer.parseInt(initMessage[0]), Integer.parseInt(initMessage[1]), Integer.parseInt(initMessage[2]), Integer.parseInt(initMessage[3]), Integer.parseInt(initMessage[4]));
                            break;
                        case ANGLE:
                            //TODO Proteger para o caso de nao ter o initialize antes.
                            ghost.gamificationOrchestrator(medxevent.getValue());
                            break;
                        case FORCE:
                        default:
                        //Do nothing;
                    }
                } catch (Exception ex) {
                    KBZ_MQTT_LOGGER.debug("Invalid Sensor Name");
                }
            }
        } catch (Exception ex) {
            KBZ_MQTT_LOGGER.error("Messaged Arrived Exception = " + ex.getMessage());
        }
    }

    public void publishKbzMessage(String kbzMessage) {
        try {
            MedxEvent medxEvent = new MedxEvent();
            medxEvent.setName(SensorName.MESSAGE.name());
            medxEvent.setTime(Instant.now().toEpochMilli());
            medxEvent.setUnits("N/A"); //Not used
            medxEvent.setStringValue(kbzMessage);

            MedxEvent medxEvents[] = new MedxEvent[1];
            medxEvents[0] = medxEvent;

            MqttMessage mqttMessage = new MqttMessage(medxGsonUtil.toJson(medxEvents).getBytes());
            mqttClient.publish(mqttPublishTopic, mqttMessage);
        } catch (MqttException ex) {
            KBZ_MQTT_LOGGER.error("publishAngleMeasuremente= " + ex.getMessage());
        }
    }

    public void publishKbzImage(String kbzImage) {
        try {
            MedxEvent medxEvent = new MedxEvent();
            medxEvent.setName(SensorName.IMAGE.name());
            medxEvent.setTime(Instant.now().toEpochMilli());
            medxEvent.setUnits("N/A"); //Not used
            medxEvent.setStringValue(kbzImage);

            MedxEvent medxEvents[] = new MedxEvent[1];
            medxEvents[0] = medxEvent;

            MqttMessage mqttMessage = new MqttMessage(medxGsonUtil.toJson(medxEvents).getBytes());
            mqttClient.publish(mqttPublishTopic, mqttMessage);
        } catch (MqttException ex) {
            KBZ_MQTT_LOGGER.error("publishKbzImage= " + ex.getMessage());
        }
    }

    public void publishKbzImageAndMessage(String kbzImage, String kbzMessage) {
        try {
            MedxEvent medxEventImage = new MedxEvent();
            medxEventImage.setName(SensorName.IMAGE.name());
            medxEventImage.setTime(Instant.now().toEpochMilli());
            medxEventImage.setUnits("N/A"); //Not used
            medxEventImage.setStringValue(kbzImage);

            MedxEvent medxEventMessage = new MedxEvent();
            medxEventMessage.setName(SensorName.MESSAGE.name());
            medxEventMessage.setTime(Instant.now().toEpochMilli());
            medxEventMessage.setUnits("N/A"); //Not used
            medxEventMessage.setStringValue(kbzMessage);

            MedxEvent medxEvents[] = new MedxEvent[2];
            medxEvents[0] = medxEventImage;
            medxEvents[1] = medxEventMessage;

            MqttMessage mqttMessage = new MqttMessage(medxGsonUtil.toJson(medxEvents).getBytes());
            mqttClient.publish(mqttPublishTopic, mqttMessage);
        } catch (MqttException ex) {
            KBZ_MQTT_LOGGER.error("publishKbzImageAndMessage= " + ex.getMessage());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Not implemented using QoS = 0;
        //KBZ_MQTT_LOGGER.warn("DeliveryComplete: Token = " + token);
    }

    public void stopClient() throws KbzMqttClientException {
        try {
            mqttClient.disconnect();
            mqttClient.close();
        } catch (MqttException ex) {
            throw new KbzMqttClientException(ex.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        KBZ_MQTT_LOGGER.error("The MQTT connection is lost = " + cause.getMessage());
    }
}
