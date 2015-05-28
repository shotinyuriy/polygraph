package kz.aksay.polygraph.desktop;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;
import kz.aksay.polygraph.entityfx.StateFX;
import kz.aksay.polygraph.util.FormatUtil;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class OrderConsumptionTableViewController implements Initializable,
		SessionAware {

	private IMaterialConsumptionService materialConsumptionService = 
			StartingPane.getBean(IMaterialConsumptionService.class);; 
	
	private Map<String, Object> session;
	
	@FXML TableView<MaterialConsumptionFX> orderConsumptionsTableView; 
	@FXML ComboBox<StateFX> stateCombo;
	@FXML DatePicker fromDate;
	@FXML DatePicker toDate;
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		List<MaterialConsumption> materialConsumptions= materialConsumptionService.findAll();
		Collection<MaterialConsumptionFX> ordersFX = MaterialConsumptionFX.convertListEntityToFX(materialConsumptions);
		
		orderConsumptionsTableView.getItems().addAll(ordersFX);
		
		stateCombo.getItems().addAll(StateFX.VALUES);
		stateCombo.getSelectionModel().select(0);
		fromDate.setValue(LocalDate.now().minusMonths(1));
		toDate.setValue(LocalDate.now());
		
		orderConsumptionsTableView.setRowFactory(new Callback<TableView<MaterialConsumptionFX>, TableRow<MaterialConsumptionFX>>() {
			
			@Override
			public TableRow<MaterialConsumptionFX> call(TableView<MaterialConsumptionFX> param) {
				// TODO Auto-generated method stub
				TableRow<MaterialConsumptionFX> tableRow = new TableRow<>();
				tableRow.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						int clickCount = event.getClickCount();
						if(clickCount == 2) {
							openOrderForm(new ActionEvent(event.getSource(), 
								event.getTarget()));
						}
					}
					
				});
				
				return tableRow;
			}
		});
	}
	
	@FXML
	public void findMaterialConsumptions() {
		MaterialConsumption materialConsumptionExample = new MaterialConsumption();
		Order orderExample = new Order();
		
		orderExample.setState(stateCombo.getSelectionModel().getSelectedItem().getState());
		
		List<MaterialConsumption> materialConsumptions = null;
		
		if(fromDate.getValue() != null) {
			orderExample.setCreatedAt(
					FormatUtil.convertToDate(fromDate.getValue()));
		}
		
		if(toDate.getValue() != null) {
			orderExample.setUpdatedAt(FormatUtil.convertToDate(toDate.getValue()));
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
						StartingPane.FXML_ROOT+"order_form.fxml", "Заказ №"+orderId, parameters);
			}
		}
	}
}
