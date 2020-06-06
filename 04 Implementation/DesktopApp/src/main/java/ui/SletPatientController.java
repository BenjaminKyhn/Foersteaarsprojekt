package ui;

import entities.Bruger;
import entities.exceptions.ForkertEmailException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.BrugerFacade;

public class SletPatientController {
    private BrugerFacade brugerFacade;
    private Bruger patient;

    @FXML
    private Label lblNavn;

    @FXML
    private TextField tfEmail, tfGentagEmail;

    @FXML
    private Button btnBekraeft;

    public void initialize() {
        btnBekraeft.setOnMouseClicked(e -> sletPatient());
    }

    public void initData(Bruger patient) {
        if (patient != null) {
            this.patient = patient;
            lblNavn.setText(patient.getNavn());
        }
    }

    public SletPatientController() {
        brugerFacade = BrugerFacade.getInstance();
    }

    public void sletPatient() {
        String email = tfEmail.getText();
        String gentagEmail = tfGentagEmail.getText();
        if (!email.equals(gentagEmail))
            popupWindow("Fejl: email matcher ikke");
        else {
            if (patient != null) {
                try {
                    brugerFacade.sletPatient(patient, email);
                    patient = null;
                } catch (ForkertEmailException fee) {
                    popupWindow("Fejl: Forkert email indtastet");
                }
                if (patient == null)
                    popupWindow("Patientens bruger er slettet");
            }
            else
                popupWindow("Patienten findes ikke i systemet");
        }
    }

    public void popupWindow(String infoText) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../SystemBeskedPopup.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;

        Scene popupScene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Infoboks");
        stage.setScene(popupScene);
        stage.show();

        SystemBeskedPopupController systemBeskedPopupController = fxmlLoader.getController();
        systemBeskedPopupController.getTxtLabel().setText(infoText);
    }
}
