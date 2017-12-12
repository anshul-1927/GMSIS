/**
 * FXML Controller class
 *
 * @author Nexus
 * Credits:
 * Formatting LocalDateTime/LocalDate: http://stackoverflow.com/questions/28177370/how-to-format-localdate-to-string
 * ListView: http://www.java2s.com/Tutorials/Java/JavaFX/0640__JavaFX_ListView.htm
 * reading/writing an external file in Windows: http://stackoverflow.com/questions/5797208/java-how-do-i-write-a-file-to-a-specified-directory
 */
package diagrep.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.util.Callback;


public class HolidayWindowController implements Initializable {
	
	@FXML private ListView list;
	
	@FXML private DatePicker dateP;

	@FXML private Button addButton;
	@FXML private Button deleteButton;
	
	ObservableList<String> dates;
	
	public void initialize(URL url, ResourceBundle rb)
	{
		restrictPastDate();
		dateP.setEditable(false);
		dateP.setValue(NOW_LOCALDATE().plusDays(1));
		dates = FXCollections.observableArrayList();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("res/holidays.txt"));
			String line = "";
			while ((line = br.readLine()) != null)
			{
				dates.add(line);
			}
			list.setItems(dates);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Alert alertX = new Alert(AlertType.ERROR);
			alertX.setTitle("Holiday List Load Error");
			alertX.setContentText("Unable to access ./res/holidays.txt. The file or the folder might be missing, or there might be an issue with its permissions.\n"
				+ "You can also generate a new list of holidays by simply adding dates to a new list from the holiday manager.\n"
				+ "If you choose not to do so, when you are adding or editting a booking, please ensure that your chosen appointment date does not fall on a holiday.");
			alertX.showAndWait();
		}
		
	}
	
	@FXML
	private void addHoliday()
	{
		String newDate = dateP.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		if (!dates.contains(newDate))
			dates.add(newDate);
		else
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Add Holiday Error");
			alert.setContentText(newDate+" is already registered as a holiday.");
			alert.showAndWait();
			return;
		}
		
		try
		{
			File file = new File("res/holidays.txt");
			PrintWriter pw = new PrintWriter(file);
			for (int i=0; i<dates.size(); i++)
			{
				pw.println(dates.get(i));
			}
			pw.close();
		}
		catch (FileNotFoundException e)
		{
			
		}
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Add Holiday Success");
		alert.setContentText("Successfully registered "+newDate+" as a holiday.");
		alert.showAndWait();
	}
	
	@FXML
	private void deleteHoliday()
	{
		String date = (String) list.getSelectionModel().getSelectedItem();
		if (date == null)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Delete Holiday Error");
			alert.setContentText("To delete a holiday, select a date from the list.");
			alert.showAndWait();
			return;
		}
		
		dates.remove(date);
		
		try
		{
			File file = new File("res/holidays.txt");
			PrintWriter pw = new PrintWriter(file);
			for (int i=0; i<dates.size(); i++)
			{
				pw.println(dates.get(i));
			}
			pw.close();
		}
		catch (FileNotFoundException e)
		{
			
		}
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Delete Holiday Success");
		alert.setContentText("Successfully deleted "+date+" from the list of holidays.");
		alert.showAndWait();
	}
	
	private void restrictPastDate()
	{
		Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell()
		{
			@Override
			public void updateItem(LocalDate item, boolean empty)
			{
				super.updateItem(item, empty);		
				if(item.isBefore(LocalDate.now().plusDays(1)))
				{
					setStyle("-fx-background-color: #ffc0cb;");
					Platform.runLater(() -> setDisable(true));
				}
			}
		};
		dateP.setDayCellFactory(dayCellFactory);
	}

	private LocalDate parseLocalDate(String str)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(str, formatter);
	}
	
	private LocalDate NOW_LOCALDATE()
	{
		return LocalDate.now();
	}
	
}
