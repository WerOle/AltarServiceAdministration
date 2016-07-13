package asa.address;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import asa.address.mail.AttachmentsController;
import asa.address.mail.DistributorController;
import asa.address.mail.GroupMailController;
import asa.address.mail.RemainderController;
import asa.address.mail.SingleMailController;
import asa.address.model.Mass;
import asa.address.model.Person;
import asa.address.model.PersonListWrapper;
import asa.address.plan.ASSelectController;
import asa.address.plan.ChangeController;
import asa.address.plan.CreateController;
import asa.address.plan.MassEditController;
import asa.address.view.BirthdayStatisticsController;
import asa.address.view.PersonEditDialogController;
import asa.address.view.PersonOverviewController;
import asa.address.view.RootLayoutController;
import jxl.write.WriteException;

/**
 * Main Class for the application
 * 
 * @author Marco Jakob adapted by Ole Werger
 */
public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	/**
	 * The data as an observable list of Persons.
	 */
	private ObservableList<Person> personData = FXCollections.observableArrayList();

	/**
	 * Constructor
	 */
	public MainApp() {
		// Add some sample data
		personData.add(new Person("Max", "Mustermann"));
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AddressApp");

		// Set the application icon.
		this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png"));

		initRootLayout();

		showPersonOverview();
	}

	/**
	 * Initializes the root layout and tries to load the last opened person
	 * file.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Try to load last opened person file.
		File file = getPersonFilePath();
		if (file != null) {
			loadPersonDataFromFile(file);
		}
	}

	/**
	 * Shows the person overview inside the root layout.
	 */
	public void showPersonOverview() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);

			// Give the controller access to the main app.
			PersonOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens a dialog to edit details for the specified person. If the user
	 * clicks OK, the changes are saved into the provided person object and true
	 * is returned.
	 * 
	 * @param person
	 *            the person object to be edited
	 * @return true if the user clicked OK, false otherwise.
	 */
	public boolean showPersonEditDialog(Person person) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Person");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			PersonEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(person);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Shows the SingleMail overview
	 */
	public void showSingleMail() {
		System.out.println("SingleMail started!");
		try {
			// Load SingleMail.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("mail/SingleMail.fxml"));
			AnchorPane einzelmail = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Send a single Mail");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
			Scene scene = new Scene(einzelmail);
			dialogStage.setScene(scene);

			// Give the controller access to the main app.
			SingleMailController controller = loader.getController();
			controller.setMainApp(this);
			controller.setDialogStage(dialogStage);
			controller.setPersonData(personData);
			// Show the dialog and wait until the user closes it
			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showChange() {
		System.out.println("Change started!");
		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("plan/Change.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			asa.address.logger.Logger.logAdd(e.getMessage());
			e.printStackTrace();
		}

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Change altar servers");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		// Set the filepaths
		ChangeController controller = loader.getController();
		controller.setMainApp(this);
		controller.setDialogStage(dialogStage);
		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
	}

	public String[] showAttachment() {
		System.out.println("Attachment started!");
		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("mail/Attachment.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			asa.address.logger.Logger.logAdd(e.getMessage());
			e.printStackTrace();
		}

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Choose attachment");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		// Set the filepaths
		AttachmentsController controller = loader.getController();
		controller.setMainApp(this);
		controller.setDialogStage(dialogStage);
		String[] sArray = new String[3];
		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
		sArray[0] = controller.getA1();
		sArray[1] = controller.getA2();
		sArray[2] = controller.getA3();
		asa.address.logger.Logger.logAdd("Attachment 1: " + sArray[0]);
		asa.address.logger.Logger.logAdd("Attachment 2: " + sArray[1]);
		asa.address.logger.Logger.logAdd("Attachment 3: " + sArray[2]);

		return sArray;
	}

	public Person showASSelect() {
		System.out.println("ASSelect started!");
		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("plan/ASSelect.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			asa.address.logger.Logger.logAdd(e.getMessage());
			e.printStackTrace();
		}

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Choose altar service");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		// Set the filepaths
		ASSelectController controller = loader.getController();
		controller.setMainApp(this);
		controller.setDialogStage(dialogStage);
		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
		return controller.getSelectedMD();
	}

	public Mass showMassEdit() {
		System.out.println("MassEdit gestartet!");
		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("plan/MassEdit.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			asa.address.logger.Logger.logAdd(e.getMessage());
			e.printStackTrace();
		}

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Create mass");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		// Set the filepaths
		MassEditController controller = loader.getController();
		controller.setMainApp(this);
		controller.setDialogStage(dialogStage);
		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
		return controller.getMass();
	}

	public void showCreate() {
		System.out.println("Create started!");
		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("plan/Create.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			asa.address.logger.Logger.logAdd(e.getMessage());
			e.printStackTrace();
		}

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Create a duty roster");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		// Set the filepaths
		CreateController controller = loader.getController();
		controller.setMainApp(this);
		controller.setDialogStage(dialogStage);
		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
	}

	public void showRemainder() {
		System.out.println("Remainder started!");
		try {
			// Load Erinnerung.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("mail/Remainder.fxml"));
			AnchorPane erinnerung = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Send remainder-mails");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image("file:resources/images/calender.png"));
			Scene scene = new Scene(erinnerung);
			dialogStage.setScene(scene);

			// Give the controller access to the main app.
			RemainderController controller = loader.getController();
			controller.setMainApp(this);
			controller.setDialogStage(dialogStage);
			// Show the dialog and wait until the user closes it
			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Opens a dialog to show birthday statistics.
	 */
	public void showBirthdayStatistics() {
		try {
			// Load the fxml file and create a new stage for the popup.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Birthday Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image("file:resources/images/calendar.png"));
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the persons into the controller.
			BirthdayStatisticsController controller = loader.getController();
			controller.setPersonData(personData);

			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ObservableList<Person> showDistributor() {
		System.out.println("Distributor started!");
		// Load the fxml file and create a new stage for the popup dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("mail/Distributor.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			asa.address.logger.Logger.logAdd(e.getMessage());
			e.printStackTrace();
		}

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Collect Dsitributor");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		// Set the filepaths
		DistributorController controller = loader.getController();
		controller.setMainApp(this);
		controller.setDialogStage(dialogStage);
		controller.setPersonData(personData);

		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
		ObservableList<Person> receiver = controller.getReceiver();
		return receiver;
	}

	public void showGroupMail() {
		System.out.println("GroupMail started!");
		try {
			// Load GroupMail.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("mail/GroupMail.fxml"));
			AnchorPane gruppenmail = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Send GroupMails ");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
			Scene scene = new Scene(gruppenmail);
			dialogStage.setScene(scene);

			// Give the controller access to the main app.
			GroupMailController controller = loader.getController();
			controller.setMainApp(this);
			controller.setDialogStage(dialogStage);
			// Show the dialog and wait until the user closes it
			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveXLS(String path) {
		asa.address.logger.Logger.logAdd("XLS saving started!");
		asa.address.logger.Logger.logAdd("Path: " + path);
		asa.address.list.WriteExcel we = new asa.address.list.WriteExcel();
		we.setOutputFile(path);
		try {
			we.write(personData);
		} catch (WriteException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	/**
	 * Returns the person file preference, i.e. the file that was last opened.
	 * The preference is read from the OS specific registry. If no such
	 * preference can be found, null is returned.
	 * 
	 * @return
	 */
	public File getPersonFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * Sets the file path of the currently loaded file. The path is persisted in
	 * the OS specific registry.
	 * 
	 * @param file
	 *            the file or null to remove the path
	 */
	public void setPersonFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());

			// Update the stage title.
			primaryStage.setTitle("AddressApp - " + file.getName());
		} else {
			prefs.remove("filePath");

			// Update the stage title.
			primaryStage.setTitle("AddressApp");
		}
	}

	/**
	 * Loads person data from the specified file. The current person data will
	 * be replaced.
	 * 
	 * @param file
	 */
	public void loadPersonDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

			personData.clear();
			personData.addAll(wrapper.getPersons());

			// Save the file path to the registry.
			setPersonFilePath(file);

		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data from file:");
			alert.setContentText(file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * Saves the current person data to the specified file.
	 * 
	 * @param file
	 */
	public void savePersonDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data.
			PersonListWrapper wrapper = new PersonListWrapper();
			wrapper.setPersons(personData);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, file);

			// Save the file path to the registry.
			setPersonFilePath(file);
		} catch (Exception e) { // catches ANY exception

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data from file:");
			alert.setContentText(file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Returns the data as an observable list of Persons.
	 * 
	 * @return
	 */
	public ObservableList<Person> getPersonData() {
		return personData;
	}

	public static void main(String[] args) {
		launch(args);
	}
}