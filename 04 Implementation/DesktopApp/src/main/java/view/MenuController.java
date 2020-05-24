package view;

import domain.Bruger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.BrugerFacade;

import java.io.IOException;

/**
 * @author Tommy og Patrick
 */
public class MenuController {
    private BrugerFacade brugerFacade;
    private Bruger aktivBruger;

    @FXML
    private AnchorPane menuAnchorPane;

    @FXML
    private Label beskederLabel, navnLabel, mailLabel, logUdLabel;

    @FXML
    private Circle fotoCircle;

    @FXML
    private ImageView logoImageView;

    public void initialize() throws IOException {
        brugerFacade = BrugerFacade.getInstance();
        aktivBruger = brugerFacade.getAktivBruger();

        Image image = new Image("Logo2x.png");
        logoImageView.setImage(image);

        /** Indlæs brugerens oplysninger */
        if (aktivBruger.getFotoURL() != null)
            fotoCircle.setFill(new ImagePattern(new Image(aktivBruger.getFotoURL())));
            // TODO gør så billedet ikke strækkes
        else
            fotoCircle.setFill(new ImagePattern(new Image("intetBillede.png")));
        if (aktivBruger.getNavn() != null)
            navnLabel.setText(aktivBruger.getNavn());
        if (aktivBruger.getEmail() != null)
            mailLabel.setText(aktivBruger.getEmail());

        /** Sæt egenskaber på labels */
        beskederLabel.setOnMouseClicked(event -> nextScene());
    }

    public void nextScene() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ChatWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) logoImageView.getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * @author Benjamin
     */
    public void logUd() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Start.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene secondScene = new Scene(root);

        Stage stage = (Stage) menuAnchorPane.getScene().getWindow();
        stage.setScene(secondScene);
    }
}
