package view;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Tommy
 */

public class SecondSceneController {


    @FXML
    private ImageView photoImageView, logoImageView;

    public void initialize(){
        Image logo = new Image("Logo.jpg");
        logoImageView.setImage(logo);
        Image photo = new Image("chad.png");
        photoImageView.setImage(photo);
    }

}
