package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.validation.ValidationException;

import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.EmployeeType;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entityfx.EmployeeTypeFX;
import kz.aksay.polygraph.service.EmployeeService;
import kz.aksay.polygraph.service.EmployeeTypeService;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.FormatUtil;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

import org.springframework.context.ApplicationContext;

public class EmployeeFormController implements Initializable, SessionAware, ParametersAware {

	private ApplicationContext applicationContext = ContextUtils.getApplicationContext();
	
	private EmployeeService employeeService;
	private EmployeeTypeService employeeTypeService;
	
	public EmployeeFormController() {
		employeeService = applicationContext.getBean(EmployeeService.class);
		employeeTypeService = applicationContext.getBean(EmployeeTypeService.class);
	}
	
	@FXML
	private Label employeeIdLabel;
	
	@FXML
	private TextField firstNameField;
	
	@FXML
	private TextField lastNameField;
	
	@FXML
	private TextField middleNameField;
	
	@FXML
	private TextField birthDateField;
	
	@FXML
	private Label birthDateValidator;
	
	@FXML private ComboBox<EmployeeTypeFX> employeeTypeCombo;
	
	@FXML private Label validationLabel;

	private Map<String, Object> session;

	private Map<String, Object> parameters;
	
	@FXML
	public void save(ActionEvent actionEvent) {
		
		Long employeeId = null;
		if(employeeIdLabel.getText() != null && !employeeIdLabel.getText().isEmpty()) {
			employeeId = Long.valueOf(employeeIdLabel.getText());
		}
		
		Employee employee = null;
		Person person = null;
		if(employeeId != null) {
			employee = employeeService.find(employeeId);
		}
		if (employee == null) {
			employee = new Employee();
			employee.setCreatedAt(new Date());
			employee.setCreatedBy(SessionUtil.retrieveUser(session));
		}
		else {
			employee.setUpdatedAt(new Date());
			employee.setUpdatedBy(SessionUtil.retrieveUser(session));
			
			person = employee.getPerson();
		}
		
		if(person == null) {
			person = new Person();
			person.setCreatedAt(new Date());
			person.setCreatedBy(SessionUtil.retrieveUser(session));
			employee.setPerson(person);
		} else {
			person.setUpdatedAt(new Date());
			person.setUpdatedBy(SessionUtil.retrieveUser(session));
		}
		
		person.setLastName(lastNameField.getText());
		person.setFirstName(firstNameField.getText());
		person.setMiddleName(middleNameField.getText());
		
		try {
			person.setBirthDate(FormatUtil.dateFormatter.parse(birthDateField.getText()));
		}
		catch(ParseException pe) {
			birthDateValidator.setText(
					"Дата рождения указывается в формате "
							+FormatUtil.DATE_FORMAT_DESCRIPTION);
		}
		
		employee.setType(retrieveEmployeeType());
		
		try {
			employeeService.checkPersonAndSave(employee);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private EmployeeType retrieveEmployeeType() {
		EmployeeTypeFX employeeTypeFX 
			= employeeTypeCombo.getSelectionModel().getSelectedItem();
		if(employeeTypeFX != null) {
			return employeeTypeFX.getEmployeeType();
		}
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<EmployeeType> employeeTypes = employeeTypeService.findAll();
		List<EmployeeTypeFX> employeeTypesFX 
			= EmployeeTypeFX.contvertListEntityToFX(employeeTypes);
		employeeTypeCombo.getItems().addAll(employeeTypesFX);
	}
	
	private void initializeByParameters() {
		if(parameters != null) {
			Long employeeId = ParametersUtil.extractParameter(
					parameters, ParameterKeys.EMPLOYEE_ID, Long.class);
			this.fillForm(employeeId);
		}
	}
	
	public void fillForm(Long employeeId) {
		if(employeeId != null) {
			Employee employee = employeeService.find(employeeId);
			if(employee != null) {
				employeeIdLabel.setText(employee.getId().toString());
				Person person = employee.getPerson();
				if(person != null) {
					firstNameField.setText(person.getFirstName());
					lastNameField.setText(person.getLastName());
					middleNameField.setText(person.getMiddleName());
					if(person.getBirthDate() != null) {
						birthDateField.setText(FormatUtil.dateFormatter.format(
								person.getBirthDate()));
					}
				}
				selectEqualEmployeeType(employee.getType());
			}
		}
	}

	private void selectEqualEmployeeType(EmployeeType type) {
		if(type != null) {
			for(EmployeeTypeFX employeeTypeFX : employeeTypeCombo.getItems()) {
				if(type.equals(employeeTypeFX.getEmployeeType())) {
					employeeTypeCombo.getSelectionModel().select(employeeTypeFX);
				}
			}
		}
		else {
			employeeTypeCombo.getSelectionModel().select(null);
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
		initializeByParameters();
	}
}
