package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.BeskedFacade;

import java.io.IOException;

/** @author Benjamin */
public class NyBeskedController {
    private BeskedFacade beskedFacade;

    @FXML
    public Label lblEmail;

    @FXML
    public Label lblEmne;

    @FXML
    public TextField tfNavn;

    @FXML
    public TextField tfEmne;

    @FXML
    public Button btnSend;

    @FXML
    private AnchorPane nyBeskedAnchorPane;

    @FXML
    private GridPane nyBeskedGridPane;

    public void initialize() throws IOException {
        beskedFacade = BeskedFacade.getInstance();

        btnSend.setOnMouseClicked(event -> {
            opretChat(tfNavn.getText(), tfEmne.getText());
            lukPopup();
        });
    }

    public void opretChat(String navn, String emne){
        beskedFacade.opretChat(navn, emne);
    }

    public ChatWindowController hentChatWindowController(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatWindow.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException io) {
            io.printStackTrace();
        }

        /** Hent controlleren */
        return loader.getController();
    }

    public void lukPopup(){
        Stage stage = (Stage) nyBeskedAnchorPane.getScene().getWindow();
        stage.close();
    }
}
