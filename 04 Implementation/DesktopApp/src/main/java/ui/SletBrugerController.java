package ui;

import entities.Bruger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import usecases.BrugerFacade;

/** @author Benjamin */
public class SletBrugerController {
    private BrugerFacade brugerFacade;
    private Bruger aktivBruger;

    @FXML
    private AnchorPane sletBrugerAnchorPane;

    @FXML
    private Button btnSletBruger;

    @FXML
    private PasswordField tfPassword, tfGentagPw;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        aktivBruger = brugerFacade.getAktivBruger();
        btnSletBruger.setOnMouseClicked(event -> sletBruger());
    }

    public void sletBruger() {
        String password = tfPassword.getText();
        String gentagPassword = tfGentagPw.getText();
        if (!password.equals(gentagPassword))
            System.out.println("Fejl: password matcher ikke");

        try {
            brugerFacade.sletBruger(aktivBruger, password);
            skiftTilStartscene();
        } catch (ForkertPasswordException fpe){
            System.out.println("Fejl: passwordet er forkert");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void skiftTilStartscene(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Start.fxml"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = (Stage) sletBrugerAnchorPane.getScene().getWindow();
        stage.setScene(scene);
    }
}
