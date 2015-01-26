package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import kz.aksay.polygraph.desktop.controls.DateField;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;
import kz.aksay.polygraph.entityfx.StateFX;
import kz.aksay.polygraph.service.MaterialConsumptionService;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class OrderConsumptionTableViewController implements Initializable,
		SessionAware {

	private MaterialConsumptionService materialConsumptionService; 
	
	private Map<String, Object> session;
	
	@FXML TableView<MaterialConsumptionFX> orderConsumptionsTableView; 
	@FXML ComboBox<StateFX> stateCombo;
	@FXML DateField fromDate;
	@FXML DateField toDate;
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		materialConsumptionService = StartingPane.getBean(MaterialConsumptionService.class);
		
		List<MaterialConsumption> materialConsumptions= materialConsumptionService.findAll();
		Collection<MaterialConsumptionFX> ordersFX = MaterialConsumptionFX.convertListEntityToFX(materialConsumptions);
		
		orderConsumptionsTableView.getItems().addAll(ordersFX);
		
		stateCombo.getItems().addAll(StateFX.VALUES);
		stateCombo.getSelectionModel().select(0);
	}
	
	@FXML
	public void findMaterialConsumptions() {
		MaterialConsumption materialConsumptionExample = new MaterialConsumption();
		Order orderExample = new Order();
		
		orderExample.setState(stateCombo.getSelectionModel().getSelectedItem().getState());
		
		List<MaterialConsumption> materialConsumptions = null;
		
		if(fromDate.getDate() != null) {
			System.out.println("fromDate: "+fromDate.getDate());
			orderExample.setCreatedAt(fromDate.getDate());
		}
		
		if(toDate.getDate() != null) {
			System.out.println("toDate: "+toDate.getDate());
			orderExample.setUpdatedAt(toDate.getDate());
		}
		
		materialConsumptionExample.setOrder(orderExample);
		
		orderConsumptionsTableView.getItems().clear();
		
		materialConsumptions = materialConsumptionService.findByExample(
				materialConsumptionExample);
		
		if(materialConsumptions != null) {
			Collection<MaterialConsumptionFX> ordersFX = MaterialConsumptionFX.convertListEntityToFX(materialConsumptions);
			orderConsumptionsTableView.getItems().addAll(ordersFX);
		}
		
		
	}

	@FXML
	public void openOrderForm(ActionEvent actionEvent) {
		MaterialConsumptionFX materialConsumptionFX = orderConsumptionsTableView.getSelectionModel().getSelectedItem();
		if(materialConsumptionFX != null) {
			
			Order order = materialConsumptionFX.getMaterialConsumption().getOrder();
		
			if(order != null) {
				Long orderId = order.getId();
				Map<String, Object> parameters = new HashMap<>();
				parameters.put(ParameterKeys.ORDER_ID, orderId);
				
				MainMenu mainMenu = SessionUtil.retrieveMainMenu(session);
				mainMenu.loadFxmlAndOpenInTab(
						"order_form.fxml", "Заказ №"+orderId, parameters);
			}
		}
	}
}
