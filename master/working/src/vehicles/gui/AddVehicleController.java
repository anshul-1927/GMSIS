/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles.gui;

import common.Database;
import customers.Customer;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import vehicles.Vehicle;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class AddVehicleController implements Initializable {

   @FXML
    private ComboBox<customers.Customer> customerDropDownList;
    @FXML
    private ComboBox<vehicles.Vehicle> vehicleTemplateDropDownList;
    @FXML
    private ComboBox<String> vehicleTypeDropDownList;
    @FXML
    private TextField modelFieldAdd;
    @FXML
    private TextField regNumberFieldAdd;
    @FXML
    private TextField manufacturerFieldAdd;
    @FXML
    private TextField engineSizeFieldAdd;
    @FXML
    private TextField fuelTypeFieldAdd;
    @FXML
    private TextField colourFieldAdd;
    @FXML
    private TextField mileageFieldAdd;
    @FXML
    private DatePicker MoTFieldAdd;
    @FXML
    private TextField warantyCompanyField;
    @FXML
    private TextField warantyAddressField;
    @FXML
    private DatePicker warantyExpDateField;
    @FXML
    private RadioButton uWarranty;
    @FXML
    private RadioButton wWarranty;
    
    private ObservableList<vehicles.Vehicle> vehicleTemplate;

    private ObservableList<customers.Customer> customers;
    
    private ToggleGroup groupWarranty;
    
    private Database db;
    
    private VehiclesScreenController parent;
    
    @FXML
    private Button exit;
    /**
     *  Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = Database.getInstance();
        this.setWarrantyToggle();
        this.setVehicleTemplate();
        this.setCustomers();
        this.setDropDown();
        
    }   
    private void setDropDown() {
        vehicleTypeDropDownList.setItems(FXCollections.observableArrayList("Car", "Van", "Truck"));
    }

    private void setWarrantyToggle(){
        groupWarranty=new ToggleGroup();
        uWarranty.setToggleGroup(groupWarranty);
        wWarranty.setToggleGroup(groupWarranty);
        groupWarranty.selectToggle(uWarranty);
        groupWarranty.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                    if (new_toggle == wWarranty){
                        warantyCompanyField.setText("Not Applicable");
                        warantyAddressField.setText("Not Applicable");
                        warantyExpDateField.setValue(null);
                        
                        warantyCompanyField.setEditable(false);
                        warantyAddressField.setEditable(false);
                        warantyExpDateField.setDisable(true);
                    }else{
                        clearWarrantyFields();
                        warantyCompanyField.setEditable(true);
                        warantyAddressField.setEditable(true);
                        warantyExpDateField.setDisable(false);
                    }
                    
                       
            }
        });
    }
    private void clearWarrantyFields(){
        warantyCompanyField.clear();
        warantyAddressField.clear();
        warantyExpDateField.setValue(null);
    }
    
    private void setCustomers() {
        customers = FXCollections.observableArrayList();
        String getCustomersQuery = "SELECT CustomerID, CustomerFirstName, CustomerLastName FROM Customer;";
        ResultSet customersRs;
        try {
            db.connect();
            customersRs = db.query(getCustomersQuery);

            while (customersRs.next()) {
                Customer cs = new Customer(customersRs.getInt("CustomerID"), customersRs.getString("CustomerFirstName"), customersRs.getString("CustomerLastName"), "", "", "", "", "");
                customers.add(cs);
            }
            db.closeConnection();
        } catch (SQLException ex) {
        }
        customerDropDownList.setItems(customers);
    }
    
    @FXML
    public void addVehicle() {

        if (customerDropDownList.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer!");
            alert.showAndWait();
            return;
        }
        if (vehicleTypeDropDownList.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a vehicle type!");
            alert.showAndWait();
            return;
        }
        if (modelFieldAdd.getText() == null || modelFieldAdd.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in model details!");
            alert.showAndWait();
            return;
        }

        if (regNumberFieldAdd.getText() == null || regNumberFieldAdd.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in the Registration number!");
            alert.showAndWait();
            return;
        }

        if (manufacturerFieldAdd.getText() == null || manufacturerFieldAdd.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in manufacturer details!");
            alert.showAndWait();
            return;
        }
        if (engineSizeFieldAdd.getText() == null || engineSizeFieldAdd.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in engine size detail!");
            alert.showAndWait();
            return;
        }
        if (fuelTypeFieldAdd.getText() == null || fuelTypeFieldAdd.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in fuel type detail!");
            alert.showAndWait();
            return;
        }
        if (colourFieldAdd.getText() == null || colourFieldAdd.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in colour detail!");
            alert.showAndWait();
            return;
        }
        
        if (MoTFieldAdd.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in MoT date!");
            alert.showAndWait();
            return;
        }
        if (mileageFieldAdd.getText() == null || mileageFieldAdd.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in Mileage details!");
            alert.showAndWait();
            return;
        }
        Customer customerSelection = customerDropDownList.getSelectionModel().getSelectedItem();
        String typeSelection = vehicleTypeDropDownList.getValue();
        int customerID = customerSelection.getId();
        String mod = modelFieldAdd.getText();
        String regNumber = regNumberFieldAdd.getText();
        String manufacturer = manufacturerFieldAdd.getText();
        String engineSize = engineSizeFieldAdd.getText();
        String fuelType = fuelTypeFieldAdd.getText();
        String colour = colourFieldAdd.getText();
        String MoT = MoTFieldAdd.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String milage = mileageFieldAdd.getText();
        
        String regexDate="([0-9]{4})/([0-9]{2})/([0-9]{2})";
        String regexAddress = "[\\w\\s\\d\\.\\,]+";
        String regexChar= "[a-zA-Z]+";
        String regexModel="[\\w\\d]+";
        String regex5="[-+]?[0-9]*\\.?[0-9]+";
        
        if(!mod.matches(regexModel) ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Vehicle Model");
            alert.setContentText("Please do not enter illegal characters");
            alert.showAndWait();
            return;
        }
        if(!regNumber.matches("[\\w\\s]+") ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Vehicle Registration Number");
            alert.setContentText("Please do not enter illegal characters");
            alert.showAndWait();
            return;
        }
        if( !manufacturer.matches(regexChar) ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Vehicle Manufacturer");
            alert.setContentText("Please do not enter illegal characters");
            alert.showAndWait();
            return;
        }
        if(!fuelType.matches(regexChar)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Vehicle Fuel Type");
            alert.setContentText("Please do not enter illegal characters");
            alert.showAndWait();
            return;
        }
        if(!engineSize.matches(regex5) ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Vehicle Engine Size Input");
            alert.setContentText("The input should be a floating point number in the form of 0.0");
            alert.showAndWait();
            return;
        }
        if(!colour.matches(regexChar)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Vehicle Colour");
            alert.setContentText("Please do not enter illegal characters");
            alert.showAndWait();
            return;
        }
        if(!milage.matches(regex5)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Vehicle Mileage");
            alert.setContentText("Please enter only numerical digits!");
            alert.showAndWait();
            return;
        }
        

        String insertVehicle;
        if(groupWarranty.getSelectedToggle()==wWarranty){  

            insertVehicle = "INSERT INTO Vehicle (VehicleType, VehicleRegNo, VehicleModel, VehicleMake,VehicleMileage, VehicleEngSize, VehicleFuelType, VehicleColour, VehicleMoTDate, CustomerID,  WarrantyName,WarrantyAddress,WarrantyExpDate) VALUES('"
                    + typeSelection + "','" + regNumber + "','" + mod + "','" + manufacturer + "','" + milage + "','" + engineSize + "','" + fuelType + "','" + colour + "','" + MoT + "','" + customerID + "', NULL, NULL, NULL );";

            db.connect();
            try {
                db.update(insertVehicle);
            } catch (SQLException ex) {
                ex.printStackTrace();
                if(ex.getErrorCode() == 0){
                     regNumberFieldAdd.clear();
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setTitle("VEHICLE EXISTS");
                     alert.setHeaderText("Vehicle: "+regNumber+" already exists");
                     alert.setContentText("Please add another vehicle that does not exist!");
                     alert.showAndWait();
                     return;
                }
            }
            db.closeConnection();
            clearAddFields();
            parent.outputTableData(null);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Vehicle: "+regNumber);
            alert.setContentText("Added successfully without warranty!");
            alert.showAndWait();
            return;

        } else {

            if (warantyCompanyField.getText() == null || warantyCompanyField.getText().trim().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Fill in Warranty Company details!");
                alert.showAndWait();
                return;
            }
            if (warantyAddressField.getText() == null || warantyAddressField.getText().trim().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Fill in Warranty Address details!");
                alert.showAndWait();
                return;
            }
            if (warantyExpDateField.getValue() == null || warantyAddressField.getText().trim().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Fill in Warranty Expiry Date details!");
                alert.showAndWait();
                return;
            }

            String warantyComp = warantyCompanyField.getText();
            String warAdr = warantyAddressField.getText();
            String expDate = warantyExpDateField.getValue().format(DateTimeFormatter.ofPattern("yyyy/dd/MM"));
            if(!warantyComp.matches(regexChar)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Warranty Company");
                alert.setContentText("Please do not enter illegal characters");
                alert.showAndWait();
                return;
            }
            if(!warAdr.matches(regexAddress)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Warranty Address");
                alert.setContentText("Please do not enter illegal characters");
                alert.showAndWait();
                return;
            }
            insertVehicle = "INSERT INTO Vehicle (VehicleType, VehicleRegNo, VehicleModel, VehicleMake,VehicleMileage, VehicleEngSize, VehicleFuelType, VehicleColour, VehicleMoTDate, CustomerID, WarrantyName,WarrantyAddress,WarrantyExpDate)VALUES('"
                    + typeSelection + "','" + regNumber + "','" + mod + "','" + manufacturer + "','" + milage + "','" + engineSize + "','" + fuelType + "','" + colour + "','" + MoT + "','" + customerID + "','" + warantyComp + "','" + warAdr + "','" + expDate + "');";

            db.connect();
            try {
                db.update(insertVehicle);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            db.closeConnection();
            parent.outputTableData(null);
            clearAddFields();
           // outputTableData(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Vehicle: "+regNumber);
            alert.setContentText("Added successfully with warranty!");
            alert.showAndWait();       
        }
        return;
    }
    private void setVehicleTemplate() {
        String getTemplateQuery = "SELECT * FROM vehicle_template;";

        try {
            db.connect();
            ResultSet TemplateRs = db.query(getTemplateQuery);

            vehicleTemplate = FXCollections.observableArrayList();

            while (TemplateRs.next()) {
                Vehicle vh = new Vehicle(TemplateRs.getString("Type"), "", TemplateRs.getString("template_make"), TemplateRs.getString("template_model"), TemplateRs.getString("template_engine_size"), TemplateRs.getString("template_fuel_type"), "", "", "", "", null);
                vehicleTemplate.add(vh);
            }
            db.closeConnection();
        } catch (SQLException ex) {

            
        }

        vehicleTemplateDropDownList.setItems(vehicleTemplate);
        vehicleTemplateDropDownList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vehicle>() {

            @Override
            public void changed(ObservableValue<? extends Vehicle> observable, Vehicle oldValue, Vehicle veh) {

                vehicleTypeDropDownList.setValue(veh.getVehicleType());
                manufacturerFieldAdd.setText(veh.getMake());
                modelFieldAdd.setText(veh.getModel());
                engineSizeFieldAdd.setText(veh.getEngSize());
                fuelTypeFieldAdd.setText(veh.getFuel());

            }
        });
    }
    
    @FXML
    public void clearAddFields() {
        modelFieldAdd.clear();
        regNumberFieldAdd.clear();
        manufacturerFieldAdd.clear();
        engineSizeFieldAdd.clear();
        fuelTypeFieldAdd.clear();
        colourFieldAdd.clear();
        MoTFieldAdd.setValue(null);
        mileageFieldAdd.clear();
        warantyCompanyField.clear();
        warantyAddressField.clear();
        warantyExpDateField.setValue(null);

        customerDropDownList.getSelectionModel().clearSelection();
        vehicleTypeDropDownList.getSelectionModel().clearSelection();
        vehicleTemplateDropDownList.getSelectionModel().clearSelection();
        
        
    }
    
    /**
     *
     * @param par
     */
    public void setParentController(VehiclesScreenController par){
        this.parent=par;
    }
    @FXML 
    public void handleExit(){
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    
    
}
