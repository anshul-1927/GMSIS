/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import customers.gui.*;
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

public class EditUserScreenController implements Initializable {

    private SystemUser systemUser;

    @FXML
    private TextField idField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField passwordField;

    @FXML
    private ToggleGroup userType;
    @FXML
    private RadioButton adminRadio;
    @FXML
    private RadioButton standardRadio;
    @FXML
    private Button cancelButton;

    Database db = Database.getInstance();
    AdminScreenController parent;

    private boolean changed;
    private boolean saved;
    private boolean firstLoad = true;
    private ToggleGroup radios;

    public void setParentController(AdminScreenController controller) {
        parent = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	    radios = new ToggleGroup();
	    adminRadio.setToggleGroup(userType);
	    standardRadio.setToggleGroup(userType);
        adminRadio.setUserData("Admin");
        standardRadio.setUserData("Standard");

        idField.setDisable(true);
        idField.setStyle("-fx-opacity: 0.5");

        //add listener to all fields so when a textField is changed the boolean value 'changed' is updated
        addListenerToAllFields();

        //add listener to the toggle group which includes both radio buttons
        userType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
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
        cancelButton.requestFocus();
    } // END INITIALIZE METHOD

    public void addListenerToAllFields() {
        ArrayList<TextField> textFields = new ArrayList<>();
        textFields.add(firstNameField);
        textFields.add(lastNameField);
        textFields.add(passwordField);
        textFields.add(idField);

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

    public void setUser(SystemUser su) {
        this.systemUser = su;

        idField.setText(systemUser.getID());
        firstNameField.setText(systemUser.getName());
        lastNameField.setText(systemUser.getSurname());
        passwordField.setText(systemUser.getPassword());

        String userType = systemUser.getIsAdmin();
        if (userType.equals("Admin")) {
            adminRadio.fire();
        } else {
            standardRadio.fire();
        }
    }

    @FXML
    private void saveUser() {
        String id = idField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String password = passwordField.getText();
        String type = (String) userType.getSelectedToggle().getUserData();
        int isAdmin = (type.equals("Admin")) ? 1 : 0;
        
        if (firstName.equals("")
                || lastName.equals("")
                || password.equals("")
                || id.equals("")) {
            showInformationBox("Save User Details", "Ensure there are no empty fields before attempting to save.");
            return;
        }
        String sqlStatement = "UPDATE USER SET UserFirstName='" + firstName + "',UserLastName='" + lastName + "',UserPassword='"+ password +"',UserIsAdmin=" + isAdmin + " WHERE UserID=" + systemUser.getID() + ";";
        db.connect();

        try {
            db.update(sqlStatement);
        } catch (SQLException ex) {
            Logger.getLogger(EditUserScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.closeConnection();

        saved = true;
        parent.refresh();
    }

    public void saveUserAndClose() {
        saveUser();
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
