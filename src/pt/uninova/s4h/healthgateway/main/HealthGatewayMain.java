package pt.uninova.s4h.healthgateway.main;

import ch.qos.logback.classic.Level;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import pt.uninova.s4h.healthgateway.gui.FXMLDocumentController;
import pt.uninova.s4h.healthgateway.gui.PhysiotherapyGui;
import pt.uninova.s4h.healthgateway.ittm.api.IttmApi;
import pt.uninova.s4h.healthgateway.box.api.BoxApi;
import pt.uninova.s4h.healthgateway.mqtt.HgMqttClient;
import pt.uninova.s4h.healthgateway.util.logging.LoggerUtils;

/**
 * Main class of the Health Gateway project.
 */
public class HealthGatewayMain {

    private static final ch.qos.logback.classic.Logger MAIN_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PROP_FILE_NAME = "HealthGatewayConfig.properties";
    private static final String LOGGING_LEVEL = "logging_level";
    private static final String MQTT_SUBSCRIBE_TOPICS = "mqtt_subscribe_topics";
    private static final String MQTT_PUBLISH_TOPIC = "mqtt_publish_topic";
    private static final String MQTT_PUBLISH_INTERVAL = "mqtt_publish_interval";
    private static final String MQTT_BROKER_URL = "mqtt_broker_url";
    private static final String MQTT_CLIENT_ID = "mqtt_client_id";
    private static final String ITTM_HOST = "ittm_host";
    private static final String ITTM_MACHINE_ID = "ittm_machine_id";
    private static final String HUB_SENSORIZATION = "hub_sensorization";
    private static final String LOAD_CELL = "load_cell";
    private static final String BOX_HOST = "box_host";     

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
        Properties prop = new Properties();
        try {
            boolean deployment = false;
            String saveDirectory;
            String jarLocation = new File(HealthGatewayMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
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
            String hubSensorization = prop.getProperty(HUB_SENSORIZATION);
            String loadcell = prop.getProperty(LOAD_CELL);
            String boxHost = prop.getProperty(BOX_HOST);
            LoggerUtils.setRootLoggingConsoleAndFile(Level.toLevel(loggingLevel, Level.WARN), Level.WARN);
            MAIN_LOGGER.info(LOGGING_LEVEL + " = " + loggingLevel);
            MAIN_LOGGER.info(MQTT_SUBSCRIBE_TOPICS + " = " + mqttSubscribeTopics);
            MAIN_LOGGER.info(MQTT_PUBLISH_TOPIC + " = " + mqttPublishTopic);
            MAIN_LOGGER.info(MQTT_PUBLISH_INTERVAL + " = " + mqttPublishInterval);
            MAIN_LOGGER.info(MQTT_BROKER_URL + " = " + mqttBrokerUrl);
            MAIN_LOGGER.info(MQTT_CLIENT_ID + " = " + mqttClientId);
            MAIN_LOGGER.info(ITTM_HOST + " = " + ittmHost);
            MAIN_LOGGER.info(ITTM_MACHINE_ID + " = " + ittmMachineID);
            MAIN_LOGGER.info(HUB_SENSORIZATION + " = " + hubSensorization);
            MAIN_LOGGER.info(LOAD_CELL + " = " + loadcell);
            MAIN_LOGGER.info(BOX_HOST + " = " + boxHost);
            String OS = (System.getProperty("os.name")).toUpperCase();
            if (OS.contains("WIN")) {
                saveDirectory = System.getenv("APPDATA") + "\\HG_Files\\";
            } else {
                saveDirectory = System.getProperty("user.home");
                saveDirectory += "/Library/Application Support/HG_Files/";
            }
            File file = new File(saveDirectory);
            if (!file.exists()) {
                if (file.mkdir()) {
                    MAIN_LOGGER.debug("Directory is created!");
                } else {
                    MAIN_LOGGER.debug("Failed to create directory!");
                }
            }
            MAIN_LOGGER.debug("The current working directory is " + saveDirectory);
            HgMqttClient.getFirstInstance(mqttSubscribeTopics.split(";"), mqttPublishTopic, mqttBrokerUrl, mqttClientId, saveDirectory);
            IttmApi.getFirstInstance(ittmHost, ittmMachineID, saveDirectory);
            BoxApi.getFirstInstance(boxHost);
            PhysiotherapyGui gui = new PhysiotherapyGui();
            gui.setParm(ittmMachineID,hubSensorization,loadcell,saveDirectory);
            gui.main(args);
        } catch (Exception ex) {
            MAIN_LOGGER.error(ex.getMessage());
            System.exit(-1);
        }
    }

}
