/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts.gui;

import common.Database;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import parts.BookingView;

/**
 * FXML Controller class
 *
 * @author Mario_
 */

public class BookingsController implements Initializable {
    
    @FXML private Label customerID;
    @FXML private Label customerName;
    @FXML private Label vehicleID;
    
    @FXML private TableView<parts.BookingView> pastBookingsTable;
    @FXML private TableColumn<parts.BookingView, String> bookingTypePast;
    @FXML private TableColumn<parts.BookingView, String> bookingDatePast;
    
    @FXML private TableView<parts.BookingView> futureBookingsTable;
    @FXML private TableColumn<parts.BookingView, String> bookingTypeFuture;
    @FXML private TableColumn<parts.BookingView, String> bookingDateFuture;
      
    private PartsScreenController mainController;
    private ObservableList<parts.BookingView> data1;
    private ObservableList<parts.BookingView> data2;
    private Database db;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        db = Database.getInstance();
        
        bookingTypePast.setCellValueFactory(new PropertyValueFactory<parts.BookingView, String>("bookingType"));
        bookingDatePast.setCellValueFactory(new PropertyValueFactory<parts.BookingView, String>("bookingDate"));
        bookingTypeFuture.setCellValueFactory(new PropertyValueFactory<parts.BookingView, String>("bookingType"));
        bookingDateFuture.setCellValueFactory(new PropertyValueFactory<parts.BookingView, String>("bookingDate"));
        
    }    
    
    protected void populateTables(int bookingID) throws SQLException {
        data1 = FXCollections.observableArrayList();
        data2 = FXCollections.observableArrayList();
        
        db.connect();
        ResultSet rsP = db.query("SELECT BookingType, BookingDate FROM Booking WHERE (BookingID = '" + bookingID + "' AND BookingDate < CURRENT_DATE);");
        while(rsP.next()){
            data1.add(new BookingView(rsP.getString("BookingDate"), rsP.getString("BookingType")));
            pastBookingsTable.setItems(data1);
        }
        db.closeConnection();
        
    
        
        db.connect();
        ResultSet rsF = db.query("SELECT BookingType, BookingDate FROM Booking WHERE (BookingID = '" + bookingID + "' AND BookingDate > CURRENT_DATE);");
        while(rsF.next()){
            data2.add(new BookingView(rsF.getString("BookingDate"), rsF.getString("BookingType")));
            futureBookingsTable.setItems(data2);
        }
        db.closeConnection();
    }
    
    protected void populateLabels(int custId, String custName, String vehicleId){
        customerID.setText(Integer.toString(custId));
        customerName.setText(custName);
        vehicleID.setText(vehicleId);
        
    }    
    
    public void setMainController(PartsScreenController mainController)
    {
        this.mainController = mainController;
    }
 
    
    

}
