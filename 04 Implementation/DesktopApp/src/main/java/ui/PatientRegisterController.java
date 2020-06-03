package ui;

import entities.Bruger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import unittests.usecases.BrugerFacade;
import database.DatabaseManager;

public class PatientRegisterController {

    @FXML
    private TableView<Bruger> patientTableView;

    @FXML
    private TableColumn<Bruger, String> patientNavn, patientEmail, patientSidsteCheck;

    BrugerFacade brugerFacade;
    DatabaseManager databaseManager;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        databaseManager = DatabaseManager.getInstance();

        patientNavn.setCellValueFactory(new PropertyValueFactory<>("navn"));
        patientEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        ObservableList<Bruger> patienter = FXCollections.observableList(brugerFacade.hentPatienter());
        patientTableView.setItems(patienter);
    }
}
