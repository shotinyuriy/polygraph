package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.OrderFX;
import kz.aksay.polygraph.entityfx.StateFX;
import kz.aksay.polygraph.service.OrderService;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class OrderTableViewController implements Initializable,
		SessionAware {

	private OrderService orderService; 
	
	private Map<String, Object> session;
	
	@FXML TableView<OrderFX> ordersTableView;
	@FXML TextField searchField; 
	@FXML ComboBox<StateFX> stateCombo;
	@FXML CheckBox onlyMy;
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderService = StartingPane.getBean(OrderService.class);
		
		List<Order> orders = orderService.findAll();
		List<OrderFX> ordersFX = OrderFX.convertListEntityToFX(orders);
		
		ordersTableView.getItems().addAll(ordersFX);
		
		stateCombo.getItems().addAll(StateFX.VALUES);
		stateCombo.getSelectionModel().select(0);
	}
	
	@FXML
	public void findOrders() {
		Order orderExample = new Order();
		orderExample.setState(stateCombo.getSelectionModel().getSelectedItem().getState());
		System.out.println("onlyMy.isSelected(): "+onlyMy.isSelected());
		if(onlyMy.isSelected()) {
			User user = SessionUtil.retrieveUser(session);
			if(user != null && user.getEmployee() != null) {
				orderExample.setCurrentExecutor(user.getEmployee());
			} else {
				orderExample.setCurrentExecutor(new Employee());
			}
		}
		
		List<Order> orders = null;
		if(searchField.getText() != null && !searchField.getText().isEmpty()) {
			orders = orderService.findByExampleAndSearchString(
					orderExample, searchField.getText());
		} else {
			orders = orderService.findByExample(orderExample);
		}
		
		ordersTableView.getItems().clear();
		
		if(orders != null) {
			List<OrderFX> ordersFX = OrderFX.convertListEntityToFX(orders);
			ordersTableView.getItems().addAll(ordersFX);
		}
	}

	@FXML
	public void openOrderForm(ActionEvent actionEvent) {
		OrderFX orderFX = ordersTableView.getSelectionModel().getSelectedItem();
		if(orderFX != null) {
			Long orderId = orderFX.getOrder().getId();
			Map<String, Object> parameters = new HashMap<>();
			parameters.put(ParameterKeys.ORDER_ID, orderId);
			
			MainMenu mainMenu = SessionUtil.retrieveMainMenu(session);
			mainMenu.loadFxmlAndOpenInTab(
					"order_form.fxml", "Заказ №"+orderId, parameters);
		}
	}
}
