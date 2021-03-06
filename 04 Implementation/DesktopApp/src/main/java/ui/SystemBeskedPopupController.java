package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/** @author Benjamin */
public class SystemBeskedPopupController {
    @FXML
    private AnchorPane popupAnchorPane;

    @FXML
    private Label txtLabel;

    @FXML
    private Button btnOK;

    public void initialize(){
        btnOK.setOnAction(event -> lukPopup());
    }

    public Label getTxtLabel() {
        return txtLabel;
    }

    public void setTxtLabel(Label txtLabel) {
        this.txtLabel = txtLabel;
    }

    public Button getBtnOK() {
        return btnOK;
    }

    public void setBtnOK(Button btnOK) {
        this.btnOK = btnOK;
    }

    public void lukPopup(){
        Stage stage = (Stage) popupAnchorPane.getScene().getWindow();
        stage.close();
    }
}