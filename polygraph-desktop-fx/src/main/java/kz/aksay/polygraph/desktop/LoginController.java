package kz.aksay.polygraph.desktop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.UserService;
import kz.aksay.polygraph.util.ContextUtils;
import kz.aksay.polygraph.util.SessionUtil;

public class LoginController {

	private ApplicationContext applicationContext = ContextUtils.getApplicationContext();
	
	private UserService userService;
	
	@FXML
	private TextField loginField;
	
	@FXML
	private PasswordField passwordField;
	
	@FXML
	private Label errorLabel;
	
	public LoginController() {
		userService = applicationContext.getBean(UserService.class);
	}
	
	@FXML
	public void login(ActionEvent actionEvent) throws IOException {
		
		
		User user = userService.findByLoginAndPassword(
				loginField.getText(), passwordField.getText());
		
		if(user == null) {
			errorLabel.setText("Вы ввели неправильные логин и пароль");
		}
		else {
			Map<String, Object> session = new HashMap<>();
			session.put(SessionUtil.USER_KEY, user);
			
			Node node = SessionUtil.loadFxmlNodeWithSession(
					getClass(), "main_menu.fxml", session, null);
			
			if(node instanceof Parent) {
				Scene scene = new Scene((Parent)node);
				StartingPane.getPrimaryStage().setScene(scene);
			}
		}
	}
}
