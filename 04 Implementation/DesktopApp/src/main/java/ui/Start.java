package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;

/**
 * @author Benjamin
 */

public class Start extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Locale.setDefault(new Locale("da", "DK"));

        // Datotesting
//        long start = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("25/06/2020 10:00:00").getTime();
//        long slut = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("25/06/2020 12:00:00").getTime();
//        Date startDato = new Date(start);
//        Date slutDato = new Date(slut);

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