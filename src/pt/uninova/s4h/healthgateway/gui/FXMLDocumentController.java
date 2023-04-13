package pt.uninova.s4h.healthgateway.gui;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Marker;
import eu.hansolo.medusa.Marker.MarkerType;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.LoggerFactory;
import pt.uninova.s4h.healthgateway.ittm.api.IttmApi;
import pt.uninova.s4h.healthgateway.ittm.api.IttmApi500Exception;
import pt.uninova.s4h.healthgateway.ittm.api.IttmApiException;
import pt.uninova.s4h.healthgateway.ittm.api.IttmApiTokenException;
import pt.uninova.s4h.healthgateway.ittm.manager.IttmTrainingManager;
import pt.uninova.s4h.healthgateway.ittm.message.force.ForceTestData;
import pt.uninova.s4h.healthgateway.ittm.message.force.ForceTestRequestJson;
import pt.uninova.s4h.healthgateway.ittm.message.parameters.ParametersGsonUtil;
import pt.uninova.s4h.healthgateway.ittm.message.parameters.ParametersResponseJson;
import pt.uninova.s4h.healthgateway.mqtt.HgMqttClient;
import pt.uninova.s4h.healthgateway.mqtt.HgMqttClientException;
import pt.uninova.s4h.healthgateway.util.listeners.EventDispatcher;
import pt.uninova.s4h.healthgateway.util.listeners.HgHubMessageListener;
import pt.uninova.s4h.healthgateway.util.listeners.HubHgMessageListener;
import pt.uninova.s4h.healthgateway.util.message.HgHubMessage;
import pt.uninova.s4h.healthgateway.util.message.MessagesUtil.EventMessageType;

