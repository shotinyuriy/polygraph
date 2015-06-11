package kz.aksay.polygraph.desktop;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javax.validation.ValidationException;

import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IEquipmentService;
import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.api.IMaterialService;
import kz.aksay.polygraph.api.IPaperService;
import kz.aksay.polygraph.api.IProducedWorkService;
import kz.aksay.polygraph.api.IWorkTypeService;
import kz.aksay.polygraph.desktop.fxml.packageInfo;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Equipment;
import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.entityfx.EquipmentFX;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;
import kz.aksay.polygraph.entityfx.MaterialFX;
import kz.aksay.polygraph.entityfx.ProducedWorkFX;
import kz.aksay.polygraph.entityfx.StateFX;
import kz.aksay.polygraph.entityfx.WorkTypeFX;
import kz.aksay.polygraph.exception.InternalLogicException;
import kz.aksay.polygraph.fxapi.MaterialConsumptionForm;
import kz.aksay.polygraph.fxapi.MaterialConsumptionHolderFX;
import kz.aksay.polygraph.fxapi.OrderForm;
import kz.aksay.polygraph.fxapi.ProducedWorkForm;
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
		ParametersAware, SessionAware, InitializingBean, ProducedWorkForm {

	private Map<String, Object> session;
	
	@FXML private VBox producedWorkContainer;
	@FXML private ComboBox<EmployeeFX> executorCombo;
	@FXML private ComboBox<WorkTypeFX> workTypeCombo;
	@FXML private ComboBox<MaterialFX> materialCombo;
	@FXML private ComboBox<EquipmentFX> equipmentCombo;
	@FXML private ComboBox<Format> formatCombo;
	@FXML private TextField quantityField;
	@FXML private TextField wastedField;
	@FXML private TextField priceField;
	@FXML private TextField coloredQuantityField;
	@FXML private Label costField;
	@FXML private AnchorPane materialConsumptionPane;
	@FXML private Label producedWorkIdLabel;
	@FXML private Label validationLabel;
	@FXML private Label amountLabel;
	@FXML private Label coloredAmountLabel;
	@FXML private Label errorLabel;
	@FXML private HBox equipmentPanel;
	@FXML private Label equipmentOrderNumber;
	

	private IEmployeeService employeeService;
	private IWorkTypeService workTypeService;
	private IMaterialService materialService;
	private IPaperService paperService;
	private IMaterialConsumptionService materialConsumptionService;
	private IEquipmentService equipmentService;
	private ProducedWorkFX producedWorkFX;
	private ProducedWork producedWork;
	private OrderForm orderForm;
	private MaterialConsumptionForm materialConsumptionForm;
		
	private SimpleDoubleProperty price = new SimpleDoubleProperty();
	private SimpleIntegerProperty quantity = new SimpleIntegerProperty(1);
	private SimpleIntegerProperty wasted = new SimpleIntegerProperty(0);
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
		Format format = formatCombo.getSelectionModel().getSelectedItem();
		
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
		
		if(isPrinting && format == null) {
			throw new InternalLogicException("Для печати необходимо указать оборудование!");
		}
		
		if(quantity.get() < wasted.get()) {
			throw new InternalLogicException("Количество брака не может быть больше общего количества!");
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
		
		if(isPrinting) {
			producedWork.setFormat(format);
			producedWork.setEquipment(equipment);
		}
		
		producedWork.setExecutor(executor);
		producedWork.setWorkType(workType);
		producedWork.setPrice(BigDecimal.valueOf(price.get()));
		producedWork.setQuantity(quantity.get());
		producedWork.setWasted(wasted.get());
		producedWork.setMaterialConsumption(new HashSet<MaterialConsumption>());
		
		producedWork.getMaterialConsumption().addAll(
				materialConsumptionForm.getMaterialConsumptionList());
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		employeeService = StartingPane.getBean(IEmployeeService.class);
		workTypeService = StartingPane.getBean(IWorkTypeService.class);
		materialService = StartingPane.getBean(IMaterialService.class);
		materialConsumptionService = StartingPane.getBean(
				IMaterialConsumptionService.class);
		equipmentService = StartingPane.getBean(IEquipmentService.class);
		paperService = StartingPane.getBean(IPaperService.class);
		
		Collection<EmployeeFX> employeesFX 
			= EmployeeFX.contvertListEntityToFX(employeeService.findAll());
		executorCombo.getItems().addAll(employeesFX);
		Collection<WorkTypeFX> workTypesFX 
			= WorkTypeFX.convertListEntityToFX(workTypeService.findAll());
		workTypeCombo.getItems().addAll(workTypesFX);
		equipmentCombo.getItems().addAll(EquipmentFX.convertListEntityToFX(equipmentService.findAll()));
		formatCombo.getItems().addAll(paperService.findAvailableFormats());
		
		Bindings.bindBidirectional(quantityField.textProperty(), 
                quantity, 
                new NumberStringConverter());
		
		Bindings.bindBidirectional(priceField.textProperty(), 
                price, 
                new NumberStringConverter());
		
		Bindings.bindBidirectional(wastedField.textProperty(),
				wasted,
				new NumberStringConverter());
		
		costField.textProperty().bind(cost.asString());
		cost.bind(price.multiply(quantity).subtract(price.multiply(wasted)));
		
		workTypeCombo.getSelectionModel().selectedItemProperty().addListener( 
				new ChangeListener<WorkTypeFX>() {

			@Override
			public void changed(
					ObservableValue<? extends WorkTypeFX> observable,
					WorkTypeFX oldValue, WorkTypeFX newValue) {
				
				if(workTypeService.isEquipmentRequired(newValue.getEntity())) {
					
					isPrinting = true;					
					amountLabel.setText("Количество прогонов");
				} else {
					
					isPrinting = false;
					amountLabel.setText("Количество");
				}
				
				equipmentPanel.setVisible(isPrinting);
				equipmentPanel.setManaged(isPrinting);
				
				if(materialConsumptionForm != null) {
					Format format = formatCombo.getSelectionModel().getSelectedItem();
					newValue.getEntity().setFormat(format);
					materialConsumptionForm.loadMaterialsByWorkType(
							newValue.getEntity());
				}
			}
			
		});
		
		formatCombo.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<Format>() {

			@Override
			public void changed(ObservableValue<? extends Format> observable,
					Format oldValue, Format newValue) {
				
				WorkTypeFX workTypeFX = workTypeCombo.getSelectionModel()
						.getSelectedItem();
				
				if(materialConsumptionForm != null) {
					Format format = formatCombo.getSelectionModel()
							.getSelectedItem();
					workTypeFX.getEntity().setFormat(format);
					materialConsumptionForm.loadMaterialsByWorkType(
							workTypeFX.getEntity());
				}
				
			}
			
		});
		
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
				equipmentOrderNumber.setText(producedWorkFX.getEquipmentOrderNumberString());
				quantity.set(producedWork.getQuantity());
				price.set(producedWork.getPrice().doubleValue());
				formatCombo.getSelectionModel().select(producedWork.getFormat());
				
			} else {
				
				if(orderForm != null ) {
					executorCombo.getSelectionModel().select(orderForm.getCurrentExecutor());
				}
			}
		}
	}
	
	private void initializeMaterialConsumptionTableView() {
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.MATERIAL_CONSUMER, producedWorkFX);
		parameters.put(ParameterKeys.PRODUCED_WORK_FORM, this);
		Node node = SessionUtil.loadFxmlNodeWithSession(packageInfo.class, 
				StartingPane.FXML_ROOT+"material_consumption_tableview.fxml", 
				session, parameters);
		materialConsumptionPane.getChildren().add(node);
		WorkTypeFX workTypeFX = workTypeCombo.getSelectionModel().getSelectedItem();
		if(materialConsumptionForm != null && workTypeFX != null) {
			materialConsumptionForm.loadMaterialsByWorkType(workTypeFX.getEntity());
		}
	}
	
	@Override
	public void afterPropertiesSet() {
		initializeByParameters();
		initializeMaterialConsumptionTableView();
	}

	@Override
	public void setMaterialConsumptionForm(
			MaterialConsumptionForm materialConsumptionForm) {
		
		this.materialConsumptionForm = materialConsumptionForm;
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