package ui;

import database.DatabaseManager;
import entities.Bruger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import usecases.BrugerFacade;
import usecases.TraeningsprogramFacade;

public class TildelProgramController {
    @FXML
    private TableView<Bruger> tableViewPatient;

    @FXML
    private TableColumn<Bruger, String> tableColumnNavn, tableColumnEmail;

    @FXML
    private ChoiceBox<String> choiceBoxKategori, choiceBoxOevelse;

    @FXML
    private ListView<String> listViewProgram;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Button btnTilbage;

    ChangeListener<String> oevelseListener;
    BrugerFacade brugerFacade;
    TraeningsprogramFacade traeningsprogramFacade;
    DatabaseManager databaseManager;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        databaseManager = DatabaseManager.getInstance();
        traeningsprogramFacade = new TraeningsprogramFacade();

        tableColumnNavn.setCellValueFactory(new PropertyValueFactory<>("navn"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        ObservableList<Bruger> patienter = FXCollections.observableList(brugerFacade.hentPatienter());
        tableViewPatient.setItems(patienter);

        ObservableList<String> kategorier = FXCollections.observableArrayList();
        kategorier.add("Styrketræning");
        kategorier.add("Mobilitet");
        kategorier.add("Stabilitet");
        kategorier.add("Rygproblemer");
        choiceBoxKategori.setItems(kategorier);
        choiceBoxKategori.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                choiceBoxOevelse.getSelectionModel().selectedItemProperty().removeListener(oevelseListener);
                switch (newValue.intValue()) {
                    case 0:
                        styrketraening();
                        break;
                    case 1:
                        mobilitet();
                        break;
                    case 2:
                        stabilitet();
                        break;
                    case 3:
                        rygproblemer();
                        break;
                }
                tilfoejListener();
            }
        });
        choiceBoxOevelse.getItems().add("Vælg kategori først");
        choiceBoxOevelse.getSelectionModel().select(0);
        oevelseListener = (observable, oldValue, newValue) -> {
            listViewProgram.getItems().add(newValue);
            traeningsprogramFacade.tilfoejOevelse(newValue);
        };
    }

    @FXML
    private void fjernFraListe() {
        String valgt = listViewProgram.getSelectionModel().getSelectedItem();
        if (valgt != null) {
            listViewProgram.getItems().remove(valgt);
            traeningsprogramFacade.fjernOevelse(valgt);
        }
    }

    private void styrketraening() {
        choiceBoxOevelse.getItems().clear();
        choiceBoxOevelse.getItems().add("Dødløft");
    }

    private void mobilitet() {
        choiceBoxOevelse.getItems().clear();
        choiceBoxOevelse.getItems().addAll("Hoftebøjer", "Nakke");
    }

    private void stabilitet() {
        choiceBoxOevelse.getItems().clear();
        choiceBoxOevelse.getItems().addAll("Planken på albuer og tær");
    }

    private void rygproblemer() {
        choiceBoxOevelse.getItems().clear();
        choiceBoxOevelse.getItems().addAll("Firefodstående krum - svaj");
    }

    private void tilfoejListener() {
        choiceBoxOevelse.getSelectionModel().selectedItemProperty().addListener(oevelseListener);
    }

    @FXML
    private void bekraeft() {
        Bruger patient = tableViewPatient.getSelectionModel().getSelectedItem();
        if (patient == null || listViewProgram.getItems().size() == 0) {
            return;
        }
        databaseManager.opdaterTraeningsprogram(patient, traeningsprogramFacade.hentListe());
        Parent menuLoader = null;
        try {
            menuLoader = FXMLLoader.load(getClass().getResource("../Menu.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scene menuScene = new Scene(menuLoader);

        Stage stage = (Stage) tableViewPatient.getScene().getWindow();
        stage.setScene(menuScene);
    }
}
