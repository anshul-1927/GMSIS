/**
 * @author Lloyd
 */

package specialist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main  extends Application{
    
        public static Stage stage;
        
	public static void main(String[] args) {
            launch(args);
	}
        
        @Override
	public void start(Stage stage) throws Exception{
            Parent root = FXMLLoader.load(getClass().getResource("/specialist/gui/SpecialistScreen.fxml"));
            Scene scene = new Scene(root);
            this.stage = stage;
            stage.setScene(scene);
            stage.setTitle("Specialist Repairs");
            stage.show();
	}
        
}
