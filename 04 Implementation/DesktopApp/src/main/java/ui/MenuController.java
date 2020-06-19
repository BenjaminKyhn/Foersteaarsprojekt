package ui;

import entities.*;
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
import model.BookingFacade;
import model.BrugerFacade;
import database.DatabaseManager;
import model.TraeningsprogramFacade;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

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
    private Label lblBeskeder, navnLabel, mailLabel, lblLogUd, lblIndstillinger, lblPatientregister, lblTraeningsprogram, lblBooking;

    @FXML
    private Circle fotoCircle;

    @FXML
    private ImageView logoImageView;

    /**
     * @author Benjamin
     */
    public void initialize() {
        brugerFacade = BrugerFacade.getInstance();
        aktivBruger = brugerFacade.getAktivBruger();
        BeskedFacade beskedFacade = BeskedFacade.getInstance();
        TraeningsprogramFacade traeningsprogramFacade = TraeningsprogramFacade.getInstance();
        BookingFacade bookingFacade = BookingFacade.getInstance();

        /** Indlæs alle brugerens chats og send dem til BeskedFacade */
        if (beskedFacade.hentChats() == null) {
            DatabaseManager.getInstance().tilfoejObserver(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    if (propertyChangeEvent.getPropertyName().equals("hentChatsMedNavn")) {
                        @SuppressWarnings("unchecked")
                        ArrayList<Chat> chats = (ArrayList<Chat>) propertyChangeEvent.getNewValue();
                        beskedFacade.setChats(chats);
                    }
                }
            });
            DatabaseManager.getInstance().hentChatsMedNavn(aktivBruger.getNavn());
        }

        /** Indlæs alle brugere og send dem til BrugerFacade */
        if (brugerFacade.hentBrugere() == null) {
            DatabaseManager.getInstance().tilfoejObserver(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    if (propertyChangeEvent.getPropertyName().equals("hentBrugere")) {
                        @SuppressWarnings("unchecked")
                        ArrayList<Bruger> brugere = (ArrayList<Bruger>) propertyChangeEvent.getNewValue();
                        brugerFacade.setBrugere(brugere);
                    }
                }
            });
            DatabaseManager.getInstance().hentBrugere();
        }

        /** Indlæs alle øvelser og send dem til TraeningsprogramFacade */
        if (traeningsprogramFacade.hentOevelser() == null) {
            DatabaseManager.getInstance().tilfoejObserver(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    if (propertyChangeEvent.getPropertyName().equals("hentOevelser")) {
                        @SuppressWarnings("unchecked")
                        ArrayList<Oevelse> oevelser = (ArrayList<Oevelse>) propertyChangeEvent.getNewValue();
                        traeningsprogramFacade.angivOevelser(oevelser);
                    }
                }
            });
            DatabaseManager.getInstance().hentOevelser();
        }

        /** Indlæs alle træeningsprogrammer og send dem til TraeningsprogramFacade */
            DatabaseManager.getInstance().tilfoejObserver(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    if (propertyChangeEvent.getPropertyName().equals("hentProgrammer")) {
                        @SuppressWarnings("unchecked")
                        ArrayList<Traeningsprogram> programmer = (ArrayList<Traeningsprogram>) propertyChangeEvent.getNewValue();
                        traeningsprogramFacade.angivProgrammer(programmer);
                    }
                }
            });
            DatabaseManager.getInstance().hentProgrammer();

        /** Indlæs alle begivenheder og send dem til BookingFacade */
        if (bookingFacade.hentBegivenheder() == null) {
            DatabaseManager.getInstance().tilfoejObserver(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    if (propertyChangeEvent.getPropertyName().equals("hentBegivenheder")) {
                        @SuppressWarnings("unchecked")
                        ArrayList<Begivenhed> begivenheder = (ArrayList<Begivenhed>) propertyChangeEvent.getNewValue();
                        bookingFacade.angivBegivenheder(begivenheder);
                    }
                }
            });
            DatabaseManager.getInstance().hentBegivenheder(aktivBruger.getNavn());
        }

        /** Indlæs alle brugerens chats og send dem til BeskedFacade */
        if (beskedFacade.hentChats() == null) {
            DatabaseManager.getInstance().tilfoejObserver(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    if (propertyChangeEvent.getPropertyName().equals("hentChatsMedNavn")) {
                        @SuppressWarnings("unchecked")
                        ArrayList<Chat> chats = (ArrayList<Chat>) propertyChangeEvent.getNewValue();
                        beskedFacade.setChats(chats);
                    }
                }
            });
            DatabaseManager.getInstance().hentChatsMedNavn(aktivBruger.getNavn());
        }

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
        lblTraeningsprogram.setOnMouseClicked(event -> traeningsprogram());
        lblBooking.setOnMouseClicked(e -> booking());

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
            root = FXMLLoader.load(getClass().getResource("/fxml/ChatWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) logoImageView.getScene().getWindow();
        stage.setScene(scene);
    }

    public void skiftTilBrugerindstillinger() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Brugerindstillinger.fxml"));
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
            root = FXMLLoader.load(getClass().getResource("/fxml/Start.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = (Stage) menuAnchorPane.getScene().getWindow();
        stage.setScene(scene);
    }

    public void patientregister() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/PatientRegister.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) logoImageView.getScene().getWindow();
        stage.setScene(scene);
    }

    public void traeningsprogram() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/TildelProgram.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) logoImageView.getScene().getWindow();
        stage.setScene(scene);
    }

    public void booking() {
        BookingFacade bookingFacade = BookingFacade.getInstance();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Kalender.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) logoImageView.getScene().getWindow();
        stage.setScene(scene);
    }
}