/**
 * FXML Controller class
 *
 * @author Mario Tawfelis
 */


package parts.gui;

import common.Database;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import parts.Parts;


public class EditStockController implements Initializable {
    
    @FXML private TextField partID;   
    @FXML private TextField partName;       
    @FXML private TextField partQuantity;    
    @FXML private TextField partCost;
    
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
        
    @FXML private TextArea partDescription;   

    private StockScreenController mainController;
    private Parts part;
    private Database db;
       
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = Database.getInstance();
        
        editPart(part);
        Platform.runLater(() -> {
            initializeTextFields();
        });   
    }    

    @FXML
    private void handleConfirmButtonAction(ActionEvent event) throws SQLException  {
        String name = partName.getText();
        String quantity  = partQuantity.getText();        
        String description = partDescription.getText();
        String cost = partCost.getText();
        
        
            /* Regex Formats for validation purposes*/       
        String nameFormat ="^[\\p{L} .'-]+$";
        String quantityFormat = "^\\d*[1-9]\\d*$";
        String descriptionFormat = "([a-zA-Z]+(\\.|\\. |'(s |re |t |m |ll )|s' | )?)+";
        String costFormat = "^\\d*[1-9]\\d*(.[0-9]+?)?";
        
        boolean valid = true;         
        
        
        //Validate that all fields were filled
        if("".equals(name) || "".equals(quantity) || "".equals(description) || "".equals(cost)){
            warningDialog("All fields are required!");
            valid = false;
        }
        
        //Validate that fields were filled appropriately
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
            String sql = "UPDATE Part SET PartName ='" + name + "', PartDescription ='" + description + "', PartQuantity ='" + Integer.parseInt(quantity) + "', PartCost ='" + Double.parseDouble(cost) + "' WHERE PartID ='" + part.getPartID() + "';";
            
            try {
                db.connect();
                db.update(sql);
                db.closeConnection();
            } catch (NullPointerException ex) {
                Logger.getLogger(EditStockController.class.getName()).log(Level.SEVERE, null, ex);
            }

            mainController.displayRecords();

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();    
        }
    }
    
    
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    
    public void setMainController(StockScreenController mainController){
        this.mainController = mainController;
    }
    
    
    public void editPart(Parts part){
        this.part = part;
    }

    
    private void initializeTextFields() {
        partID.setText(part.getPartID() + "");
        partID.setDisable(true);
        partName.setText(part.getPartName());
        partQuantity.setText(part.getQuantity() + "");
        partDescription.setText(part.getDescription());
        partCost.setText(part.getCost() + "");
    }
    
    
    private boolean confirmationDialog(String action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText(action);
        Optional<ButtonType> btn = alert.showAndWait();
        
        return btn.get() == ButtonType.OK;
    }
    

    private void warningDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
       
    }

    
}
