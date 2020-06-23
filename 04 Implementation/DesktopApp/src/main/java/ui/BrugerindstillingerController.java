package ui;

import entities.Bruger;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.BrugerFacade;

import java.io.IOException;

/** @author Benjamin */
public class BrugerindstillingerController {
    private BrugerFacade brugerFacade;
    private Bruger aktivBruger;

    @FXML
    private AnchorPane biAnchorPane;

    @FXML
    private VBox indholdVBox;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Circle brugerFotoCircle;

    @FXML
    private Label lblNavn, lblEmail;

    @FXML
    private Button btnTilbage;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        aktivBruger = brugerFacade.getAktivBruger();

        /* Indlæs brugerens oplysninger */
        if (aktivBruger.getFotoURL() != null)
            brugerFotoCircle.setFill(new ImagePattern(new Image(aktivBruger.getFotoURL()), 0, 0, 1, 1, true));
        else
            brugerFotoCircle.setFill(new ImagePattern(new Image("intetBillede.png")));
        if (aktivBruger.getNavn() != null)
            lblNavn.setText(aktivBruger.getNavn());
        if (aktivBruger.getEmail() != null)
            lblEmail.setText(aktivBruger.getEmail());
    }

    public void indlaesSletBruger(){
        indholdVBox.getChildren().clear();

        /* indlæs fxml */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SletBruger.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException io) {
            io.printStackTrace();
        }

        indholdVBox.getChildren().add(root);
    }

    public void indlaesSkiftPassword(){
        indholdVBox.getChildren().clear();

        /* indlæs fxml */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SkiftPassword.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException io) {
            io.printStackTrace();
        }

        indholdVBox.getChildren().add(root);
    }

    public void indlaesUploadBillede(){
        indholdVBox.getChildren().clear();

        /* indlæs fxml */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UploadBillede.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException io) {
            io.printStackTrace();
        }

        indholdVBox.getChildren().add(root);

        //TODO implementer UploadBillede
    }

    public void logUd() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Start.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = (Stage) biAnchorPane.getScene().getWindow();
        stage.setScene(scene);

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
        Scene scene = new Scene(root);

        Stage stage = (Stage) biAnchorPane.getScene().getWindow();
        stage.setScene(scene);
    }
}
