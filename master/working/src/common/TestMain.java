/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import customers.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class TestMain extends Application
{
        public static Stage stage;
        
	public static void main(String[] args)
	{
		launch(args);
	}
	

	@Override
	public void start(Stage stage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("AdminScreen.fxml"));
		Scene scene = new Scene(root);
		this.stage = stage;
		stage.setScene(scene);
		stage.setTitle("Admin Screen");
		stage.show();
	}
}


