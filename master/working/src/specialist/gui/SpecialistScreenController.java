/**
 * FXML Controller class
 *
 * @author Lloyd
 * //Way of passing variable to another controller found on stackoverflow link below
 * //http://stackoverflow.com/questions/14370183/passing-parameters-to-a-controller-when-loading-an-fxml
 * 
 */

package specialist.gui;

import common.Database;
import common.LoginScreenController;
import specialist.*;
import customers.Customer;
import customers.gui.ViewBillsScreenController;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Optional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SpecialistScreenController implements Initializable {
    
    @FXML private TextField tableFilter;
    @FXML private CheckBox isOutstanding, isReturned, isVehicle, isPart;
    @FXML private ComboBox<String> spcChoice;
    @FXML private TableView<Specialist> table;
    @FXML private TableColumn<specialist.Specialist, String> cFNameCol, cLNameCol, vRegCol, vModelCol, vMakeCol;
    @FXML private TableColumn<specialist.Specialist, String> spcNameCol,  dDateCol, rDateCol, partNameCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        spcChoice.setItems(getSPCs());
        isOutstanding.setSelected(true);
        cFNameCol.setCellValueFactory(new PropertyValueFactory<specialist.Specialist, String>("customerFName"));
        cLNameCol.setCellValueFactory(new PropertyValueFactory<specialist.Specialist, String>("customerLName"));
        vRegCol.setCellValueFactory(new PropertyValueFactory<specialist.Specialist, String>("vehicleRegNo"));
        vModelCol.setCellValueFactory(new PropertyValueFactory<specialist.Specialist, String>("vehicleModel"));
        vMakeCol.setCellValueFactory(new PropertyValueFactory<specialist.Specialist, String>("vehicleMake"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<specialist.Specialist, String>("partName"));
        dDateCol.setCellValueFactory(new PropertyValueFactory<specialist.Specialist, String>("deliveryDate"));
        rDateCol.setCellValueFactory(new PropertyValueFactory<specialist.Specialist, String>("returnDate"));
        spcNameCol.setCellValueFactory(new PropertyValueFactory<specialist.Specialist, String>("spcName"));
        fillTable();
    }

    public void refreshPage(){
        tableFilter.setText("");
        isOutstanding.setSelected(false);
        isReturned.setSelected(false);
        isVehicle.setSelected(false);
        isPart.setSelected(false);
        spcChoice.getSelectionModel().select(null);
        fillTable();
    }
    
    public void isVehicleSelected(){
        isPart.setSelected(false);
        fillTable();
    }
    
    public void isPartSelected(){
        isVehicle.setSelected(false);
        fillTable();
    }
    
    public void isReturnedSelected(){
        isOutstanding.setSelected(false);
        fillTable();
    }
    
    public void isOutstandingSelected(){
        isReturned.setSelected(false);
        fillTable();
    }
    
    public void errorAlert(String message){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void fillTable(){
        ObservableList<Specialist> specialistList = FXCollections.observableArrayList();
        String filter = tableFilter.getText();
        String sql = "SELECT SpecialistRepair.PartID, SpecialistRepair.SPCID, SpecialistRepair.BookingID, Booking.BookingDate, Customer.CustomerID, Customer.CustomerFirstName, Customer.CustomerLastName, Vehicle.VehicleRegNo, Vehicle.VehicleModel, Vehicle.VehicleMake, Part.PartName, SPC.SPCName, SpecialistRepair.SRReturnDate, SpecialistRepair.SRDeliveryDate, SpecialistRepair.SRID, SpecialistRepair.SRCost FROM SpecialistRepair "
                + "INNER JOIN Booking ON SpecialistRepair.BookingID=Booking.BookingID "
                + "INNER JOIN Vehicle ON Booking.VehicleRegNo=Vehicle.VehicleRegNo "
                + "INNER JOIN Customer ON Vehicle.CustomerID=Customer.CustomerID "
                + "LEFT JOIN SPC ON SpecialistRepair.SPCID=SPC.SPCID "
                + "LEFT JOIN Part ON SpecialistRepair.PartID=Part.PartID "
                + "WHERE (SpecialistRepair.PartID IS NULL OR SpecialistRepair.PartID=Part.PartID) "
                + "AND (Customer.CustomerFirstName || ' ' || Customer.CustomerLastName LIKE '%"+filter+"%' OR Booking.VehicleRegNo LIKE '%"+filter+"%')";
        
        if(!(spcChoice.getSelectionModel().getSelectedItem()==null || spcChoice.getSelectionModel().getSelectedItem().equals("All"))){
            sql += " AND SPC.SPCID = "+Integer.parseInt((spcChoice.getSelectionModel().getSelectedItem().split("\\|")[0]));
        }
        if(isVehicle.isSelected()){
            sql += " AND Part.PartID IS NULL";
        }
        if(isPart.isSelected()){
            sql += " AND Part.PartID IS NOT NULL";
        }
        
        try{
            Database.getInstance().connect();
            ResultSet rs = Database.getInstance().query(sql);
            while(rs.next()){
                LocalDate rDate = LocalDate.parse(rs.getString("SRReturnDate"));
                LocalDate dDate = LocalDate.parse(rs.getString("SRDeliveryDate"));
                double cost = Double.parseDouble(rs.getString("SRCost"));
                if(isOutstanding.isSelected()){
                    if(LocalDate.now().isBefore(rDate) || LocalDate.now().isEqual(rDate)){
                        specialistList.add(new Specialist(rs.getString("SRID"), rs.getString("PartID"), rs.getString("SPCID"), rs.getString("BookingID"), rs.getString("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"), rs.getString("VehicleRegNo"), rs.getString("VehicleModel"), rs.getString("VehicleMake"), rs.getString("PartName"), rs.getString("BookingDate"),rDate, dDate, cost,rs.getString("SPCName")));              
                    }
                }
                else if (isReturned.isSelected()){
                    if(LocalDate.now().isAfter(rDate) || LocalDate.now().isEqual(rDate)){
                        specialistList.add(new Specialist(rs.getString("SRID"), rs.getString("PartID"), rs.getString("SPCID"), rs.getString("BookingID"), rs.getString("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"), rs.getString("VehicleRegNo"), rs.getString("VehicleModel"), rs.getString("VehicleMake"), rs.getString("PartName"), rs.getString("BookingDate"),rDate, dDate, cost, rs.getString("SPCName")));              
                    }
                }
                else{
                    specialistList.add(new Specialist(rs.getString("SRID"), rs.getString("PartID"), rs.getString("SPCID"), rs.getString("BookingID"), rs.getString("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"), rs.getString("VehicleRegNo"), rs.getString("VehicleModel"), rs.getString("VehicleMake"), rs.getString("PartName"), rs.getString("BookingDate"), rDate, dDate, cost, rs.getString("SPCName")));              
                }
            }
            Database.getInstance().closeConnection();
        }catch(SQLException e){
            System.err.println(e);
        }
        table.setItems(specialistList);
    }
    
    public ObservableList getSPCs(){
        ObservableList<String> spcList = FXCollections.observableArrayList();
        spcList.add("All");
        try{
            Database.getInstance().connect();
            ResultSet rs = Database.getInstance().query("SELECT SPCID, SPCName FROM SPC WHERE SPCInUse=1");
            while(rs.next()){
                spcList.add(rs.getString("SPCID")+"| "+rs.getString("SPCName"));
            }
            Database.getInstance().closeConnection();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return spcList;
    }

    public void addSpecialist() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addScreen.fxml"));
        Parent root = (Parent)loader.load();
        AddScreenController controller = loader.<AddScreenController>getController();
        controller.setMainScreen(this);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add Specialist Repair");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(LoginScreenController.stage);
        stage.show();
    }

    public void editSpecialist() throws Exception{
        if(table.getSelectionModel().getSelectedItem()!=null){
            Specialist specialist = table.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editScreen.fxml"));
            Parent root = (Parent)loader.load();
            EditScreenController controller = loader.<EditScreenController>getController();
            controller.displayPassedSpecialist(specialist, this);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Specialist Repair");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(LoginScreenController.stage);
            stage.show();
        }else{
            errorAlert("Please select a row.");
        }
    }

    public void deleteSpecialist() throws Exception{
        if(table.getSelectionModel().getSelectedItem()!=null){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this specialist repair?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get()==ButtonType.OK){
                Specialist specialist = table.getSelectionModel().getSelectedItem();
                try{
                    Database.getInstance().connect();
                    String sql = "SELECT Bill.SPCCost FROM Bill WHERE BookingID="+specialist.getBookId();
                    ResultSet rs = Database.getInstance().query(sql);
                    double currentCost = Double.parseDouble(rs.getString("SPCCost"));
                    currentCost -= specialist.getCost();
                    sql = "UPDATE Bill SET SPCCost="+currentCost+" WHERE BookingID="+specialist.getBookId();
                    Database.getInstance().update(sql);
                    sql = "DELETE FROM SpecialistRepair WHERE SRID = "+specialist.getId();
                    Database.getInstance().update(sql);
                    Database.getInstance().closeConnection();
                    refreshPage();
                }catch(SQLException e){
                    System.err.println(e);
                }
            }
        }else{
            errorAlert("Please select a row.");
        }
    } 
    
    public void moreDetails() throws Exception{
        if(table.getSelectionModel().getSelectedItem()!=null){
            int id = Integer.parseInt(table.getSelectionModel().getSelectedItem().getId());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detailsScreen.fxml"));
            Parent root = (Parent)loader.load();
            DetailsScreenController controller = loader.<DetailsScreenController>getController();
            controller.displayDetails(id, this);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("More Detail's");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(LoginScreenController.stage);
            stage.show();
        }else{
            errorAlert("Please select a row.");
        } 
    } 
    
    public void showSPCs() throws Exception{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("spcScreen.fxml"));
            Parent root = (Parent)loader.load();
            SPCScreenController controller = loader.<SPCScreenController>getController();
            controller.disableTextFields();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("SPC");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(LoginScreenController.stage);
            stage.show();
    } 
    
    public void showBill() throws Exception{
        if(table.getSelectionModel().getSelectedItem()!=null){
            int id = Integer.parseInt(table.getSelectionModel().getSelectedItem().getCustomerId());
            String sql = "Select * From Customer Where CustomerID="+id;
            Database.getInstance().connect();
            ResultSet rs = Database.getInstance().query(sql);
            Customer customer = new Customer(Integer.parseInt(rs.getString("CustomerID")),
                        rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"),
                        rs.getString("CustomerType"), rs.getString("CustomerAddress"),
                        rs.getString("CustomerPostcode"), rs.getString("CustomerPhoneNo"),
                        rs.getString("CustomerEmail"));
            Database.getInstance().closeConnection();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewBillsScreen.fxml"));
            Parent root = (Parent)loader.load();
            ViewBillsScreenController controller = loader.<ViewBillsScreenController>getController();
            controller.setCustomer(customer);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Bills");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(LoginScreenController.stage);
            stage.show();
        }else{
            errorAlert("Please select a row.");
        } 
    }
    
}
