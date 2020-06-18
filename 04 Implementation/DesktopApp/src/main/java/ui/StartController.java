package ui;

import entities.Bruger;
import javafx.application.Platform;
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
import entities.exceptions.ForkertPasswordException;
import database.DatabaseManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * @author Benjamin
 */
public class StartController {
    private BrugerFacade brugerFacade;
    private DatabaseManager databaseManager;

    @FXML
    private AnchorPane startAnchorPane;

    @FXML
    private ImageView logoImageView;

    @FXML
    private GridPane startGridPane;

    private Label lblLoggerInd;
    private boolean fortrydLogIndCheck;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        databaseManager = DatabaseManager.getInstance();

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
        tfEmail.setText("sigurdo@gmail.com");
        tfPassword.setText("sigurdpw");

        lblLoggerInd = new Label("Logger ind.");
        lblLoggerInd.setVisible(false);

        /** Sæt indstillingerne på startGridPane */
        startGridPane.setHgap(5);
        startGridPane.setVgap(10);
        startGridPane.add(lblEmail, 0, 0);
        startGridPane.add(tfEmail, 1, 0);
        startGridPane.add(lblPassword, 0, 1);
        startGridPane.add(tfPassword, 1, 1);
        startGridPane.add(buttonHolder, 1, 2);
        startGridPane.add(lblLoggerInd, 1, 3);
        startGridPane.setAlignment(Pos.CENTER);

        /** Sæt UI-elementer til at skalere */
        logoImageView.setImage(image);
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            logoImageView.setX(startAnchorPane.getWidth() / 2 - logoImageView.getFitWidth() / 2);
        };
        startAnchorPane.widthProperty().addListener(redraw);


        /** Sæt events på knapperne */
        btnLogInd.setOnAction(event -> {

            if (tfEmail.getText().isEmpty()) {
                logIndFejlPopup("Fejl: Emailfeltet er tomt");
                return;
            }
            if (tfPassword.getText().isEmpty()) {
                logIndFejlPopup("Fejl: Passwordfeltet er tomt");
                return;
            }
            lblLoggerInd.setVisible(true);
            databaseManager.hentBrugerMedEmail(tfEmail.getText());

        });

        databaseManager.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if (propertyChangeEvent.getPropertyName().equals("hentBrugerMedEmail") && !fortrydLogIndCheck) {
                    lblLoggerInd.setVisible(false);
                    Bruger bruger = (Bruger) propertyChangeEvent.getNewValue();
                    if (bruger == null) {
                        Platform.runLater(() -> logIndFejlPopup("Bruger findes ikke"));
                        return;
                    }
                    ArrayList<Bruger> temp = new ArrayList<>();
                    temp.add(bruger);
                    brugerFacade.setBrugere(temp);
                    try {
                        if (!brugerFacade.logInd(tfEmail.getText(), tfPassword.getText())) {
                            Platform.runLater(() -> logIndFejlPopup("Fejl i logind"));
                        }

                        if (brugerFacade.getAktivBruger() != null)
                            brugerFacade.setBrugere(null);
                        Platform.runLater(() -> skiftTilMenuScene());
                    } catch (ForkertPasswordException e) {
                        Platform.runLater(() -> logIndFejlPopup("Forkert password indtastet"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnOpretBruger.setOnAction(event -> {
            fortrydLogIndCheck = true;
            skiftTilOpretBrugerScene();
        });
    }

    public void skiftTilMenuScene() {
        Parent menuLoader = null;
        try {
            menuLoader = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        } catch (Exception e) {
        }
        Scene menuScene = new Scene(menuLoader);

        Stage stage = (Stage) startAnchorPane.getScene().getWindow();
        stage.setScene(menuScene); //TODO Fix NullPointerException, som opstår, hvis man logger ud og prøver at logge ind igen (uden at lukke programmet)
    }

    public void skiftTilOpretBrugerScene() {
        Parent opretBrugerLoader = null;
        try {
            opretBrugerLoader = FXMLLoader.load(getClass().getResource("/fxml/OpretBruger.fxml"));
        } catch (Exception e) {
        }
        Scene opretBrugerScene = new Scene(opretBrugerLoader);

        Stage stage = (Stage) startAnchorPane.getScene().getWindow();
        stage.setScene(opretBrugerScene);
    }

    public void logIndFejlPopup(String infoText) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SystemBeskedPopup.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;

        Scene popupScene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Fejlbesked");
        stage.setScene(popupScene);
        stage.show();

        SystemBeskedPopupController systemBeskedPopupController = fxmlLoader.getController();
        systemBeskedPopupController.getTxtLabel().setText(infoText);
    }
}