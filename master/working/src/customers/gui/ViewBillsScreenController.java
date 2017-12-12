/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.gui;

import diagrep.Diagrep;
import common.Database;
import customers.Bill;
import customers.BillWrapper;
import customers.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ViewBillsScreenController implements Initializable {

    // the customer for which the bookings will be displayed
    private Customer customer;
    //databse instance
    final private Database db = Database.getInstance();

    @FXML
    private Button closeButton;

    @FXML
    private TableView billsTable;

    @FXML
    private TableColumn<BillWrapper, String> bookingTypeCol;
    @FXML
    private TableColumn<BillWrapper, String> dateCol;
    @FXML
    private TableColumn<BillWrapper, String> vehicleRegCol;
    @FXML
    private TableColumn<BillWrapper, String> mechanicCostCol;
    @FXML
    private TableColumn<BillWrapper, String> spcCostCol;
    @FXML
    private TableColumn<BillWrapper, String> partsCostCol;
    @FXML
    private TableColumn<BillWrapper, String> totalCostCol;
    @FXML
    private TableColumn<BillWrapper, String> settledCol;

    @FXML
    private Label customerNameLabel;

    //List used to store data from database
    private ObservableList<customers.BillWrapper> data;

    final Date date = new Date();
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bookingTypeCol.setCellValueFactory(new PropertyValueFactory<BillWrapper, String>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<BillWrapper, String>("date"));
        vehicleRegCol.setCellValueFactory(new PropertyValueFactory<BillWrapper, String>("vehReg"));
        mechanicCostCol.setCellValueFactory(new PropertyValueFactory<BillWrapper, String>("mechanicCost"));
        partsCostCol.setCellValueFactory(new PropertyValueFactory<BillWrapper, String>("partsCost"));
        spcCostCol.setCellValueFactory(new PropertyValueFactory<BillWrapper, String>("spcCost"));
        totalCostCol.setCellValueFactory(new PropertyValueFactory<BillWrapper, String>("totalCost"));
        settledCol.setCellValueFactory(new PropertyValueFactory<BillWrapper, String>("settled"));

        billsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        data = FXCollections.observableArrayList();

    }

    public void loadBills() {
        try {
            db.connect();
            String sql = "SELECT Bill.MechanicCost,Bill.PartsCost, Bill.SPCCost, Bill.isSettled,(Bill.MechanicCost + Bill.PartsCost + Bill.SPCCost) as TotalCost, Booking.* FROM Bill JOIN Booking On Bill.BookingID=Booking.BookingID WHERE Booking.CustomerID=" + customer.getId() + ";";
            ResultSet rs = db.query(sql);
            data.clear();
            while (rs.next()) {
                boolean isSettled = false;
                if (rs.getString("isSettled").equals("1")) {
                    isSettled = true;
                }
                Date bookingDate = null;
                try {
                    bookingDate = dateFormat.parse(rs.getString("BookingDate"));
                } catch (ParseException ex) {
//                    System.out.println(ex.getClass() + ": " + ex.getMessage());
                }
                if (bookingDate.compareTo(date) < 0)
                data.add(new BillWrapper(
                        new Bill(rs.getInt("MechanicCost"), rs.getInt("PartsCost"), rs.getInt("SPCCost"), rs.getInt("TotalCost"), isSettled),
                        new Diagrep(Integer.parseInt(rs.getString("BookingID")), rs.getString("BookingType"), rs.getString("BookingDate"), "", rs.getString("VehicleRegNo"), "", 0, 0, "", "", 0, "")
                ));
            }
            billsTable.setItems(data);
        } catch (SQLException ex) {
            System.out.println(ex.getClass() + ": " + ex.getMessage());
        }
        db.closeConnection();
    }

    public void setCustomer(Customer c) {
        this.customer = c;
        customerNameLabel.setText(c.getFirstName() + " " + c.getLastName());
        loadBills();
    }

    /*
     * **********************************
     * VIEW THE VEHICLES OF A SELECTED  *
     * CUSTOMER                         *
     * **********************************
     */
    public void changeSettlemetStatus(ActionEvent e) {
        ObservableList<BillWrapper> selectedBills = billsTable.getSelectionModel().getSelectedItems();
        if (selectedBills.isEmpty()) {
            showInformationBox("Select a bill", "Please select a customer before attempting to change the settlement status.");
            return;
        }
        if (selectedBills.get(0).getSettled().equals("Settled")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Settlement Error");
            alert.setContentText("Cannot settle a settled bill.");
            alert.showAndWait();
            return;
        }

        BillWrapper selected = selectedBills.get(0);
        int id = selected.getBooking().getID();

        //CONFIRMATION//
        boolean confirmed = showConfirmation("Settle Bill", "Are you sure you wish to settle this bill (Total Amount: " + selected.getTotalCost() + ") You will not be able to revert this change.");
        if (!confirmed) {
            return;
        }

        try {
            db.connect();
            String sql = "UPDATE Bill SET IsSettled= '1' WHERE BookingID=" + id + ";";
            db.update(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getClass() + ": " + ex.getMessage());
        }
        db.closeConnection();
        loadBills();
    }

    public void close() {
        Stage thisStage = (Stage) closeButton.getScene().getWindow();
        thisStage.close();
    }

    /*
     * *******************************************
     * USED TO DISPLAY A DIALOG BOX WITH A GIVEN *
     * STRING AS THE MESSAGE DISPLAYED           *
    **********************************************
     */
    public void showInformationBox(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*
     * ************************************************
     * USED TO DISPLAY A CONFIRMATION BOX             *
     * THE USER IS EXPECTED TO MAKE A YES/NO DECISION *
    ***************************************************
     */
    public boolean showConfirmation(String title, String message) {
        boolean confirm = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) {
            // ... user chose Yes
            confirm = true;
            return confirm;
        } else {
            // ... user chose CANCEL or closed the dialog
            return confirm;
        }
    }

}
