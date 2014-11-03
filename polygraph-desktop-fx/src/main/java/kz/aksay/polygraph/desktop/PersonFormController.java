package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.ValidationException;

import org.springframework.context.ApplicationContext;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.service.PersonService;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.FormatUtil;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class PersonFormController implements Initializable, SessionAware, ParametersAware {

	private ApplicationContext applicationContext = ContextUtils.getApplicationContext();
	
	private PersonService personService;
	
	public PersonFormController() {
		personService = applicationContext.getBean(PersonService.class);
	}
	
	@FXML
	private Label personIdLabel; 
	
	@FXML
	private GridPane personContainer;
	
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
	
	@FXML private Label validationLabel;

	private Map<String, Object> session;

	private Map<String, Object> parameters;
	
	@FXML
	public void savePerson(ActionEvent actionEvent) {
		
		Long personId = null;
		if(personIdLabel.getText() != null && !personIdLabel.getText().isEmpty()) {
			personId = Long.valueOf(personIdLabel.getText());
		}
		
		Person person = null;
		if(personId != null) {
			person = personService.find(personId);
		}
		if (person == null) {
			person = new Person();
			person.setCreatedAt(new Date());
			person.setCreatedBy(SessionUtil.retrieveUser(session));
		}
		else {
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
		
		try {
			personService.save(person);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeForm(ActionEvent actionEvent) {
		StartingPane.popScene();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeByParameters();
	}
	
	private void initializeByParameters() {
		if(parameters != null) {
			Long personId = ParametersUtil.extractParameter(
					parameters, ParameterKeys.PERSON_ID, Long.class);
			this.fillForm(personId);
		}
	}
	
	public void fillForm(Long personId) {
		if(personId != null) {
			Person person = personService.find(personId);
			personIdLabel.setText(person.getId().toString());
			firstNameField.setText(person.getFirstName());
			lastNameField.setText(person.getLastName());
			middleNameField.setText(person.getMiddleName());
			if(person.getBirthDate() != null) {
				birthDateField.setText(FormatUtil.dateFormatter.format(person.getBirthDate()));
			}
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
