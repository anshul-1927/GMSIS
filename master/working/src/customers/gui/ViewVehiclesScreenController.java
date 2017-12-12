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
import vehicles.Booking;
import vehicles.NonWarranty;
import vehicles.UnderWarranty;
import vehicles.Vehicle;
import vehicles.VehicleWrapper;
import vehicles.gui.VehicleDetailsScreenController;

public class ViewVehiclesScreenController implements Initializable {

    // the customer for which the vehicles will be displayed
    private Customer customer;
    //databse instance
    final private Database db = Database.getInstance();

    @FXML
    private Button closeButton;

    @FXML
    private TableView vehiclesTable;

    @FXML
    private TableColumn<vehicles.Vehicle, String> vehicleRegCol;
    @FXML
    private TableColumn<vehicles.Vehicle, String> typeCol;
    @FXML
    private TableColumn<vehicles.Vehicle, String> makeCol;
    @FXML
    private TableColumn<vehicles.Vehicle, String> modelCol;
    @FXML
    private TableColumn<vehicles.Vehicle, String> mileageCol;
    @FXML
    private TableColumn<vehicles.Vehicle, String> engineCol;
    @FXML
    private TableColumn<vehicles.Vehicle, String> fuelCol;
    @FXML
    private TableColumn<vehicles.Vehicle, String> colourCol;
    @FXML
    private TableColumn<vehicles.Vehicle, String> motCol;

    @FXML
    private Label customerNameLabel;
    @FXML
    private Button viewVehicleRecord;

    //List used to store data from database
    private ObservableList<vehicles.Vehicle> data;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vehicleRegCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("regNum"));
        typeCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("vehicleType"));
        makeCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("make"));
        modelCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("model"));
        mileageCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("mileage"));
        engineCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("engSize"));
        fuelCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("fuel"));
        colourCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("col"));
        motCol.setCellValueFactory(new PropertyValueFactory<vehicles.Vehicle, String>("MoT"));

        vehiclesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        data = FXCollections.observableArrayList();

    }

    public void loadVehicles() {
        try {
//            ResultSet rs = db.query("SELECT * FROM BookingIntegrated WHERE BookingDate "+ operator +" "+ date +" AND CustomerID="+customer.getId()+";");
            db.connect();
            String sql = "SELECT * FROM Vehicle WHERE CustomerID=" + customer.getId() + ";";
            ResultSet rs = db.query(sql);
            while (rs.next()) {
                NonWarranty nonWarrantyRole = null;
                UnderWarranty underWarrantyRole = null;

                if (rs.getString("WarrantyName") == null || rs.getString("WarrantyExpDate") == null || rs.getString("WarrantyAddress") == null) {
                    nonWarrantyRole = new NonWarranty();
                    data.add(new Vehicle(rs.getString("VehicleType"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getString("VehicleModel"), rs.getString("VehicleEngSize"), rs.getString("VehicleFuelType"), rs.getString("VehicleColour"), rs.getString("VehicleMoTDate"), rs.getString("VehicleMileage"), "", nonWarrantyRole));
                } else {
                    underWarrantyRole = new UnderWarranty(rs.getString("WarrantyName"), rs.getString("WarrantyExpDate"), rs.getString("WarrantyAddress"));
                    data.add(new Vehicle(rs.getString("VehicleType"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getString("VehicleModel"), rs.getString("VehicleEngSize"), rs.getString("VehicleFuelType"), rs.getString("VehicleColour"), rs.getString("VehicleMoTDate"), rs.getString("VehicleMileage"), "", underWarrantyRole));
                }

//                data.add(new Vehicle(rs.getString("VehicleType"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getString("VehicleModel"), rs.getString("VehicleEngSize"), rs.getString("VehicleFuelType"), rs.getString("VehicleColour"), rs.getString("VehicleMoTDate"), rs.getString("VehicleMileage"), "", null));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getClass() + ": " + ex.getMessage());
        }
        vehiclesTable.setItems(data);
        db.closeConnection();
    }

    public void setCustomer(Customer c) {
        this.customer = c;
        customerNameLabel.setText(c.getFirstName() + " " + c.getLastName());
        loadVehicles();
    }

    public void vehicleRecord() throws IOException {

        ObservableList<Vehicle> selectedVehicle = vehiclesTable.getSelectionModel().getSelectedItems();
        if (selectedVehicle.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Select a vehicle");
            alert.setHeaderText(null);
            alert.setContentText("Select a vehicle before attempting to view the vehicle record.");
            alert.showAndWait();

            return;
        }
        Vehicle vec = selectedVehicle.get(0);
        Customer cust = null;
        Booking b = null;

        int cId = 0;
        String firstName = "";
        String lastName = "";
        String type = "";
        String address = "";
        String postcode = "";
        String phoneNumber = "";
        String emailAddress = "";

        try {
            db.connect();
            String sql = "SELECT Vehicle.*, Customer.*, Booking.* FROM Vehicle JOIN Customer On Vehicle.CustomerID=Customer.CustomerID JOIN Booking ON Vehicle.VehicleRegNo=Booking.VehicleRegNo WHERE Vehicle.VehicleRegNo='" + vec.getRegNum() + "';";
            ResultSet rs = db.query(sql);
            while (rs.next()) {
                cId = rs.getInt(15);
                firstName = rs.getString("CustomerFirstName");
                lastName = rs.getString("CustomerLastName");
                type = rs.getString("CustomerType");
                address = rs.getString("CustomerAddress");
                postcode = rs.getString("CustomerPostcode");
                phoneNumber = rs.getString("CustomerPhoneNo");
                emailAddress = rs.getString("CustomerEmail");

//                cust = new Customer(rs.getInt("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"), rs.getString("CustomerType"), rs.getString("CustomerAddress"), rs.getString("CustomerPostcode"), rs.getString("CustomerPhoneNo"), rs.getString("CustomerEmail"));
                b = new Booking(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), rs.getString("BookingDuration"), rs.getInt("MechanicID"), rs.getString("BookingDuration"), 0);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getClass() + ": " + ex.getMessage());
        }
        db.closeConnection();

        VehicleWrapper wrapper = new VehicleWrapper(new vehicles.Customer(cId, firstName, lastName, type, address, postcode, phoneNumber, emailAddress), vec, b);
        if (wrapper == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Select a vehicle first from the table");
            alert.showAndWait();
        } else {
            Vehicle veh = wrapper.getVehicle();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VehicleDetailsScreen.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            VehicleDetailsScreenController controller = fxmlLoader.<VehicleDetailsScreenController>getController();
            controller.setVehicle(wrapper);
            controller.reinit();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Vehicle Details");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(LoginScreenController.stage);
            stage.show();
        }
    }

    public void InstalledParts() throws IOException {
        ObservableList<Vehicle> selectedVehicle = vehiclesTable.getSelectionModel().getSelectedItems();
        if (selectedVehicle.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Select a vehicle");
            alert.setHeaderText(null);
            alert.setContentText("Select a vehicle before attempting to view the parts installed.");
            alert.showAndWait();
            return;
        }
        String reg = selectedVehicle.get(0).getRegNum();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewVehiclesPartsUsed.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("View Customer Vehicles");
        stage.initModality(Modality.APPLICATION_MODAL);
        ViewVehiclesPartsUsedController vvpuc = loader.getController();
        vvpuc.setVehicle(reg);
        stage.show();

    }

    public void close() {
        Stage thisStage = (Stage) closeButton.getScene().getWindow();
        thisStage.close();
    }
}
