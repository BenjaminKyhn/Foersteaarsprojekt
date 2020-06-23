package ui;

import entities.Bruger;
import entities.exceptions.*;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.BrugerFacade;
import database.DatabaseManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * @author Benjamin
 */
public class OpretBrugerController {
    private BrugerFacade brugerFacade;
    private Bruger aktivBruger;
    private ArrayList<Bruger> brugere;

    @FXML
    private AnchorPane opretBrugerAnchorPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Button btnOpretBruger, btnTilbage, btnTest;

    @FXML
    private TextField tfNavn, tfEmail;

    @FXML
    private PasswordField pfPassword, pfGentagPassword;

    @FXML
    private CheckBox cbBehandler;

    @FXML
    private Label lblInfo;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        aktivBruger = brugerFacade.getAktivBruger();
        if (brugerFacade.hentBrugere() == null){
            brugere = new ArrayList<>();
            brugerFacade.setBrugere(brugere);
        }

        else {
            brugere = brugerFacade.hentBrugere();
        }

        /* Check om brugeren er logged ind og tilpas de visuelle elementer */
        if (aktivBruger == null)
            cbBehandler.setVisible(false);
        else{
            lblInfo.setText("OBS: Sæt flueben i tjekboksen 'Behandler', hvis brugeren er behandler hos klinikken. " +
                    "Lad tjekboksen være blank, hvis brugeren er patient hos klinikken.");
        }

        /* Tilføj observer på listen */
        brugerFacade.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("opretBruger"))
                    DatabaseManager.getInstance().gemBruger((Bruger) evt.getNewValue());
            }
        });

        /* Sæt events på knapperne */
        btnOpretBruger.setOnMouseClicked(event -> {
            try {
                brugerFacade.tjekNavn(tfNavn.getText());
                brugerFacade.tjekEmail(tfEmail.getText());
                brugerFacade.tjekPassword(pfPassword.getText());
                if (!tjekOmPasswordMatcher(pfPassword.getText(), pfGentagPassword.getText())) {
                    popupWindow("Fejl: Password matcher ikke");
                    return;
                }

                /* Tjek om checkbox'en er checked */
                boolean erBehandler = false;
                if (cbBehandler.isSelected())
                    erBehandler = true;

                /* Tilføj bruger til listen i BrugerManager */
                brugerFacade.opretBruger(tfNavn.getText(), tfEmail.getText(), pfPassword.getText(), erBehandler);

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

        btnTest.setOnMouseClicked(e ->{
            System.out.println(opretBrugerAnchorPane.getWidth());
            System.out.println(menuBar.getWidth());
        });

        menuBar.prefWidthProperty().bind(opretBrugerAnchorPane.widthProperty());

//        menuBox.prefWidthProperty().bind(opretBrugerAnchorPane.widthProperty());

//        /** Sæt UI-elementer til at skalere med vinduets størrelse */
//        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
//            menuBar.setPrefWidth(opretBrugerAnchorPane.getWidth() - btnTilbage.getPrefWidth());
//            btnTilbage.setPrefWidth(btnTilbage.getPrefWidth());
//        };
//        opretBrugerAnchorPane.widthProperty().addListener(redraw);
//        System.out.println(opretBrugerAnchorPane.widthProperty());
    }

    public void tilbage() {
        Parent root = null;
        try {
            if (aktivBruger == null)
                root = FXMLLoader.load(getClass().getResource("/fxml/Start.fxml"));
            if (aktivBruger != null)
                root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
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
                root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
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

    public boolean tjekOmPasswordMatcher(String password1, String password2) {
        boolean matcher = false;
        if (password1.equals(password2))
            matcher = true;
        return matcher;
    }

    public void logUd() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Start.fxml"));
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