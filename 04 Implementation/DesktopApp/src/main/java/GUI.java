import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUI extends Application{
    private TextField tfUsername = new TextField();
    private TextField tfPassword = new TextField();
    private Label lblUsername = new Label("Bruger:");
    private Label lblPassword = new Label("Kodeord:");
    private Button btLogin = new Button("Login");

    @Override
    public void start(Stage stage) throws Exception {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.add(lblUsername, 0, 0);
        gridPane.add(tfUsername, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(tfPassword, 1, 1);
        gridPane.add(btLogin, 1, 2);

        gridPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(gridPane, 800, 600);
        stage.setTitle("Fys Desktop App");
        stage.setScene(scene);

//        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}