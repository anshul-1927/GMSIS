package common;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminScreenController implements Initializable {

    @FXML
    private ToggleGroup group;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField password;
    @FXML
    private TextField search;
    @FXML
    private ChoiceBox searchOpt;
    @FXML
    private RadioButton standard;
    @FXML
    private RadioButton admin;
    @FXML
    private Button editButton;
    @FXML
    private TableView<SystemUser> adminTable;
    @FXML
    private TableColumn<SystemUser, String> userIDCol;
    @FXML
    private TableColumn<SystemUser, String> nameCol;
    @FXML
    private TableColumn<SystemUser, String> surnameCol;
    @FXML
    private TableColumn<SystemUser, String> passwordCol;
    @FXML
    private TableColumn<SystemUser, String> isAdminCol;

    @FXML
    private Label tableInfo;

    private Database db;
    private ObservableList<SystemUser> systemUsers;
    private boolean editMode = false;
    private SystemUser entry;

    final private String tInfo = "Number of rows:";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        db = db.getInstance();
        searchOpt.setItems(FXCollections.observableArrayList("First Name", "Last Name"));
        searchOpt.getSelectionModel().selectFirst();
        group = new ToggleGroup();
        standard.setToggleGroup(group);
        admin.setToggleGroup(group);

        userIDCol.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("surname"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("password"));
        isAdminCol.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("isAdmin"));

        outputTableData(null);

    }

    private void outputTableData(String sql) {

        systemUsers = FXCollections.observableArrayList();
        systemUsers.clear();

        if (sql == null) {

            sql = "SELECT UserID, UserFirstName, UserLastName, UserPassword, UserIsAdmin "
                    + " FROM User; ";

        }

        ResultSet rs;

        try {

            db.connect();
            boolean isAdmin;
            rs = db.query(sql);
            while (rs.next()) {
                isAdmin = (rs.getInt("UserIsAdmin") == 1) ? true : false;
                systemUsers.add(new SystemUser(rs.getString("UserFirstName"), rs.getString("UserLastName"), rs.getString("UserID"), rs.getString("UserPassword"), isAdmin));
            }
            db.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        adminTable.setItems(systemUsers);
        clearAddFields();
        setRowsLabel();

    }

    @FXML
    public void addSystemUser() {

        String nameField;
        String surnameField;
        String passwordField;
        int isAdmin = 0;

        if (name.getText() == null || name.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Fill in name details!");
            alert.showAndWait();
            return;
        }
        if (surname.getText() == null || surname.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Fill in surname!");
            alert.showAndWait();
            return;
        }
        if (password.getText() == null || password.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Fill in password details!");
            alert.showAndWait();
            return;
        }

        if (group.getSelectedToggle() == admin) {
            isAdmin = 1;
        } else {
            isAdmin = 0;
        }

        nameField = name.getText();
        surnameField = surname.getText();
        passwordField = password.getText();

        String sql = "INSERT INTO `User` (UserFirstName, UserLastName, UserPassword, UserIsAdmin)"
                + " VALUES ('" + nameField + "','" + surnameField + "','" + passwordField + "'," + isAdmin + ");";
        db.connect();
        try {
            db.update(sql);
        } catch (SQLException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Database connection failed!");

            alert.showAndWait();
        }
        db.closeConnection();

        outputTableData(null);
    }

    @FXML
    public void editSystemUser() {
        if (!editMode) {
            entry = adminTable.getSelectionModel().getSelectedItem();
            if (entry == null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Edit Error");
                alert.setContentText("Please select a user to edit.");
                alert.showAndWait();
                return;
            }
            name.setText(entry.getName());
            surname.setText(entry.getSurname());
            password.setText(entry.getPassword());
            if (entry.getIsAdmin().equals("Admin")) {
                group.selectToggle(admin);
            } else {
                group.selectToggle(standard);
            }
            editButton.setText("Confirm Edit");
            editMode = true;
        } else {
            if (adminTable.getSelectionModel().getSelectedItem() != entry) {
                Alert alert2 = new Alert(AlertType.ERROR);
                alert2.setTitle("Edit Error");
                alert2.setContentText("Selected user has changed. Edit operation aborted.");
                alert2.showAndWait();
                clearAddFields();
                editButton.setText("Edit User");
                editMode = false;
                return;
            }
            String userType = (group.getSelectedToggle() == admin) ? "1" : "0";
            String sql = "UPDATE User SET UserFirstName='" + name.getText() + "', UserLastName='" + surname.getText() + "', UserPassword='" + password.getText() + "', UserIsAdmin='" + userType + "' "
                    + "WHERE UserID ='" + entry.getID() + "';";
            try {
                db.connect();
                db.update(sql);
                db.closeConnection();
            } catch (SQLException se) {
                Alert alert3 = new Alert(AlertType.ERROR);
                alert3.setTitle("Connection Error");
                alert3.setContentText("Error connecting to the SQL database.");
                alert3.showAndWait();
            }
            clearAddFields();
            editButton.setText("Edit User");
            editMode = false;
            outputTableData(null);
        }
    }

    public void editUser() {
        ObservableList<SystemUser> selectedUsers = adminTable.getSelectionModel().getSelectedItems();
        if (selectedUsers.isEmpty()) {
            showInformationBox("Edit", "Please select a  system user before attempting to edit.");
            return;
        }
        SystemUser userToEdit = selectedUsers.get(0);
        try {
            showEditUserScreen(userToEdit);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showEditUserScreen(SystemUser userToEdit) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditUserScreen.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Edit User");
        stage.initModality(Modality.APPLICATION_MODAL);
        EditUserScreenController eusc = loader.getController();
        eusc.setUser(userToEdit);
        eusc.setParentController(this);
        stage.show();
    }

    @FXML
    public void deleteSystemUser() {
        ObservableList<SystemUser> selectedUsers = adminTable.getSelectionModel().getSelectedItems();
        if (selectedUsers.isEmpty()) {
            return;
        }
        SystemUser userToDelete = selectedUsers.get(0);

        boolean confirm = false;
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the user: \n" + userToDelete.getID() + ", " + userToDelete.getName() + ", " + userToDelete.getSurname());

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != yesButton) {
            // ... user chose Yes
            return;
        }

        db.connect();
        for (SystemUser su : selectedUsers) {
            String userID = su.getID();
            try {
                db.update("DELETE FROM User WHERE UserID=" + userID + ";");
            } catch (SQLException ex) {
                System.out.println(ex.getClass() + " " + ex.getMessage());
            }
            db.closeConnection();
        }
        outputTableData(null);
    }

    @FXML
    public void handleSearch(ActionEvent Event) {
        String searchFor = search.getText();
        String searchBy = (String) searchOpt.getSelectionModel().getSelectedItem();

        if (searchBy == null) {
            showInformationBox("Search", "Select an option (from the dropdown) to search against.");
            return;
        } else if (searchFor.equals("")) {
            showInformationBox("Search", "You must enter a search string before searching.");
            return;
        }

        String sql = "";
        switch (searchBy) {
            case "First Name":
                sql = "SELECT * FROM USER WHERE UserFirstName LIKE '%" + searchFor + "%';";
                break;
            case "Last Name":
                sql = "SELECT * FROM USER WHERE UserLastName LIKE '%" + searchFor + "%';";
                break;

        }
        outputTableData(sql);
    }

    private void clearAddFields() {
        name.clear();
        surname.clear();
        password.clear();

    }

    public void displaySPC() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("spcScreen.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Specialist Repair Company Details");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(LoginScreenController.stage);
        stage.show();
    }

    public void refresh() {
        search.clear();
        outputTableData(null);
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

    public void setRowsLabel() {
        int numRowsInTable = adminTable.getItems().size();
        tableInfo.setText(tInfo + " " + numRowsInTable);

    }
    
        /*
     * ******************************
     * CLEAR THE TABLE OF ALL DATA  *
     * ******************************
     */
    public void clearTable() {
        systemUsers.clear();
        setRowsLabel();
    }
}
