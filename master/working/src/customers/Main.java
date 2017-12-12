package customers;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("gui/customersScreen.fxml"));
        Scene scene = new Scene(root);
        Main.stage = stage;
        stage.setScene(scene);
        stage.setTitle("Customers");
        stage.show();
    }
}
