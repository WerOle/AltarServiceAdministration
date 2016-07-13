package asa.address.plan;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import asa.address.MainApp;
import asa.address.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jxl.write.WriteException;

/**
 * Handling the change of two altar services for a mass
 *
 * @author Ole Werger
 */
public class ChangeController {
	// Reference to the main application
	private MainApp mainApp;
	@FXML
	private DatePicker dateLabel;
	@FXML
	private ChoiceBox choiceLabel;
	@FXML
	private ComboBox comboLabel;
	@FXML
	private Button button;

	private Stage dialogStage;

	private ObservableList<Person> allPersons, oldPersons;
	private String[] times;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		dateLabel.setOnAction(new EventHandler() {

			@Override
			public void handle(Event event) {
				allPersons = FXCollections.observableArrayList();

				System.out.println("datumLabel Event");
				LocalDate date = dateLabel.getValue();

				String year = date.getYear() + "";
				String month = date.getMonthValue() + "";

				if (month.length() < 2)
					month = "0" + month;
				String day = date.getDayOfMonth() + "";
				if (day.length() < 2)
					day = "0" + day;

				String datum = year + "" + month + "" + day;

				ReadExcelPerson re = new ReadExcelPerson();
				File file = new File("resources/md-plan.xls");
				re.setInputFile(file.getAbsolutePath());
				try {
					re.read(datum, mainApp.getPersonData());
				} catch (IOException e) {
					asa.address.logger.Logger.logAdd("IOException: " + e.getMessage());
					e.printStackTrace();
				}

				oldPersons = re.getPersons();
				times = re.getTimes();
				System.out.println("length scheduled: " + oldPersons.size());
				ObservableList<String> old = FXCollections.observableArrayList();
				int index = 0;
				for (Person p : oldPersons) {
					old.add(p.getComName() + " (" + re.getTimes(index) + ")");
					index++;
				}

				ObservableList<String> all = FXCollections.observableArrayList();
				for (Person p : mainApp.getPersonData()) {
					all.add(p.getComName());
					allPersons.add(p);
				}
				choiceLabel.setItems(old);
				comboLabel.setItems(all);
			}

		});

	}

	public ObservableList<Person> getAllePersonen() {
		return allPersons;
	}

	public void setAllePersonen(ObservableList<Person> personData) {
		this.allPersons = allPersons;
		comboLabel.setItems(allPersons);
	}

	@FXML
	public void handleText() {
		int indexAlt = choiceLabel.getSelectionModel().getSelectedIndex();
		int indexNeu = comboLabel.getSelectionModel().getSelectedIndex();
		File file = new File("resources/md-plan.xls");

		ReadExcelPerson re = new ReadExcelPerson();
		re.setInputFile(file.getAbsolutePath());
		String[][] array = re.read();
		LocalDate date = dateLabel.getValue();
		String year = date.getYear() + "";
		String month = date.getMonthValue() + "";
		if (month.length() < 2)
			month = "0" + month;
		String day = date.getDayOfMonth() + "";
		if (day.length() < 2)
			day = "0" + day;
		String datum = day + "." + month + "." + year;

		for (int i = 0; i < array.length; i++) {
			if (datum.equals(array[i][0])) {
				String name = oldPersons.get(indexAlt).getComName();
				if (array[i][2].equals(name)) {

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("TAUSCH!");
					alert.setHeaderText("TAUSCH!");
					alert.setContentText(name + " (" + i + ") gegen " + allPersons.get(indexNeu).getComName());
					alert.showAndWait();

					array[i][2] = allPersons.get(indexNeu).getComName();
				}
			}
		}

		WriteExcel we = new WriteExcel();
		we.setOutputFile(file.getAbsolutePath());
		try {
			we.write(array);
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
