package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
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
    private GridPane chatWindowMessageGridPane;

    @FXML
    private Circle chatUserPhotoCircle;

    @FXML
    private ImageView logoImageView;

    public void initialize() {
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
        if (name.equals("Testperson1")) {
            chatWindowMessageGridPane.getChildren().clear();
            chatWindowMessageGridPane.setVgap(10);
            chatWindowMessageGridPane.add(new Label("Hej Christian. Jeg har her i den sidste " +
                    "\nmåned udviklet nogle forfærdelige rygsmerter, " +
                    "\nder efterhånden er begyndt at hindre mig i at " +
                    "\nudføre mit arbejde. Kan du give mig nogle" +
                    "\nøvelser, der kan afhjælpe mit problem? Det " +
                    "\nmå simplethen stoppe nu!"), 0, 0);

            chatWindowMessageGridPane.add(new Label("Hej Testperson 1. Jeg har " +
                    "\nfundet et program til dig, som du skal bruge " +
                    "\n3 gange om ugen. Du kan se programmet, hvis du" +
                    "\ngår til menuen og vælger træningsprogam. " +
                    "\nGod bedring."), 1, 1);

//            chatWindowMessageGridPane.setGridLinesVisible(true);
        }

        /** Dynamic method */
        if (name.equals("Testperson2")) {
            ArrayList<Label> messages = new ArrayList<Label>();
            messages.add(new Label("Hejsa"));
            messages.add(new Label("Hvordan går det?"));
            messages.add(new Label("Min pik gør ondt"));
            messages.add(new Label("Av :^)"));
            messages.add(new Label("Av D:"));
            messages.add(new Label("Av :^)"));
            messages.add(new Label("Av D:"));
            messages.add(new Label("Av :^)"));
            messages.add(new Label("Av D:"));

            chatWindowMessageGridPane.getChildren().clear();
            chatWindowMessageGridPane.getRowConstraints().clear();
            chatWindowMessageGridPane.setPrefHeight(messages.size() * 120);

            for (int i = 0; i < messages.size(); i++) {
                RowConstraints row = new RowConstraints();
                row.setPercentHeight(100.0 / messages.size());
                row.setValignment(VPos.TOP);
                chatWindowMessageGridPane.getRowConstraints().add(row);

                if (i % 2 == 0){
                    chatWindowMessageGridPane.add(messages.get(i), 0, i);
                }
                else
                    chatWindowMessageGridPane.add(messages.get(i), 1, i);
            }

//            chatWindowMessageGridPane.setGridLinesVisible(true);
        }
    }

}
