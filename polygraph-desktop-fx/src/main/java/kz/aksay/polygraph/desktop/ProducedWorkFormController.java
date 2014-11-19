package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.EmployeeType;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.entityfx.ProducedWorkFX;
import kz.aksay.polygraph.entityfx.WorkTypeFX;
import kz.aksay.polygraph.fxapi.OrderForm;
import kz.aksay.polygraph.service.EmployeeService;
import kz.aksay.polygraph.service.WorkTypeService;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class ProducedWorkFormController implements Initializable,
		ParametersAware, SessionAware {

	private Map<String, Object> session;
	
	@FXML private GridPane producedWorkContainer;
	@FXML private ComboBox<EmployeeFX> executorCombo;
	@FXML private ComboBox<WorkTypeFX> workTypeCombo;
	@FXML private Label producedWorkIdLabel;
	@FXML private Label validationLabel;

	private EmployeeService employeeService;
	private WorkTypeService workTypeService;
	private ProducedWorkFX producedWorkFX;
	private ProducedWork producedWork;
	private OrderForm orderForm;
	
	@FXML
	public void save(ActionEvent actionEvent) {
		try {
			EmployeeFX executorFX 
				= executorCombo.getSelectionModel().getSelectedItem();
			WorkTypeFX workTypeFX 
				= workTypeCombo.getSelectionModel().getSelectedItem();
			Employee executor = null;
			WorkType workType = null;
			
			if(executorFX != null) {
				executor = executorFX.getEmployee();
			}
			if(workTypeFX != null) {
				workType = workTypeFX.getWorkType();
			}
			
			if(producedWork == null) {
				producedWork = new ProducedWork();
				producedWork.setCreatedAt(new Date());
				producedWork.setCreatedBy(SessionUtil.retrieveUser(session));
			}
			else {
				producedWork.setUpdatedAt(new Date());
				producedWork.setUpdatedBy(SessionUtil.retrieveUser(session));
			}
			
			producedWork.setExecutor(executor);
			producedWork.setWorkType(workType);
			
			if(producedWorkFX == null) {
				producedWorkFX = new ProducedWorkFX(producedWork);
				orderForm.addProducedWork(producedWorkFX);
			} else {
				orderForm.saveProducedWork(producedWorkFX);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			validationLabel.setText(e.getMessage());
		}
	}
	
	private Long retrieveProducedWorkId() {
		String producedWorkIdText = producedWorkIdLabel.getText();
		if(producedWorkIdText != null && !producedWorkIdText.trim().isEmpty()) {
			return Long.valueOf(producedWorkIdText.trim());
		}
		return null;
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
		orderForm = ParametersUtil.extractParameter(
				parameters, ParameterKeys.ORDER_FORM, OrderForm.class);
		producedWorkFX = ParametersUtil.extractParameter(
				parameters, ParameterKeys.PRODUCED_WORK, ProducedWorkFX.class);
		if(producedWorkFX != null) {
			producedWork = producedWorkFX.getProducedWork();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		employeeService = StartingPane.getBean(EmployeeService.class);
		workTypeService = StartingPane.getBean(WorkTypeService.class);
		
		Collection<EmployeeFX> employeesFX 
			= EmployeeFX.contvertListEntityToFX(employeeService.findAll());
		executorCombo.getItems().addAll(employeesFX);
		Collection<WorkTypeFX> workTypesFX 
			= WorkTypeFX.convertListEntityToFX(workTypeService.findAll());
		workTypeCombo.getItems().addAll(workTypesFX);
	}

}
