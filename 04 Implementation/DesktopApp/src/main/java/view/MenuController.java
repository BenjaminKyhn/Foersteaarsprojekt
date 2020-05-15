package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private Label beskederLabel, navnLabel, mailLabel;

    @FXML
    private Circle FotoCircle;

    @FXML
    private ImageView logoImageView;

    public void initialize() {
        Image image = new Image("Logo2x.png");
        logoImageView.setImage(image);

        Image foto = new Image("Christian.png");
        FotoCircle.setFill(new ImagePattern(foto));

        beskederLabel.setOnMouseClicked(event -> nextScene());

        navnLabel.setText("Navn: Christian Iuul");
        mailLabel.setText("Mail: mail@frbsport.dk");
    }

    public void nextScene() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ChatWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) logoImageView.getScene().getWindow();
        stage.setScene(scene);
    }
}
