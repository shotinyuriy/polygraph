package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.ValidationException;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kz.aksay.polygraph.api.IContractService;
import kz.aksay.polygraph.api.IOrganizationService;
import kz.aksay.polygraph.api.IVicariousPowerService;
import kz.aksay.polygraph.desktop.controls.AddressPane;
import kz.aksay.polygraph.desktop.fxml.packageInfo;
import kz.aksay.polygraph.entity.Address;
import kz.aksay.polygraph.entity.Contract;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.VicariousPower;
import kz.aksay.polygraph.entityfx.AddressFX;
import kz.aksay.polygraph.entityfx.ContractFX;
import kz.aksay.polygraph.entityfx.EntityFX;
import kz.aksay.polygraph.entityfx.VicariousPowerFX;
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
	private IVicariousPowerService vicariousPowerService = StartingPane.getBean(IVicariousPowerService.class);
	
	@FXML private HBox controlPane;
	@FXML private VBox contentPane;
	@FXML private TextField code1cField;
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
	@FXML private TableView<VicariousPowerFX> vicariousPowerTableView;
	@FXML private HBox contractControls;
	@FXML private HBox vicPowerControls;
	@FXML private AddressPane legalAddressPane;
	@FXML private AddressPane physicalAddressPane;
	@FXML private AddressPane mailAddressPane;
	@FXML private CheckBox physicalAddressSameAsLegal;
	@FXML private CheckBox mailAddressSameAsLegal;

	private Map<String, Object> session;
	private Map<String, Object> parameters;
	private Long organizationId;
	private Organization organization;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) { 
		Bindings.equal( controlPane.maxWidthProperty(), contentPane.widthProperty() );
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
			if(organization == null) {
				organization = new Organization();
			}
		}
		physicalAddressSameAsLegal.selectedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if(newValue != null && newValue) {
					physicalAddressPane.setAddressFX( legalAddressPane.getAddressFX() );
				} else {
					physicalAddressPane.setAddressFX( new AddressFX( organization.getPhysicalAddress() ));
				}
			}			
		});
		mailAddressSameAsLegal.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if(newValue != null && newValue) {
					mailAddressPane.setAddressFX( legalAddressPane.getAddressFX() );
				} else {
					mailAddressPane.setAddressFX( new AddressFX( organization.getMailAddress() ));
				}
			}
		});
	}

	@FXML
	public void save(ActionEvent actionEvent) {
		
		if(organizationId != null) {
			organization = organizationService.find(organizationId);
		}
		if(organization == null) {
			organization = new Organization();
		}
		if (organization.getCreatedAt() == null) {
			organization.setCreatedAt(new Date());
			organization.setCreatedBy(SessionUtil.retrieveUser(session));
		}
		else {
			organization.setUpdatedAt(new Date());
			organization.setUpdatedBy(SessionUtil.retrieveUser(session));
		}
		organization.setCode1c(code1cField.getText());
		organization.setInn(innField.getText());
		organization.setFullname(fullNameField.getText());
		organization.setDirectorName(directorNameField.getText());
		organization.setKpp(kppField.getText());
		organization.setShortname(shortNameField.getText());
		organization.setEmail(emailField.getText());
		organization.setMobile(mobileField.getText());
		organization.setPhone(phoneField.getText());
		organization.setLegalAddress(legalAddressPane.getAddressFX().getEntity());
		organization.setPhysicalAddress(physicalAddressPane.getAddressFX().getEntity());
		organization.setMailAddress(mailAddressPane.getAddressFX().getEntity());
		
		try {
			organization = organizationService.save(organization);
			newOrderButton.setVisible(true);
			vicPowerControls.setManaged(true);
			vicPowerControls.setVisible(true);
			contractControls.setManaged(true);
			contractControls.setVisible(true);
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
		organization = organizationService.find(organizationId);
		Contract contract = new Contract();
		contract.setParty1(Organization.FIRMA_SERVER_PLUS);
		contract.setParty2(organization);
		
		ContractFX contractFX = new ContractFX(contract);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.ORGANIZATION_FORM, this);
		parameters.put(ParameterKeys.CONTRACT, contractFX);
		
		Parent root = (Parent) SessionUtil.loadFxmlNodeWithSession(
				packageInfo.class, StartingPane.FXML_ROOT+"short_contract_form.fxml", session, parameters);
		
		Stage stage = new Stage(); 
		stage.setScene(new Scene(root));
		stage.setTitle("Новый договор");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
		stage.showAndWait();
		
		loadContracts(organization);
	}
	
	@FXML
	public void editContract(ActionEvent actionEvent) {
		ContractFX contractFX = contractsTableView.getSelectionModel().getSelectedItem();
		
		if(contractFX != null) {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put(ParameterKeys.ORGANIZATION_FORM, this);
			parameters.put(ParameterKeys.CONTRACT, contractFX);
			
			Parent root = (Parent) SessionUtil.loadFxmlNodeWithSession(
					packageInfo.class, StartingPane.FXML_ROOT+"short_contract_form.fxml", session, parameters);
			
			Stage stage = new Stage(); 
			stage.setScene(new Scene(root));
			stage.setTitle("Новый договор");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
			stage.showAndWait();
			
			loadContracts(contractFX.getEntity().getParty2());									
		}
	}
	
	@FXML
	public void deleteContract(ActionEvent actionEvent) {
		
	}
	
	@FXML
	public void addVicariousPower(ActionEvent actionEvent) {
		organization = organizationService.find(organizationId);
		VicariousPower vicariousPower = new VicariousPower();
		vicariousPower.setOrganization(organization);
		
		VicariousPowerFX vicariousPowerFX = new VicariousPowerFX(vicariousPower);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.ORGANIZATION_FORM, this);
		parameters.put(ParameterKeys.VICARIOUS_POWER, vicariousPowerFX);
		
		Parent root = (Parent) SessionUtil.loadFxmlNodeWithSession(
				packageInfo.class, StartingPane.FXML_ROOT+"vicarious_power_form.fxml", session, parameters);
		
		Stage stage = new Stage(); 
		stage.setScene(new Scene(root));
		stage.setTitle("Новая доверенность");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
		stage.showAndWait();
		
		loadVicariousPower(organization);
	}
	
	@FXML
	public void editVicariousPower(ActionEvent actionEvent) {
		VicariousPowerFX vicariousPowerFX = vicariousPowerTableView.getSelectionModel().getSelectedItem();
		
		if(vicariousPowerFX != null) {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put(ParameterKeys.ORGANIZATION_FORM, this);
			parameters.put(ParameterKeys.VICARIOUS_POWER, vicariousPowerFX);
			
			Parent root = (Parent) SessionUtil.loadFxmlNodeWithSession(
					packageInfo.class, StartingPane.FXML_ROOT+"vicarious_power_form.fxml", session, parameters);
			
			Stage stage = new Stage(); 
			stage.setScene(new Scene(root));
			stage.setTitle("Новый договор");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
			stage.showAndWait();
			
			loadVicariousPower(vicariousPowerFX.getEntity().getOrganization());									
		}
	}
	
	@FXML
	public void deleteVicariousPower(ActionEvent actionEvent) {
		
	}
	
	@FXML
	public void openNewOrder(ActionEvent actionEvent) {
		validationLabel.setText(null);
		if(organizationId != null) {
			if(vicariousPowerTableView.getItems().size() == 0) {
				validationLabel.setText("Необходимо добавить хотя бы одну доверенность!");
			} else {
				
				VicariousPowerFX vicariousPowerFX;
				if (vicariousPowerTableView.getItems().size() == 1) {
					vicariousPowerFX = vicariousPowerTableView.getItems().get(0);
				} else {
					vicariousPowerFX = vicariousPowerTableView.getSelectionModel().getSelectedItem();
				}
				if(vicariousPowerFX == null) {
					validationLabel.setText("Необходимо выбрать доверенность!");
				} else {
					Map<String, Object> parameters = new HashMap<>();
					parameters.put(ParameterKeys.CUSTOMER_ID, organizationId);
					parameters.put(ParameterKeys.VICARIOUS_POWER, vicariousPowerFX);
					MainMenu mainMenu = SessionUtil.retrieveMainMenu(session);
					if(mainMenu != null) {
						mainMenu.loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"order_form.fxml", 
								"Новый заказ", parameters);
					}
				}
			}
		}
	}
	
	public void fillForm(Long id) {
		organization = organizationService.find(id);
		if(organization != null) {
			organizationId = id;
			directorNameField.setText(organization.getDirectorName());
			code1cField.setText(organization.getCode1c());
			innField.setText(organization.getInn());
			kppField.setText(organization.getKpp());
			fullNameField.setText(organization.getFullname());
			shortNameField.setText(organization.getShortname());
			emailField.setText(organization.getEmail());
			mobileField.setText(organization.getMobile());
			phoneField.setText(organization.getPhone());
			AddressFX legalAddressFX = new AddressFX(organization.getLegalAddress());
			legalAddressPane.setAddressFX(legalAddressFX);
			
			if(organizationService.physicalAddressSameAsLegal(organization)) {
				physicalAddressPane.setAddressFX(legalAddressFX);
				physicalAddressSameAsLegal.selectedProperty().set(true);
			} else {
				physicalAddressPane.setAddressFX(new AddressFX(organization.getPhysicalAddress()));
				physicalAddressSameAsLegal.selectedProperty().set(false);
			}
			
			if(organizationService.mailAddressSameAsLegalAddress(organization)) {
				mailAddressPane.setAddressFX(legalAddressFX);
				mailAddressSameAsLegal.selectedProperty().set(true);
			} else {
				mailAddressPane.setAddressFX(new AddressFX(organization.getMailAddress()));
				mailAddressSameAsLegal.selectedProperty().set(false);
			}
			
			
			newOrderButton.setVisible(true);
			vicPowerControls.setManaged(true);
			vicPowerControls.setVisible(true);
			contractControls.setManaged(true);
			contractControls.setVisible(true);
			
			loadContracts(organization);
			loadVicariousPower(organization);
		}
	}
	
	public void loadContracts(Organization organization) {
		List<Contract> contracts = contractService.findByParty2(organization);
		List<ContractFX> contractsFX = EntityFX.convertListEntityToFX(contracts, ContractFX.class);
		contractsTableView.getItems().clear();
		contractsTableView.getItems().addAll(contractsFX);
	}
	
	public void loadVicariousPower(Organization organization) {
		List<VicariousPower> vicariousPowers = vicariousPowerService.findByOrganization(organization);
		List<VicariousPowerFX> vicariousPowersFX = EntityFX.convertListEntityToFX(vicariousPowers, VicariousPowerFX.class);
		vicariousPowerTableView.getItems().clear();
		vicariousPowerTableView.getItems().addAll(vicariousPowersFX);
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
