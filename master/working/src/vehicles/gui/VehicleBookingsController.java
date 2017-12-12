 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles.gui;

import common.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vehicles.Booking;
import vehicles.Customer;
import vehicles.Vehicle;
import vehicles.VehicleWrapper;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class VehicleBookingsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button exit;
    
    @FXML 
    private TextField custNameField;
    @FXML 
    private TextField custSurnameField;
    @FXML
    private TextField vehRegField;
    @FXML
    private TextField vehModelField;
    @FXML
    private ComboBox pastFutureCombo;
    
    /***********************************************************
     * BOOKINGS TABLE
     ***********************************************************/
     
    @FXML
    private TableView<vehicles.Booking> bookingsTable;
    @FXML
    private TableColumn<vehicles.Booking, String> colBookingType;
    @FXML
    private TableColumn<vehicles.Booking, String> colBookingDate;
    @FXML 
    private TableColumn<vehicles.Booking, String> colBookingCost;
    
    private VehicleWrapper wrapper;
    
    private Vehicle vehicle;
    
    private Customer customer;
    
    private Booking booking;
    
    private Database db;
    
    private ObservableList<vehicles.Booking> BookingsObservable;
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
    }   
    
    public void reinit(){
        
        db=db.getInstance();
        this.setVariables();
        this.setFields();
        this.setBookings();
        this.setTable();
        outputTableData(null);

    }
    private void setVariables(){
        customer=wrapper.getCustomer();
        vehicle=wrapper.getVehicle();
        booking=wrapper.getBooking();
    }
    private void setFields(){
        
        custNameField.setText(customer.getFirstName());
        custSurnameField.setText(customer.getLastName());
        vehRegField.setText(vehicle.getRegNum());
        vehModelField.setText(vehicle.getModel());
    }
    private void setBookings(){
        pastFutureCombo.setItems(FXCollections.observableArrayList("Future Bookings","Past Bookings", "Past & Future"));
        
        pastFutureCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    
                    ResultSet rs;
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime now = LocalDateTime.now();
                    BookingsObservable.clear();
                    BookingsObservable=FXCollections.observableArrayList();

                    String sql="SELECT BookingType, BookingDate, MechanicCost, PartsCost, SPCCost, b.BookingID " +
                    " FROM Booking b " +
                    " JOIN Bill bi " +
                    " ON bi.BookingID=b.BookingID " +
                    " WHERE b.VehicleRegNo='"+vehicle.getRegNum()+"';";
                    
                    if(newValue.equals("Past Bookings")){
                            try {
                                db.connect();
                                rs=db.query(sql);
                                
                                
                                while(rs.next()){
                                     if(LocalDateTime.parse(rs.getString("BookingDate"), dtf).compareTo(now) < 0){
                                         BookingsObservable.add(new Booking(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), "",0,"", rs.getInt("MechanicCost")+rs.getInt("PartsCost")+rs.getInt("SPCCost")));
                                     }
                                }
                                db.closeConnection();
                                if(BookingsObservable.isEmpty() || BookingsObservable==null){
                                    outputTableData(null);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Booking Information");
                                    alert.setHeaderText("Vehicle: "+vehicle.getRegNum());
                                    alert.setContentText("Has no Past Bookings!");
                                    alert.showAndWait();
                                    pastFutureCombo.getSelectionModel().select(2);

                                }

                            } catch (SQLException ex) {
                                Logger.getLogger(VehicleBookingsController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            bookingsTable.setItems(BookingsObservable);
                        
                    }else if(newValue.equals("Future Bookings")){
                            try {
                                db.connect();
                                rs=db.query(sql);
                                 

                               
                               while(rs.next()){
                                    if(LocalDateTime.parse(rs.getString("BookingDate"), dtf).compareTo(now) > 0){
                                         BookingsObservable.add(new Booking(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), "",0,"", rs.getInt("MechanicCost")+rs.getInt("PartsCost")+rs.getInt("SPCCost")));

                                    }
  
                               }
                                db.closeConnection();
                                
                                if(BookingsObservable.isEmpty() || BookingsObservable==null){
                                    outputTableData(null);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Booking Information");
                                    alert.setHeaderText("Vehicle: "+vehicle.getRegNum());
                                    alert.setContentText("Has no Future Bookings!");
                                    alert.showAndWait();
                                    pastFutureCombo.getSelectionModel().select(2);
                                }

                            } catch (SQLException ex) {
                                Logger.getLogger(VehicleBookingsController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                           bookingsTable.setItems(BookingsObservable);
                       
                    }else{
                           outputTableData(null);
                    }
                    
                        
        }});
    }
    private void setTable(){
        colBookingDate.setCellValueFactory(new PropertyValueFactory<vehicles.Booking, String>("date"));
        colBookingType.setCellValueFactory(new PropertyValueFactory<vehicles.Booking, String>("type"));
        colBookingCost.setCellValueFactory(new PropertyValueFactory<vehicles.Booking, String>("bill"));
    }
    private void outputTableData(String sql){
        
        BookingsObservable=FXCollections.observableArrayList();
        BookingsObservable.clear();
        
        if(sql==null){
            
                sql="SELECT BookingType, BookingDate, MechanicCost, PartsCost, SPCCost, b.BookingID " +
                    " FROM Booking b " +
                    " JOIN Bill bi " +
                    " ON bi.BookingID=b.BookingID " +
                    " WHERE b.VehicleRegNo='"+vehRegField.getText()+"';";
                
        }
                
        ResultSet rs;
        try {
                db.connect();
                rs = db.query(sql);
                
                while(rs.next()){
                    BookingsObservable.add(new Booking(rs.getInt("BookingID"), rs.getString("BookingType"), rs.getString("BookingDate"), "",0,"", rs.getInt("MechanicCost")+rs.getInt("PartsCost")+rs.getInt("SPCCost")));

                }
                db.closeConnection();
        }catch(SQLException e){
                 e.printStackTrace();
        }
        bookingsTable.setItems(BookingsObservable);
        
        
    }
    
    
    @FXML
    public void handleVehicleParts() throws ClassNotFoundException, SQLException, IOException{
       Booking bk=bookingsTable.getSelectionModel().getSelectedItem();
        
        
        if(bk==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Select a Booking from the list");
            alert.showAndWait();
        }else{
            String sql="SELECT PartName, SRReturnDate "
                + "FROM Part p "
                + "JOIN SpecialistRepair r "
                + "ON 	p.PartID=r.PartID "
                + "WHERE r.BookingID='" + bk.getID() + "' ";
             String sql2 = "SELECT PartName, PartInstalledDate "
                + "FROM Part p "
                + "JOIN Parts_Installed pi "
                + "ON p.PartID=pi.PartID "
                + "WHERE pi.BookingID='" + bk.getID() + "';";
            
            db.connect();
            ResultSet rs=db.query(sql);
            boolean result=rs.isBeforeFirst();
            db.closeConnection();
            db.connect();
            ResultSet rs2=db.query(sql2);
            boolean result2=rs2.isBeforeFirst();
            db.closeConnection();
            
            if(!result && !result2){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Bookings Information");
                alert.setHeaderText("Booking: "+bk.getDate());
                alert.setContentText("Has no parts Installed yet!");
                alert.showAndWait();
            }else{
                
                
                booking.setDate(bk.getDate());
                booking.setID(bk.getID());
                booking.setType(bk.getType());
                
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VehicleParts.fxml"));     
                Parent root = (Parent)fxmlLoader.load(); 

                VehiclePartsController controller = fxmlLoader.<VehiclePartsController>getController();
                controller.setVehicle(wrapper); 
                controller.reinit();

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.setTitle("Vehicle Parts");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(VehiclesScreenController.bookingStage);
                stage.show();
            }
        }
        
    }
   
    
    public void setVehicle(VehicleWrapper vh){
        this.wrapper=vh;
    }
    
    @FXML 
    public void handleExit(){
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    
}
