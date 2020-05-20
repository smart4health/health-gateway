package pt.uninova.s4h.hub.medx.main;

import ch.qos.logback.classic.Level;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import pt.uninova.s4h.hub.kbz.mqtt.KbzMqttClient;
import pt.uninova.s4h.hub.medx.ittm.api.IttmApi;
import pt.uninova.s4h.hub.medx.logging.LoggerUtils;
import pt.uninova.s4h.hub.medx.mqtt.MedxMqttClient;
import pt.uninova.s4h.hub.medx.physiotherapy.FXMLDocumentController;
import pt.uninova.s4h.hub.medx.physiotherapy.PhysiotherapyGui;

/**
 *
 * @author Vasco Delgado-Gomes
 */
public class MedxHubMain {

    private static final ch.qos.logback.classic.Logger MAIN_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PROP_FILE_NAME = "HubConfig.properties";
    private static final String LOGGING_LEVEL = "logging_level";
    private static final String MQTT_SUBSCRIBE_TOPICS = "mqtt_subscribe_topics";
    private static final String MQTT_PUBLISH_TOPIC = "mqtt_publish_topic";
    private static final String MQTT_PUBLISH_INTERVAL = "mqtt_publish_interval";
    private static final String MQTT_BROKER_URL = "mqtt_broker_url";
    private static final String MQTT_CLIENT_ID = "mqtt_client_id";
    private static final String ITTM_HOST = "ittm_host";
    private static final String ITTM_MACHINE_ID = "ittm_machine_id";
//    private static final String ITTM_USERNAME = "ittm_username";
//    private static final String ITTM_PASSWORD = "ittm_password";

    private static final String KBZ_MQTT_SUBSCRIBE_TOPICS = "kbz_mqtt_subscribe_topics";
    private static final String KBZ_MQTT_PUBLISH_TOPIC = "kbz_mqtt_publish_topic";
    private static final String KBZ_MQTT_BROKER_URL = "kbz_mqtt_broker_url";
    private static final String KBZ_MQTT_CLIENT_ID = "kbz_mqtt_client_id";

    static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        Properties prop = new Properties();
        try {

            boolean deployment = false;
            String saveDirectory;
            String jarLocation = new File(MedxHubMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
            if (!deployment) {
                prop.load(FXMLDocumentController.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME));
            } else {
                prop.load(new FileInputStream(jarLocation + "\\" + PROP_FILE_NAME));
            }

            String loggingLevel = prop.getProperty(LOGGING_LEVEL);
            String mqttSubscribeTopics = prop.getProperty(MQTT_SUBSCRIBE_TOPICS);
            String mqttPublishTopic = prop.getProperty(MQTT_PUBLISH_TOPIC);
            int mqttPublishInterval = Integer.parseInt(prop.getProperty(MQTT_PUBLISH_INTERVAL));
            String mqttBrokerUrl = prop.getProperty(MQTT_BROKER_URL);
            String mqttClientId = prop.getProperty(MQTT_CLIENT_ID);
            String ittmHost = prop.getProperty(ITTM_HOST);
            String ittmMachineID = prop.getProperty(ITTM_MACHINE_ID);
//            String ittmUsername = prop.getProperty(ITTM_USERNAME);
//            String ittmPassword = prop.getProperty(ITTM_PASSWORD);
            LoggerUtils.setRootLoggingConsole(Level.toLevel(loggingLevel, Level.WARN));

            String kbzMqttSubscribeTopics = prop.getProperty(KBZ_MQTT_SUBSCRIBE_TOPICS);
            String kbzMqttPublishTopic = prop.getProperty(KBZ_MQTT_PUBLISH_TOPIC);
            String kbzMqttBrokerUrl = prop.getProperty(KBZ_MQTT_BROKER_URL);
            String kbzMqttClientId = prop.getProperty(KBZ_MQTT_CLIENT_ID);

            MAIN_LOGGER.info(LOGGING_LEVEL + " = " + loggingLevel);
            MAIN_LOGGER.info(MQTT_SUBSCRIBE_TOPICS + " = " + mqttSubscribeTopics);
            MAIN_LOGGER.info(MQTT_PUBLISH_TOPIC + " = " + mqttPublishTopic);
            MAIN_LOGGER.info(MQTT_PUBLISH_INTERVAL + " = " + mqttPublishInterval);
            MAIN_LOGGER.info(MQTT_BROKER_URL + " = " + mqttBrokerUrl);
            MAIN_LOGGER.info(MQTT_CLIENT_ID + " = " + mqttClientId);
            MAIN_LOGGER.info(ITTM_HOST + " = " + ittmHost);
            MAIN_LOGGER.info(ITTM_MACHINE_ID + " = " + ittmMachineID);
            //MAIN_LOGGER.info(ITTM_USERNAME + " = " + ittmUsername);
            //MAIN_LOGGER.info(ITTM_PASSWORD + " = " + ittmPassword);

            MAIN_LOGGER.info(KBZ_MQTT_SUBSCRIBE_TOPICS + " = " + kbzMqttSubscribeTopics);
            MAIN_LOGGER.info(KBZ_MQTT_PUBLISH_TOPIC + " = " + kbzMqttPublishTopic);
            MAIN_LOGGER.info(KBZ_MQTT_BROKER_URL + " = " + kbzMqttBrokerUrl);
            MAIN_LOGGER.info(KBZ_MQTT_CLIENT_ID + " = " + kbzMqttClientId);

            String OS = (System.getProperty("os.name")).toUpperCase();
            if (OS.contains("WIN")) {
                saveDirectory = System.getenv("APPDATA") + "\\MedX_MQTT_Files\\";
            } else {
                saveDirectory = System.getProperty("user.home");
                saveDirectory += "/Library/Application Support/MedX_MQTT_Files/";
            }
            boolean result = deleteDirectory(new File(saveDirectory));
            MedxMqttClient.getFirstInstance(mqttSubscribeTopics.split(";"), mqttPublishTopic, mqttBrokerUrl, mqttClientId, saveDirectory);
            IttmApi.getFirstInstance(ittmHost, ittmMachineID);
            KbzMqttClient.getFirstInstance(kbzMqttSubscribeTopics.split(";"), kbzMqttPublishTopic, kbzMqttBrokerUrl, kbzMqttClientId, saveDirectory).initialise();

            String[] guiMain = {ittmHost};           
            new PhysiotherapyGui().main(guiMain);
        } catch (Exception ex) {
            MAIN_LOGGER.error(ex.getMessage());
            System.exit(-1);
        }
    }

}
