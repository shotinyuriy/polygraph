package kz.aksay.polygraph.desktop;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javax.jws.Oneway;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import kz.aksay.polygraph.util.MainMenu;
import kz.aksay.polygraph.util.SessionAware;
import kz.aksay.polygraph.util.SessionUtil;

public class MainMenuController implements MainMenu, SessionAware {
	
	@FXML
	private Pane contentPane;
	
	@FXML
	private TabPane tabPane;
	
	private Map<String, Object> session;
	
	@FXML
	public void openNewPersonForm(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab("person_form.fxml", "Новый клиент физ.лицо");
	}
	
	@FXML
	public void openNewOrganizaitonForm(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab("organization_form.fxml", "Новый клиент юр.лицо");
	}
	
	@FXML
	public void openPersonsList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab("persons_list.fxml", "Список физ. лиц");
	}
	
	@FXML
	public void openOrganizationList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab("organization_list.fxml", "Список юр. лиц");
	}
	
	@FXML
	public void openMaterialTypeList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab("material_type_tableview.fxml", "Типы материалов");
	}
	
	@FXML
	public void openEmployeeTypeList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab("employee_type_tableview.fxml", "Типы сотрудников");
	}
	
	@FXML
	public void openMaterialList(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab("material_tableview.fxml", "Материалы");
	}
	
	@FXML
	public void openNewEmployeeForm(ActionEvent actionEvent) {
		loadFxmlAndOpenInTab("employee_form.fxml", "Новый сотрудник");
	}
	
	@FXML
	public void openEmployeeList() {
		loadFxmlAndOpenInTab("employee_list.fxml", "Список сотрудников");
	}
	
	public Node loadFxmlNode(String url) {
		try {
			Node node = (Node)FXMLLoader.load(getClass().getResource(url));
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
				getClass(), url, session, null);
		openInTab(node, tabName);
	}
	
	@Override
	public void loadFxmlAndOpenInTab(String url, String tabName, 
			Map<String, Object> parameters) {
		Node node = SessionUtil.loadFxmlNodeWithSession(
				getClass(), url, session, parameters);
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
			Pane pane = (Pane)FXMLLoader.load(getClass().getResource(
					"fxml_login.fxml"));
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
	}
}
