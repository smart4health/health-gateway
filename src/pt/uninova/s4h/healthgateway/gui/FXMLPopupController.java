package pt.uninova.s4h.healthgateway.gui;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

/**
 * Class to control the popup.
 *
 * @author Fábio Januário and Vasco Delgado-Gomes
 * @email faj@uninova.pt, vmdg@uninova.pt
 * @version 12 November 2019 - v1.0.
 */
public class FXMLPopupController implements Initializable {

    @FXML
    private Label popupLabel;
    @FXML
    private Button popupButton;

    private ResourceBundle resourceBundle;

    private static final ch.qos.logback.classic.Logger POPUP_LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    void popupButtonPress(ActionEvent event) {
        Platform.runLater(() -> {
            Stage stage = (Stage) popupButton.getScene().getWindow();
            stage.close();
        });
    }

    public void setPopupLabel(String stringText) {
        popupLabel.setText(stringText);
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.resourceBundle = rb;
        assert popupLabel != null : "fx:id=\"popupLabel\" was not injected: check your FXML file 'FXMLPopup.fxml'.";
    }
}