/**
 * Class to control the interface and perform user commands.
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private Label calibanglesteps;
    @FXML
    private Label calibforcesteps;
    @FXML
    private Label titleLabel;
    @FXML
    private Label homeUserLabel;
    @FXML
    private Label homeCalibrationLabel;
    @FXML
    private Label homeCounterweightLabel;
    @FXML
    private Label homeExitLabel;
    @FXML
    private Label homeIttmLabel;
    @FXML
    private Label homeExerciseLabel;
    @FXML
    private Label homeIsometricLabel;
    @FXML
    private Label homeConnectLabel;
    @FXML
    private Circle CircleConnect;
    @FXML
    private ImageView ImageConnect;
    @FXML
    private Label tabConnectTitle;
    @FXML
    private Label communicationLabel;
    @FXML
    private Label dataAcquisitionLabel;
    @FXML
    private Label sampleTimeLabel;
    @FXML
    private Label tabUserTitle;
    @FXML
    private Label userIdLabel;
    @FXML
    private Label trainIdLabel;
    @FXML
    private Label forceTesteIdLabel;
    @FXML
    private Label extendedAngleLabel;
    @FXML
    private Label flexedAngleLabel;
    @FXML
    private Label timeELabel;
    @FXML
    private Label timeFLabel;
    @FXML
    private Label timeHLabel;
    @FXML
    private Label calibrationTitleLabel;
    @FXML
    private Label angleCalibrationLabel;
    @FXML
    private Label pressureCalibrationLabel;
    @FXML
    private Label extensionAngleLabel;
    @FXML
    private Label intAngleLabel;
    @FXML
    private Label flexionAngleLabel;
    @FXML
    private Label anglecountLabel;
    @FXML
    private Label citizencountLabel;
    @FXML
    private Label CountCitizenLabel;
    @FXML
    private Label forceCountLabel;
    @FXML
    private Label IsoExtensionAngleLabel;
    @FXML
    private Label IsoAngleLabel;
    @FXML
    private Label IsoFlexionAngleLabel;
    @FXML
    private Label IsoForceLabel;
    @FXML
    private Label exerciseAngleLabel;
    @FXML
    private Label exerciseForceLabel;
    @FXML
    private ComboBox<String> LanguageList;
    @FXML
    private TabPane MenuTabpane;
    @FXML
    private Tab tabHome;
    @FXML
    private Tab tabConnect;
    @FXML
    private Tab tabUser;
    @FXML
    private Tab tabCalib;
    @FXML
    private Tab tabIttm;
    @FXML
    private GridPane boxConnect;
    @FXML
    private HBox boxAquisition;
    @FXML
    private ProgressIndicator ProgressConnect;
    @FXML
    private ProgressIndicator ProgressConnectHome;
    @FXML
    private Button AutoConnectButton;
    @FXML
    private Button ConnectButton;
    @FXML
    private Button DisconnectButton;
    @FXML
    private Spinner<Integer> startSpinner;
    @FXML
    private Button StartButton;
    @FXML
    private Button StopButton;
    @FXML
    private GridPane AngleCaliGrid;
    @FXML
    private Button ExtensionAngleButton;
    @FXML
    private Button FlexionAngleButton;
    @FXML
    private Button IntAngleButton;
    @FXML
    private Label ExtensionAngleLabel;
    @FXML
    private Label FlexionAngleLabel;
    @FXML
    private Label IntAngleLabel;
    @FXML
    private Button PressureButton;
    @FXML
    private Label PressureLabel;
    @FXML
    private TextField UserIdText;
    @FXML
    private TextField TrainIdText;
    @FXML
    private TextField ForceTestIdText;
    @FXML
    private Spinner<Integer> UserAngleExtension;
    @FXML
    private Spinner<Integer> UserAngleFlexion;
    @FXML
    private ImageView lockImageCalibration;
    @FXML
    private ImageView lockImageConnect;
    @FXML
    private Tab tabCounter;
    @FXML
    private Label CountAngleLabel;
    @FXML
    private Label CountInstructionLabel;
    @FXML
    private Label CountForceLabel;
    @FXML
    private Tab tabIsometric;
    @FXML
    private HBox IsoBox;
    @FXML
    private Label IsoExtensionAngleText;
    @FXML
    private Label IsoAngleText;
    @FXML
    private Label IsoFlexionAngleText;
    @FXML
    private Label IsoForceText;
    @FXML
    private Button IsometricStartButton;
    @FXML
    private Button IsometricCancelButton;
    @FXML
    private Button IsometricFinishButton;
    @FXML
    private Tab tabExercise;
    @FXML
    private Gauge ForceGauge;
    @FXML
    private Gauge AngleGauge;
    @FXML
    private Button StartExerciseButton;
    @FXML
    private Gauge AngleGauge1;
    @FXML
    private ImageView ExerciseImage;
    @FXML
    private ImageView MachineImage;    
    @FXML
    private Label ExerciseText;
    @FXML
    private GridPane KbzGrid;
    @FXML
    private TextField ExerciseTimeLabel;
    @FXML
    private TextField RepetitionTimeLabel;
    @FXML
    private TextField RepetitionNumberLabel;
    @FXML
    private Label LabelExerciseTime;
    @FXML
    private Label LabelRepetitionTime;
    @FXML
    private Label LabelRepetitionNumber;
    @FXML
    private Button StopExerciseButton;
    @FXML
    private TextField AngleLabel;
    @FXML
    private TextField ForceLabel;
    @FXML
    private Label citizenExerciseLabel;
    @FXML
    private Label ExerciseCitizenLabel;
    @FXML
    private Label trainExerciseLabel;
    @FXML
    private Label ExerciseTrainLabel;
    @FXML
    private Spinner<Integer> UserExtensionTime;
    @FXML
    private Spinner<Integer> UserFlexionTime;
    @FXML
    private Spinner<Integer> UserHoldTime;
    @FXML
    private Spinner<Integer> SessionNumberText;
    @FXML
    private Label sessionNumberLabel;
    @FXML
    private Spinner<Integer> TrainingNumberText;
    @FXML
    private Label trainNumberLabel;
    @FXML
    private Label CounterweightUserLabel;
    @FXML
    private Spinner<Integer> CounterweightUserText;
    @FXML
    private Label trainWeightLabel;
    @FXML
    private Spinner<Integer> TrainingWeightText;
    @FXML
    private Label zeroPositionLabel;
    @FXML
    private Spinner<Integer> ZeroPositionText;
    @FXML
    private Label TightPositionLabel;
    @FXML
    private Spinner<Integer> TightPositionText;
    @FXML
    private Label seatCushionLabel;
    @FXML
    private TextField SeatCushionText;
    @FXML
    private Button UserButton;
    @FXML
    private Label infoLabelTitle;
    @FXML
    private Label infoLabel;
    @FXML
    private Label infoLabel2;
    @FXML
    private Label infoLabel3;
    @FXML
    private Tab tabMovement;
    @FXML
    private Label homeMovementLabel;
    @FXML
    private Label citizenMovementLabel;
    @FXML
    private Label movementCitizenLabel;
    @FXML
    private Label angleMovementLabel;
    @FXML
    private Label movementAngleLabel;
    @FXML
    private Spinner<Integer> movementExtensionLabel;
    @FXML
    private Spinner<Integer> movementZeroLabel;
    @FXML
    private Spinner<Integer> movementFlexionLabel;
    @FXML
    private Label extensionMovementLabel;
    @FXML
    private Label zeroMovementLabel;
    @FXML
    private Label flexionMovementLabel;
    @FXML
    private Label instructionMovementLabel;
    @FXML
    private Label movementTitleLabel;
    @FXML
    private Button movementBackButton;
    @FXML
    private Button movementNextButton;
    @FXML
    private Button movementCancelButton;
    @FXML
    private Label countTitleLabel;
    @FXML
    private Label CountZeroLabel;
    @FXML
    private Label CountExtensionLabel;
    @FXML
    private Button countBackButton;
    @FXML
    private Button countNextButton;
    @FXML
    private Button countCancelButton;
    @FXML
    private Label zeroCountLabel;
    @FXML
    private Label extensionCountLabel;
    @FXML
    private Tab tabInfo;
    @FXML
    private Button WarmUpButton;     
    @FXML
    private Pane exercisePane; 
    @FXML
    private Label HeadBarL;
    @FXML
    private Label HeadBarR;
    @FXML
    private Label HandsBarL;
    @FXML
    private Label HandsBarR;    
    @FXML
    private Label BackBar;
    @FXML
    private Label LegsBarL;
    @FXML
    private Label LegsBarR;
    @FXML
    private Label FootBarL;
    @FXML
    private Label FootBarR;    
    @FXML
    private Rectangle HeadCircleL;
    @FXML
    private Rectangle HeadCircleR;   
    @FXML
    private Rectangle HandsCircleL;  
    @FXML
    private Rectangle HandsCircleR;      
    @FXML
    private Rectangle BackCircle;
    @FXML
    private Rectangle LegsCircleL;
    @FXML
    private Rectangle LegsCircleR;    
    @FXML
    private Rectangle FootCircleL;
    @FXML
    private Rectangle FootCircleR;    
    @FXML
    private Rectangle HeadCircleL1;
    @FXML
    private Rectangle HeadCircleR1;   
    @FXML
    private Rectangle HandsCircleL1;  
    @FXML
    private Rectangle HandsCircleR1;      
    @FXML
    private Rectangle BackCircle1;   
    private static final ch.qos.logback.classic.Logger VIEWER_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private IttmApi ittmApi = null;
    private ResourceBundle bundle;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static String SAVE_DIRECTORY = "";
    private final static String FILENAME = "config.txt";
    private String ittmUser = "";
    private String calibrationTime = "1990-01-01";
    private boolean connectLogin = false;
    private Timer timerConnection;
    private boolean connectionRequest = false;
    private final long CONN_DELAY = 10000;
    private final int A_FLEXION = (byte) 11;
    private final int A_EXTENSION = (byte) 21;
    private final int A_INT = (byte) 23;
    private final int PRESSURE = (byte) 43;
    private final int AUTOSTART = (byte) 50;
    private final int CONNECT = (byte) 52;
    private final int START = (byte) 51;
    private final int STOP = (byte) 61;
    private final short ANGLE_FLEXION = (short) 72.0;
    private final short ANGLE_EXTENSION = (short) 0.0;
    private final short ANGLE_INT = (short) 36.0;
    private Timer timerConf;
    private final int CONF_DELAY = 5000;
    private int stateConf = 0;
    private boolean calibrationLogin = false;
    private int stateMovement = 1;
    private int flexionUserAngle = 0;
    private int extensionUserAngle = 0;
    private int zeroUserAngle = 0;
    private int stateCount = 1;
    private int counterValue = 0;
    private int stateIso = 1;
    private List<Integer> IsoList = new ArrayList<>();
    private final List<Integer> ForceMax = new ArrayList<>();
    private final BarChartIso<String, Number> barchartiso = new BarChartIso(new CategoryAxis(), new NumberAxis());
    private boolean IsoChart = false;
    private int IsoPosition = -1;
    private boolean exercise = false;
    private boolean exercisedone = false;
    private int userAngle = 0;
    private int userForce = 0;
    private boolean healthMonitor = false;
    private String pc_id;
    private String hub_sensorization;
    private boolean loadcell = false;
    private String citizenForce = "";
    private String citizenExercise = "";
    private String citizenID = "";

    public FXMLDocumentController(String pcid, String sensorization, String loadcell, String directory) {
        pc_id = pcid;
        hub_sensorization = sensorization;
        if (loadcell.equals("4mv")){
            this.loadcell = true;
        }
        SAVE_DIRECTORY = directory;
    }

//------------------- Main Login ----------------------------------------------      
    public void lockMain(boolean login) {
        MenuTabpane.setDisable(true);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLoginMain.fxml"), bundle);
            FXMLLoginControllerMain fxmllogin = new FXMLLoginControllerMain(this, login);
            loader.setController(fxmllogin);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon_s4h.png")));
            stage.setTitle("Health Gateway Login");
            stage.showAndWait();
        } catch (IOException ex) {
            VIEWER_LOGGER.error("Error Login Main root: " + ex);
        }
    }

    public void unlockMain(boolean healthMonitor) {
        MenuTabpane.setDisable(false);
        healthMonitorConnected(healthMonitor);
    }

    public String getIttmUser() {
        return ittmUser;
    }

    public void setIttmUser(String user) {
        ittmUser = user;
        WriteConfig();
    }

//------------------- Config File ----------------------------------------------    
    void ReadConfig() {
        try (BufferedReader br = new BufferedReader(new FileReader(SAVE_DIRECTORY + FILENAME))) {
            if (br.ready()) {
                calibrationTime = br.readLine();
            }
            if (br.ready()) {
                ittmUser = br.readLine();
            }
        } catch (IOException e) {
            VIEWER_LOGGER.info("Error read config file: " + e.getMessage());
        }
    }

    void WriteConfig() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(SAVE_DIRECTORY + FILENAME))) {
            out.write(calibrationTime);
            out.newLine();
            out.write(ittmUser);
            out.close();
        } catch (IOException ex) {
            VIEWER_LOGGER.info("Exception write config file: " + ex.getMessage());
        }
    }

//------------------- Connect Tab ----------------------------------------------
    @FXML
    void ConnectIconPress() {
        if ((CircleConnect.getOpacity() == 1.0) && (!AutoConnectButton.isDisable())) {
            AutoConnectButtonPress();
        } else {
            DisconnectButtonPress();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                VIEWER_LOGGER.warn("Error Sleep.");
            }
            AutoConnectButtonPress();
        }
    }

    @FXML
    void ConnectTabPress() {
        Platform.runLater(() -> {
            if (tabConnect.isSelected()) {
                if (!connectLogin) {
                    MenuTabpane.getSelectionModel().select(tabHome);
                    connectButtonLock();
                }
            }
        });
    }

    @FXML
    void connectButtonLock() {
        Platform.runLater(() -> {
            if (!connectLogin) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogin.fxml"), bundle);
                    FXMLLoginControllerConnect fxmllogin = new FXMLLoginControllerConnect(this);
                    loader.setController(fxmllogin);
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(IsometricStartButton.getScene().getWindow());
                    stage.showAndWait();
                } catch (IOException ex) {
                    VIEWER_LOGGER.error("Error Login root: " + ex);
                }
            } else {
                lockConnect();
            }
        });
    }

    public void lockConnect() {
        Platform.runLater(() -> {
            if (tabConnect.isSelected()) {
                boxConnect.setDisable(true);
                boxAquisition.setDisable(true);
                connectLogin = false;
                try {
                    lockImageConnect.setImage(new Image(getClass().getResource("images/lock.png").toURI().toString()));
                } catch (URISyntaxException ex) {
                    VIEWER_LOGGER.error("Error load image lock: " + ex);
                }
                MenuTabpane.getSelectionModel().select(tabHome);
            }
        });
    }

    public void unlockConnect() {
        Platform.runLater(() -> {
            boxConnect.setDisable(false);
            boxAquisition.setDisable(false);
            connectLogin = true;
            try {
                lockImageConnect.setImage(new Image(getClass().getResource("images/unlock.png").toURI().toString()));
            } catch (URISyntaxException ex) {
                VIEWER_LOGGER.error("Error load image lock: " + ex);
            }
            MenuTabpane.getSelectionModel().select(tabConnect);
        });
    }

    @FXML
    void AutoConnectButtonPress() {
        Platform.runLater(() -> {
            ProgressConnectHome.setVisible(true);
            CircleConnect.setOpacity(0.2);
            ProgressConnect.setVisible(true);
            AutoConnectButton.setDisable(true);          
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.AUTO_CONNECTION_REQUEST, pc_id, null));
            stateConf = AUTOSTART;
            timerConf = new Timer();
            timerConf.schedule(new TimerConfTask(), CONF_DELAY);
        });
    }

    public void receiveAutoConnect(boolean connection, String comPort) {
        Platform.runLater(() -> {
            ProgressConnect.setVisible(false);
            ProgressConnectHome.setVisible(false);
            if (!connection) {
                AutoConnectButton.setDisable(false);
            } else {
                try {
                    ImageConnect.setImage(new Image(getClass().getResource("images/connect-icon.png").toURI().toString()));
                    tabConnect.setGraphic(new ImageView(new Image(getClass().getResource("images/connect-icon-green.png").toURI().toString(), 40, 40, true, true)));
                } catch (URISyntaxException ex) {
                    VIEWER_LOGGER.error("Error loading image connect: " + ex);
                }
                CircleConnect.setFill(Color.valueOf("#48960B"));
                ConnectButton.setDisable(true);
                DisconnectButton.setDisable(false);
                StartButton.setDisable(false);
                tabCalib.setDisable(false);
                lockImageCalibration.setDisable(false);
                LocalDate localDateFile = LocalDate.parse(calibrationTime, formatter);
                LocalDate localDateNow = LocalDate.now();
                if (localDateNow.isAfter(localDateFile)) {
                    this.showPopupBundle("popup.label.calibration");
                    this.unlockCalib();
                    MenuTabpane.getSelectionModel().select(tabCalib);
                }
                timerConnection = new Timer();
                timerConnection.scheduleAtFixedRate(new TimerConnectionTask(), CONN_DELAY, CONN_DELAY);
                StopButton.setDisable(false);
                StartExerciseButton.setDisable(false);
                tabCounter.setDisable(false);
                tabMovement.setDisable(false);
                tabIsometric.setDisable(false);
                tabExercise.setDisable(false);
                stateConf = 0;
            }
            CircleConnect.setOpacity(1);
        });
    }

    @FXML
    void ConnectButtonPress() {
        Platform.runLater(() -> {
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.CONNECTION_REQUEST, pc_id, null));
            ConnectButton.setDisable(true);
            stateConf = CONNECT;
            timerConf = new Timer();
            timerConf.schedule(new TimerConfTask(), CONF_DELAY);            
        });
    }

    public void receiveConnectionResponse(boolean status) {
        Platform.runLater(() -> {
            timerConf.cancel();
            timerConf.purge();
            if (!status) {
                return;
            }
            ConnectButton.setDisable(true);
            DisconnectButton.setDisable(false);
            StartButton.setDisable(false);
            tabCalib.setDisable(false);
            lockImageCalibration.setDisable(false);
            try {
                ImageConnect.setImage(new Image(getClass().getResource("images/connect-icon.png").toURI().toString()));
                tabConnect.setGraphic(new ImageView(new Image(getClass().getResource("images/connect-icon-green.png").toURI().toString(), 40, 40, true, true)));
            } catch (URISyntaxException ex) {
                VIEWER_LOGGER.error("Error loading image connect: " + ex);
            }
            CircleConnect.setFill(Color.GREEN);
            timerConnection = new Timer();
            timerConnection.scheduleAtFixedRate(new TimerConnectionTask(), CONN_DELAY, CONN_DELAY);
            LocalDate localDateFile = LocalDate.parse(calibrationTime, formatter);
            LocalDate localDateNow = LocalDate.now();
            if (localDateNow.isAfter(localDateFile)) {
                this.showPopupBundle("popup.label.calibration");
                this.unlockCalib();
                MenuTabpane.getSelectionModel().select(tabCalib);
            }            
        });
    }

    @FXML
    void DisconnectButtonPress() {
        onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.DISCONNECT, null, null));
        communicationError();
    }

    @FXML
    void StartButtonPress() {
        Platform.runLater(() -> {
            StartButton.setDisable(true);
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.START, "", startSpinner.getValue().floatValue()));
            stateConf = START;
            timerConf = new Timer();
            timerConf.schedule(new TimerConfTask(), CONF_DELAY);
        });
    }

    public void receiveStartConf() {
        Platform.runLater(() -> {
            timerConf.cancel();
            timerConf.purge();
            StopButton.setDisable(false);
            StartExerciseButton.setDisable(false);
            tabCounter.setDisable(false);
            tabMovement.setDisable(false);
            tabIsometric.setDisable(false);
            tabExercise.setDisable(false);
            stateConf = 0;
        });
    }

    @FXML
    void StoptButtonPress() {
        Platform.runLater(() -> {
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.STOP, null, null));
            StopButton.setDisable(true);
            stateConf = STOP;
            timerConf = new Timer();
            timerConf.schedule(new TimerConfTask(), CONF_DELAY);
        });
    }

    public void receiveStopConf() {
        Platform.runLater(() -> {
            timerConf.cancel();
            timerConf.purge();
            StartButton.setDisable(false);
            tabCounter.setDisable(true);
            tabIsometric.setDisable(true);
            tabExercise.setDisable(true);
            StopExerciseButton.setDisable(true);
            StartExerciseButton.setDisable(true);
            if (healthMonitor){
                WarmUpButton.setDisable(false);
            }
            stateConf = 0;
        });
    }

    public void communicationError() {
        Platform.runLater(() -> {
            timerConnection.cancel();
            timerConnection.purge();
            ConnectButton.setDisable(false);
            AutoConnectButton.setDisable(false);
            DisconnectButton.setDisable(true);
            ExtensionAngleLabel.setText("");
            IntAngleLabel.setText("");
            FlexionAngleLabel.setText("");
            tabCalib.setDisable(true);
            tabCounter.setDisable(true);
            tabMovement.setDisable(true);
            tabIsometric.setDisable(true);
            tabExercise.setDisable(true);
            StartButton.setDisable(true);
            StopButton.setDisable(true);
            lockConnect();
            lockCalib();
            lockImageCalibration.setDisable(true);
            try {
                ImageConnect.setImage(new Image(getClass().getResource("images/disconnect-icon.png").toURI().toString()));
                tabConnect.setGraphic(new ImageView(new Image(getClass().getResource("images/disconnect-icon-red.png").toURI().toString(), 40, 40, true, true)));
            } catch (URISyntaxException ex) {
                VIEWER_LOGGER.error("Error loading image connect: " + ex);
            }
            CircleConnect.setFill(Color.valueOf("#064266"));
        });
    }

    public void receiveCheckPort(boolean status) {
        if (!status){
            communicationError();
        }
        connectionRequest = false;
    }

    private class TimerConnectionTask extends TimerTask {
        @Override
        public void run() {
            if (connectionRequest){
                communicationError();
                connectionRequest = false;
            }
            else {
                onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.CHECK_PORT_REQUEST, null, null));
                connectionRequest = true;
            }
        }
    }

//------------------- User Configuration Tab -----------------------------------
    @FXML
    void UserIconPress() {
        Platform.runLater(() -> {
            MenuTabpane.getSelectionModel().select(tabUser);
        });
    }

    public void updateUser() {
        if (!healthMonitor) {           
            return;
        }
        UserIdText.setText("");
        UserAngleExtension.getValueFactory().setValue(0);
        UserAngleFlexion.getValueFactory().setValue(0);
        TightPositionText.getValueFactory().setValue(0);
        ZeroPositionText.getValueFactory().setValue(0);
        SessionNumberText.getValueFactory().setValue(0);
        TrainingNumberText.getValueFactory().setValue(0);
        CounterweightUserText.getValueFactory().setValue(0);
        SeatCushionText.setText("");
        TrainingWeightText.getValueFactory().setValue(0);
        TrainIdText.setText("");
        ForceTestIdText.setText("");
        try {
            ParametersResponseJson parametersResponseJson = this.ittmApi.downloadTrainingParametersLatest();
            UserIdText.setText(parametersResponseJson.getValues().getCitizenId());
            UserAngleExtension.getValueFactory().setValue(parametersResponseJson.getValues().getLeRomExtension());
            UserAngleFlexion.getValueFactory().setValue(parametersResponseJson.getValues().getLeRomFlexion());
            TightPositionText.getValueFactory().setValue(parametersResponseJson.getValues().getLeTightPosition());
            ZeroPositionText.getValueFactory().setValue(parametersResponseJson.getValues().getLeZeroPosition());
            SessionNumberText.getValueFactory().setValue(parametersResponseJson.getValues().getSessionNumber());
            TrainingNumberText.getValueFactory().setValue(parametersResponseJson.getValues().getTrainingNumber());
            CounterweightUserText.getValueFactory().setValue((int) parametersResponseJson.getValues().getLeCounterWeight());
            SeatCushionText.setText(parametersResponseJson.getValues().getLeSeatCushion());
            TrainingWeightText.getValueFactory().setValue(Math.round(parametersResponseJson.getValues().getLeTrainingWeight()));
            String trainingId = parametersResponseJson.getValues().getTrainingId();
            if (trainingId.startsWith("MT_")) {
                TrainIdText.setText(trainingId);
            } else if (trainingId.startsWith("FT_")) {
                ForceTestIdText.setText(trainingId);
            }
            VIEWER_LOGGER.warn("UpdateUser = " + MenuTabpane.getSelectionModel().getSelectedItem().getText());
            VIEWER_LOGGER.warn((new ParametersGsonUtil().toResponseJson(parametersResponseJson)));            
        } catch (IttmApiException ex) {
            VIEWER_LOGGER.error(ex.getMessage());
            showPopupIttmError(ex.getMessage());
        } catch (IttmApi500Exception ex) {
            VIEWER_LOGGER.error(ex.getMessage());
            showPopupIttmError(ex.getMessage()); 
            healthMonitorConnected(false);
        } catch (IttmApiTokenException ex) {
            VIEWER_LOGGER.warn(ex.getMessage());
            healthMonitorConnected(false);
        }        
    }

    @FXML
    void loadUserButtonPress() {
        Platform.runLater(() -> {
            updateUser();
        });
    }

    void lockCitizenConfig(boolean state) {
        if (state) {
            UserButton.setDisable(false);
            UserIdText.setEditable(false);
            TrainIdText.setEditable(false);
            ForceTestIdText.setEditable(false);
            SessionNumberText.setDisable(true);
            SessionNumberText.setOpacity(1);
            TrainingNumberText.setDisable(true);
            TrainingNumberText.setOpacity(1);
            CounterweightUserText.setDisable(true);
            CounterweightUserText.setOpacity(1);
            TrainingWeightText.setDisable(true);
            TrainingWeightText.setOpacity(1);
            UserAngleExtension.setDisable(true);
            UserAngleExtension.setOpacity(1);
            UserAngleFlexion.setDisable(true);
            UserAngleFlexion.setOpacity(1);
            ZeroPositionText.setDisable(true);
            ZeroPositionText.setOpacity(1);
            TightPositionText.setDisable(true);
            TightPositionText.setOpacity(1);
            SeatCushionText.setEditable(false);
            UserExtensionTime.setDisable(false);
            UserExtensionTime.setOpacity(1);
            UserFlexionTime.setDisable(false);
            UserFlexionTime.setOpacity(1);
            UserHoldTime.setDisable(false);
            UserHoldTime.setOpacity(1);
        } else {
            UserButton.setDisable(true);
            UserIdText.setEditable(true);
            TrainIdText.setEditable(true);
            ForceTestIdText.setEditable(true);
            SessionNumberText.setDisable(false);
            TrainingNumberText.setDisable(false);
            CounterweightUserText.setDisable(false);
            TrainingWeightText.setDisable(false);
            UserAngleExtension.setDisable(false);
            UserAngleFlexion.setDisable(false);
            ZeroPositionText.setDisable(false);
            TightPositionText.setDisable(false);
            SeatCushionText.setEditable(true);
            UserExtensionTime.setDisable(false);
            UserFlexionTime.setDisable(false);
            UserHoldTime.setDisable(false);
        }
    }

//------------------- ITTM Tab -------------------------------------------------  
    @FXML
    void ITTMTabPress() {
        Platform.runLater(() -> {
            if (tabIttm.isSelected()) {
                MenuTabpane.getSelectionModel().select(tabHome);
                ITTMIconPress();
            }
        });
    }

    @FXML
    void ITTMIconPress() {
        Platform.runLater(() -> {
            if (healthMonitor) {
                try {
                    Desktop.getDesktop().browse(new URL(ittmApi.getIttmHost()).toURI());
                } catch (IOException e) {
                    VIEWER_LOGGER.error("Error loading HealthMonitor: " + e);
                } catch (URISyntaxException e) {
                    VIEWER_LOGGER.error("Error loading HealthMonitor 2: " + e);
                }
            } else {
                lockMain(false);
            }
        });
    }

    public void healthMonitorConnected(boolean state) {
        healthMonitor = state;
        if (state) {
            tabIttm.setStyle("-fx-font-size: 24; -fx-background-color: #06344f; -fx-text-base-color: white; -fx-font-weight: bold");
            lockCitizenConfig(true);
        } else {
            tabIttm.setStyle("-fx-font-size: 24; -fx-background-color: #06344f; -fx-text-base-color: #ba0000; -fx-font-weight: bold");
            lockCitizenConfig(false);
        }
    }

//------------------- Sensors Calibration Tab ----------------------------------
    @FXML
    void CalibrationTabSelected() {
        Platform.runLater(() -> {
            if (tabCalib.isSelected()) {
                ExtensionAngleLabel.setText("");
                IntAngleLabel.setText("");
                FlexionAngleLabel.setText("");
                PressureLabel.setText("");
            }
        });
    }

    @FXML
    void CalibrationIconPress() {
        Platform.runLater(() -> {
            MenuTabpane.getSelectionModel().select(tabCalib);
        });
    }

    @FXML
    void calibrationButton() {
        Platform.runLater(() -> {
            if (!calibrationLogin) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogin.fxml"), bundle);
                    FXMLLoginControllerCalib fxmllogin = new FXMLLoginControllerCalib(this);
                    loader.setController(fxmllogin);
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(IsometricStartButton.getScene().getWindow());
                    stage.showAndWait();
                } catch (IOException ex) {
                    VIEWER_LOGGER.error("Error Login root: " + ex);
                }
            } else {
                lockCalib();
            }
        });
    }

    public void lockCalib() {
        Platform.runLater(() -> {
            if (tabCalib.isSelected()) {
                AngleCaliGrid.setDisable(true);
                PressureButton.setDisable(true);
                calibrationLogin = false;
                try {
                    lockImageCalibration.setImage(new Image(getClass().getResource("images/lock.png").toURI().toString()));
                } catch (URISyntaxException ex) {
                    VIEWER_LOGGER.error("Error load image lock: " + ex);
                }
            }
        });
    }

    @FXML
    public void unlockCalib() {
        Platform.runLater(() -> {
            if (tabCalib.isSelected()) {
                AngleCaliGrid.setDisable(false);
                PressureButton.setDisable(false);
                calibrationLogin = true;
                try {
                    lockImageCalibration.setImage(new Image(getClass().getResource("images/unlock.png").toURI().toString()));
                } catch (URISyntaxException ex) {
                    VIEWER_LOGGER.error("Error load image unlock: " + ex);
                }
            }
        });
    }

    @FXML
    void AIntButtonPress() {
        Platform.runLater(() -> {
            if ((ExtensionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (IntAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (FlexionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (PressureLabel.getText().equals(bundle.getString("tab.calibration.ok")))){
                ExtensionAngleLabel.setText("");
                IntAngleLabel.setText("");
                FlexionAngleLabel.setText("");
                PressureLabel.setText("");
            }       
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.ZERO_ANGLE, null, new Short(ANGLE_INT).floatValue()));
            IntAngleLabel.setText(bundle.getString("tab.calibration.calibrating"));
            ExtensionAngleButton.setDisable(true);
            FlexionAngleButton.setDisable(true);
            IntAngleButton.setDisable(true);
            PressureButton.setDisable(true);
            stateConf = A_INT;
            timerConf = new Timer();
            timerConf.schedule(new TimerConfTask(), CONF_DELAY);
        });
    }

    public void receiveIntAngleConf() {
        Platform.runLater(() -> {
            timerConf.cancel();
            timerConf.purge();
            IntAngleLabel.setText(bundle.getString("tab.calibration.ok"));
            ExtensionAngleButton.setDisable(false);
            FlexionAngleButton.setDisable(false);
            IntAngleButton.setDisable(false);
            PressureButton.setDisable(false);
            stateConf = 0;
            if ((ExtensionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (IntAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (FlexionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (PressureLabel.getText().equals(bundle.getString("tab.calibration.ok")))) {
                calibrationTime = LocalDate.now().toString();
                WriteConfig();
            }
        });
    }

    @FXML
    void flexionAngleButtonPress() {
        Platform.runLater(() -> {
            if ((ExtensionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (IntAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (FlexionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (PressureLabel.getText().equals(bundle.getString("tab.calibration.ok")))){
                ExtensionAngleLabel.setText("");
                IntAngleLabel.setText("");
                FlexionAngleLabel.setText("");
                PressureLabel.setText("");
            }           
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.FLEXION_ANGLE, null, new Short(ANGLE_FLEXION).floatValue()));
            FlexionAngleLabel.setText(bundle.getString("tab.calibration.calibrating"));
            ExtensionAngleButton.setDisable(true);
            FlexionAngleButton.setDisable(true);
            IntAngleButton.setDisable(true);
            PressureButton.setDisable(true);
            stateConf = A_FLEXION;
            timerConf = new Timer();
            timerConf.schedule(new TimerConfTask(), CONF_DELAY);
        });
    }

    public void receiveFlexionAngleConf() {
        Platform.runLater(() -> {
            timerConf.cancel();
            timerConf.purge();
            FlexionAngleLabel.setText(bundle.getString("tab.calibration.ok"));
            ExtensionAngleButton.setDisable(false);
            FlexionAngleButton.setDisable(false);
            IntAngleButton.setDisable(false);
            PressureButton.setDisable(false);
            stateConf = 0;
            if ((ExtensionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (IntAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (FlexionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (PressureLabel.getText().equals(bundle.getString("tab.calibration.ok")))) {
                calibrationTime = LocalDate.now().toString();
                WriteConfig();
            }
        });
    }

    @FXML
    void extensionAngleButtonPress() {
        Platform.runLater(() -> {
            if ((ExtensionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (IntAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (FlexionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (PressureLabel.getText().equals(bundle.getString("tab.calibration.ok")))){
                ExtensionAngleLabel.setText("");
                IntAngleLabel.setText("");
                FlexionAngleLabel.setText("");
                PressureLabel.setText("");
            }              
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.EXTENSION_ANGLE, null, new Short(ANGLE_EXTENSION).floatValue()));
            ExtensionAngleLabel.setText(bundle.getString("tab.calibration.calibrating"));
            ExtensionAngleButton.setDisable(true);
            FlexionAngleButton.setDisable(true);
            IntAngleButton.setDisable(true);
            PressureButton.setDisable(true);
            stateConf = A_EXTENSION;
            timerConf = new Timer();
            timerConf.schedule(new TimerConfTask(), CONF_DELAY);
        });
    }

    public void receiveExtensionAngleConf() {
        Platform.runLater(() -> {
            timerConf.cancel();
            timerConf.purge();
            ExtensionAngleLabel.setText(bundle.getString("tab.calibration.ok"));
            ExtensionAngleButton.setDisable(false);
            FlexionAngleButton.setDisable(false);
            IntAngleButton.setDisable(false);
            PressureButton.setDisable(false);
            stateConf = 0;
            if ((ExtensionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (IntAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (FlexionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (PressureLabel.getText().equals(bundle.getString("tab.calibration.ok")))) {
                calibrationTime = LocalDate.now().toString();
                WriteConfig();
            }
        });
    }

    @FXML
    void PressureButtonPress() {
        Platform.runLater(() -> {
            if ((ExtensionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (IntAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (FlexionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (PressureLabel.getText().equals(bundle.getString("tab.calibration.ok")))){
                ExtensionAngleLabel.setText("");
                IntAngleLabel.setText("");
                FlexionAngleLabel.setText("");
                PressureLabel.setText("");
            }             
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.PRESSURE, null, null));
            PressureLabel.setText(bundle.getString("tab.calibration.calibrating"));
            ExtensionAngleButton.setDisable(true);
            FlexionAngleButton.setDisable(true);
            IntAngleButton.setDisable(true);
            PressureButton.setDisable(true);
            stateConf = PRESSURE;
            timerConf = new Timer();
            timerConf.schedule(new TimerConfTask(), CONF_DELAY);
        });
    }

    public void receivePressureConf() {
        Platform.runLater(() -> {
            timerConf.cancel();
            timerConf.purge();
            PressureLabel.setText(bundle.getString("tab.calibration.ok"));
            ExtensionAngleButton.setDisable(false);
            FlexionAngleButton.setDisable(false);
            IntAngleButton.setDisable(false);
            PressureButton.setDisable(false);
            stateConf = 0;
            if ((ExtensionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (IntAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (FlexionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (PressureLabel.getText().equals(bundle.getString("tab.calibration.ok")))) {
                calibrationTime = LocalDate.now().toString();
                WriteConfig();
            }            
        });
    }

//------------------- Movement Tab ----------------------------------------    
    @FXML
    void MovementTabSelected() {
        Platform.runLater(() -> {
            if (tabMovement.isSelected()) {
                updateUser();
                movementCitizenLabel.setText(UserIdText.getText());
            }
        });
    }

    @FXML
    void MovementIconPress() {
        Platform.runLater(() -> {
            MenuTabpane.getSelectionModel().select(tabMovement);
        });
    }

    @FXML
    void MovementButtonBack() {
        Platform.runLater(() -> {
            switch (stateMovement) {
                case 2:
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step0"));
                    movementNextButton.setText(bundle.getString("tab.movement.start.button"));
                    movementExtensionLabel.getValueFactory().setValue(0);
                    movementZeroLabel.getValueFactory().setValue(0);
                    movementFlexionLabel.getValueFactory().setValue(0);
                    movementBackButton.setDisable(true);
                    movementCancelButton.setDisable(true);
                    stateMovement = 1;
                    break;
                case 3:
                    movementNextButton.setText(bundle.getString("tab.movement.next.button"));
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step1"));
                    movementExtensionLabel.getValueFactory().setValue(0);
                    stateMovement = 2;
                    break;
                case 4:
                    movementFlexionLabel.getValueFactory().setValue(0);
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step2"));
                    stateMovement = 3;
                    break;
                case 0:
                    movementZeroLabel.getValueFactory().setValue(0);
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step3"));
                    movementNextButton.setText(bundle.getString("tab.movement.next.button"));
                    stateMovement = 4;
                    break;
                default:
            }
        });
    }

    @FXML
    void MovementButtonNext() {
        Platform.runLater(() -> {
            switch (stateMovement) {
                case 0:
                    extensionUserAngle = movementExtensionLabel.getValue();
                    flexionUserAngle = movementFlexionLabel.getValue();
                    zeroUserAngle = movementZeroLabel.getValue();
                    ZeroPositionText.getValueFactory().setValue(zeroUserAngle);
                    UserAngleExtension.getValueFactory().setValue(extensionUserAngle);
                    UserAngleFlexion.getValueFactory().setValue(flexionUserAngle);                    
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step0"));
                    movementNextButton.setText(bundle.getString("tab.movement.start.button"));
                    movementBackButton.setDisable(true);
                    movementCancelButton.setDisable(true);
                    stateMovement = 1;
                    break;
                case 1:
                    movementNextButton.setText(bundle.getString("tab.movement.next.button"));
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step1"));
                    movementExtensionLabel.getValueFactory().setValue(0);
                    movementZeroLabel.getValueFactory().setValue(0);
                    movementFlexionLabel.getValueFactory().setValue(0);
                    movementBackButton.setDisable(false);
                    movementCancelButton.setDisable(false);
                    stateMovement = 2;
                    break;
                case 2:
                    extensionUserAngle = userAngle;
                    movementExtensionLabel.getValueFactory().setValue(extensionUserAngle);
                    UserAngleExtension.getValueFactory().setValue(extensionUserAngle);
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step2"));
                    stateMovement = 3;
                    break;
                case 3:
                    flexionUserAngle = userAngle;
                    movementFlexionLabel.getValueFactory().setValue(flexionUserAngle);
                    UserAngleFlexion.getValueFactory().setValue(flexionUserAngle);
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step3"));
                    stateMovement = 4;
                    break;
                case 4:
                    zeroUserAngle = userAngle;
                    movementZeroLabel.getValueFactory().setValue(zeroUserAngle);
                    ZeroPositionText.getValueFactory().setValue(zeroUserAngle);
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step4"));
                    movementNextButton.setText(bundle.getString("tab.movement.finish.button"));
                    stateMovement = 0;
                    break;
                default:
            }
        });
    }

    @FXML
    void MovementButtonCancel() {
        movementExtensionLabel.getValueFactory().setValue(0);
        movementZeroLabel.getValueFactory().setValue(0);
        movementFlexionLabel.getValueFactory().setValue(0);
        stateMovement = 0;
        MovementButtonNext();
    }

//------------------- Counterweight Tab ----------------------------------------
    @FXML
    void CounterTabSelected() {
        Platform.runLater(() -> {
            if (tabCounter.isSelected()) {
                CountCitizenLabel.setText(UserIdText.getText());
                CountZeroLabel.setText(Integer.toString(ZeroPositionText.getValue()));
                CountExtensionLabel.setText(Integer.toString(UserAngleExtension.getValue()));
            }
        });
    }

    @FXML
    void CounterIconPress() {
        Platform.runLater(() -> {
            MenuTabpane.getSelectionModel().select(tabCounter);
        });
    }

    @FXML
    void CountButtonBack() {
        Platform.runLater(() -> {
            switch (stateCount) {
                case 2:
                    CountInstructionLabel.setText(bundle.getString("tab.counterweight.label.step0"));
                    countNextButton.setText(bundle.getString("tab.counterweight.button.start"));
                    countBackButton.setDisable(true);
                    countCancelButton.setDisable(true);
                    stateCount = 1;
                    break;
                case 3:
                    MessageFormat mf = new MessageFormat(bundle.getString("tab.counterweight.label.step1"));
                    CountInstructionLabel.setText(mf.format(new Object[]{Integer.toString(ZeroPositionText.getValue())}));
                    stateCount = 2;
                    break;                    
                case 0:
                    countNextButton.setText(bundle.getString("tab.counterweight.button.next"));
                    MessageFormat mf2 = new MessageFormat(bundle.getString("tab.counterweight.label.step2"));
                    CountInstructionLabel.setText(mf2.format(new Object[]{Integer.toString(UserAngleExtension.getValue())}));
                    stateCount = 3;
                    break;
                default:
            }
        });
    }

    @FXML
    void CountButtonCancel() {
        stateCount = 0;
        CountButtonNext();
    }

    @FXML
    void CountButtonNext() {
        Platform.runLater(() -> {
            switch (stateCount) {
                case 0:
                    countNextButton.setText(bundle.getString("tab.counterweight.button.start"));
                    CountInstructionLabel.setText(bundle.getString("tab.counterweight.label.step0"));
                    countBackButton.setDisable(true);
                    countCancelButton.setDisable(true);
                    stateCount = 1;
                    break;
                case 1:
                    countNextButton.setText(bundle.getString("tab.counterweight.button.next"));
                    MessageFormat mf = new MessageFormat(bundle.getString("tab.counterweight.label.step1"));
                    CountInstructionLabel.setText(mf.format(new Object[]{Integer.toString(ZeroPositionText.getValue())}));
                    countBackButton.setDisable(false);
                    countCancelButton.setDisable(false);
                    stateCount = 2;
                    break;
                case 2:
                    if ((userAngle >= (ZeroPositionText.getValue() - 1)) && (userAngle <= (ZeroPositionText.getValue() + 1))) {
                        MessageFormat mf2 = new MessageFormat(bundle.getString("tab.counterweight.label.step2"));
                        CountInstructionLabel.setText(mf2.format(new Object[]{Integer.toString(UserAngleExtension.getValue())}));
                        stateCount = 3;
                    } else {
                        showPopupBundle("tab.counterweight.popup.angle");
                    }
                    break;
                case 3:
                    if ((userAngle >= (UserAngleExtension.getValue() - 1)) && (userAngle <= (UserAngleExtension.getValue() + 1))) {
                        countNextButton.setText(bundle.getString("tab.counterweight.button.finish"));
                        CountInstructionLabel.setText(bundle.getString("tab.counterweight.label.step3"));
                        stateCount = 0;
                    } else {
                        showPopupBundle("tab.counterweight.popup.angle");
                    }
                    break;
                default:
            }
        });
    }    

//------------------- Isometric Test Tab --------------------------------------- 
    @FXML
    void IsometricTabSelected() {
        Platform.runLater(() -> {
            if (tabIsometric.isSelected()) {
                updateUser();     
                if (!citizenID.equalsIgnoreCase(UserIdText.getText())){
                    IsometricCancelButtonPress();
                }   
                citizenID = UserIdText.getText();                
                IsoExtensionAngleText.setText(Integer.toString(UserAngleExtension.getValue()));
                IsoFlexionAngleText.setText(Integer.toString(UserAngleFlexion.getValue()));
                barchartiso.setTitle(bundle.getString("tab.isometric.chart.title") + UserIdText.getText() + ". " + bundle.getString("tab.user.label.force") + " " + ForceTestIdText.getText());
                barchartiso.setAxisLabel(bundle.getString("tab.isometric.chart.xaxis"), bundle.getString("tab.isometric.chart.yaxis"));
            }
        });
    }

    @FXML
    void IsometricIconPress() {
        Platform.runLater(() -> {
            MenuTabpane.getSelectionModel().select(tabIsometric);
        });
    }

    @FXML
    void IsometricStartButtonPress() {
        Platform.runLater(() -> {
            Double average = 0.0;
            switch (stateIso) {
                case 0:
                    IsometricStartButton.setText(bundle.getString("tab.isometric.button.start"));
                    IsometricFinishButton.setDisable(true);
                    stateIso = 1;
                    break;
                case 1:
                    updateUser();
                    IsometricFinishButton.setDisable(true);
                    IsometricStartButton.setText(bundle.getString("tab.isometric.button.next"));
                    IsoForceText.setText("--");
                    flexionUserAngle = UserAngleFlexion.getValue();
                    extensionUserAngle = UserAngleExtension.getValue();
                    IsoExtensionAngleText.setText(String.valueOf(extensionUserAngle));
                    IsoFlexionAngleText.setText(String.valueOf(flexionUserAngle));
                    IsoList = getIsoList(extensionUserAngle, flexionUserAngle);
                    ForceMax.clear();
                    for (Integer IsoList1 : IsoList) {
                        ForceMax.add(-1);
                    }
                    barchartiso.setTitle(bundle.getString("tab.isometric.chart.title") + UserIdText.getText() + ". " + bundle.getString("tab.user.label.force") + " " + ForceTestIdText.getText());
                    barchartiso.StartChart(IsoList);
                    stateIso = 2;
                    break;
                case 2:
                    //Start angle
                    IsoPosition = -1;
                    for (int i = 0; i < IsoList.size(); i++) {
                        if ((userAngle >= (IsoList.get(i) - 1)) && (userAngle <= (IsoList.get(i) + 1))) {
                            IsoPosition = i;
                            break;
                        }
                    }
                    if (IsoPosition >= 0) {
                        barchartiso.setCurrentAngle(IsoPosition);
                        barchartiso.setFlagMax(true);
                        barchartiso.clearMax(IsoPosition);
                        IsoChart = true;
                        stateIso = 3;
                    } else {
                        showPopupBundle("popup.label.angle3");
                    }
                    break;
                case 3:
                    //Save angle
                    if ((userAngle >= (IsoList.get(IsoPosition) - 1)) && (userAngle <= (IsoList.get(IsoPosition) + 1))) {
                        ForceMax.set(IsoPosition, barchartiso.getMax(IsoPosition));
                        barchartiso.setFlagMax(false);
                        barchartiso.removeVerticalMarker();
                        IsoChart = false;
                        average = IsometricAverage();
                        IsoForceText.setText(String.valueOf(Math.round(average)));
                        stateIso = 2;
                    } else {
                        IsoChart = false;
                        barchartiso.setFlagMax(false);
                        barchartiso.clearMax(IsoPosition);
                        barchartiso.removeVerticalMarker();
                        barchartiso.setCurrentForce(0);
                        showPopupForceTest("popup.label.angle2", IsoList.get(IsoPosition));
                        stateIso = 2;
                    }
                    if (!ForceMax.contains(-1)) {
                        IsometricFinishButton.setDisable(false);
                    }
                    break;
                default:
            }
        });
    }

    @FXML
    void IsometricFinishButtonPress() {
        Platform.runLater(() -> {
            if (ForceMax.contains(-1)) {
                showPopupBundle("popup.label.isometric");
            } else {
                ForceTestRequestJson forceTestRequestJson = new ForceTestRequestJson();
                forceTestRequestJson.setCitizenId(UserIdText.getText());
                forceTestRequestJson.setForceTestId(ForceTestIdText.getText());
                forceTestRequestJson.setTestTime(Instant.now().toEpochMilli());
                Double average = IsometricAverage();
                forceTestRequestJson.setRecomendedWeight((float) Math.round(average));
                ForceTestData[] forceTestData = new ForceTestData[IsoList.size()];
                for (int i = 0; i < forceTestData.length; i++) {
                    ForceTestData value = new ForceTestData();
                    value.setAngle(IsoList.get(i));
                    value.setForce(ForceMax.get(i));
                    forceTestData[i] = value;
                }
                forceTestRequestJson.setForceTestData(forceTestData);
                try {
                    ittmApi.uploadForceTest(forceTestRequestJson);
                } catch (IttmApiException ex) {
                    VIEWER_LOGGER.error(ex.getMessage());
                    showPopupIttmError(ex.getMessage());
                } catch (IttmApi500Exception ex) {
                    VIEWER_LOGGER.error(ex.getMessage());
                    showPopupIttmError(ex.getMessage()); 
                    healthMonitorConnected(false);    
                } catch (IttmApiTokenException ex) {
                    VIEWER_LOGGER.warn(ex.getMessage());
                    healthMonitorConnected(false);
                }
                stateIso = 0;
                IsometricStartButtonPress();
            }
        });
    }

    @FXML
    void IsometricCancelButtonPress() {
        Platform.runLater(() -> {
            IsoList.clear();
            barchartiso.removeVerticalMarker();
            IsoChart = false;
            barchartiso.setFlagMax(false);
            IsoExtensionAngleText.setText("--");
            IsoFlexionAngleText.setText("--");
            IsoForceText.setText("--");
            barchartiso.StartChart(IsoList);
            IsoPosition = -1;
            stateIso = 0;
            IsometricStartButtonPress();
        });
    }

    double IsometricAverage() {
        double average = 0.0;
        int aux = 0;
        for (Integer force : ForceMax) {
            if (force != -1) {
                average = average + force;
                aux = aux + 1;
            }
        }
        average = average / aux;
        return average / 2;
    }

    @FXML
    private void showPopupBundle(String bundleString) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPopup.fxml"), bundle);
            Parent root = loader.load();
            FXMLPopupController controller = loader.getController();
            controller.setPopupLabel(bundle.getString(bundleString));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(IsometricStartButton.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException ex) {
            VIEWER_LOGGER.error("Error showing popup" + ex.getMessage());
        }
    }

    private void showPopupIttmStatus(String str1, int statusCode, String str2) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPopup.fxml"), bundle);
            Parent root = loader.load();
            FXMLPopupController controller = loader.getController();
            MessageFormat mf = new MessageFormat(bundle.getString("popup.label.ittm.status.error"));
            controller.setPopupLabel(mf.format(new Object[]{str1, statusCode, str2}));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(IsometricStartButton.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException ex) {
            VIEWER_LOGGER.error("Error showing popup" + ex.getMessage());
        }
    }

    public void showPopupIttmError(String error) {
       Platform.runLater(() -> { 
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPopup.fxml"), bundle);
                Parent root = loader.load();
                FXMLPopupController controller = loader.getController();
                controller.setPopupLabel(error);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(IsometricStartButton.getScene().getWindow());
                stage.showAndWait();
            } catch (IOException ex) {
                VIEWER_LOGGER.error("Error showing popup" + ex.getMessage());
            }
       });
    }

    private void showPopupForceTest(String label, Integer value) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPopup.fxml"), bundle);
            Parent root = loader.load();
            FXMLPopupController controller = loader.getController();
            MessageFormat mf = new MessageFormat(bundle.getString(label));
            controller.setPopupLabel(mf.format(new Object[]{value}));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(IsometricStartButton.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException ex) {
            VIEWER_LOGGER.error("Error showing popup" + ex.getMessage());
        }
    }

    List<Integer> getIsoList(int extensionAngle, int flexionAngle) {
        List<Integer> list = new ArrayList<>();
        if ((extensionAngle <= 0) && (flexionAngle >= 0)) {
            list.add(0);
        }
        if ((extensionAngle <= 12) && (flexionAngle >= 12)) {
            list.add(12);
        }
        if ((extensionAngle <= 24) && (flexionAngle >= 24)) {
            list.add(24);
        }
        if ((extensionAngle <= 36) && (flexionAngle >= 36)) {
            list.add(36);
        }
        if ((extensionAngle <= 48) && (flexionAngle >= 48)) {
            list.add(48);
        }
        if ((extensionAngle <= 60) && (flexionAngle >= 60)) {
            list.add(60);
        }
        if ((extensionAngle <= 72) && (flexionAngle >= 72)) {
            list.add(72);
        }
        if (!list.contains(extensionAngle)) {
            list.add(extensionAngle);
        }
        if (!list.contains(flexionAngle)) {
            list.add(flexionAngle);
        }
        Collections.sort(list);
        Collections.reverse(list);
        return list;
    }

//------------------- Exercise Tab ---------------------------------------------  
    @FXML
    void ExerciseIconPress() {
        Platform.runLater(() -> {
            MenuTabpane.getSelectionModel().select(tabExercise);
        });
    }

    @FXML
    void ExerciseTabSelected() {
        Platform.runLater(() -> {
            if (tabExercise.isSelected()) {
                updateUser();
                if (!citizenID.equalsIgnoreCase(UserIdText.getText())){
                    CleanExercise();
                }
                citizenID = UserIdText.getText();             
                ExerciseCitizenLabel.setText(UserIdText.getText());
                ExerciseTrainLabel.setText(TrainIdText.getText());
                try {
                    ForceGauge.setMaxValue(TrainingWeightText.getValue());
                } catch (NumberFormatException ex) {
                    VIEWER_LOGGER.error("Training Weight not a number.");
                }
                flexionUserAngle = UserAngleFlexion.getValue();
                extensionUserAngle = UserAngleExtension.getValue();            
                AngleGauge.setMarkers(new Marker(extensionUserAngle, Color.valueOf("#59C7D1"), MarkerType.STANDARD), new Marker(flexionUserAngle, Color.valueOf("#064266"), MarkerType.STANDARD));
                AngleGauge.setMarkersVisible(true);     
            }
        });
    }

    void CleanExercise(){
        if (exercise) {
            StopExerciseButtonPress();
        }    
        ExerciseText.setText("");
        ExerciseImage.imageProperty().set(null);
        KbzGrid.setVisible(false);
        WarmUpButton.setDisable(true);  
    }
    
    @FXML
    void StartExerciseButtonPress() {
        Platform.runLater(() -> {
            exercise = true;
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.EXERCISE, null, 1.0f));
            updateUser();
            ExerciseText.setText("");
            ExerciseImage.imageProperty().set(null);                                                          
            StartExerciseButton.setDisable(true);
            WarmUpButton.setDisable(true);
            StopExerciseButton.setDisable(false);
            UserAngleExtension.setDisable(true);
            UserAngleFlexion.setDisable(true);
            UserIdText.setDisable(true);
            TrainIdText.setDisable(true);
            UserExtensionTime.setDisable(true);
            UserFlexionTime.setDisable(true);
            UserHoldTime.setDisable(true);
            flexionUserAngle = UserAngleFlexion.getValue();
            extensionUserAngle = UserAngleExtension.getValue();            
            AngleGauge.setMarkers(new Marker(extensionUserAngle, Color.valueOf("#59C7D1"), MarkerType.STANDARD), new Marker(flexionUserAngle, Color.valueOf("#064266"), MarkerType.STANDARD));
            AngleGauge.setMarkersVisible(true);
            AngleGauge1.setVisible(true);
            KbzGrid.setVisible(true);
            ExerciseText.setVisible(true);
            ExerciseImage.setVisible(true);
            sendStartTrainingMessages();
            exercisedone=false;
            VIEWER_LOGGER.warn("Start Button Pressed.");
        });
    }

    private void sendStartTrainingMessages() {
        onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.TRAINING_TIMES, UserAngleFlexion.getValue() + ";" + UserAngleExtension.getValue() + ";" + UserExtensionTime.getValue() + ";" + UserFlexionTime.getValue() + ";" + UserHoldTime.getValue() + ";", null));
        onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.CITIZEN_ID, UserIdText.getText(), null));
        onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.FLEXION_ANGLE, null, (float)UserAngleFlexion.getValue()));
        onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.EXTENSION_ANGLE, null, (float)UserAngleExtension.getValue()));
        onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.TRAINING_ID, TrainIdText.getText(), null));
        onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.TRAINING_NUMBER, null, (float)TrainingNumberText.getValue()));
        onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.TRAINING_SESSION, null, (float)SessionNumberText.getValue()));
        onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.TRAINING_WEIGHT, null, (float)TrainingWeightText.getValue()));
        onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.START_TRAINING, null, null));
    }

    @FXML
    void StopExerciseButtonPress() {
        Platform.runLater(() -> {
            exercise = false;
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.EXERCISE, null, 0.0f));
            StopExerciseButton.setDisable(true);
            StartExerciseButton.setDisable(false);
            if (healthMonitor){
                WarmUpButton.setDisable(false);
            }
            UserAngleExtension.setDisable(false);
            UserAngleFlexion.setDisable(false);
            UserIdText.setDisable(false);
            TrainIdText.setDisable(false);
            UserExtensionTime.setDisable(false);
            UserFlexionTime.setDisable(false);
            UserHoldTime.setDisable(false);
            AngleGauge1.setVisible(false);
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.STOP_TRAINING, null, null));
            exercisedone=true;
            VIEWER_LOGGER.warn("Stop Button Pressed.");
        });
    }

    @FXML
    void WarmUpButtonPress() {
        VIEWER_LOGGER.warn("Upload Button Pressed: " + exercisedone);
        if (exercisedone) {
            exercisedone = false;
            onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.UPLOAD_TRAINING, null, null));
            WarmUpButton.setDisable(true);
        }
    }      
    
    public void addKbzMessage(String text) {
        Platform.runLater(() -> {
            if (text.contains("#")) {
                String[] data2 = text.split("#");
                ExerciseText.setText(data2[1] + "\n" + data2[2] + "\n" + data2[3]);
            } else {
                String[] data = text.split(";");
                if (data.length > 3) {
                    AngleGauge1.setValue(Double.valueOf(data[0]));
                    long milis = Long.valueOf(data[1]);
                    ExerciseTimeLabel.setText(new SimpleDateFormat("mm:ss").format(new Date(milis)));
                    milis = Long.valueOf(data[2]);
                    RepetitionTimeLabel.setText(new SimpleDateFormat("mm:ss").format(new Date(milis)));
                    RepetitionNumberLabel.setText(data[3]);
                }
            }
        });
    }

    public void addKbzImage(String directory) {
        Platform.runLater(() -> {
            if ((directory == null) || (directory.isEmpty()) || (directory.contentEquals("back")) || (directory.contentEquals("front")) || (directory.contentEquals("hold"))) {
                return;
            }
            try {
                ExerciseImage.setImage(new Image(getClass().getResource("images/" + directory.toUpperCase() + ".png").toURI().toString()));
            } catch (URISyntaxException ex) {
                VIEWER_LOGGER.error("Error loading image kbz: " + ex);
            }
        });
    }

//------------------- Other Receive Messages -----------------------------------            
    private class TimerConfTask extends TimerTask {

        @Override
        public void run() {
            Platform.runLater(() -> {
                switch (stateConf) {
                    case A_EXTENSION:
                        ExtensionAngleLabel.setText(bundle.getString("tab.calibration.error"));
                        ExtensionAngleButton.setDisable(false);
                        break;
                    case A_FLEXION:
                        FlexionAngleLabel.setText(bundle.getString("tab.calibration.error"));
                        FlexionAngleButton.setDisable(false);
                        break;
                    case A_INT:
                        IntAngleLabel.setText(bundle.getString("tab.calibration.error"));
                        IntAngleButton.setDisable(false);
                        break;
                    case PRESSURE:
                        PressureLabel.setText(bundle.getString("tab.calibration.error"));
                        PressureButton.setDisable(false);
                        break;
                    case AUTOSTART:
                        ProgressConnect.setVisible(false);
                        ProgressConnectHome.setVisible(false);
                        AutoConnectButton.setDisable(false);
                        CircleConnect.setOpacity(1);
                        break;
                    case CONNECT:
                        ConnectButton.setDisable(false);
                        break;
                    case START:
                        StartButton.setDisable(false);
                        break;
                    case STOP:
                        StopButton.setDisable(false);
                        break;
                    default:
                        break;
                }
                stateConf = 0;
            });
        }
    }

    public void receiveForceValue(float force) {
        Platform.runLater(() -> {
            userForce = Math.round(force);
            if (loadcell) {
                userForce = userForce/2;
            }
            if (userForce < 0) {
                ForceLabel.setText(String.valueOf(0));
            } else {
                ForceLabel.setText(String.valueOf(userForce));
            }
            CountForceLabel.setText(String.valueOf(userForce));
            ForceGauge.setValue(userForce);
            if (IsoChart) {
                if (userForce < 0) {
                    barchartiso.setCurrentForce(0);
                } else {
                    barchartiso.setCurrentForce(userForce);
                }
            }
        });
    }

    public void receiveAngleValue(float angle) {
        Platform.runLater(() -> {
            userAngle = Math.round(angle);
            AngleLabel.setText(String.valueOf(userAngle));
            movementAngleLabel.setText(String.valueOf(userAngle));
            CountAngleLabel.setText(String.valueOf(userAngle));
            IsoAngleText.setText(String.valueOf(userAngle));
            AngleGauge.setValue(angle);
        });
    }

    public void receiveHeadValueL(float head) {
        Platform.runLater(() -> {
            HeadBarL.setText(String.valueOf(Math.round(head)));
            if (head > 100) {
                HeadCircleL.setFill(Color.GREEN);
                HeadCircleL1.setFill(Color.GREEN);
            } else {
                HeadCircleL.setFill(Color.RED);
                HeadCircleL1.setFill(Color.RED);
            }
        });
    }
    
    public void receiveHeadValueR(float head) {
        Platform.runLater(() -> {
            HeadBarR.setText(String.valueOf(Math.round(head)));
            if (head > 100) {
                HeadCircleR.setFill(Color.GREEN);
                HeadCircleR1.setFill(Color.GREEN);
            } else {
                HeadCircleR.setFill(Color.RED);
                HeadCircleR1.setFill(Color.RED);
            }
        });
    }    

    public void receiveHandsValueL(float hands) {
        Platform.runLater(() -> {
            HandsBarL.setText(String.valueOf(Math.round(hands)));
            if (hands > 25) {
                HandsCircleL.setFill(Color.GREEN);
                HandsCircleL1.setFill(Color.GREEN);
            } else {
                HandsCircleL.setFill(Color.RED);
                HandsCircleL1.setFill(Color.RED);
            }
        });
    }
    
    public void receiveHandsValueR(float hands) {
        Platform.runLater(() -> {
            HandsBarR.setText(String.valueOf(Math.round(hands)));
            if (hands > 25) {
                HandsCircleR.setFill(Color.GREEN);
                HandsCircleR1.setFill(Color.GREEN);
            } else {
                HandsCircleR.setFill(Color.RED);
                HandsCircleR1.setFill(Color.RED);
            }
        });
    }    

    public void receiveBackValue(float back) {
        Platform.runLater(() -> {
            BackBar.setText(String.valueOf(Math.round(back)));
            if (back > 100) {
                BackCircle.setFill(Color.GREEN);
                BackCircle1.setFill(Color.GREEN);
            } else {
                BackCircle.setFill(Color.RED);
                BackCircle1.setFill(Color.RED);
            }
        });
    }

    public void receiveLegsValueL(float legs) {
        Platform.runLater(() -> {
            LegsBarL.setText(String.valueOf(Math.round(legs)));
            if (legs > 1) {
                LegsCircleL.setFill(Color.GREEN);
            } else {
                LegsCircleL.setFill(Color.RED);
            }
        });
    }
    
    public void receiveLegsValueR(float legs) {
        Platform.runLater(() -> {
            LegsBarR.setText(String.valueOf(Math.round(legs)));
            if (legs > 1) {
                LegsCircleR.setFill(Color.GREEN);
            } else {
                LegsCircleR.setFill(Color.RED);
            }
        });
    }    

    public void receiveFootValueL(float foot) {
        Platform.runLater(() -> {
            FootBarL.setText(String.valueOf(Math.round(foot)));
            if (foot > 1) {
                FootCircleL.setFill(Color.GREEN);
            } else {
                FootCircleL.setFill(Color.RED);
            }
        });
    }
    
    public void receiveFootValueR(float foot) {
        Platform.runLater(() -> {
            FootBarR.setText(String.valueOf(Math.round(foot)));
            if (foot > 1) {
                FootCircleR.setFill(Color.GREEN);
            } else {
                FootCircleR.setFill(Color.RED);
            }
        });
    }    

    public void receiveButton() {
        Platform.runLater(() -> {
            if (tabCalib.isSelected()) {
                if ((!ExtensionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (!ExtensionAngleButton.isDisable())) {
                    extensionAngleButtonPress();
                } else if ((!IntAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (!IntAngleButton.isDisable())) {
                    AIntButtonPress();
                } else if ((!FlexionAngleLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (!FlexionAngleButton.isDisable())) {
                    flexionAngleButtonPress();
                } else if ((!PressureLabel.getText().equals(bundle.getString("tab.calibration.ok"))) && (!PressureButton.isDisable())) {
                    PressureButtonPress();
                }
            } else if (tabMovement.isSelected()) {
                MovementButtonNext();
            } else if (tabCounter.isSelected()) {
                CountButtonNext();
            } else if (tabIsometric.isSelected() && IsometricStartButton.getText().equalsIgnoreCase(bundle.getString("tab.isometric.button.next"))) {
                IsometricStartButtonPress();
            } else if (tabExercise.isSelected()) {
                if (exercise) {
                    StopExerciseButtonPress();
                } else {
                    StartExerciseButtonPress();
                }
            }
        });
    }

//------------------- Language -------------------------------------------------     
    @FXML
    void LanguageListPress() {
        Locale locale;
        switch (LanguageList.getValue()) {
            case "English":
                locale = new Locale("en", "EN");
                break;
            case "Deutsch":
                locale = new Locale("de", "DE");
                break;
            case "Português":
                locale = new Locale("por", "POR");
                break;
            default:
                return;
        }
        bundle = ResourceBundle.getBundle("pt.uninova.s4h.healthgateway.gui.bundles.bundle", locale);
        RefreshLanguage();
    }

    private void RefreshLanguage() {
        Platform.runLater(() -> {
            calibanglesteps.setText(bundle.getString("tab.calibration.label.angle.steps"));
            calibforcesteps.setText(bundle.getString("tab.calibration.label.angle.steps2"));
            titleLabel.setText(bundle.getString("main.title"));
            LanguageList.setPromptText(bundle.getString("main.language"));
            homeConnectLabel.setText(bundle.getString("home.connect"));
            homeUserLabel.setText(bundle.getString("home.user"));
            homeCalibrationLabel.setText(bundle.getString("home.calibration"));
            homeCounterweightLabel.setText(bundle.getString("home.counterweight"));
            homeIsometricLabel.setText(bundle.getString("home.isometric"));
            homeExerciseLabel.setText(bundle.getString("home.exercise"));
            homeIttmLabel.setText(bundle.getString("home.ittm"));
            homeExitLabel.setText(bundle.getString("home.exit"));
            tabConnectTitle.setText(bundle.getString("tab.connect.title"));
            communicationLabel.setText(bundle.getString("tab.connect.label.communication"));
            AutoConnectButton.setText(bundle.getString("tab.connect.button.autoconnect"));
            ConnectButton.setText(bundle.getString("tab.connect.button.connect"));
            DisconnectButton.setText(bundle.getString("tab.connect.button.disconnect"));
            dataAcquisitionLabel.setText(bundle.getString("tab.connect.label.acquisition"));
            sampleTimeLabel.setText(bundle.getString("tab.connect.label.sampletime"));
            StartButton.setText(bundle.getString("tab.connect.button.start"));
            StopButton.setText(bundle.getString("tab.connect.button.stop"));
            tabUser.setText(bundle.getString("tab.user.tab"));
            tabUserTitle.setText(bundle.getString("tab.user.title"));
            userIdLabel.setText(bundle.getString("tab.user.label.id"));
            trainIdLabel.setText(bundle.getString("tab.user.label.train"));
            forceTesteIdLabel.setText(bundle.getString("tab.user.label.force"));
            sessionNumberLabel.setText(bundle.getString("tab.user.label.sessionNumber"));
            trainNumberLabel.setText(bundle.getString("tab.user.label.trainingNumber"));
            CounterweightUserLabel.setText(bundle.getString("tab.user.label.counterweight"));
            trainWeightLabel.setText(bundle.getString("tab.user.label.trainingWeight"));
            zeroPositionLabel.setText(bundle.getString("tab.user.label.zeroposition"));
            TightPositionLabel.setText(bundle.getString("tab.user.label.tightposition"));
            seatCushionLabel.setText(bundle.getString("tab.user.label.seatcushion"));
            extendedAngleLabel.setText(bundle.getString("tab.user.label.extended"));
            flexedAngleLabel.setText(bundle.getString("tab.user.label.flexed"));
            timeELabel.setText(bundle.getString("tab.user.label.extensiontime"));
            timeFLabel.setText(bundle.getString("tab.user.label.flexiontime"));
            timeHLabel.setText(bundle.getString("tab.user.label.holdtime"));
            UserButton.setText(bundle.getString("tab.user.button.load"));
            tabCalib.setText(bundle.getString("tab.calibration.tab"));
            calibrationTitleLabel.setText(bundle.getString("tab.calibration.title"));
            angleCalibrationLabel.setText(bundle.getString("tab.calibration.label.angle"));
            pressureCalibrationLabel.setText(bundle.getString("tab.calibration.label.pressure"));
            extensionAngleLabel.setText(bundle.getString("tab.calibration.label.extension"));
            intAngleLabel.setText(bundle.getString("tab.calibration.label.int"));
            flexionAngleLabel.setText(bundle.getString("tab.calibration.label.flexion"));
            ExtensionAngleButton.setText(bundle.getString("tab.calibration.label.calibrate"));
            FlexionAngleButton.setText(bundle.getString("tab.calibration.label.calibrate"));
            IntAngleButton.setText(bundle.getString("tab.calibration.label.calibrate"));
            PressureButton.setText(bundle.getString("tab.calibration.label.calibrate"));
            tabIsometric.setText(bundle.getString("tab.isometric.tab"));
            IsometricCancelButton.setText(bundle.getString("tab.isometric.button.cancel"));
            IsometricFinishButton.setText(bundle.getString("tab.isometric.button.finish"));
            if (stateIso == 1) {
                IsometricStartButton.setText(bundle.getString("tab.isometric.button.start"));
            } else {
                IsometricStartButton.setText(bundle.getString("tab.isometric.button.next"));
            }
            IsoExtensionAngleLabel.setText(bundle.getString("tab.isometric.label.extension"));
            IsoAngleLabel.setText(bundle.getString("tab.isometric.label.angle"));
            IsoFlexionAngleLabel.setText(bundle.getString("tab.isometric.label.flexion"));
            IsoForceLabel.setText(bundle.getString("tab.isometric.label.force"));
            tabExercise.setText(bundle.getString("tab.exercise.tab"));
            StartExerciseButton.setText(bundle.getString("tab.exercise.button.start"));
            WarmUpButton.setText(bundle.getString("tab.exercise.button.warmup"));
            StopExerciseButton.setText(bundle.getString("tab.exercise.button.stop"));
            LabelExerciseTime.setText(bundle.getString("tab.exercise.label.exercisetime"));
            LabelRepetitionTime.setText(bundle.getString("tab.exercise.label.repetitiontime"));
            LabelRepetitionNumber.setText(bundle.getString("tab.exercise.label.repetitionnumber"));
            exerciseAngleLabel.setText(bundle.getString("tab.exercise.label.angle"));
            exerciseForceLabel.setText(bundle.getString("tab.exercise.label.force"));
            tabIttm.setText(bundle.getString("tab.ittm.tab"));
            citizencountLabel.setText(bundle.getString("tab.counterweight.label.citizen"));
            barchartiso.setTitle(bundle.getString("tab.isometric.chart.title") + UserIdText.getText() + ". " + bundle.getString("tab.user.label.force") + " " + ForceTestIdText.getText());
            barchartiso.setAxisLabel(bundle.getString("tab.isometric.chart.xaxis"), bundle.getString("tab.isometric.chart.yaxis"));
            citizenExerciseLabel.setText(bundle.getString("tab.user.label.id"));
            trainExerciseLabel.setText(bundle.getString("tab.user.label.train"));
            infoLabelTitle.setText(bundle.getString("tab.info.title"));
            infoLabel.setText(bundle.getString("info.label"));
            infoLabel2.setText(bundle.getString("info.label2"));
            infoLabel3.setText(bundle.getString("info.label3"));
            homeMovementLabel.setText(bundle.getString("home.movement"));
            tabMovement.setText(bundle.getString("tab.movement.tab"));
            citizenMovementLabel.setText(bundle.getString("tab.movement.label.citizen"));
            angleMovementLabel.setText(bundle.getString("tab.movement.label.angle"));
            extensionMovementLabel.setText(bundle.getString("tab.movement.label.extension"));
            zeroMovementLabel.setText(bundle.getString("tab.movement.label.zero"));
            flexionMovementLabel.setText(bundle.getString("tab.movement.label.flexion"));
            movementTitleLabel.setText(bundle.getString("tab.movement.title"));
            movementBackButton.setText(bundle.getString("tab.movement.back.button"));
            movementCancelButton.setText(bundle.getString("tab.movement.cancel.button"));
            switch (stateMovement) {
                case 1:
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step0"));
                    movementNextButton.setText(bundle.getString("tab.movement.start.button"));
                    break;
                case 2:
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step1"));
                    movementNextButton.setText(bundle.getString("tab.movement.next.button"));
                    break;
                case 3:
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step2"));
                    movementNextButton.setText(bundle.getString("tab.movement.next.button"));
                    break;
                case 4:
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step3"));
                    movementNextButton.setText(bundle.getString("tab.movement.next.button"));
                    break;
                case 0:
                    instructionMovementLabel.setText(bundle.getString("tab.movement.label.step4"));
                    movementNextButton.setText(bundle.getString("tab.movement.finish.button"));
                    break;
            }
            countTitleLabel.setText(bundle.getString("tab.counterweight.title"));
            countBackButton.setText(bundle.getString("tab.counterweight.button.back"));
            countCancelButton.setText(bundle.getString("tab.counterweight.button.cancel"));
            tabCounter.setText(bundle.getString("tab.counterweight.tab"));
            anglecountLabel.setText(bundle.getString("tab.counterweight.label.angle"));
            forceCountLabel.setText(bundle.getString("tab.counterweight.label.force"));
            zeroCountLabel.setText(bundle.getString("tab.counterweight.label.zero"));
            extensionCountLabel.setText(bundle.getString("tab.counterweight.label.extension"));
            switch (stateCount) {
                case 1:
                    CountInstructionLabel.setText(bundle.getString("tab.counterweight.label.step0"));
                    countNextButton.setText(bundle.getString("tab.counterweight.button.start"));
                    break;
                case 2:
                    MessageFormat mf = new MessageFormat(bundle.getString("tab.counterweight.label.step1"));
                    CountInstructionLabel.setText(mf.format(new Object[]{Integer.toString(UserAngleExtension.getValue())}));
                    countNextButton.setText(bundle.getString("tab.counterweight.button.next"));
                    break;
                case 3:
                    CountInstructionLabel.setText(bundle.getString("tab.counterweight.label.step2"));
                    countNextButton.setText(bundle.getString("tab.counterweight.button.next"));
                    break;
                case 4:
                    CountInstructionLabel.setText(bundle.getString("tab.counterweight.label.step3"));
                    countNextButton.setText(bundle.getString("tab.counterweight.button.next"));
                    break;
                case 0:
                    instructionMovementLabel.setText(bundle.getString("tab.counterweight.label.step4"));
                    countNextButton.setText(bundle.getString("tab.counterweight.button.finish"));
                    break;
            }
        });
    }
//------------------- Other ---------------------------------------------------- 

    public String getSaveDirectory() {
        return SAVE_DIRECTORY;
    }

    @FXML
    void exitApplication() {
        Platform.runLater(() -> {            
            try {
                onHgHubMessage.dispatch(new HgHubMessage(EventMessageType.DISCONNECT, null, null));
            } catch (Exception ex) {
                //Do nothing
            }
            System.exit(0);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        ReadConfig();
        titleLabel.setFont(Font.loadFont(getClass().getResourceAsStream("fonts/RobotoSlab-Regular.ttf"), 55));
        startSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 5000, 100, 100));
        UserAngleExtension.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 72, 0, 1));
        UserAngleFlexion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 72, 72, 1));
        UserExtensionTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 4, 1));
        UserFlexionTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 4, 1));
        UserHoldTime.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 2, 1));
        ZeroPositionText.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 72, 0, 1));
        TightPositionText.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 0, 1));
        TrainingWeightText.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 0, 10));
        CounterweightUserText.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 0, 1));
        SessionNumberText.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 0, 1));
        TrainingNumberText.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 18, 0, 1));
        movementExtensionLabel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 72, 0, 1));
        movementExtensionLabel.editorProperty().get().setAlignment(Pos.CENTER);
        movementFlexionLabel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 72, 0, 1));
        movementFlexionLabel.editorProperty().get().setAlignment(Pos.CENTER);
        movementZeroLabel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 72, 0, 1));
        movementZeroLabel.editorProperty().get().setAlignment(Pos.CENTER);
        boxConnect.setDisable(true);
        boxAquisition.setDisable(true);
        DisconnectButton.setDisable(true);
        StartButton.setDisable(true);
        StopButton.setDisable(true);
        AngleCaliGrid.setDisable(true);
        PressureButton.setDisable(true);
        tabCalib.setDisable(true);
        tabMovement.setDisable(true);
        movementBackButton.setDisable(true);
        movementCancelButton.setDisable(true);
        tabCounter.setDisable(true);
        tabIsometric.setDisable(true);
        IsoBox.getChildren().add(barchartiso);
        HBox.setHgrow(barchartiso, Priority.ALWAYS);
        tabExercise.setDisable(true);
        StartExerciseButton.setDisable(true);
        WarmUpButton.setDisable(true);
        StopExerciseButton.setDisable(true);
        ExtensionAngleLabel.setText("");
        IntAngleLabel.setText("");
        FlexionAngleLabel.setText("");
        PressureLabel.setText("");
        CountInstructionLabel.setText(bundle.getString("tab.counterweight.label.step0"));
        countBackButton.setDisable(true);
        countCancelButton.setDisable(true);
        AngleGauge.setNeedleColor(Color.valueOf("#06344f"));
        AngleGauge.setNeedleBorderColor(Color.BLACK);
        AngleGauge1.setNeedleColor(Color.valueOf("#f99133"));
        AngleGauge1.setNeedleBorderColor(Color.BLACK);
        AngleGauge1.setVisible(false);
        ForceGauge.setGradientBarStops(new Stop(0, Color.valueOf("#064266")), new Stop(0.3, Color.valueOf("#064266")), new Stop(0.7, Color.valueOf("#064266")));
        KbzGrid.setVisible(false);
        ExerciseText.setVisible(false);
        ExerciseImage.setVisible(false);
        lockImageCalibration.setDisable(true);
        ProgressConnect.setVisible(false);
        ProgressConnectHome.setVisible(false);
        try {
            tabHome.setGraphic(new ImageView(new Image(getClass().getResource("images/home.png").toURI().toString(), 45, 45, true, true)));
            tabUser.setGraphic(new ImageView(new Image(getClass().getResource("images/user-icon.png").toURI().toString(), 35, 35, true, true)));
            tabIttm.setGraphic(new ImageView(new Image(getClass().getResource("images/ittm-icon.png").toURI().toString(), 35, 35, true, true)));
            tabCalib.setGraphic(new ImageView(new Image(getClass().getResource("images/calibration-icon.png").toURI().toString(), 35, 35, true, true)));
            tabMovement.setGraphic(new ImageView(new Image(getClass().getResource("images/movement-icon.png").toURI().toString(), 35, 35, true, true)));
            tabCounter.setGraphic(new ImageView(new Image(getClass().getResource("images/counterweight-icon.png").toURI().toString(), 35, 35, true, true)));
            tabIsometric.setGraphic(new ImageView(new Image(getClass().getResource("images/isometric-icon.png").toURI().toString(), 35, 35, true, true)));
            tabExercise.setGraphic(new ImageView(new Image(getClass().getResource("images/exercise-icon.png").toURI().toString(), 35, 35, true, true)));
            tabConnect.setGraphic(new ImageView(new Image(getClass().getResource("images/connect-icon.png").toURI().toString(), 40, 40, true, true)));
            tabInfo.setGraphic(new ImageView(new Image(getClass().getResource("images/info.png").toURI().toString(), 40, 40, true, true)));
        } catch (URISyntaxException ex) {
            VIEWER_LOGGER.error("Load images tabs: " + ex.getMessage());
        }
        LanguageList.getItems().addAll("English", "Deutsch", "Português");
        LanguageList.setCellFactory((ListView<String> p) -> new ListCell<String>() {
            private final ImageView imageView;
            private Label label;
            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                imageView = new ImageView();
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new Image(getClass().getResource("images/" + item + ".png").toURI().toString()));
                        imageView.setFitWidth(50);
                        imageView.setPreserveRatio(true);
                        label = new Label(item, imageView);
                        label.setFont(Font.font(24));
                        setGraphic(label);
                    } catch (URISyntaxException ex) {
                        VIEWER_LOGGER.warn("Initialise, URISyntaxException = " + ex.getMessage());
                    }
                }
            }
        });
        LanguageList.getSelectionModel().selectFirst();
        this.LanguageListPress();
        if (hub_sensorization.equals("false")){
            HeadBarL.setVisible(false);
            HeadBarR.setVisible(false);
            HandsBarL.setVisible(false);
            HandsBarR.setVisible(false);    
            BackBar.setVisible(false);
            LegsBarL.setVisible(false);
            LegsBarR.setVisible(false);
            FootBarL.setVisible(false);
            FootBarR.setVisible(false);    
            HeadCircleL.setVisible(false);
            HeadCircleR.setVisible(false);   
            HandsCircleL.setVisible(false);
            HandsCircleR.setVisible(false);
            BackCircle.setVisible(false);
            LegsCircleL.setVisible(false);
            LegsCircleR.setVisible(false);
            FootCircleL.setVisible(false);
            FootCircleR.setVisible(false); 
            HeadCircleL1.setVisible(false);
            HeadCircleR1.setVisible(false);   
            HandsCircleL1.setVisible(false);  
            HandsCircleR1.setVisible(false);      
            BackCircle1.setVisible(false);
        } else if (hub_sensorization.equals("true2")){            
            HeadBarL.setVisible(false);
            HeadBarR.setVisible(false);
            HandsBarL.setVisible(false);
            HandsBarR.setVisible(false);    
            BackBar.setVisible(false);
            LegsBarL.setVisible(true);
            LegsBarR.setVisible(true);
            FootBarL.setVisible(true);
            FootBarR.setVisible(true);    
            HeadCircleL.setVisible(true);
            HeadCircleR.setVisible(true);   
            HandsCircleL.setVisible(true);
            HandsCircleR.setVisible(true);
            BackCircle.setVisible(true);
            LegsCircleL.setVisible(true);
            LegsCircleR.setVisible(true);
            FootCircleL.setVisible(true);
            FootCircleR.setVisible(true); 
            HeadCircleL1.setVisible(true);
            HeadCircleR1.setVisible(true);   
            HandsCircleL1.setVisible(true);  
            HandsCircleR1.setVisible(true);      
            BackCircle1.setVisible(true);            
        } else if (hub_sensorization.equals("true")){           
            HeadBarL.setVisible(false);
            HeadBarR.setVisible(false);
            HandsBarL.setVisible(false);
            HandsBarR.setVisible(false);    
            BackBar.setVisible(false);
            LegsBarL.setVisible(true);
            LegsBarR.setVisible(true);
            FootBarL.setVisible(false);
            FootBarR.setVisible(false);    
            HeadCircleL.setVisible(true);
            HeadCircleR.setVisible(true);   
            HandsCircleL.setVisible(true);
            HandsCircleR.setVisible(true);
            BackCircle.setVisible(true);
            LegsCircleL.setVisible(true);
            LegsCircleR.setVisible(true);
            FootCircleL.setVisible(false);
            FootCircleR.setVisible(false); 
            HeadCircleL1.setVisible(true);
            HeadCircleR1.setVisible(true);   
            HandsCircleL1.setVisible(true);  
            HandsCircleR1.setVisible(true);      
            BackCircle1.setVisible(true);            
        } else if (hub_sensorization.equals("test")){
            HeadBarL.setVisible(true);
            HeadBarR.setVisible(true);
            HandsBarL.setVisible(true);
            HandsBarR.setVisible(true);    
            BackBar.setVisible(true);
            LegsBarL.setVisible(true);
            LegsBarR.setVisible(true);
            FootBarL.setVisible(true);
            FootBarR.setVisible(true);    
            HeadCircleL.setVisible(true);
            HeadCircleR.setVisible(true);   
            HandsCircleL.setVisible(true);
            HandsCircleR.setVisible(true);
            BackCircle.setVisible(true);
            LegsCircleL.setVisible(true);
            LegsCircleR.setVisible(true);
            FootCircleL.setVisible(true);
            FootCircleR.setVisible(true); 
            HeadCircleL1.setVisible(true);
            HeadCircleR1.setVisible(true);   
            HandsCircleL1.setVisible(true);  
            HandsCircleR1.setVisible(true);      
            BackCircle1.setVisible(true);            
        }
        ittmApi = IttmApi.getInstance();
        HgMqttClient hgMqttClient = HgMqttClient.getInstance();
        IttmTrainingManager ittmManager = IttmTrainingManager.getInstance();
        HubHgMessageListener hubHgMessageListener = new HubHgMessageListener(this, ittmManager);
        hgMqttClient.onHubHgMessage().addListener(hubHgMessageListener);
        ittmManager.onHubHgMessage().addListener(hubHgMessageListener);
        this.onHgHubMessage = new EventDispatcher<>();
        this.onHgHubMessage().addListener(new HgHubMessageListener(hgMqttClient, ittmManager));
        try {
            hgMqttClient.initialise();
        } catch (HgMqttClientException ex) {
            VIEWER_LOGGER.error(ex.getMessage());
            System.exit(-1);
        }
        lockMain(true);
    }

    private EventDispatcher<HgHubMessage> onHgHubMessage;

    public EventDispatcher<HgHubMessage> onHgHubMessage() {
        return onHgHubMessage;
    }
}
