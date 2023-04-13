package pt.uninova.s4h.healthgateway.gui;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main Class.
 */

public class PhysiotherapyGui extends Application {
    private static String machine;
    private static String sensorization;
    private static String loadcell;
    private static String directory;
    
    public void setParm(String machine, String sensorization, String loadcell, String directory){
        this.machine = machine;
        this.sensorization = sensorization;
        this.loadcell = loadcell;
        this.directory = directory;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Locale locale = new Locale("en", "EN");
        ResourceBundle bundle = ResourceBundle.getBundle("pt.uninova.s4h.healthgateway.gui.bundles.bundle",locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"),bundle);
        FXMLDocumentController fxmldocument = new FXMLDocumentController(machine,sensorization,loadcell,directory);
        loader.setController(fxmldocument);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon_s4h.png")));
        stage.setTitle("Health Gateway");
        stage.show();
        stage.setMaximized(true);  
        stage.setOnCloseRequest((WindowEvent t) -> {
            fxmldocument.exitApplication();
            Platform.exit();
        });     
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
