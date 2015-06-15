package kz.aksay.polygraph.desktop;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import kz.aksay.polygraph.desktop.fxml.packageInfo;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entityfx.EmployeeFX;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class MainMenuController implements MainMenu, SessionAware, Initializable {
	
	@FXML private VBox menuAccordion;
	@FXML private Pane contentPane;
	@FXML private TabPane tabPane;
	@FXML private TreeView<String> treeViewMenu;
	@FXML private Label leftStatus;
	@FXML private Label rightStatus;
	@FXML private ProgressBar progressBar;
	
	@FXML private TitledPane clientsMenu;
	@FXML private TitledPane ordersMenu;
	@FXML private TitledPane employeesMenu;
	@FXML private TitledPane dictionariesMenu;
	
	private Map<String, Object> session;
	
	@FXML
	public void generateAllEntities(ActionEvent actionEvent) {
		StartingPane.generateAllEntities();
	}
	
	@FXML
	public void openNewPersonForm(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"person_form.fxml", "Новый клиент физ.лицо");
	}
	
	@FXML
	public void openNewOrganizaitonForm(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"organization_form.fxml", "Новый клиент юр.лицо");
	}
	
	@FXML
	public void openPersonsList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"persons_list.fxml", "Список физ. лиц");
	}
	
	@FXML
	public void openOrganizationList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"organization_list.fxml", "Список юр. лиц");
	}
	
	@FXML
	public void openMaterialTypeList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"material_type_tableview.fxml", "Типы материалов");
	}
	
	@FXML
	public void openEmployeeTypeList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"employee_type_tableview.fxml", "Типы сотрудников");
	}
	
	@FXML
	public void openEquipmentTypeList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"equipment_tableview.fxml", "Типы оборудования");
	}
	
	@FXML
	public void openMaterialList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"paper_tableview.fxml", "Бумага");
	}
	
	@FXML
	public void openBindingSpringList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"binding_spring_tableview.fxml", "Пружины");
	}
	
	@FXML
	public void openStickersList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"sticker_tableview.fxml", "Наклейки");
	}
	
	@FXML
	public void openLaminateList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"laminate_tableview.fxml", "Ламинат. пленки");
	}
	
	@FXML
	public void openComplexityTypeList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"complexity_tableview.fxml", "Сложность");
	}
	
	@FXML
	public void openNewEmployeeForm(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"employee_form.fxml", "Новый сотрудник");
	}
	
	@FXML
	public void openEmployeeList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"employee_list.fxml", "Список сотрудников");
	}
	
	@FXML
	public void openEmployeeWorkload(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"employee_workload_view.fxml", "Загрузка сотрудников");
	}
	
	@FXML
	public void openOrderList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"order_list.fxml", "Список заказов");
	}
	
	@FXML
	public void openWorkTypeList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"work_type_tableview.fxml", "Виды работ");
	}
	
	@FXML
	public void openOrderConsumptionsList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"order_consumption_list.fxml", "Расходы по заказам");
	}
	
	@FXML
	public void openOrderProceedReport(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab(StartingPane.FXML_ROOT+"order_proceed_graph.fxml", "Среднее время выполнения заказа");
	}
	
	public Node loadFxmlNode(String url) {
		try {
			Node node = (Node)FXMLLoader.load(packageInfo.class.getResource(url));
			return node;
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void loadFxmlAndSetContent(String url) {
		Node node = loadFxmlNode(url);
		setContent(node);
	}
	
	@Override
	public void loadFxmlAndOpenInTab(String url, String tabName) {
		Node node = SessionUtil.loadFxmlNodeWithSession(
				packageInfo.class, url, session, null);
		openInTab(node, tabName);
	}
	
	@Override
	public void loadFxmlAndOpenInTab(String url, String tabName, 
			Map<String, Object> parameters) {
		Node node = SessionUtil.loadFxmlNodeWithSession(
				packageInfo.class, url, session, parameters);
		openInTab(node, tabName);
	}
	
	public void setContent(Node node) {
		contentPane.getChildren().clear();
		contentPane.getChildren().add(node);
	}
	
	public void openInTab(Node node, String tabName) {
		Tab tab = new Tab();
		tab.setContent(node);
		tab.setText(tabName);
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tab);
	}
	
	@FXML
	public void exit(ActionEvent actionEvent) {
		try {
			TabPane pane = (TabPane)FXMLLoader.load(packageInfo.class.getResource(
					StartingPane.FXML_ROOT+"fxml_login.fxml"));
			StartingPane.changeScene(pane);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		session.put(SessionUtil.MAIN_MENU_KEY, this);
		
		User user = SessionUtil.retrieveUser(session);
		if(user != null) {
			Employee employee = user.getEmployee();
			if(employee != null) {
				EmployeeFX employeeFX = new EmployeeFX(employee);
				setLeftStatus(employeeFX.toString());
			} else {
				setLeftStatus(user.getLogin());
			}
			User.Role role = user.getRole();
			switch(role) {
				case DESIGNER:
					employeesMenu.setVisible(false);
					dictionariesMenu.setVisible(false);
					employeesMenu.setManaged(false);
					dictionariesMenu.setManaged(false);
					break;
				case ACCOUNTANT:
					clientsMenu.setVisible(false);
					employeesMenu.setVisible(false);
					dictionariesMenu.setVisible(false);
					clientsMenu.setManaged(false);
					employeesMenu.setManaged(false);
					dictionariesMenu.setManaged(false);
					break;
				case DIRECTOR:
					ordersMenu.setVisible(false);
					employeesMenu.setVisible(false);
					ordersMenu.setManaged(false);
					employeesMenu.setManaged(false);
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setRightStatus(null);
		
		if(menuAccordion != null) {
		}
		
		
		if(treeViewMenu != null) {
			TreeItem<String> rootItem = new TreeItem<String>("Меню");
			treeViewMenu.setRoot(rootItem);
			
			TreeItem<String> clients = new TreeItem<String>("Клиенты");
			TreeItem<String> orders = new TreeItem<String>("Заказы");
			TreeItem<String> employees = new TreeItem<String>("Сотрудники");
			TreeItem<String> dictionaries = new TreeItem<String>("Справочники");
			
			TreeItem<String> persons = new TreeItem<String>("Физ. лица");
			TreeItem<String> organizations = new TreeItem<String>("Юр. Лица");
			clients.getChildren().add(persons);
			clients.getChildren().add(organizations);
			
			TreeItem<String> orderList = new TreeItem<String>("Список заказов");
			TreeItem<String> orderConsumptionList = new TreeItem<String>("Список расходов");
			orders.getChildren().add(orderList);
			orders.getChildren().add(orderConsumptionList);
			
			TreeItem<String> employeeList = new TreeItem<String>("Список сотрудников");
			TreeItem<String> employeeWorkload = new TreeItem<String>("Заруженность");
			employees.getChildren().add(employeeList);
			employees.getChildren().add(employeeWorkload);
			
			TreeItem<String> paperTypes = new TreeItem<String>("Типы бумаги");
			TreeItem<String> employeeRoles = new TreeItem<String>("Роли сотрудников");
			TreeItem<String> bindingSprings = new TreeItem<String>("Пружины");
			TreeItem<String> workTypes = new TreeItem<String>("Виды работ");
			TreeItem<String> equipment = new TreeItem<String>("Оборудование");
			
			dictionaries.getChildren().add(paperTypes);
			dictionaries.getChildren().add(employeeRoles);
			dictionaries.getChildren().add(bindingSprings);
			dictionaries.getChildren().add(workTypes);
			dictionaries.getChildren().add(equipment);
						
			rootItem.getChildren().add(clients);
			rootItem.getChildren().add(orders);
			rootItem.getChildren().add(employees);
			rootItem.getChildren().add(dictionaries);
		}
		
		
	}

	@Override
	public void setLeftStatus(String status) {
		leftStatus.setText(status);
	}

	@Override
	public void setRightStatus(String status) {
		rightStatus.setText(status);
	}

	@Override
	public void setProgressBarVisible(boolean visible) {
		progressBar.setVisible(visible);
	}
}
