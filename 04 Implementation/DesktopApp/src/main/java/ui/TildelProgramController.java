package ui;

import database.DatabaseManager;
import entities.Bruger;
import entities.Oevelse;
import entities.Traeningsprogram;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import model.BrugerFacade;
import model.TraeningsprogramFacade;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * @author Tommy
 */
public class TildelProgramController {
    @FXML
    private AnchorPane tildelProgramAnchorPane;

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

    @FXML
    private Pane videoPane;

    private ChangeListener<String> oevelseListener;
    private BrugerFacade brugerFacade;
    private TraeningsprogramFacade traeningsprogramFacade;
    private DatabaseManager databaseManager;
    private ArrayList<Oevelse> oevelser;
    private Bruger valgtePatient;
    private MediaPlayer player;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        databaseManager = DatabaseManager.getInstance();
        traeningsprogramFacade = TraeningsprogramFacade.getInstance();
        oevelser = traeningsprogramFacade.hentOevelser();

        // Tilføj observer på gemProgram-metoden
        traeningsprogramFacade.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("gemProgram"))
                    DatabaseManager.getInstance().gemProgram((Traeningsprogram) evt.getNewValue());
            }
        });

        tableColumnNavn.setCellValueFactory(new PropertyValueFactory<>("navn"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        ObservableList<Bruger> patienter = FXCollections.observableList(brugerFacade.hentPatienter());
        tableViewPatient.setItems(patienter);

        // Når man klikker på patienten indlæses nuværende øvelser i listViewProgram,
        tableViewPatient.setOnMouseClicked(e -> indlaesProgram());

        ObservableList<String> kategorier = FXCollections.observableArrayList();
        for (int i = 0; i < oevelser.size(); i++) {
            if (!kategorier.contains(oevelser.get(i).getKategori()))
                kategorier.add(oevelser.get(i).getKategori());
        }
        choiceBoxKategori.setItems(kategorier);
        choiceBoxKategori.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                choiceBoxOevelse.getSelectionModel().selectedItemProperty().removeListener(oevelseListener);
                videoPane.getChildren().clear();
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
            MediaView videoView = new MediaView();
            videoView.setFitWidth(320);
            videoView.setFitHeight(180);
            Media media;
            videoPane.getChildren().add(videoView);
            switch (newValue) {
                case "Dødløft":
                    media = new Media(getClass().getResource("/videoer/doedloeft.mp4").toExternalForm());
                    player = new MediaPlayer(media);
                    videoView.setMediaPlayer(player);
                    break;
                case "Hoftebøjer":
                    media = new Media(getClass().getResource("/videoer/hofteboejer.mp4").toExternalForm());
                    player = new MediaPlayer(media);
                    videoView.setMediaPlayer(player);
                    break;
                case "Nakke":
                    media = new Media(getClass().getResource("/videoer/nakke.mp4").toExternalForm());
                    player = new MediaPlayer(media);
                    videoView.setMediaPlayer(player);
                    break;
                case "Planken på albuer og tær":
                    media = new Media(getClass().getResource("/videoer/planke.mp4").toExternalForm());
                    player = new MediaPlayer(media);
                    videoView.setMediaPlayer(player);
                    break;
                case "Firefodstående krum - svaj":
                    media = new Media(getClass().getResource("/videoer/svaj.mp4").toExternalForm());
                    player = new MediaPlayer(media);
                    videoView.setMediaPlayer(player);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + newValue);
            }
            player.play();
        };
    }

    @FXML
    private void afspilVideo(){
        player.play();
    }

    @FXML
    private void stopVideo(){
        player.stop();
    }

    @FXML
    private void fjernFraListe() {
        String valgt = listViewProgram.getSelectionModel().getSelectedItem();
        if (valgt != null) {
            listViewProgram.getItems().remove(valgt);
        }
    }

    @FXML
    private void tilfoejTilListe() {
        String oevelse = choiceBoxOevelse.getSelectionModel().getSelectedItem();
        if (oevelse != null) {
            listViewProgram.getItems().add(oevelse);
        }
    }

    private void styrketraening() {
        choiceBoxOevelse.getItems().clear();
        for (Oevelse oevelse : oevelser) {
            if (oevelse.getKategori().equals("Styrketræning"))
                choiceBoxOevelse.getItems().add(oevelse.getNavn());
        }
    }

    private void mobilitet() {
        choiceBoxOevelse.getItems().clear();
        for (Oevelse oevelse : oevelser) {
            if (oevelse.getKategori().equals("Mobilitet"))
                choiceBoxOevelse.getItems().add(oevelse.getNavn());
        }
    }

    private void stabilitet() {
        choiceBoxOevelse.getItems().clear();
        for (Oevelse oevelse : oevelser) {
            if (oevelse.getKategori().equals("Stabilitet"))
                choiceBoxOevelse.getItems().add(oevelse.getNavn());
        }
    }

    private void rygproblemer() {
        choiceBoxOevelse.getItems().clear();
        for (Oevelse oevelse : oevelser) {
            if (oevelse.getKategori().equals("Rygproblemer"))
                choiceBoxOevelse.getItems().add(oevelse.getNavn());
        }
    }

    private void tilfoejListener() {
        choiceBoxOevelse.getSelectionModel().selectedItemProperty().addListener(oevelseListener);
    }

    private void tildelProgram() {
        ArrayList<String> patientensOevelser = new ArrayList<>(listViewProgram.getItems());
        Traeningsprogram program = new Traeningsprogram(valgtePatient.getEmail(), patientensOevelser);
        traeningsprogramFacade.tildelProgram(program);
    }

    private void indlaesProgram(){
        valgtePatient = tableViewPatient.getSelectionModel().getSelectedItem();
        ArrayList<Traeningsprogram> programmer = traeningsprogramFacade.hentProgrammer();
        ObservableList<String> patientensOevelser = FXCollections.observableArrayList();
        for (int i = 0; i < programmer.size(); i++) {
            if (programmer.get(i).getPatientEmail().equals(valgtePatient.getEmail())) {
                ArrayList<String> oevelserTemp = programmer.get(i).getOevelser();
                for (int j = 0; j < oevelserTemp.size(); j++) {
                    patientensOevelser.add(oevelserTemp.get(j));
                }
            }
        }
        listViewProgram.setItems(patientensOevelser);
    }

    @FXML
    private void bekraeft() {
        if (valgtePatient == null || listViewProgram.getItems().size() == 0) {
            return;
        }

        tildelProgram();

        Parent menuLoader = null;
        try {
            menuLoader = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scene menuScene = new Scene(menuLoader);

        Stage stage = (Stage) tableViewPatient.getScene().getWindow();
        stage.setScene(menuScene);
    }

    public void tilbage() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = (Stage) tildelProgramAnchorPane.getScene().getWindow();
        stage.setScene(scene);

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

        Stage stage = (Stage) tildelProgramAnchorPane.getScene().getWindow();
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

        Stage stage = (Stage) tildelProgramAnchorPane.getScene().getWindow();
        stage.setScene(scene);
    }
}
