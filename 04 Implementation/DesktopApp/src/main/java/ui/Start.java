package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Date;
import java.util.Locale;

/**
 * @author Benjamin
 */

public class Start extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Locale.setDefault(new Locale("da", "DK"));

        // Start scene
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Start.fxml"));
        Scene scene = new Scene(root);
        ;
        stage.setTitle("Frederiksberg Sportsklinik");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}