package asa.address.list;

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
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Handling the writing of a xls-file for saving the person.
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
		wbSettings.setLocale(new Locale("de", "DE"));
		WritableWorkbook workbook = Workbook.createWorkbook(file);

		workbook.createSheet("MD-Liste", 0);
		WritableSheet excelSheet = workbook.getSheet(0);

		System.out.println("alles gefunden!");

		createLabel(excelSheet);
		createPlan(excelSheet, pListe);

		workbook.write();
		workbook.close();
	}

	private void createPlan(WritableSheet sheet, ObservableList<Person> pListe) throws WriteException {
		int i = 0;
		String lastname;
		String firstname;
		String gebDates;
		String streets;
		String telNumbers;
		String group;
		String email1;// used if e-mail should appear in list
		String email2;// used if e-mail should appear in list

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
		// addLabel(sheet, 6, 1, "E-Mail1", true);
		// addLabel(sheet, 7, 1, "E-Mail2", true);

		for (Person p : pListe) {

			lastname = (p.getLastName());
			firstname = (p.getFirstName());
			gebDates = (DateUtil.format(p.getBirthday()));
			streets = (p.getStreet());
			telNumbers = (Integer.toString(p.getTelNumber()));
			group = (p.getGroup());
			email1 = (p.getEmail1());
			email2 = (p.getEmail2());

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
				// addLabel(sheet, 6, i + 2, email1, bold);
				// addLabel(sheet, 7, i + 2, email2, bold);
				i++;
			}
		}
		System.out.println("Finished!");
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

}
