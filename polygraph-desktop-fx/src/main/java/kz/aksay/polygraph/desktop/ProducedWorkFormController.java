package kz.aksay.polygraph.desktop;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javax.validation.ValidationException;

import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.WorkType;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;
import kz.aksay.polygraph.entityfx.MaterialFX;
import kz.aksay.polygraph.entityfx.ProducedWorkFX;
import kz.aksay.polygraph.entityfx.WorkTypeFX;
import kz.aksay.polygraph.fxapi.MaterialConsumptionHolderFX;
import kz.aksay.polygraph.fxapi.OrderForm;
import kz.aksay.polygraph.service.EmployeeService;
import kz.aksay.polygraph.service.MaterialConsumptionService;
import kz.aksay.polygraph.service.MaterialService;
import kz.aksay.polygraph.service.WorkTypeService;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class ProducedWorkFormController implements Initializable,
		ParametersAware, SessionAware {

	private Map<String, Object> session;
	
	@FXML private VBox producedWorkContainer;
	@FXML private ComboBox<EmployeeFX> executorCombo;
	@FXML private ComboBox<WorkTypeFX> workTypeCombo;
	@FXML private ComboBox<MaterialFX> materialCombo;
	@FXML private TextField quantityField;
	@FXML private TextField priceField;
	@FXML private Label costField;
	@FXML private AnchorPane materialConsumptionPane;
	@FXML private Label producedWorkIdLabel;
	@FXML private Label validationLabel;
	@FXML private Button finishWorkButton;

	private EmployeeService employeeService;
	private WorkTypeService workTypeService;
	private MaterialService materialService;
	private MaterialConsumptionService materialConsumptionService;
	private ProducedWorkFX producedWorkFX;
	private ProducedWork producedWork;
	private OrderForm orderForm;
	
	private MaterialConsumptionHolderFX materialConsumptionHolder 
		= new MaterialConsumptionHolderFX();
	
	private SimpleDoubleProperty price = new SimpleDoubleProperty();
	private SimpleIntegerProperty quantity = new SimpleIntegerProperty();
	private SimpleDoubleProperty cost = new SimpleDoubleProperty();
	
	private void closeByActionEvent(ActionEvent actionEvent) {
		Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void save(ActionEvent actionEvent) {
		try {
			BigDecimal cost = retrieveCost();
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
			producedWork.setPrice(BigDecimal.valueOf(price.get()));
			producedWork.setQuantity(quantity.get());
			
			if(producedWorkFX == null) {
				producedWorkFX = new ProducedWorkFX(producedWork);
				fillMaterialConsumtions(producedWorkFX);
				orderForm.addProducedWork(producedWorkFX);
				orderForm.refreshTotalCost();
			} else {
				fillMaterialConsumtions(producedWorkFX);
				orderForm.saveProducedWork(producedWorkFX);
				orderForm.refreshTotalCost();
			}
			
			closeByActionEvent(actionEvent);
		} catch (Exception e) {
			e.printStackTrace();
			validationLabel.setText(e.getMessage());
		}
	}
	
	private BigDecimal retrieveCost() {
		try {
			String costString = costField.getText();
			BigDecimal cost = new BigDecimal(costString);
			return cost;
		}
		catch(NumberFormatException nfe) {
			throw new ValidationException("Стоимость указана в некорректном формате!");
		}
	}
	
	private void fillMaterialConsumtions(ProducedWorkFX producedWorkFX) {
		producedWorkFX.setMaterialConsumption(
				materialConsumptionHolder.getMaterialConsumption());
	}

	@FXML
	public void finishWork(ActionEvent actionEvent) {
		if(producedWorkFX != null && producedWork != null) {
			
			producedWorkFX.finishWork();
			
			orderForm.saveProducedWork(producedWorkFX);
		}
		
		closeByActionEvent(actionEvent);
	}
	
	private BigDecimal retrieveQuantity() throws Exception {
		String quantityText = quantityField.getText();
		if(quantityText != null) {
			quantityText = quantityText.trim();
			try {
				return new BigDecimal(quantityText);
			}
			catch(NumberFormatException e) {
				throw new NumberFormatException("Некорректно указано количество!");
			}
		}
		return BigDecimal.ZERO; 
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
		orderForm = ParametersUtil.extractParameter(
				parameters, ParameterKeys.ORDER_FORM, OrderForm.class);
		producedWorkFX = ParametersUtil.extractParameter(
				parameters, ParameterKeys.PRODUCED_WORK, ProducedWorkFX.class);
		if(producedWorkFX != null) {
			producedWork = producedWorkFX.getProducedWork();
			
			executorCombo.setValue( producedWorkFX.getExecutorFX() );
			workTypeCombo.setValue( producedWorkFX.getWorkTypeFX() );
			
			quantity.set(producedWork.getQuantity());
			price.set(producedWork.getPrice().doubleValue());
			//materialConsumptionHolder.setMaterialConsumption(producedWork.getMaterialConsumption());

			finishWorkButton.setVisible(true);
		} else {
			finishWorkButton.setVisible(false);
		}
	}
	
	private void initializeMaterialConsumptionTableView() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.MATERIAL_CONSUMER, materialConsumptionHolder);
		Node node = SessionUtil.loadFxmlNodeWithSession(MaterialConsumptionTableViewController.class, 
				"material_consumption_tableview.fxml", session, parameters);
		materialConsumptionPane.getChildren().add(node);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		employeeService = StartingPane.getBean(EmployeeService.class);
		workTypeService = StartingPane.getBean(WorkTypeService.class);
		materialService = StartingPane.getBean(MaterialService.class);
		materialConsumptionService = StartingPane.getBean(
				MaterialConsumptionService.class);
		
		Collection<EmployeeFX> employeesFX 
			= EmployeeFX.contvertListEntityToFX(employeeService.findAll());
		executorCombo.getItems().addAll(employeesFX);
		Collection<WorkTypeFX> workTypesFX 
			= WorkTypeFX.convertListEntityToFX(workTypeService.findAll());
		workTypeCombo.getItems().addAll(workTypesFX);
		
		
		Bindings.bindBidirectional(quantityField.textProperty(), 
                quantity, 
                new NumberStringConverter());
		
		Bindings.bindBidirectional(priceField.textProperty(), 
                price, 
                new NumberStringConverter());
		
		costField.textProperty().bind(cost.asString());
		
		cost.bind(price.multiply(quantity));
		
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