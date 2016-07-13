package asa.address.view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import asa.address.MainApp;
import asa.address.mail.SingleMailController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 * 
 * @author Marco Jakob adapted by: Ole Werger
 */
public class RootLayoutController {

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

	/**
	 * Creates an empty address book.
	 */
	@FXML
	private void handleNew() {
		mainApp.getPersonData().clear();
		mainApp.setPersonFilePath(null);
	}

	/**
	 * Opens a FileChooser to let the user select an address book to load.
	 */
	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			mainApp.loadPersonDataFromFile(file);
		}
	}

	/**
	 * Saves the file to the person file that is currently open. If there is no
	 * open file, the "save as" dialog is shown.
	 */
	@FXML
	private void handleSave() {
		File personFile = mainApp.getPersonFilePath();
		if (personFile != null) {
			mainApp.savePersonDataToFile(personFile);
		} else {
			handleSaveAs();
		}
	}

	/**
	 * Saves the file to the person file that is currently open. If there is no
	 * open file, the "save as" dialog is shown.
	 */
	@FXML
	private void handleSaveXLS() {

		String path = "";
		Date date = java.util.Calendar.getInstance().getTime();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = dateFormatter.format(date);
		path = dateString.charAt(0) + "" + dateString.charAt(1) + "" + dateString.charAt(2) + "" + dateString.charAt(3)
				+ "-MD-Liste-" + dateString + ".xls";
		mainApp.saveXLS(path);

	}

	/**
	 * Opens a FileChooser to let the user select a file to save to.
	 */
	@FXML
	private void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

		if (file != null) {
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			mainApp.savePersonDataToFile(file);
		}
	}

	/**
	 * Opens the birthday statistics.
	 */
	@FXML
	private void handleShowBirthdayStatistics() {
		mainApp.showBirthdayStatistics();
	}

	/**
	 * Opens an about dialog.
	 */
	@FXML
	private void handleAbout() {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AdressApp - altar service version");
		alert.setHeaderText("About");
		alert.setContentText(
				"Author of AdressApp: Marco Jakob\nWebsite: http://code.makery.ch\nadapted by: Ole Werger\nMail to: ole@4w-online.de");
		alert.showAndWait();
	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}

	/**
	 * Send a single Mail.
	 */
	@FXML
	private void handleSingleMail() {
		mainApp.showSingleMail();
	}

	/**
	 * Send a group Mail.
	 */
	@FXML
	private void handleGroupMail() {
		mainApp.showGroupMail();
	}

	/**
	 * Send Remainder.
	 */
	@FXML
	private void handleRemainder() {
		mainApp.showRemainder();
	}

	/**
	 * Change ass.
	 */
	@FXML
	private void handleChange() {
		mainApp.showChange();
	}

	/**
	 * Create new Masses.
	 */
	@FXML
	private void handleCreate() {
		mainApp.showCreate();
	}
}