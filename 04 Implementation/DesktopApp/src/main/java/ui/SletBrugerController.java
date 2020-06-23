package ui;

import database.DatabaseManager;
import entities.Bruger;
import entities.exceptions.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.BrugerFacade;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/** @author Benjamin */
public class SletBrugerController {
    private BrugerFacade brugerFacade;
    private Bruger aktivBruger;

    @FXML
    private AnchorPane sletBrugerAnchorPane;

    @FXML
    private Button btnSletBruger;

    @FXML
    private PasswordField tfPassword, tfGentagPw;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        aktivBruger = brugerFacade.getAktivBruger();

        brugerFacade.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("sletBruger"))
                    DatabaseManager.getInstance().sletBruger((Bruger) evt.getNewValue());
            }
        });

        btnSletBruger.setOnMouseClicked(event -> sletBruger());
    }

    public void sletBruger() {
        String password = tfPassword.getText();
        String gentagPassword = tfGentagPw.getText();
        if (!password.equals(gentagPassword))
            popupWindow("Fejl: password matcher ikke");

        try {
            brugerFacade.sletBruger(aktivBruger, password);
            skiftTilStartscene();
        } catch (ForkertPasswordException fpe){
            popupWindow("Fejl: passwordet er forkert");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void skiftTilStartscene(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Start.fxml"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assert root != null;
        sletBrugerAnchorPane.getScene().setRoot(root);
    }

    public void popupWindow(String infoText) {
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
        stage.setTitle("Infoboks");
        stage.setScene(popupScene);
        stage.show();

        SystemBeskedPopupController systemBeskedPopupController = fxmlLoader.getController();
        systemBeskedPopupController.getTxtLabel().setText(infoText);
    }
}
