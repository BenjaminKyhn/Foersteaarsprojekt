package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.BrugerFacade;

import java.io.IOException;

public class BrugerindstillingerController {
    private BrugerFacade brugerFacade;

    @FXML
    private AnchorPane biAnchorPane;

    @FXML
    private VBox indholdVBox;

    public void initialize() throws IOException {
        brugerFacade = BrugerFacade.getInstance();
    }

    public void indlaesSletBruger(){
        indholdVBox.getChildren().clear();

        /** Hent controlleren */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatWindowChats.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException io) {
            io.printStackTrace();
        }
        ChatWindowChatController controller = loader.getController();

        indholdVBox.getChildren().add(root);
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
