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
import kz.aksay.polygraph.api.IVicariousPowerService;
import kz.aksay.polygraph.entity.Contract;
import kz.aksay.polygraph.entity.VicariousPower;
import kz.aksay.polygraph.entityfx.ContractFX;
import kz.aksay.polygraph.entityfx.VicariousPowerFX;
import kz.aksay.polygraph.fxapi.OrganizationForm;
import kz.aksay.polygraph.util.FormatUtil;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class VicariousPowerFormController implements Initializable,
		ParametersAware, SessionAware {

	private IVicariousPowerService vicariousPowerService = StartingPane.getBean(IVicariousPowerService.class);
	
	private Map<String, Object> session;
	private VicariousPowerFX vicariousPowerFX;
	
	@FXML private Label organizationNameField;
	@FXML private TextField numberField;
	@FXML private TextField personNameField;
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
			
			VicariousPower contract = vicariousPowerFX.getEntity();
			
			contract.setNumber(numberField.getText());
			contract.setPersonName(personNameField.getText());
			contract.setBeginDate(FormatUtil.convertToDate(beginDateField.getValue()));
			contract.setEndDate(FormatUtil.convertToDate(endDateField.getValue()));
			
			if(contract.getId() == null) {
				contract.setCreatedAt(new Date());
				contract.setCreatedBy(SessionUtil.retrieveUser(session));
			} else {
				contract.setUpdatedAt(new Date());
				contract.setUpdatedBy(SessionUtil.retrieveUser(session));
			}
			
			vicariousPowerService.save(contract);
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
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		initializeByParameters(parameters);
	}

	private void initializeByParameters(Map<String, Object> parameters) {
		
		vicariousPowerFX = ParametersUtil.extractParameter(
				parameters, ParameterKeys.VICARIOUS_POWER, VicariousPowerFX.class);
		
		if(vicariousPowerFX != null) {
			organizationNameField.setText(vicariousPowerFX.getOrganizationName());
			personNameField.setText(vicariousPowerFX.getPersonName());
			numberField.setText(vicariousPowerFX.getNumber());
			if(vicariousPowerFX.getEntity().getBeginDate() != null) {
				beginDateField.valueProperty().set(FormatUtil.convertToLocalDate(vicariousPowerFX.getEntity().getBeginDate()));
			}
			if(vicariousPowerFX.getEntity().getEndDate() != null) {
				endDateField.valueProperty().set(FormatUtil.convertToLocalDate(vicariousPowerFX.getEntity().getEndDate()));
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
}