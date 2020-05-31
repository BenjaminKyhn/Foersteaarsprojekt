package view;

import domain.Besked;
import domain.Bruger;
import domain.Chat;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.BeskedFacade;
import model.BrugerFacade;
import model.ObserverbarListe;
import persistence.DatabaseManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Tommy og Patrick
 */
public class ChatWindowController {
    private BeskedFacade beskedFacade;
    private BrugerFacade brugerFacade;
    private ChatWindowChatController selectedChatController;
    private Chat selectedChat;
    private ObserverbarListe<Chat> chats;
    private Bruger aktivBruger;

    @FXML
    private AnchorPane chatWindowAnchorPane;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private VBox chatWindowChatVBox;

    @FXML
    private VBox chatWindowMessageVBox;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Circle chatUserPhotoCircle;

    @FXML
    private Button nyBeskedKnap, sendBeskedKnap, btnTilbage;

    @FXML
    private TextField tfSendBesked;

    @FXML
    private Label lblBrugernavn, lblEmail;

    /**
     * @author Benjamin
     */
    public void initialize() {
        beskedFacade = BeskedFacade.getInstance();
        brugerFacade = BrugerFacade.getInstance();
        aktivBruger = brugerFacade.getAktivBruger();
        chats = (ObserverbarListe<Chat>) beskedFacade.hentChats();

        /** Tilføj observer på nye chats */
        chats.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Ny Addition")) {
                    DatabaseManager.getInstance().opretChat((Chat) evt.getNewValue());
                }
            }
        });

        /** Indlæs brugerens oplysninger */
        if (aktivBruger.getFotoURL() != null)
            chatUserPhotoCircle.setFill(new ImagePattern(new Image(aktivBruger.getFotoURL()), 0, 0, 1, 1.3, true));
        else
            chatUserPhotoCircle.setFill(new ImagePattern(new Image("intetBillede.png")));
        if (aktivBruger.getNavn() != null)
            lblBrugernavn.setText(aktivBruger.getNavn());
        if (aktivBruger.getEmail() != null)
            lblEmail.setText(aktivBruger.getEmail());

        nyBeskedKnap.setOnMouseClicked(event -> nyBeskedPopup());

        /** Sæt UI-elementer til at skalere med vinduets størrelse */
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            menuBar.setMinWidth(chatWindowAnchorPane.getWidth() - btnTilbage.getPrefWidth());
            btnTilbage.setMinWidth(btnTilbage.getPrefWidth());
        };
        chatWindowAnchorPane.widthProperty().addListener(redraw);

        indlaesChats();
    }

    public void indlaesChats() {
        chatWindowChatVBox.getChildren().clear();

        for (int i = 0; i < chats.size(); i++) {
            Chat chat = chats.get(i);

            DatabaseManager databaseManager = DatabaseManager.getInstance();
            databaseManager.observerKaldFraFirestoreTilChat(chat);

            /** Tilføj observer og opdater chatten i databasen med den nye værdi */
            chat.tilfoejObserver(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("Ny Besked")) {
                        Platform.runLater(() -> visBeskeder((Chat) evt.getNewValue()));
                    }
                }
            });

            /** Sæt afsender */
            Bruger afsender = aktivBruger;

            /** Sæt modtager */
            Bruger modtager;
            if (chats.get(i).getAfsender().equals(aktivBruger.getNavn()))
                modtager = brugerFacade.hentBrugerMedNavn(chats.get(i).getModtager());
            else
                modtager = brugerFacade.hentBrugerMedNavn(chats.get(i).getAfsender());

            // TODO det rigtige navn vises ikke altid

            /** Hent controlleren */
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatWindowChats.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException io) {
                io.printStackTrace();
            }
            ChatWindowChatController controller = loader.getController();

            /** Sæt informationer i chatvinduet */
            controller.getChatWindowChatNavn().setText(modtager.getNavn());
            controller.getChatWindowChatEmne().setText(chat.getEmne());
            if (modtager.getFotoURL() == null || modtager.getFotoURL().equals(""))
                controller.getChatWindowChatFoto().setFill(new ImagePattern(new Image("intetBillede.png")));
            else
                controller.getChatWindowChatFoto().setFill(new ImagePattern(new Image(modtager.getFotoURL()), 0, 0, 1, 1.3, true));

            /** Sæt en onMouseClicked-metode til chatpanelet */
            controller.getChatWindowChatAnchorPane().setOnMouseClicked(event -> {
                if (selectedChatController != null) {
                    selectedChatController.getChatWindowChatAnchorPane().setStyle(null);
                }
                controller.getChatWindowChatAnchorPane().setStyle("-fx-background-color: dodgerblue");
                selectedChatController = controller;
                selectedChat = chat;
                visBeskeder(chat);

            });


            chatWindowChatVBox.getChildren().add(root);
        }

        // TODO: håndter chats med brugere, som ikke længere eksisterer (blev slettet)
    }

    public void visBeskeder(Chat chat) {
        if (!chat.equals(selectedChat)) {
            return;
        }

        aktivBruger = brugerFacade.getAktivBruger();
        ArrayList<Besked> beskeder = chat.getBeskeder();

        chatWindowMessageVBox.getChildren().clear();
        chatWindowMessageVBox.setSpacing(2);

        for (int i = 0; i < beskeder.size(); i++) {
            VBox chatContainer = new VBox();
            chatContainer.setStyle("-fx-border-color: #808080");

            String besked = beskeder.get(i).getBesked();
            String afsender = beskeder.get(i).getAfsender();
            String tidspunkt = beskeder.get(i).getTidspunkt().substring(0, 16);

            Label navnLabel = new Label(afsender + " " + tidspunkt);
            navnLabel.setStyle("-fx-font-weight: bold");

            Label beskedLabel = new Label(besked);
            beskedLabel.setWrapText(true);

            chatContainer.getChildren().addAll(navnLabel, beskedLabel);
            chatWindowMessageVBox.getChildren().add(chatContainer);

            chatContainer.setPadding(new Insets(16, 16, 16, 16));

            if (afsender.equals(aktivBruger.getNavn())) {
                if (i == 0)
                    VBox.setMargin(chatContainer, new Insets(16, 16, 8, 32));
                else
                    VBox.setMargin(chatContainer, new Insets(8, 16, 8, 32));
            } else {
                if (i == 0)
                    VBox.setMargin(chatContainer, new Insets(16, 32, 8, 16));
                else
                    VBox.setMargin(chatContainer, new Insets(8, 32, 8, 16));
            }
        }

        /** Scroll ned til seneste besked */
        chatScrollPane.setVvalue(1.0);

        /** Tilføj en listener på VBoxen, som scroller viewet, hvis der tilføjes flere children */
        chatWindowMessageVBox.getChildren().addListener((ListChangeListener<Node>) c -> {
            Platform.runLater(() -> chatScrollPane.setVvalue(chatWindowMessageVBox.getHeight()));
        });

        /** Giv sendknappen et on click event */
        sendBeskedKnap.setOnMouseClicked(event -> {
            beskedFacade.sendBesked(tfSendBesked.getText(), chat);
            DatabaseManager.getInstance().opdaterChat(chat);
            visBeskeder(chat);
        });
    }

    /**
     * @author Tommy og Patrick
     */
    public void logUd() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Start.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);

        Stage stage = (Stage) chatWindowMessageVBox.getScene().getWindow();
        stage.setScene(scene);

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
        Scene scene = new Scene(root);

        Stage stage = (Stage) chatWindowMessageVBox.getScene().getWindow();
        stage.setScene(scene);
    }

    /**
     * @author Benjamin
     */
    public void nyBeskedPopup() {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../NyBesked.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert root != null;

        Scene popupScene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Ny Besked");
        stage.setScene(popupScene);

        /** setOnHidden kaldes, når popup'en lukkes igen */
        stage.setOnHidden(event -> indlaesChats());

        stage.show();
    }

    //TODO få menuen til at scale med vinduets størrelse
}