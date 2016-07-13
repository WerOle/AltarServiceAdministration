package asa.address.mail;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.mail.MessagingException;

import asa.address.MainApp;
import asa.address.logger.WriteLogger;
import asa.address.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Handling sending remainder-mails
 * 
 * @author Ole Werger
 */
public class RemainderController {
	// Reference to the main application
	private MainApp mainApp;
	@FXML
	private DatePicker startLabel;
	@FXML
	private DatePicker endLabel;
	@FXML
	private TextField senderLabel;
	@FXML
	private PasswordField passwordLabel;
	@FXML
	private TextArea textLabel;

	private Stage dialogStage;
	private String mailtext;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		startLabel.setShowWeekNumbers(true);
		endLabel.setShowWeekNumbers(true);
		getTextFromFile();

		textLabel.setText(mailtext);
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
	 * read Emailtext
	 */
	public void getTextFromFile() {
		File file = new File("resources/Emailtext.txt");
		WriteLogger wl = new WriteLogger();
		System.out.println(file.getPath());
		String in = wl.readTextFile(file.getPath(), "UTF-8");
		System.out.println(in);
		mailtext = in;
	}

	/**
	 * Called when the user clicks the "SENDEN" button.
	 */
	@FXML
	public void handleContinue() {

		asa.address.list.ReadExcelPerson re = new asa.address.list.ReadExcelPerson();

		File file = new File("resources/md-plan.xls");
		re.setInputFile(file.getAbsolutePath());

		LocalDate start = startLabel.getValue();
		LocalDate end = endLabel.getValue();

		String sYear = start.getYear() + "";
		String eYear = end.getYear() + "";
		String sMonth = start.getMonthValue() + "";
		String eMonth = end.getMonthValue() + "";
		if (sMonth.length() < 2)
			sMonth = "0" + sMonth;
		if (eMonth.length() < 2)
			eMonth = "0" + eMonth;
		String sDay = start.getDayOfMonth() + "";
		String eDay = end.getDayOfMonth() + "";
		if (sDay.length() < 2)
			sDay = "0" + sDay;
		if (eDay.length() < 2)
			eDay = "0" + eDay;

		String st = sYear + "" + sMonth + "" + sDay;
		String en = eYear + "" + eMonth + "" + eDay;

		asa.address.logger.Logger.logAdd("Startdate " + startLabel.getValue() + " " + st);
		asa.address.logger.Logger.logAdd("Enddate " + endLabel.getValue() + " " + en);

		int startdate = Integer.parseInt(st);
		int enddate = Integer.parseInt(en);

		ArrayList<String> times = new ArrayList<String>();
		ArrayList<String> dates = new ArrayList<String>();
		ArrayList<Person> p = new ArrayList<Person>();

		if (enddate >= startdate) {
			for (int date = startdate; date <= enddate; date++) {

				try {
					re.read(date + "", mainApp.getPersonData());
				} catch (IOException e) {
					asa.address.logger.Logger.logAdd("IOException " + e.getMessage());
					e.printStackTrace();
				}
				for (int i = 0; i < re.getLength(); i++) {
					dates.add(date + "");
					times.add(re.getTimes(i));
					p.add(re.getPerson(i));
				}

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(date + "");
				alert.setHeaderText("founded: ");
				alert.setContentText(re.getLength() + "");

				alert.showAndWait();

			}

			System.out.println("Mailer started!");
			System.out.println("total length: " + times.size());
			asa.address.logger.Logger.logAdd("Mailer started!");
			asa.address.logger.Logger.logAdd("total length: " + times.size());
			Mailer m = new Mailer();
			m.setPassword(passwordLabel.getText());
			String subject = "";
			String text = textLabel.getText();
			String sender = senderLabel.getText();

			ObservableList<Person> persons = FXCollections.observableArrayList();

			for (int i = 0; i < p.size(); i++) {
				persons.add(p.get(i));
			}

			asa.address.logger.Logger.logAdd("total length persons: " + persons.size());
			System.out.println("Gesamtlänge Personen: " + persons.size());
			for (int i = 0; i < times.size(); i++) {
				asa.address.logger.Logger.logAdd((i + 1) + ". Person gets remainded!");

				char[] d = dates.get(i).toCharArray();

				String date = d[6] + "" + d[7] + "." + d[4] + "" + d[5] + ".";

				/////// ATTENTION MAIL-TEXT IS GERMAN
				subject = "Erinnerung für den " + date;// "Remainder for ..."

				String stext = "Hallo " + persons.get(i).getComName() + "!\nDenke bitte daran, du bist am " + date
						+ " um " + times.get(i) + " Uhr als Messdiener aufgestellt! " + text;

				/*
				 * English Translation Hello [Name]! Please remind that you are
				 * scheduled as a altar service on [date] at [time]!
				 */

				try {
					m.postMail(persons.get(i).getEmail1(), subject, stext, sender, "", "", "");
				} catch (MessagingException e) {
					asa.address.logger.Logger.logAdd("MessagingException " + e.getMessage());
					e.printStackTrace();
				}
				asa.address.logger.Logger.logAdd("Mail to: " + persons.get(i).getEmail1() + " sended!");
				String m2 = persons.get(i).getEmail2();
				if (m2 != null && !m2.equals("0") && m2.length() > 3) {
					try {
						m.postMail(persons.get(i).getEmail2(), subject, stext, sender, "", "", "");
					} catch (MessagingException e) {
						asa.address.logger.Logger.logAdd("MessagingException " + e.getMessage());
						e.printStackTrace();
					}
				}
				asa.address.logger.Logger.logAdd("2. Mail to: " + persons.get(i).getEmail2() + " sended!");
			}

		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText("enddate is before startdate");
			alert.setContentText("Please change dates!");

			alert.showAndWait();
		}

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Finished!");
		alert.setHeaderText("FInished!");
		alert.setContentText("All mails are sended!");

		alert.showAndWait();

	}

}
