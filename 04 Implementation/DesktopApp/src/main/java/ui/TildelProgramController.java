package ui;

import entities.Bruger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    public void initialize() {
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
        oevelseListener = (observable, oldValue, newValue) -> listViewProgram.getItems().add(newValue);
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
}
