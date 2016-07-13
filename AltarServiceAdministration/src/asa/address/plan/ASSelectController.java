package asa.address.plan;

import asa.address.MainApp;
import asa.address.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 * Handling the selection of altar service for a mass
 *
 * @author Ole Werger
 */
public class ASSelectController {
	// Reference to the main application
	private MainApp mainApp;

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		persons = mainApp.getPersonData();
		for (Person p : persons) {
			names.add(p.getComName());
		}
		cb.setItems(names);
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
	private ChoiceBox cb;
	@FXML
	private Button button;

	private ObservableList<String> names;
	private ObservableList<Person> persons;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		names = FXCollections.observableArrayList();
		System.out.println("initialize started!");

	}

	/**
	 * Called when the user clicks the "SENDEN" button.
	 */
	public void handleSave() {
		dialogStage.close();
	}

	public Person getSelectedMD() {
		return persons.get(cb.getSelectionModel().getSelectedIndex());
	}

}
