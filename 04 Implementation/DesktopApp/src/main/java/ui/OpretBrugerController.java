package ui;

import entities.Bruger;
import entities.exceptions.*;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import usecases.BrugerFacade;
import usecases.ObserverbarListe;
import database.DatabaseManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Benjamin
 */
public class OpretBrugerController {
    private BrugerFacade brugerFacade;
    private Bruger aktivBruger;
    private ObserverbarListe<Bruger> brugere;

    @FXML
    private AnchorPane opretBrugerAnchorPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Button btnOpretBruger, btnTilbage;

    @FXML
    private TextField tfNavn, tfEmail;

    @FXML
    private PasswordField pfPassword, pfGentagPassword;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        aktivBruger = brugerFacade.getAktivBruger();
        if (brugerFacade.hentBrugere() == null){
            brugere = new ObserverbarListe<>();
            brugerFacade.setBrugere(brugere);
        }

        else {
            brugere = (ObserverbarListe<Bruger>) brugerFacade.hentBrugere();
        }

        /** Tilføj observer på listen */
        brugere.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Ny Addition"))
                    DatabaseManager.getInstance().gemBruger((Bruger) evt.getNewValue());
            }
        });

        /** Sæt events på knapperne */
        btnOpretBruger.setOnMouseClicked(event -> {
            try {
                brugerFacade.tjekNavn(tfNavn.getText());
                brugerFacade.tjekEmail(tfEmail.getText());
                brugerFacade.tjekPassword(pfPassword.getText());
                if (!tjekOmPasswordMatcher(pfPassword.getText(), pfGentagPassword.getText())) {
                    popupWindow("Fejl: Password matcher ikke");
                    return;
                }

                /** Tilføj bruger til listen i BrugerManager */
                brugerFacade.opretBruger(tfNavn.getText(), tfEmail.getText(), pfPassword.getText());

                popupWindow("Brugeren er oprettet");
            } catch (TomNavnException tne) {
                popupWindow("Fejl: Navnefeltet kan ikke være tomt");
            } catch (TomEmailException tee) {
                popupWindow("Fejl: Emailfeltet kan ikke være tomt");
            } catch (EksisterendeBrugerException ebe) {
                popupWindow("Fejl: Brugeren eksisterer allerede");
            } catch (TomPasswordException tpe) {
                popupWindow("Fejl: Passwordfeltet kan ikke være tomt");
            } catch (PasswordLaengdeException ple) {
                popupWindow("Fejl: Password skal være mellem 6 og 20 tegn");
            } catch (BrugerErIkkeBehandlerException blie) {
                popupWindow("Fejl: Du er allerede logged ind");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /** Sæt UI-elementer til at skalere med vinduets størrelse */
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            menuBar.setMinWidth(opretBrugerAnchorPane.getWidth() - btnTilbage.getPrefWidth());
            btnTilbage.setMinWidth(btnTilbage.getPrefWidth());
        };
        opretBrugerAnchorPane.widthProperty().addListener(redraw);
    }

    public void tilbage() {
        Parent root = null;
        try {
            if (aktivBruger == null)
                root = FXMLLoader.load(getClass().getResource("../Start.fxml"));
            if (aktivBruger != null)
                root = FXMLLoader.load(getClass().getResource("../Menu.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = (Stage) opretBrugerAnchorPane.getScene().getWindow();
        stage.setScene(scene);
    }

    public void skiftTilHovedMenu() {
        if (aktivBruger == null){
            popupWindow("Du er ikke logget ind");
        }

        else {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("../Menu.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert root != null;
            Scene scene = new Scene(root);

            Stage stage = (Stage) opretBrugerAnchorPane.getScene().getWindow();
            stage.setScene(scene);
        }
    }

    public void popupWindow(String infoText) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../SystemBeskedPopup.fxml"));
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

    public boolean tjekOmPasswordMatcher(String password1, String password2) {
        boolean matcher = false;
        if (password1.equals(password2))
            matcher = true;
        return matcher;
    }

    public void logUd() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Start.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = (Stage) opretBrugerAnchorPane.getScene().getWindow();
        stage.setScene(scene);

        brugerFacade.logUd();
    }
}

//TODO Christian skal kunne oprette brugere, selvom han er logged ind