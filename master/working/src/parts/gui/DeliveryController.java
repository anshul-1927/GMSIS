/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts.gui;

import common.Database;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mario
 */
public class DeliveryController implements Initializable {

    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    
    @FXML private TextField quantityTf;
    @FXML private TextField idTf;
    
    
    private StockScreenController parentController;
    private int partID;
    private Database db;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = Database.getInstance();
        
        Platform.runLater(new Runnable() {
            @Override public void run() {
                initializeTextFields();
            }
        });
        
    }    
    
    @FXML
    private void handleConfirmButton(ActionEvent event) throws SQLException{     
        String format = "^\\d*[1-9]\\d*$";
        
        if(!quantityTf.getText().matches(format)){
            warningDialog();
        }else{
        
            int quantity = Integer.parseInt(quantityTf.getText());
            int id = Integer.parseInt(idTf.getText());
            
            String sql = "UPDATE Part SET PartQuantity = PartQuantity + '" + quantity + "' WHERE PartID = '" + partID + "';";

            db.connect();
            db.update(sql);
            db.closeConnection();
            
            updateStock(quantity);
            
            parentController.displayRecords();
            parentController.displayDeliveriesAndWithdrawals();
            
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        }
    }
    
    @FXML
    private void handleCancelButton(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    public void setParentController(StockScreenController parentController)
    {
        this.parentController = parentController;
    }
    
    public void selectedPart(int partID){
        this.partID = partID;
    }
    
    private void updateStock(int quantity) throws SQLException {
        LocalDate date = now();
        String sql = "INSERT INTO StockDeliveries VALUES ('"+ partID +"','"+ date +"','"+ quantity +"');";     
        db.connect();
        db.update(sql);
        db.closeConnection();
    }
    
    private void warningDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText("This field could only have positive whole numbers.");
        alert.showAndWait();
       
    }
    
    private void initializeTextFields() {
        idTf.setText(Integer.toString(partID));
        idTf.setDisable(true);
    }
    
}
