package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ChatWindowController {

    private ChatWindowChatController selectedChat;

    @FXML
    private VBox chatWindowChatVBox;

    @FXML
    private GridPane chatWindowMessageGridPane;

    @FXML
    private VBox chatWindowMessageVBox;

    @FXML
    private Circle chatUserPhotoCircle;

    @FXML
    private ImageView logoImageView;

    public void initialize() {
        chatUserPhotoCircle.setFill(new ImagePattern(new Image("Christian.png")));
        Image logo = new Image("Logo.jpg");
        logoImageView.setImage(logo);
        createDummyChats("Camilla Kron", "Rygskade", "");
        createDummyChats("Karsten Wiren", "Træningsvideo feedback", "chad.png");
    }

    public void createDummyChats(String name, String subject, String picturePath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatWindowChats.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException io) {
            io.printStackTrace();
        }
        ChatWindowChatController controller = loader.getController();
        controller.getChatWindowChatName().setText(name);
        controller.getChatWindowChatSubject().setText(subject);
        if (!picturePath.equals("")) {
            controller.getChatWindowChatPhoto().setFill(new ImagePattern(new Image(picturePath)));
        } else {
            controller.getChatWindowChatPhoto().setFill(new ImagePattern(new Image("intetBillede.png")));
        }
        chatWindowChatVBox.getChildren().add(root);
        controller.getChatWindowChatAnchorPane().setOnMouseClicked(event -> {
            if (selectedChat != null) {
                selectedChat.getChatWindowChatAnchorPane().setStyle(null);
            }
            controller.getChatWindowChatAnchorPane().setStyle("-fx-background-color: dodgerblue");
            selectedChat = controller;
            showFakeMessages(name);
        });
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
            Label message = new Label("Hej Camilla, som vi talte om forleden" +
                    "mht dine smerter i lænden, er det som sagt" +
                    "kritis for din helbreden, at du" + "gennemfører det udleverede program." + "Rigtig god vind og hæng i!\n" +
                    "\n Sendt d. 29-05-2020");
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
            Label message2 = new Label("Hej Karsten. Jeg har " +
                    "fundet et program til dig, som du skal bruge " +
                    "3 gange om ugen. Du kan se programmet, hvis du " +
                    "går til menuen og vælger træningsprogam. " +
                    "God bedring.");
            message2.setWrapText(true);
            chatContainer2.getChildren().add(message2);

            chatWindowMessageVBox.getChildren().add(chatContainer2);
        }
    }

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
