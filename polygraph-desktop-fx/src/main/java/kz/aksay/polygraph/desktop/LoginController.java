package kz.aksay.polygraph.desktop;

import static kz.aksay.polygraph.util.PropertiesUtils.DIALECT_KEY;
import static kz.aksay.polygraph.util.PropertiesUtils.DRIVER_KEY;
import static kz.aksay.polygraph.util.PropertiesUtils.PASSWORD_KEY;
import static kz.aksay.polygraph.util.PropertiesUtils.URL_KEY;
import static kz.aksay.polygraph.util.PropertiesUtils.USERNAME_KEY;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.desktop.fxml.packageInfo;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.util.PropertiesUtils;
import kz.aksay.polygraph.util.SessionUtil;

public class LoginController implements Initializable {

	private IUserService userService;
	
	@FXML private TextField loginField;
	@FXML private PasswordField passwordField;
	@FXML private Label errorLabel;
	@FXML private ComboBox<String> dbCombo;
	@FXML private TextField dialectField;
	@FXML private TextField driverField;
	@FXML private TextField urlField;
	@FXML private TextField dbUsernameField;
	@FXML private TextField dbPasswordField;
	
	private Properties properties;
	
	@FXML
	public void actionLogin(ActionEvent actionEvent) throws IOException {
		login();
	}
	
	
	@FXML
	public void keyLogin(KeyEvent keyEvent) throws IOException {
		if(keyEvent.getCode().equals(KeyCode.ENTER)) {
			login();
		}
	}
	
	public void login() throws IOException {
		
		writeDatabaseProperties();
		loadContext();
		
		userService = StartingPane.getBean(IUserService.class);
		User user = userService.findByLoginAndPassword(
				loginField.getText(), passwordField.getText());
		
		if(user == null) {
			errorLabel.setText("Вы ввели неправильные логин и пароль");
		}
		else {
			Map<String, Object> session = new HashMap<>();
			session.put(SessionUtil.USER_KEY, user);
			
			Node node = SessionUtil.loadFxmlNodeWithSession(
					packageInfo.class, StartingPane.FXML_ROOT+"main_menu.fxml", session, null);
			
			if(node instanceof Parent) {
				Scene scene = new Scene((Parent)node);
				StartingPane.getPrimaryStage().setScene(scene);
			}
		}
	}

	private void loadContext() {
		StartingPane.loadContext();
	}
	
	public void loadProperties(String fileName) {
		properties = PropertiesUtils.getCurrentDatabaseProperties(
				"META-INF/properties/"+fileName);
		dialectField.setText(properties.getProperty(DIALECT_KEY));
		driverField.setText(properties.getProperty(DRIVER_KEY));
		urlField.setText(properties.getProperty(URL_KEY));
		dbUsernameField.setText(properties.getProperty(USERNAME_KEY));
		dbPasswordField.setText(properties.getProperty(PASSWORD_KEY));
	}

	private void writeDatabaseProperties() {
		
		properties.setProperty(DIALECT_KEY, dialectField.getText());
		properties.setProperty(DRIVER_KEY, driverField.getText());
		properties.setProperty(URL_KEY, urlField.getText());
		properties.setProperty(USERNAME_KEY, dbUsernameField.getText());
		properties.setProperty(PASSWORD_KEY, dbPasswordField.getText());
		PropertiesUtils.writeDateBaseProperties(properties, "META-INF/properties/database");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbCombo.getItems().add("default-postgresql");
		dbCombo.getItems().add("default-hsql");
		
		dbCombo.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				
				loadProperties(newValue);
			}
			
		});
		
		dbCombo.getSelectionModel().select(0);
	}

}
