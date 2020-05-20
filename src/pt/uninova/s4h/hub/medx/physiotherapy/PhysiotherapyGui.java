package pt.uninova.s4h.hub.medx.physiotherapy;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main Class.
 *
 * @author Fábio Januário
 * @email faj@uninova.pt
 * @version 07 October 2019 - v1.0.
 */

public class PhysiotherapyGui extends Application {
    
    static String ITTM_URL;

    @Override
    public void start(Stage stage) throws Exception {
        Locale locale = new Locale("en", "EN");
        ResourceBundle bundle = ResourceBundle.getBundle("pt.uninova.s4h.hub.medx.physiotherapy.bundles.bundle",locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"),bundle);
        FXMLDocumentController fxmldocument = new FXMLDocumentController(ITTM_URL);
        loader.setController(fxmldocument);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon_s4h.png")));
        stage.setTitle("Health Gateway");
        stage.show();
        stage.setMaximized(true);  
        stage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });     
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ITTM_URL = args[0];
        launch(args);
    }
    
}
