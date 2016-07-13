package asa.address.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import asa.address.util.LocalDateAdapter;

/**
 * Model class for a Person.
 *
 * @author Marco Jakob changed by: Ole Werger
 */
public class Person {

	private final StringProperty firstName;
	private final StringProperty lastName;
	private final StringProperty street;
	private final IntegerProperty postalCode;
	private final StringProperty city;
	private final ObjectProperty<LocalDate> birthday;
	private final IntegerProperty telNumber;
	private final StringProperty group;
	private final StringProperty email1;
	private final StringProperty email2;
	private final StringProperty comName;

	/**
	 * Default constructor.
	 */
	public Person() {
		this(null, null);
	}

	/**
	 * Constructor with some initial data.
	 * 
	 * @param firstName
	 * @param lastName
	 */
	public Person(String firstName, String lastName) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);

		// Some initial dummy data, just for convenient testing.
		this.street = new SimpleStringProperty("some street");
		this.postalCode = new SimpleIntegerProperty(1234);
		this.city = new SimpleStringProperty("some city");
		this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
		this.telNumber = new SimpleIntegerProperty(987654321);
		this.group = new SimpleStringProperty("5Sonstige");
		this.email1 = new SimpleStringProperty("test@test.com");
		this.email2 = new SimpleStringProperty("test@test.com");
		this.comName = new SimpleStringProperty(firstName + " " + lastName);
	}

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
		this.comName.set(firstName + " " + this.lastName.get());
	}

	public StringProperty firstNameProperty() {
		return firstName;
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String lastName) {
		this.lastName.set(lastName);
		this.comName.set(this.firstName.get() + " " + lastName);
	}

	public StringProperty lastNameProperty() {
		return lastName;
	}

	public String getStreet() {
		return street.get();
	}

	public void setStreet(String street) {
		this.street.set(street);
	}

	public StringProperty streetProperty() {
		return street;
	}

	public int getPostalCode() {
		return postalCode.get();
	}

	public void setPostalCode(int postalCode) {
		this.postalCode.set(postalCode);
	}

	public IntegerProperty postalCodeProperty() {
		return postalCode;
	}

	public void setTelNumber(int phoneNumber) {
		this.telNumber.set(phoneNumber);
	}

	public IntegerProperty phoneNumberProperty() {
		return telNumber;
	}

	public int getTelNumber() {
		return telNumber.get();
	}

	public String getCity() {
		return city.get();
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public StringProperty cityProperty() {
		return city;
	}

	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getBirthday() {
		return birthday.get();
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday.set(birthday);
	}

	public ObjectProperty<LocalDate> birthdayProperty() {
		return birthday;
	}

	public StringProperty groupProperty() {
		return group;
	}

	public StringProperty email1Property() {
		return email1;
	}

	public StringProperty email2Property() {
		return email2;
	}

	public String getGroup() {
		return group.get();
	}

	public String getEmail1() {
		return email1.get();
	}

	public String getEmail2() {
		return email2.get();
	}

	public void setGroup(String group) {
		this.group.set(group);
	}

	public void setEmail1(String email1) {
		this.email1.set(email1);
	}

	public void setEmail2(String email2) {
		this.email2.set(email2);
	}

	/**
	 * @return the comName
	 */
	public StringProperty getComNameProperty() {
		return comName;
	}

	public String getComName() {
		return this.comName.get();
	}

	public void setComName(String firstName, String lastName) {
		this.comName.set(firstName + " " + lastName);
	}

}