package view;

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
        Image image = new Image("Logo2x.png");
        startImageView.setImage(image);
        TextField tfUsername = new TextField();
        TextField tfPassword = new TextField();
        Label lblUsername = new Label("Bruger:");
        Label lblPassword = new Label("Kodeord:");
        Button btLogin = new Button("Login");
        startGridPane.setHgap(5);
        startGridPane.setVgap(10);
        startGridPane.add(lblUsername, 0, 0);
        startGridPane.add(tfUsername, 1, 0);
        startGridPane.add(lblPassword, 0, 1);
        startGridPane.add(tfPassword, 1, 1);
        startGridPane.add(btLogin, 1, 2);

        startGridPane.setAlignment(Pos.CENTER);
    }
}
