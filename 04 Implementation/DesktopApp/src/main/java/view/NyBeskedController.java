package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/** @author Benjamin */
public class NyBeskedController {
    @FXML
    public Label lblEmail;

    @FXML
    public Label lblEmne;

    @FXML
    public TextField tfEmail;

    @FXML
    public TextField tfEmne;

    @FXML
    public Button btnSend;

    @FXML
    private AnchorPane nyBeskedAnchorPane;

    @FXML
    private GridPane nyBeskedGridPane;

    public void initialize() {
    }

    public void opretChat(String email, String emne){

    }
}
