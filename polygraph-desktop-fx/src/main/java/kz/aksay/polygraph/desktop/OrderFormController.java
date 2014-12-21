package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kz.aksay.polygraph.entity.Customer;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.entityfx.OrderFX;
import kz.aksay.polygraph.entityfx.ProducedWorkFX;
import kz.aksay.polygraph.fxapi.OrderForm;
import kz.aksay.polygraph.service.CustomerService;
import kz.aksay.polygraph.service.EmployeeService;
import kz.aksay.polygraph.service.OrderService;
import kz.aksay.polygraph.util.InitializingBean;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class OrderFormController implements 
	Initializable, SessionAware, ParametersAware, OrderForm, InitializingBean {
	
	private OrderService orderService;
	private EmployeeService employeeService;
	private CustomerService customerService;
	
	private Map<String, Object> parameters;
	private Map<String, Object> session;
	
	private boolean isNewOrder;
	//private Order order;
	private OrderFX orderFX;
	private Customer customer;
	
	@FXML private GridPane orderContainer;
	@FXML private Label orderIdLabel;
	@FXML private Label customerIdLabel;
	@FXML private Label validationLabel;
	@FXML private Label totalCostLabel;
	
	@FXML private Label customerField;
	@FXML private ComboBox<EmployeeFX> currentExecutorCombo;
	@FXML private TextArea descriptionField;
	
	@FXML private TableView<ProducedWorkFX> producedWorksTableView;
	@FXML private AnchorPane materialConsumptionPane;
	@FXML private Button addProducedWorkButton;

	@FXML
	public void save(ActionEvent actionEvent) {
		try {
			
			if(isNewOrder) {
				orderFX.getCreatedAtProperty().set(new Date());
				orderFX.getCreatedByProperty().set(SessionUtil.retrieveUser(session));
			}
			else {
				orderFX.getUpdatedAtProperty().set(new Date());
				orderFX.getUpdatedByProperty().set(SessionUtil.retrieveUser(session));
			}
						
			orderService.save(orderFX.getOrder());
			
			validationLabel.setText("Сохранение успешно");
			
		} catch (Exception e) {
			e.printStackTrace();
			validationLabel.setText(e.getMessage());
		}
	}
	
	@FXML
	public void addProducedWork(ActionEvent actionEvent) {
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.ORDER_FORM, this);
		
		Parent root = (Parent) SessionUtil.loadFxmlNodeWithSession(
				OrderFormController.class, "produced_work_form.fxml", session, parameters);
		
		Stage stage = new Stage(); 
		stage.setScene(new Scene(root));
		stage.setTitle("Новая работа по заказу");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
		stage.show();
	}
	
	@FXML
	public void editProducedWork(ActionEvent actionEvent) {
		ProducedWorkFX producedWorkFX 
			= producedWorksTableView.getSelectionModel().getSelectedItem();
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.ORDER_FORM, this);
		parameters.put(ParameterKeys.PRODUCED_WORK, producedWorkFX);
		
		Parent root = (Parent) SessionUtil.loadFxmlNodeWithSession(
				OrderFormController.class, "produced_work_form.fxml", session, parameters);
		
		Stage stage = new Stage(); 
		stage.setScene(new Scene(root));
		stage.setTitle("Новая работа по заказу");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
		stage.show();
	}
	
	@FXML
	public void deleteProducedWork(ActionEvent actionEvent) {
		
		List<ProducedWorkFX> producedWorksFXToRemove 
			= producedWorksTableView.getSelectionModel().getSelectedItems();
		
		List<ProducedWork> producedWorksToRemove = new LinkedList<ProducedWork>();
		for(ProducedWorkFX producedWorkFX : producedWorksFXToRemove) {
			producedWorksToRemove.add(producedWorkFX.getProducedWork());
		}
		producedWorksTableView.getItems().removeAll(producedWorksFXToRemove);
		
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	private void initializeByParameters() {
		if(parameters != null) {
			Long orderId = ParametersUtil.extractParameter(
					parameters, ParameterKeys.ORDER_ID, Long.class);
			if(orderId != null) {
				isNewOrder = false;
				Order order = orderService.find(orderId);
				orderFX = new OrderFX(order);
				//orderIdLabel.setText(orderId+"");
				//customerIdLabel.setText(orderFX.getCustomerId()+"");
				customerField.setText(orderFX.getCustomerFullName());
				descriptionField.textProperty().bindBidirectional(orderFX.getDescriptionProperty());
				currentExecutorCombo.getSelectionModel().select(orderFX.getCurrentExecutorFX());
				orderFX.getCurrentExecutorProperty().bind(currentExecutorCombo.getSelectionModel().selectedItemProperty());
				
				producedWorksTableView.getItems().addAll(orderFX.getProducedWorkProperty());
				orderFX.setProducedWorkProperty(producedWorksTableView.getItems());
				
				refreshTotalCost();
			} else {
				isNewOrder = true;
				orderFX = new OrderFX(null);
				Long customerId = ParametersUtil.extractParameter(
						parameters, ParameterKeys.CUSTOMER_ID, Long.class);
				customer = customerService.find(customerId);
				if(customer != null) {
					orderFX.getCustomerProperty().set(customer);
					customerIdLabel.setText(customer.getId()+"");
					customerField.setText(customer.getFullName());
				}
			}
			
			
		}
	}
	
	@Override
	public void refreshTotalCost() {
		totalCostLabel.setText(orderFX.getOrder().getTotalCost().setScale(2).toString());
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		initializeMaterialConsumptionTableView();
	}
	
	private void initializeMaterialConsumptionTableView() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.MATERIAL_CONSUMER, orderFX);
		Node node = SessionUtil.loadFxmlNodeWithSession(MaterialConsumptionTableViewController.class, 
				"material_consumption_tableview.fxml", session, parameters);
		materialConsumptionPane.getChildren().add(node);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderService = StartingPane.getBean(OrderService.class);
		employeeService = StartingPane.getBean(EmployeeService.class);
		customerService = StartingPane.getBean(CustomerService.class);
		
		List<Employee> employees = employeeService.findAll();
		Collection<EmployeeFX> employeesFX = EmployeeFX.contvertListEntityToFX(employees);
		
		currentExecutorCombo.getItems().setAll(employeesFX);
	}
	
	private void refreshProducedWorkTableView() {
		List<ProducedWorkFX> producedWorksFX = new LinkedList<>(); 
		producedWorksFX.addAll(producedWorksTableView.getItems());
		producedWorksTableView.getItems().removeAll(producedWorksFX);
		producedWorksTableView.getItems().addAll(producedWorksFX);
	}
	
	@Override
	public void addProducedWork(ProducedWorkFX producedWorkFX) {
		System.out.println("ADD NEW PRODUCED WORK "+producedWorkFX);
		producedWorkFX.setDirty(true);
		producedWorksTableView.getItems().add(producedWorkFX);
		refreshProducedWorkTableView();
	}

	@Override
	public void saveProducedWork(ProducedWorkFX producedWorkFX) {
		producedWorkFX.setDirty(true);
		refreshProducedWorkTableView();
	}

	@Override
	public EmployeeFX getCurrentExecutor() {
		return currentExecutorCombo.getSelectionModel().getSelectedItem();
	}

	@Override
	public void openNewProducedWorkForm() {
		addProducedWorkButton.fire();
	}

	@Override
	public void afterPropertiesSet() {
		initializeByParameters();
		initializeMaterialConsumptionTableView();
	}

	
}
