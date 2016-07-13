package asa.address.mail;

import asa.address.MainApp;
import asa.address.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Handling different groups of people
 * 
 * @author Ole Werger
 */
public class DistributorController {
	@FXML
	private ObservableList<Person> dataOBM;
	@FXML
	private ObservableList<Person> dataAelter;
	@FXML
	private ObservableList<Person> dataNormal;
	@FXML
	private ObservableList<Person> dataNeu;
	@FXML
	private ObservableList<Person> dataSonstige;
	@FXML
	private ObservableList<Person> receiver;

	public ObservableList<Person> getReceiver() {
		return receiver;
	}

	public void setEmpfaenger(ObservableList<Person> receiver) {
		this.receiver = receiver;
	}

	@FXML
	private TableView<Person> tableOBM;
	@FXML
	private TableView<Person> tableAelter;
	@FXML
	private TableView<Person> tableNormal;
	@FXML
	private TableView<Person> tableNeu;
	@FXML
	private TableView<Person> tableSonstige;
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
	private CheckBox cbOBM;
	@FXML
	private CheckBox cbAelter;
	@FXML
	private CheckBox cbNormal;
	@FXML
	private CheckBox cbNeu;
	@FXML
	private CheckBox cbSonstige;
	// FirstNameColumn
	@FXML
	private TableColumn<Person, String> obmFNC;
	@FXML
	private TableColumn<Person, String> alterFNC;
	@FXML
	private TableColumn<Person, String> normalFNC;
	@FXML
	private TableColumn<Person, String> neuFNC;
	@FXML
	private TableColumn<Person, String> sonstigeFNC;
	// LastNameColumn
	@FXML
	private TableColumn<Person, String> obmLNC;
	@FXML
	private TableColumn<Person, String> alterLNC;
	@FXML
	private TableColumn<Person, String> normalLNC;
	@FXML
	private TableColumn<Person, String> neuLNC;
	@FXML
	private TableColumn<Person, String> sonstigeLNC;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		System.out.println("initialize started!");
		// Initialize the person table with the two columns.
		obmFNC.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		obmLNC.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		alterFNC.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		alterLNC.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		normalFNC.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		normalLNC.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		neuFNC.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		neuLNC.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		sonstigeFNC.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		sonstigeLNC.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

		tableOBM.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableAelter.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		;
		tableNormal.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		;
		tableNeu.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		;
		tableSonstige.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		;
	}

	public DistributorController() {

	}

	/**
	 * Called when the user clicks the "SENDEN" button.
	 */
	public void handleSave() {
		receiver = FXCollections.observableArrayList();
		if (cbOBM.isSelected()) {
			for (Person p : dataOBM) {
				receiver.add(p);
			}
		} else {
			ObservableList<Person> personen = tableOBM.getSelectionModel().getSelectedItems();
			for (Person p : personen) {
				receiver.add(p);
			}
		}

		if (cbAelter.isSelected()) {
			for (Person p : dataAelter) {
				receiver.add(p);
			}
		} else {
			ObservableList<Person> personen = tableAelter.getSelectionModel().getSelectedItems();
			for (Person p : personen) {
				receiver.add(p);
			}
		}
		if (cbNormal.isSelected()) {
			for (Person p : dataNormal) {
				receiver.add(p);
			}
		} else {
			ObservableList<Person> personen = tableNormal.getSelectionModel().getSelectedItems();
			for (Person p : personen) {
				receiver.add(p);
			}
		}
		if (cbNeu.isSelected()) {
			for (Person p : dataNeu) {
				receiver.add(p);
			}
		} else {
			ObservableList<Person> personen = tableNeu.getSelectionModel().getSelectedItems();
			for (Person p : personen) {
				receiver.add(p);
			}
		}
		if (cbSonstige.isSelected()) {
			for (Person p : dataSonstige) {
				receiver.add(p);
			}
		} else {
			ObservableList<Person> personen = tableSonstige.getSelectionModel().getSelectedItems();
			for (Person p : personen) {
				receiver.add(p);
			}
		}

		dialogStage.close();
	}

	public ObservableList<Person> getDataOBM() {
		return dataOBM;
	}

	public void setDataOBM(ObservableList<Person> dataOBM) {
		this.dataOBM = dataOBM;
		tableOBM.setItems(dataOBM);
	}

	public ObservableList<Person> getDataAelter() {
		return dataAelter;
	}

	public void setDataAelter(ObservableList<Person> dataAelter) {
		this.dataAelter = dataAelter;
		tableAelter.setItems(dataAelter);
	}

	public ObservableList<Person> getDataNormal() {
		return dataNormal;
	}

	public void setDataNormal(ObservableList<Person> dataNormal) {
		this.dataNormal = dataNormal;
		tableNormal.setItems(dataNormal);
	}

	public ObservableList<Person> getDataNeu() {
		return dataNeu;
	}

	public void setDataNeu(ObservableList<Person> dataNeu) {
		this.dataNeu = dataNeu;
		tableNeu.setItems(dataNeu);
	}

	public ObservableList<Person> getDataSonstige() {
		return dataSonstige;
	}

	public void setDataSonstige(ObservableList<Person> dataSonstige) {
		this.dataSonstige = dataSonstige;
		tableSonstige.setItems(dataSonstige);
	}

	public void setPersonData(ObservableList<Person> personData) {
		dataOBM = FXCollections.observableArrayList();
		dataAelter = FXCollections.observableArrayList();
		dataNormal = FXCollections.observableArrayList();
		dataNeu = FXCollections.observableArrayList();
		dataSonstige = FXCollections.observableArrayList();

		for (Person p : personData) {
			if (p.getGroup().equals("1OBM"))
				dataOBM.add(p);
			else if (p.getGroup().equals("2Aelter"))
				dataAelter.add(p);
			else if (p.getGroup().equals("3Normal"))
				dataNormal.add(p);
			else if (p.getGroup().equals("4Neu"))
				dataNeu.add(p);
			else if (p.getGroup().equals("5Sonstige"))
				dataSonstige.add(p);
			else {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText("Group is invalid!");
				alert.setContentText("" + p.getGroup() + " is not achievable, set to 5Sonstige");
				alert.showAndWait();

				dataSonstige.add(p);
			}
		}
		tableOBM.setItems(dataOBM);
		tableAelter.setItems(dataAelter);
		tableNormal.setItems(dataNormal);
		tableNeu.setItems(dataNeu);
		tableSonstige.setItems(dataSonstige);

	}

}
