package asa.address.plan;

import java.time.LocalDate;

import asa.address.MainApp;
import asa.address.model.Mass;
import asa.address.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Handling the editing of a mass
 *
 * @author Ole Werger
 */
public class MassEditController {
	// Reference to the main application
	private MainApp mainApp;

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	private Stage dialogStage;

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@FXML
	private DatePicker dateLabel;
	@FXML
	private TextField timeLabel;
	@FXML
	private TextField titelLabel;
	@FXML
	private ListView listLabel;
	@FXML
	private Button plus;
	@FXML
	private Button minus;
	@FXML
	private Button save;

	private ObservableList<Person> ass;// altar services
	private ObservableList<String> names;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		System.out.println("initialize started!");
		ass = FXCollections.observableArrayList();
		names = FXCollections.observableArrayList();

	}

	/**
	 * Called when the user clicks the "Save" button.
	 */
	public void handleSave() {
		if (isInputValid())
			dialogStage.close();

	}

	/**
	 * Called when the user clicks the "+" button.
	 */
	public void handlePlus() {
		Person p = mainApp.showASSelect();
		ass.add(p);
		names.add(p.getComName());
		listLabel.setItems(names);
	}

	/**
	 * Called when the user clicks the "-" button.
	 */
	public void handleMinus() {
		int index = listLabel.getSelectionModel().getSelectedIndex();
		ass.remove(index);
		names.remove(index);
		listLabel.setItems(names);
	}

	public Mass getMass() {
		Mass m = new Mass();
		LocalDate date = dateLabel.getValue();

		String year = date.getYear() + "";
		String month = date.getMonthValue() + "";

		if (month.length() < 2)
			month = "0" + month;
		String day = date.getDayOfMonth() + "";
		if (day.length() < 2)
			day = "0" + day;

		String datum = day + "." + month + "." + year;

		m.setDate(datum);

		m.setTime(timeLabel.getText());

		m.setTitle(titelLabel.getText());

		m.setAss(ass);

		return m;
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (timeLabel.getText() == null || timeLabel.getText().length() == 0) {
			errorMessage += "No valid time! It's empty!\n";
		} else {
			char[] ca = timeLabel.getText().toCharArray();
			if (ca.length != 5)
				errorMessage += "No valid timelength! Use 'hh.mm'!\n";
			if (ca[2] != '.')
				errorMessage += "No valid split symbol! Use 'hh.mm'!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

}
