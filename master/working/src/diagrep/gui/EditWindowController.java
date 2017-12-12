/**
 * FXML Controller class
 *
 * @author Nexus
 * Credits:
 * Alert Dialogs: http://code.makery.ch/blog/javafx-dialogs-official/
 * Mileage input restriction: http://stackoverflow.com/questions/17586367/java-regex-to-match-words-spaces
 * Formatting LocalDateTime/LocalDate: http://stackoverflow.com/questions/28177370/how-to-format-localdate-to-string
 * fillTime: http://stackoverflow.com/questions/11994790/parse-time-of-format-hhmmss
 * reading/writing an external file in Windows: http://stackoverflow.com/questions/5797208/java-how-do-i-write-a-file-to-a-specified-directory
 */
package diagrep.gui;

import common.Database;
import diagrep.Diagrep;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Callback;

public class EditWindowController implements Initializable {
    
	@FXML private Button confirmButton;
	@FXML private Button cancelButton;

	@FXML private Label custN;
	@FXML private Label custA;
	@FXML private Label custP;
	@FXML private Label custT;
	@FXML private Label custE;
	
	@FXML private Label vehT;
	@FXML private Label vehMa;
	@FXML private Label vehMo;
	@FXML private Label vehC;
	@FXML private TextField vehMi;
	
	@FXML private Label mechR;

	@FXML private ToggleGroup radioGroup;
	@FXML private RadioButton radioBookD;
	@FXML private RadioButton radioBookM;

	@FXML private DatePicker entryDate;
	@FXML private ChoiceBox entryReg;
	@FXML private ChoiceBox entryCustomer;
	@FXML private ChoiceBox entryMechanic;
	
	@FXML private ComboBox entryT;
	@FXML private ComboBox entryD;
	@FXML private ComboBox mechD;

	private DiagrepScreenController parentController;
	private Database db;
	private ObservableList<String> customerList;
	private ObservableList<String> vehicleList;
	private ObservableList<String> mechanicList;
	private Diagrep entry;
	private ArrayList<LocalDate> holidays;
	private int mileage;
	
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{

	}
	
