package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.ValidationException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import kz.aksay.polygraph.entity.EmployeeType;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.EmployeeTypeFX;
import kz.aksay.polygraph.service.EmployeeTypeService;
import kz.aksay.polygraph.service.MaterialTypeService;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class EmployeeTypeTableViewController implements Initializable, SessionAware {
	@FXML	private TableView<EmployeeTypeFX> tableView;
	@FXML	private TextField nameField;
	@FXML	private Label validationLabel;
	
	private EmployeeTypeService employeeTypeService 
		= ContextUtils.getBean(EmployeeTypeService.class);
	
	private Map<String, Object> session;
	
	private User user;
	
	@FXML
	protected void add(ActionEvent event) {
		ObservableList<EmployeeTypeFX> data = tableView.getItems();
		EmployeeType newEmployeeType = create(nameField.getText()); 
		EmployeeTypeFX employeeTypeFX = new EmployeeTypeFX(newEmployeeType);
		data.add(employeeTypeFX);
		save(newEmployeeType);
		nameField.setText("");
		
	}
	
	@FXML
	protected void update(
			TableColumn.CellEditEvent<EmployeeTypeFX, String> cellEditEvent) {
		EmployeeTypeFX employeeTypeFX = cellEditEvent.getRowValue();
		EmployeeType employeeType = employeeTypeFX.getEmployeeType(); 
		employeeType.setName(cellEditEvent.getNewValue());
		employeeType.setUpdatedAt(new Date());
		employeeType.setUpdatedBy(SessionUtil.retrieveUser(session));
		
		save(employeeType);
	}
	
	protected void save(EmployeeType entity) {
		try {
			employeeTypeService.save(entity);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected EmployeeType create(String name) {
		EmployeeType et = new EmployeeType();
		et.setCreatedAt(new Date());
		et.setCreatedBy(SessionUtil.retrieveUser(session));
		et.setName(name.toString());
		return et;
	}

	public TableView<EmployeeTypeFX> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<EmployeeTypeFX> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		List<EmployeeType> employeeTypes = employeeTypeService.findAll();
		List<EmployeeTypeFX> employeeTypesFX 
			= EmployeeTypeFX.contvertListEntityToFX(employeeTypes);
		tableView.getItems().setAll(employeeTypesFX);
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
