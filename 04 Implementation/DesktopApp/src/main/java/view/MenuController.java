package view;

import domain.Bruger;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.BeskedFacade;
import model.BrugerFacade;
import persistence.DatabaseManager;

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
    private Rectangle topRectangle, bundRectangle;

    @FXML
    private Label lblBeskeder, navnLabel, mailLabel, lblLogUd, lblIndstillinger, lblPatientregister, lblTraeningsprogram, lblOpretPatient;

    @FXML
    private Circle fotoCircle;

    @FXML
    private ImageView logoImageView;

    /** @author Benjamin */
    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        aktivBruger = brugerFacade.getAktivBruger();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();

        /** Indlæs alle brugerens chats og send dem til BeskedFacade */
        beskedFacade.setChats(DatabaseManager.getInstance().hentChatsMedNavn(aktivBruger.getNavn()));

        /** Indlæs alle brugere og send dem til BrugereFacade */
        brugerFacade.setBrugere(DatabaseManager.getInstance().hentBrugere());

        Image image = new Image("Logo2x.png");
        logoImageView.setImage(image);

        /** Indlæs brugerens oplysninger */
        if (aktivBruger.getFotoURL() != null)
            fotoCircle.setFill(new ImagePattern(new Image(aktivBruger.getFotoURL()), 0, 0, 1, 1.3, true));
            // TODO find en måde at indsætte billede på, så det kan tage imod alle højde:bredde forhold. Det kan evt. gøres ved at lave en custom class, som extender ImageView og kalde setPreserveRatio(boolean)
        else
            fotoCircle.setFill(new ImagePattern(new Image("intetBillede.png")));
        if (aktivBruger.getNavn() != null)
            navnLabel.setText(aktivBruger.getNavn());
        if (aktivBruger.getEmail() != null)
            mailLabel.setText(aktivBruger.getEmail());

        /** Sæt egenskaber på labels */
        lblBeskeder.setOnMouseClicked(event -> skiftTilChatvindue());
        lblIndstillinger.setOnMouseClicked(event -> skiftTilBrugerindstillinger());
        lblPatientregister.setOnMouseClicked(event -> patientregister());
        lblLogUd.setOnMouseClicked(event -> logUd());
        lblOpretPatient.setOnMouseClicked(event -> opretPatient());
        lblTraeningsprogram.setOnMouseClicked(event -> traeningsprogram());

        /** Sæt UI-elementer til at skalere med vinduets størrelse */
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            topRectangle.setWidth(menuAnchorPane.getWidth());
            bundRectangle.setWidth(menuAnchorPane.getWidth());
        };
        menuAnchorPane.widthProperty().addListener(redraw);
    }

    public void skiftTilChatvindue() {
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

    public void skiftTilBrugerindstillinger(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Brugerindstillinger.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) logoImageView.getScene().getWindow();
        stage.setScene(scene);
    }

    public void logUd() {
        brugerFacade.logUd();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Start.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = (Stage) menuAnchorPane.getScene().getWindow();
        stage.setScene(scene);
    }

    public void opretPatient(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/OpretBruger.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) logoImageView.getScene().getWindow();
        stage.setScene(scene);
    }

    public void patientregister() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/PatientRegister.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Patientregister");
        stage.show();
    }

    public void traeningsprogram() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/TildelProgram.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) logoImageView.getScene().getWindow();
        stage.setScene(scene);
    }
}