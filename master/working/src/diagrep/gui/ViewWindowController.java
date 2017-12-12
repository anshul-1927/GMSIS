/**
 * FXML Controller class
 *
 * @author Nexus
 */
package diagrep.gui;

import common.Database;
import diagrep.Diagrep;
import diagrep.Part;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ViewWindowController implements Initializable {
	
	private DiagrepScreenController parentController;
	private Database db;
	private Diagrep entry;
	private int mileage;
	private ObservableList<diagrep.Part> partsData;
	
	@FXML private Label custID;
	@FXML private Label custN;
	@FXML private Label custA;
	@FXML private Label custP;
	@FXML private Label custT;
	@FXML private Label custE;
	
	@FXML private Label vehReg;
	@FXML private Label vehT;
	@FXML private Label vehMa;
	@FXML private Label vehMo;
	@FXML private Label vehC;
	@FXML private TextField vehMi;
	
	@FXML private Label bookID;
	@FXML private Label bookTy;
	@FXML private Label bookDu;
	@FXML private Label bookDa;
	@FXML private Label bookTi;
	
	
	@FXML private Label billM;
	@FXML private Label billP;
	@FXML private Label billS;
	@FXML private Label billTotal;
	@FXML private Label billStatus;
	
	@FXML private Label mechN;
	@FXML private Label mechR;
	@FXML private Label mechD;
	
	@FXML private TableView<diagrep.Part> partsTable;
	@FXML private TableColumn<diagrep.Part, Integer> colID;
	@FXML private TableColumn<diagrep.Part, String> colName;
	@FXML private TableColumn<diagrep.Part, String> colDes;
	@FXML private TableColumn<diagrep.Part, String> colDate;
	@FXML private TableColumn<diagrep.Part, String> colCost;

	@FXML private Button quitButton;
	@FXML private Button updateButton;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}
	
	public void reinit()
	{
		db = Database.getInstance();
		db.connect();
		try
		{
			ResultSet rsC = db.query("SELECT * FROM Customer WHERE CustomerID='" + entry.getCustID() + "';");
			rsC.next();
			custID.setText(rsC.getString("CustomerID"));
			custN.setText(rsC.getString("CustomerFirstName") + " " + rsC.getString("CustomerLastName"));
			custA.setText(rsC.getString("CustomerAddress"));
			custP.setText(rsC.getString("CustomerPostcode"));
			custT.setText(rsC.getString("CustomerPhoneNo"));
			custE.setText(rsC.getString("CustomerEmail"));
			
			ResultSet rsV = db.query("SELECT * FROM Vehicle WHERE VehicleRegNo='" + entry.getVehReg() + "';");
			rsV.next();
			vehReg.setText(rsV.getString("VehicleRegNo"));
			vehT.setText(rsV.getString("VehicleType"));
			vehMa.setText(rsV.getString("VehicleMake"));
			vehMo.setText(rsV.getString("VehicleModel"));
			vehC.setText(rsV.getString("VehicleColour"));
			vehMi.setText(rsV.getString("VehicleMileage"));
			mileage = Integer.parseInt(vehMi.getText());
		
			vehMi.textProperty().addListener(new ChangeListener<String>() {	//only allows numbers to be entered into the mileage field
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					try {
						if (newValue.equals("")) {
							vehMi.setText(String.valueOf(mileage));
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Input Error");
							alert.setContentText("Mileage should not be blank.");
							alert.showAndWait();
						} else if (!newValue.matches("[\\d]+")) {
							vehMi.setText(newValue.replaceAll("[^\\d]", ""));
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Input Error");
							alert.setContentText("Only numbers are allowed for mileage.");
							alert.showAndWait();
						}
					} catch (NullPointerException npe) {

					}
				}
			});
			
			ResultSet rsB = db.query("SELECT * FROM Booking WHERE BookingID='" + entry.getID() + "';");
			rsB.next();
			bookID.setText(rsB.getString("BookingID"));
			bookTy.setText(rsB.getString("BookingType"));
			bookDu.setText(rsB.getString("BookingDuration"));
			String datetime = rsB.getString("BookingDate");
			String[] split = datetime.split("[\\s]+");
			bookDa.setText(split[0]);
			bookTi.setText(split[1]);
			mechD.setText(rsB.getString("MechanicDuration"));
			
			ResultSet rsM = db.query("SELECT * FROM Mechanic WHERE MechanicID='" + entry.getMechID() + "';");
			rsM.next();
			mechN.setText(rsM.getString("MechanicFirstName") + " " + rsM.getString("MechanicLastName"));
			mechR.setText(rsM.getString("MechanicHourlyRate") + " per hour");
			
			ResultSet rsL = db.query("SELECT Bill.MechanicCost,Bill.PartsCost, Bill.SPCCost, Bill.isSettled, (Bill.MechanicCost + Bill.PartsCost + Bill.SPCCost) as TotalCost FROM Bill WHERE BookingID='" + entry.getID() + "';");
			rsL.next();
			billM.setText(rsL.getString("MechanicCost"));
			billP.setText(rsL.getString("PartsCost"));
			billS.setText(rsL.getString("SPCCost"));
			billTotal.setText(rsL.getString("TotalCost"));
			boolean paid = rsL.getString("IsSettled").equals("1");
			if (paid)
			{
				billStatus.setText("PAID");
				billStatus.setStyle("-fx-text-fill: #388E3C !important; -fx-font-weight: bold; -fx-font-size: 20px;");
			}
			else
			{
				billStatus.setText("UNPAID");
				billStatus.setStyle("-fx-text-fill: #e53935 !important; -fx-font-weight: bold; -fx-font-size: 20px;");
			}
			
			ResultSet rsP = db.query("SELECT P.PartID, P.PartName, P.PartDescription, I.PartInstalledDate, P.PartCost FROM Part P "
				+ "INNER JOIN Parts_Installed I ON I.PartID=P.PartID "
				+ "WHERE VehicleRegNo = '" + entry.getVehReg() + "' AND BookingID = '" + entry.getID() + "';");
		
			colID.setCellValueFactory(new PropertyValueFactory<diagrep.Part, Integer>("id"));
			colName.setCellValueFactory(new PropertyValueFactory<diagrep.Part, String>("name"));
			colDes.setCellValueFactory(new PropertyValueFactory<diagrep.Part, String>("des"));
			colDate.setCellValueFactory(new PropertyValueFactory<diagrep.Part, String>("date"));
			colCost.setCellValueFactory(new PropertyValueFactory<diagrep.Part, String>("cost"));

			partsData =FXCollections.observableArrayList();
			while (rsP.next())
			{
				partsData.add(new Part(rsP.getInt("PartID"), rsP.getString("PartName"), rsP.getString("PartDescription"), rsP.getString("PartInstalledDate"), rsP.getString("PartCost")));	
			}
			partsTable.setItems(partsData);
			
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
		
	}
	
	@FXML
	private void updateMileage()
	{
		if (Integer.parseInt(vehMi.getText()) > mileage)
		{
			db.connect();
			try
			{
				String sql= "UPDATE Vehicle SET VehicleMileage='"+Integer.parseInt(vehMi.getText())+"' WHERE VehicleRegNo='"+entry.getVehReg()+"';";
				db.update(sql);
				parentController.reset();
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Update Success");
				alert.setContentText("The mileage for "+entry.getVehReg()+" has been updated to "+Integer.parseInt(vehMi.getText())+".");
				Optional<ButtonType> action = alert.showAndWait();
			}
			catch (SQLException se)
			{
				se.printStackTrace();
				Alert alertE = new Alert(AlertType.ERROR);
				alertE.setTitle("Connection Error");
				alertE.setContentText("Error connecting to the SQL database.");
				Optional<ButtonType> action = alertE.showAndWait();
			}

		}
		else if (Integer.parseInt(vehMi.getText()) == mileage)
		{
			Alert alertM = new Alert(AlertType.ERROR);
			alertM.setTitle("Mileage Update Error");
			alertM.setContentText("Your new mileage is the same as the current mileage.");
			Optional <ButtonType> action = alertM.showAndWait();
		}
		else
		{
			Alert alertM = new Alert(AlertType.ERROR);
			alertM.setTitle("Mileage Update Error");
			alertM.setContentText("Your new mileage cannot be lower than the current mileage.");
			Optional <ButtonType> action = alertM.showAndWait();
		}
	}
	
	@FXML
	private void quit()
	{
		Stage stage = (Stage) quitButton.getScene().getWindow();
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
	
}
