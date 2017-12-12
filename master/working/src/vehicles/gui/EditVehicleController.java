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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import vehicles.NonWarranty;
import vehicles.UnderWarranty;
import vehicles.Vehicle;
import vehicles.VehicleWrapper;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class EditVehicleController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button exit;
        
    @FXML
    private RadioButton wWarranty;
    @FXML
    private RadioButton uWarranty;
    private ToggleGroup warrantyGroup;
    
    @FXML
    private TextField modelFieldEdit;
    @FXML
    private TextField manufacturerFieldEdit;
    @FXML
    private TextField engineSizeFieldEdit;
    @FXML
    private TextField fuelTypeFieldEdit;
    @FXML
    private TextField colourFieldEdit;
    @FXML
    private TextField MoTFieldEdit;
    @FXML
    private TextField mileageFieldEdit;
    @FXML
    private ComboBox<vehicles.Vehicle> vehicleTemplateDropDownList;
    @FXML
    private ComboBox<String> vehicleTypeDropDownList;
    @FXML
    private TextField warantyCompanyField;
    @FXML
    private TextField warantyAddressField;
    @FXML
    private TextField warantyExpDateField;
    
    private Database db;
    
    private VehicleWrapper wrapper;
    
    private Vehicle veh;
     
    private ObservableList<vehicles.Vehicle> vehicleTemplate;
    
    
    private VehiclesScreenController parent;
    
    private String regNo;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void reinit(){
        db=db.getInstance();
        this.setWarrantyToggle();
        this.setVariables();
        this.setVehicleDropDown();
        this.setVehicleTextFields();
        this.setVehicleTextFields();
    }
    private void setVariables(){
        veh=wrapper.getVehicle();
    }
    private void setVehicleTextFields(){
        modelFieldEdit.setText(veh.getModel());
        manufacturerFieldEdit.setText(veh.getMake());
        engineSizeFieldEdit.setText(veh.getEngSize());
        fuelTypeFieldEdit.setText(veh.getFuel());
        colourFieldEdit.setText(veh.getCol());
        MoTFieldEdit.setText(veh.getMoT());
        mileageFieldEdit.setText(veh.getMileage());
        if(veh.getWarrantyRole() instanceof NonWarranty){
            warrantyGroup.selectToggle(null);
            warrantyGroup.selectToggle(wWarranty);
        }else{
            UnderWarranty underWarrantyRole = (UnderWarranty) veh.getWarrantyRole();
            warantyCompanyField.setText(underWarrantyRole.getCompany());
            warantyAddressField.setText(underWarrantyRole.getAddress());
            warantyExpDateField.setText(underWarrantyRole.getExpDate());
        }
    }
    private void setVehicleDropDown(){
        vehicleTypeDropDownList.setItems(FXCollections.observableArrayList("Car","Van","Truck"));
        vehicleTypeDropDownList.getSelectionModel().select(veh.getVehicleType());
        regNo=veh.getRegNum();
        
        String getTemplateQuery="SELECT * FROM vehicle_template;";
        
        
        try{
                db.connect();
                ResultSet TemplateRs = db.query(getTemplateQuery);


                vehicleTemplate=FXCollections.observableArrayList();
                while(TemplateRs.next()){
                    Vehicle vh=new Vehicle(TemplateRs.getString("Type"),"",TemplateRs.getString("template_make"), TemplateRs.getString("template_model"), TemplateRs.getString("template_engine_size"), TemplateRs.getString("template_fuel_type"), "", "","","", null);
                    vehicleTemplate.add(vh);
                }
                db.closeConnection();
        }catch (SQLException ex) {
                ex.printStackTrace();
        }

        vehicleTemplateDropDownList.setItems(vehicleTemplate);
        vehicleTemplateDropDownList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vehicle>() {
        
            @Override
            public void changed(ObservableValue<? extends Vehicle> observable, Vehicle oldValue, Vehicle veh) {
                                         
                     vehicleTypeDropDownList.setValue(veh.getVehicleType());
                     manufacturerFieldEdit.setText(veh.getMake());
                     modelFieldEdit.setText(veh.getModel());
                     engineSizeFieldEdit.setText(veh.getEngSize());
                     fuelTypeFieldEdit.setText(veh.getFuel());
                        
        }});
    }
    private void setWarrantyToggle(){
          warrantyGroup=new ToggleGroup();
          uWarranty.setToggleGroup(warrantyGroup);
          wWarranty.setToggleGroup(warrantyGroup);
          warrantyGroup.selectToggle(uWarranty);
          warrantyGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                    if (new_toggle == wWarranty){
                        warantyCompanyField.setText("Not Applicable");
                        warantyAddressField.setText("Not Applicable");
                        warantyExpDateField.setText("Not Applicable");
                        warantyCompanyField.setEditable(false);
                        warantyAddressField.setEditable(false);
                        warantyExpDateField.setEditable(false);
                    }else{
                        clearWarrantyFields();
                        warantyCompanyField.setEditable(true);
                        warantyAddressField.setEditable(true);
                        warantyExpDateField.setEditable(true);
                    }
                    
                       
            }
        });
    }
    private void clearWarrantyFields(){
        warantyCompanyField.clear();
        warantyAddressField.clear();
        warantyExpDateField.clear();
    }
    
    @FXML
    public void handleUpdate(){
        
       
        if(vehicleTypeDropDownList.getSelectionModel().isEmpty() || vehicleTypeDropDownList.getValue() == null ){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a vehicle type!");
            alert.showAndWait();
            return ;
        }
        
        if(modelFieldEdit.getText()==null || modelFieldEdit.getText().trim().isEmpty()){
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in vehicle model details!");
            alert.showAndWait();
            return ;
        }
        
        if(manufacturerFieldEdit.getText()==null || manufacturerFieldEdit.getText().trim().isEmpty()){
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in vehicle manufacturer details!");
            alert.showAndWait();
            return ;
        }
        if(fuelTypeFieldEdit.getText()==null || fuelTypeFieldEdit.getText().trim().isEmpty()){
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in vehicle fuel type details!");
            alert.showAndWait();
            return ;
        }
        if(colourFieldEdit.getText()==null || colourFieldEdit.getText().trim().isEmpty()){
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in vehicle color details!");
            alert.showAndWait();
            return ;
        }
        if(MoTFieldEdit.getText()==null || MoTFieldEdit.getText().trim().isEmpty()){
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in vehicle MoT date!");
            alert.showAndWait();
            return ;
        }
        if(mileageFieldEdit.getText()==null || mileageFieldEdit.getText().trim().isEmpty()){
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in vehicle mileage details!");
            alert.showAndWait();
            return ;
        }
        String type=vehicleTypeDropDownList.getValue();
        String mod=modelFieldEdit.getText();
        String manufacturer=manufacturerFieldEdit.getText();
        String engineSize=engineSizeFieldEdit.getText();
        String fuelType=fuelTypeFieldEdit.getText();
        String colour=colourFieldEdit.getText();
        String MoT=MoTFieldEdit.getText();
        String milage=mileageFieldEdit.getText();
        
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
        if(!engineSize.matches(regex5)){
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
        if(!MoT.matches(regexDate)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Vehicle MoT Date Format Error");
            alert.setContentText("Please do not enter illegal characters. The format should be YYYY/MM/dd");
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
        
        if(warrantyGroup.getSelectedToggle()==wWarranty){ 
                    String editSql="UPDATE Vehicle SET  WarrantyName =NULL, WarrantyAddress =NULL, WarrantyExpDate =NULL, VehicleType ='"+type+"',  VehicleModel ='"+mod+"', VehicleMake ='"+manufacturer+"',VehicleEngSize='"+engineSize+"', VehicleMileage='"+milage+"', VehicleFuelType='"+fuelType+"', VehicleColour='"+colour+"', VehicleMoTDate='"+MoT+"' WHERE VehicleRegNo='"+regNo+"';";

                    db.connect();
                    try {
                        db.update(editSql);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    db.closeConnection();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Vehicle: "+veh.getRegNum());
                    alert.setContentText("Updated without Warranty!");
                    alert.showAndWait();
        }else{
                    if(warantyCompanyField.getText()==null || warantyCompanyField.getText().trim().isEmpty()){
            
                         Alert alert = new Alert(Alert.AlertType.INFORMATION);
                         alert.setTitle("Information");
                         alert.setHeaderText(null);
                         alert.setContentText("Fill in Warranty Company Details");
                         alert.showAndWait();
                         return ;
                    } 
                    if(warantyAddressField.getText()==null || warantyAddressField.getText().trim().isEmpty()){
            
                         Alert alert = new Alert(Alert.AlertType.INFORMATION);
                         alert.setTitle("Information");
                         alert.setHeaderText(null);
                         alert.setContentText("Please fill Warranty Address Details!");
                         alert.showAndWait();
                         return ;
                    } 
                    if(warantyExpDateField.getText()==null || warantyExpDateField.getText().trim().isEmpty()){
            
                         Alert alert = new Alert(Alert.AlertType.INFORMATION);
                         alert.setTitle("Information");
                         alert.setHeaderText(null);
                         alert.setContentText("Please fill in Warranty Expiry Date Details!");
                         alert.showAndWait();
                         return ;
                    } 
                    String warantyComp=warantyCompanyField.getText();
                    String warAdr=warantyAddressField.getText();
                    String expDate=warantyExpDateField.getText();
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
                     if(!expDate.matches(regexDate)){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error");
                        alert.setHeaderText("Warranty Expiry Date Format Error");
                        alert.setContentText("Please do not enter illegal characters. The format should be YYYY/MM/dd");
                        alert.showAndWait();
                        return;
                     }
                    
                    String editSql="UPDATE Vehicle SET VehicleType ='"+type+"', VehicleModel ='"+mod+"', VehicleMake ='"+manufacturer+"', VehicleMileage ='"+milage+"', VehicleEngSize='"+engineSize+"', VehicleFuelType='"+fuelType+"', VehicleColour='"+colour+"', VehicleMoTDate='"+MoT+"',WarrantyName='"+warantyComp+"',WarrantyAddress='"+warAdr+"',WarrantyExpDate='"+expDate+"'  WHERE VehicleRegNo='"+regNo+"';";
                    db.connect();
                    try {
                        db.update(editSql);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    db.closeConnection();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Vehicle: "+veh.getRegNum());
                    alert.setContentText("Updated with warranty!");
                    alert.showAndWait();
        }
            
        
        parent.outputTableData(null);
        
    }
    public void setWrapper(VehicleWrapper vh){
        this.wrapper=vh;
    }
    
    @FXML 
    public void handleExit(){
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void clearEditFields(){
        modelFieldEdit.clear();
        manufacturerFieldEdit.clear();
        engineSizeFieldEdit.clear();
        fuelTypeFieldEdit.clear();
        colourFieldEdit.clear();
        MoTFieldEdit.clear();
        mileageFieldEdit.clear();
        warantyCompanyField.clear();
        warantyAddressField.clear();
        warantyExpDateField.clear();
       
        vehicleTypeDropDownList.getSelectionModel().clearSelection();
        vehicleTemplateDropDownList.getSelectionModel().clearSelection();
    }
    public void setParentController(VehiclesScreenController par){
        this.parent=par;
    }
    
}
