/**
 * FXML Controller class
 *
 * @author Lloyd
 */

package specialist.gui;

import common.Database;
import java.net.URL;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class DetailsScreenController implements Initializable {

    private SpecialistScreenController mainScreen;
    @FXML private Button closeButton;
    @FXML private Label cFNameLabel, cLNameLabel, cTypeLabel, cAddressLabel, cPCodeLabel, cPNoLabel, cEmailLabel;
    @FXML private Label sNameLabel, sAddressLabel, sPNoLabel, sEmailLabel, sDDateLabel, sRDateLabel, sCostLabel;
    @FXML private Label vRegNoLabel, vTypeLabel, vModelLabel, vMakeLabel, vMileageLabel, vESizeLabel, vFTypeLabel, vColourLabel, vMoTLabel;
    @FXML private Label pNameLabel;
    @FXML private TextArea pDescBox;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }
    
    public void displayDetails(int id, SpecialistScreenController mainScreen){
        this.mainScreen = mainScreen;
        String sql = "Select Customer.CustomerFirstName, Customer.CustomerLastName, Customer.CustomerType, Customer.CustomerAddress, Customer.CustomerPostcode, Customer.CustomerPhoneNo, Customer.CustomerEmail, "
                + "Vehicle.VehicleType, Vehicle.VehicleRegNo, Vehicle.VehicleModel, Vehicle.VehicleMake, Vehicle.VehicleMileage, Vehicle.VehicleEngSize, Vehicle.VehicleFuelType, Vehicle.VehicleColour, Vehicle.VehicleMoTDate, Vehicle.LastServiceDate, "
                + "SPC.SPCName, SPC.SPCAddress, SPC.SPCPhoneNo, SPC.SPCEmail, Part.PartName, Part.PartDescription, SpecialistRepair.PartID, "
                + "SpecialistRepair.SRDeliveryDate, SpecialistRepair.SRReturnDate, SpecialistRepair.SRCost from SpecialistRepair "
                + "INNER JOIN Booking ON SpecialistRepair.BookingID=Booking.BookingID "
                + "INNER JOIN Vehicle ON Booking.VehicleRegNo=Vehicle.VehicleRegNo "
                + "INNER JOIN Customer ON Vehicle.CustomerID=Customer.CustomerID "
                + "LEFT JOIN SPC ON SpecialistRepair.SPCID=SPC.SPCID "
                + "LEFT JOIN Part ON SpecialistRepair.PartID=Part.PartID "
                + "WHERE (SpecialistRepair.PartID IS NULL OR SpecialistRepair.PartID=Part.PartID) "
                + "AND (SpecialistRepair.SRID = "+id+")";

        try{
            Database.getInstance().connect();
            ResultSet rs = Database.getInstance().query(sql);
            
            cFNameLabel.setText(rs.getString("CustomerFirstName"));
            cLNameLabel.setText(rs.getString("CustomerLastName"));
            cTypeLabel.setText(rs.getString("CustomerType"));
            cAddressLabel.setText(rs.getString("CustomerAddress"));
            cPCodeLabel.setText(rs.getString("CustomerPostCode"));
            cPNoLabel.setText(rs.getString("CustomerPhoneNo"));
            cEmailLabel.setText(rs.getString("CustomerEmail"));
            
            sNameLabel.setText(rs.getString("SPCName"));
            sAddressLabel.setText(rs.getString("SPCAddress"));
            sPNoLabel.setText(rs.getString("SPCPhoneNo"));
            sEmailLabel.setText(rs.getString("SPCEmail"));
            sDDateLabel.setText(rs.getString("SRDeliveryDate"));
            sRDateLabel.setText(rs.getString("SRReturnDate"));
            sCostLabel.setText("£"+rs.getString("SRCost"));
            
            vRegNoLabel.setText(rs.getString("VehicleRegNo"));
            vTypeLabel.setText(rs.getString("VehicleType"));
            vModelLabel.setText(rs.getString("VehicleModel"));
            vMakeLabel.setText(rs.getString("VehicleMake"));
            vMileageLabel.setText(rs.getString("VehicleMileage"));
            vESizeLabel.setText(rs.getString("VehicleEngSize"));
            vFTypeLabel.setText(rs.getString("VehicleFuelType"));
            vColourLabel.setText(rs.getString("VehicleColour"));
            vMoTLabel.setText(rs.getString("VehicleMoTDate"));
            
            pNameLabel.setText(rs.getString("PartName"));
            pDescBox.setText(rs.getString("PartDescription"));
            
            Database.getInstance().closeConnection();
        }catch(SQLException e){
            System.err.println(e);
        }
    }
    
    public void editSpecialist() throws Exception{
        mainScreen.editSpecialist();
        closeWindow();
    }
    
    public void deleteSpecialist() throws Exception{
        mainScreen.deleteSpecialist();
        closeWindow();
    }
    
    
    public void closeWindow(){
        Stage buttonStage = (Stage) closeButton.getScene().getWindow();
        buttonStage.close();
    }

}
