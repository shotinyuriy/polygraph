package kz.aksay.polygraph.desktop;

import java.net.URL;
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
import kz.aksay.polygraph.api.IPersonService;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entityfx.PersonFX;
import kz.aksay.polygraph.service.PersonService;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class PersonTableViewController implements Initializable, SessionAware {
	
	private IPersonService personService 
		= StartingPane.getBean(IPersonService.class);
	
	@FXML
	private TableView<PersonFX> personTable;

	private Map<String, Object> session;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<Person> personList = personService.findAll();
		List<PersonFX> personFXList = PersonFX.convertListEntityToFX(personList);
		personTable.getItems().setAll(personFXList);
		
		personTable.setRowFactory(new Callback<TableView<PersonFX>, TableRow<PersonFX>>() {
			
			@Override
			public TableRow<PersonFX> call(TableView<PersonFX> param) {
				// TODO Auto-generated method stub
				TableRow<PersonFX> tableRow = new TableRow<>();
				tableRow.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						int clickCount = event.getClickCount();
						if(clickCount == 2) {
							openPersonForm(new ActionEvent(event.getSource(), 
								event.getTarget()));
						}
					}
					
				});
				
				return tableRow;
			}
		});
	}
	
	@FXML
	public void openPersonForm(ActionEvent actionEvent) {
		PersonFX personFX = personTable.getSelectionModel().getSelectedItem();
		if(personFX != null) {
			Person person = personFX.getPerson();
			if(person != null) {
				Map<String, Object> parameters = new HashMap<>();
				parameters.put(ParameterKeys.PERSON_ID, person.getId());
				MainMenu mainMenu = SessionUtil.retrieveMainMenu(session);
				mainMenu.loadFxmlAndOpenInTab(
						StartingPane.FXML_ROOT+"person_form.fxml", person.getFullName(), parameters);
			}
		}
	}
	
	@FXML
	public void newPersonForm(ActionEvent actionEvent) {
		MainMenu mainMenu = SessionUtil.retrieveMainMenu(session);
		if(mainMenu != null) {
			mainMenu.loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"person_form.fxml", "Новый клиент физ.лицо");
		}
	}
	
	@FXML
	public void openNewOrderForm(ActionEvent actionEvent) {
		PersonFX personFX = personTable.getSelectionModel().getSelectedItem();
		if(personFX != null) {
			Person person = personFX.getPerson();
			if(person != null) {
				Map<String, Object> parameters = new HashMap<>();
				parameters.put(ParameterKeys.CUSTOMER_ID, person.getId());
				MainMenu mainMenu = SessionUtil.retrieveMainMenu(session);
				mainMenu.loadFxmlAndOpenInTab(
						StartingPane.FXML_ROOT+"order_form.fxml", "Новый заказ клиента "+person.getFullName(), parameters);
			}
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

}
