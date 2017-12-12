/**
 *
 * @author Nexus
 */
package common;

import customers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application
{
        public static Stage stage;
        
	public static void main(String[] args)
	{
		launch(args);
	}
	

	@Override
	public void start(Stage stage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("loginScreen.fxml"));
		Scene scene = new Scene(root);
		this.stage = stage;
		stage.setScene(scene);
		stage.setTitle("Login Menu");
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();
	}
}
