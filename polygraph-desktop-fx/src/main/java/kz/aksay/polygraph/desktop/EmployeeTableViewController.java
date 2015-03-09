package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.service.EmployeeService;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class EmployeeTableViewController implements Initializable,
		SessionAware {
	
	private IEmployeeService employeeService;
	
	@FXML private TableView<EmployeeFX> employeeTable;
	private Map<String, Object> session;

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		employeeService = StartingPane.getBean(IEmployeeService.class);
		List<Employee> employees = employeeService.findAll();
		Collection<EmployeeFX> employeesFX 
			= EmployeeFX.contvertListEntityToFX(employees);
		employeeTable.getItems().addAll(employeesFX);
		
		employeeTable.setRowFactory(new Callback<TableView<EmployeeFX>, TableRow<EmployeeFX>>() {
			
			@Override
			public TableRow<EmployeeFX> call(TableView<EmployeeFX> param) {
				// TODO Auto-generated method stub
				TableRow<EmployeeFX> tableRow = new TableRow<>();
				tableRow.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						int clickCount = event.getClickCount();
						if(clickCount == 2) {
							openEmployeeForm(new ActionEvent(event.getSource(), 
								event.getTarget()));
						}
					}
					
				});
				
				return tableRow;
			}
		});
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
