package asa.address.list;

import java.io.File;
import java.io.IOException;

import asa.address.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 * Handling the reading of a xls-file with the masses in it.
 *
 * @author Ole Werger
 */
public class ReadExcelPerson {

	private String inputFile;
	String[] times;
	ObservableList<Person> persons;

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void read(String readDate, ObservableList<Person> allPersons) throws IOException {
		persons = FXCollections.observableArrayList();
		String date = readDate;

		File inputWorkbook = new File(inputFile);
		Workbook w;
		try {
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("ISO-8859-1");
			ws.setExcelDisplayLanguage("DE");
			ws.setExcelRegionalSettings("DE");
			w = Workbook.getWorkbook(inputWorkbook, ws);

			Sheet sheet = w.getSheet(0);
			int count = 0;
			int countBefore = 0;

			for (int i = 0; i < sheet.getRows(); i++) {
				if (date.equals(sheet.getCell(8, i).getContents()))
					count++;
				else if (count == 0)
					countBefore++;

			}
			asa.address.logger.Logger.logAdd("date: " + date + " date: " + sheet.getCell(8, 0).getContents());
			asa.address.logger.Logger.logAdd("date: " + date + " date: " + sheet.getCell(8, 65).getContents());
			asa.address.logger.Logger.logAdd("testtime: " + sheet.getCell(1, 0).getContents());
			asa.address.logger.Logger.logAdd("testname: " + sheet.getCell(2, 0).getContents());
			asa.address.logger.Logger.logAdd("testMail1: " + sheet.getCell(3, 0).getContents());
			asa.address.logger.Logger
					.logAdd("count: " + count + "   before: " + countBefore + "     maxRows: " + sheet.getRows());
			times = new String[count];
			int n = 0;
			for (int j = countBefore; j < sheet.getRows(); j++) {
				if (date.equals(sheet.getCell(8, j).getContents())) {
					String name = sheet.getCell(2, j).getContents();
					String comName = null;
					for (Person p : allPersons) {

						if (name.equals(p.getComName())) {
							comName = name;
							persons.add(p);
						}
					}

					times[n] = sheet.getCell(1, j).getContents();
					if (comName != null)
						asa.address.logger.Logger.logAdd("altar service" + (n + 1) + " founded: "
								+ sheet.getCell(2, j).getContents() + "  j:" + j + " comName: " + comName);
					else {

						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error!");
						alert.setHeaderText("Person not found!");
						alert.setContentText(name + "could not be founded!");

						alert.showAndWait();

						for (Person p : allPersons) {
							if (p.getFirstName().equals("OBMs")) {
								persons.add(p);
							}
						}

					}
					n++;
				}
			}

			asa.address.logger.Logger.logAdd("length: " + persons.size());

		} catch (BiffException e) {
			e.printStackTrace();
		}

	}

	public String[][] read() {

		String[][] array = null;

		File inputWorkbook = new File(inputFile);
		Workbook w = null;
		try {

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("ISO-8859-1");
			ws.setExcelDisplayLanguage("DE");
			ws.setExcelRegionalSettings("DE");
			w = Workbook.getWorkbook(inputWorkbook, ws);

		} catch (BiffException e) {
			asa.address.logger.Logger.logAdd("BiffException: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			asa.address.logger.Logger.logAdd("IOException: " + e.getMessage());
			e.printStackTrace();
		}
		Sheet sheet = w.getSheet("Allgemein");
		array = new String[sheet.getRows()][9];
		for (int i = 2; i < sheet.getRows(); i++) {
			array[i - 2][0] = sheet.getCell(0, i).getContents();
			array[i - 2][1] = sheet.getCell(1, i).getContents();
			array[i - 2][2] = sheet.getCell(2, i).getContents();
			array[i - 2][3] = sheet.getCell(3, i).getContents();
			array[i - 2][4] = sheet.getCell(4, i).getContents();
			array[i - 2][5] = sheet.getCell(5, i).getContents();
			array[i - 2][6] = sheet.getCell(6, i).getContents();
			array[i - 2][7] = sheet.getCell(7, i).getContents();
			array[i - 2][8] = sheet.getCell(8, i).getContents();
		}

		return array;
	}

	public int getLength() {
		return persons.size();
	}

	public String getTimes(int i) {
		return times[i];
	}

	public String[] getTimes() {
		return times;
	}

	public Person getPerson(int i) {
		return persons.get(i);
	}

	public ObservableList<Person> getPersons() {
		return persons;
	}

}
