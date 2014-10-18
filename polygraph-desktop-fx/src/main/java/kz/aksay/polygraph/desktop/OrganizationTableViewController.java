package kz.aksay.polygraph.desktop;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.service.OrganizationService;

public class OrganizationTableViewController implements Initializable {
	
	private OrganizationService organizationService;
	
	@FXML
	private TableView<Organization> organizationTable;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		organizationService = StartingPane.getBean(OrganizationService.class);
		organizationTable.getItems().setAll(organizationService.findAll());
	}
	
	public void openOrganizationForm(ActionEvent actionEvent) {
		Organization organization = organizationTable.getSelectionModel().getSelectedItem();
		
		GridPane gridPane = null;
		try {
			URL url = getClass().getResource("organization_form.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader(url);
			gridPane = (GridPane)fxmlLoader.load();
			fxmlLoader.getRoot();
			OrganizationFormController organizationFormController 
				= (OrganizationFormController)fxmlLoader.getController();
			organizationFormController.fillForm(organization.getId());
			StartingPane.pushScene(gridPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		
	}

}
