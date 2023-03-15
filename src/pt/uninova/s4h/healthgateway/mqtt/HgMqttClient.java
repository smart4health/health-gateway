package pt.uninova.s4h.healthgateway.mqtt;

import java.lang.invoke.MethodHandles;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.LoggerFactory;
import pt.uninova.s4h.healthgateway.senml.MedxEvent;
import pt.uninova.s4h.healthgateway.senml.MedxGsonUtil;
import pt.uninova.s4h.healthgateway.util.listeners.EventDispatcher;
import pt.uninova.s4h.healthgateway.util.message.HubHgMessage;
import pt.uninova.s4h.healthgateway.util.message.MessagesUtil.EventMessageType;

/**
 * Class to implement a MQTT client.
 */
public class HgMqttClient implements MqttCallbackExtended {

    private static final ch.qos.logback.classic.Logger HG_MQTT_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String directory;
    private final String mqttPublishTopic;
    private final String[] mqttSubscribeTopics;
    private final String mqttBrokerUrl;
    private final String mqttClientId;
    private MedxGsonUtil medxGsonUtil = null;
    private MqttClient mqttClient = null;
    private static HgMqttClient hgMqttClient = null;
    private final EventDispatcher<HubHgMessage> onHubHgMessage;

    public EventDispatcher<HubHgMessage> onHubHgMessage() {
        return onHubHgMessage;
    }

    private HgMqttClient(String[] mqttSubscribeTopics, String mqttPublishTopic, String mqttBrokerUrl, String mqttClientId, String directory) {
        this.mqttSubscribeTopics = mqttSubscribeTopics;
        this.mqttPublishTopic = mqttPublishTopic;
        this.mqttBrokerUrl = mqttBrokerUrl;
        this.mqttClientId = mqttClientId;
        this.medxGsonUtil = new MedxGsonUtil();
        this.directory = directory;
        this.onHubHgMessage = new EventDispatcher<>();
    }

    public static synchronized HgMqttClient getFirstInstance(String[] mqttSubscribeTopics, String mqttPublishTopic, String mqttBrokerUrl, String mqttClientId, String directory) {
        if (hgMqttClient == null) {
            hgMqttClient = new HgMqttClient(mqttSubscribeTopics, mqttPublishTopic, mqttBrokerUrl, mqttClientId, directory);
        }
        return hgMqttClient;
    }

    public static synchronized HgMqttClient getInstance() {
        return hgMqttClient;
    }

    public void initialise() throws HgMqttClientException {
        try {
            MqttDefaultFilePersistence mqttDefaultFilePersistence = new MqttDefaultFilePersistence(directory);
            mqttClient = new MqttClient(mqttBrokerUrl, mqttClientId, null);//mqttDefaultFilePersistence);
            mqttClient.setCallback(this);
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setMaxInflight(100000);
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttClient.connect(mqttConnectOptions);
        } catch (MqttException ex) {
            HG_MQTT_LOGGER.error("Error starting MEDX MQTT Client= " + ex.getMessage());
            throw new HgMqttClientException(ex.getMessage());
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String messageString = new String(mqttMessage.getPayload());
        HG_MQTT_LOGGER.debug("New message: " + messageString);
        HG_MQTT_LOGGER.debug("Topic: " + topic);
        try {
            MedxEvent[] medxEvents = medxGsonUtil.fromJson(messageString);
            for (MedxEvent medxEvent : medxEvents) {
                try {
                    onHubHgMessage.dispatch(new HubHgMessage(EventMessageType.valueOf(medxEvent.getName()), medxEvent.getStringValue(), medxEvent.getValue()));
                } catch (Exception ex) {
                    HG_MQTT_LOGGER.debug("Invalid Sensor Name or message value is null");
                }
            }
        } catch (Exception ex) {
            HG_MQTT_LOGGER.error("Messaged Arrived Exception = " + ex.getMessage());
        }
    }

    public void publishMedxEvents(MedxEvent medxEvents[]) {
        try {
            MqttMessage mqttMessage = new MqttMessage(medxGsonUtil.toJson(medxEvents).getBytes());
            mqttClient.publish(mqttPublishTopic, mqttMessage);
        } catch (MqttException ex) {
            HG_MQTT_LOGGER.error("Error publishing MedX Events = " + ex.getMessage());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Not implemented using QoS = 0;
    }

    public void stopClient() throws HgMqttClientException {
        try {
            mqttClient.disconnect();
            mqttClient.close();
        } catch (MqttException ex) {
            throw new HgMqttClientException(ex.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        HG_MQTT_LOGGER.error("The MQTT connection is lost = " + cause.getMessage());
    }
    
    public boolean isConnected(){
        return mqttClient.isConnected();
    }

    @Override
    public void connectComplete(boolean bln, String string) {
        try {
            for (String mqttSubscribeTopic : mqttSubscribeTopics) {
                    HG_MQTT_LOGGER.debug("Subscribing to the following topics:");
                    mqttClient.subscribe(mqttSubscribeTopic);
                    HG_MQTT_LOGGER.info(mqttSubscribeTopic);
            }
            HG_MQTT_LOGGER.info("== End.");
        } catch (MqttException ex) {
            HG_MQTT_LOGGER.error("Error starting MEDX MQTT Client= " + ex.getMessage());
        }
    }
}