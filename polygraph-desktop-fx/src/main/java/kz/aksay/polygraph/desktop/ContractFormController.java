package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.aksay.polygraph.api.IContractService;
import kz.aksay.polygraph.entity.Contract;
import kz.aksay.polygraph.entityfx.ContractFX;
import kz.aksay.polygraph.fxapi.OrganizationForm;
import kz.aksay.polygraph.util.FormatUtil;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class ContractFormController implements Initializable,
		ParametersAware, SessionAware {

	private IContractService contractService = StartingPane.getBean(IContractService.class);
	
	private Map<String, Object> session;
	private OrganizationForm organizationForm;
	private ContractFX contractFX;
	
	@FXML private Label party1NameLabel;
	@FXML private Label party2NameLabel;
	@FXML private Label numberField;
	@FXML private DatePicker beginDateField;
	@FXML private DatePicker endDateField;
	@FXML private Label errorLabel;
		
	@FXML
	public void save(ActionEvent actionEvent) {
		saveOrAdd(actionEvent);
		closeByActionEvent(actionEvent);
	}
	
	public void saveOrAdd(ActionEvent actionEvent) {
		try {
			errorLabel.setText(null);
			
			Contract contract = contractFX.getEntity();
			contract.setBeginDate(FormatUtil.convertToDate(beginDateField.getValue()));
			contract.setEndDate(FormatUtil.convertToDate(endDateField.getValue()));
			
			if(contract.getId() == null) {
				contract.setCreatedAt(new Date());
				contract.setCreatedBy(SessionUtil.retrieveUser(session));
				contract.setNumber(contractService.getNewNumber());
			} else {
				contract.setUpdatedAt(new Date());
				contract.setUpdatedBy(SessionUtil.retrieveUser(session));
				contract.setNumber(numberField.getText());
			}
			
			contractService.save(contract);
		} catch(Exception e) {
			errorLabel.setText(e.getMessage());
		}
	}
	
	private void closeByActionEvent(ActionEvent actionEvent) {
		Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		stage.close();
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		//initializeMaterialConsumptionTableView();
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		initializeByParameters(parameters);
	}

	private void initializeByParameters(Map<String, Object> parameters) {
		organizationForm = ParametersUtil.extractParameter(
				parameters, ParameterKeys.ORGANIZATION_FORM, OrganizationForm.class);
		contractFX = ParametersUtil.extractParameter(
				parameters, ParameterKeys.CONTRACT, ContractFX.class);
		if(contractFX != null) {
			party1NameLabel.setText(contractFX.getPartyName1());
			party2NameLabel.setText(contractFX.getPartyName2());
			if(contractFX.getNumber() != null) {
				numberField.setText(contractFX.getNumber());
			}
			if(contractFX.getEntity().getBeginDate() != null) {
				beginDateField.valueProperty().set(FormatUtil.convertToLocalDate(contractFX.getEntity().getBeginDate()));
			}
			if(contractFX.getEntity().getEndDate() != null) {
				endDateField.valueProperty().set(FormatUtil.convertToLocalDate(contractFX.getEntity().getEndDate()));
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}