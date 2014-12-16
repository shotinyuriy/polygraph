package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.util.SessionAware;

public class EmployeeTypeTableViewController implements Initializable, SessionAware {
	@FXML	private TableView<User.Role> tableView;
	@FXML	private TextField nameField;
	@FXML	private Label validationLabel;
	
	private Map<String, Object> session;

	public TableView<User.Role> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<User.Role> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		List<User.Role> employeeTypes = Arrays.asList(User.Role.values());
		tableView.getItems().setAll(employeeTypes);
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
