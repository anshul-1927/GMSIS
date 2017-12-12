/**
 * FXML Controller class
 * 
 * @author Mario Tawfelis 
 */


package parts.gui;

import common.Database;
import common.LoginScreenController;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import parts.partsInstalled;



public class PartsScreenController implements Initializable {

    
            /** FXML Components **/
    
    @FXML private TableView<parts.partsInstalled> table;

    @FXML private TableColumn<parts.partsInstalled, Integer> custIDCol;
    @FXML private TableColumn<parts.partsInstalled, String> custFNameCol;
    @FXML private TableColumn<parts.partsInstalled, String> custSNameCol;
    @FXML private TableColumn<parts.partsInstalled, String> vehicleIDCol;
    @FXML private TableColumn<parts.partsInstalled, Integer> bookingID;
    @FXML private TableColumn<parts.partsInstalled, String> bookingType;
    @FXML private TableColumn<parts.partsInstalled, String> bookingDate;
     
    @FXML private Button searchButton;
    @FXML private Button refreshButton;
    @FXML private Button stockButton;
    @FXML private Button viewParts;
    @FXML private Button bookings;
    
    @FXML private TextField searchField;
    
    @FXML private Label errors;
    
    @FXML private ComboBox searchByCB;
    
    
        /**Modality and Ownership**/
    
    public static  Stage installedStage;
    public static Stage stockStage;
    
       
    private ObservableList<parts.partsInstalled> data;
    private Database db;
    
    
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = Database.getInstance();       
              
        searchByCB.setItems(FXCollections.observableArrayList("First Name", "Last Name", "Vehicle Registration Number"));
        custIDCol.setCellValueFactory(new PropertyValueFactory<parts.partsInstalled, Integer>("custID"));
        custFNameCol.setCellValueFactory(new PropertyValueFactory<parts.partsInstalled, String>("custFName"));
        custSNameCol.setCellValueFactory(new PropertyValueFactory<parts.partsInstalled, String>("custSName"));
        vehicleIDCol.setCellValueFactory(new PropertyValueFactory<parts.partsInstalled, String>("vehicleRegNo"));
        bookingID.setCellValueFactory(new PropertyValueFactory<parts.partsInstalled, Integer>("bookingID"));
        bookingType.setCellValueFactory(new PropertyValueFactory<parts.partsInstalled, String>("bookingType"));
        bookingDate.setCellValueFactory(new PropertyValueFactory<parts.partsInstalled, String>("bookingDate"));
                
