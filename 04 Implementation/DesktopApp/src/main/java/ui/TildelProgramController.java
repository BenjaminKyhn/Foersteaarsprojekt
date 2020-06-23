package ui;

import database.DatabaseManager;
import entities.Bruger;
import entities.Oevelse;
import entities.Traeningsprogram;
import entities.exceptions.BrugerFindesIkkeException;
import javafx.application.Platform;
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

import javax.net.ssl.HttpsURLConnection;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    private URL url;
    private Label indlaesLabel;
    private Thread downloadTraad;
    private File videoFil;


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
        choiceBoxKategori.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                choiceBoxOevelse.getSelectionModel().selectedItemProperty().removeListener(oevelseListener);
                videoPane.getChildren().clear();
                choiceBoxOevelse.getItems().clear();
                for (Oevelse oevelse : oevelser) {
                    if (oevelse.getKategori().equals(newValue))
                        choiceBoxOevelse.getItems().add(oevelse.getNavn());
                }
                tilfoejListener();
            }
        });
        choiceBoxOevelse.getItems().add("Vælg kategori først");
        choiceBoxOevelse.getSelectionModel().select(0);
        oevelseListener = (observable, oldValue, newValue) -> {
            Oevelse valgteOevelse = null;
            for (Oevelse oevelse : oevelser) {
                if (oevelse.getNavn().equals(newValue)) {
                    valgteOevelse = oevelse;
                    break;
                }

            }
            if (valgteOevelse == null) {
                return;
            }

            videoPane.getChildren().clear();
            indlaesLabel = new Label("Indlæser video");
            videoPane.getChildren().add(indlaesLabel);

            url = null;
            try {
                url = new URL(valgteOevelse.getVideoURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                videoFil = File.createTempFile("oevelseVideo", ".mp4");
                videoFil.deleteOnExit();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            assert url != null;

            if (downloadTraad != null && downloadTraad.isAlive()) {
                downloadTraad.interrupt();
            }

            downloadTraad = new Thread(() -> {
                try {
                    downloadFil(url.toString());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                if (!Thread.currentThread().isInterrupted()) {
                    Platform.runLater(this::faerdigloadetMedie);
                }
            });
            downloadTraad.start();
        };
    }

    @FXML
    private void afspilVideo() {
        if (player != null) {
            player.play();
        }
    }

    @FXML
    private void stopVideo() {
        if (player != null) {
            player.stop();
        }
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

    public void downloadFil(String fileURL) throws IOException {
        URL url = new URL(fileURL);
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
        int responseCode = httpsConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {

            InputStream inputStream = httpsConn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(videoFil);

            int bytesRead = -1;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

        } else {
            System.out.println("Ingen fil at downloade. Serveren svarede med HTTPS code: " + responseCode);
        }
        httpsConn.disconnect();
    }

    public void faerdigloadetMedie() {
        MediaView videoView = new MediaView();
        videoView.setFitWidth(320);
        videoView.setFitHeight(180);
        videoPane.getChildren().add(videoView);
        Media media = new Media(videoFil.toURI().toString());
        player = new MediaPlayer(media);
        videoView.setMediaPlayer(player);
        videoPane.getChildren().remove(indlaesLabel);
        player.play();
    }

    private void tilfoejListener() {
        choiceBoxOevelse.getSelectionModel().selectedItemProperty().addListener(oevelseListener);
    }

    private void tildelProgram() throws BrugerFindesIkkeException {
        ArrayList<String> patientOevelser = new ArrayList<>(listViewProgram.getItems());
        traeningsprogramFacade.tildelProgram(valgtePatient.getEmail(), patientOevelser);
    }

    private void indlaesProgram() {
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

        try {
            tildelProgram();
        } catch (BrugerFindesIkkeException bfie){
            popupWindow("Brugeren eksisterer ikke.");
        }

        Parent menuLoader = null;
        try {
            menuLoader = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tildelProgramAnchorPane.getScene().setRoot(menuLoader);
    }

    public void tilbage() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        tildelProgramAnchorPane.getScene().setRoot(root);
    }

    public void logUd() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Start.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        tildelProgramAnchorPane.getScene().setRoot(root);

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
        tildelProgramAnchorPane.getScene().setRoot(root);
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
