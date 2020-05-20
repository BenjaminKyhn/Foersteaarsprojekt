package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.BrugerFacade;
import persistence.DatabaseManager;

/**
 * @author Benjamin & Tommy
 */

public class Start extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        // Start scene
        Parent root = FXMLLoader.load(getClass().getResource("../Start.fxml"));
        Scene scene = new Scene(root);

        stage.setTitle("Fys Desktop App");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}