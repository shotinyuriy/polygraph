package kz.aksay.polygraph.desktop;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IOrderService;
import kz.aksay.polygraph.desktop.reports.PrintFacade;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.entityfx.OrderFX;
import kz.aksay.polygraph.entityfx.ProducedWorkFX;
import kz.aksay.polygraph.entityfx.StateFX;
import kz.aksay.polygraph.exception.InternalLogicException;
import kz.aksay.polygraph.integration1c.OrderToXMLExporter;
import kz.aksay.polygraph.service.OrderService;
import kz.aksay.polygraph.util.FormatUtil;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.ParameterKeys;
import kz.aksay.polygraph.util.ParametersAware;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class OrderTableViewController implements Initializable,
		SessionAware {

	private IOrderService orderService = StartingPane.getBean(IOrderService.class); 
	private IEmployeeService employeeService = StartingPane.getBean(IEmployeeService.class);
	
	private Map<String, Object> session;
	
	@FXML private TableView<OrderFX> ordersTableView;
	@FXML private TextField searchField; 
	@FXML private ComboBox<StateFX> stateCombo;
	@FXML private ComboBox<EmployeeFX> executorCombo;
	@FXML private CheckBox onlyMy;
	@FXML private Label errorLabel;
	@FXML private DatePicker dateFromPicker;
	@FXML private DatePicker dateToPicker;
	@FXML private Button printButton;
	@FXML private Button exportButton;
	
	private List<Order> orders;
	private MainMenu mainMenu;
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		mainMenu = SessionUtil.retrieveMainMenu(session);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		orders = orderService.findAll();
		List<OrderFX> ordersFX = OrderFX.convertListEntityToFX(orders);
		
		List<Employee> employees = employeeService.findAllByUserRole(User.Role.DESIGNER);
		
		onlyMy.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				findOrders();
			}
		});
		
		ordersTableView.getItems().addAll(ordersFX);
		
		stateCombo.getItems().addAll(StateFX.VALUES_PLUS_ALL);
		stateCombo.getSelectionModel().select(0);
		
		executorCombo.getItems().add(EmployeeFX.ALL_EMPLOYEES);
		executorCombo.getItems().addAll(EmployeeFX.contvertListEntityToFX(employees));
		
		setTableRowFactory();
	}
	
	@FXML
	public void findOrders() {
		Order orderExample = new Order();
		orderExample.setState(stateCombo.getSelectionModel().getSelectedItem().getState());
		
		if(onlyMy.isSelected()) {
			User user = SessionUtil.retrieveUser(session);
			if(user != null && user.getEmployee() != null) {
				orderExample.setCurrentExecutor(user.getEmployee());
			} else {
				orderExample.setCurrentExecutor(new Employee());
			}
		}
		
		if(orderExample.getCurrentExecutor() == null) { 
			if(executorCombo.getSelectionModel().getSelectedItem() == null)
				orderExample.setCurrentExecutor(null);
			else {
				orderExample.setCurrentExecutor(executorCombo.getSelectionModel().getSelectedItem().getEmployee());
			}
		}
		
		if(dateFromPicker.getValue() != null) {
			orderExample.setCreatedAt(FormatUtil.convertToDate(dateFromPicker.getValue()));
		}
		if(dateToPicker.getValue() != null) {
			orderExample.setUpdatedAt(FormatUtil.convertToDate(dateToPicker.getValue()));
		}
		
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
			
			mainMenu.loadFxmlAndOpenInTab(
					StartingPane.FXML_ROOT+"order_form.fxml", "Заказ №"+orderId, parameters);
		}
	}
	
	@FXML
	public void printOrders(final ActionEvent actionEvent) {
		
		mainMenu.setRightStatus("Формирование отчета");
		
		Task<Void> visibleProgressBar = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				updateProgress(0, 0);
				return null;
			}
			
			@Override
			protected void updateProgress(long workDone, long max) {
				printButton.setDisable(true);
				mainMenu.setProgressBarVisible(true);
			}
			
		};
		Thread visibleProgressBarThread = new Thread(visibleProgressBar);
		visibleProgressBarThread.start();
		
		final SwingNode jrViewerNode = new SwingNode();
		
		Task<Void> printOrdersTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				updateProgress(0, 0);
				return null;
			}
			
			@Override
			protected void updateProgress(long workDone, long max) {
				try {
					List<OrderFX> orders = new ArrayList<>(ordersTableView.getItems().size());
					
					for(OrderFX orderFX : ordersTableView.getItems()) {
						OrderFX newOrderFX = new OrderFX( orderService.find( orderFX.getOrder().getId() ) );
						orders.add(newOrderFX);
					}
					
					List<JasperPrint> jasperPrintList = PrintFacade.generateOrderDetails(orders);
					PrintFacade.embedJRViewerIntoSwingNode(jrViewerNode, jasperPrintList);
				} catch (JRException e) {
					e.printStackTrace();
				}
			};
		};
		
		printOrdersTask.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				printButton.setDisable(false);
				mainMenu.setProgressBarVisible(false);
				mainMenu.setRightStatus(null);
			}
			
		});
		
		printOrdersTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				printButton.setDisable(false);
				mainMenu.setProgressBarVisible(false);
				mainMenu.setRightStatus(null);
				
				StackPane stackPane = new StackPane();
				stackPane.getChildren().add(jrViewerNode);
				Stage stage = new Stage(); 
				stage.setScene(new Scene(stackPane));
				stage.setTitle("Предварительный просмотр");
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
				stage.show();
			}
			
		});
		
		Thread expportToXMLThread = new Thread(printOrdersTask);
		expportToXMLThread.start();
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
						OrderToXMLExporter exporter = StartingPane.getBean(OrderToXMLExporter.class);
						exporter.export(orders, file);
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
	
	private void setTableRowFactory() {
		
		final List<String> classes = new ArrayList<>(2);
		classes.add("orderInTimeRow");
		classes.add("orderLateRow");
		
		ordersTableView.setRowFactory(new Callback<TableView<OrderFX>, TableRow<OrderFX>>() {
	        @Override
	        public TableRow<OrderFX> call(TableView<OrderFX> tableView) {
//	        	final TableRow<OrderFX> tableRow = new TableRow<OrderFX>();
	            final TableRow<OrderFX> tableRow = new TableRow<OrderFX>() {
	                @Override
	                protected void updateItem(OrderFX orderFX, boolean empty){
	                	super.updateItem(orderFX, empty);
	                	getStyleClass().removeAll(classes);
	                	if(!empty && orderFX != null) {
		                    Boolean inTime = orderFX.isInTime();
		                    if (inTime != null) {
		                    	if(inTime) {
			                        if (! getStyleClass().contains("orderInTimeRow")) {
			                            getStyleClass().add("orderInTimeRow");
			                        }
		                    	} else {
		                    		if (! getStyleClass().contains("orderLateRow")) {
			                            getStyleClass().add("orderLateRow");
			                        }
		                    	}
		                    }
		                } else {
		                	setText(null);
		                	setGraphic(null);
		                }
	                }
	            };
	            
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
				
				ordersTableView.getItems().addListener(new ListChangeListener<OrderFX>() {
	                @Override
	                public void onChanged(Change<? extends OrderFX> change) {
	                    tableRow.getStyleClass().removeAll(classes);
	                    if( tableRow.getItem() != null ) {
	                    	Boolean inTime = tableRow.getItem().isInTime();
		                    if (inTime != null) {
		                    	if(inTime) {
			                        if (! tableRow.getStyleClass().contains("orderInTimeRow")) {
			                        	tableRow.getStyleClass().add("orderInTimeRow");
			                        }
		                    	} else {
		                    		if (! tableRow.getStyleClass().contains("orderLateRow")) {
		                    			tableRow.getStyleClass().add("orderLateRow");
			                        }
		                    	}
		                    }
	                    }
	                }
	            });
	            return tableRow;
	        }
	    });
	}
}
