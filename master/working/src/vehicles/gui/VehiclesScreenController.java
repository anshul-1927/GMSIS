/**
 * FXML Controller class
 *
 * @author Efthymios
 */
package vehicles.gui;

import common.Database;
import common.LoginScreenController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import vehicles.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;

public class VehiclesScreenController implements Initializable {

    @FXML
    private ChoiceBox searchOpt;
    @FXML
    private ComboBox<vehicles.Vehicle> vehicleTemplateDropDownList;
    @FXML
    private TextField searchField;
    
    
    
    private ToggleGroup groupSearch;
    
    @FXML
    private RadioButton car;
    @FXML
    private RadioButton van;
    @FXML
    private RadioButton truck;
    @FXML 
    private RadioButton all;
    
    @FXML
    private TableView<vehicles.VehicleWrapper> vehicleTable;

    @FXML
    private TableColumn<vehicles.VehicleWrapper, String> colVehicleType;
    
    
    @FXML
    private TableColumn<vehicles.VehicleWrapper, String> colRegNum;
    @FXML
    private TableColumn<vehicles.VehicleWrapper, String> colModel;
    @FXML
    private TableColumn<vehicles.VehicleWrapper, String> colMileage;
    @FXML
    private TableColumn<vehicles.VehicleWrapper, String> colVehMake;
    @FXML
    private TableColumn<vehicles.VehicleWrapper, String> colLastService;
    @FXML
    private TableColumn<vehicles.VehicleWrapper, String> colName;
    @FXML
    private TableColumn<vehicles.VehicleWrapper, String> colLastName;
    @FXML
    private TableColumn<vehicles.VehicleWrapper, String> colNextBookingDate;
    @FXML
    private TableColumn<vehicles.VehicleWrapper, String> colBookingType;

    private Database db;
    
    private ObservableList<vehicles.VehicleWrapper> vehiclesObservable;
    
    public static Stage bookingStage;
    
