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
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.BeskedFacade;
import model.BrugerFacade;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Tommy og Patrick
 */
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
    private Button nyBeskedKnap;

    public void initialize() throws IOException {
        beskedFacade = BeskedFacade.getInstance();
        brugerFacade = BrugerFacade.getInstance();

        chatUserPhotoCircle.setFill(new ImagePattern(new Image("Christian.png")));

        nyBeskedKnap.setOnMouseClicked(event -> nyBeskedPopup());

        indlaesChats();
    }

    /**
     * @author Benjamin
     */
    public void indlaesChats() {
        aktivBruger = brugerFacade.getAktivBruger();
        chats = beskedFacade.hentChatsMedNavn(aktivBruger.getNavn());

        for (int i = 0; i < chats.size(); i++) {
            Chat chat = chats.get(i);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatWindowChats.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException io) {
                io.printStackTrace();
            }

            /** Hent controlleren */
            ChatWindowChatController controller = loader.getController();

            /** Vis det rigtige navn i chatvinduet */
            if (chats.get(i).getAfsender().equals(aktivBruger.getNavn())) {
                controller.getChatWindowChatNavn().setText(chat.getModtager());
            } else
                controller.getChatWindowChatNavn().setText(chat.getAfsender());

            controller.getChatWindowChatEmne().setText(chat.getEmne());

            controller.getChatWindowChatFoto().setFill(new ImagePattern(new Image("intetBillede.png")));

            chatWindowChatVBox.getChildren().add(root);

            controller.getChatWindowChatAnchorPane().setOnMouseClicked(event -> {
                if (selectedChat != null) {
                    selectedChat.getChatWindowChatAnchorPane().setStyle(null);
                }
                controller.getChatWindowChatAnchorPane().setStyle("-fx-background-color: dodgerblue");
                selectedChat = controller;
                visBeskeder(chat);
            });
        }

//        if (!picturePath.equals("")) {
//            controller.getChatWindowChatPhoto().setFill(new ImagePattern(new Image(picturePath)));
//        } else {
//            controller.getChatWindowChatPhoto().setFill(new ImagePattern(new Image("intetBillede.png")));
//        }

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

            Label navnLabel = new Label(afsender);
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

    public void nyBeskedPopup(){
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../NyBesked.fxml"));
        try {
            root = fxmlLoader.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assert root != null;

        Scene popupScene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Ny Besked");
        stage.setScene(popupScene);
        stage.show();
    }
}
