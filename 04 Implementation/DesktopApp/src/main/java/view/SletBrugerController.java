package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.BrugerFacade;

public class SletBrugerController {
    private BrugerFacade brugerFacade;

    @FXML
    private Button btnSletBruger;

    @FXML
    private TextField tfNavn, tfEmail, tfPassword;

    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();

    }

    public void sletBruger(){
//        brugerFacade.sletBruger();
    }
}
