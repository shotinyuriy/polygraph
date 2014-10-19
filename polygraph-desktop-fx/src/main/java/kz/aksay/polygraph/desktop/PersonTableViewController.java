package kz.aksay.polygraph.desktop;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.sun.javafx.collections.ObservableListWrapper;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.service.PersonService;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class PersonTableViewController implements Initializable, SessionAware {
	
	private PersonService personService;
	
	@FXML
	private TableView<Person> personTable;

	private Map<String, Object> session;
	
	public PersonTableViewController() {
		personService = StartingPane.getBean(PersonService.class);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		List<Person> personList = personService.findAll();
		
		personTable.getItems().setAll(personList);
	}
	
	public void openPersonForm(ActionEvent actionEvent) {
		Person person = personTable.getSelectionModel().getSelectedItem();
		
		if(person != null) {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put(ParameterKeys.PERSON_ID, person.getId());
			SessionUtil.loadFxmlNodeWithSession(
					getClass(), "person_form.fxml", session, parameters);
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

}
