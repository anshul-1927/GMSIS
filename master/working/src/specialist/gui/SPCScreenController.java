/**
 * FXML Controller class
 *
 * @author Lloyd
 */

package specialist.gui;

import common.Database;
import specialist.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SPCScreenController implements Initializable{
    
    @FXML private Button closeButton, updateButton, addButton, deleteButton;
    @FXML private TextField nameTf, addressTf, phoneTf, emailTf;
    @FXML private TableView<SPC> table;
    @FXML private TableColumn<specialist.SPC, String> nameCol, addressCol, phoneCol, emailCol, idCol, inUseCol;
    @FXML private ObservableList<SPC> spcList;
    @FXML private CheckBox inUseBox;
    private int inUseCount;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        spcList = FXCollections.observableArrayList();
        idCol.setCellValueFactory(new PropertyValueFactory<specialist.SPC, String>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<specialist.SPC, String>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<specialist.SPC, String>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<specialist.SPC, String>("phoneNo"));
        emailCol.setCellValueFactory(new PropertyValueFactory<specialist.SPC, String>("email"));
        inUseCol.setCellValueFactory(new PropertyValueFactory<specialist.SPC, String>("inUse"));
        
        fillTable();
    }
    
    public void fillTable(){
        inUseCount = 0;
        spcList.clear();
        String sql = "SELECT SPCID, SPCName, SPCAddress, SPCPhoneNo, SPCEmail, SPCInUse FROM SPC";
        try{
            Database.getInstance().connect();
            ResultSet rs = Database.getInstance().query(sql);
            while(rs.next()){
                spcList.add(new SPC(rs.getString("SPCID"), rs.getString("SPCName"), rs.getString("SPCAddress"),rs.getString("SPCPhoneNo"),rs.getString("SPCEmail"), rs.getInt("SPCInUse")));
                if(rs.getInt("SPCInUse")==1){
                    inUseCount++;
                }
            }
            Database.getInstance().closeConnection();
        }catch(SQLException e){
            System.err.println(e);
        }
        table.setItems(spcList);
    } 
    
    public void fillTextFields(){
        nameTf.setText(table.getSelectionModel().getSelectedItem().getName());
        addressTf.setText(table.getSelectionModel().getSelectedItem().getAddress());
        phoneTf.setText(table.getSelectionModel().getSelectedItem().getPhoneNo());
        emailTf.setText(table.getSelectionModel().getSelectedItem().getEmail());
        if(table.getSelectionModel().getSelectedItem().getInUse()==1){
            inUseBox.setSelected(true);
        }
        else{
            inUseBox.setSelected(false);
        }
    }
        
    public void refreshPage(){
        nameTf.setText(null);
        addressTf.setText(null);
        phoneTf.setText(null);
        emailTf.setText(null);
        inUseBox.setSelected(false);
        fillTable();
    }
    
    public void closeWindow(){
        if(inUseCount != 10){
            errorAlert("Garage must only have 10 SPC's in use. Currently at: "+inUseCount);
        }else{
            Stage buttonStage = (Stage) closeButton.getScene().getWindow();
            buttonStage.close();
        }
    }   
    
    public void addSPC(){
        if(nameTf.getText().equals("") || addressTf.getText().equals("") || phoneTf.getText().equals("") || emailTf.getText().equals("") || emailTf.getText().equals("")){
            errorAlert("Make sure to fill all fields.");
        }
        else{
            try{
                int inUse = 0;
                if(inUseBox.isSelected()){
                    inUse = 1;
                }
                String sql = "INSERT INTO SPC VALUES (NULL, '"+nameTf.getText()+"', '"+addressTf.getText()+"', '"+phoneTf.getText()+"', '"+emailTf.getText()+"', '"+inUse+"')";
                Database.getInstance().connect();
                Database.getInstance().update(sql);
                Database.getInstance().closeConnection();
            }catch(SQLException e){
                System.err.println(e);
            }
            fillTable();
        }
    }
    
    public void editSPC(){
        if(nameTf.getText().equals("") || addressTf.getText().equals("") || phoneTf.getText().equals("") || emailTf.getText().equals("")){
            errorAlert("Try again: Empty field");
        }
        else if(table.getSelectionModel().getSelectedItem()==null){
            errorAlert("Please select a SPC to update");
        }
        else{
            try{
                int inUse = 0;
                if(inUseBox.isSelected()){
                    inUse = 1;
                }
                String sql = "UPDATE SPC SET SPCName='"+nameTf.getText()+"', SPCAddress='"+addressTf.getText()+"', SPCPhoneNo='"+phoneTf.getText()+"', SPCEmail='"+emailTf.getText()+"', SPCInUse='"+inUse+"' WHERE SPCID="+table.getSelectionModel().getSelectedItem().getId();
                Database.getInstance().connect();
                Database.getInstance().update(sql);
                Database.getInstance().closeConnection();
            }catch(SQLException e){
                System.err.println(e);
            }
            fillTable();
        }
    }
    
    public void deleteSPC(){
        if(table.getSelectionModel().getSelectedItem()==null){
            errorAlert("Please select a row");
        }
        else{
            try{
                String sql = "DELETE FROM SPC WHERE SPCID = "+table.getSelectionModel().getSelectedItem().getId();
                Database.getInstance().connect();
                Database.getInstance().update(sql);
                Database.getInstance().closeConnection();
            }catch(SQLException e){
                System.err.println(e);
            }
            fillTable();
        }
    }
    
    public void disableTextFields(){
        nameTf.setEditable(false);
        addressTf.setEditable(false);
        phoneTf.setEditable(false);
        emailTf.setEditable(false);
        inUseBox.setDisable(true);
        addButton.setVisible(false);
        deleteButton.setVisible(false);
        updateButton.setVisible(false);
        
    }
    
    public void errorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
