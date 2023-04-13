package pt.uninova.s4h.healthgateway.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * Class to control the login interface and perform user commands.
 */

public class FXMLLoginControllerCalib implements Initializable {
 
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button PasswordOkButton;
    @FXML
    private Button PasswordCancelButton; 
    @FXML
    private Label passwordLabel;    
    private static final String CALIBRATION_PASS = "1234"; 
    private static FXMLDocumentController GUI;
    private ResourceBundle bundle;
    
    FXMLLoginControllerCalib (FXMLDocumentController controller){
        GUI = controller;
    }
    
    @FXML
    void CancelButtonPress(ActionEvent event) {
        Platform.runLater(() -> {
            Stage stage = (Stage) PasswordCancelButton.getScene().getWindow();
            stage.close();            
        });
    }

    @FXML
    void OkButtonPress(ActionEvent event) {
        Platform.runLater(() -> {
            if (passwordField.getText().equals(CALIBRATION_PASS)) {          
                GUI.unlockCalib();
                Stage stage = (Stage) PasswordOkButton.getScene().getWindow();
                stage.close();
            } else {
                passwordLabel.setText(bundle.getString("login.label.error"));
            }
        });
    }   
    
    @FXML
    void EnterKey(ActionEvent event){
        OkButtonPress(event);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        passwordLabel.setText("");
    }    
}
