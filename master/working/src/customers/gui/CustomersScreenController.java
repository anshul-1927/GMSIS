/*
Credits:
    - CodeMakery: Dialog and ALert Boxes: http://code.makery.ch/blog/javafx-dialogs-official/


 */
package customers.gui;

import common.Database;
import customers.Customer;
import diagrep.gui.AddWindowController;

import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

public class CustomersScreenController implements Initializable {

    //Connection to the Database
    Connection conn;

    //FXML Elements
    @FXML
    private HBox hBox;

    //Inject FX Choicebox
    @FXML
    private ChoiceBox searchOptions;

    //Inject FX TableView
    @FXML
    private TableView<customers.Customer> customersTable;

    //Inject FX TableColumns
    @FXML
    private TableColumn<customers.Customer, String> firstNameCol;
    @FXML
    private TableColumn<customers.Customer, String> lastNameCol;
    @FXML
    private TableColumn<customers.Customer, String> addressCol;
    @FXML
    private TableColumn<customers.Customer, String> postcodeCol;
    @FXML
    private TableColumn<customers.Customer, String> phoneNumberCol;
    @FXML
    private TableColumn<customers.Customer, String> emailAddressCol;
    @FXML
    private TableColumn<customers.Customer, String> typeCol;

    //Inject FX TextFields
    @FXML
    private TextField searchField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postcodeField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailAddressField;

    //Inject FX Button Controls
    @FXML
    private Button searchButton;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button pastBookingsButton;
    @FXML
    private Button futureBookingsButton;
    @FXML
    private Button vehiclesButton;
    @FXML
    private Button billsButton;

    //Inject FX ToggleGroup and Child Controls(x2 RadioButtons)
    @FXML
    private ToggleGroup customerType;
    @FXML
    private RadioButton privateRadio;
    @FXML
    private RadioButton businessRadio;
    /*For filter radio Buttons*/
    @FXML
    private ToggleGroup customerTypeFilter;
    @FXML
    private RadioButton privateFilter;
    @FXML
    private RadioButton businessFilter;
    @FXML
    private RadioButton allFilter;

    //Inject FX Labels
    @FXML
    private Label fieldLabel1;
    @FXML
    private Label fieldLabel2;
    @FXML
    private Label fieldLabel3;
    @FXML
    private Label fieldLabel4;
    @FXML
    private Label fieldLabel5;
    @FXML
    private Label fieldLabel6;
    @FXML
    private Label tableInfo;

    //Database instance
    private Database db = Database.getInstance();

    //Lists used to display data in table
    private ObservableList<customers.Customer> data;
    private ObservableList<customers.Customer> dataFiltered;
    //String used to display number of rows in table
    final private String tInfo = "Number of rows:";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // add items to ChoiceBox
        searchOptions.setItems(
                FXCollections.observableArrayList("First Name", "Last Name", new Separator(), "Vehicle Registration Number")
        );
        //First choicebox item is selected by default
        searchOptions.getSelectionModel().selectFirst();

        // create a list of buttons to be used as an argument for method allButtonOnEnter
        ArrayList<Button> buttonList = new ArrayList<>();
        buttonList.add(addButton);
        buttonList.add(deleteButton);
        buttonList.add(editButton);
        buttonList.add(searchButton);
        buttonList.add(vehiclesButton);
        buttonList.add(pastBookingsButton);
        buttonList.add(futureBookingsButton);

        // allows hitting enter on a button which will trigger the buttons onAction method
        allButtonOnEnter(buttonList);

        // set user data, will be useful later on to determine which is selected
        privateRadio.setUserData("Private");
        businessRadio.setUserData("Business");

        // set user data, will be useful later on to determine which is selected
        privateFilter.setUserData("filterPrivate");
        businessFilter.setUserData("filterBusiness");
        allFilter.setUserData("filterAll");

        // do not allow columns to be wider than table width
        customersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // ensures only one row can be selected in the table
        customersTable.getSelectionModel().setSelectionMode(
                SelectionMode.SINGLE
        );

