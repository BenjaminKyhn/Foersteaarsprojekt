package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class ChatWindowChatController {
    @FXML
    private AnchorPane chatWindowChatAnchorPane;

    @FXML
    private Circle chatWindowChatPhoto;

    @FXML
    private Label chatWindowChatName, chatWindowChatSubject;

    public AnchorPane getChatWindowChatAnchorPane() {
        return chatWindowChatAnchorPane;
    }

    public Circle getChatWindowChatPhoto() {
        return chatWindowChatPhoto;
    }

    public Label getChatWindowChatName() {
        return chatWindowChatName;
    }

    public Label getChatWindowChatSubject() {
        return chatWindowChatSubject;
    }
}
