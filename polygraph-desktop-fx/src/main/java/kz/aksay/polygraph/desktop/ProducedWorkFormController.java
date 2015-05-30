package kz.aksay.polygraph.desktop;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javax.validation.ValidationException;

import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IEquipmentService;
import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.api.IWorkTypeService;
import kz.aksay.polygraph.desktop.fxml.packageInfo;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.entityfx.EquipmentFX;
import kz.aksay.polygraph.entityfx.MaterialFX;
import kz.aksay.polygraph.entityfx.ProducedWorkFX;
import kz.aksay.polygraph.entityfx.StateFX;
import kz.aksay.polygraph.entityfx.WorkTypeFX;
import kz.aksay.polygraph.exception.InternalLogicException;
import kz.aksay.polygraph.fxapi.MaterialConsumptionHolderFX;
import kz.aksay.polygraph.fxapi.OrderForm;
import kz.aksay.polygraph.service.EmployeeService;
import kz.aksay.polygraph.service.MaterialConsumptionService;
import kz.aksay.polygraph.service.MaterialService;
import kz.aksay.polygraph.service.WorkTypeService;
import kz.aksay.polygraph.util.InitializingBean;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class ProducedWorkFormController implements Initializable,
		ParametersAware, SessionAware, InitializingBean {

	private Map<String, Object> session;
	
	@FXML private VBox producedWorkContainer;
	@FXML private ComboBox<EmployeeFX> executorCombo;
	@FXML private ComboBox<WorkTypeFX> workTypeCombo;
	@FXML private ComboBox<MaterialFX> materialCombo;
	@FXML private ComboBox<EquipmentFX> equipmentCombo;
	@FXML private TextField quantityField;
	@FXML private TextField priceField;
	@FXML private TextField coloredQuantityField;
	@FXML private Label costField;
	@FXML private AnchorPane materialConsumptionPane;
	@FXML private Label producedWorkIdLabel;
	@FXML private Label validationLabel;
	@FXML private Label amountLabel;
	@FXML private Label coloredAmountLabel;
	@FXML private Label equipmentLabel;
	@FXML private Label errorLabel;
	

	private IEmployeeService employeeService;
	private IWorkTypeService workTypeService;
	private IMaterialService materialService;
	private IMaterialConsumptionService materialConsumptionService;
	private IEquipmentService equipmentService;
	private ProducedWorkFX producedWorkFX;
	private ProducedWork producedWork;
	private OrderForm orderForm;
		
	private SimpleDoubleProperty price = new SimpleDoubleProperty();
	private SimpleIntegerProperty quantity = new SimpleIntegerProperty(1);
	private SimpleDoubleProperty cost = new SimpleDoubleProperty();
	private boolean isPrinting = false;

	private Map<String, Object> parameters;
	
	private void closeByActionEvent(ActionEvent actionEvent) {
		Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void saveAndOpenNew(ActionEvent actionEvent) {
		saveOrAdd(actionEvent);
		closeByActionEvent(actionEvent);
		orderForm.openNewProducedWorkForm();
	}
	
	@FXML
	public void save(ActionEvent actionEvent) {
		saveOrAdd(actionEvent);
		closeByActionEvent(actionEvent);
	}
	
	@FXML
	public void saveOrAdd(ActionEvent actionEvent) {
		try {
			errorLabel.setText(null);
			fillProducedWork();
			if(producedWorkFX == null) {
				producedWorkFX = new ProducedWorkFX(producedWork);
				orderForm.addProducedWork(producedWorkFX);
				orderForm.refreshTotalCost();
			} else {
				orderForm.saveProducedWork(producedWorkFX);
				orderForm.refreshTotalCost();
			}
		} catch(Exception e) {
			errorLabel.setText(e.getMessage());
		}
	}
	
	private void fillProducedWork() throws Exception {
		EmployeeFX executorFX 
			= executorCombo.getSelectionModel().getSelectedItem();
		WorkTypeFX workTypeFX 
			= workTypeCombo.getSelectionModel().getSelectedItem();
		EquipmentFX equipmentFX 
			= equipmentCombo.getSelectionModel().getSelectedItem();
		Employee executor = null;
		WorkType workType = null;
		Equipment equipment = null;
		
		if(executorFX != null) {
			executor = executorFX.getEmployee();
		}
		if(workTypeFX != null) {
			workType = workTypeFX.getWorkType();
		}
		if(equipmentCombo.isVisible() && equipmentFX != null) {
			equipment = equipmentFX.getEquipment();
		}
		
		if(isPrinting && equipment == null) {
			throw new InternalLogicException("Для печати необходимо указать оборудование!");
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
		producedWork.setEquipment(equipment);
		producedWork.setPrice(BigDecimal.valueOf(price.get()));
		producedWork.setQuantity(quantity.get());
		
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	private void initializeByParameters() {
		if(parameters != null) {
			orderForm = ParametersUtil.extractParameter(
					parameters, ParameterKeys.ORDER_FORM, OrderForm.class);
			producedWorkFX = ParametersUtil.extractParameter(
					parameters, ParameterKeys.PRODUCED_WORK, ProducedWorkFX.class);
			if(producedWorkFX != null) {
				producedWork = producedWorkFX.getEntity();
				
				executorCombo.setValue( producedWorkFX.getExecutorFX() );
				workTypeCombo.setValue( producedWorkFX.getWorkTypeFX() );
				equipmentCombo.setValue( producedWorkFX.getEquipmentFX() );
				
				quantity.set(producedWork.getQuantity());
				price.set(producedWork.getPrice().doubleValue());
				
			} else {
				
				if(orderForm != null ) {
					executorCombo.getSelectionModel().select(orderForm.getCurrentExecutor());
				}
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		employeeService = StartingPane.getBean(IEmployeeService.class);
		workTypeService = StartingPane.getBean(IWorkTypeService.class);
		materialService = StartingPane.getBean(IMaterialService.class);
		materialConsumptionService = StartingPane.getBean(
				IMaterialConsumptionService.class);
		equipmentService = StartingPane.getBean(IEquipmentService.class);
		
		Collection<EmployeeFX> employeesFX 
			= EmployeeFX.contvertListEntityToFX(employeeService.findAll());
		executorCombo.getItems().addAll(employeesFX);
		Collection<WorkTypeFX> workTypesFX 
			= WorkTypeFX.convertListEntityToFX(workTypeService.findAll());
		workTypeCombo.getItems().addAll(workTypesFX);
		equipmentCombo.getItems().addAll(EquipmentFX.convertListEntityToFX(equipmentService.findAll()));
		
		Bindings.bindBidirectional(quantityField.textProperty(), 
                quantity, 
                new NumberStringConverter());
		
		Bindings.bindBidirectional(priceField.textProperty(), 
                price, 
                new NumberStringConverter());
		
		costField.textProperty().bind(cost.asString());
		cost.bind(price.multiply(quantity));
		
		workTypeCombo.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<WorkTypeFX>() {

			@Override
			public void changed(
					ObservableValue<? extends WorkTypeFX> observable,
					WorkTypeFX oldValue, WorkTypeFX newValue) {
				
				if(newValue != null && 
						( 
								WorkType.PRINTING_BLACK_AND_WHITE.equals(newValue.getEntity()) || 
								WorkType.PRINTING_COLORED.equals(newValue.getEntity())
						)) {
					
					isPrinting = true;
					equipmentLabel.setVisible(isPrinting);
					equipmentCombo.setVisible(isPrinting);
					amountLabel.setText("Количество прогонов");
				} else {
					
					isPrinting = false;
					equipmentLabel.setVisible(isPrinting);
					equipmentCombo.setVisible(isPrinting);
					amountLabel.setText("Количество");
				}
			}
			
		});
		
	}
	
	private void initializeMaterialConsumptionTableView() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.MATERIAL_CONSUMER, producedWorkFX);
		Node node = SessionUtil.loadFxmlNodeWithSession(packageInfo.class, 
				StartingPane.FXML_ROOT+"material_consumption_tableview.fxml", session, parameters);
		materialConsumptionPane.getChildren().add(node);
		
	}
	
	@Override
	public void afterPropertiesSet() {
		initializeByParameters();
		initializeMaterialConsumptionTableView();
	}
	
	public IntegerProperty getQuantityProperty() {
		return quantity;
	}
	
	public DoubleProperty getPriceProperty() {
		return price;
	}
	
	public DoubleProperty getCostProperty() {
		return cost;
	}
}