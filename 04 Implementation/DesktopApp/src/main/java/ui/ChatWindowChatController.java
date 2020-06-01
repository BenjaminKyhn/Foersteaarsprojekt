package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

/** @author Tommy og Patrick */
public class ChatWindowChatController {
    @FXML
    private AnchorPane chatWindowChatAnchorPane;

    @FXML
    private Circle chatWindowChatFoto;

    @FXML
    private Label chatWindowChatNavn, chatWindowChatEmne;

    public AnchorPane getChatWindowChatAnchorPane() {
        return chatWindowChatAnchorPane;
    }

    public Circle getChatWindowChatFoto() {
        return chatWindowChatFoto;
    }

    public Label getChatWindowChatNavn() {
        return chatWindowChatNavn;
    }

    public Label getChatWindowChatEmne() {
        return chatWindowChatEmne;
    }
}