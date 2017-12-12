/**
 * FXML Controller class
 *
 * @author Nexus
 * Credits:
 * bookingPrompt, displayFutureBookings / displayPastBookings: http://code.makery.ch/blog/javafx-dialogs-official/
 * Formatting LocalDateTime/LocalDate:  http://stackoverflow.com/questions/28177370/how-to-format-localdate-to-string
 * searchField input restriction: http://stackoverflow.com/questions/17586367/java-regex-to-match-words-spaces
 * autocomplete: https://www.youtube.com/watch?v=0TV2lBP6hz0
 * Modality/Ensuring that any new window has exclusive pointer focus only: Efthymios Chatziathanasiadis (Teammate)
 */
package diagrep.gui;

import common.Database;
import common.LoginScreenController;
import diagrep.Diagrep;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Modality;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class DiagrepScreenController implements Initializable
{   
	@FXML
	private TextField searchField;
	@FXML
	private ChoiceBox searchOptions;
	@FXML
	private Button searchButton;
	@FXML
	private RadioButton radioAll;
	@FXML
	private RadioButton radioPast;
	@FXML
	private RadioButton radioFuture;
	@FXML
	private RadioButton radioHourly;
	@FXML
	private RadioButton radioDaily;
	@FXML
	private RadioButton radioMonthly;
	//@FXML
	//private TabPane viewTabPane;
	@FXML
	private TableView<diagrep.Diagrep> diagrepTable;
	@FXML
	private TableColumn<diagrep.Diagrep, Integer> colID;
	@FXML
	private TableColumn<diagrep.Diagrep, String> colType;
	@FXML
	private TableColumn<diagrep.Diagrep, String> colDate;
	@FXML
	private TableColumn<diagrep.Diagrep, String> colDuration;
	//@FXML
	//private TableColumn<diagrep.Diagrep, String> colVeh;
	@FXML
	private TableColumn<diagrep.Diagrep, String> colVehReg;
	@FXML
	private TableColumn<diagrep.Diagrep, String> colVehManufacturer;
	@FXML
	private TableColumn<diagrep.Diagrep, String> colVehMileage;
	//@FXML
	//private TableColumn<diagrep.Diagrep, String> colCust;
	@FXML
	private TableColumn<diagrep.Diagrep, String> colCustID;
	@FXML
	private TableColumn<diagrep.Diagrep, String> colCustfirstName;
	@FXML
	private TableColumn<diagrep.Diagrep, String> colCustlastName;
	//@FXML
	//private TableColumn<diagrep.Diagrep, String> colMech;
	@FXML
	private TableColumn<diagrep.Diagrep, String> colMechID;
	@FXML
	private TableColumn<diagrep.Diagrep, String> colMechDuration;

	private ToggleGroup radioView;
	private ObservableList<diagrep.Diagrep> dataList;
	private Database db;
	public static Stage windowStage;
	AutoCompletionBinding<String> acb;

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		db = Database.getInstance();
		searchOptions.setItems(FXCollections.observableArrayList("Vehicle Registration No.", new Separator(), "Vehicle Manufacturer", new Separator(), "Vehicle Template", new Separator(), "Customer Name"));
		searchOptions.getSelectionModel().selectFirst();	//set the options to search from in dropdown list
		
		db.connect();
		try
		{
			ArrayList<String> list = new ArrayList<>();
			ResultSet rs = db.query("SELECT DISTINCT VehicleRegNo FROM Vehicle;");
			while (rs.next())
			{
				list.add(rs.getString("VehicleRegNo"));
			}
			acb = TextFields.bindAutoCompletion(searchField, list);
		}
		catch(SQLException se)
		{
		}
		db.closeConnection();
		
		searchOptions.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue ov, Number value, Number newvalue) {
				db.connect();
				try
				{
					if ((Integer) newvalue == 0)
					{
						ArrayList<String> list = new ArrayList<>();
						ResultSet rs = db.query("SELECT DISTINCT VehicleRegNo FROM Vehicle;");
						while (rs.next())
						{
							list.add(rs.getString("VehicleRegNo"));
						}
						acb.dispose();
						acb =TextFields.bindAutoCompletion(searchField, list);
					}
					else if ((Integer) newvalue == 2)
					{
						ArrayList<String> list = new ArrayList<>();
						ResultSet rs = db.query("SELECT DISTINCT VehicleMake FROM Vehicle;");
						while (rs.next())
						{
							list.add(rs.getString("VehicleMake"));
						}
						acb.dispose();
						acb = TextFields.bindAutoCompletion(searchField, list);
					}
					else if ((Integer) newvalue == 4)
					{
						ArrayList<String> list = new ArrayList<>();
						ResultSet rs = db.query("SELECT DISTINCT * FROM Vehicle;");
						while (rs.next())
						{
							list.add(rs.getString("VehicleMake")+ " "+rs.getString("VehicleModel"));
						}
						acb.dispose();
						acb = TextFields.bindAutoCompletion(searchField, list);
					}
					else if ((Integer) newvalue == 6)
					{
						ArrayList<String> list = new ArrayList<>();
						ResultSet rs = db.query("SELECT CustomerFirstName, CustomerLastName FROM Customer;");
						while (rs.next())
						{
							list.add(rs.getString("CustomerFirstName") +" "+rs.getString("CustomerLastName"));
						}
						acb.dispose();
						acb = TextFields.bindAutoCompletion(searchField, list);
					}
				}
				catch (SQLException se)
				{
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Auto-Complete Error");
					alert.setContentText("Failed to connect to the database, auto-complete functionality is disabled.");
					alert.showAndWait();
				}
				db.closeConnection();
			}
		});

		searchField.textProperty().addListener(new ChangeListener<String>() {	//only allows letters, numbers, or spaces to be entered into the search field
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try
				{
					if (newValue.equals(""))
					{
						reset();
					}
					else if (!newValue.matches("[\\w\\s]+"))
					{
						searchField.setText(newValue.replaceAll("[^\\w\\s]", ""));
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Input Error");
						alert.setContentText("Only letters, numbers, or spaces are allowed.");
						alert.showAndWait();
					}
				}
				catch (NullPointerException npe)
				{
					
				}
			}
		});
		
		searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					searchButton.fire();
				}
			}
		});
		
		searchField.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (!ke.getCode().equals(KeyCode.ENTER)) {
					searchButton.fire();
				}
			}
		});
		

		radioView = new ToggleGroup();
		radioAll.setToggleGroup(radioView);
		radioPast.setToggleGroup(radioView);
		radioFuture.setToggleGroup(radioView);
		radioHourly.setToggleGroup(radioView);
		radioDaily.setToggleGroup(radioView);
		radioMonthly.setToggleGroup(radioView);
		radioAll.setSelected(true);
		radioView.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
		{
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle)
			{
				if (radioView.getSelectedToggle() == radioAll)
				{
					displayTableData(null);
				}
				else if (radioView.getSelectedToggle() == radioHourly)
				{
					String hour = hourView();
					if (hour == null)
					{
						radioView.selectToggle(radioAll);
						displayTableData(null);
						return;
					}
					else
					{
						String start = nowTimeString(hour);
						LocalDateTime endLDT = parseLocalDateTime(start).plusHours(1);
						String end =endLDT.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
						String sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
							+ "FROM Booking B, Vehicle V, Customer C "
							+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID AND B.BookingDate>='"+start+"' AND B.BookingDate<='"+end+"';";
						db.connect();
						try
						{
							dataList = FXCollections.observableArrayList();
							ResultSet rs = db.query(sql);	
							boolean hasData = false;
							while (rs.next())	//starts from before the first row of results
							{
								dataList.add(new Diagrep(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), rs.getString("BookingDuration"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getInt("VehicleMileage"), rs.getInt("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"),  rs.getInt("MechanicID"), rs.getString("MechanicDuration")));
								hasData = true;
							}
							diagrepTable.setItems(dataList);
							if (!hasData) {
								Alert alertE = new Alert(AlertType.INFORMATION);
								alertE.setTitle("No bookings found.");
								alertE.setContentText("There are no bookings found for your chosen search term/criteria.");
								alertE.showAndWait();
							}
						}
						catch (SQLException se)
						{
							se.printStackTrace();
							radioView.selectToggle(radioAll);
							displayTableData(null);
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Connection Error");
							alert.setContentText("Error connecting to the SQL database.");
							alert.showAndWait();
							return;
						}
						db.closeConnection();
					}
				}
				else if (radioView.getSelectedToggle() == radioDaily)
				{
					String start = nowTimeString("00:00");
					LocalDateTime endLDT = parseLocalDateTime(start).plusDays(1);
					String end =endLDT.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
					String sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
						+ "FROM Booking B, Vehicle V, Customer C "
						+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID AND B.BookingDate>='"+start+"' AND B.BookingDate<='"+end+"';";
					db.connect();
					try
					{
						dataList = FXCollections.observableArrayList();
						ResultSet rs = db.query(sql);	
						boolean hasData = false;
						while (rs.next())	//starts from before the first row of results
						{
							dataList.add(new Diagrep(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), rs.getString("BookingDuration"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getInt("VehicleMileage"), rs.getInt("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"),  rs.getInt("MechanicID"), rs.getString("MechanicDuration")));
							hasData = true;
						}
						diagrepTable.setItems(dataList);
						if (!hasData) {
							Alert alertE = new Alert(AlertType.INFORMATION);
							alertE.setTitle("No bookings found.");
							alertE.setContentText("There are no bookings found for your chosen search term/criteria.");
							alertE.showAndWait();
						}
								
					}
					catch (SQLException se)
					{
						se.printStackTrace();
						radioView.selectToggle(radioAll);
						displayTableData(null);
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Connection Error");
						alert.setContentText("Error connecting to the SQL database.");
						alert.showAndWait();
						return;
					}
					db.closeConnection();
				}
				else if (radioView.getSelectedToggle() == radioMonthly)
				{
					String sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
						+ "FROM Booking B, Vehicle V, Customer C "
						+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID AND B.BookingDate>='"+nowMonthStart()+"' AND B.BookingDate<='"+nowMonthEnd()+"';";
					db.connect();
					try
					{
						dataList = FXCollections.observableArrayList();
						ResultSet rs = db.query(sql);	
						boolean hasData = false;
						while (rs.next())	//starts from before the first row of results
						{
							dataList.add(new Diagrep(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), rs.getString("BookingDuration"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getInt("VehicleMileage"), rs.getInt("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"),  rs.getInt("MechanicID"), rs.getString("MechanicDuration")));
							hasData = true;
						}
						diagrepTable.setItems(dataList);	
						if (!hasData) {
							Alert alertE = new Alert(AlertType.INFORMATION);
							alertE.setTitle("No bookings found.");
							alertE.setContentText("There are no bookings found for your chosen search term/criteria.");
							alertE.showAndWait();
						}
					}
					catch (SQLException se)
					{
						se.printStackTrace();
						radioView.selectToggle(radioAll);
						displayTableData(null);
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Connection Error");
						alert.setContentText("Error connecting to the SQL database.");
						alert.showAndWait();
						return;
					}
					db.closeConnection();
				}
			}
		});
		
		colID.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, Integer>("ID"));
		colType.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("type"));
		colDate.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("date"));
		colDuration.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("duration"));

		colVehReg.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("vehReg"));
		colVehManufacturer.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("vehManufacturer"));
		colVehMileage.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("vehMileage"));
		
		colCustID.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("custID"));
		colCustfirstName.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("custFirstName"));
		colCustlastName.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("custLastName"));

		colMechID.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("mechID"));
		colMechDuration.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("mechDuration"));
		displayTableData(null);
	}
	
	@FXML
	public void viewEntry() throws IOException
	{
		Diagrep entry = diagrepTable.getSelectionModel().getSelectedItem();
		if (entry == null)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("View Error");
			alert.setContentText("Please select a booking to view.");
			alert.showAndWait();
			return;
		}
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewWindow.fxml"));     
		Parent root = (Parent) fxmlLoader.load();
		ViewWindowController controllerE = fxmlLoader.<ViewWindowController>getController();
		controllerE.setParentController(this);
		controllerE.setEntry(entry);
		controllerE.reinit();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.setTitle("View Booking Details");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(LoginScreenController.stage);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();
	}

	@FXML
	public void addEntry() throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addWindow.fxml"));     
		Parent root = (Parent) fxmlLoader.load(); 
		AddWindowController controllerA = fxmlLoader.<AddWindowController>getController();
		controllerA.setParentController(this);
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.setTitle("Add New Diagnosis/Repair Booking");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(LoginScreenController.stage);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();
	}
		
	@FXML
	public void editEntry() throws IOException
	{
		Diagrep entry = diagrepTable.getSelectionModel().getSelectedItem();
		if (entry == null)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Edit Error");
			alert.setContentText("Please select a booking to edit.");
			alert.showAndWait();
			return;
		}
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editWindow.fxml"));     
		Parent root = (Parent) fxmlLoader.load();
		EditWindowController controllerE = fxmlLoader.<EditWindowController>getController();
		controllerE.setParentController(this);
		controllerE.setEntry(entry);
		controllerE.reinit();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.setTitle("Edit Diagnosis/Repair Booking");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(LoginScreenController.stage);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();
	}
	
	@FXML
	public boolean deleteEntry()
	{
		Diagrep selectedBooking = diagrepTable.getSelectionModel().getSelectedItem();
		if (selectedBooking == null)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Delete Error");
			alert.setContentText("Please select a booking to delete.");
			alert.showAndWait();
			return false;
		}
		Alert alertC = new Alert(AlertType.CONFIRMATION);
		alertC.setTitle("Confirm Delete");
		alertC.setContentText("Are you sure you want to delete the selected entry?\n\nBooking ID:\t"+selectedBooking.getID()+"\nDate:\t\t"+selectedBooking.getDate()+"\nCustomer:\t"+selectedBooking.getCustFirstName()+" "+selectedBooking.getCustLastName()+"\nVehicle:\t\t"+selectedBooking.getVehReg());
		Optional <ButtonType> action = alertC.showAndWait();
		if (action.get() != ButtonType.OK)	//user cancels delete operation
		{
			return false;
		}
		else
		{
			try
			{
				db.connect();
				int bookingID = selectedBooking.getID();
				int start = dataList.size();		//no. of entries before delete
				db.update("DELETE FROM Booking WHERE BookingID=\'" + bookingID + "\';");	
				//db.update("DELETE FROM Bill WHERE BillID='"+bookingID+"';");	//Manual on delete cascade from Booking --> Bill
			
				dataList.clear();
				displayTableData(null);
				radioView.selectToggle(radioAll);
				return true;
			}
			catch (SQLException se)
			{
				se.printStackTrace();
				Alert alertS = new Alert(AlertType.ERROR);
				alertS.setTitle("Connection Error");
				alertS.setContentText("Error connecting to the SQL database.");
				alertS.showAndWait();
				return false;
			}
			catch (NullPointerException npe)
			{
				npe.printStackTrace();
				Alert alertN = new Alert(AlertType.ERROR);
				alertN.setTitle("Connection Error");
				alertN.setContentText("SQL database not found.");
				alertN.showAndWait();
				return false;
			}
		}
	}
	
	@FXML
	public void search()
	{
		String search = searchField.getText();
		if (search == null || search.equals(""))
		{
			return;
		}
		String searchBy = (String) searchOptions.getSelectionModel().getSelectedItem();
		String sql = "";
		switch (searchBy)	//possible to optimise this where searchOptions variable is used, but the search options displayed on GUI would not look as good
		{
			case "Vehicle Registration No.":
				sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
					+ "FROM Booking B, Vehicle V, Customer C "
					+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID AND B.VehicleRegNo LIKE \'%"+search+"%\';";
				break;
			case "Vehicle Manufacturer":
				sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
					+ "FROM Booking B, Vehicle V, Customer C "
					+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID AND V.VehicleMake LIKE \'%"+search+"%\';";
				break;
			case "Customer Name":
				String[] names = search.split("\\s+");
				if (names.length == 2)		//searching with first and last name
				{
					sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
						+ "FROM Booking B, Vehicle V, Customer C "
						+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID AND C.CustomerFirstName LIKE \'%"+names[0]+"%\' AND C.CustomerLastName LIKE \'%"+names[1]+"%\';";
				}
				else
				{
					sql = "SELECT BookingID, BookingType, BookingDate, BookingDuration, VehicleRegNo, VehicleMake, VehicleMileage, CustomerID, CustomerFirstName, CustomerLastName, MechanicID, MechanicDuration "
						+ "FROM (SELECT * FROM Booking B, Vehicle V, Customer C WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID) "
						+ "WHERE CustomerFirstName LIKE '%"+names[0]+"%' OR CustomerLastName LIKE '%"+names[0]+"%'";
				}
				break;
			case "Vehicle Template":
				String[] split = search.split("\\s+");
				if (split.length == 2)
				{
					sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
					+ "FROM Booking B, Vehicle V, Customer C "
					+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID AND V.VehicleMake LIKE \'%"+split[0]+"%\' AND V.VehicleModel LIKE \'%"+split[1]+"%\';";
				}
				else
				{
					sql = null;
				}
				break;
		}
		dataList.clear();
		radioView.selectToggle(radioAll);
		displayTableData(sql);
	}
	
	@FXML
	public void displayRegistry()
	{
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("mechanicsRegistryWindow.fxml"));
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.setTitle("Mechanics Registry");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(LoginScreenController.stage);
			stage.setResizable(false);
			stage.sizeToScene();
			stage.show();
		}
		catch (IOException ioe)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Load Error");
			alert.setContentText("The mechanics registry cannot be displayed.");
			alert.showAndWait();
		}
	}
	
	@FXML
	public void displayHolidays()
	{
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("holidayWindow.fxml"));
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.setTitle("Mechanics Registry");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(LoginScreenController.stage);
			stage.setResizable(false);
			stage.sizeToScene();
			stage.show();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Load Error");
			alert.setContentText("The holiday management system cannot be displayed.");
			alert.showAndWait();
		}
	}
	
	@FXML
	public void reset()
	{
		searchField.setText(null);
		dataList.clear();
		displayTableData(null);
	}
	
	public void displayTableData(String sql)	//optional String to display data based on query
	{
		dataList = FXCollections.observableArrayList();
		try
		{
			if (sql == null)	//display all data normally instead
			{
				sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
					+ "FROM Booking B, Vehicle V, Customer C "
					+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID;";
			}
			db.connect();
			ResultSet rs = db.query(sql);
			boolean hasData = false;
			while (rs.next())	//starts from before the first row of results
			{
				dataList.add(new Diagrep(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), rs.getString("BookingDuration"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getInt("VehicleMileage"), rs.getInt("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"),  rs.getInt("MechanicID"), rs.getString("MechanicDuration")));
				hasData = true;
			}
			diagrepTable.setItems(dataList);
			radioView.selectToggle(radioAll);
			db.closeConnection();
			if (!hasData)
			{
				Alert alertE = new Alert(AlertType.INFORMATION);
				alertE.setTitle("No bookings found.");
				alertE.setContentText("There are no bookings found for your chosen search term/criteria.");
				alertE.showAndWait();
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
			Alert alertE = new Alert(AlertType.ERROR);
			alertE.setTitle("Connection Error");
			alertE.setContentText("Error connecting to the SQL database.");
			alertE.showAndWait();
		}
	}
	
	@FXML
	public void displayPastBookings()
	{
		dataList = FXCollections.observableArrayList();
		try
		{
			int choice = bookingPrompt("past");
			if (choice == -1)
			{
				return;
			}
			else if (choice == 0)
			{
				String sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
						+ "FROM Booking B, Vehicle V, Customer C "
						+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID;";
				db.connect();
				ResultSet rs = db.query(sql);
				while (rs.next())	//starts from before the first row of results
				{
					if(parseLocalDateTime(rs.getString("BookingDate")).compareTo(NOW_LOCALDATETIME()) < 0)
					{
						dataList.add(new Diagrep(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), rs.getString("BookingDuration"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getInt("VehicleMileage"), rs.getInt("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"),  rs.getInt("MechanicID"), rs.getString("MechanicDuration")));
					}	
				}
				diagrepTable.setItems(dataList);
				db.closeConnection();
			}
			else
			{
				ArrayList<String> vehicleList = new ArrayList<String>();	
				db.connect();
				ResultSet rsV = db.query("SELECT DISTINCT VehicleRegNo FROM Vehicle ORDER BY VehicleRegNo;");
				while (rsV.next())
				{
					vehicleList.add(rsV.getString("VehicleRegNo"));
				}
				Object[] vehicles = vehicleList.toArray();
				List<String> choices = new ArrayList<String>();
				for (int i=0; i<vehicles.length; i++)
					choices.add(vehicles[i].toString());

				ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
				dialog.setTitle("Vehicle Choice");
				dialog.setContentText("Choose a vehicle to display its bookings");

				Optional<String> result = dialog.showAndWait();
				String input;
				if (result.isPresent()) 
					input = result.get();
				else
					input = null;

				if (input == null)
				{
					return;
				}
				String sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
						+ "FROM Booking B, Vehicle V, Customer C "
						+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID AND B.VehicleRegNo='"+input+"';";
				ResultSet rs = db.query(sql);
				while (rs.next())	//starts from before the first row of results
				{
					if(parseLocalDateTime(rs.getString("BookingDate")).compareTo(NOW_LOCALDATETIME()) < 0)
					{
						dataList.add(new Diagrep(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), rs.getString("BookingDuration"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getInt("VehicleMileage"), rs.getInt("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"),  rs.getInt("MechanicID"), rs.getString("MechanicDuration")));
					}
					
				}
				diagrepTable.setItems(dataList);
				db.closeConnection();
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
			Alert alertE = new Alert(AlertType.ERROR);
			alertE.setTitle("Connection Error");
			alertE.setContentText("Error connecting to the SQL database.");
			alertE.showAndWait();
		}
	}
	
	@FXML
	public void displayFutureBookings()
	{
		dataList = FXCollections.observableArrayList();
		try
		{
			int choice = bookingPrompt("future");
			if (choice == -1)
			{
				return;
			}
			else if (choice == 0)
			{
				String sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
						+ "FROM Booking B, Vehicle V, Customer C "
						+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID;";
				db.connect();
				ResultSet rs = db.query(sql);
				while (rs.next())	//starts from before the first row of results
				{
					if(parseLocalDateTime(rs.getString("BookingDate")).compareTo(NOW_LOCALDATETIME()) >= 0)
					{
						dataList.add(new Diagrep(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), rs.getString("BookingDuration"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getInt("VehicleMileage"), rs.getInt("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"),  rs.getInt("MechanicID"), rs.getString("MechanicDuration")));
					}	
				}
				diagrepTable.setItems(dataList);
				db.closeConnection();
			}
			else
			{
				ArrayList<String> vehicleList = new ArrayList<String>();	
				db.connect();
				ResultSet rsV = db.query("SELECT DISTINCT VehicleRegNo FROM Vehicle ORDER BY VehicleRegNo;");
				while (rsV.next())
				{
					vehicleList.add(rsV.getString("VehicleRegNo"));
				}
				Object[] vehicles = vehicleList.toArray();
				List<String> choices = new ArrayList<String>();
				for (int i=0; i<vehicles.length; i++)
					choices.add(vehicles[i].toString());

				ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
				dialog.setTitle("Vehicle Choice");
				dialog.setContentText("Choose a vehicle to display its bookings");

				Optional<String> result = dialog.showAndWait();
				String input;
				if (result.isPresent()) 
					input = result.get();
				else
					input = null;

				if (input == null)
				{
					return;
				}
				String sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
						+ "FROM Booking B, Vehicle V, Customer C "
						+ "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID AND B.VehicleRegNo='"+input+"';";
				ResultSet rs = db.query(sql);
				while (rs.next())	//starts from before the first row of results
				{
					if(parseLocalDateTime(rs.getString("BookingDate")).compareTo(NOW_LOCALDATETIME()) >= 0)
					{
						dataList.add(new Diagrep(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), rs.getString("BookingDuration"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getInt("VehicleMileage"), rs.getInt("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"),  rs.getInt("MechanicID"), rs.getString("MechanicDuration")));
					}
					
				}
				diagrepTable.setItems(dataList);
				db.closeConnection();
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
			Alert alertE = new Alert(AlertType.ERROR);
			alertE.setTitle("Connection Error");
			alertE.setContentText("Error connecting to the SQL database.");
			alertE.showAndWait();
		}
	}
	
	
	public int bookingPrompt(String str)		//returns 0 if the user wants to view all vehicle bookings, and 1 for a specific vehicle
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Past/Future Bookings");
		//alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
		alert.setContentText("Would you like to view the "+str+" bookings of all vehicles, or a specific one?");

		ButtonType buttonAll = new ButtonType("All");
		ButtonType buttonSpecific = new ButtonType("Specific");
		ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonAll, buttonSpecific, buttonCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonAll) {
			return 0;
		} else if (result.get() == buttonSpecific) {
			return 1;
		} else {	//... user chose CANCEL or closed the dialog
			return -1;
		}
	}
	
	public String hourView()
	{
		List<String> times = new ArrayList<String>();
		if (NOW_LOCALDATETIME().getDayOfWeek().name().equals("SATURDAY"))
		{
			times.add("09:00");
			for (int i=10; i<=11; i++)
			{
				String str = Integer.toString(i).concat(":00");
				times.add(str);
			}
		}
		else
		{
			times.add("09:00");
			for (int i=10; i<=16; i++)
			{
				String str = Integer.toString(i).concat(":00");
				times.add(str);
			}
		}

		ChoiceDialog<String> dialog = new ChoiceDialog<>(times.get(0), times);
		dialog.setTitle("Hourly View");
		dialog.setContentText("Please select a time to view all bookings in that hour.");
		Optional<String> result = dialog.showAndWait();
		String input;
		if (result.isPresent()) 
			input = result.get();
		else
			input = null;
		return input;
	}
	
	public LocalDateTime NOW_LOCALDATETIME()
	{
		return LocalDateTime.now();
	}
	
	public LocalDateTime parseLocalDateTime(String str)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return LocalDateTime.parse(str, formatter);
	}
	
	public String nowTimeString(String str)
	{
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = localDate.format(formatter).concat(" "+str);
		return formattedString;
	}
	
	public String nowMonthStart()
	{
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime start = today.withDayOfMonth(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = start.format(formatter).concat(" 00:00");
		return formattedString;
	}
	
	public String nowMonthEnd()
	{
		LocalDate today = LocalDate.now();
		LocalDate end = today.withDayOfMonth(today.lengthOfMonth());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = end.format(formatter).concat(" 23:59");
		return formattedString;
	}
}
