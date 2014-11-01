package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.ValidationException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.service.OrganizationService;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class OrganizationFormController implements Initializable, SessionAware {

	private OrganizationService organizationService;
	
	@FXML private Label idLabel;
	
	@FXML private TextField innField;
	
	@FXML private TextField kppField;
	
	@FXML private TextField fullNameField;
	
	@FXML private TextField shortNameField;
	
	@FXML private Label validationLabel;

	private Map<String, Object> session;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		organizationService = StartingPane.getBean(OrganizationService.class);
	}
	
	@FXML
	public void save(ActionEvent actionEvent) {
		Long id = null;
		if(idLabel.getText() != null && !idLabel.getText().isEmpty()) {
			id = Long.valueOf(idLabel.getText());
		}
		
		Organization organization = null;
		if(id != null) {
			organization = organizationService.find(id);
		}
		if (organization == null) {
			organization = new Organization();
			organization.setCreatedAt(new Date());
			organization.setCreatedBy(SessionUtil.retrieveUser(session));
		}
		else {
			organization.setUpdatedAt(new Date());
			organization.setUpdatedBy(SessionUtil.retrieveUser(session));
		}
		organization.setInn(innField.getText());
		organization.setFullname(fullNameField.getText());
		organization.setKpp(kppField.getText());
		organization.setShortname(shortNameField.getText());
		
		try {
			organizationService.save(organization);
		}
		catch(ValidationException ve) {
			validationLabel.setText(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void cancel(ActionEvent actionEvent) {
		StartingPane.popScene();
	}
	
	public void fillForm(Long id) {
		Organization organization = organizationService.find(id);
		if(organization != null) {
			idLabel.setText(organization.getId().toString());
			innField.setText(organization.getInn());
			kppField.setText(organization.getKpp());
			fullNameField.setText(organization.getFullname());
			shortNameField.setText(organization.getShortname());
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
}
