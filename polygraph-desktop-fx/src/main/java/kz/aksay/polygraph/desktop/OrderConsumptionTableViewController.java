package kz.aksay.polygraph.desktop;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import kz.aksay.polygraph.api.IMaterialConsumptionService;
import kz.aksay.polygraph.entity.MaterialConsumption;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entityfx.EntityFX;
import kz.aksay.polygraph.entityfx.MaterialConsumptionFX;
import kz.aksay.polygraph.entityfx.StateFX;
import kz.aksay.polygraph.exception.InternalLogicException;
import kz.aksay.polygraph.integration1c.MaterialConsumptionToXMLExporter;
import kz.aksay.polygraph.integration1c.OrderToXMLExporter;
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
	@FXML private Button exportButton;
	@FXML private Label errorLabel;

	private MainMenu mainMenu;
	private List<MaterialConsumption> materialConsumptions;
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		mainMenu = SessionUtil.retrieveMainMenu(session);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		materialConsumptions= materialConsumptionService.findAll();
		List<MaterialConsumptionFX> materialConsumptionsFX = EntityFX.convertListEntityToFX(
				materialConsumptions, MaterialConsumptionFX.class);
		
		orderConsumptionsTableView.getItems().addAll(materialConsumptionsFX);
		
		stateCombo.getItems().addAll(StateFX.VALUES);
		stateCombo.getSelectionModel().select(0);
		fromDate.setValue(LocalDate.now().minusMonths(1));
		toDate.setValue(LocalDate.now());
		
		orderConsumptionsTableView.setRowFactory(new Callback<TableView<MaterialConsumptionFX>, TableRow<MaterialConsumptionFX>>() {
			
			@Override
			public TableRow<MaterialConsumptionFX> call(TableView<MaterialConsumptionFX> param) {
				
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
		
		materialConsumptions = null;
		
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
			List<MaterialConsumptionFX> ordersFX = EntityFX.convertListEntityToFX(
					materialConsumptions, MaterialConsumptionFX.class);
			orderConsumptionsTableView.getItems().addAll(ordersFX);
		}
		
		
	}

	@FXML
	public void openOrderForm(ActionEvent actionEvent) {
		MaterialConsumptionFX materialConsumptionFX = orderConsumptionsTableView.getSelectionModel().getSelectedItem();
		if(materialConsumptionFX != null) {
			
			Order order = materialConsumptionFX.getEntity().getOrder();
		
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
	
	@FXML
	public void exportToXML(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Укажите файл для сохранения");
		final File file = fileChooser.showSaveDialog(StartingPane.getPrimaryStage());
		if(file != null) {
			
			mainMenu.setRightStatus("Выгрузка в XML "+file.getName());
			
			Task<Void> visibleProgressBar = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					updateProgress(0, 0);
					return null;
				}
				
				@Override
				protected void updateProgress(long workDone, long max) {
					exportButton.setDisable(true);
					
					mainMenu.setProgressBarVisible(true);
				}
				
			};
			
			Thread visibleProgressBarThread = new Thread(visibleProgressBar);
			visibleProgressBarThread.start();
			
			
			Task<Void> exportToXMLTask = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					
					updateProgress(0, 0);
					return null;
				}
				
				@Override
				protected void updateProgress(long workDone, long max) {
					try {
						MaterialConsumptionToXMLExporter exporter = StartingPane.getBean(MaterialConsumptionToXMLExporter.class);
						exporter.export(materialConsumptions, file);
					} catch (InternalLogicException e) {
						exportButton.setDisable(true);
						errorLabel.setText(e.getLocalizedMessage());
					}
				};
			};
			
			exportToXMLTask.setOnFailed(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					exportButton.setDisable(false);
					mainMenu.setProgressBarVisible(false);
					mainMenu.setRightStatus(null);
				}
				
			});
			
			exportToXMLTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					exportButton.setDisable(false);
					mainMenu.setProgressBarVisible(false);
					mainMenu.setRightStatus(null);
				}
				
			});
			
			Thread expportToXMLThread = new Thread(exportToXMLTask);
			expportToXMLThread.start();
		}
	}
}
