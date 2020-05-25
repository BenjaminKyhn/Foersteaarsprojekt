package view;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.BrugerFacade;
import model.exceptions.ForkertPasswordException;

/** @author Benjamin */
public class StartController {
    private BrugerFacade brugerFacade;

    @FXML
    private AnchorPane startAnchorPane;

    @FXML
    private ImageView logoImageView;

    @FXML
    private GridPane startGridPane;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();

        /** Lav TextFields, Buttons, Labels og ImageView */
        TextField tfEmail = new TextField();
        PasswordField tfPassword = new PasswordField();
        Label lblEmail = new Label("Email:");
        Label lblPassword = new Label("Password:");

        HBox buttonHolder = new HBox();
        Button btnLogInd = new Button("Log ind");
        Button btnOpretBruger = new Button("Opret Bruger");
        buttonHolder.setSpacing(17);
        buttonHolder.getChildren().addAll(btnLogInd, btnOpretBruger);
        Image image = new Image("Logo2x.png");

        /** temporary */
        tfEmail.setText("fys@frbsport.dk");
        tfPassword.setText("morsfødselsdag228");

        /** Sæt indstillingerne på startGridPane */
        startGridPane.setHgap(5);
        startGridPane.setVgap(10);
        startGridPane.add(lblEmail, 0, 0);
        startGridPane.add(tfEmail, 1, 0);
        startGridPane.add(lblPassword, 0, 1);
        startGridPane.add(tfPassword, 1, 1);
        startGridPane.add(buttonHolder, 1, 2);
        startGridPane.setAlignment(Pos.CENTER);

        /** Sæt UI-elementer til at skalere */
        logoImageView.setImage(image);
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            logoImageView.setX(startAnchorPane.getWidth() / 2 - logoImageView.getFitWidth() / 2);
        };
        startAnchorPane.widthProperty().addListener(redraw);

        /** Sæt events på knapperne */
        btnLogInd.setOnAction(event -> {
            try {
                if (tfEmail.getText().isEmpty()){
                    logIndFejlPopup("Fejl: Emailfeltet er tomt");
                    return;
                }
                if (tfPassword.getText().isEmpty()){
                    logIndFejlPopup("Fejl: Passwordfeltet er tomt");
                    return;
                }
                try {
                    brugerFacade.logInd(tfEmail.getText(), tfPassword.getText());
                } catch (ForkertPasswordException fpe){
                    logIndFejlPopup("Forkert password indtastet");
                }
            } catch (Exception e){
                logIndFejlPopup("Fejl i logind");
            }
            if (brugerFacade.getAktivBruger() != null)
                skiftTilMenuScene();
        });
        btnOpretBruger.setOnAction(event -> skiftTilOpretBrugerScene());
    }

    public void skiftTilMenuScene() {
        Parent menuLoader = null;
        try {
            menuLoader = FXMLLoader.load(getClass().getResource("../Menu.fxml"));
        }
        catch (Exception e){
        }
        Scene menuScene = new Scene(menuLoader);

        Stage stage = (Stage) startAnchorPane.getScene().getWindow();
        stage.setScene(menuScene);
    }

    public void skiftTilOpretBrugerScene(){
        Parent opretBrugerLoader = null;
        try {
            opretBrugerLoader = FXMLLoader.load(getClass().getResource("../OpretBruger.fxml"));
        }
        catch (Exception e){
        }
        Scene opretBrugerScene = new Scene(opretBrugerLoader);

        Stage stage = (Stage) startAnchorPane.getScene().getWindow();
        stage.setScene(opretBrugerScene);
    }

    public void logIndFejlPopup(String infoText) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../SystemBeskedPopup.fxml"));
        try {
            root = fxmlLoader.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assert root != null;

        Scene popupScene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Fejlbesked");
        stage.setScene(popupScene);
        stage.show();

        OpretBrugerPopupController opretBrugerPopupController = fxmlLoader.getController();
        opretBrugerPopupController.getTxtLabel().setText(infoText);
    }
}