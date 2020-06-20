package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LukProgramPopup {

    private String svar = "";

    public String vis(String besked){
        Stage vindue = new Stage();
        vindue.initModality(Modality.APPLICATION_MODAL);
        vindue.setTitle("Advarsel");
        vindue.setMinWidth(250);
        Label lblBesked = new Label();
        lblBesked.setText(besked);

        Button btnJa = new Button("Ja");
        Button btnNej = new Button("Nej");
        Button btnAnnuller = new Button("Annuller");

        btnJa.setOnAction(e -> {
            svar = "ja";
            vindue.close();
        });

        btnNej.setOnAction(e -> {
            svar = "nej";
            vindue.close();
        });

        btnAnnuller.setOnAction(e -> {
            vindue.close();
        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(lblBesked, btnJa, btnNej, btnAnnuller);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        vindue.setScene(scene);
        vindue.showAndWait();

        return svar;
    }
}
