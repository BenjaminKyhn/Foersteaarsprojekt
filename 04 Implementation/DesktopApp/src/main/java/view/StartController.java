package view;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class StartController {
    @FXML
    private AnchorPane startAnchorPane;

    @FXML
    private ImageView startImageView;

    @FXML
    private GridPane startGridPane;

    public void initialize() {
        // Create TextFields, Labels, Buttons and Images
        TextField tfUsername = new TextField();
        TextField tfPassword = new TextField();
        Label lblUsername = new Label("Bruger:");
        Label lblPassword = new Label("Kodeord:");
        Button btLogin = new Button("Login");
        Image image = new Image("Logo2x.png");

        // Set the properties of the GridPane
        startGridPane.setHgap(5);
        startGridPane.setVgap(10);
        startGridPane.add(lblUsername, 0, 0);
        startGridPane.add(tfUsername, 1, 0);
        startGridPane.add(lblPassword, 0, 1);
        startGridPane.add(tfPassword, 1, 1);
        startGridPane.add(btLogin, 1, 2);
        startGridPane.setAlignment(Pos.CENTER);

        // Create an ImageView and set its properties
        startImageView.setImage(image);
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            startImageView.setX(startAnchorPane.getWidth() / 2 - startImageView.getFitWidth() / 2);
        };
        startAnchorPane.widthProperty().addListener(redraw);
    }
}
