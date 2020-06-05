package ui;

import entities.Bruger;
import entities.exceptions.BehandlerFindesAlleredeException;
import entities.exceptions.ForkertRolleException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.BrugerFacade;
import database.DatabaseManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * @author Tommy
 */
public class PatientRegisterController {

    @FXML
    private TableView<Bruger> patientTableView;

    @FXML
    private TableColumn<Bruger, String> patientNavn, patientEmail, patientBehandler, patientSidsteCheck;

    @FXML
    private ChoiceBox behandlerChoiceBox;

    @FXML
    private Button btnOpretPatient, btnSletPatient, btnTilknytBehandler;

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

        /** Tilføj et event til knappen */
        btnTilknytBehandler.setOnMouseClicked(e -> {
            try {
                tilknytBehandler();
            } catch (ForkertRolleException fre) {
                popupWindow("Brugeren er ikke behandler");
            } catch (BehandlerFindesAlleredeException bfae){
                popupWindow("Behandleren er allerede tilknyttet denne patient");
            }
        });

        brugerFacade.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Ny Behandler")){
                    databaseManager.opdaterBruger((Bruger) evt.getNewValue());
                }
            }
        });
    }

    /** @author Benjamin */
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
}
