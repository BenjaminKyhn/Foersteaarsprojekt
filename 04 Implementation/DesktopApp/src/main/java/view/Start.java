package view;

import domain.Chat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import persistence.DatabaseManager;

/** @author Benjamin & Tommy */

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

        DatabaseManager databaseManager = DatabaseManager.getInstance();
        Chat chat = databaseManager.hentChat("test", "test", "test");
        System.out.println(chat.getBeskeder().get(0).getBesked());
    }

    public static void main(String[] args) {
        launch(args);
    }
}