package ui;

import entities.Bruger;
import entities.exceptions.BehandlerFindesAlleredeException;
import entities.exceptions.ForkertRolleException;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.BrugerFacade;
import database.DatabaseManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

/**
 * @author Tommy
 */
public class PatientRegisterController {
    @FXML
    private AnchorPane patientregisterAnchorPane;

    @FXML
    private TableView<Bruger> patientTableView;

    @FXML
    private TableColumn<Bruger, String> patientNavn, patientEmail, patientBehandler, patientSidsteCheck;

    @FXML
    private ChoiceBox behandlerChoiceBox;

    @FXML
    private Button btnOpretPatient, btnSletPatient, btnTilknytBehandler, btnTilbage;

    @FXML
    private MenuBar menuBar;

    BrugerFacade brugerFacade;
    DatabaseManager databaseManager;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        databaseManager = DatabaseManager.getInstance();

        patientNavn.setCellValueFactory(new PropertyValueFactory<>("navn"));
        patientEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        patientBehandler.setCellValueFactory(new PropertyValueFactory<>("behandlere"));

        ObservableList<Bruger> patienter = FXCollections.observableList(brugerFacade.hentPatienter());
        patientTableView.setItems(patienter);

        fyldChoiceBox();

        /* Tilføj et events til knapperne */
        btnOpretPatient.setOnMouseClicked(e -> opretPatient());
        btnSletPatient.setOnMouseClicked(e -> {
            Bruger patient = patientTableView.getSelectionModel().getSelectedItem();
            sletPatient(patient);
        });
        btnTilknytBehandler.setOnMouseClicked(e -> {
            try {
                tilknytBehandler();
            } catch (ForkertRolleException fre) {
                popupWindow("Brugeren er ikke behandler");
            } catch (BehandlerFindesAlleredeException bfae) {
                popupWindow("Behandleren er allerede tilknyttet denne patient");
            }
        });

        brugerFacade.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Ny Behandler")) {
                    databaseManager.opdaterBruger((Bruger) evt.getNewValue());
                }
            }
        });

        /* Sæt UI-elementer til at skalere med vinduets størrelse */
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            menuBar.setPrefWidth(patientregisterAnchorPane.getWidth() - btnTilbage.getPrefWidth());
            btnTilbage.setPrefWidth(btnTilbage.getPrefWidth());
        };
        patientregisterAnchorPane.widthProperty().addListener(redraw);
    }

    /**
     * @author Benjamin
     */
    public void tilknytBehandler() throws ForkertRolleException, BehandlerFindesAlleredeException {
        if (patientTableView.getSelectionModel().getSelectedItem() != null && behandlerChoiceBox.getSelectionModel().getSelectedItem() != null) {
            Bruger patient = patientTableView.getSelectionModel().getSelectedItem();
            String behandlerNavn = (String) behandlerChoiceBox.getSelectionModel().getSelectedItem();
            Bruger behandler = brugerFacade.hentBrugerMedNavn(behandlerNavn);
            brugerFacade.tilknytBehandler(patient, behandler);
            popupWindow("Behandleren er tilknyttet");
            patientTableView.refresh();
        }
    }

    public void fyldChoiceBox() {
        List<Bruger> behandlere = brugerFacade.hentBehandlere();

        for (int i = 0; i < behandlere.size(); i++) {
            behandlerChoiceBox.getItems().add(behandlere.get(i).getNavn());
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

    public void opretPatient() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/OpretBruger.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) patientregisterAnchorPane.getScene().getWindow();
        stage.setScene(scene);
    }

    public void sletPatient(Bruger patient) {
        if (patient == null) {
            popupWindow("Ingen patient valgt");
        } else {
            Parent root = null;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SletPatient.fxml"));
            try {
                root = fxmlLoader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert root != null;

            Scene popupScene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Slet Patient");
            stage.setScene(popupScene);
            stage.show();

            SletPatientController sletPatientController = fxmlLoader.getController();
            sletPatientController.initData(patient);
        }
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

        Stage stage = (Stage) patientregisterAnchorPane.getScene().getWindow();
        stage.setScene(scene);

        brugerFacade.logUd();
    }

    public void tilHovedmenu() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = (Stage) patientregisterAnchorPane.getScene().getWindow();
        stage.setScene(scene);
    }
}
