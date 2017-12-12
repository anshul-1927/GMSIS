package common;

import static common.Main.stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginScreenController implements Initializable {

	@FXML
	private TextField userField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label errorMessage;
	@FXML
	private Button loginButton;

	public Parent root;

	public static Stage stage;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		loginButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					loginButton.fire();
				}
			}
		});
	
		passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					loginButton.fire();
				}
			}
		});

		userField.textProperty().addListener(new ChangeListener<String>() {	//only allows numbers to be entered into the search field
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if (newValue.equals(""))
					{
						
					}
					else if (!newValue.matches("[\\d]+")) {
						userField.setText(newValue.replaceAll("[^\\d]", ""));
						errorMessage.setText("Username consists of digits only.");
					}
				} catch (NullPointerException npe) {

				}
			}
		});
	}

	@FXML
	public void login(ActionEvent event) {
		Database db = Database.getInstance();

		try {

			db.connect();
			ResultSet resultset = db.query("SELECT * FROM User WHERE UserID=\'" + userField.getText() + "\' AND UserPassword=\'" + passwordField.getText() + "\'");

			if (!resultset.isBeforeFirst()) //isBeforeFirst returns false if no rows in resultset
			{
				errorMessage.setText("Invalid user/password");
			} else {
				Stage stag = (Stage) loginButton.getScene().getWindow();
				stag.close();

				boolean isAdmin = (resultset.getInt("UserIsAdmin") == 1) ? true : false;

				SystemUser user = new SystemUser(resultset.getString("UserFirstName"), resultset.getString("UserLastName"), resultset.getString("UserID"), resultset.getString("UserPassword"), isAdmin);
				db.closeConnection();
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainScreen.fxml"));
				root = (Parent) fxmlLoader.load();

				mainScreenController controller = fxmlLoader.<mainScreenController>getController();
				controller.setUser(user);
				controller.reinit();

				Scene scene = new Scene(root);
				stage = Main.stage;
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.setTitle("Main Menu");
				stage.setResizable(false);
				stage.sizeToScene();
				stage.show();

			}

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
