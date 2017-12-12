/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.gui;

import diagrep.Diagrep;
import common.Database;
import customers.Customer;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ViewBookingsScreenController implements Initializable {

    // the customer for which the bookings will be displayed
    private Customer customer;
    //used to determine if past or future bookings are shown
    private boolean showPastBookings;
    //databse instance
    final private Database db = Database.getInstance();

    @FXML
    private Button closeButton;
    
    @FXML
    private TableView bookingsTable;
    
    @FXML
    private TableColumn<diagrep.Diagrep, String> bookingTypeCol;
    @FXML
    private TableColumn<diagrep.Diagrep, String> dateCol;
    @FXML
    private TableColumn<diagrep.Diagrep, String> durationCol;
    @FXML
    private TableColumn<diagrep.Diagrep, String> vehicleRegCol;
    @FXML
    private TableColumn<diagrep.Diagrep, String> mechanicCol;
    
    @FXML
    private Label customerNameLabel;
    final Date date = new Date();
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    //List used to store data from database
    private ObservableList<diagrep.Diagrep> data;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bookingTypeCol.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("date"));
        durationCol.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("duration"));
        vehicleRegCol.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("vehReg"));
        mechanicCol.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("mechID"));

        bookingsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        data = FXCollections.observableArrayList();

        System.out.println(showPastBookings);

    }

    public void loadBookings() {
        String operator = "";
        operator = showPastBookings ? "<" : ">";
        try {
//            ResultSet rs = db.query("SELECT * FROM BookingIntegrated WHERE BookingDate "+ operator +" "+ date +" AND CustomerID="+customer.getId()+";");
            db.connect();
            String sql = "SELECT B.BookingID, B.BookingType, B.BookingDate, B.BookingDuration, B.VehicleRegNo, V.VehicleMake, V.VehicleMileage, B.CustomerID, C.CustomerFirstName, C.CustomerLastName, B.MechanicID, B.MechanicDuration "
                    + "FROM Booking B, Vehicle V, Customer C "
                    + "WHERE B.VehicleRegNo = V.VehicleRegNo AND B.CustomerID = C.CustomerID AND C.CustomerID=" + customer.getId() + ";";
            ResultSet rs = db.query(sql);
            while (rs.next()) {
                Date bookingDate = null;
                try {
                    bookingDate = dateFormat.parse(rs.getString("BookingDate"));
                } catch (ParseException ex) {
                    System.out.println(ex.getClass() + ": " + ex.getMessage());
                }
                if (showPastBookings) {
                    if (bookingDate.compareTo(date) <= 0) {
                        data.add(new Diagrep(
                                rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"),
                                rs.getString("BookingDuration"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"),
                                rs.getInt("VehicleMileage"), rs.getInt("CustomerID"), rs.getString("CustomerFirstName"),
                                rs.getString("CustomerLastName"), rs.getInt("MechanicID"), rs.getString("MechanicDuration")));
                    }
                } else {
                    if (bookingDate.compareTo(date) > 0) {
                        data.add(new Diagrep(
                                rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"),
                                rs.getString("BookingDuration"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"),
                                rs.getInt("VehicleMileage"), rs.getInt("CustomerID"), rs.getString("CustomerFirstName"),
                                rs.getString("CustomerLastName"), rs.getInt("MechanicID"), rs.getString("MechanicDuration")));
                    }
                }
            }
            bookingsTable.setItems(data);
        } catch (SQLException ex) {
            System.out.println(ex.getClass() + ": " + ex.getMessage());
        }
        db.closeConnection();
    }

    public void setCustomer(Customer c) {
        this.customer = c;
        customerNameLabel.setText(c.getFirstName() + " " + c.getLastName());
    }

    public void setPastOrFuture(int i) {
        if (i == 0) {
            showPastBookings = true;
        }
        loadBookings();
    }
    
    public void close(){
        Stage thisStage = (Stage) closeButton.getScene().getWindow();
        thisStage.close();
    }
}
