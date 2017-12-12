/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles.gui;

import common.Database;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import vehicles.Booking;
import vehicles.Customer;
import vehicles.Vehicle;
import vehicles.VehicleWrapper;
import vehicles.partsInstalle;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class VehiclePartsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button exit;

    @FXML
    private TextField custNameField;
    @FXML
    private TextField custSurnameField;
    @FXML
    private TextField vehRegField;
    @FXML
    private TextField vehModelField;
    @FXML
    private TextField bookingType;
    @FXML
    private TextField bookingDate;

    @FXML
    private TableView<partsInstalle> partsTable;
    @FXML
    private TableColumn<partsInstalle, String> colPartName;
    @FXML
    private TableColumn<partsInstalle, String> colPartInstallDate;
    @FXML
    private ObservableList<partsInstalle> partsObservable;

    private Database db;

    private VehicleWrapper wrapper;
    
    private Booking booking;
    
    private Vehicle vehicle;
    
    private Customer customer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void reinit(){
        db = db.getInstance();
        this.setVariables();
        this.setFields();
        this.setTable();
        this.setParts();
    }
    private void setVariables(){
        booking=wrapper.getBooking();
        vehicle=wrapper.getVehicle();
        customer=wrapper.getCustomer();
    }
    private void setTable() {
        colPartName.setCellValueFactory(new PropertyValueFactory<partsInstalle, String>("partName"));
        colPartInstallDate.setCellValueFactory(new PropertyValueFactory<partsInstalle, String>("installDate"));
    }

    private void setParts(){
        partsObservable = FXCollections.observableArrayList();
        String sql = "SELECT PartName, SRReturnDate "
                + "FROM Part p "
                + "JOIN SpecialistRepair r "
                + "ON 	p.PartID=r.PartID "
                + "WHERE r.BookingID='" + booking.getID()+ "' ";

        db.connect();
        try {
            ResultSet rs = db.query(sql);
            while (rs.next()) 
                partsObservable.add(new partsInstalle(0, rs.getString("PartName"), rs.getString("SRReturnDate"), "", 0, "", "", "", 0));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();

        String sql2 = "SELECT PartName, PartInstalledDate "
                + "FROM Part p "
                + "JOIN Parts_Installed pi "
                + "ON p.PartID=pi.PartID "
                + "WHERE pi.BookingID='" + booking.getID()+ "';";

        db.connect();
        SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        
        try{
            ResultSet rs = db.query(sql2);
            while (rs.next()) 
                partsObservable.add(new partsInstalle(0, rs.getString("PartName"), myFormat.format(fromUser.parse(rs.getString("PartInstalledDate"))), "", 0, "", "", "", 0));
            
        }catch (SQLException e) {
            e.printStackTrace();
        }catch(ParseException p){
            p.printStackTrace();
        }
        db.closeConnection();

        partsTable.setItems(partsObservable);

    }

    private void setFields() {
        custNameField.setText(customer.getFirstName());
        custSurnameField.setText(customer.getLastName());
        vehRegField.setText(vehicle.getRegNum());
        vehModelField.setText(vehicle.getModel());
        bookingType.setText(booking.getType());
        bookingDate.setText(booking.getDate());
    }

    public void setVehicle(VehicleWrapper veh) {
        this.wrapper = veh;
    }

    @FXML
    public void handleExit() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

}
