package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.BrugerFacade;

import java.io.IOException;

public class BrugerindstillingerController {
    private BrugerFacade brugerFacade;

    @FXML
    private AnchorPane biAnchorPane;

    public void initialize() throws IOException {
        brugerFacade = BrugerFacade.getInstance();
    }

    public void logUd() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Start.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene secondScene = new Scene(root);

        Stage stage = (Stage) biAnchorPane.getScene().getWindow();
        stage.setScene(secondScene);

        brugerFacade.logUd();
    }

    public void tilHovedmenu() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Menu.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene secondScene = new Scene(root);

        Stage stage = (Stage) biAnchorPane.getScene().getWindow();
        stage.setScene(secondScene);
    }
}
