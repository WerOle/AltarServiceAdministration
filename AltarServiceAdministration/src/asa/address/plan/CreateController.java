package asa.address.plan;

import java.io.File;
import java.io.IOException;

import asa.address.MainApp;
import asa.address.model.Mass;
import asa.address.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import jxl.write.WriteException;

/**
 * Handling the creation of a mass
 *
 * @author Ole Werger
 */
public class CreateController {
	// Reference to the main application
	private MainApp mainApp;

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		allAss = mainApp.getPersonData();

		ObservableList<Mass> old = readOldMass();
		for (Mass m : old) {
			masses.add(m);
			String niceDate = m.getDate().charAt(6) + "" + m.getDate().charAt(7) + "." + m.getDate().charAt(4) + ""
					+ m.getDate().charAt(5) + "." + m.getDate().charAt(0) + "" + m.getDate().charAt(1) + ""
					+ m.getDate().charAt(2) + "" + m.getDate().charAt(3);
			notations.add(niceDate + " " + m.getTime() + " " + m.getTitle());
		}
		list.setItems(notations);
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
	private ListView list;
	@FXML
	private Button plus;
	@FXML
	private Button minus;
	@FXML
	private Button save;

	private ObservableList<Mass> masses;
	private ObservableList<Person> allAss;
	private ObservableList<String> notations;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		System.out.println("initialize started!");
		masses = FXCollections.observableArrayList();
		notations = FXCollections.observableArrayList();
	}

	private ObservableList<Mass> readOldMass() {
		ReadExcelPerson re = new ReadExcelPerson();
		File file = new File("resources/md-plan.xls");
		re.setInputFile(file.getAbsolutePath());
		String[][] array = re.read();
		ObservableList<Mass> mess = FXCollections.observableArrayList();
		for (int i = 0; i < array.length; i++) {
			String date = array[i][8];
			String titel = array[i][3];
			String time = array[i][1];
			System.out.print(date + " - ");
			System.out.print(titel + " - ");
			System.out.println(time);
			ObservableList<Person> ass = FXCollections.observableArrayList();
			if (date != null && time != null) {
				if (date.length() > 5 && time.length() > 4) {
					while (date.equals(array[i][8]) && time.equals(array[i][1]) && i < array.length) {
						for (Person p : allAss) {
							if (array[i][2].equals(p.getComName())) {
								ass.add(p);
							}
						}
						i++;
					}
					i--;
					Mass m = new Mass();
					if (date.length() > 5) {
						String d = date.charAt(6) + "" + date.charAt(7) + "." + date.charAt(4) + "" + date.charAt(5)
								+ "." + date.charAt(0) + "" + date.charAt(1) + "" + date.charAt(2) + ""
								+ date.charAt(3);

						m.setDate(d);
					} else {
						System.out.println(date);
						m.setDate(null);
					}
					if (time != null && time.length() > 0)
						m.setTime(time);
					if (titel != null && titel.length() > 0)
						m.setTitle(titel);
					if (ass != null && ass.size() > 0)
						m.setAss(ass);
					mess.add(m);
				}
			}
		}
		return mess;

	}

	/**
	 * Called when the user clicks the "Save" button.
	 */
	public void handleSave() {
		WriteExcel we = new WriteExcel();
		File file = new File("resources/md-plan.xls");
		we.setOutputFile(file.getAbsolutePath());
		String[][] array = new String[400][9];
		int rowIndex = 0;
		for (Mass m : masses) {
			if (rowIndex < array.length) {
				for (Person p : m.getMDs()) {
					String niceDate = m.getDate().charAt(6) + "" + m.getDate().charAt(7) + "." + m.getDate().charAt(4)
							+ "" + m.getDate().charAt(5) + "." + m.getDate().charAt(0) + "" + m.getDate().charAt(1) + ""
							+ m.getDate().charAt(2) + "" + m.getDate().charAt(3);
					array[rowIndex][0] = niceDate;
					array[rowIndex][1] = m.getTime();
					array[rowIndex][2] = p.getComName();
					array[rowIndex][3] = m.getTitle();
					array[rowIndex][4] = "";
					rowIndex++;
				}
			} else
				asa.address.logger.Logger.logAdd("Attention! To much masses, longer then file!");
		}
		try {
			we.write(array);
		} catch (WriteException | IOException e) {
			asa.address.logger.Logger.logAdd(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}

		dialogStage.close();

	}

	/**
	 * Called when the user clicks the "+" button.
	 */
	public void handlePlus() {
		Mass m = mainApp.showMassEdit();
		masses.add(m);
		String niceDate = m.getDate().charAt(6) + "" + m.getDate().charAt(7) + "." + m.getDate().charAt(4) + ""
				+ m.getDate().charAt(5) + "." + m.getDate().charAt(0) + "" + m.getDate().charAt(1) + ""
				+ m.getDate().charAt(2) + "" + m.getDate().charAt(3);

		notations.add(niceDate + " " + m.getTime() + " " + m.getTitle());
		list.setItems(notations);
	}

	/**
	 * Called when the user clicks the "-" button.
	 */
	public void handleMinus() {
		int index = list.getSelectionModel().getSelectedIndex();
		masses.remove(index);
		notations.remove(index);
		list.setItems(notations);
	}
}
