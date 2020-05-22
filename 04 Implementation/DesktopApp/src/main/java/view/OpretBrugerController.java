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

/** @author Benjamin */
public class OpretBrugerController {
    @FXML
    AnchorPane opretBrugerAnchorPane;

    @FXML
    private ImageView logoImageView;

    @FXML
    GridPane opretBrugerGridPane;

    /** Lav TextFields, Buttons, Labels og ImageView */
    TextField tfBrugernavn = new TextField();
    TextField tfEmail = new TextField();
    TextField tfPassword = new TextField();
    TextField tfGentagPassword = new TextField();
    Label lblBrugernavn = new Label("Bruger:");
    Label lblEmail = new Label("Email:");
    Label lblPassword = new Label("Password:");
    Label lblGentagPassword = new Label("Gentag \npassword:");
    HBox buttonHolder = new HBox();
    Button btnTilbage = new Button("Tilbage");
    Button btnOpretBruger = new Button("Opret Bruger");
    Image image = new Image("Logo2x.png");

    public void initialize(){
        buttonHolder.setSpacing(17);
        buttonHolder.getChildren().addAll(btnTilbage, btnOpretBruger);

        /** Sæt indstillingerne på startGridPane */
        opretBrugerGridPane.setHgap(5);
        opretBrugerGridPane.setVgap(10);
        opretBrugerGridPane.add(lblBrugernavn, 0, 0);
        opretBrugerGridPane.add(tfBrugernavn, 1, 0);
        opretBrugerGridPane.add(lblEmail, 0, 1);
        opretBrugerGridPane.add(tfEmail, 1, 1);
        opretBrugerGridPane.add(lblPassword, 0, 2);
        opretBrugerGridPane.add(tfPassword, 1, 2);
        opretBrugerGridPane.add(lblGentagPassword,0,3);
        opretBrugerGridPane.add(tfGentagPassword, 1, 3);
        opretBrugerGridPane.add(buttonHolder, 1, 4);
        opretBrugerGridPane.setAlignment(Pos.CENTER);

        /** Sæt startImageViews indstillinger */
        logoImageView.setImage(image);
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            logoImageView.setX(opretBrugerAnchorPane.getWidth() / 2 - logoImageView.getFitWidth() / 2);
        };
        opretBrugerAnchorPane.widthProperty().addListener(redraw);

        btnTilbage.setOnMouseClicked(event -> tilbage());
    }

    public void tilbage() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Start.fxml"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assert root != null;
        Scene secondScene = new Scene(root);

        Stage stage = (Stage) opretBrugerAnchorPane.getScene().getWindow();
        stage.setScene(secondScene);
    }
}