        displayRecords(null);
    }    
    
        
    public void displayRecords(String sql){       
        data = FXCollections.observableArrayList();
         
        //If it is not a query, display all data normally
        if(sql==null){       
            sql = "SELECT DISTINCT c.CustomerID, c.CustomerFirstName, c.CustomerLastName, v.VehicleRegNo, b.BookingID, b.BookingType, b.BookingDate " +
                  "FROM Booking b " + 
                  "JOIN Customer c " + "ON c.CustomerID=b.CustomerID " +
                  "JOIN Vehicle v " + "ON v.VehicleRegNo=b.VehicleRegNo";
        }
        
        try {      
            db.connect();
            ResultSet rs = db.query(sql);
            while(rs.next()){
                data.add(new partsInstalled(
                                            rs.getInt(1),rs.getString(2),
                                            rs.getString(3),rs.getString(4),
                                            rs.getInt(5),rs.getString(6),
                                            rs.getString(7)));
            }
            
            table.setItems(data);

            db.closeConnection();
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }    
    }

    
    @FXML
    private void handleBookingsButton(ActionEvent event) throws IOException, SQLException{
        
        partsInstalled part = table.getSelectionModel().getSelectedItem();
        
        if(part == null){
            warningDialog("Please select a vehicle from the table.");
        }else{
            int customerId = table.getSelectionModel().getSelectedItem().getCustID();
            String customerName = table.getSelectionModel().getSelectedItem().getCustFName() + " " + table.getSelectionModel().getSelectedItem().getCustSName();
            String vehicleId = table.getSelectionModel().getSelectedItem().getVehicleRegNo();
            int bookingId = table.getSelectionModel().getSelectedItem().getBookingID();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Bookings.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            
            BookingsController bookingsController = fxmlLoader.<BookingsController>getController();
            bookingsController.setMainController(this);
            bookingsController.populateLabels(customerId, customerName, vehicleId);
            bookingsController.populateTables(bookingId);
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Bookings");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(LoginScreenController.stage);
            stage.show();
                   
        }
    }
    
    @FXML
    private void handleViewParts(ActionEvent event) throws IOException, SQLException{  
        
        partsInstalled part = table.getSelectionModel().getSelectedItem();
        
        if(part == null){
            warningDialog("Please select a booking from the table.");       
        }else{                     
            String vehicle = (String) table.getSelectionModel().getSelectedItem().getVehicleRegNo();
            int bID =  table.getSelectionModel().getSelectedItem().getBookingID();
            String customerName = table.getSelectionModel().getSelectedItem().getCustFName() + " " + table.getSelectionModel().getSelectedItem().getCustSName();
            String customerID = Integer.toString(table.getSelectionModel().getSelectedItem().getCustID());
            String bType = table.getSelectionModel().getSelectedItem().getBookingType();
            String bDate = table.getSelectionModel().getSelectedItem().getBookingDate();
                     
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Installed.fxml"));     
            Parent root = (Parent) fxmlLoader.load(); 
            
            InstalledController partsController = fxmlLoader.<InstalledController>getController();
            partsController.setParentController(this);  
            partsController.selectedRecord(vehicle, bID);
            partsController.populateLabels(customerID, customerName, vehicle, bID, bType, bDate);
            
            Scene scene = new Scene(root);          
            installedStage = new Stage();
            installedStage.setScene(scene);
            installedStage.centerOnScreen();
            installedStage.setTitle("Installed Parts");
            installedStage.initModality(Modality.WINDOW_MODAL);
            installedStage.initOwner(LoginScreenController.stage);
            installedStage.show();
        }
    }
    
    
    @FXML
    private void handleStockButtonAction(ActionEvent event) throws IOException{
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StockScreen.fxml"));     
        Parent root = (Parent) fxmlLoader.load(); 
        
        StockScreenController stockController = fxmlLoader.<StockScreenController>getController();
        stockController.setParentController(this);
        Scene scene = new Scene(root);
        
        stockStage = new  Stage();
        stockStage.setScene(scene);
        stockStage.centerOnScreen();
        stockStage.setTitle("Stock Levels");
        stockStage.initModality(Modality.WINDOW_MODAL);
        stockStage.initOwner(LoginScreenController.stage);
        stockStage.show();
    }
    
    
    @FXML
    private void handleSearch(ActionEvent Event){
        
        String searchFor = searchField.getText();   
        String searchBy = (String) searchByCB.getSelectionModel().getSelectedItem();
        
        if(searchBy == null){
            warningDialog("Please specify a searching criteria from the dropdown list.");
        }else{
        String sql = "";
        
        switch (searchBy)	
        {
            case "Vehicle Registration Number":
                sql = "SELECT DISTINCT c.CustomerID, c.CustomerFirstName, c.CustomerLastName, v.VehicleRegNo, b.BookingID, b.BookingType, b.BookingDate " +
                      "FROM Booking b " + 
                      "JOIN Customer c " + "ON c.CustomerID=b.CustomerID " +
                      "JOIN Vehicle v " + "ON v.VehicleRegNo=b.VehicleRegNo " +
                      "WHERE v.VehicleRegNo LIKE '%"+searchFor+"%';"; 
                break;
            case "First Name":
                sql = "SELECT DISTINCT c.CustomerID, c.CustomerFirstName, c.CustomerLastName, v.VehicleRegNo, b.BookingID, b.BookingType, b.BookingDate " +
                      "FROM Booking b " + 
                      "JOIN Customer c " + "ON c.CustomerID=b.CustomerID " +
                      "JOIN Vehicle v " + "ON v.VehicleRegNo=b.VehicleRegNo " +
                      "WHERE c.CustomerFirstName LIKE '%"+searchFor+"%';";
                break;
            case "Last Name":
                sql = "SELECT c.CustomerID, c.CustomerFirstName, c.CustomerLastName, v.VehicleRegNo, b.BookingID, b.BookingType, b.BookingDate " +
                      "FROM Booking b " + 
                      "JOIN Customer c " + "ON c.CustomerID=b.CustomerID " +
                      "JOIN Vehicle v " + "ON v.VehicleRegNo=b.VehicleRegNo " +
                      "WHERE c.CustomerLastName LIKE '%"+searchFor+"%';";
                break;                   
        }        
        displayRecords(sql); 
        }
    }
    
    
    @FXML 
    private void handleRefreshButtonAction(ActionEvent event){
        displayRecords(null);
        searchField.clear();
    }  

    
    private void warningDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Missing Action");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
       
    }
}//END PartsScreenController

 
