package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LukProgramPopupController {
    @FXML
    private AnchorPane popupAnchorPane;

    @FXML
    private Button btnJa, btnNej, btnAnnuller;

    @FXML
    private Label lblBesked;

    private String svar = "";

    public String vis(String besked){
        Stage stage = (Stage) popupAnchorPane.getScene().getWindow();

        lblBesked.setText(besked);

        btnJa.setOnAction(e -> {
            svar = "ja";
            stage.close();
        });

        btnNej.setOnAction(e -> {
            svar = "nej";
            stage.close();
        });

        btnAnnuller.setOnAction(e -> {
            stage.close();
        });

        return svar;
    }
}
