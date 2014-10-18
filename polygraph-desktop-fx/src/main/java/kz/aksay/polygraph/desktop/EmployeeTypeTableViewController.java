package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.ValidationException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import kz.aksay.polygraph.entity.EmployeeType;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.EmployeeTypeService;
import kz.aksay.polygraph.service.MaterialTypeService;
import kz.aksay.polygraph.session.SessionAware;
import kz.aksay.polygraph.session.SessionUtil;
import kz.aksay.polygraph.util.ContextUtils;

public class EmployeeTypeTableViewController implements Initializable, SessionAware {
	@FXML	private TableView<EmployeeType> tableView;
	@FXML	private TextField nameField;
	@FXML	private Label validationLabel;
	
	private EmployeeTypeService employeeTypeService 
		= ContextUtils.getBean(EmployeeTypeService.class);
	
	private Map<String, Object> session;
	
	private User user;
	
	@FXML
	protected void addMaterialType(ActionEvent event) {
		ObservableList<EmployeeType> data = tableView.getItems();
		EmployeeType newEmployeeType = createMaterialType(nameField.getText()); 
		data.add(newEmployeeType);
		try {
			employeeTypeService.save(newEmployeeType);
			nameField.setText("");
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	protected EmployeeType createMaterialType(String name) {
		EmployeeType et = new EmployeeType();
		et.setCreatedAt(new Date());
		et.setCreatedBy(SessionUtil.retrieveUser(session));
		et.setName(name.toString());
		return et;
	}

	public TableView<EmployeeType> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<EmployeeType> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		tableView.getItems().setAll(employeeTypeService.findAll());
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
