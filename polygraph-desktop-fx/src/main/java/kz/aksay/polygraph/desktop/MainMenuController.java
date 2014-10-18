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
import kz.aksay.polygraph.session.SessionAware;
import kz.aksay.polygraph.session.SessionUtil;

public class MainMenuController implements SessionAware {
	
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
		loadFxmlAndOpenInTab("organization_form.fxml", "Новая клиент юр.лицо");
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
	
	public void loadFxmlAndOpenInTab(String url, String tabName) {
		Node node = SessionUtil.loadFxmlNodeWithSession(
				getClass(), url, session, null);
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
		// TODO Auto-generated method stub
		
	}
}
