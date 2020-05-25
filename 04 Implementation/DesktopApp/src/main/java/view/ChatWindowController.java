package view;

import domain.Besked;
import domain.Bruger;
import domain.Chat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.BeskedFacade;
import model.BrugerFacade;

import java.io.IOException;
import java.util.ArrayList;

/** @author Tommy og Patrick */
public class ChatWindowController {
    private BeskedFacade beskedFacade;
    private BrugerFacade brugerFacade;
    private ChatWindowChatController selectedChat;
    private ArrayList<Chat> chats;
    private Bruger aktivBruger;

    @FXML
    private VBox chatWindowChatVBox;

    @FXML
    private VBox chatWindowMessageVBox;

    @FXML
    private Circle chatUserPhotoCircle;

    @FXML
    private Button nyBeskedKnap, sendBeskedKnap;

    @FXML
    private TextField tfSendBesked;

    @FXML
    private Label lblBrugernavn, lblEmail;

    /** @author Benjamin */
    public void initialize() throws IOException {
        beskedFacade = BeskedFacade.getInstance();
        brugerFacade = BrugerFacade.getInstance();
        aktivBruger = brugerFacade.getAktivBruger();

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

        indlaesChats();
    }

    public void indlaesChats() {
        chats = beskedFacade.hentChatsMedNavn(aktivBruger.getNavn());
        chatWindowChatVBox.getChildren().clear();

        for (int i = 0; i < chats.size(); i++) {
            Chat chat = chats.get(i);

            /** Sæt afsender */
            Bruger afsender = aktivBruger;

            /** Sæt modtager */
            Bruger modtager;
            if (chats.get(i).getAfsender().equals(aktivBruger.getNavn()))
                modtager = brugerFacade.hentBrugerMedNavn(chats.get(i).getModtager());
            else
                modtager = brugerFacade.hentBrugerMedNavn(chats.get(i).getAfsender());

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
            controller.getChatWindowChatNavn().setText(chat.getModtager());
            controller.getChatWindowChatEmne().setText(chat.getEmne());
            if (modtager.getFotoURL() == null || modtager.getFotoURL().equals(""))
                controller.getChatWindowChatFoto().setFill(new ImagePattern(new Image("intetBillede.png")));
            else
                controller.getChatWindowChatFoto().setFill(new ImagePattern(new Image(modtager.getFotoURL()), 0, 0, 1, 1.3, true));

            /** Sæt en onMouseClicked-metode til chatpanelet */
            controller.getChatWindowChatAnchorPane().setOnMouseClicked(event -> {
                if (selectedChat != null) {
                    selectedChat.getChatWindowChatAnchorPane().setStyle(null);
                }
                controller.getChatWindowChatAnchorPane().setStyle("-fx-background-color: dodgerblue");
                selectedChat = controller;
                visBeskeder(chat);
            });


            chatWindowChatVBox.getChildren().add(root);
        }

        // TODO: håndter chats med brugere, som ikke længere eksisterer (blev slettet)
    }

    public void visBeskeder(Chat chat) {
        aktivBruger = brugerFacade.getAktivBruger();
        ArrayList<Besked> beskeder = chat.getBeskeder();

        chatWindowMessageVBox.getChildren().clear();
        chatWindowMessageVBox.setSpacing(2);

        for (int i = 0; i < beskeder.size(); i++) {
            VBox chatContainer = new VBox();
            chatContainer.setStyle("-fx-border-color: gray");

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

        /** Giv sendknappen et on click event */
        sendBeskedKnap.setOnMouseClicked(event -> {
            beskedFacade.sendBesked(tfSendBesked.getText(), chat);
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
        Scene secondScene = new Scene(root);

        Stage stage = (Stage) chatWindowMessageVBox.getScene().getWindow();
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

        Stage stage = (Stage) chatWindowMessageVBox.getScene().getWindow();
        stage.setScene(secondScene);
    }

    /** @author Benjamin */
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
}