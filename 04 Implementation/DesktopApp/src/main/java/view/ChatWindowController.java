package view;

import domain.Besked;
import domain.Bruger;
import domain.Chat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
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
    private Button nyBeskedKnap;

    public void initialize() throws IOException {
        beskedFacade = BeskedFacade.getInstance();
        brugerFacade = BrugerFacade.getInstance();

        chatUserPhotoCircle.setFill(new ImagePattern(new Image("Christian.png")));
        indlaesChats();
//        indlaesChats("Camilla Kron", "Rygskade", "");
//        indlaesChats("Karsten Wiren", "Træningsvideo feedback", "Karsten.png");
    }

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
            if (chats.get(i).getAfsender().equals(aktivBruger.getNavn())){
                controller.getChatWindowChatName().setText(chat.getModtager());
            }
            else
                controller.getChatWindowChatName().setText(chat.getAfsender());

            controller.getChatWindowChatSubject().setText(chat.getEmne());

            controller.getChatWindowChatPhoto().setFill(new ImagePattern(new Image("intetBillede.png")));

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

    public void visBeskeder(Chat chat){
        aktivBruger = brugerFacade.getAktivBruger();
        ArrayList<Besked> beskeder = chat.getBeskeder();

        chatWindowMessageVBox.getChildren().clear();
        chatWindowMessageVBox.setSpacing(2);

        for (int i = 0; i < beskeder.size(); i++) {
            VBox chatContainer = new VBox();
            chatContainer.setStyle("-fx-border-color: gray");

            String besked = beskeder.get(i).getBesked();

            Label navnLabel = new Label("Kurt");
            navnLabel.setStyle("-fx-font-weight: bold");

            Label beskedLabel = new Label(besked);
            beskedLabel.setWrapText(true);

            chatContainer.getChildren().addAll(navnLabel, beskedLabel);
            chatWindowMessageVBox.getChildren().add(chatContainer);

            if (i % 2 == 0){
                chatContainer.setPadding(new Insets(16,32, 16, 16));
            }
            else{
                chatContainer.setPadding(new Insets(16,16, 16, 32));
                navnLabel.setPrefWidth(546);
                navnLabel.setTextAlignment(TextAlignment.RIGHT);
                navnLabel.setAlignment(Pos.CENTER_RIGHT);
            }
        }
    }

    /**@author Benjamin*/
    public void showFakeMessages(String name) {
        /** Non-dynamic method */
        if (name.equals("Camilla Kron")) {
            chatWindowMessageVBox.getChildren().clear();
            chatWindowMessageVBox.setSpacing(2);
            VBox chatContainer = new VBox();
            chatContainer.setStyle("-fx-border-color: gray");
            chatContainer.setPadding(new Insets(16,32, 16, 16));

            Label label = new Label(name);
            label.setStyle("-fx-font-weight: bold");
            chatContainer.getChildren().add(label);
            Label message = new Label("Hej Christian. Jeg har her i den sidste " +
                    "måned udviklet nogle forfærdelige rygsmerter, " +
                    "der efterhånden er begyndt at hindre mig i at " +
                    "udføre mit arbejde. Kan du give mig nogle " +
                    "øvelser, der kan afhjælpe mit problem? Det " +
                    "må simplethen stoppe nu!");
            message.setWrapText(true);
            chatContainer.getChildren().add(message);
            chatWindowMessageVBox.getChildren().add(chatContainer);

            VBox chatContainer2 = new VBox();
            chatContainer2.setStyle("-fx-border-color: gray");
            chatContainer2.setPadding(new Insets(16, 16, 16, 32));
            Label label2 = new Label("Christian");
            label2.setPrefWidth(546);
            label2.setTextAlignment(TextAlignment.RIGHT);
            label2.setAlignment(Pos.CENTER_RIGHT);
            label2.setStyle("-fx-font-weight: bold");
            chatContainer2.getChildren().add(label2);
            Label message2 = new Label("Hej Camilla. Jeg har " +
                    "fundet et program til dig, som du skal bruge " +
                    "3 gange om ugen. Du kan se programmet, hvis du " +
                    "går til menuen og vælger træningsprogam. " +
                    "God bedring.");
            message2.setWrapText(true);
            chatContainer2.getChildren().add(message2);

            chatWindowMessageVBox.getChildren().add(chatContainer2);

//            chatWindowMessageGridPane.setGridLinesVisible(true);
        }

        /** Dynamic method */
        if (name.equals("Karsten Wiren")) {
//            ArrayList<Label> messages = new ArrayList<>();
//            messages.add(new Label("Hej Camilla, som vi talte om forleden\n " +
//                    "mht dine smerter i lænden, er det som sagt\n" +
//                    "kritis for din helbreden, at du \n" +
//                    "gennemfører det udleverede program.\n" +
//                    "Rigtig god vind og hæng i!\n" +
//                    "\n" +
//                    "Venlig hilsen, Christian Fys.\n"
//                    +
//                    "\n Sendt d. 29-05-2020"));
            chatWindowMessageVBox.getChildren().clear();
            chatWindowMessageVBox.setSpacing(2);
            VBox chatContainer = new VBox();
            chatContainer.setStyle("-fx-border-color: gray");
            chatContainer.setPadding(new Insets(16,32, 16, 16));

            Label label = new Label(name);
            label.setStyle("-fx-font-weight: bold");
            chatContainer.getChildren().add(label);
            Label message = new Label("Hej Christian, jeg har gennemført det program, du gav mig" + " " +
                    "til nakke og skulder." + " " +
                    "Jeg er løbet ind i udfordringer, da jeg ikke føler min skulder er smedig nok" + " " +
                    "til mange af øvelserne." + " " +
                    "\n" + " " +
                    "\nHilsen Karsten" +
                    "\nSendt d. 29-05-2020");
            message.setWrapText(true);
            chatContainer.getChildren().add(message);
            chatWindowMessageVBox.getChildren().add(chatContainer);

            VBox chatContainer2 = new VBox();
            chatContainer2.setStyle("-fx-border-color: gray");
            chatContainer2.setPadding(new Insets(16, 16, 16, 32));
            Label label2 = new Label("Christian");
            label2.setPrefWidth(546);
            label2.setTextAlignment(TextAlignment.RIGHT);
            label2.setAlignment(Pos.CENTER_RIGHT);
            label2.setStyle("-fx-font-weight: bold");
            chatContainer2.getChildren().add(label2);
            Label message2 = new Label("Hej Karsten, godt at høre du har fuldført programmet! " +
                    "mht. den manglende smedighed, har jeg sendt nogle alternative " +
                    "skulderøvelser til din privat mail. Prøv dem af og hvis, du undgår " +
                    "ubehagen, så forsætter du med dem." +
                    "\n" + " " +
                    "\nVenlig hilsen, Christian Iuul" +
                    "\nSendt d. 30-05-2020");
            message2.setWrapText(true);
            chatContainer2.getChildren().add(message2);

            chatWindowMessageVBox.getChildren().add(chatContainer2);
        }
    }

    /** @author Tommy og Patrick */
    public void logOut() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Start.fxml"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assert root != null;
        Scene secondScene = new Scene(root);

        Stage stage = (Stage) chatWindowMessageVBox.getScene().getWindow();
        stage.setScene(secondScene);
    }

    public void toMainMenu() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Menu.fxml"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assert root != null;
        Scene secondScene = new Scene(root);

        Stage stage = (Stage) chatWindowMessageVBox.getScene().getWindow();
        stage.setScene(secondScene);
    }

}
