package view;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.BrugerFacade;

import java.io.IOException;

/** @author Benjamin */
public class StartController {
    BrugerFacade brugerFacade = new BrugerFacade();

    @FXML
    private AnchorPane startAnchorPane;

    @FXML
    private ImageView logoImageView;

    @FXML
    private GridPane startGridPane;

    public StartController() throws IOException {
    }

    public void initialize() {
        /** Lav TextFields, Buttons, Labels og ImageView */
        TextField tfUsername = new TextField();
        TextField tfPassword = new TextField();
        Label lblUsername = new Label("Bruger:");
        Label lblPassword = new Label("Password:");
        HBox buttonHolder = new HBox();
        Button btLogin = new Button("Log ind");
        Button btCreateUser = new Button("Opret Bruger");
        buttonHolder.setSpacing(17);
        buttonHolder.getChildren().addAll(btLogin, btCreateUser);
        Image image = new Image("Logo2x.png");

        /** Sæt indstillingerne på startGridPane */
        startGridPane.setHgap(5);
        startGridPane.setVgap(10);
        startGridPane.add(lblUsername, 0, 0);
        startGridPane.add(tfUsername, 1, 0);
        startGridPane.add(lblPassword, 0, 1);
        startGridPane.add(tfPassword, 1, 1);
        startGridPane.add(buttonHolder, 1, 2);
        startGridPane.setAlignment(Pos.CENTER);

        /** Sæt startImageViews indstillinger */
        logoImageView.setImage(image);
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            logoImageView.setX(startAnchorPane.getWidth() / 2 - logoImageView.getFitWidth() / 2);
        };
        startAnchorPane.widthProperty().addListener(redraw);

        /** Sæt events på knapperne */
        btLogin.setOnAction(event -> skiftTilMenuScene());
        btCreateUser.setOnAction(event -> skiftTilOpretBrugerScene());
//        btCreateUser.setOnAction(event -> brugerFacade.opretBruger());
    }

    public void skiftTilMenuScene() {
        /** Scene 2 */
        Parent secondPageLoader = null;
        try {
            secondPageLoader = FXMLLoader.load(getClass().getResource("../Menu.fxml"));
        }
        catch (Exception e){
        }
        Scene secondScene = new Scene(secondPageLoader);

        Stage stage = (Stage) startAnchorPane.getScene().getWindow();
        stage.setScene(secondScene);
    }

    public void skiftTilOpretBrugerScene(){
        /** Scene 2 */
        Parent secondPageLoader = null;
        try {
            secondPageLoader = FXMLLoader.load(getClass().getResource("../OpretBruger.fxml"));
        }
        catch (Exception e){
        }
        Scene secondScene = new Scene(secondPageLoader);

        Stage stage = (Stage) startAnchorPane.getScene().getWindow();
        stage.setScene(secondScene);
    }
}