        // set cell value so the setItems method used later will work correctly
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postcodeCol.setCellValueFactory(new PropertyValueFactory<>("postcode"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailAddressCol.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));

        data = FXCollections.observableArrayList();
        dataFiltered = FXCollections.observableArrayList();

        // change the data in the table based on radio buttons which will filter the data
        customerTypeFilter.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
//                System.out.println("old: "+old_toggle.getUserData());
//                System.out.println("new: "+new_toggle.getUserData());
                if (new_toggle.getUserData().equals("filterAll")) {
//                    loadAllCustomers();
                    customersTable.setItems(data);
                } else if (new_toggle.getUserData().equals("filterBusiness")) {
                    dataFiltered = filterData("business");
                    customersTable.setItems(dataFiltered);
                } else if (new_toggle.getUserData().equals("filterPrivate")) {
                    dataFiltered = filterData("private");
                    customersTable.setItems(dataFiltered);
                }
                setRowsLabel();
            }

            private ObservableList<Customer> filterData(String type) {
                Iterator itr = data.iterator();
                ObservableList<customers.Customer> filtered = FXCollections.observableArrayList();
                while (itr.hasNext()) {
                    Customer c = (Customer) itr.next();
                    String custType = c.getType();
                    if (type.equals("business") && custType.equals("Business")) {
                        filtered.add(c);
                    } else if (type.equals("private") && custType.equals("Private")) {
                        filtered.add(c);
                    }
                }
                return filtered;
            }
        });

        boolean dataLoaded = loadAllCustomers();
        if (!dataLoaded) {
            showError("There was an error attempting to load the data.");
        }

    }

    // print label below table showing number of customers
    public int getRowsCustomerTable() {
        int numRows = -1;
        try {
            ResultSet rowCount = db.query("SELECT COUNT(*) FROM Customer;");
            numRows = rowCount.getInt(1);
        } catch (SQLException ex) {
            System.out.println(ex.getClass() + ": " + ex.getMessage());
            return numRows;
        }
        return numRows;
    }

    // print label below table showing number of customers present in the table
    public void setRowsLabel() {
        int numRowsInTable = customersTable.getItems().size();
        tableInfo.setText(tInfo + " " + numRowsInTable);

    }

    //load data into table based on sql statement
    public boolean loadData(String sqlStatement) {
        Statement stmt = null;
        try {
            boolean connected = db.connect();
            ResultSet rs = db.query(sqlStatement);
            data.clear();
            while (rs.next()) {
                data.add(new Customer(Integer.parseInt(rs.getString("CustomerID")),
                        rs.getString("CustomerFirstName"), rs.getString("CustomerLastName"),
                        rs.getString("CustomerType"), rs.getString("CustomerAddress"),
                        rs.getString("CustomerPostcode"), rs.getString("CustomerPhoneNo"),
                        rs.getString("CustomerEmail")));
            }
            customersTable.setItems(data);
            setRowsLabel();
        } catch (SQLException ex) {
            System.out.println(ex.getClass() + ": " + ex.getMessage());
            return false;
        }
        db.closeConnection();
        return true;

    }

    // load all customers into the table
    public boolean loadAllCustomers() {
        boolean loadAll = loadData("SELECT * FROM Customer;");
        selectAllToggle();
        clearSearchField();
        return loadAll;
    }

    /*
     * ******************************
     * CLEAR THE TABLE OF ALL DATA  *
     * ******************************
     */
    public void clearTable() {
        data.clear();
        selectAllToggle();
        setRowsLabel();
    }

    /*
     * ********************************
     * ADD A CUSTOMER TO THE DATABASE *
     * ********************************
     */
    public void addCustomer() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String postcode = postcodeField.getText();
        String phoneNumber = phoneNumberField.getText();
        String emailAddress = emailAddressField.getText();
        String type = (String) customerType.getSelectedToggle().getUserData();

        boolean passCheck = checkTextFieldsAreFilledBeforeAdding();
        if (!passCheck) {
            return;
        }
        boolean passCheck2 = checkTextFieldsAreValidBeforeAdding();
        if (!passCheck2) {
            return;
        }

