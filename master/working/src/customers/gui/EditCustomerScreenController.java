/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.gui;

import common.Database;
import customers.Customer;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class EditCustomerScreenController implements Initializable {

    private Customer customer;

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
    @FXML
    private ToggleGroup customerType;
    @FXML
    private RadioButton privateRadio;
    @FXML
    private RadioButton businessRadio;
    @FXML
    private Button cancelButton;

    Database db = Database.getInstance();
    CustomersScreenController parent;
    private boolean changed;
    private boolean saved;
    private boolean firstLoad = true;

    public void setParentController(CustomersScreenController controller) {
        parent = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        privateRadio.setUserData("Private");
        businessRadio.setUserData("Business");

        //add listener to all fields so when a textField is changed the boolean value 'changed' is updated
        addListenerToAllFields();

        //add listener to the toggle group which includes both radio buttons
        customerType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (!firstLoad) {
                    if (old_toggle != new_toggle) {
//                        System.out.println("Changed to toggle to: " + new_toggle.getUserData());
                        changed = true;
                    }
                }
                firstLoad = false;
            }
        });

    } // END INITIALIZE METHOD

    public void addListenerToAllFields() {
        ArrayList<TextField> textFields = new ArrayList<>();
        textFields.add(firstNameField);
        textFields.add(lastNameField);
        textFields.add(addressField);
        textFields.add(postcodeField);
        textFields.add(phoneNumberField);
        textFields.add(emailAddressField);

        for (final TextField field : textFields) {
            field.textProperty().addListener(new ChangeListener<String>() {

                // method to handle what happens when any of the text fields are changed
                @Override
                public void changed(ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                    if (!firstLoad) {
                        if (!oldValue.equals(newValue)) {
//                            System.out.println("Changed text field w/ id: " + field.getId());
                            changed = true;
                        }
                    }
                }
            });
        }
    }

    public void setCustomer(Customer c) {
        this.customer = c;

        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        addressField.setText(customer.getAddress());
        postcodeField.setText(customer.getPostcode());
        phoneNumberField.setText(customer.getPhoneNumber());
        emailAddressField.setText(customer.getEmailAddress());

        String custType = customer.getType();
        if (custType.equalsIgnoreCase("Private")) {
            privateRadio.fire();
        } else {
            businessRadio.fire();
        }
    }

    @FXML
    private void saveCustomer() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String postcode = postcodeField.getText();
        String phoneNumber = phoneNumberField.getText();
        String emailAddress = emailAddressField.getText();
        String type = (String) customerType.getSelectedToggle().getUserData();

        if (firstName.equals("")
                || lastName.equals("")
                || address.equals("")
                || postcode.equals("")
                || phoneNumber.equals("")
                || emailAddress.equals("")) {
            showInformationBox("Save Cutomer Details", "Ensure there are no empty fields before attempting to save.");
            return;
        }

        String regex_letttersOnly = "[A-Za-z']+";
        String regex_numbersOnly = "[0-9]+";
        String regex_address = "[A-Za-z0-9 ]+";
        String regex_postcode = "[A-Za-z0-9]{2,4} [A-Za-z0-9]{3,3}";
        String regex_email = "[A-Za-z0-9._%+-]+@[A-Za-z0-9._%+-]+[.]{1,1}[A-Za-z0-9]{2,}";

        String validName = "Please enter only letters for this field";
        String validNumber = "Please enter only numbers for this field";
        String validAddress = "Please enter a valid address for this field";
        String validPostcode = "Please enter a valid postocde (with a space included) for this field";
        String validEmail = "Please enter a valid email address for this field";

        boolean atLeastOneInvalidField = false;

        if (!firstName.matches(regex_letttersOnly)) {
            atLeastOneInvalidField = true;
        }
        if (!lastName.matches(regex_letttersOnly)) {
            atLeastOneInvalidField = true;
        }
        if (!address.matches(regex_address)) {
            atLeastOneInvalidField = true;
        }
        if (!postcode.matches(regex_postcode)) {
            atLeastOneInvalidField = true;
        }
        if (!phoneNumber.matches(regex_numbersOnly)) {
            atLeastOneInvalidField = true;
        }
        if (!emailAddress.matches(regex_email)) {
            atLeastOneInvalidField = true;
        }

        if (atLeastOneInvalidField) {
            showInformationBox("Edit Customer", "Ensure all fields are valid. \n\nName field must only contain letters. \nNumber must only contain numerical values. \nPost code must match: {2-4 characters}[space]{3 characters}. \nEmail must be in the form: name@domain.");
            return;
        }

        String sqlStatement = "UPDATE Customer SET CustomerFirstName='" + firstName + "',CustomerLastName='" + lastName + "',CustomerType='" + type
                + "',CustomerAddress='" + address + "',CustomerPostcode='" + postcode + "',CustomerPhoneNo='" + phoneNumber + "',CustomerEmail='" + emailAddress + "' "
                + "WHERE CustomerID=" + customer.getId() + ";";
        db.connect();

        try {
            db.update(sqlStatement);
        } catch (SQLException ex) {
            Logger.getLogger(EditCustomerScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.closeConnection();

        saved = true;
        parent.loadAllCustomers();
    }

    public void saveAndClose() {
        saveCustomer();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void cancel() {
        boolean close;
        if (!saved && !changed) {
            close = true;
        } else if (saved) {
            close = true;
        } else {
            close = closeConfirmation("Warning", "You are about to close without saving your changes.");
        }

        if (close) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    /*
     * ************************************************
     * USED TO DISPLAY A CONFIRMATION BOX             *
     * THE USER IS EXPECTED TO MAKE A YES/NO DECISION *
    ***************************************************
     */
    public boolean closeConfirmation(String title, String message) {
        boolean closeWihtoutSave = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType yesButton = new ButtonType("Close without saving", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) {
            closeWihtoutSave = true;
            return closeWihtoutSave;
        } else {
            return closeWihtoutSave;
        }
    }

    public void showInformationBox(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
