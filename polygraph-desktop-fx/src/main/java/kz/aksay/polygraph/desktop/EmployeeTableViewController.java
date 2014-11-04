package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.service.EmployeeService;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class EmployeeTableViewController implements Initializable,
		SessionAware {
	
	private EmployeeService employeeService;
	
	@FXML private TableView<EmployeeFX> employeeTable;
	private Map<String, Object> session;

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		employeeService = StartingPane.getBean(EmployeeService.class);
		List<Employee> employees = employeeService.findAll();
		List<EmployeeFX> employeesFX 
			= EmployeeFX.contvertListEntityToFX(employees);
		employeeTable.getItems().addAll(employeesFX);
	}
	
	@FXML
	public void openEmployeeForm(ActionEvent actionEvent) {
		EmployeeFX employeeFX 
			= employeeTable.getSelectionModel().getSelectedItem();
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put(ParameterKeys.EMPLOYEE_ID, employeeFX.getId());
		MainMenu mainMenu = SessionUtil.retrieveMainMenu(session);
		if(mainMenu != null) {
			mainMenu.loadFxmlAndOpenInTab(
					"employee_form.fxml", employeeFX.getFullName(), parameters);
		}
	}
}