	public void reinit()
	{
		db = Database.getInstance();
		entryDate.setEditable(false);
		
		radioGroup=new ToggleGroup();
		radioBookD.setToggleGroup(radioGroup);
		radioBookM.setToggleGroup(radioGroup);
		if (entry.getType().equals("Repair"))
			radioGroup.selectToggle(radioBookD);
		else
			radioGroup.selectToggle(radioBookM);
		
		String[] line = entry.getDate().split("[\\s]+");
		entryDate.setValue(LocalDate.parse(line[0], DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		restrictPastDate();

		holidays = new ArrayList<LocalDate>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("res/holidays.txt"));
			String lineR = "";
			while ((lineR = br.readLine()) != null)
			{
				LocalDate ld = parseLocalDate(lineR);
				holidays.add(ld);
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			Alert alertX = new Alert(AlertType.ERROR);
			alertX.setTitle("Holiday List Load Error");
			alertX.setContentText("Failed to load the list of holidays. Please manually check that your chosen appointment date does not fall on a holiday.");
			alertX.showAndWait();
		}
		
		try
		{
			customerList = FXCollections.observableArrayList();	//customer choicebox
			db.connect();
			ResultSet rsC = db.query("SELECT DISTINCT CustomerID, CustomerFirstName, CustomerLastName FROM Customer ORDER BY CustomerID;");
			while (rsC.next())
			{
				customerList.add(rsC.getString("CustomerID")+": "+rsC.getString("CustomerFirstName")+" "+rsC.getString("CustomerLastName"));
			}
			db.closeConnection();
			entryCustomer.setItems(customerList);
			entryCustomer.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
			{
				public void changed(ObservableValue ov, Number value, Number newvalue)
				{	
					String lineC = customerList.get((Integer) newvalue);
					String[] custData = lineC.split("[\\s,:]+");
					try
					{
						db.connect();
						ResultSet rsC = db.query("SELECT * FROM Customer WHERE CustomerID='"+custData[0]+"';");
						rsC.next();
						custN.setText(rsC.getString("CustomerFirstName")+" "+rsC.getString("CustomerLastName"));
						custA.setText(rsC.getString("CustomerAddress"));
						custP.setText(rsC.getString("CustomerPostcode"));
						custT.setText(rsC.getString("CustomerPhoneNo"));
						custE.setText(rsC.getString("CustomerEmail"));
						
						vehicleList = FXCollections.observableArrayList();		//vehicle choicebox
						ResultSet rsV = db.query("SELECT * FROM Vehicle WHERE CustomerID='"+custData[0]+"' ORDER BY VehicleRegNo;");
						int count = 0;
						while (rsV.next())
						{
							vehicleList.add(rsV.getString("VehicleRegNo"));
							count++;
						}
						if (count==0)
						{
							Alert alertE = new Alert(AlertType.ERROR);
							alertE.setTitle("Customer Selection Error");
							alertE.setContentText("The selected customer does not have a vehicle registered, please choose another customer.");
							entryCustomer.getSelectionModel().selectFirst();
							alertE.showAndWait();
						}
						entryReg.setItems(vehicleList);
						entryReg.getSelectionModel().selectFirst();
					}
					catch (SQLException se)
					{
						Alert alertE = new Alert(AlertType.ERROR);
						alertE.setTitle("Connection Error");
						alertE.setContentText("Failed to connect to the SQL database.");
						alertE.showAndWait();
					}
					db.closeConnection();
				}
			});
			entryCustomer.getSelectionModel().select(entry.getCustID()+": "+entry.getCustFirstName()+" "+entry.getCustLastName());

			db.connect();
			vehicleList = FXCollections.observableArrayList();		//vehicle choicebox
			int ID = entry.getCustID();
			ResultSet rsV = db.query("SELECT * FROM Vehicle WHERE CustomerID='"+ID+"' ORDER BY VehicleRegNo;");
			while (rsV.next())
			{
				vehicleList.add(rsV.getString("VehicleRegNo"));
			}
			db.closeConnection();
			entryReg.setItems(vehicleList);
			entryReg.getSelectionModel().select(entry.getVehReg());
			try
			{
				String lineV = entry.getVehReg();
				db.connect();
				ResultSet rsVT = db.query("SELECT * FROM Vehicle WHERE VehicleRegNo='"+lineV+"';");
				rsVT.next();
				vehT.setText(rsVT.getString("VehicleType"));
				vehMa.setText(rsVT.getString("VehicleMake"));
				vehMo.setText(rsVT.getString("VehicleModel"));
				vehC.setText(rsVT.getString("VehicleColour"));
				vehMi.setText(rsVT.getString("VehicleMileage"));
				mileage = Integer.parseInt(vehMi.getText());
				db.closeConnection();
			}
			catch (SQLException se)
			{
				se.printStackTrace();
				Alert alertE = new Alert(AlertType.ERROR);
				alertE.setTitle("Connection Error");
				alertE.setContentText("Failed to connect to the SQL database.");
				alertE.showAndWait();
			}
			
			entryReg.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
			{
				public void changed(ObservableValue ov, Number value, Number newvalue)
				{				
					try
					{
						String lineV = vehicleList.get((Integer) newvalue);	
						db.connect();
						ResultSet rsV = db.query("SELECT * FROM Vehicle WHERE VehicleRegNo='"+lineV+"';");
						rsV.next();
						vehT.setText(rsV.getString("VehicleType"));
						vehMa.setText(rsV.getString("VehicleMake"));
						vehMo.setText(rsV.getString("VehicleModel"));
						vehC.setText(rsV.getString("VehicleColour"));
						vehMi.setText(rsV.getString("VehicleMileage"));
						mileage = Integer.parseInt(vehMi.getText());
					}
					catch (SQLException se)
					{
						se.printStackTrace();
						Alert alertE = new Alert(AlertType.ERROR);
						alertE.setTitle("Connection Error");
						alertE.setContentText("Failed to connect to the SQL database.");
						alertE.showAndWait();
					}
					catch (ArrayIndexOutOfBoundsException ae)
					{
						
					}
					db.closeConnection();
				}
			});

			db.connect();
			mechanicList = FXCollections.observableArrayList();	//mechanic choicebox
			ResultSet rsM = db.query("SELECT DISTINCT * FROM Mechanic ORDER BY MechanicID;");
			while (rsM.next())
			{
				mechanicList.add(rsM.getString("MechanicID")+": "+rsM.getString("MechanicFirstName")+" "+rsM.getString("MechanicLastName"));
			}
			db.closeConnection();
			entryMechanic.setItems(mechanicList);
			entryMechanic.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
			{
				public void changed(ObservableValue ov, Number value, Number newvalue)
				{	
					String lineM = mechanicList.get((Integer) newvalue);
					String[] mechData = lineM.split("[\\s,:]+");
					try
					{
						db.connect();
						ResultSet rsM = db.query("SELECT * FROM Mechanic WHERE MechanicID='"+mechData[0]+"';");
						rsM.next();
						mechR.setText(rsM.getString("MechanicHourlyRate"));					
					}
					catch (SQLException se)
					{
						Alert alertE = new Alert(AlertType.ERROR);
						alertE.setTitle("Connection Error");
						alertE.setContentText("Failed to connect to the SQL database.");
						alertE.showAndWait();
					}
					db.closeConnection();
				}
			});
			entryMechanic.getSelectionModel().select(entry.getMechID()-1);
			
