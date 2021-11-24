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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.uninova.s4h.healthgateway.ittm.api.IttmApi;
import pt.uninova.s4h.healthgateway.ittm.api.IttmApiException;

/**
 * Class to control the login interface and perform user commands.
 *
 * @author Fábio Januário
 * @email faj@uninova.pt
 * @version 10 October 2019 - v1.0.
 */
public class FXMLLoginControllerMain implements Initializable {

    @FXML
    private Label titleLabel;    
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button PasswordOkButton;
    @FXML
    private Button PasswordCancelButton;
    @FXML
    private Button PasswordHMButton;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField userField;

    private static FXMLDocumentController GUI;
    private boolean login;

    private ResourceBundle bundle;

    FXMLLoginControllerMain(FXMLDocumentController controller, boolean login) {
        this.GUI = controller;
        this.login = login;
    }

    @FXML
    void OkButtonPress(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                IttmApi.getInstance().login(userField.getText(), passwordField.getText());
                GUI.setIttmUser(userField.getText());
                GUI.unlockMain(true);
                //HM ON
                Stage stage = (Stage) PasswordOkButton.getScene().getWindow();
                stage.close();
            } catch (IttmApiException ex) {
                passwordLabel.setText(bundle.getString("login.label.error.main"));
            }
        });
    }
    
    @FXML
    void EnterKey(ActionEvent event){
        OkButtonPress(event);
    }    

    @FXML
    void CancelButtonPress(ActionEvent event) {
        if (login){
            GUI.exitApplication();
        } else {
            HMButtonPress(null);
        }
    }

    @FXML
    void HMButtonPress(ActionEvent event) {
        //HM OFF
        GUI.unlockMain(false);
        Stage stage = (Stage) PasswordOkButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        passwordLabel.setText("");
        userField.setText(GUI.getIttmUser());
        if (login){
            titleLabel.setText(bundle.getString("login.title.main"));
        } else {
            PasswordHMButton.setVisible(false);
            titleLabel.setText(bundle.getString("login.title.main"));
        }
    }
}
