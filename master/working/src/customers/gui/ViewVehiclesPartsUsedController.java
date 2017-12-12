/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.gui;

import diagrep.Diagrep;
import common.Database;
import common.LoginScreenController;
import customers.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import parts.Parts;
import parts.partsInstalled;
import vehicles.Booking;
import vehicles.NonWarranty;
import vehicles.UnderWarranty;
import vehicles.Vehicle;
import vehicles.VehicleWrapper;
import vehicles.gui.VehicleDetailsScreenController;

public class ViewVehiclesPartsUsedController implements Initializable {

    // the vechicle ID for which the parts used will be displayed
    private String vehicleReg;
    //databse instance
    final private Database db = Database.getInstance();

    @FXML
    private Button closeButton;

    @FXML
    private TableView partsTable;

    @FXML
    private TableColumn<vehicles.Vehicle, String> partNameCol;
    @FXML
    private TableColumn<vehicles.Vehicle, String> partDescriptionCol;
    @FXML
    private TableColumn<vehicles.Vehicle, String> dateInstalledCol;

    @FXML
    private Label customerNameLabel;

    //List used to store data from database
    private ObservableList<parts.partsInstalled> data;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partNameCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("custFName"));
        partDescriptionCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("custSName"));
        dateInstalledCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("bookingDate"));;

        partsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        data = FXCollections.observableArrayList();

    }

    public void loadPartsInstallled() {
        try {
//            ResultSet rs = db.query("SELECT * FROM BookingIntegrated WHERE BookingDate "+ operator +" "+ date +" AND CustomerID="+customer.getId()+";");
            db.connect();
            String sql = "SELECT Parts_Installed.*, Part.* FROM Parts_Installed JOIN Part ON Parts_Installed.PartID=Part.PartID Where VehicleRegNo='" + vehicleReg + "';";
            ResultSet rs = db.query(sql);
            while (rs.next()) {
                data.add(new partsInstalled(0, rs.getString("PartName"), rs.getString("PartDescription"), "", 0, "", rs.getString("PartInstalledDate")));
            }

        } catch (SQLException ex) {
//            System.out.println(ex.getClass() + ": " + ex.getMessage());
        }
        partsTable.setItems(data);
        db.closeConnection();
    }

    public void setVehicle(String reg) {
        this.vehicleReg = reg;
        customerNameLabel.setText("Vehicle Registration: "+reg);
        loadPartsInstallled();
    }

    public void close() {
        Stage thisStage = (Stage) closeButton.getScene().getWindow();
        thisStage.close();
    }
}
