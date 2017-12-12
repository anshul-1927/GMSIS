/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts.gui;

import common.Database;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mario_
 */

public class AddStockController implements Initializable {

    @FXML private Button addRecordButton;   
    @FXML private Button cancelButton;
    
    @FXML private TextField partID;   
    @FXML private TextField partName;    
    @FXML private TextArea partDescription;   
    @FXML private TextField partQuantity;    
    @FXML private TextField partCost;

    @FXML private Label errors;
    
    private StockScreenController mainController;
    private Database db;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        db = Database.getInstance();
    }    

    @FXML
    private void handleAddRecordButtonAction(ActionEvent event) throws SQLException {   
        //String id = partID.getText();
        String name = partName.getText();
        String quantity  = partQuantity.getText();        
        String description = partDescription.getText();
        String cost = partCost.getText();
        
        //String idFormat = "^\\d+$";
        String nameFormat ="[A-Za-z']+";
        String quantityFormat = "^\\d*[1-9]\\d*$";
        String descriptionFormat = "([a-zA-Z]+(\\.|\\. |'(s |re |t |m |ll )|s' | )?)+";
        String costFormat = "[0-9]+";
        
        boolean valid = true;
        
        
        
        if("".equals(name) || "".equals(quantity) || "".equals(description) || "".equals(cost)){
            warningDialog("All fields are required!");
            valid = false;
        }
        
        if(valid){
            if(!name.matches(nameFormat)){
                warningDialog("Name field could only contain letters.");
                valid = false;
            }
            if(!quantity.matches(quantityFormat)){
                warningDialog("Quantiy could only be a positive integer!");
                valid = false;
            }
            if(!description.matches(descriptionFormat)){
                warningDialog("Description field could only contain letters.");
                valid = false;
            }
            if(!cost.matches(costFormat)){
                warningDialog("Cost field could only contain numeric values.");
                valid = false;
            }
        }
        
        if(valid){  
            String sqlStatement = "INSERT INTO Part (PartName,PartDescription,PartQuantity,PartCost) "
                    + "VALUES ('" + name + "','" + description + "','" + Integer.parseInt(quantity) + "','" + Double.parseDouble(cost) + "');";

            db.connect();
            db.update(sqlStatement);
            db.closeConnection();

            mainController.displayRecords();

            Stage stage = (Stage) addRecordButton.getScene().getWindow();
            stage.close();  
        }
    }
    
    
    @FXML
    private void handleCancelButton(ActionEvent event)
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setMainController(StockScreenController mainController)
    {
        this.mainController = mainController;
    }
    
    private void handleErrorMessages(String message, boolean visible){
        errors.setText(message);
        errors.setVisible(visible);
    }
    
    private void warningDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
       
    }
}
