package asa.address.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for a Mass (church).
 *
 * @author Ole Werger
 */
public class Mass {
	private final StringProperty titel;
	private final ObjectProperty<LocalDate> date;
	private final ObjectProperty<LocalTime> time;
	private ObservableList<Person> ass;// altar services

	/**
	 * Default constructor.
	 */
	public Mass() {
		this(null, null, null);
	}

	/**
	 * Constructor with some initial data.
	 * 
	 * @param firstName
	 * @param lastName
	 */
	public Mass(String titel, String date, String time) {
		this.titel = new SimpleStringProperty(titel);
		this.date = new SimpleObjectProperty<LocalDate>(LocalDate.of(2000, 1, 1));
		this.time = new SimpleObjectProperty<LocalTime>(LocalTime.of(11, 0));

		if (date != null) {
			char[] ca = date.toCharArray();
			this.date.set(LocalDate.of(Integer.parseInt(ca[6] + "" + ca[7] + "" + ca[8] + "" + ca[9]),
					Integer.parseInt(ca[3] + "" + ca[4]), Integer.parseInt(ca[0] + "" + ca[1])));
		}
		if (time != null) {
			char[] ct = time.toCharArray();
			this.time.set(LocalTime.of(Integer.parseInt(ct[0] + "" + ct[1]), Integer.parseInt(ct[3] + "" + ct[4])));
		}

		ass = FXCollections.observableArrayList();
	}

	/**
	 * @return the titel
	 */
	public StringProperty getTitelProperty() {
		return titel;
	}

	/**
	 * @return the date
	 */
	public ObjectProperty<LocalDate> getDateProperty() {
		return date;
	}

	/**
	 * @return the time
	 */
	public ObjectProperty<LocalTime> getTimeProperty() {
		return time;
	}

	/**
	 * @return the mds
	 */
	public ObservableList<Person> getMdsProperty() {
		return ass;
	}

	public String getTitle() {
		return titel.getValue();
	}

	public String getDate() {
		String year = date.get().getYear() + "";
		String month = date.get().getMonthValue() + "";

		if (month.length() < 2)
			month = "0" + month;
		String day = date.get().getDayOfMonth() + "";
		if (day.length() < 2)
			day = "0" + day;

		return year + "" + month + "" + day;

	}

	public String getTime() {
		String hours = time.get().getHour() + "";

		if (hours.length() < 2)
			hours = "0" + hours;
		String min = time.get().getMinute() + "";
		if (min.length() < 2)
			min = "0" + min;

		return hours + "." + min;

	}

	public ObservableList<Person> getMDs() {
		return ass;
	}

	public void setTitle(String title) {
		this.titel.set(title);
	}

	public void setDate(String date) {
		if (date != null) {
			char[] ca = date.toCharArray();
			this.date.set(LocalDate.of(Integer.parseInt(ca[6] + "" + ca[7] + "" + ca[8] + "" + ca[9]),
					Integer.parseInt(ca[3] + "" + ca[4]), Integer.parseInt(ca[0] + "" + ca[1])));
		}
	}

	public void setTime(String time) {
		if (time != null) {
			char[] ct = time.toCharArray();
			this.time.set(LocalTime.of(Integer.parseInt(ct[0] + "" + ct[1]), Integer.parseInt(ct[3] + "" + ct[4])));
		}
	}

	/**
	 * 
	 * @param ass
	 *            lsit of altar services
	 */
	public void setAss(ObservableList<Person> ass) {
		this.ass = ass;
	}

}
