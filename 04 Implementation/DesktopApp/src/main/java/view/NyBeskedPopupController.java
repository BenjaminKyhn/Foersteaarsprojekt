package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.BeskedFacade;
import model.exceptions.BrugerFindesIkkeException;

import java.io.IOException;

/** @author Benjamin */
public class NyBeskedPopupController {
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
            lukVindue();
        });
    }

    public void opretChat(String navn, String emne){
        try {
            beskedFacade.opretChat(navn, emne);
        }
        catch (BrugerFindesIkkeException e){
            popupWindow("Brugeren findes ikke");
        }
    }

    public void lukVindue(){
        Stage stage = (Stage) nyBeskedAnchorPane.getScene().getWindow();
        stage.close();

        // TODO få chatten i det andet vindue til at opdatere med den nye chat
    }

    public void popupWindow(String infoText) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../SystemBeskedPopup.fxml"));
        try {
            root = fxmlLoader.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assert root != null;

        Scene popupScene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Infoboks");
        stage.setScene(popupScene);
        stage.show();

        OpretBrugerPopupController opretBrugerPopupController = fxmlLoader.getController();
        opretBrugerPopupController.getTxtLabel().setText(infoText);
    }
}
