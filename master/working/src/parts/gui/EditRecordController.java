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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Mario_
 */

public class EditRecordController implements Initializable {

    @FXML private ComboBox partBox;
    
    @FXML private DatePicker installDateField;
    
    @FXML private Label errors;
    
    @FXML private Button editRecordButton;
    @FXML private Button cancelButton;
    
    private InstalledController parentController;
    private String partName;
    private int partID;
    private String vehicleID;
    private String bookingDate;
    private int bookingID;
    private Database db;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            db = Database.getInstance();
                        
            ObservableList<String> partsList = FXCollections.observableArrayList();
            
            db.connect();
            ResultSet rsP = db.query("SELECT PartID, PartName FROM Part WHERE PartQuantity > 0 ORDER BY PartID;");
            while(rsP.next()){
                partsList.add(rsP.getString("PartID") + ": " + rsP.getString("PartName"));
            }
            db.closeConnection();
            
            partBox.setItems(partsList);
 
        } catch (SQLException ex) {
            Logger.getLogger(EditRecordController.class.getName()).log(Level.SEVERE, null, ex);
        }     
        
        Platform.runLater(new Runnable() {
            @Override public void run() {
                initializeTextFields();
            }
        });
    }    

    @FXML
    private void handleConfirmButton(ActionEvent event) throws SQLException {
        String partRecord = (String) partBox.getSelectionModel().getSelectedItem();
        String[] partDetails = partRecord.split("[\\s,:]+");
        
        //String installDate = (String) installDateField.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        //LocalDate ld = installDateField.getValue().plusYears(1);
        //String warrantyDate = ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        if(partDetails[0] == null){
            warningDialog("All fields are required!");
        }else{              
            String sql = "UPDATE Parts_Installed SET PartID = '"+ Integer.parseInt(partDetails[0]) +"' WHERE BookingID = '"+ bookingID +"' AND VehicleRegNo = '"+  vehicleID +"' AND PartID = '"+ partID +"';";
            db.connect();
            db.update(sql);
            db.closeConnection();

            parentController.populateTable();

            Stage stage = (Stage) editRecordButton.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    public void setParentController(InstalledController parentController)
    {
        this.parentController = parentController;
    }
    
    public void editPart(String pName, int pID, String vID, int bID, String bDate){
        this.partID = pID;
        this.partName = pName;
        this.vehicleID = vID;
        this.bookingID = bID;
        this.bookingDate = bDate;
    }
    
     private void handleErrorMessages(String message, boolean visible){
        errors.setText(message);
        errors.setVisible(visible);
    }
     
     private void warningDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing Fields");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
       
    }
     
    
    private void initializeTextFields() {
        partBox.setValue(partID + " : " + partName);
    }

}