//        String sqlStatement = "INSERT INTO Customer (CustomerID,CustomerFirstName,CustomerLastName,CustomerType,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail)"
//                + " VALUES ('" + rowNumber + "','" + firstName + "','" + lastName + "','" + type + "','" + address + "','" + postcode + "','" + phoneNumber + "','" + emailAddress + "');";
        String sqlStatement = "INSERT INTO Customer (CustomerFirstName,CustomerLastName,CustomerType,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail)"
                + " VALUES ('" + firstName + "','" + lastName + "','" + type + "','" + address + "','" + postcode + "','" + phoneNumber + "','" + emailAddress + "');";

        db.connect();
        try {
            db.update(sqlStatement);
        } catch (SQLException ex) {
//            Logger.getLogger(CustomersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.closeConnection();

        clearTextFieldsForAddingCustomer();
        hideRequiredFieldLabels();

        loadAllCustomers();
    }

    // ensure there are no empty text fields when adding a new customer
    public boolean checkTextFieldsAreFilledBeforeAdding() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String postcode = postcodeField.getText();
        String phoneNumber = phoneNumberField.getText();
        String emailAddress = emailAddressField.getText();

        boolean atLeastOneFieldEmpty = false;
        hideRequiredFieldLabels();

        if (firstName.equals("")) {
            fieldLabel1.setText("The first name field is required.");
            fieldLabel1.setVisible(true);
            atLeastOneFieldEmpty = true;
        }
        if (lastName.equals("")) {
            fieldLabel2.setText("The last name field is required.");
            fieldLabel2.setVisible(true);
            atLeastOneFieldEmpty = true;
        }
        if (address.equals("")) {
            fieldLabel3.setText("The address field is required.");
            fieldLabel3.setVisible(true);
            atLeastOneFieldEmpty = true;
        }
        if (postcode.equals("")) {
            fieldLabel4.setText("The postcode field is required.");
            fieldLabel4.setVisible(true);
            atLeastOneFieldEmpty = true;
        }
        if (phoneNumber.equals("")) {
            fieldLabel5.setText("The phone number field is required.");
            fieldLabel5.setVisible(true);
            atLeastOneFieldEmpty = true;
        }
        if (emailAddress.equals("")) {
            fieldLabel6.setText("The email address field is required.");
            fieldLabel6.setVisible(true);
            atLeastOneFieldEmpty = true;
        }

        if (atLeastOneFieldEmpty) {
            showInformationBox("Add Customer", "Ensure all required fields are filled in before attempting to add a new customer.");
            return false;
        }
        return true;
    }

    /*
     * *****************************************
     * CHECK THE TEXT FIELDS HAVE VALID INPUTS *
     * *****************************************
     */
    private boolean checkTextFieldsAreValidBeforeAdding() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String postcode = postcodeField.getText();
        String phoneNumber = phoneNumberField.getText();
        String emailAddress = emailAddressField.getText();

        String regex_letttersOnly = "[A-Za-z']+";
        String regex_numbersOnly = "[0-9]+";
        String regex_address = "[A-Za-z0-9 ]+";
        String regex_postcode = "[A-Za-z0-9]{2,4} [A-Za-z0-9]{3,3}";
        String regex_email = "[A-Za-z0-9._%+-]+@[A-Za-z0-9._%+-]+.[A-Za-z0-9.-]{2,}";

        String validName = "Please enter only letters for this field";
        String validNumber = "Please enter only numbers for this field";
        String validAddress = "Please enter a valid address for this field";
        String validPostcode = "Please enter a valid postocde (with a space included) for this field";
        String validEmail = "Please enter a valid email address for this field";

        boolean atLeastOneInvalidField = false;

        if (!firstName.matches(regex_letttersOnly)) {
            fieldLabel1.setText(validName);
            fieldLabel1.setVisible(true);
            atLeastOneInvalidField = true;
        }
        if (!lastName.matches(regex_letttersOnly)) {
            fieldLabel2.setText(validName);
            fieldLabel2.setVisible(true);
            atLeastOneInvalidField = true;
        }
        if (!address.matches(regex_address)) {
            fieldLabel3.setText(validAddress);
            fieldLabel3.setVisible(true);
            atLeastOneInvalidField = true;
        }
        if (!postcode.matches(regex_postcode)) {
            fieldLabel4.setText(validPostcode);
            fieldLabel4.setVisible(true);
            atLeastOneInvalidField = true;
        }
        if (!phoneNumber.matches(regex_numbersOnly)) {
            fieldLabel5.setText(validNumber);
            fieldLabel5.setVisible(true);
            atLeastOneInvalidField = true;
        }
        if (!emailAddress.matches(regex_email)) {
            fieldLabel6.setText(validEmail);
            fieldLabel6.setVisible(true);
            atLeastOneInvalidField = true;
        }

        if (atLeastOneInvalidField) {
            showInformationBox("Add Customer", "Ensure all required fields have valid inputs before attempting to add a new customer.");
            return false;
        }
        return true;
    }

    /*
     * **************************************************
     * CLEAR THE TEXT FIELDS USED TO ADD A NEW CUSTOMER *
     * **************************************************
     */
    public void clearTextFieldsForAddingCustomer() {
        ArrayList<TextInputControl> textFields = new ArrayList<>();
        textFields.add(firstNameField);
        textFields.add(lastNameField);
        textFields.add(addressField);
        textFields.add(postcodeField);
        textFields.add(phoneNumberField);
        textFields.add(emailAddressField);
        for (TextInputControl textInput : textFields) {
            textInput.clear();
        }
    }

    /*
     * ***************************************************
     * HIDE THE LABELS USED TO INDICATE REQUIRED FIELDS *
     * ***************************************************
     */
    public void hideRequiredFieldLabels() {
        ArrayList<Label> labels = new ArrayList<>();
        labels.add(fieldLabel1);
        labels.add(fieldLabel2);
        labels.add(fieldLabel3);
        labels.add(fieldLabel4);
        labels.add(fieldLabel5);
        labels.add(fieldLabel6);
        for (Label l : labels) {
            l.setVisible(false);
        }

    }

    /*
     * *******************************************
     * EDIT AN EXISTING CUSTOMER IN THE DATABASE *
     * *******************************************
     */
    public void editCustomer() {
        ObservableList<Customer> selectedCustomers = customersTable.getSelectionModel().getSelectedItems();
        if (selectedCustomers.isEmpty()) {
            showInformationBox("Edit", "Please select a customer before attempting to edit.");
            return;
        }
        Customer customerToEdit = selectedCustomers.get(0);
        try {
            showEditCustomerScreen(customerToEdit);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
     * ***********************************
     * DELETE CUSTOMER FROM THE DATABASE *
     * ***********************************
     */
    public boolean deleteCustomer() {
        ObservableList<Customer> selectedCustomers = customersTable.getSelectionModel().getSelectedItems();
        if (selectedCustomers.isEmpty()) {
            return false;
        }
        boolean continueWithDelete;
        String message = "Are you sure you want to delete the customer: \n";

        String cFirstName = selectedCustomers.get(0).getFirstName();
        String cLastName = selectedCustomers.get(0).getLastName();

        continueWithDelete = showConfirmation("Warning", message + cFirstName + " " + cLastName + ".");
        if (!continueWithDelete) {
            return false;
        }

        db.connect();
        boolean allBillsSettled = true;
        int rows = 0;
        try {
            ResultSet r = db.query("SELECT Customer.*, Booking.*, Bill.* FROM Customer JOIN Booking on Customer.CustomerID=Booking.CustomerID JOIN Bill On Booking.BookingID=Bill.BookingID WHERE Customer.CustomerID=" + selectedCustomers.get(0).getId() + ";");
            while (r.next()) {
                rows++;
                int i = r.getInt("IsSettled");
//                System.out.println(i);
                if (i == 0) {
                    allBillsSettled = false;
                }
            }
        } catch (SQLException ex) {
//            System.out.println(ex.getClass()+" "+ex.getMessage());
        }
        if(rows==0){
        
        }else if (!allBillsSettled) {
            showInformationBox("Cannot Delete Customer", "Cannot delete the customer as they have unsettled bills.");
            return false;
        }

        for (Customer c : selectedCustomers) {
            int customerID = c.getId();
            try {
                db.update("DELETE FROM Customer WHERE CustomerID=" + customerID + ";");
            } catch (SQLException ex) {
                Logger.getLogger(CustomersScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
            db.closeConnection();
        }
        loadAllCustomers();
        return true;
    }

    /*
     * ***********************************
     * SEARCH FOR CUSTOMER(S) FROM THE   *
     * DATABASE AND DISPLAY IN THE TABLE *
     * ***********************************
     */
    public void search() {
        String searchString = searchField.getText();
        String searchBy = (String) searchOptions.getSelectionModel().getSelectedItem();
        allFilter.fire();

        // Check if selection was made on ChoiceBox
        if (searchString.equals("")) {
            showInformationBox("Search", "You must enter a search string before searching.");
            return;
        } else if (searchBy == null) {
            showInformationBox("Search", "Select an option (from the dropdown) to search against.");
            return;
        }

        String whereKey = "";

//        String andKey = "";
//        String type = (String) customerTypeFilter.getSelectedToggle().getUserData();
//        if (type.equals("filterPrivate")) {
//            andKey = "AND Customer.CustomerType='Private';";
//        } else if (type.equals("filterBusiness")) {
//            andKey = "AND Customer.CustomerType='Business';";
//        }
        int stringToUse = -1;
        switch (searchBy) {
            case "First Name":
                whereKey = "CustomerFirstName";
                stringToUse = 0;
                break;
            case "Last Name":
                whereKey = "CustomerLastName";
                stringToUse = 0;
                break;
            case "Vehicle Registration Number":
                whereKey = "VehicleRegNo";
                stringToUse = 1;
                break;
        }
        String sqlString = "Select * from Customer Where " + whereKey + " Like '%" + searchString + "%'";
        String sqlString2 = "Select Customer.*, Vehicle.VehicleRegNo FROM Customer JOIN Vehicle ON Customer.CustomerID=Vehicle.CustomerID"
                + " Where " + whereKey + " Like '%" + searchString + "%'";
        String[] sqlStrings = new String[]{sqlString, sqlString2};

        boolean searchSuccessfull = loadData(sqlStrings[stringToUse]);
        if (searchSuccessfull) {
            if (!data.isEmpty()) {
//                searchField.clear();
            }
        }
    }

    public void clearSearchField() {
        searchField.clear();
    }

    /*
     * ***********************************
     * VIEW THE BOOKINGS FOR A SELECTED  *
     * CUSTOMER                          *
     * ***********************************
     */
    public void viewBookings(ActionEvent e) {
        Button btn = (Button) e.getSource();
//        System.out.println(btn.getId());

        ObservableList<Customer> selectedCustomers = customersTable.getSelectionModel().getSelectedItems();
        if (selectedCustomers.isEmpty()) {
            showInformationBox("Select a Customer", "Please select a customer before attempting to view bookings.");
            return;
        }

        int pastOrFuture;
        if (btn.getId().equals("pastBookingsButton")) {
            pastOrFuture = 0;
        } else {
            pastOrFuture = 1;
        }

//        Date dateNow = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/y HH:mm");
//        System.out.println(df.format(dateNow));
        try {
            showViewBookingsScreen(selectedCustomers.get(0), pastOrFuture);
        } catch (IOException ex) {
            Logger.getLogger(CustomersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
     * **********************************
     * VIEW THE VEHICLES OF A SELECTED  *
     * CUSTOMER                         *
     * **********************************
     */
    public void viewVehicles(ActionEvent e) {
        ObservableList<Customer> selectedCustomers = customersTable.getSelectionModel().getSelectedItems();
        if (selectedCustomers.isEmpty()) {
            showInformationBox("Select a Customer", "Please select a customer before attempting to view vehicles.");
            return;
        }

        try {
            showViewVehiclesScreen(selectedCustomers.get(0));
        } catch (IOException ex) {
            Logger.getLogger(CustomersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * **********************************
     * VIEW THE VEHICLES OF A SELECTED  *
     * CUSTOMER                         *
     * **********************************
     */
    public void viewBills(ActionEvent e) {
        ObservableList<Customer> selectedCustomers = customersTable.getSelectionModel().getSelectedItems();
        if (selectedCustomers.isEmpty()) {
            showInformationBox("Select a Customer", "Please select a customer before attempting to view their bills.");
            return;
        }

        try {
            showViewBillsScreen(selectedCustomers.get(0));
        } catch (IOException ex) {
            Logger.getLogger(CustomersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * **********************************
     * INITIATES THE CREATION OF A NEW  *
     * BOOKING FOR A SELECTED CUSTOMER  *
     * **********************************
     */
    public void newBooking() throws IOException {
        ObservableList<Customer> selectedCustomers = customersTable.getSelectionModel().getSelectedItems();
        if (selectedCustomers.isEmpty()) {
            showInformationBox("Select a Customer", "Please select a customer before attempting to add a new booking.");
            return;
        }
        Customer cust = selectedCustomers.get(0);

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../diagrep/gui/AddWindow.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addWindow.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AddWindowController controllerA = fxmlLoader.<AddWindowController>getController();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Add New Diagnosis/Repair Booking");
        controllerA.setEntryCustomer(cust.getId() - 1);
        stage.show();

    }

    /*
     * *****************************************************
     * ALLOWS HITTING ENTER WHILE A BUTTON HAS FOCUS       *
     * DOING SO WILL TRIGGER THE BUTTONS ON ACTION METHOD  *
     * *****************************************************
     */
    private void allButtonOnEnter(ArrayList<Button> buttonList) {
        for (Iterator<Button> iterator = buttonList.iterator(); iterator.hasNext();) {
            final Button next = iterator.next();
            next.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        EventHandler eH = next.getOnAction();
                        eH.handle(ke);
                    }
                }
            });

        }
    }

    /*
     * *******************************************
     * USED TO DISPLAY A DIALOG BOX WITH A GIVEN *
     * STRING AS THE MESSAGE DISPLAYED           *
    **********************************************
     */
    public void showInformationBox(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
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
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType yesButton = new ButtonType("Yes", ButtonData.YES);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

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

    /*
     * **************************
     * USED TO DISPLAY AN ERROR *
    *****************************
     */
    public void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showEditCustomerScreen(Customer customerToEdit) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditCustomerScreen.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Edit Customer");
        stage.initModality(Modality.APPLICATION_MODAL);
        EditCustomerScreenController ecsc = loader.getController();
        ecsc.setCustomer(customerToEdit);
        ecsc.setParentController(this);
        stage.show();
    }

    private void showViewBookingsScreen(Customer customerToEdit, int pastOrFuture) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewBookingsScreen.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("View Customer Bookings");
        stage.initModality(Modality.APPLICATION_MODAL);
        ViewBookingsScreenController vbsc = loader.getController();
        vbsc.setCustomer(customerToEdit);
        vbsc.setPastOrFuture(pastOrFuture);
        stage.show();
    }

    private void showViewVehiclesScreen(Customer customerToView) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewVehiclesScreen.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("View Customer Vehicles");
        stage.initModality(Modality.APPLICATION_MODAL);
        ViewVehiclesScreenController vvsc = loader.getController();
        vvsc.setCustomer(customerToView);
        stage.show();
    }

    private void showViewBillsScreen(Customer customerToView) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewBillsScreen.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("View Customer Bills");
        stage.initModality(Modality.APPLICATION_MODAL);
        ViewBillsScreenController vbsc = loader.getController();
        vbsc.setCustomer(customerToView);
        stage.show();
    }

    // select the radio button 'all'
    public void selectAllToggle() {
        allFilter.fire();
    }

}
