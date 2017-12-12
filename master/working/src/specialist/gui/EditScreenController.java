/**
 * FXML Controller class
 *
 * @author Lloyd
 */

package specialist.gui;

import common.Database;
import specialist.Specialist;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
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

public class EditScreenController implements Initializable{
    
    private Specialist specialist;
    private SpecialistScreenController mainScreen;
    @FXML private Button closeButton;
    @FXML private ComboBox<String> spcChoice, bookChoice, partChoice, custChoice;
    @FXML private DatePicker dDatePicker, rDatePicker; 
    @FXML private Label vRegNoLabel, vMakeLabel, vModelLabel, bTypeLabel, bDateLabel, bDurationLabel, errorMessage;
    @FXML private TextField costField;
    
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
            errorAlert("Make sure cost is in the correct format. (e.g. 150, 150.0 or 150.00 to represent £150.00)");
        }
        else if(dDatePicker.getValue().isAfter(rDatePicker.getValue())){
            errorAlert("Return date must be after Delivery date.");
        }
        else{
            try{
                //remove from old bill
                Database.getInstance().connect();
                String sql = "SELECT Bill.SPCCost FROM Bill WHERE BookingID="+specialist.getBookId();
                ResultSet rs = Database.getInstance().query(sql);
                double currentCost = Double.parseDouble(rs.getString("SPCCost"));
                currentCost -= specialist.getCost();
                if(currentCost<0){
                    currentCost = 0;
                }
                sql = "UPDATE Bill SET SPCCost="+currentCost+" WHERE BookingID="+specialist.getBookId();
                Database.getInstance().update(sql);
                Database.getInstance().closeConnection();
                
                //change specialist repair
                int spcId = Integer.parseInt(spcChoice.getSelectionModel().getSelectedItem().split("\\|")[0]);
                int newBookId = Integer.parseInt(bookChoice.getSelectionModel().getSelectedItem().split("\\|")[0]);
                if(partChoice.getSelectionModel().getSelectedItem() == null || partChoice.getSelectionModel().getSelectedItem().equals("None")){
                    sql = "UPDATE SpecialistRepair SET  SRReturnDate='"+rDatePicker.getValue()+"', SRDeliveryDate='"+dDatePicker.getValue()+"', SRCost="+Double.parseDouble(costField.getText())+",PartID=NULL, BookingID="+newBookId+", SPCID="+spcId+" "
                            + "WHERE SRID="+specialist.getId();

                }else{
                    int partId = Integer.parseInt(partChoice.getSelectionModel().getSelectedItem().split("\\|")[0]);
                    sql = "UPDATE SpecialistRepair SET SRReturnDate='"+rDatePicker.getValue()+"', SRDeliveryDate='"+dDatePicker.getValue()+"', SRCost="+Double.parseDouble(costField.getText())+" ,PARTID="+partId+" ,BookingID="+newBookId+", SPCID="+spcId+" "
                            + "WHERE SRID="+specialist.getId();
                }
                Database.getInstance().connect();
                Database.getInstance().update(sql);
                Database.getInstance().closeConnection();
                
                //add to new bill
                Database.getInstance().connect();
                sql = "SELECT Bill.SPCCost FROM Bill WHERE BookingID="+newBookId;
                ResultSet rs2 = Database.getInstance().query(sql);
                currentCost = Double.parseDouble(rs2.getString("SPCCost"));
                currentCost += Double.parseDouble(costField.getText());
                if(currentCost<0){
                    currentCost = 0;
                }
                sql = "UPDATE Bill SET SPCCost="+currentCost+" WHERE BookingID="+newBookId;
                Database.getInstance().update(sql);
                Database.getInstance().closeConnection();
            
            }catch(SQLException e){
                System.err.println(e);
            }
            closeWindow();
        }
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
        mainScreen.fillTable();
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
                bookList.add(rs.getString("BookingID")+"| Vehicle: "+rs.getString("VehicleRegNo")+"| Date: "+rs.getString("BookingDate"));
            }
            Database.getInstance().closeConnection();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return bookList;
    }
    
    public void setBookComboBox(){
        bookChoice.setItems(getBookings());
    }
    
    public void setBookDetails(){
        if(bookChoice.getSelectionModel().getSelectedItem()!=null){
            int bookId = Integer.parseInt(bookChoice.getSelectionModel().getSelectedItem().split("\\|")[0]);
            String sql = "SELECT Booking.BookingType, Booking.BookingDate, Booking.BookingDuration, Booking.VehicleRegNo, Vehicle.VehicleMake, Vehicle.VehicleModel FROM Booking "
                    + "INNER JOIN Vehicle ON Booking.VehicleRegNo=Vehicle.VehicleRegNo "
                    + "WHERE BookingID="+bookId;
            Database.getInstance().connect();
            try{
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
    
    public void displayPassedSpecialist(Specialist specialist, SpecialistScreenController mainScreen){
        this.mainScreen = mainScreen;
        spcChoice.getSelectionModel().select(specialist.getSpcId()+"| "+specialist.getSpcName());
        custChoice.getSelectionModel().select(specialist.getCustomerId()+"| "+specialist.getCustomerFName()+" "+specialist.getCustomerLName());     
        setBookComboBox();
        bookChoice.getSelectionModel().select(specialist.getBookId()+"| "+specialist.getVehicleRegNo()+"| "+specialist.getBookingDate());
        setBookDetails();
        if(specialist.getPartName() != null){
            partChoice.getSelectionModel().select(specialist.getPartId()+"| "+specialist.getPartName());
        }
        dDatePicker.setValue(specialist.getDeliveryDate());
        rDatePicker.setValue(specialist.getReturnDate());
        costField.setText(Double.toString(specialist.getCost()));
        this.specialist = specialist;
    }
    
}