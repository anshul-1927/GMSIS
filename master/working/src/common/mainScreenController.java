/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import diagrep.gui.DiagrepScreenController;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import parts.gui.PartsScreenController;
import specialist.gui.SpecialistScreenController;
import vehicles.gui.VehiclesScreenController;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */


public class mainScreenController implements Initializable {
   @FXML
   private Tab adminTab;

   private SystemUser user;
   @FXML
   private TabPane pane;
   
   private Parent root;
   
   private Map<String, Object> moduleControllers = new HashMap<String, Object>();
   
   @FXML 
   private javafx.scene.control.Button closeButton;
   
    @Override
   public void initialize(URL url, ResourceBundle rb){
        pane.getSelectionModel().clearSelection();
        pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            
        @Override
        public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
            FXMLLoader fXMLLoader = new FXMLLoader();
            if (newValue.getContent() == null) {
                try {
                    // Loading content on demand
                    fXMLLoader.setLocation(this.getClass().getResource(newValue.getId() + ".fxml"));
                    //root = (Parent) fXMLLoader.load(this.getClass().getResource(newValue.getId() + ".fxml").openStream());
                    root = (Parent) fXMLLoader.load();
                    newValue.setContent(root);

                    // Store the controller to be used later for calling refresh methods for each module selected on demand.
                    moduleControllers.put(newValue.getId(), fXMLLoader.getController());

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                // Content is already loaded. Update/refresh methods of each module will be called.
                root = (Parent) newValue.getContent();
                //get the controller from Map and manipulate and refresh table views of the selected module
                // via its controller.
                Object module=moduleControllers.get(newValue.getId());
                
                if(module instanceof VehiclesScreenController){
                    VehiclesScreenController veh=(VehiclesScreenController)module;
                    veh.allVehicles();
                }else if(module instanceof DiagrepScreenController){
                    DiagrepScreenController diagrep=(DiagrepScreenController)module;
                    diagrep.displayTableData(null);
                }else if(module instanceof PartsScreenController){
                    PartsScreenController parts=(PartsScreenController)module;
                    parts.displayRecords(null);
                }else if(module instanceof SpecialistScreenController){
                    SpecialistScreenController spc=(SpecialistScreenController)module;
                    spc.fillTable();
                }else{
                    
                }
                
            }
        }
    });
// By default, select 1st tab and load its content.
   pane.getSelectionModel().selectFirst();

       
   }
   public void reinit(){
       if(user.getIsAdmin().equals("Admin")){
           
       }else{
                adminTab.setDisable(true);
       }
       
   }

    public void setUser(SystemUser user){
        this.user=user;
    }
    @FXML
    public void logout() throws IOException{
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginScreen.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stag = new Stage();
            stag.setScene(scene);
            stag.centerOnScreen();
            stag.setTitle("Login Menu");
            stag.show(); 
         
    }
    
    

    
}
