package kz.aksay.polygraph.desktop;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import kz.aksay.polygraph.api.ICustomerService;
import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IOrderService;
import kz.aksay.polygraph.desktop.fxml.packageInfo;
import kz.aksay.polygraph.desktop.reports.PrintFacade;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.entity.Subject;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.entityfx.OrderFX;
import kz.aksay.polygraph.entityfx.ProducedWorkFX;
import kz.aksay.polygraph.entityfx.StateFX;
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
	
	private Map<String, Object> parameters;
	private Map<String, Object> session;
	
	private boolean isNewOrder;
	//private Order order;
	private OrderFX orderFX;
	private Subject customer;
	
	@FXML private GridPane orderContainer;
	@FXML private Label orderIdLabel;
	@FXML private Label customerIdLabel;
	@FXML private Label validationLabel;
	@FXML private Label totalCostLabel;
	@FXML private Label errorLabel;
	
	@FXML private Label customerField;
	@FXML private ComboBox<EmployeeFX> currentExecutorCombo;
	@FXML private TextArea descriptionField;
	@FXML private ComboBox<StateFX> currentStatusCombo;
	@FXML private DatePicker dateEndPlan;
	
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
			
			if(dateEndPlan.getValue() != null) {
				orderFX.getOrder().setDateEndPlan(
						FormatUtil.convertLocalDate(dateEndPlan.getValue())
					);
			} else {
				orderFX.getOrder().setDateEndPlan(null);
			}
			
			orderService.save(orderFX.getOrder());
			
			validationLabel.setText("Сохранение успешно");
			
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
		File file = fileChooser.showOpenDialog(StartingPane.getPrimaryStage());
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
				packageInfo.class, "produced_work_form.fxml", session, parameters);
		
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
				packageInfo.class, "produced_work_form.fxml", session, parameters);
		
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
				dateEndPlan.valueProperty().set(
						FormatUtil.convertFromLocalDate(orderFX.getDateEndPlanProperty().get()));
				currentExecutorCombo.getSelectionModel().select(orderFX.getCurrentExecutorFX());
				orderFX.getCurrentExecutorProperty().bind(currentExecutorCombo.getSelectionModel().selectedItemProperty());
				
				currentStatusCombo.getSelectionModel().select(new StateFX(orderFX.getStateProperty().get(), orderFX.getStateProperty().get().getName()));
				currentStatusCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StateFX>() {
					@Override
					public void changed(
							ObservableValue<? extends StateFX> observable,
							StateFX oldValue, StateFX newValue) {
						orderFX.getStateProperty().set(newValue.getState());
					}
				});
				
				
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
		Node node = SessionUtil.loadFxmlNodeWithSession(packageInfo.class, 
				"material_consumption_tableview.fxml", session, parameters);
		materialConsumptionPane.getChildren().add(node);
		currentStatusCombo.getItems().clear();
		currentStatusCombo.getItems().addAll(StateFX.VALUES);
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		List<Employee> employees = employeeService.findAll();
		Collection<EmployeeFX> employeesFX = EmployeeFX.contvertListEntityToFX(employees);
		
		currentExecutorCombo.getItems().setAll(employeesFX);
		
		producedWorksTableView.setRowFactory(new Callback<TableView<ProducedWorkFX>, TableRow<ProducedWorkFX>>() {
			
			@Override
			public TableRow<ProducedWorkFX> call(TableView<ProducedWorkFX> param) {
				// TODO Auto-generated method stub
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