    public ObservableList<vehicles.Vehicle> vehicleTemplate;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        db = Database.getInstance();
        this.setVehicleTemplate();
        this.setVehicleViewing();
        this.setDropDown();
        this.setTable();
        outputTableData(null);
    }
    @FXML
    public void allVehicles(){
        groupSearch.selectToggle(null);
        groupSearch.selectToggle(all);
    }
   
    private void setVehicleTemplate(){
         String getTemplateQuery = "SELECT * FROM vehicle_template;";
         vehicleTemplate = FXCollections.observableArrayList();
         Vehicle veh = new Vehicle("", "", "ALL", "", "", "", "", "", "", "",null);
         vehicleTemplate.add(veh);
         try {
            db.connect();
            ResultSet TemplateRs = db.query(getTemplateQuery);

            

            while (TemplateRs.next()) {
                Vehicle vh = new Vehicle(TemplateRs.getString("Type"), "", TemplateRs.getString("template_make"), TemplateRs.getString("template_model"), TemplateRs.getString("template_engine_size"), TemplateRs.getString("template_fuel_type"), "", "", "", "",null);
                vehicleTemplate.add(vh);
            }
            db.closeConnection();
            

         }catch (SQLException ex) {
            ex.printStackTrace();
            
         }

         vehicleTemplateDropDownList.setItems(vehicleTemplate);
         vehicleTemplateDropDownList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vehicle>() {

            @Override
            public void changed(ObservableValue<? extends Vehicle> observable, Vehicle oldValue, Vehicle veh) {
                
                String sql;
                if(veh.getMake().equals("ALL")){
                    groupSearch.selectToggle(null);
                    groupSearch.selectToggle(all);  
                }else{
                    sql=
                           "SELECT c.CustomerType, v.VehicleType, v.VehicleRegNo, v.VehicleModel, v.VehicleFuelType, v.VehicleColour, v.VehicleMake, v.VehicleEngSize, v.VehicleMileage, v.VehicleMoTDate, v.LastServiceDate, v.WarrantyName, v.WarrantyAddress, v.WarrantyExpDate, c.CustomerFirstName, c.CustomerLastName,c.CustomerAddress, c.CustomerPostcode,c.CustomerPhoneNo,c.CustomerEmail, c.CustomerID, b.BookingID , b.BookingDate, b.BookingType "+
                         "FROM Customer c  "+ 
                         "JOIN Vehicle v  "+ 
                         "ON v.CustomerID=c.CustomerID  "+
                         "LEFT JOIN  "+
                         "( "+
                           "SELECT b.BookingID , b.BookingDate, b.BookingType, b.VehicleRegNo "+
                           "FROM( "+
                                  "SELECT BookingID , BookingDate, BookingType, VehicleRegNo "+
                                  "FROM Booking b "+
                                  "ORDER BY b.BookingDate DESC "+
                               ") b "+

                            "WHERE b.BookingDate>'"+getCurrentTime()+"' "+

                            "GROUP BY b.VehicleRegNo "+
                         ") B "+
                         "ON B.VehicleRegNo=v.VehicleRegNo "+
                         "WHERE v.VehicleMake='"+veh.getMake()+"' AND v.VehicleModel='"+veh.getModel()+"' "+
                         "ORDER BY c.CustomerFirstName DESC;";
                    if(!checkDataExists(sql)){
                          Alert alert = new Alert(AlertType.WARNING);
                          alert.setTitle("Information");
                          alert.setHeaderText("There are no "+veh.getMake()+" "+veh.getModel()+" records!");
                          alert.showAndWait();
                          groupSearch.selectToggle(null); 
                          groupSearch.selectToggle(all);  
                          return;
                      }
                    
                    outputTableData(sql);
                  
            }
             
            
            
            }
            
        });
    }
    
    private void setVehicleViewing(){
        groupSearch=new ToggleGroup();
        car.setToggleGroup(groupSearch);
        van.setToggleGroup(groupSearch); 
        truck.setToggleGroup(groupSearch);
        all.setToggleGroup(groupSearch);
        groupSearch.selectToggle(all);
        groupSearch.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                    vehicleTemplateDropDownList.getSelectionModel().clearSelection();
                    String sql="";
                    if(groupSearch.getSelectedToggle() == all){
                             sql=null;
                    }else if (groupSearch.getSelectedToggle() == car){
                     sql = 
                      "SELECT c.CustomerType, v.VehicleType, v.VehicleRegNo, v.VehicleModel, v.VehicleFuelType, v.VehicleColour, v.VehicleMake, v.VehicleEngSize, v.VehicleMileage, v.VehicleMoTDate, v.LastServiceDate, v.WarrantyName, v.WarrantyAddress, v.WarrantyExpDate, c.CustomerFirstName, c.CustomerLastName,c.CustomerAddress, c.CustomerPostcode,c.CustomerPhoneNo,c.CustomerEmail, c.CustomerID, b.BookingID , b.BookingDate, b.BookingType "+
                      "FROM Customer c  "+ 
                      "JOIN Vehicle v  "+ 
                      "ON v.CustomerID=c.CustomerID  "+
                      "LEFT JOIN  "+
		      "( "+
		        "SELECT b.BookingID , b.BookingDate, b.BookingType, b.VehicleRegNo "+
		        "FROM( "+
                               "SELECT BookingID , BookingDate, BookingType, VehicleRegNo "+
                               "FROM Booking b "+
                               "ORDER BY b.BookingDate DESC "+
			    ") b "+
			     
		         "WHERE b.BookingDate>'"+getCurrentTime()+"' "+
		        
		         "GROUP BY b.VehicleRegNo "+
		      ") B "+
                      "ON B.VehicleRegNo=v.VehicleRegNo "+
                      "WHERE v.VehicleType='Car' "+
                      "ORDER BY c.CustomerFirstName DESC;";
                     if(!checkDataExists(sql)){
                          Alert alert = new Alert(AlertType.WARNING);
                          alert.setTitle("Information");
                          alert.setHeaderText("There are no Car records!");
                          alert.showAndWait();
                          groupSearch.selectToggle(all);  
                          return;
                      }
                        
                    }else if(groupSearch.getSelectedToggle() == van){
                        
                            sql = 
                      "SELECT c.CustomerType, v.VehicleType, v.VehicleRegNo, v.VehicleModel, v.VehicleFuelType, v.VehicleColour, v.VehicleMake, v.VehicleEngSize, v.VehicleMileage, v.VehicleMoTDate, v.LastServiceDate, v.WarrantyName, v.WarrantyAddress, v.WarrantyExpDate, c.CustomerFirstName, c.CustomerLastName,c.CustomerAddress, c.CustomerPostcode,c.CustomerPhoneNo,c.CustomerEmail, c.CustomerID, b.BookingID , b.BookingDate, b.BookingType "+
                      "FROM Customer c  "+ 
                      "JOIN Vehicle v  "+ 
                      "ON v.CustomerID=c.CustomerID  "+
                      "LEFT JOIN  "+
		      "( "+
		        "SELECT b.BookingID , b.BookingDate, b.BookingType, b.VehicleRegNo "+
		        "FROM( "+
                               "SELECT BookingID , BookingDate, BookingType, VehicleRegNo "+
                               "FROM Booking b "+
                               "ORDER BY b.BookingDate DESC "+
			    ") b "+
			     
		         "WHERE b.BookingDate>'"+getCurrentTime()+"' "+
		        
		         "GROUP BY b.VehicleRegNo "+
		      ") B "+
                      "ON B.VehicleRegNo=v.VehicleRegNo "+
                      "WHERE v.VehicleType='Van' "+
                      "ORDER BY c.CustomerFirstName DESC;"; 
                      if(!checkDataExists(sql)){
                          Alert alert = new Alert(AlertType.WARNING);
                          alert.setTitle("Information");
                          alert.setHeaderText("There are no Van records!");
                          alert.showAndWait();
                          groupSearch.selectToggle(all);  
                          return;
                      }
                        
                    }else{
                            sql = 
                      "SELECT c.CustomerType, v.VehicleType, v.VehicleRegNo, v.VehicleModel, v.VehicleFuelType, v.VehicleColour, v.VehicleMake, v.VehicleEngSize, v.VehicleMileage, v.VehicleMoTDate, v.LastServiceDate, v.WarrantyName, v.WarrantyAddress, v.WarrantyExpDate, c.CustomerFirstName, c.CustomerLastName,c.CustomerAddress, c.CustomerPostcode,c.CustomerPhoneNo,c.CustomerEmail, c.CustomerID, b.BookingID , b.BookingDate, b.BookingType "+
                      "FROM Customer c  "+ 
                      "JOIN Vehicle v  "+ 
                      "ON v.CustomerID=c.CustomerID  "+
                      "LEFT JOIN  "+
		      "( "+
		        "SELECT b.BookingID , b.BookingDate, b.BookingType, b.VehicleRegNo "+
		        "FROM( "+
                               "SELECT BookingID , BookingDate, BookingType, VehicleRegNo "+
                               "FROM Booking b "+
                               "ORDER BY b.BookingDate DESC "+
			    ") b "+
			     
		         "WHERE b.BookingDate>'"+getCurrentTime()+"' "+
		        
		         "GROUP BY b.VehicleRegNo "+
		      ") B "+
                      "ON B.VehicleRegNo=v.VehicleRegNo "+
                      "WHERE v.VehicleType='Truck' "+
                      "ORDER BY c.CustomerFirstName DESC;";
                       if(!checkDataExists(sql)){
                          Alert alert = new Alert(AlertType.WARNING);
                          alert.setTitle("Information");
                          alert.setHeaderText("There are no Truck records!");
                          alert.showAndWait();
                          groupSearch.selectToggle(all);  
                          return;
                      }
                                    
                    }
                    outputTableData(sql);
                    
                   
            }
        });
    }
    private void setDropDown() {
        searchOpt.setItems(FXCollections.observableArrayList("Vehicle Reg. Number", new Separator(), "Vehicle Manufacturer"));
        searchOpt.getSelectionModel().selectFirst();
        
    }

    private void setTable() {
        vehiclesObservable = FXCollections.observableArrayList();
        colVehicleType.setCellValueFactory(new PropertyValueFactory<vehicles.VehicleWrapper, String>("vehicleType"));
        colRegNum.setCellValueFactory(new PropertyValueFactory<vehicles.VehicleWrapper, String>("regNum"));
        colModel.setCellValueFactory(new PropertyValueFactory<vehicles.VehicleWrapper, String>("model"));
        colVehMake.setCellValueFactory(new PropertyValueFactory<vehicles.VehicleWrapper, String>("make"));
        colMileage.setCellValueFactory(new PropertyValueFactory<vehicles.VehicleWrapper, String>("mileage"));
        colLastService.setCellValueFactory(new PropertyValueFactory<vehicles.VehicleWrapper, String>("lastServiceDate"));
        colName.setCellValueFactory(new PropertyValueFactory<vehicles.VehicleWrapper, String>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<vehicles.VehicleWrapper, String>("LastName"));
        colNextBookingDate.setCellValueFactory(new PropertyValueFactory<vehicles.VehicleWrapper, String>("date"));
        colBookingType.setCellValueFactory(new PropertyValueFactory<vehicles.VehicleWrapper, String>("type"));
        
        
    }

    

    

    @FXML
    public void delete() {
        VehicleWrapper wrapper=vehicleTable.getSelectionModel().getSelectedItem();
        if(wrapper==null){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Vehicle from the table!");
            alert.showAndWait();
        }else{
            Vehicle veh=wrapper.getVehicle();
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Vehicle: "+veh.getRegNum());
            alert.setContentText("Are you sure you want to delete Vehicle: "+veh.getRegNum()+" ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                String reg = veh.getRegNum();
                db.connect();
                try {
                    db.update("DELETE FROM Vehicle WHERE VehicleRegNo=\'" + reg + "\';");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                db.closeConnection();
                outputTableData(null);
            } 
            
        }
    }

   

    @FXML
    public void loadButtonHandler() {
        outputTableData(null);
    }
    private String getCurrentTime(){
        Calendar cal =Calendar.getInstance();
        SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return formatt.format(cal.getTime());
        
    }
    public void outputTableData(String sql) {
        searchField.clear();       
        vehiclesObservable.clear();
        vehiclesObservable = FXCollections.observableArrayList();

        if (sql == null) {
            
            sql=
                      "SELECT v.VehicleType, v.VehicleRegNo, v.VehicleModel, v.VehicleFuelType, v.VehicleColour, v.VehicleMake, v.VehicleEngSize, v.VehicleMileage, v.VehicleMoTDate, v.LastServiceDate, v.WarrantyName, v.WarrantyAddress, v.WarrantyExpDate, c.CustomerFirstName, c.CustomerLastName, c.CustomerType, c.CustomerAddress, c.CustomerPostcode,c.CustomerPhoneNo,c.CustomerEmail, c.CustomerID, b.BookingID , b.BookingDate, b.BookingType "+
                      "FROM Customer c  "+ 
                      "JOIN Vehicle v  "+ 
                      "ON v.CustomerID=c.CustomerID  "+
                      "LEFT JOIN  "+
		      "( "+
		        "SELECT b.BookingID , b.BookingDate, b.BookingType, b.VehicleRegNo "+
		        "FROM( "+
                               "SELECT BookingID , BookingDate, BookingType, VehicleRegNo "+
                               "FROM Booking b "+
                               "ORDER BY b.BookingDate DESC "+
			    ") b "+
			     
		         "WHERE b.BookingDate>'"+this.getCurrentTime()+"' "+
		        
		         "GROUP BY b.VehicleRegNo "+
		      ") B "+
                      "ON B.VehicleRegNo=v.VehicleRegNo "+
                      "ORDER BY c.CustomerFirstName DESC;";
            groupSearch.selectToggle(all);
        }
        try {
            ResultSet rs;
            db.connect();
            rs = db.query(sql);
           
            while (rs.next()){
                Customer cust=new Customer(rs.getInt("CustomerID"), rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"), rs.getString("CustomerType"),rs.getString("CustomerAddress"), rs.getString("CustomerPostcode"),rs.getString("CustomerPhoneNo"),rs.getString("CustomerEmail"));
                Vehicle veh;
                if(rs.getString("WarrantyName")==null || rs.getString("WarrantyExpDate")==null|| rs.getString("WarrantyAddress")==null){
                    NonWarranty nonWarrantyRole=new NonWarranty();
                    veh=new Vehicle(rs.getString("VehicleType"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getString("VehicleModel"), rs.getString("VehicleEngSize"), rs.getString("VehicleFuelType"), rs.getString("VehicleColour"), rs.getString("VehicleMoTDate"), rs.getString("VehicleMileage"), rs.getString("LastServiceDate"), nonWarrantyRole);

                }else{
                    UnderWarranty underWarrantyRole=new UnderWarranty(rs.getString("WarrantyName"),rs.getString("WarrantyExpDate"), rs.getString("WarrantyAddress") );
                    veh=new Vehicle(rs.getString("VehicleType"), rs.getString("VehicleRegNo"), rs.getString("VehicleMake"), rs.getString("VehicleModel"), rs.getString("VehicleEngSize"), rs.getString("VehicleFuelType"), rs.getString("VehicleColour"), rs.getString("VehicleMoTDate"), rs.getString("VehicleMileage"), rs.getString("LastServiceDate"), underWarrantyRole);
  
                }
                Booking bk=new Booking(0,rs.getString("BookingType"),rs.getString("BookingDate"),"",0,"",0);
                VehicleWrapper wrapper=new VehicleWrapper(cust, veh, bk);
                vehiclesObservable.add(wrapper);
            }

            db.closeConnection();
            this.lastServiceDate();//calculates last service date for each vehicle
        } catch (SQLException e) {
            e.printStackTrace();
        }
        vehicleTable.setItems(vehiclesObservable);

    }
    private void lastServiceDate(){
        String regNum="";
        String lastService="";
        String sql="";
        for(VehicleWrapper wrapper : vehiclesObservable){
            Vehicle veh=wrapper.getVehicle();
            regNum=veh.getRegNum();
            
            sql=
                       "SELECT b.BookingDate, b.VehicleRegNo "+
		       "FROM( "+
			     "SELECT   BookingDate, VehicleRegNo "+  
		             "FROM Booking b "+ 
			     "ORDER BY b.BookingDate ASC "+ 
			   ") b "+ 
			     
		      "WHERE b.BookingDate<'"+this.getCurrentTime()+"' AND b.VehicleRegNo='"+regNum+"' "+
		        
		      "GROUP BY b.VehicleRegNo; ";
            
            try {
                    db.connect();
                    ResultSet rs=db.query(sql);
                    lastService=rs.getString("BookingDate");
                    veh.setLastServiceDate(lastService);
                    db.closeConnection();

                    db.connect();
                    sql="UPDATE Vehicle SET LastServiceDate="+lastService+" WHERE VehicleRegNo='"+regNum+"';";
                    db.update(sql);
                    db.closeConnection();
                
            } catch (SQLException ex) {
               
            }
            
            
            
            
        }
    }
    private boolean checkDataExists(String sql){
        db.connect();
        ResultSet rs;
        boolean result=false;
        try {
            rs = db.query(sql);
            result=rs.isBeforeFirst();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        db.closeConnection();
        return result;
    }

    
    @FXML
    public void handleSearch() {
        String searchBy = (String) searchOpt.getSelectionModel().getSelectedItem();
        String input = searchField.getText();
        
        if (input.equals("")) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Fill in the search bar!");
            alert.showAndWait();     
            groupSearch.selectToggle(null);
            groupSearch.selectToggle(all);
            return;
        }else if (!input.matches("[\\w\\s]+")) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Input Error");
            alert.setHeaderText("Error");
	    alert.setContentText("Illegal input detected, Try again!");
            alert.showAndWait();
	    groupSearch.selectToggle(null);
            groupSearch.selectToggle(all);
            return;
            
        }else{
            String query = "";
            switch (searchBy) {

                case "Vehicle Reg. Number":
                    query= "SELECT c.CustomerType, v.VehicleType, v.VehicleRegNo, v.VehicleModel, v.VehicleFuelType, v.VehicleColour, v.VehicleMake, v.VehicleEngSize, v.VehicleMileage, v.VehicleMoTDate, v.LastServiceDate, v.WarrantyName, v.WarrantyAddress, v.WarrantyExpDate, c.CustomerFirstName, c.CustomerLastName,c.CustomerAddress, c.CustomerPostcode,c.CustomerPhoneNo,c.CustomerEmail, c.CustomerID, b.BookingID , b.BookingDate, b.BookingType "+
                      "FROM Customer c  "+ 
                      "JOIN Vehicle v  "+ 
                      "ON v.CustomerID=c.CustomerID  "+
                      "LEFT JOIN  "+
		      "( "+
		        "SELECT b.BookingID , b.BookingDate, b.BookingType, b.VehicleRegNo "+
		        "FROM( "+
                               "SELECT BookingID , BookingDate, BookingType, VehicleRegNo "+
                               "FROM Booking b "+
                               "ORDER BY b.BookingDate DESC "+
			    ") b "+
			     
		         "WHERE b.BookingDate>'"+this.getCurrentTime()+"' "+
		        
		         "GROUP BY b.VehicleRegNo "+
		      ") B "+
                      "ON B.VehicleRegNo=v.VehicleRegNo "+
                      "WHERE v.VehicleRegNo LIKE '%" + input + "%' "+
                      "GROUP BY v.VehicleRegNo;";
                     if(!checkDataExists(query)){
                          Alert alert = new Alert(AlertType.WARNING);
                          alert.setTitle("Information");
                          alert.setHeaderText("There is no matching vehicle registration number!");
                          alert.showAndWait();
                          groupSearch.selectToggle(all); 
                          searchField.clear();
                          return;
                      }
                    break;

                case "Vehicle Manufacturer":
                    query = "SELECT c.CustomerType, v.VehicleType, v.VehicleRegNo, v.VehicleModel, v.VehicleFuelType, v.VehicleColour, v.VehicleMake, v.VehicleEngSize, v.VehicleMileage, v.VehicleMoTDate, v.LastServiceDate, v.WarrantyName, v.WarrantyAddress, v.WarrantyExpDate, c.CustomerFirstName, c.CustomerLastName,c.CustomerAddress, c.CustomerPostcode,c.CustomerPhoneNo,c.CustomerEmail, c.CustomerID, b.BookingID , b.BookingDate, b.BookingType "+
                      "FROM Customer c  "+ 
                      "JOIN Vehicle v  "+ 
                      "ON v.CustomerID=c.CustomerID  "+
                      "LEFT JOIN  "+
		      "( "+
		        "SELECT b.BookingID , b.BookingDate, b.BookingType, b.VehicleRegNo "+
		        "FROM( "+
                               "SELECT BookingID , BookingDate, BookingType, VehicleRegNo "+
                               "FROM Booking b "+
                               "ORDER BY b.BookingDate DESC "+
			    ") b "+
			     
		         "WHERE b.BookingDate>'"+this.getCurrentTime()+"' "+
		        
		         "GROUP BY b.VehicleRegNo "+
		      ") B "+
                      "ON B.VehicleRegNo=v.VehicleRegNo "+
                      "WHERE v.VehicleMake LIKE '%" + input + "%' "+
                      "GROUP BY v.VehicleRegNo;";
                    if(!checkDataExists(query)){
                          Alert alert = new Alert(AlertType.WARNING);
                          alert.setTitle("Information");
                          alert.setHeaderText("There is no matching vehicle manufacturer!");
                          alert.showAndWait();
                          groupSearch.selectToggle(all);  
                          searchField.clear();
                          return;
                      }
                    break;
            }
            outputTableData(query);

        }

    }

    @FXML
    public void handleEdit() throws ClassNotFoundException, SQLException, IOException {

        VehicleWrapper wrapper=vehicleTable.getSelectionModel().getSelectedItem();
        
        if (wrapper == null) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Select a vehicle first from the table");
                alert.showAndWait();        
        } else {
            Vehicle veh = wrapper.getVehicle();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditVehicle.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            EditVehicleController controller = fxmlLoader.<EditVehicleController>getController();
            controller.setWrapper(wrapper);
            controller.setParentController(this);
            controller.reinit();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Edit Vehicle!");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(LoginScreenController.stage);
            stage.show();
        }

    }

    @FXML
    public void handleVehicleBookings() throws ClassNotFoundException, SQLException, IOException {

        VehicleWrapper wrapper=vehicleTable.getSelectionModel().getSelectedItem();
        
        if (wrapper == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Vehicle from the table");
            alert.showAndWait();
        }else{
            Vehicle veh = wrapper.getVehicle();
            String sql="SELECT BookingType, BookingDate, MechanicCost, PartsCost, SPCCost, b.BookingID " +
                    " FROM Booking b " +
                    " JOIN Bill bi " +
                    " ON bi.BookingID=b.BookingID " +
                    " WHERE b.VehicleRegNo='"+veh.getRegNum()+"';";
            
            db.connect();
            ResultSet rs=db.query(sql);
            boolean result=rs.isBeforeFirst();
            db.closeConnection();
            
            if(!result){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Bookings Information");
                alert.setHeaderText("Vehicle: "+veh.getRegNum());
                alert.setContentText("Has no repair bookings!");
                alert.showAndWait();
            }else{

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VehicleBookings.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                VehicleBookingsController controller = fxmlLoader.<VehicleBookingsController>getController();
                controller.setVehicle(wrapper);
                controller.reinit();

                Scene scene = new Scene(root);
                bookingStage = new Stage();
                bookingStage.setScene(scene);
                bookingStage.centerOnScreen();
                bookingStage.setTitle("Vehicle Bookings");
                bookingStage.initModality(Modality.WINDOW_MODAL);
                bookingStage.initOwner(LoginScreenController.stage);
                bookingStage.show();
            }
        }

    }
    @FXML
    public void handleVehicleDetails() throws ClassNotFoundException, SQLException, IOException {

        VehicleWrapper wrapper = vehicleTable.getSelectionModel().getSelectedItem();
        if (wrapper == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Select a vehicle first from the table");
            alert.showAndWait();
        } else {
            Vehicle veh = wrapper.getVehicle();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VehicleDetailsScreen.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            VehicleDetailsScreenController controller = fxmlLoader.<VehicleDetailsScreenController>getController();
            controller.setVehicle(wrapper);
            controller.reinit();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Vehicle Details");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(LoginScreenController.stage);
            stage.show();
        }

    }
    @FXML
    public void addVehicle() throws ClassNotFoundException, SQLException, IOException {

        

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addVehicle.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            AddVehicleController controller = fxmlLoader.<AddVehicleController>getController();
            controller.setParentController(this);
            

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Add Vehicle");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(LoginScreenController.stage);
            stage.show();
    }

 }
    