			LocalDate today = NOW_LOCALDATE();
			if (today.getDayOfWeek().name().equals("SUNDAY"))
			{
				entryDate.setValue(today.plusDays(1));
			}
			else
			{
				entryDate.setValue(NOW_LOCALDATE());
			}
			LocalDate localD = parseLocalDate(entryDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			entryDate.setOnAction(new EventHandler()
			{
 
				@Override
				public void handle(Event event)
				{
					LocalDate date = entryDate.getValue();
					String DOW = date.getDayOfWeek().name();
					boolean isHol = false;
					for (int i=0; i<holidays.size(); i++)
					{
						if (holidays.get(i).compareTo(date) == 0)
						{
							isHol=true;
							break;
						}
					}
					
					if (isHol == true)
					{
						Alert alertE = new Alert(AlertType.WARNING);
						alertE.setTitle("Date Selection Error");
						alertE.setContentText("The garage is closed on holidays. The next date is chosen for you.");
						alertE.showAndWait();
						entryDate.setValue(date.plusDays(1));
						LocalDate date2 = entryDate.getValue();
						String DOW2 = date2.getDayOfWeek().name();
						entryT.getItems().clear();
						if (DOW2.equals("SUNDAY"))
						{
							entryDate.setValue(date2.plusDays(1));
							fillTime(7);
						}
						else if (DOW2.equals("SATURDAY"))
						{
							fillTime(2);
						}
						else
						{
							fillTime(7);
						}
						entryT.getSelectionModel().selectFirst();
					}
					else if (DOW.equals("SUNDAY"))
					{
						Alert alertE = new Alert(AlertType.WARNING);
						alertE.setTitle("Date Selection Error");
						alertE.setContentText("The garage is closed on Sundays. The next date is chosen for you.");
						alertE.showAndWait();
						entryDate.setValue(date.plusDays(1));
						entryT.getItems().clear();
						LocalDate date2 = entryDate.getValue();
						String DOW2 = date2.getDayOfWeek().name();
						entryT.getItems().clear();
						boolean isHol2 = false;
						for (int i=0; i<holidays.size(); i++)
						{
							if (holidays.get(i).compareTo(date2) == 0)
							{
								isHol2=true;
								break;
							}
						}
						if (isHol2 == true)
						{
							entryDate.setValue(date2.plusDays(1));
							LocalDate date3 = entryDate.getValue();
							String DOW3 = date3.getDayOfWeek().name();
							entryT.getItems().clear();
							if (DOW3.equals("SUNDAY"))
							{
								entryDate.setValue(date3.plusDays(1));
								fillTime(7);
							}
							else if (DOW3.equals("SATURDAY"))
							{
								fillTime(2);
							}
							else
							{
								fillTime(7);
							}
						}
						else if (DOW2.equals("SATURDAY"))
						{
							fillTime(2);
						}
						else
						{
							fillTime(7);
						}
						entryT.getSelectionModel().selectFirst();
					}
					else if (DOW.equals("SATURDAY"))
					{
						entryT.getItems().clear();
						fillTime(2);
						entryT.getSelectionModel().selectFirst();
					}
					else
					{
						entryT.getItems().clear();
						fillTime(7);
						entryT.getSelectionModel().selectFirst();
					}
				}

			});
			
		}
		catch (SQLException se)
		{
			se.printStackTrace();
			Alert alertE = new Alert(AlertType.ERROR);
			alertE.setTitle("Connection Error");
			alertE.setContentText("Failed to connect to the SQL database.");
			alertE.showAndWait();
		}
		
