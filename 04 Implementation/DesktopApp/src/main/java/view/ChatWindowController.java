package view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;

public class ChatWindowController {
    private ChatWindowChatController selectedChat;

    @FXML
    private VBox chatWindowChatVBox;

    @FXML
    private Circle chatUserPhotoCircle;

    @FXML
    private ImageView logoImageView;

    public void initialize(){
        chatUserPhotoCircle.setFill(new ImagePattern(new Image("Christian.png")));
        Image logo = new Image("Logo.jpg");
        logoImageView.setImage(logo);
        createDummyChats("Testperson1", "Rygskade", "");
        createDummyChats("Testperson2", "Træningsvideo feedback", "chad.png");
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
        }
        else {
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

    public void showFakeMessages(String name) {
        if (name.equals("testperson1")) {

        }
    }

}
