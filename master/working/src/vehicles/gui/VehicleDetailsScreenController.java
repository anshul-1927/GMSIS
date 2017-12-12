/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import vehicles.Customer;
import vehicles.NonWarranty;
import vehicles.UnderWarranty;
import vehicles.Vehicle;
import vehicles.VehicleWarrantyRole;
import vehicles.VehicleWrapper;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class VehicleDetailsScreenController implements Initializable {

   
    @FXML Button exit;
    @FXML
    private Label custName;
    @FXML
    private Label custEmail;
    @FXML
    private Label custAddress;
    @FXML
    private Label custPost;
    @FXML
    private Label custMobile;
    @FXML
    private Label regNum; 
    @FXML
    private Label model;
    @FXML
    private Label make;
    @FXML
    private Label MoT;
    @FXML
    private Label lastS;
    @FXML
    private Label eng;
    @FXML
    private Label mileage;
    @FXML
    private Label color;
    @FXML
    private Label fuel;
    @FXML
    private Label type;
    @FXML
    private Label company;
    @FXML
    private Label address;
    @FXML
    private Label expDate;
    
    
    private VehicleWrapper wrapper;
    
    private Vehicle vehicle;
    
    private Customer customer;
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    public void reinit(){
        this.setVariables();
        this.setCusomer();
        this.setVehicleDetails();
        this.setWarranty();
    }
    private void setVariables(){
        vehicle=wrapper.getVehicle();
        customer=wrapper.getCustomer();
    }
    private void setCusomer(){
        this.custName.setText(customer.getFirstName()+" "+customer.getLastName());
        this.custAddress.setText(customer.getAddress());
        this.custPost.setText(customer.getPostcode());
        this.custEmail.setText(customer.getEmailAddress());
        this.custMobile.setText(customer.getPhoneNumber());
    }
    private void setVehicleDetails(){
        this.MoT.setText(vehicle.getMoT());
        this.color.setText(vehicle.getCol());
        this.eng.setText(vehicle.getEngSize());
        this.fuel.setText(vehicle.getFuel());
        this.make.setText(vehicle.getMake());
        this.regNum.setText(vehicle.getRegNum());
        this.type.setText(vehicle.getVehicleType());
        this.mileage.setText(vehicle.getMileage());
        if(vehicle.getLastServiceDate()==null){
            this.lastS.setText("Not Applicable");
        }else{
            this.lastS.setText(vehicle.getLastServiceDate());
        }
        this.model.setText(vehicle.getModel());
        
    }
    private void setWarranty(){
        if(vehicle.getWarrantyRole() instanceof NonWarranty){
            this.company.setText("Not Applicable");
            this.address.setText("Not Applicable");
            this.expDate.setText("Not Applicable");
        }else{
            
            VehicleWarrantyRole details=vehicle.getWarrantyRole();
            UnderWarranty underWarrantyRole = (UnderWarranty) details;
            
            this.company.setText(underWarrantyRole.getCompany());
            this.address.setText(underWarrantyRole.getAddress());
            this.expDate.setText(underWarrantyRole.getExpDate());
        }
    }
    
    public void setVehicle(VehicleWrapper wrap){
     this.wrapper=wrap;  
    }
    @FXML 
    public void handleExit(){
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
}
