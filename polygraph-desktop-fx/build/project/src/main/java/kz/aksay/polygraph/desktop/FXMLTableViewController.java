package kz.aksay.polygraph.desktop;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import kz.aksay.polygraph.desktop.model.Person;

public class FXMLTableViewController {
	@FXML	private TableView<Person> tableView;
	@FXML	private TextField firstNameField;
	@FXML	private TextField lastNameField;
	@FXML	private TextField emailField;
	
	@FXML
	protected void addPerson(ActionEvent event) {
		ObservableList<Person> data = tableView.getItems();
		data.add(new Person(firstNameField.getText(), 
				lastNameField.getText(), emailField.getText()));
		
		firstNameField.setText("");
		lastNameField.setText("");
		emailField.setText("");
	}

	public TableView<Person> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<Person> tableView) {
		this.tableView = tableView;
	}

	public TextField getFirstNameField() {
		return firstNameField;
	}

	public void setFirstNameField(TextField firstNameField) {
		this.firstNameField = firstNameField;
	}

	public TextField getLastNameField() {
		return lastNameField;
	}

	public void setLastNameField(TextField lastNameField) {
		this.lastNameField = lastNameField;
	}

	public TextField getEmailField() {
		return emailField;
	}

	public void setEmailField(TextField emailField) {
		this.emailField = emailField;
	}
}