		vehMi.textProperty().addListener(new ChangeListener<String>() {	//only allows numbers to be entered into the search field
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try
				{
					if (newValue.equals(""))
					{
						vehMi.setText(String.valueOf(mileage));
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Input Error");
						alert.setContentText("Mileage should not be blank.");
						alert.showAndWait();
					}
					else if (!newValue.matches("[\\d]+"))
					{
						vehMi.setText(newValue.replaceAll("[^\\d]", ""));
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Input Error");
						alert.setContentText("Only numbers are allowed for mileage.");
						alert.showAndWait();
					}
				}
				catch (NullPointerException npe)
				{
					
				}
			}
		});
		
		/* Dynamically fills the duration combobox to the maximum allowed hours possible for that booking
		 * E.g. If a booking is at 10am on a saturday, the possible duration range is 1-2 hours (since the garage closes at 12pm)
		*/
		entryT.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {	
			public void changed(ObservableValue ov, Number value, Number newvalue) {
				try {
					entryD.getItems().clear();

					String appointment_time = entryT.getSelectionModel().getSelectedItem().toString();
					LocalDate date = entryDate.getValue();
					String DOW = date.getDayOfWeek().name();

					//Last hour is the last hour of work
					int lastHour;
					if (DOW.equals("SATURDAY")) {
						lastHour = 12;
					} else if (!DOW.equals("SUNDAY")) {
						lastHour = 17;
					} else {
						lastHour = 0;
					}

					String[] time = appointment_time.split(":");
					int currentHour = Integer.parseInt(time[0]);

					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

					calendar.set(Calendar.MINUTE, 0);

					int difference = lastHour - currentHour;

					for (int i = 1; i <= difference; i++) {
						calendar.set(Calendar.HOUR_OF_DAY, i);
						entryD.getItems().add(timeFormat.format(calendar.getTime()));
					}
					entryD.getSelectionModel().selectFirst();

				} catch (NullPointerException ex)
				{
				}
			}
		});
		
		/* Dynamically fills the mechanic duration combobox to the maximum allowed hours possible for that booking
		 * E.g. If a booking has a duration of 6 hours, fills the combobox from 0 hours spent to 6 hours spent repairing
		*/
		entryD.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue ov, Number value, Number newvalue) {
				try {
					String mechInit = (String) entryD.getSelectionModel().getSelectedItem();
					int hours = Integer.parseInt(mechInit.substring(1, 2));
					fillDurationMech(hours);
					mechD.getSelectionModel().selectFirst();

				} catch (NullPointerException ex) {
				}
			}
		});
		
		
		
		mileage = entry.getVehMileage();
		vehMi.setText(String.valueOf(mileage));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDate date= LocalDate.parse(entry.getDate(), formatter);
		formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String dateStr = date.format(formatter);
		entryDate.setValue(date);
		entryT.getItems().clear();
		if (entryDate.getValue().getDayOfWeek().name().equals("SATURDAY"))
		{
			fillTime(2);
		}
		else
		{
			fillTime(7);
		}
		formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime time = LocalDateTime.parse(entry.getDate(), formatter);
		formatter = DateTimeFormatter.ofPattern("HH:mm");
		String timeStr = time.format(formatter);
		entryT.getSelectionModel().selectFirst();
		entryT.getSelectionModel().select(timeStr);
		entryD.getSelectionModel().select(entry.getDuration());
		
		String mechInit = entry.getDuration();
		int hours = Integer.parseInt(mechInit.substring(1,2));
		fillDurationMech(hours);
		mechD.getSelectionModel().select(entry.getMechDuration());
	}

	@FXML
	private void editEntry(ActionEvent event)
	{
		if (Integer.parseInt(vehMi.getText()) >= mileage) {
			try {
				String lineC = (String) entryCustomer.getSelectionModel().getSelectedItem();
				String[] custData = lineC.split("[\\s,:]+");
				String lineM = (String) entryMechanic.getSelectionModel().getSelectedItem();
				String[] mechData = lineM.split("[\\s,:]+");
				String bookingType = (radioGroup.getSelectedToggle() == radioBookD) ? "Repair" : "Maintenance";

				String temp = (String) mechD.getSelectionModel().getSelectedItem();
				String mechTime = temp.substring(0, 2);	//Gets the hours
				int hours = Integer.parseInt(mechTime);
				String minsTemp = temp.substring(3, 5);
				int mins = Integer.parseInt(minsTemp);
				if (mins != 0) //Hourly charge basis, so if the duration is e.g. 2 hours 10 mins, the charges would be for 3 hours
				{
					hours++;
				}
				String mechRate = mechR.getText();
				int rate = Integer.parseInt(mechRate);
				int cost = hours * rate;

				String date = entryDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (String) entryT.getSelectionModel().getSelectedItem();

				db.connect();
				String sql = "UPDATE Booking SET BookingType='" + bookingType + "', BookingDate='" + date + "', BookingDuration='" + (String) entryD.getSelectionModel().getSelectedItem() + "', VehicleRegNo='" + (String) entryReg.getSelectionModel().getSelectedItem() + "', CustomerID='" + custData[0] + "', MechanicID='" + mechData[0] + "', MechanicDuration='" + (String) mechD.getSelectionModel().getSelectedItem() + "' WHERE BookingID=" + entry.getID();
				db.update(sql);
				db.closeConnection();
				db.connect();
				String sqlAgain = "UPDATE Bill SET MechanicCost='" + cost + "' WHERE BookingID='" + entry.getID() + "';";
				db.update(sqlAgain);
				sql= "UPDATE Vehicle SET VehicleMileage='"+Integer.parseInt(vehMi.getText())+"' WHERE VehicleRegNo='"+(String) entryReg.getSelectionModel().getSelectedItem()+"';";
				db.update(sql);
				db.closeConnection();
				parentController.reset();
				Stage stage = (Stage) confirmButton.getScene().getWindow();
				stage.close();
			} catch (SQLException sqe) {
				sqe.printStackTrace();
				Alert alertE = new Alert(AlertType.ERROR);
				alertE.setTitle("Connection Error");
				alertE.setContentText("Error connecting to the SQL database.");
				Optional<ButtonType> action = alertE.showAndWait();
			} catch (NullPointerException npe) {
				Alert alertE = new Alert(AlertType.ERROR);
				alertE.setTitle("Connection Error");
				alertE.setContentText("SQL database not found.");
				Optional<ButtonType> action = alertE.showAndWait();
			}
		} else {
			Alert alertM = new Alert(AlertType.ERROR);
			alertM.setTitle("Add Booking Error");
			alertM.setContentText("Your new mileage cannot be lower than the current mileage.");
			Optional<ButtonType> action = alertM.showAndWait();
		}
	}

	@FXML
	private void cancel(ActionEvent event)
	{
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	public void setEntry(Diagrep entry)
	{
		this.entry = entry;
	}

	public void setParentController(DiagrepScreenController parentController)
	{
		this.parentController = parentController;
	}
	
	public LocalDate NOW_LOCALDATE()
	{
		return LocalDate.now();
	}

	public LocalDateTime NOW_LOCALDATETIME()
	{
		return LocalDateTime.now();
	}

	public LocalDate parseLocalDate(String str)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(str, formatter);
	}
	public LocalDateTime parseLocalDateTime(String str)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return LocalDateTime.parse(str, formatter);
	}
	
	private void fillTime(int workHours) {
		final int startTime = 9;

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);	//Set the minutes in the calendar to 00

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		for (int i = 0; i <= workHours; i++) {
			calendar.set(Calendar.HOUR_OF_DAY, (startTime + i));
			entryT.getItems().add(timeFormat.format(calendar.getTime()));
		}
	}
    
	public void fillDurationMech(int workHours) {
		mechD.getItems().clear();
		final int startTime = 0;

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);	//Set the minutes in the calendar to 00

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		for (int i = 0; i <= workHours; i++) {
			calendar.set(Calendar.HOUR_OF_DAY, (startTime + i));
			mechD.getItems().add(timeFormat.format(calendar.getTime()));
		}
	}
	
	/*
	 * Past dates are allowed to be picked while editing a booking.
	 * However, holidays and sundays are still disabled, because the garage is not opened then.
	 */
	private void restrictPastDate()
	{
		Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell()
		{
			@Override
			public void updateItem(LocalDate item, boolean empty)
			{
				super.updateItem(item, empty);
				
				for (int i=0; i<holidays.size(); i++)
					if (holidays.get(i).compareTo(item) == 0)
					{
						setStyle("-fx-background-color: #ffc0cb;");
						Platform.runLater(() -> setDisable(true));
					}
		
				if(item.getDayOfWeek().name().equals("SUNDAY"))
				{
					setStyle("-fx-background-color: #ffc0cb;");
					Platform.runLater(() -> setDisable(true));
				}
			}
		};
		entryDate.setDayCellFactory(dayCellFactory);
	}
}
