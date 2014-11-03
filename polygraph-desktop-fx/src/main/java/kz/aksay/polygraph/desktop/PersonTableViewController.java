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
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entityfx.PersonFX;
import kz.aksay.polygraph.service.PersonService;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class PersonTableViewController implements Initializable, SessionAware {
	
	private PersonService personService;
	
	@FXML
	private TableView<PersonFX> personTable;

	private Map<String, Object> session;
	
	public PersonTableViewController() {
		personService = StartingPane.getBean(PersonService.class);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<Person> personList = personService.findAll();
		List<PersonFX> personFXList = PersonFX.convertListEntityToFX(personList);
		personTable.getItems().setAll(personFXList);
	}
	
	public void openPersonForm(ActionEvent actionEvent) {
		PersonFX personFX = personTable.getSelectionModel().getSelectedItem();
		if(personFX != null) {
			Person person = personFX.getPerson();
			if(person != null) {
				Map<String, Object> parameters = new HashMap<>();
				parameters.put(ParameterKeys.PERSON_ID, person.getId());
				MainMenu mainMenu = SessionUtil.retrieveMainMenu(session);
				mainMenu.loadFxmlAndOpenInTab(
						"person_form.fxml", person.getFullName(), parameters);
			}
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

}
