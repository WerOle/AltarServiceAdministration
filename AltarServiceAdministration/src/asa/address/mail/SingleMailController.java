package asa.address.mail;

import java.util.ArrayList;

import javax.mail.MessagingException;

import asa.address.MainApp;
import asa.address.model.Person;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Handling sending a single mail to one receiver
 * 
 * @author Ole Werger
 */
public class SingleMailController {
	// Reference to the main application
	private MainApp mainApp;

	@FXML
	private TableView<Person> personTable;
	@FXML
	private TableColumn<Person, String> firstNameColumn;
	@FXML
	private TableColumn<Person, String> lastNameColumn;

	@FXML
	private TextField senderLabel;
	@FXML
	private PasswordField passwordLabel;
	@FXML
	private TextField subjectLabel;
	@FXML
	private TextArea textLabel;
	@FXML
	private CheckBox cbAttachment;

	private Stage dialogStage;

	private ObservableList<Person> personData;
	private ArrayList<String> listReceiver;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		System.out.println("initialize started!");
		// Initialize the person table with the two columns.
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
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
	}

	/**
	 * Called when the user clicks the "SENDEN" button. in case the Checkbox is
	 * selected, another Dialog will open for choosing Files
	 */
	@FXML
	public void handleSend() {
		System.out.println("started to send!");
		String a1 = "", a2 = "", a3 = "";
		if (cbAttachment.isSelected()) {
			String[] attachments = mainApp.showAttachment();
			if (attachments[0] != null)
				a1 = attachments[0];
			if (attachments[1] != null)
				a2 = attachments[1];
			if (attachments[2] != null)
				a3 = attachments[2];
			asa.address.logger.Logger.logAdd("attachments:");
			asa.address.logger.Logger.logAdd(a1);
			asa.address.logger.Logger.logAdd(a2);
			asa.address.logger.Logger.logAdd(a3);
		}

		listReceiver = new ArrayList<String>();
		ObservableList<Person> list = personTable.getSelectionModel().getSelectedItems();
		for (Person p : list) {
			String mail1 = p.getEmail1();
			String mail2 = p.getEmail2();
			if (!mail1.equals("0") && mail1 != null && mail1.length() != 0) {
				listReceiver.add(mail1);
			}
			if (!mail2.equals("0") && mail2 != null && mail2.length() != 0) {
				listReceiver.add(mail2);
			}
		}
		System.out.println("Mailer started!");
		Mailer m = new Mailer();
		m.setPassword(passwordLabel.getText());
		String subject = subjectLabel.getText();
		String text = textLabel.getText();
		String sender = senderLabel.getText();
		for (String s : listReceiver) {
			boolean possible = false;
			try {
				m.postMail(s, subject, text, sender, a1, a2, a3);
				asa.address.logger.Logger.logAdd("Mail to: " + s + " sended!");
				possible = true;
			} catch (MessagingException e) {
				asa.address.logger.Logger.logAdd(e.getMessage());
				possible = false;
				e.printStackTrace();
			} finally {
				if (!possible)
					listReceiver.add(s);
			}
		}
	}

	public ObservableList<Person> getPersonData() {
		return personData;
	}

	public void setPersonData(ObservableList<Person> personData) {
		this.personData = personData;
		personTable.setItems(personData);
	}

}
