package kz.aksay.polygraph.desktop.model;

import javafx.beans.property.SimpleStringProperty;

public class Person {

	private final SimpleStringProperty firstName = new SimpleStringProperty("");
	private final SimpleStringProperty lastName = new SimpleStringProperty("");
	private final SimpleStringProperty email = new SimpleStringProperty("");
	
	public Person() {
		this("", "", "");
	}
	
	public Person(String firstName, String lastName, String email) {
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
	}
	
	public String getFirstName() {
		return firstName.get();
	}
	
	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}

	public String getLastName() {
		return lastName.get();
	}
	
	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}

	public String getEmail() {
		return email.get();
	}
	
	public void setEmail(String email) {
		this.email.set(email);
	}
}
