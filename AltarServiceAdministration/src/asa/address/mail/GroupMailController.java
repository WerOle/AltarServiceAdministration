package asa.address.mail;

import java.util.ArrayList;

import javax.mail.MessagingException;

import asa.address.MainApp;
import asa.address.model.Person;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Handling sending mail to a lot of people
 * 
 * @author Ole Werger
 */
public class GroupMailController {
	// Reference to the main application
	private MainApp mainApp;

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
	}

	/**
	 * Called when the user clicks the "Continue..." button. in case the
	 * Checkbox is selected, another Dialog will open for choosing Files
	 * 
	 * @throws InterruptedException
	 */
	@FXML
	public void handleContinue() {
		System.out.println("Sending started!");
		asa.address.logger.Logger.logAdd("Sending started!");
		String a1 = "", a2 = "", a3 = "";
		if (cbAttachment.isSelected()) {
			String[] attachments = mainApp.showAttachment();
			if (attachments[0] != null)
				a1 = attachments[0];
			if (attachments[1] != null)
				a2 = attachments[1];
			if (attachments[2] != null)
				a3 = attachments[2];
			asa.address.logger.Logger.logAdd("Attachments:");
			asa.address.logger.Logger.logAdd(a1);
			asa.address.logger.Logger.logAdd(a2);
			asa.address.logger.Logger.logAdd(a3);
		}

		ObservableList<Person> receiver = mainApp.showDistributor();
		asa.address.logger.Logger.logAdd("total count of receiver: " + receiver.size());
		ArrayList<String> listReceiver = new ArrayList<String>();
		for (Person p : receiver) {
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
		asa.address.logger.Logger.logAdd("Sender: " + sender);
		asa.address.logger.Logger.logAdd("Betreff: " + subject);
		asa.address.logger.Logger.logAdd("Text: " + text);

		int xy = 0;// index in listReceiver

		for (String s : listReceiver) {
			boolean possible = false;
			try {
				xy++;
				m.postMail(s, subject, text, sender, a1, a2, a3);
				asa.address.logger.Logger
						.logAdd(xy + ". of " + listReceiver.size() + " Mails, Mail to: " + s + " sended!");
				possible = true;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					asa.address.logger.Logger.logAdd(e.getMessage());

					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error!");
					alert.setHeaderText("Error while sending mails (RUNTIME!):");
					alert.setContentText(e.getMessage());

					alert.showAndWait();
					e.printStackTrace();
				}
			} catch (MessagingException e) {
				asa.address.logger.Logger.logAdd(e.getMessage());

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText("Error while sending mails:");
				alert.setContentText(e.getMessage() + " at " + s + " \nTry to send again at end of list!");

				alert.showAndWait();

				e.printStackTrace();
			} finally {

				if (!possible)
					listReceiver.add(s);
			}
		}

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("FINISHED!");
		alert.setHeaderText("All mails could be sended!");
		alert.setContentText("Back to mail overview.");

		alert.showAndWait();

	}
}
