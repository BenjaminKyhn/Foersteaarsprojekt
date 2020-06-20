package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LukProgramPopup {

    private String svar = "";

    public String vis(String besked){
        Stage vindue = new Stage();
        vindue.initModality(Modality.APPLICATION_MODAL);
        vindue.setTitle("Advarsel");
        Label lblBesked = new Label(besked);

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
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(btnJa, btnNej, btnAnnuller);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(lblBesked, hBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(vBox);
        vindue.setScene(scene);
        vindue.showAndWait();

        return svar;
    }
}
