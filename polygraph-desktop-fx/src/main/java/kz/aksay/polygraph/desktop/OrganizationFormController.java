package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.ValidationException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kz.aksay.polygraph.api.IContractService;
import kz.aksay.polygraph.api.IOrganizationService;
import kz.aksay.polygraph.desktop.fxml.packageInfo;
import kz.aksay.polygraph.entity.Contract;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entityfx.ContractFX;
import kz.aksay.polygraph.entityfx.EntityFX;
import kz.aksay.polygraph.service.OrganizationService;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class OrganizationFormController implements Initializable, SessionAware, 
	ParametersAware {

	private IOrganizationService organizationService 
		= StartingPane.getBean(IOrganizationService.class);
	private IContractService contractService = StartingPane.getBean(IContractService.class);
	
	@FXML private Label idLabel;
	@FXML private TextField innField;
	@FXML private TextField kppField;
	@FXML private TextArea fullNameField;
	@FXML private TextField shortNameField;
	@FXML private Label validationLabel;
	@FXML private TextField emailField;
	@FXML private TextField mobileField;
	@FXML private TextField phoneField;
	@FXML private TextField directorNameField;
	@FXML private Button newOrderButton;
	@FXML private TableView<ContractFX> contractsTableView;

	private Map<String, Object> session;
	private Map<String, Object> parameters;
	private Long organizationId;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) { 
		
		initializeByParameters();
	}
	
	private void initializeByParameters() {
		if(parameters!=null) {
			Long organizationId 
				= ParametersUtil.extractParameter(
						parameters, ParameterKeys.ORGANIZATION_ID, Long.class);
			if(organizationId != null) {
				fillForm(organizationId);
			}
		}
	}

	@FXML
	public void save(ActionEvent actionEvent) {
		Organization organization = null;
		if(organizationId != null) {
			organization = organizationService.find(organizationId);
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
		organization.setDirectorName(directorNameField.getText());
		organization.setKpp(kppField.getText());
		organization.setShortname(shortNameField.getText());
		organization.setEmail(emailField.getText());
		organization.setMobile(mobileField.getText());
		organization.setPhone(phoneField.getText());
		
		try {
			organization = organizationService.save(organization);
			newOrderButton.setVisible(true);
			organizationId = organization.getId();
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
	
	@FXML
	public void addContract(ActionEvent actionEvent) {
		Contract contract = new Contract();
		contract.setParty1(Organization.FIRMA_SERVER_PLUS);
		contract.setParty2(organizationService.find(organizationId));
		
		ContractFX contractFX = new ContractFX(contract);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.ORGANIZATION_FORM, this);
		parameters.put(ParameterKeys.CONTRACT, contractFX);
		
		Parent root = (Parent) SessionUtil.loadFxmlNodeWithSession(
				packageInfo.class, "short_contract_form.fxml", session, parameters);
		
		Stage stage = new Stage(); 
		stage.setScene(new Scene(root));
		stage.setTitle("Новый договор");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
		stage.show();
	}
	
	@FXML
	public void editContract(ActionEvent actionEvent) {
		StartingPane.popScene();
	}
	
	@FXML
	public void deleteContract(ActionEvent actionEvent) {
		StartingPane.popScene();
	}
	
	@FXML
	public void openNewOrder(ActionEvent actionEvent) {
		if(organizationId != null) {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put(ParameterKeys.CUSTOMER_ID, organizationId);
			MainMenu mainMenu = SessionUtil.retrieveMainMenu(session);
			if(mainMenu != null) {
				mainMenu.loadFxmlAndOpenInTab("order_form.fxml", 
						"Новый заказ", parameters);
			}
		}
	}
	
	public void fillForm(Long id) {
		Organization organization = organizationService.find(id);
		if(organization != null) {
			organizationId = organization.getId();
			directorNameField.setText(organization.getDirectorName());
			idLabel.setText(organization.getId().toString());
			innField.setText(organization.getInn());
			kppField.setText(organization.getKpp());
			fullNameField.setText(organization.getFullname());
			shortNameField.setText(organization.getShortname());
			emailField.setText(organization.getEmail());
			mobileField.setText(organization.getMobile());
			phoneField.setText(organization.getPhone());
			newOrderButton.setVisible(true);
			
			List<Contract> contracts = contractService.findByParty2(organization);
			List<ContractFX> contractsFX = EntityFX.convertListEntityToFX(contracts, ContractFX.class);
			contractsTableView.getItems().addAll(contractsFX);
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
