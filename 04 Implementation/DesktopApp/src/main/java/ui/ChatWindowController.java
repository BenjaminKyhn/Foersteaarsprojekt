package ui;

import entities.Besked;
import entities.Bruger;
import entities.Chat;
import entities.exceptions.ForMangeTegnException;
import entities.exceptions.TomBeskedException;
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
import database.DatabaseManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**@author Tommy */
public class ChatWindowController {
    private BeskedFacade beskedFacade;
    private BrugerFacade brugerFacade;
    private ChatWindowChatController selectedChatController;
    private Chat selectedChat;
    private ArrayList<Chat> chats;
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
    private Button nyChatKnap, sendBeskedKnap, btnTilbage;

    @FXML
    private TextField tfSendBesked;

    @FXML
    private Label lblBrugernavn, lblEmail;

    /**
     * @author Benjamin
     */
    public void initialize() {
        //TODO Lav søgefunktion i beskeder
        //TODO fix bug, hvor de samme chats visses uanset, hvem der logger ind

        beskedFacade = BeskedFacade.getInstance();
        brugerFacade = BrugerFacade.getInstance();
        aktivBruger = brugerFacade.getAktivBruger();
        chats = beskedFacade.hentChats();

        /* Tilføj observer på nye chats */
        beskedFacade.tilfoejObserver(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("opretChat")) {
                    DatabaseManager.getInstance().opretChat((Chat) evt.getNewValue());
                }
            }
        });

        /* Indlæs brugerens oplysninger */
        if (aktivBruger.getFotoURL() != null)
            chatUserPhotoCircle.setFill(new ImagePattern(new Image(aktivBruger.getFotoURL()), 0, 0, 1, 1, true));
        else
            chatUserPhotoCircle.setFill(new ImagePattern(new Image("intetBillede.png")));
        if (aktivBruger.getNavn() != null)
            lblBrugernavn.setText(aktivBruger.getNavn());
        if (aktivBruger.getEmail() != null)
            lblEmail.setText(aktivBruger.getEmail());

        nyChatKnap.setOnMouseClicked(event -> nyChatPopup());

        indlaesChats();
    }

    public void indlaesChats() {
        chatWindowChatVBox.getChildren().clear();

        for (int i = 0; i < chats.size(); i++) {
            Chat chat = chats.get(i);

            DatabaseManager databaseManager = DatabaseManager.getInstance();
            databaseManager.observerKaldFraFirestoreTilChat(chat);

            /* Tilføj observer og opdater chatten i databasen med den nye værdi */
            chat.tilfoejObserver(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("Ny Besked")) {
                        Platform.runLater(() -> visBeskeder((Chat) evt.getNewValue()));
                    }
                }
            });

            /* Sæt afsender */
            Bruger afsender = aktivBruger;

            /* Sæt modtager */
            Bruger modtager;
            if (chats.get(i).getAfsender().equals(aktivBruger.getNavn()))
                modtager = brugerFacade.hentBrugerMedNavn(chats.get(i).getModtager());
            else
                modtager = brugerFacade.hentBrugerMedNavn(chats.get(i).getAfsender());

            /* Hent controlleren */
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChatWindowChats.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException io) {
                io.printStackTrace();
            }
            ChatWindowChatController controller = loader.getController();

            /* Sæt informationer i chatvinduet */
            controller.getChatWindowChatNavn().setText(modtager.getNavn());
            controller.getChatWindowChatEmne().setText(chat.getEmne());
            if (modtager.getFotoURL() == null || modtager.getFotoURL().equals(""))
                controller.getChatWindowChatFoto().setFill(new ImagePattern(new Image("intetBillede.png")));
            else
                controller.getChatWindowChatFoto().setFill(new ImagePattern(new Image(modtager.getFotoURL()), 0, 0, 1, 1, true));

            /* Sæt en onMouseClicked-metode til chatpanelet */
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

            Date date = new Date(beskeder.get(i).getTidspunkt());
            String tidspunkt = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);

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

        /* Scroll ned til seneste besked */
        chatScrollPane.setVvalue(1.0);

        /* Tilføj en listener på VBoxen, som scroller viewet, hvis der tilføjes flere children */
        chatWindowMessageVBox.getChildren().addListener((ListChangeListener<Node>) c -> {
            Platform.runLater(() -> chatScrollPane.setVvalue(chatWindowMessageVBox.getHeight()));
        });

        /* Giv sendknappen et on click event */
        sendBeskedKnap.setOnMouseClicked(event -> {
            try {
                beskedFacade.sendBesked(tfSendBesked.getText(), chat);
                DatabaseManager.getInstance().opdaterChat(chat);
                visBeskeder(chat);
            } catch (TomBeskedException tbe){
                popupWindow("Beskeden er tom");
            } catch (ForMangeTegnException fmt){
                popupWindow("Du har skrevet mere end 160 tegn");
            }
        });
    }

    /**
     * @author Tommy og Patrick
     */
    public void logUd() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/Start.fxml"));
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
            root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
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
    public void nyChatPopup() {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NyChat.fxml"));
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

        /* setOnHidden kaldes, når popup'en lukkes igen */
        stage.setOnHidden(event -> indlaesChats());

        stage.show();
    }

    public void popupWindow(String infoText) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SystemBeskedPopup.fxml"));
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