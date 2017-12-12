/**
 * FXML Controller class
 *
 * @author Lloyd
 */

package specialist.gui;

import common.Database;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddScreenController implements Initializable{
    
    @FXML private Button closeButton;
    @FXML private ComboBox<String> spcChoice, bookChoice, partChoice, custChoice;
    @FXML private DatePicker dDatePicker, rDatePicker;   
    @FXML private Label vRegNoLabel, vMakeLabel, vModelLabel, bTypeLabel, bDateLabel, bDurationLabel, errorMessage;
    @FXML private TextField costField;
    private SpecialistScreenController mainScreen;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        custChoice.setItems(getCustomers());
        partChoice.setItems(getParts());
        spcChoice.setItems(getSPCs());
    }
    
    public void confirmAdd(){
        if(costField.getText()==null || costField.getText().isEmpty() || spcChoice.getSelectionModel().getSelectedItem()==null || bookChoice.getSelectionModel().getSelectedItem()==null || dDatePicker.getValue()==null || rDatePicker.getValue()==null){
            errorAlert("Please make sure to fill all necessary field.");
        }
        else if(!costField.getText().matches("\\d*\\.\\d\\d") && !costField.getText().matches("\\d*") && !costField.getText().matches("\\d*\\.\\d")){
            errorAlert("Make sure cost is in the correct format. (e.g. 150 or 150.00 to represent £150.00)");
        }
        else if(dDatePicker.getValue().isAfter(rDatePicker.getValue())){
            errorAlert("Return date must be after Delivery date.");
        }
        else if(dDatePicker.getValue().isBefore(LocalDate.now()) || dDatePicker.getValue().isBefore(LocalDate.now())){
            errorAlert("Repairs must be made in the future.");
        }
        else{
            int spcId = Integer.parseInt(spcChoice.getSelectionModel().getSelectedItem().split("\\|")[0]);
            int bookId = Integer.parseInt(bookChoice.getSelectionModel().getSelectedItem().split("\\|")[0]);
            Double cost = Double.parseDouble(costField.getText());
            String sql = "";
            if(partChoice.getSelectionModel().getSelectedItem() == null || partChoice.getSelectionModel().getSelectedItem().equals("None")){
                sql = "INSERT INTO SpecialistRepair VALUES (NULL, '"+rDatePicker.getValue()+"', '"+dDatePicker.getValue()+"', "+cost+", NULL, "+bookId+", "+spcId+")";

            }else{
                int partId = Integer.parseInt(partChoice.getSelectionModel().getSelectedItem().split("\\|")[0]);
                sql = "INSERT INTO SpecialistRepair VALUES (NULL, '"+rDatePicker.getValue()+"', '"+dDatePicker.getValue()+"', "+cost+", "+partId+", "+bookId+", "+spcId+")";
            }
            try{
                Database.getInstance().connect();
                Database.getInstance().update(sql);
                sql = "Select SPCCost FROM Bill WHERE BookingID="+bookId;
                ResultSet rs = Database.getInstance().query(sql);
                double currentCost = Double.parseDouble(rs.getString("SPCCost"));
                currentCost += cost;
                sql = "Update Bill SET SPCCost="+currentCost+" WHERE BookingID="+bookId; 
                Database.getInstance().update(sql);
                Database.getInstance().closeConnection();         
            }catch(SQLException e){
                System.err.println(e);
            }
            closeWindow();
        }
    }
    
    public void setMainScreen(SpecialistScreenController mainScreen){
        this.mainScreen = mainScreen;
    }
    
    public void errorAlert(String message){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void closeWindow(){
        Stage buttonStage = (Stage) closeButton.getScene().getWindow();
        mainScreen.refreshPage();
        buttonStage.close();
    }
    
    public ObservableList getCustomers(){
        ObservableList<String> custList = FXCollections.observableArrayList();
        String sql = "Select DISTINCT Customer.CustomerID, Customer.CustomerFirstName, Customer.CustomerLastName FROM Booking "
                + "INNER JOIN Vehicle ON Booking.VehicleRegNo=Vehicle.VehicleRegNo "
                + "INNER JOIN Customer ON Vehicle.CustomerID=Customer.CustomerID "
                + "ORDER BY Customer.CustomerID";
        try{
            Database.getInstance().connect();
            ResultSet rs = Database.getInstance().query(sql);
            while(rs.next()){
                custList.add(rs.getString("CustomerID")+"| "+rs.getString("CustomerFirstName")+" "+rs.getString("CustomerLastName"));
            }
            Database.getInstance().closeConnection();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return custList;
    }
       
    public ObservableList getSPCs(){
        ObservableList<String> spcList = FXCollections.observableArrayList();
        try{
            Database.getInstance().connect();
            ResultSet rs = Database.getInstance().query("SELECT SPCID, SPCName FROM SPC WHERE SPCInUse=1");
            while(rs.next()){
                spcList.add(rs.getString("SPCID")+"| "+rs.getString("SPCName"));
            }
            Database.getInstance().closeConnection();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return spcList;
    }
    
    public ObservableList getBookings(){
        ObservableList<String> bookList = FXCollections.observableArrayList();
        String customer = "";
        if(custChoice.getSelectionModel().getSelectedItem()!= null){
            customer = custChoice.getSelectionModel().getSelectedItem().split("\\|")[0];
        }
        try{
            Database.getInstance().connect();
            ResultSet rs = Database.getInstance().query("SELECT Booking.BookingID, Booking.VehicleRegNo, Booking.BookingDate FROM Booking "
                    + "INNER JOIN Vehicle ON Booking.VehicleRegNo=Vehicle.VehicleRegNo "
                    + "WHERE Vehicle.CustomerID LIKE '%"+customer+"%'");
            while(rs.next()){
                LocalDate date = LocalDate.parse(rs.getString("BookingDate").split("\\s+")[0]);
                if(LocalDate.now().isBefore(date) || LocalDate.now().isEqual(date)){
                    bookList.add(rs.getString("BookingID")+"| Vehicle: "+rs.getString("VehicleRegNo")+"| Date: "+rs.getString("BookingDate"));
                }
            }
            Database.getInstance().closeConnection();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return bookList;
    }
    
    public void setBookComboBox(){
        bookChoice.setItems(getBookings());
        bookChoice.getSelectionModel().selectFirst();
    }
    
    public void setBookDetails(){
        if(bookChoice.getSelectionModel().getSelectedItem()!=null){
            int bookId = Integer.parseInt(bookChoice.getSelectionModel().getSelectedItem().split("\\|")[0]);
            String sql = "SELECT Booking.BookingType, Booking.BookingDate, Booking.BookingDuration, Booking.VehicleRegNo, Vehicle.VehicleMake, Vehicle.VehicleModel FROM Booking "
                    + "INNER JOIN Vehicle ON Booking.VehicleRegNo=Vehicle.VehicleRegNo "
                    + "WHERE BookingID="+bookId;
            try{
                Database.getInstance().connect();
                ResultSet rs = Database.getInstance().query(sql);
                vRegNoLabel.setText(rs.getString("VehicleRegNo"));
                vMakeLabel.setText(rs.getString("VehicleMake"));
                vModelLabel.setText(rs.getString("VehicleModel"));
                bTypeLabel.setText(rs.getString("BookingType"));
                bDateLabel.setText(rs.getString("BookingDate"));
                bDurationLabel.setText(rs.getString("BookingDuration"));
                Database.getInstance().closeConnection();

            }catch(SQLException e){
                System.err.println(e);
            }
        }else{
            vRegNoLabel.setText(null);
            vMakeLabel.setText(null);
            vModelLabel.setText(null);
            bTypeLabel.setText(null);
            bDateLabel.setText(null);
            bDurationLabel.setText(null);   
        }
    }
    
    public void noCustChoice(){
        if(custChoice.getSelectionModel().getSelectedItem()==null){
            errorAlert("Select a Customer before a Booking");
        }
    }
    
    public ObservableList getParts(){
        ObservableList partList = FXCollections.observableArrayList();
        partList.add("None");
        try{
            Database.getInstance().connect();
            ResultSet rs = Database.getInstance().query("SELECT PartID, PartName FROM Part");
            while(rs.next()){
                partList.add(rs.getString("PartID")+"| "+rs.getString("PartName"));
            }
            Database.getInstance().closeConnection();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return partList;
    }
    
}