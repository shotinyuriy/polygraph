package kz.aksay.polygraph.desktop;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import kz.aksay.polygraph.api.IComplexityService;
import kz.aksay.polygraph.api.ICustomerService;
import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IOrderService;
import kz.aksay.polygraph.api.IProducedWorkService;
import kz.aksay.polygraph.desktop.fxml.packageInfo;
import kz.aksay.polygraph.desktop.reports.PrintFacade;
import kz.aksay.polygraph.entity.Complexity;
import kz.aksay.polygraph.entity.DefaultData;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.Subject;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;
import kz.aksay.polygraph.entityfx.OrderFX;
import kz.aksay.polygraph.entityfx.ProducedWorkFX;
import kz.aksay.polygraph.entityfx.StateFX;
import kz.aksay.polygraph.entityfx.VicariousPowerFX;
import kz.aksay.polygraph.fxapi.OrderForm;
import kz.aksay.polygraph.util.FormatUtil;
import kz.aksay.polygraph.util.InitializingBean;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public class OrderFormController implements 
	Initializable, SessionAware, ParametersAware, OrderForm, InitializingBean {
	
	private IOrderService orderService = StartingPane.getBean(IOrderService.class);
	private IEmployeeService employeeService = StartingPane.getBean(IEmployeeService.class);
	private ICustomerService customerService = StartingPane.getBean(ICustomerService.class);
	private IProducedWorkService producedWorkService = StartingPane.getBean(IProducedWorkService.class);
	private IComplexityService complexityService = StartingPane.getBean(IComplexityService.class);
	
	private Map<String, Object> parameters;
	private Map<String, Object> session;
	
	private boolean isNewOrder;
	private OrderFX orderFX;
	private Subject customer;
	
	@FXML private GridPane orderContainer;
	@FXML private Label orderIdLabel;
	@FXML private Label customerIdLabel;
	@FXML private Label validationLabel;
	@FXML private Label totalCostLabel;
	@FXML private Label errorLabel;
	
	@FXML private Label vicariousPowerNumberField;
	@FXML private Label customerField;
	@FXML private ComboBox<EmployeeFX> currentExecutorCombo;
	@FXML private TextArea descriptionField;
	@FXML private TextField circulationField;
	@FXML private ComboBox<StateFX> currentStatusCombo;
	@FXML private DatePicker dateEndPlan;
	@FXML private DatePicker dateEndReal;
	@FXML private ComboBox<Complexity> complexityCombo;
	@FXML private Button showEmployeeWorkLoad;
	
	@FXML private TableView<ProducedWorkFX> producedWorksTableView;
	@FXML private AnchorPane materialConsumptionPane;
	@FXML private Button addProducedWorkButton;
	@FXML private AnchorPane employeeWorkloadPane;
	
	private TableView<MaterialConsumptionFX> materialConsumptionsTableView;
	private List<ProducedWork> producedWorksToRemove = new LinkedList<ProducedWork>();

	@FXML
	public void save(ActionEvent actionEvent) {
		try {
			validationLabel.setText(null);
			if(isNewOrder) {
				orderFX.getCreatedAtProperty().set(new Date());
				orderFX.getCreatedByProperty().set(SessionUtil.retrieveUser(session));
				
			}
			else {
				orderFX.getUpdatedAtProperty().set(new Date());
				orderFX.getUpdatedByProperty().set(SessionUtil.retrieveUser(session));
			}
			
			Order order = orderFX.getOrder();
			
			if(dateEndPlan.getValue() != null) {
				order.setDateEndPlan(
						FormatUtil.convertToDate(dateEndPlan.getValue())
					);
			} else {
				order.setDateEndPlan(null);
			}
			
			if(dateEndReal.getValue() != null) {
				order.setDateEndReal(
						FormatUtil.convertToDate(dateEndReal.getValue())
					);
			} else {
				order.setDateEndReal(new Date());
			}
			
			
			order.setProducedWorks(new HashSet<ProducedWork>());
			for(ProducedWorkFX prodWorkFX : producedWorksTableView.getItems()) {
				order.getProducedWorks().add(prodWorkFX.getEntity());
			}
			
			orderService.save(order);
			
			if(!producedWorksToRemove.isEmpty() ) {
				for(ProducedWork producedWork : producedWorksToRemove) {
					producedWorkService.delete(producedWork);
				}
			}
			
			validationLabel.setText("Сохранение успешно");
			showWorkload();
			
		} catch (Exception e) {
			e.printStackTrace();
			validationLabel.setText(e.getMessage());
		}
	}
	
	@FXML
	public void print(ActionEvent actionEvent) {
		
		try {
			JasperPrint jasperPrint = PrintFacade.generateOrderDetails(orderFX);
			SwingNode jrViewerNode = new SwingNode();
			PrintFacade.embedJRViewerIntoSwingNode(jrViewerNode, jasperPrint);
			StackPane stackPane = new StackPane();
			stackPane.getChildren().add(jrViewerNode);
			Stage stage = new Stage(); 
			stage.setScene(new Scene(stackPane));
			stage.setTitle("Предварительный просмотр");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
			stage.show();
		} catch (JRException e) {
			errorLabel.setText(e.getLocalizedMessage());
		}
	}
	
	@FXML
	public void exportToDocx(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Укажите файл для сохранения");
		File file = fileChooser.showSaveDialog(StartingPane.getPrimaryStage());
		if(file != null) {
			try {
				PrintFacade.generateOrderDetails(orderFX);
			} catch (JRException e) {
				errorLabel.setText(e.getLocalizedMessage());
			}
		}
	}
	
	@FXML
	public void addProducedWork(ActionEvent actionEvent) {
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.ORDER_FORM, this);
		
		Parent root = (Parent) SessionUtil.loadFxmlNodeWithSession(
				packageInfo.class, StartingPane.FXML_ROOT+"produced_work_form.fxml", session, parameters);
		
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
				packageInfo.class, StartingPane.FXML_ROOT+"produced_work_form.fxml", session, parameters);
		
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
		
		for(ProducedWorkFX producedWorkFX : producedWorksFXToRemove) {
			producedWorksToRemove.add(producedWorkFX.getEntity());
		}
		
		producedWorksTableView.getItems().removeAll(producedWorksFXToRemove);
	}
	
	private void showWorkload() {
		AnchorPane employeeWorkloadView = (AnchorPane)SessionUtil.loadFxmlNodeWithSession(packageInfo.class, 
				StartingPane.FXML_ROOT+"employee_workload_view.fxml", session, parameters);
		employeeWorkloadPane.getChildren().clear();
		employeeWorkloadPane.getChildren().add(employeeWorkloadView);
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	private void initializeByParameters() {
		if(parameters != null) {
			Long orderId = ParametersUtil.extractParameter(
					parameters, ParameterKeys.ORDER_ID, Long.class);
			VicariousPowerFX vicariousPowerFX = ParametersUtil.extractParameter(
					parameters, ParameterKeys.VICARIOUS_POWER, VicariousPowerFX.class);
			
			if(orderId != null) {
				isNewOrder = false;
				Order order = orderService.find(orderId);
				orderFX = new OrderFX(order);
				
				customerField.setText(orderFX.getCustomerFullName());
				
				dateEndPlan.valueProperty().set(
						FormatUtil.convertToLocalDate(order.getDateEndPlan()));
				dateEndReal.valueProperty().set(
						FormatUtil.convertToLocalDate(order.getDateEndReal()));
				currentExecutorCombo.getSelectionModel().select(orderFX.getCurrentExecutorFX());
				complexityCombo.getSelectionModel().select(order.getComplexity());
				
				if(orderFX.getStateProperty().get() != null) {
				
					currentStatusCombo.getSelectionModel().select(
							new StateFX(orderFX.getStateProperty().get(), 
									orderFX.getStateProperty().get().getName()));
				}
				
				if(orderFX.getVicariousPowerProperty().get() != null) {
					vicariousPowerNumberField.setText(orderFX.getVicariousPowerProperty().get().getDescription());
				}
				
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
				if(vicariousPowerFX != null) {
					vicariousPowerNumberField.setText(vicariousPowerFX.getDescription());
					orderFX.getVicariousPowerProperty().set(vicariousPowerFX);
				}
				
				currentStatusCombo.getSelectionModel().select(new StateFX(Order.State.NEW, Order.State.NEW.getName()));
			}
			
			descriptionField.textProperty().bindBidirectional(orderFX.getDescriptionProperty());
			orderFX.getCurrentExecutorProperty().bind(currentExecutorCombo.getSelectionModel().selectedItemProperty());
			Bindings.bindBidirectional(circulationField.textProperty(),
					orderFX.getCirculationProperty(),
					new NumberStringConverter());
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
		showWorkload();
	}
	
	private void initializeMaterialConsumptionTableView() {
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ParameterKeys.MATERIAL_CONSUMER, orderFX);
		parameters.put(ParameterKeys.ORDER_FORM, this); 
		Node node = SessionUtil.loadFxmlNodeWithSession(packageInfo.class, 
				StartingPane.FXML_ROOT+"material_consumption_tableview.fxml", session, parameters);
		materialConsumptionPane.getChildren().add(node);
		currentStatusCombo.getItems().clear();
		currentStatusCombo.getItems().addAll(StateFX.VALUES);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		List<Employee> employees = employeeService.findAllByUserRole(User.Role.DESIGNER);
		Collection<EmployeeFX> employeesFX = EmployeeFX.contvertListEntityToFX(employees);
		
		currentExecutorCombo.getItems().setAll(employeesFX);
		complexityCombo.getItems().setAll(DefaultData.COMPLEXITY_DEFAULTS);
		
		
		producedWorksTableView.setRowFactory(new Callback<TableView<ProducedWorkFX>, TableRow<ProducedWorkFX>>() {
			
			@Override
			public TableRow<ProducedWorkFX> call(TableView<ProducedWorkFX> param) {
				
				TableRow<ProducedWorkFX> tableRow = new TableRow<>();
				tableRow.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						int clickCount = event.getClickCount();
						if(clickCount == 2) {
							editProducedWork(new ActionEvent(event.getSource(), 
								event.getTarget()));
						}
					}
					
				});
				
				return tableRow;
			}
		});
		
		currentStatusCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StateFX>() {
			@Override
			public void changed(
					ObservableValue<? extends StateFX> observable,
					StateFX oldValue, StateFX newValue) {
				orderFX.getStateProperty().set(newValue.getState());
				
				dateEndReal.setDisable(true);
				if(newValue.getState() != null) {
					if(newValue.getState().equals(Order.State.FINISHED)) {
						dateEndReal.setDisable(false);
					}
				}
			}
		});
	}
	
	private void refreshProducedWorkTableView() {
		List<ProducedWorkFX> producedWorksFX = new LinkedList<>(); 
		producedWorksFX.addAll(producedWorksTableView.getItems());
		producedWorksTableView.getItems().removeAll(producedWorksFX);
		producedWorksTableView.getItems().addAll(producedWorksFX);
		
		refreshMaterialConsumptionsTableView();
	}
	
	private void refreshMaterialConsumptionsTableView() {
		if(materialConsumptionsTableView != null) {
			materialConsumptionsTableView.getItems().clear();
			for(ProducedWorkFX prodWorkFX : producedWorksTableView.getItems()) {
				if(prodWorkFX.getMaterialConsumptionFX() != null) {
					materialConsumptionsTableView.getItems().addAll(
							prodWorkFX.getMaterialConsumptionFX());
				}
			}
		}
	}
	
	@Override
	public void addProducedWork(ProducedWorkFX producedWorkFX) {
		
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

	@Override
	public void setMaterialConsumptionTableView(
			TableView<MaterialConsumptionFX> tableView) {
		materialConsumptionsTableView = tableView;
		refreshMaterialConsumptionsTableView();
	}

	
}
