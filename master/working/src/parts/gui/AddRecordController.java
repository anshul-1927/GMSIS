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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import parts.Parts;

/**
 * FXML Controller class
 *
 * @author Mario_
 */


public class AddRecordController implements Initializable {
    
    @FXML private TableView<parts.Parts> table;
    
    @FXML private TableColumn<parts.Parts, Integer> idCol;
    @FXML private TableColumn<parts.Parts, String> nameCol;
    @FXML private TableColumn<parts.Parts, String> descriptionCol;
    @FXML private TableColumn<parts.Parts, Double> costCol;
    
    @FXML private Button addRecordButton;   
    @FXML private Button cancelButton;
    @FXML private Button searchButton; 
    
    @FXML private TextField search;
    
    @FXML private Label errors;
    
    @FXML private DatePicker installDateField;

    private InstalledController parentController;
    private String vID;
    private int bID;
    private String bookingDate;
    private Database db;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
             
        db = Database.getInstance();

        idCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, Integer>("partID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, String>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, String>("description"));
        costCol.setCellValueFactory(new PropertyValueFactory<parts.Parts, Double>("cost"));
        
     
        

        displayRecords(null);                          
    }
    
    public void displayRecords(String sql){
        if(sql==null)    
            sql = "SELECT PartID, PartName, PartDescription, PartCost FROM Part WHERE PartQuantity > 0"; 
        
        try {
            ObservableList<parts.Parts> partsList = FXCollections.observableArrayList();
            
            db.connect();
            ResultSet rs = db.query(sql);
            while(rs.next()){
                partsList.add(new Parts(rs.getInt(1),rs.getString(2),
                                   rs.getString(3),rs.getInt(4)));

                table.setItems(partsList);
            }
            db.closeConnection();
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }       
    }
        
    @FXML
    private void addRecord(ActionEvent event) throws SQLException{               
        Parts part = table.getSelectionModel().getSelectedItem();
                
        if(part == null){
            warningDialog("Please select a part first");
        }else{            
            int id = part.getPartID();   
            
            LocalDate installationDate = getDate();    
            String installationDateS = installationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                         
            LocalDate warrantyDate = installationDate.plusYears(1);
            String warrantyDateS = warrantyDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            try {    
                String sql = "INSERT INTO Parts_Installed VALUES('" + id + "','" + vID + "','" + installationDateS + "','" + bID + "','" + warrantyDateS + "');";
                db.connect();
                db.update(sql);
                db.closeConnection();

                Stage stage = (Stage) addRecordButton.getScene().getWindow();
                stage.close();

                updateBill(id, bID);
                updateStock(id);
                
            } catch (NullPointerException | SQLException ex) {
                Logger.getLogger(AddRecordController.class.getName()).log(Level.SEVERE, null, ex);
            }

            parentController.populateTable();
        }
    }
    
    @FXML
    private void handleSearch(ActionEvent event){
        String searchFor = search.getText();   
                
        String sql = "SELECT PartID, PartName, PartDescription, PartCost FROM Part WHERE PartQuantity > 0 AND PartName LIKE '%"+searchFor+"%';";
               
        displayRecords(sql);                
    }   

    @FXML
    private void handleCancelButton(ActionEvent event){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    } 
    
    public void setParentController(InstalledController parentController)
    {
        this.parentController = parentController;
    }
   
    private void updateStock(int partID) throws SQLException{            
        try {
                String sql = "UPDATE Part SET PartQuantity = PartQuantity - 1 WHERE PartID='" + partID + "';";
                db.connect();
                db.update(sql);
                db.closeConnection();
        
                sql = "INSERT INTO StockWithdrawals VALUES ('"+ partID +"','"+ bookingDate +"', 1);";     
                db.connect();
                db.update(sql);
                db.closeConnection();
        } catch (NullPointerException ex) {
                Logger.getLogger(EditStockController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    private void updateBill(int partID, int bookingID) throws SQLException {
        String sql = "SELECT PartCost FROM Part WHERE PartID = '" + partID + "';";        
        db.connect();             
        ResultSet rsP = db.query(sql);     
        double cost = rsP.getDouble("PartCost");
        db.closeConnection();

        sql = "UPDATE Bill SET PartsCost = PartsCost +'" + cost + "' WHERE BookingID = '" + bID + "';";
        db.connect();
        db.update(sql);
        db.closeConnection();      
    }
      
    public void selectedRecord(String vID, int bID, String bookingDate) throws SQLException{
        this.vID = vID;
        this.bID = bID;
        this.bookingDate = bookingDate;
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
    /*
    private void restrictInstallationDate(){
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MMM-dd");
        final LocalDate dt = dtf.parseLocalDate(yourinput);
        final Callback<DatePicker, DateCell> dayCellFactory = 
            (final DatePicker datePicker) -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item.isBefore(installDateField.getValue().plusDays(1))) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;");
                    }
                }
            };
        installDateField.setDayCellFactory(dayCellFactory);

    }
    */

    private LocalDate getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try
        {
            Date input = simpleDateFormat.parse(bookingDate);
            
            String output = simpleDateFormat.format(input).substring(0, 10);
            
            LocalDate localDate = LocalDate.parse(output);
                    
            return localDate;
        }
        catch (ParseException ex)
        {
            System.out.println("Exception "+ex);
        }
        
        return null;
    }
}
