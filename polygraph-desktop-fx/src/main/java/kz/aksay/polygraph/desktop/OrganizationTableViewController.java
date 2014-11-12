package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.service.OrganizationService;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class OrganizationTableViewController implements Initializable, 
	SessionAware {
	
	private OrganizationService organizationService;
	
	@FXML
	private TableView<Organization> organizationTable;

	private Map<String, Object> session;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		organizationService = StartingPane.getBean(OrganizationService.class);
		organizationTable.getItems().setAll(organizationService.findAll());
	}
	
	@FXML
	public void openOrganizationForm(ActionEvent actionEvent) {
		Organization organization = organizationTable.getSelectionModel().getSelectedItem();
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.ORGANIZATION_ID, organization.getId());
		MainMenu mainMenu = SessionUtil.retrieveMainMenu(session);
		if(mainMenu != null) {
			mainMenu.loadFxmlAndOpenInTab("organization_form.fxml", 
					organization.getShortname(), parameters);
		}
	}
	
	@FXML
	public void openNewOrder(ActionEvent actionEvent) {
		Organization organization = organizationTable.getSelectionModel().getSelectedItem();
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.CUSTOMER_ID, organization.getId());
		MainMenu mainMenu = SessionUtil.retrieveMainMenu(session);
		if(mainMenu != null) {
			mainMenu.loadFxmlAndOpenInTab("order_form.fxml", 
					"Новый заказ", parameters);
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

}
