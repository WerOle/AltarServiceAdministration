package asa.address.mail;

import java.io.File;

import asa.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * handling optional attachments of an e-mail
 * 
 * @author Ole Werger
 */
public class AttachmentsController {

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
	private Label attachment1;
	@FXML
	private Label attachment2;
	@FXML
	private Label attachment3;

	private String a1, a2, a3;

	public AttachmentsController() {

	}

	/**
	 * Called when the user clicks the "SENDEN" button.
	 */
	public void handleSave() {

		dialogStage.close();
	}

	/**
	 * Called when the user clicks the first "ändern" button.
	 */
	public void handleChangeFirst() {
		FileChooser fileChooser = new FileChooser();

		// Show save file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		a1 = file.getAbsolutePath();
		attachment1.setText(file.getAbsolutePath());
	}

	/**
	 * Called when the user clicks the second "ändern" button.
	 */
	public void handleChangeSecond() {
		FileChooser fileChooser = new FileChooser();

		// Show save file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		a2 = file.getAbsolutePath();
		attachment2.setText(file.getAbsolutePath());
	}

	/**
	 * Called when the user clicks the third "ändern" button.
	 */
	public void handleChangeThird() {
		FileChooser fileChooser = new FileChooser();

		// Show save file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		a3 = file.getAbsolutePath();
		attachment3.setText(file.getAbsolutePath());
	}

	/**
	 * setter and getter for three attachments
	 * 
	 * @return
	 */

	public String getA1() {
		return a1;
	}

	public void setA1(String a1) {
		this.a1 = a1;
	}

	public String getA2() {
		return a2;
	}

	public void setA2(String a2) {
		this.a2 = a2;
	}

	public String getA3() {
		return a3;
	}

	public void setA3(String a3) {
		this.a3 = a3;
	}
}
