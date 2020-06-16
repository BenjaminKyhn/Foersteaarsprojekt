package ui;

import entities.exceptions.ForMangeTegnException;
import entities.exceptions.TomEmneException;
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
import entities.exceptions.BrugerFindesIkkeException;

/** @author Benjamin */
public class NyChatPopupController {
    private BeskedFacade beskedFacade;

    @FXML
    public Label lblEmail, lblEmne;

    @FXML
    public TextField tfNavn, tfEmne;

    @FXML
    public Button btnSend;

    @FXML
    private AnchorPane nyBeskedAnchorPane;

    @FXML
    private GridPane nyBeskedGridPane;

    public void initialize() {
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
        catch (BrugerFindesIkkeException bfie){
            popupWindow("Brugeren findes ikke");
        }
        catch (TomEmneException tee){
            popupWindow("Tomt emnefelt");
        }
        catch (ForMangeTegnException fmte){
            popupWindow("Du har skrevet mere end 50 tegn");
        }
    }

    public void lukVindue(){
        Stage stage = (Stage) nyBeskedAnchorPane.getScene().getWindow();
        stage.close();
    }

    public void popupWindow(String infoText) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SystemBeskedPopup.fxml"));
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

        SystemBeskedPopupController systemBeskedPopupController = fxmlLoader.getController();
        systemBeskedPopupController.getTxtLabel().setText(infoText);
    }
}