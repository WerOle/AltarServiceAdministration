package asa.address.plan;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import asa.address.model.Person;
import asa.address.util.DateUtil;
import javafx.collections.ObservableList;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Handling the writing of a xls-file for saving the masses.
 *
 * @author Ole Werger
 */
public class WriteExcel {

	private WritableCellFormat arialBoldUnderline;
	private WritableCellFormat arial;
	private WritableCellFormat arialBold;
	private String inputFile;

	public void setOutputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getOutputFile() {
		return inputFile;
	}

	public void write(ObservableList<Person> pListe) throws IOException, WriteException {

		File file = new File(inputFile);
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setEncoding("UTF-8");
		wbSettings.setLocale(new Locale("en", "EN"));
		/*
		 * Workbook wbOrigin = null; try { wbOrigin = Workbook.getWorkbook(file,
		 * wbSettings); } catch (BiffException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
		WritableWorkbook workbook = Workbook.createWorkbook(file);

		workbook.createSheet("MD-Liste", 0);
		WritableSheet excelSheet = workbook.getSheet(0);

		System.out.println("alles gefunden!");

		createLabel(excelSheet);
		// createContent(excelSheet);
		createPlan(excelSheet, pListe);

		workbook.write();
		workbook.close();
	}

	public void write(String[][] array) throws IOException, WriteException {
		File file = new File(inputFile);
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setEncoding("UTF-8");
		wbSettings.setLocale(new Locale("en", "EN"));
		/*
		 * Workbook wbOrigin = null; try { wbOrigin = Workbook.getWorkbook(file,
		 * wbSettings); } catch (BiffException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
		WritableWorkbook workbook = Workbook.createWorkbook(file);

		workbook.createSheet("Allgemein", 1);
		WritableSheet excelSheet = workbook.getSheet("Allgemein");

		System.out.println("alles gefunden!");

		createComplete(excelSheet, array);

		workbook.write();
		workbook.close();
	}

	private void createComplete(WritableSheet sheet, String[][] array) {
		addLabel(sheet, 0, 2, "DATUM", true);// A = Datum
		addLabel(sheet, 1, 2, "ZEIT", true);// B = Uhrzeit
		addLabel(sheet, 2, 2, "NAME", true);// C = Name
		addLabel(sheet, 3, 2, "TITEL", true);// D = Titel

		for (int i = 0; i < array.length; i++) {
			System.out.println((i + 1) + ". Zeile wird geschrieben von " + array.length);
			addLabel(sheet, 0, i + 3, array[i][0], false);// A = Datum
			addLabel(sheet, 1, i + 3, array[i][1], false);// B = Uhrzeit
			addLabel(sheet, 2, i + 3, array[i][2], false);// C = Name
			addLabel(sheet, 3, i + 3, array[i][3], false);// D = Titel
			addLabel(sheet, 4, i + 3, array[i][4], false);// E = noch leer
			System.out.println(array[i][0]);

			if (array[i][0] != null && array[i][0].length() > 5) {
				char[] ca = array[i][0].toCharArray();
				String tag = ca[0] + "" + ca[1];
				String monat = ca[3] + "" + ca[4];
				String jahr = ca[6] + "" + ca[7] + "" + ca[8] + "" + ca[9];
				if (ca.length > 1) {
					addLabel(sheet, 5, i + 3, tag, false);// F = Tag
					addLabel(sheet, 6, i + 3, monat, false);// G = Monat
					addLabel(sheet, 7, i + 3, jahr, false);// H = Jahr
					addLabel(sheet, 8, i + 3, jahr + "" + monat + "" + tag, false);// I
																					// =
																					// Datum
				}
			}
		}
	}

	private void createPlan(WritableSheet sheet, ObservableList<Person> pListe) throws WriteException {
		int i = 0;
		String lastname;
		String firstname;
		String gebDates;
		String streets;
		String telNumbers;
		String group;
		String email1;
		String email2;

		Date date = java.util.Calendar.getInstance().getTime();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy");
		String dateString = dateFormatter.format(date);

		SimpleDateFormat dateFormatter2 = new SimpleDateFormat("dd.MM.yyyy");
		String dateString2 = dateFormatter2.format(date);

		addLabel(sheet, 0, 0, "Messdienerliste " + dateString + "   Stand: " + dateString2, true);
		addLabel(sheet, 0, 1, "#", true);
		addLabel(sheet, 1, 1, "Nachname", true);
		addLabel(sheet, 2, 1, "Vorname", true);
		addLabel(sheet, 3, 1, "Telefonnummer", true);
		addLabel(sheet, 4, 1, "Adresse", true);
		addLabel(sheet, 5, 1, "Geburtsdatum", true);

		for (Person p : pListe) {

			lastname = (p.getLastName());
			firstname = (p.getFirstName());
			gebDates = (DateUtil.format(p.getBirthday()));
			streets = (p.getStreet());
			telNumbers = (Integer.toString(p.getTelNumber()));
			group = (p.getGroup());

			boolean bold = false;

			if (!group.equals("5Sonstige")) {

				if (group.equals("4Neu"))
					bold = true;
				addLabel(sheet, 0, i + 2, (i + 1) + "", bold);
				addLabel(sheet, 1, i + 2, lastname, bold);
				addLabel(sheet, 2, i + 2, firstname, bold);
				addLabel(sheet, 3, i + 2, telNumbers, bold);
				addLabel(sheet, 4, i + 2, streets, bold);
				addLabel(sheet, 5, i + 2, gebDates, bold);
				i++;
			}
		}
		System.out.println("FERTIG!");
	}

	private void createLabel(WritableSheet sheet) {
		// Lets create a times font
		WritableFont arial12pt = new WritableFont(WritableFont.ARIAL, 12);
		// Define the cell format
		arial = new WritableCellFormat(arial12pt);
		// Lets automatically wrap the cells
		try {
			arial.setWrap(true);
		} catch (WriteException e) {
			System.out.println("WriteException " + e);
			e.printStackTrace();
		}

		// Create create a bold font with unterlines
		WritableFont arial12ptBoldUnderline = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,
				UnderlineStyle.SINGLE);
		arialBoldUnderline = new WritableCellFormat(arial12ptBoldUnderline);
		// Lets automatically wrap the cells
		try {
			arialBoldUnderline.setWrap(true);
		} catch (WriteException e) {
			System.out.println("WriteException " + e);
			e.printStackTrace();
		}

		WritableFont arial12ptBold = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);
		arialBold = new WritableCellFormat(arial12ptBold);
		// Lets automatically wrap the cells
		try {
			arialBold.setWrap(true);
		} catch (WriteException e) {
			System.out.println("WriteException " + e);
			e.printStackTrace();
		}

		CellView cv = new CellView();
		cv.setFormat(arial);
		cv.setFormat(arialBoldUnderline);
		cv.setFormat(arialBold);
		cv.setAutosize(true);

		// Write a few headers
		// addCaption(sheet, 0, 0, "MD-Plan");
		// addCaption(sheet, 0, 1, "");

	}

	private void createContent(WritableSheet sheet) throws WriteException, RowsExceededException {
		// Write a few number
		for (int i = 1; i < 10; i++) {
			// First column
			addNumber(sheet, 0, i, i + 10);
			// Second column
			addNumber(sheet, 1, i, i * i);
		}
		// Lets calculate the sum of it
		StringBuffer buf = new StringBuffer();
		buf.append("SUM(A2:A10)");
		Formula f = new Formula(0, 10, buf.toString());
		sheet.addCell(f);
		buf = new StringBuffer();
		buf.append("SUM(B2:B10)");
		f = new Formula(1, 10, buf.toString());
		sheet.addCell(f);

		// Now a bit of text
		for (int i = 12; i < 20; i++) {
			// First column
			addLabel(sheet, 0, i, "Boring text " + i, false);
			// Second column
			addLabel(sheet, 1, i, "Another text", false);
		}
	}

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, arialBoldUnderline);
		sheet.addCell(label);
	}

	private void addNumber(WritableSheet sheet, int column, int row, Integer integer)
			throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, integer, arial);
		sheet.addCell(number);
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s, boolean bold) {
		arial = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD));
		arialBold = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
		Label label;
		if (bold)
			label = new Label(column, row, s, arialBold);
		else
			label = new Label(column, row, s, arial);
		// System.out.println(column+" "+row+" "+s+" "+arial.toString());
		try {
			sheet.addCell(label);
		} catch (RowsExceededException e1) {
			System.out.println("RowsExceededException " + e1);
			e1.printStackTrace();
		} catch (WriteException e1) {
			System.out.println("WriteException " + e1);
			e1.printStackTrace();
		}
	}

	/*
	 * public static void main(String[] args) throws WriteException, IOException
	 * { WriteExcel test = new WriteExcel(); test.setOutputFile("testerei.xls");
	 * test.write();
	 * System.out.println("Please check the result file under testerei.xls "); }
	 */
}