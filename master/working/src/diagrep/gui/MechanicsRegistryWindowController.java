/**
 * FXML Controller class
 *
 * @author Nexus
 */
package diagrep.gui;

import diagrep.Mechanic;
import common.Database;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class MechanicsRegistryWindowController implements Initializable
{
	@FXML
	private TableView<diagrep.Mechanic> mechanicsTable;
	@FXML
	private TableColumn colID;
	@FXML
	private TableColumn colFirstName;
	@FXML
	private TableColumn colLastName;
	@FXML
	private TableColumn colRate;
	
	private Database db;
	
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		colID.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("id"));
		colFirstName.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("mechFirstName"));
		colLastName.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("mechLastName"));
		colRate.setCellValueFactory(new PropertyValueFactory<diagrep.Diagrep, String>("mechHourlyRate"));
		try
		{
			ObservableList<diagrep.Mechanic> dataList = FXCollections.observableArrayList();
			db = Database.getInstance();
			db.connect();
			db = Database.getInstance();
			ResultSet rs = db.query("SELECT * FROM Mechanic;");
			while (rs.next())
			{
				dataList.add(new Mechanic(rs.getString("MechanicID"), rs.getString("MechanicFirstName"), rs.getString("MechanicLastName"), rs.getInt("MechanicHourlyRate")));
				mechanicsTable.setItems(dataList);
			}
			db.closeConnection();
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
	}	
	
}
