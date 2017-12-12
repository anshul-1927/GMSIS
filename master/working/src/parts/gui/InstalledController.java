/**
 * FXML Controller class
 *
 * @author Mario Tawfelis
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


public class InstalledController implements Initializable {

    @FXML private TableView<parts.Parts> installedTable;
    
    @FXML private TableColumn<parts.Parts, Integer> idCol;
    @FXML private TableColumn<parts.Parts, String> nameCol;
    @FXML private TableColumn<parts.Parts, String> descriptionCol;
    @FXML private TableColumn<parts.Parts, Double> costCol;
    
    @FXML private Label custID;
    @FXML private Label custName;
    @FXML private Label vehicleID;
    @FXML private Label bookingID;
    @FXML private Label bookingType;
    @FXML private Label bookingDate;
    @FXML private Label installDate;
    @FXML private Label warrantyDate;
    @FXML private Label errors;
    
    @FXML private Button addButton; 
    @FXML private Button editButton;   
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;
    @FXML private Button moreInfoBttn;
    @FXML private Button closeBttn;
    
    private ObservableList<parts.Parts> data;
    private PartsScreenController parentController;
    private Database db;
     
    //** Values will be passed to from parent controller **//
    private String vID;
    private int bID;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = Database.getInstance();
        
        idCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, Integer>("partID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, String>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, String>("description"));
        costCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, Double>("cost"));
    
        TFVisibility(false);    
    }  
    
    @FXML
    private void handleAddButtonAction(ActionEvent event) throws IOException, SQLException {       
        String vrn = this.vID;
        int bid = this.bID;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddRecord.fxml"));     
        Parent root = (Parent) fxmlLoader.load(); 
        
        AddRecordController controllerA = fxmlLoader.<AddRecordController>getController();
        controllerA.setParentController(this);
        controllerA.selectedRecord(vrn, bid, bookingDate.getText());
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Add Record");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(PartsScreenController.installedStage);
        stage.show();
    }
    
    @FXML
    private void handleEditButtonAction(ActionEvent event) throws IOException {
        /*
        Storing the data of the selected row on the table in an object
        This is done, to allow passing these data to the editing controller
        */
        Parts p = installedTable.getSelectionModel().getSelectedItem();
        
        if(p == null){
            warningDialog("Please select a record to edit.");
        }else{       
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditRecord.fxml"));     
            Parent root = (Parent) fxmlLoader.load(); 
            
            EditRecordController controllerE = fxmlLoader.<EditRecordController>getController();
            controllerE.setParentController(this);
            controllerE.editPart(p.getPartName(), p.getPartID(), vID, bID, bookingDate.getText());     //Passing the table row that is to be edited 
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Edit Record");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(PartsScreenController.installedStage);
            stage.show();
        }
    }
    
    @FXML
    private void handleDeleteButtonAction(ActionEvent event) throws SQLException {
        Parts p = installedTable.getSelectionModel().getSelectedItem();
        
        if(p == null){
            warningDialog("Please select a record to delete.");
        }else{       
            getDates();

            if(confirmationDialog()){   //Returns true if the user clicked OK to the alert
                try {
                    db.connect();
                    db.update("DELETE FROM Parts_Installed WHERE VehicleRegNo = '" + vID + "' AND BookingID = '" + bID + "' AND PartID = '" + p.getPartID() + "' AND PartInstalledDate = '" + installDate.getText() + "';");
                    db.closeConnection();
                } catch (NullPointerException ex) {
                    Logger.getLogger(PartsScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            updateBill(p.getPartID(), bID);

            populateTable();
        }
    }
    
    @FXML 
    private void handleRefreshButtonAction(ActionEvent event) throws SQLException{
        populateTable();
        TFVisibility(false);
    }
    
    
    @FXML
    private void handleCancelButton(ActionEvent event){
        Stage stage = (Stage) closeBttn.getScene().getWindow();
        stage.close();
    }
    
    
    @FXML
    private void handleMoreInfo(ActionEvent event) throws IOException, SQLException{       
        getDates();
        TFVisibility(true);         
    }

    
    public void populateTable() throws SQLException{     
        data = FXCollections.observableArrayList();
        db.connect();
        ResultSet rs = db.query("SELECT p.PartID, p.PartName, p.PartDescription, p.PartCost FROM Part p JOIN Parts_Installed i ON i.PartID=p.PartID WHERE VehicleRegNo = '" + vID + "' AND BookingID = '" + bID + "';");
        while(rs.next()){
            data.add(new Parts(rs.getInt(1), rs.getString(2),
                               rs.getString(3), rs.getDouble(4)));
            
        }
        installedTable.setItems(data);
        db.closeConnection();              
    }
    
    
    public void populateLabels(String customerID, String customerName, String vehicleRegNo, int bID, String bType, String bDate){
        custID.setText(customerID);
        custName.setText(customerName);
        vehicleID.setText(vehicleRegNo);
        bookingID.setText(Integer.toString(bID));
        bookingType.setText(bType);
        bookingDate.setText(bDate);
    }
    
    
    public void setParentController(PartsScreenController parentController)
    {
        this.parentController = parentController;
    }
    
    
    public void selectedRecord(String vID, int bID) throws SQLException{
        this.vID = vID;
        this.bID = bID;
        
        populateTable();
    }
     
    
    private void TFVisibility(boolean visible) {        
        installDate.setVisible(visible);
        warrantyDate.setVisible(visible);
    }
    
    
 
    
    
    /*
    This confirmation dialog is prompted when deleting a record
    */
    private boolean confirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Selected record will be permenantely deleted.");
        Optional<ButtonType> btn = alert.showAndWait();
        
        return btn.get() == ButtonType.OK;
    }

    private void updateBill(int partID, int bookingID) throws SQLException {
        String sql = "SELECT PartCost FROM Part WHERE PartID = '" + partID + "';";        
        db.connect();             
        ResultSet rsP = db.query(sql);     
        double cost = rsP.getDouble("PartCost");
        db.closeConnection();
      /*  
        sql = "SELECT BillID FROM Booking WHERE BookingID = '" + bookingID + "';";
        db.connect();
        ResultSet rsB = db.query(sql);
        int billID = rsB.getInt("BillID");
        db.closeConnection();
       */ 
        sql = "UPDATE Bill SET PartsCost = PartsCost -'" + cost + "' WHERE BookingID = '" + bookingID + "';";
        db.connect();
        db.update(sql);
        db.closeConnection();
        
    }

    private void getDates() throws SQLException {
        Parts p;
        
        p = installedTable.getSelectionModel().getSelectedItem();
        
        if(p == null){
            warningDialog("Please select a record first.");
        }else{
            db.connect();
            ResultSet rs = db.query("SELECT i.PartInstalledDate, i.WarantyExpDate FROM Part p JOIN Parts_Installed i ON i.PartID=p.PartID WHERE VehicleRegNo = '" + vID + "' AND BookingID = '" + bID + "' AND i.PartID = '" + p.getPartID() + "';");
            installDate.setText(rs.getString(1));
            rs.next();
            warrantyDate.setText(rs.getString(2));
            db.closeConnection();
        }
    }
    

    private void warningDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Missing Action");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
       
    }
}
