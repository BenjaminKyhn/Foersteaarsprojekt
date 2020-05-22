package view;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.BrugerFacade;
import model.exceptions.*;

import java.io.IOException;

/** @author Benjamin */
public class OpretBrugerController {
    private BrugerFacade brugerFacade;

    @FXML
    private AnchorPane opretBrugerAnchorPane;

    @FXML
    private ImageView logoImageView;

    @FXML
    private GridPane opretBrugerGridPane;

    /** Lav TextFields, Buttons, Labels og ImageView */
    TextField tfBrugernavn = new TextField();
    TextField tfEmail = new TextField();
    TextField tfPassword = new TextField();
    TextField tfGentagPassword = new TextField();
    Label lblBrugernavn = new Label("Navn:");
    Label lblEmail = new Label("Email:");
    Label lblPassword = new Label("Password:");
    Label lblGentagPassword = new Label("Gentag \npassword:");
    HBox buttonHolder = new HBox();
    Button btnTilbage = new Button("Tilbage");
    Button btnOpretBruger = new Button("Opret Bruger");
    Image image = new Image("Logo2x.png");

    public void initialize() throws IOException {
        brugerFacade = BrugerFacade.getInstance();

        buttonHolder.setSpacing(17);
        buttonHolder.getChildren().addAll(btnTilbage, btnOpretBruger);

        /** Sæt indstillingerne på startGridPane */
        opretBrugerGridPane.setHgap(5);
        opretBrugerGridPane.setVgap(10);
        opretBrugerGridPane.add(lblBrugernavn, 0, 0);
        opretBrugerGridPane.add(tfBrugernavn, 1, 0);
        opretBrugerGridPane.add(lblEmail, 0, 1);
        opretBrugerGridPane.add(tfEmail, 1, 1);
        opretBrugerGridPane.add(lblPassword, 0, 2);
        opretBrugerGridPane.add(tfPassword, 1, 2);
        opretBrugerGridPane.add(lblGentagPassword,0,3);
        opretBrugerGridPane.add(tfGentagPassword, 1, 3);
        opretBrugerGridPane.add(buttonHolder, 1, 4);
        opretBrugerGridPane.setAlignment(Pos.CENTER);

        /** Sæt startImageViews indstillinger */
        logoImageView.setImage(image);
        ChangeListener<Number> redraw = (observable, oldValue, newValue) -> {
            logoImageView.setX(opretBrugerAnchorPane.getWidth() / 2 - logoImageView.getFitWidth() / 2);
        };
        opretBrugerAnchorPane.widthProperty().addListener(redraw);

        /** Sæt events på knapperne */
        btnTilbage.setOnMouseClicked(event -> tilbage());
        btnOpretBruger.setOnMouseClicked(event -> {
            try {
                brugerFacade.tjekNavn(tfBrugernavn.getText());
                brugerFacade.tjekEmail(tfEmail.getText());
                brugerFacade.tjekPassword(tfPassword.getText());
                if (!tjekOmPasswordMatcher(tfPassword.getText(), tfGentagPassword.getText())){
                    popupWindow("Fej: Password matcher ikke");
                    return;
                }
                brugerFacade.opretBruger(tfBrugernavn.getText(), tfEmail.getText(), tfPassword.getText());
                popupWindow("Brugeren er oprettet");
            } catch (TomNavnException tne){
                popupWindow("Fejl: Navnefeltet kan ikke være tomt");
            } catch (TomEmailException tee){
                popupWindow("Fejl: Emailfeltet kan ikke være tomt");
            } catch (EksisterendeBrugerException ebe){
                popupWindow("Fejl: Brugeren eksisterer allerede");
            } catch (TomPasswordException tpe){
                popupWindow("Fejl: Passwordfeltet kan ikke være tomt");
            } catch (PasswordLaengdeException ple){
                popupWindow("Fejl: Password skal være mellem 6 og 20 tegn");
            } catch (BrugerLoggedIndException blie) {
                popupWindow("Fejl: Du er allerede logged ind");
            }
        });
    }

    public void tilbage() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Start.fxml"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assert root != null;
        Scene secondScene = new Scene(root);

        Stage stage = (Stage) opretBrugerAnchorPane.getScene().getWindow();
        stage.setScene(secondScene);
    }

    public void popupWindow(String infoText) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../PopupWindow.fxml"));
        try {
            root = fxmlLoader.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assert root != null;

        Scene popupScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(popupScene);
        stage.show();

        PopupWindowController popupWindowController = fxmlLoader.getController();
        popupWindowController.getTxtLabel().setText(infoText);
    }

    public boolean tjekOmPasswordMatcher(String password1, String password2){
        boolean matcher = false;
        if (password1.equals(password2))
            matcher = true;
        return matcher;
    }
}
