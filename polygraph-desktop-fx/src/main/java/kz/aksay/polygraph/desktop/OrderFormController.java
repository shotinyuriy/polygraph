package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import kz.aksay.polygraph.entity.Customer;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.entityfx.OrderFX;
import kz.aksay.polygraph.service.CustomerService;
import kz.aksay.polygraph.service.EmployeeService;
import kz.aksay.polygraph.service.OrderService;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.ParametersUtil;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class OrderFormController implements 
	Initializable, SessionAware, ParametersAware {
	
	private OrderService orderService;
	private EmployeeService employeeService;
	private CustomerService customerService;
	
	private Map<String, Object> parameters;
	private Map<String, Object> session;
	
	@FXML private Label orderIdLabel;
	@FXML private Label customerIdLabel;
	@FXML private Label validationLabel;
	
	@FXML private Label customerField;
	@FXML private ComboBox<EmployeeFX> currentExecutorCombo;
	@FXML private TextArea descriptionField;

	@FXML
	public void save(ActionEvent actionEvent) {
		try {
			String orderIdString = orderIdLabel.getText();
			String customerIdString = customerIdLabel.getText();
			Order order = null;
			Customer customer = null;
			
			if(orderIdString != null && !orderIdString.trim().isEmpty()) {
				Long orderId = Long.valueOf(orderIdString);
				if(orderId != null) {
					order = orderService.find(orderId);
				}
			}
			
			if(customerIdString != null && !customerIdString.trim().isEmpty()) {
				Long customerId = Long.valueOf(customerIdString);
				if(customerId != null) {
					customer = customerService.find(customerId);
				}
			}
			
			if(order == null) {
				order = new Order();
				order.setCreatedAt(new Date());
				order.setCreatedBy(SessionUtil.retrieveUser(session));
			}
			else {
				order.setUpdatedAt(new Date());
				order.setUpdatedBy(SessionUtil.retrieveUser(session));
			}
			
			order.setCustomer(customer);
			order.setDescription(descriptionField.getText());
			
			EmployeeFX employeeFX 
				= currentExecutorCombo.getSelectionModel().getSelectedItem();
			if(employeeFX != null) {
				order.setCurrentExecutor(employeeFX.getEmployee());
			}
			
			orderService.save(order);
			
			validationLabel.setText("Сохранение успешно");
			
		} catch (Exception e) {
			validationLabel.setText(e.getMessage());
		}
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
		initializeByParameters();
	}

	private void initializeByParameters() {
		if(parameters != null) {
			Long orderId = ParametersUtil.extractParameter(
					parameters, ParameterKeys.ORDER_ID, Long.class);
			if(orderId != null) {
				Order order = orderService.find(orderId);
				OrderFX orderFX = new OrderFX(order);
				orderIdLabel.setText(orderId+"");
				customerIdLabel.setText(orderFX.getCustomerId()+"");
				customerField.setText(orderFX.getCustomerFullName());
				descriptionField.setText(orderFX.getDescription());
				EmployeeFX employeeFX = orderFX.getCurrentExecutorFX();
				currentExecutorCombo.getSelectionModel().select(employeeFX);
			} else {
				Long customerId = ParametersUtil.extractParameter(
						parameters, ParameterKeys.CUSTOMER_ID, Long.class);
				Customer customer = customerService.find(customerId);
				if(customer != null) {
					customerIdLabel.setText(customer.getId()+"");
					customerField.setText(customer.getFullName());
				}
			}
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderService = StartingPane.getBean(OrderService.class);
		employeeService = StartingPane.getBean(EmployeeService.class);
		customerService = StartingPane.getBean(CustomerService.class);
		
		List<Employee> employees = employeeService.findAll();
		List<EmployeeFX> employeesFX = EmployeeFX.contvertListEntityToFX(employees);
		
		currentExecutorCombo.getItems().setAll(employeesFX);
	}

	
}
