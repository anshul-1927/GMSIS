/**
 * FXML Controller class
 *
 * @author Mario
 */


package parts.gui;

import common.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import parts.Parts;
import parts.StockView;



public class StockScreenController implements Initializable {

              /*Parts Table*/
    @FXML private TableView<parts.Parts> partsTable;
    @FXML private TableColumn<parts.Parts, Integer> idCol;
    @FXML private TableColumn<parts.Parts, String> nameCol;
    @FXML private TableColumn<parts.Parts, String> descriptionCol;
    @FXML private TableColumn<parts.Parts, Integer> quantityCol;
    @FXML private TableColumn<parts.Parts, Double> costCol;
    
              /*Deliveries Table*/
    @FXML private TableView<parts.StockView> deliveryTable;
    @FXML private TableColumn<parts.StockView, String> dDate;
    @FXML private TableColumn<parts.StockView, Integer> dQuantity;
    @FXML private TableColumn<parts.StockView, Integer> dID;   
    
              /*Withdrawals Table*/
    @FXML private TableView withdrawalTable;
    @FXML private TableColumn<parts.StockView, String> wDate;
    @FXML private TableColumn<parts.StockView, Integer> wQuantity;
    @FXML private TableColumn<parts.StockView, Integer> wID;  
    
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;
    @FXML private Button receiveButton;
    
    @FXML private Label errors;
    
    
    private PartsScreenController parentController;
    private ObservableList<parts.Parts> data;
    private Database db;
 
    @Override
    public void initialize(URL url, ResourceBundle rb){
        db = Database.getInstance();

        idCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, Integer>("partID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, String>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, String>("description"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, Integer>("quantity"));
        costCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, Double>("cost"));
        
        dID.setCellValueFactory(new PropertyValueFactory<parts.StockView, Integer>("partID"));
        dDate.setCellValueFactory(new PropertyValueFactory<parts.StockView, String>("deliveryDate"));
        dQuantity.setCellValueFactory(new PropertyValueFactory<parts.StockView, Integer>("deliveryQuantity"));
        
        
        wID.setCellValueFactory(new PropertyValueFactory<parts.StockView, Integer>("partID"));
        wDate.setCellValueFactory(new PropertyValueFactory<parts.StockView, String>("withdrawDate"));
        wQuantity.setCellValueFactory(new PropertyValueFactory<parts.StockView, Integer>("withdrawQuantity"));
               
        displayRecords();
        displayDeliveriesAndWithdrawals();
    }    
    
    
    @FXML
    public void handleAddButton() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddStock.fxml"));     
        Parent root = (Parent) fxmlLoader.load(); 
        
        AddStockController controllerA = fxmlLoader.<AddStockController>getController();
        controllerA.setMainController(this);
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Add Stock Item");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(PartsScreenController.stockStage);
        stage.show();
    }
    
    
    @FXML
    public void handleEditButton() throws IOException
    {
         /*
        Storing the data of the selected row on the table in an object
        This is done, to allow passing these data to the editing controller
        */
        Parts part = partsTable.getSelectionModel().getSelectedItem();
        
        if(part == null){
            warningDialog("Please select an item to edit first.");
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditStock.fxml"));     
            Parent root = (Parent) fxmlLoader.load();
            
            EditStockController controllerE = fxmlLoader.<EditStockController>getController();
            controllerE.setMainController(this);
            controllerE.editPart(part);
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Edit Stock Item");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(PartsScreenController.stockStage);
            stage.show();
        }

    }
    
    
    @FXML
    public void handleDeleteButton() throws SQLException{
        Parts part = partsTable.getSelectionModel().getSelectedItem();
        
        if(part == null){
            warningDialog("Please select an item to delete first.");
        }else{
            if(confirmationDialog("delete")){   //Returns true, if user cliked YES/OK to the confirmation dialog
                try {
                    db.connect();
                    db.update("DELETE FROM Part WHERE PartID=\'" + part.getPartID() + "\';");
                    db.closeConnection();
                } catch (NullPointerException ex) {
                    Logger.getLogger(PartsScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }              
            }
            displayRecords();   
        }
    }
    
    
    @FXML
    private void handleReceiveButton(ActionEvent event) throws IOException{
        
        Parts part = partsTable.getSelectionModel().getSelectedItem();
        
        if(part == null){
            warningDialog("Please select the deliverd item.");
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Delivery.fxml"));     
            Parent root = (Parent) fxmlLoader.load();
            
            DeliveryController controllerD = fxmlLoader.<DeliveryController>getController();
            controllerD.setParentController(this);
            controllerD.selectedPart(part.getPartID());
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Received Delivery");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(PartsScreenController.stockStage);
            stage.show();
        }
    }
     
    
    @FXML 
    private void handleRefreshButtonAction(ActionEvent event){    
        displayRecords();
        displayDeliveriesAndWithdrawals();
    } 
    
    
    public void displayRecords(){        
        data = FXCollections.observableArrayList();
        
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM Part");
            while(rs.next()){
                data.add(new Parts(rs.getInt(1),rs.getString(2),
                                   rs.getString(3),rs.getInt(4),
                                   rs.getDouble(5)));
                partsTable.setItems(data);
            }
            db.closeConnection();
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }       
    }
    
    
    public void displayDeliveriesAndWithdrawals(){
        ObservableList<parts.StockView> listw = FXCollections.observableArrayList();
        ObservableList<parts.StockView> listd = FXCollections.observableArrayList();
        
        try {
            db.connect();
            ResultSet rsW = db.query("SELECT * FROM StockWithdrawals");
            while(rsW.next()){
                listw.add(new StockView(rsW.getInt("PartID"),rsW.getString("Date"),rsW.getInt("Quantity"), false));
                withdrawalTable.setItems(listw);
            }
            db.closeConnection();
                        
            db.connect();
            ResultSet rsD = db.query("SELECT * FROM StockDeliveries;");
            while(rsD.next()){
                listd.add(new StockView(rsD.getInt("PartID"),rsD.getString("Date"),rsD.getInt("Quantity"), true));
                deliveryTable.setItems(listd);
            }
            db.closeConnection();
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }
    
    
    public void setParentController(PartsScreenController parentController){
        this.parentController = parentController;
    }
    
    
    /*
    This confirmation dialog is prompted when modifying (editing/deleting) a record
    */
    private boolean confirmationDialog(String action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to " + action + " this record?");
        Optional<ButtonType> btn = alert.showAndWait();
        
        return btn.get() == ButtonType.OK;
    }
    
    
    private void warningDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing Action");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
       
    }    
}
