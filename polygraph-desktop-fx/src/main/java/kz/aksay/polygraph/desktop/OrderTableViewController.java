package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entityfx.OrderFX;
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
